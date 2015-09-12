// NEXT:
// gulp tests
// gulp tests --nolex (a few tests still fail without lexicon)
// gulp test --name=RiString -nolex
//
// npm: gulp test
//
// npm: test & publish
//

/**
 * USAGE:
 * 	gulp (build | lint | watch | clean | help)   [--nolex]
 *  gulp test        # all
 *  gulp test --name RiString
 */

var useLex = true,
  minimize = false,
  sourceMaps = false;

var del = require('del'),
  gulp = require('gulp'),
  gulpif = require('gulp-if')
  argv = require('yargs').argv,
  concat = require('gulp-concat'),
  size = require('gulp-size'),
  uglify = require('gulp-uglify'),
  replace = require('gulp-replace'),
  jshint = require('gulp-jshint'),
  tasks = require('gulp-task-listing'),
  sourcemaps = require('gulp-sourcemaps'),
  pjson = require('./package.json'),
  rename = require('gulp-rename'),
  exec = require('child_process').exec,
  version = pjson.version;

var testDir = 'test',
  destDir = 'dist',
  nodeDir = destDir+'/node/rita',
  tmpDir = '/tmp',
  srcDir = 'src',
  output = 'rita',
  testFile = 'rita';

if (argv.nolex) { // don't include (or test) the lexicon
  useLex = false;
}

gulp.task('build-npm', ['setup-npm'], function(done) {
  exec('cd '+destDir+' && npm pack ../'+nodeDir, function (err, stdout, stderr) {
    log("Packing "+nodeDir+'/'+stdout);
    stderr && console.error(stderr);
    done(err);
  });
});

gulp.task('setup-npm', [ 'clean-node' ], function(done) {

  //console.log(getTests(),testFiles());
  // copy in the node readme
  gulp.src('../README.node.md')
    .pipe(rename('README.md'))
    .pipe(gulp.dest(nodeDir));

  // copy in other loose files
  gulp.src(['../LICENSE', './package.json', './gulpfile.js'])
    .pipe(gulp.dest(nodeDir));

  // copy in the tests
  gulp.src(testFiles())
    .pipe(gulp.dest(nodeDir + '/test'));

  // copy in the code
  gulp.src(destDir + '/rita-full.min.js')
    .pipe(rename('rita.js'))
    .pipe(gulp.dest(nodeDir + '/lib'));

  done();
});

// list all the defined tasks
gulp.task('help', tasks);

// clean out the build-dir
gulp.task('clean', function(f) { del(destDir, f); });

gulp.task('clean-node', function(f) { del(nodeDir, f); });

// run lint on the non-uglified output
gulp.task('lint', function() {

  var opts = {
    expr: 1,
    laxbreak: 1
  };

  log('Linting '+destDir+'/rita.js');

  return gulp.src(destDir+'/rita.js')
    .pipe(jshint(opts))
    .pipe(jshint.reporter('default'));
})

// watch the src-dir for changes, then build
gulp.task('watch', function() {

  log('Watching ' + srcDir + '/*.js');
  gulp.watch(srcDir + '/*.js', ['build']);
});

gulp.task('watch-quick', function() {

  log('Watching ' + srcDir + '/*.js');
  gulp.watch(srcDir + '/*.js', ['build-quick']);
});

// concatenate sources to 'dist' folder
gulp.task('build-quick-lex', ['clean'], function() {

  return gulp.src(sourceFiles(true))
    .pipe(replace('##version##', version))
    .pipe(concat(output+'-full.js'))
    .pipe(size({showFiles:true}))
    .pipe(gulp.dest(destDir));
});

gulp.task('build-quick-nolex', ['clean'], function() {

  return gulp.src(sourceFiles(false))
    .pipe(replace('##version##', version))
    .pipe(concat(output+'.js'))
    .pipe(size({showFiles:true}))
    .pipe(gulp.dest(destDir));
});

// concatenate/minify sources to 'dist' folder
gulp.task('build-minify-lex', ['clean'], function() {

  return gulp.src(sourceFiles(true))
    .pipe(replace('##version##', version))
    .pipe(gulpif(sourceMaps,sourcemaps.init()))
    .pipe(concat(output+'-full.min.js'))
    .pipe(uglify())
    .pipe(gulpif(sourceMaps,sourcemaps.write('./')))
    .pipe(size({showFiles:true}))
    .pipe(gulp.dest('dist'));
});

// concatenate/minify sources to 'dist' folder
gulp.task('build-minify-nolex', ['clean'], function() {

  return gulp.src(sourceFiles(false))
    .pipe(replace('##version##', version))
    .pipe(gulpif(sourceMaps,sourcemaps.init()))
    .pipe(concat(output+'.min.js'))
    .pipe(uglify())
    .pipe(gulpif(sourceMaps,sourcemaps.write('./')))
    .pipe(size({showFiles:true}))
    .pipe(gulp.dest('dist'));
});

// run tests: gulp test (all) || gulp test --name RiString
gulp.task('test', ['build-quick'], function() {
  return gulp.start('test-only');
});

// runs tests without building first
gulp.task('test-full', ['build-quick'], function (done) {
  testFile = 'rita-full';
  gulp.start('test-only');
  done();
});


// runs tests without building first
gulp.task('test-only', function (done) {

  var testrunner = require("qunit"),
    tests = testFiles();

  if (argv.name) {
    if (argv.name === 'RiLexicon')
      testFile = 'rita-full'
    tests = [ testDir + '/' + argv.name + '-tests.js' ];
    log('Testing: ' + tests[0]);
  }

  testrunner.setup({
    maxBlockDuration: 50000,
    coverage: true,
    log: {
      globalSummary: true,
      errors: true
    }
  });

  var testSrc = destDir + '/' + testFile + '.js';
  log('Source: ' + testSrc);

  testrunner.run({
      deps: [ testDir + '/qunit-helpers.js' ],
      code: testSrc,
      tests: tests
    },
    function (err, report) {
      if (err) {
        done(err);
        done(report);
      }
      testFile = 'rita'
      done();
    });
});

// Helper functions --------------------------------------

function testFiles(includeLex) {

  var tests = [
    testDir + '/LibStructure-tests.js',
    testDir + '/RiTaEvent-tests.js',
    testDir + '/RiString-tests.js',
    testDir + '/RiTa-tests.js',
    testDir + '/RiGrammar-tests.js',
    testDir + '/RiMarkov-tests.js',
    testDir + '/UrlLoading-tests.js',
    testDir + '/RiLexicon-tests.js'
  ];

  return tests;
}

function sourceFiles(includeLex) {

  var src = [ srcDir + '/header.js', srcDir + '/rita.js' ];

  if (includeLex) {
    src.push(srcDir + '/rita_lts.js');
    src.push(srcDir + '/rita_dict.js');
    src.push(srcDir + '/rilexicon.js');
  }

  src.push(srcDir + '/footer.js');
  
  return src;
}

function log(msg) { console.log('[INFO] '+ msg); }

// ----------------------------------------------------

// task composition hack
gulp.task('build', ['build-minify', 'build-quick']);
gulp.task('make-lib', ['build-quick','build-minify', 'build-npm']);
gulp.task('build-quick', ['build-quick-nolex','build-quick-lex']);
gulp.task('build-minify', ['build-minify-nolex','build-minify-lex']);

// help is the default task
gulp.task('default', ['help']);
