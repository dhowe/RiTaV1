/**
 * USAGE:
 * 	gulp [ build | lint | watch | clean | help ]   [--nolex]
 *  gulp test
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
  version = pjson.version;

var testDir = 'test',
  buildDir = 'dist',
  tmpDir = '/tmp',
  srcDir = 'src',
  output = 'rita';


if (argv.nolex) { // don't include (or test) the lexicon
  useLex = false;
}

// list all the defined tasks
gulp.task('help', tasks);

// clean out the build-dir
gulp.task('clean', function(f) { del(buildDir, f); });

// run lint on the non-uglified output
gulp.task('lint', function() {

  var opts = {
    expr: 1,
    laxbreak: 1
  };

  log('Linting '+buildDir+'/rita.js');

  return gulp.src(buildDir+'/rita.js')
    .pipe(jshint(opts))
    .pipe(jshint.reporter('default'));
})

// watch the src-dir for changes, then build
gulp.task('watch', function() {

  log('Watching ' + srcDir + '/*.js');

  gulp.watch(srcDir + '/*.js', ['build']);
});

gulp.task('build', [ 'build-minify', 'build-concat' ]);

// concatenate sources to 'dist' folder
gulp.task('build-concat', ['clean'], function() {

  return gulp.src(sourceFiles(useLex))
    .pipe(replace('##version##', version))
    .pipe(concat(output+'.js'))
    .pipe(size({showFiles:true}))
    .pipe(gulp.dest(buildDir));
});

// concatenate/minify sources to 'dist' folder
gulp.task('build-minify', ['clean'], function() {

  return gulp.src(sourceFiles(useLex))
    .pipe(replace('##version##', version))
    .pipe(gulpif(sourceMaps,sourcemaps.init()))
    .pipe(concat(output+'.js'))
    .pipe(rename(output+'.min.js'))
    .pipe(uglify())
    .pipe(gulpif(sourceMaps,sourcemaps.write('./')))
    .pipe(size({showFiles:true}))
    .pipe(gulp.dest('dist'));
});

gulp.task('make-lib', [], function(cb) {
  useLex = true;
  output = 'rita-full';
  gulp.start('build');
  useLex = false;
  output = 'rita';
  gulp.start('build');
  gulp.start('node');
});

gulp.task('node', [], function(cb) {
});

// run tests: gulp test (all) || gulp test --name RiString
gulp.task('test', ['build'], function(cb) {
  gulp.start('test-only');
})

gulp.task('test-only', function(cb) {

  var tests = [
    testDir + '/LibStructure-tests',
    testDir + '/RiTaEvent-tests',
    testDir + '/RiString-tests',
    testDir + '/RiTa-tests',
    testDir + '/RiGrammar-tests',
    testDir + '/RiMarkov-tests',
    testDir + '/UrlLoading-tests'
  ];

  if (useLex)
    tests.push(testDir + '/RiLexicon-tests');

  if (argv.name) {

    tests = [ testDir + '/' + argv.name + '-tests' ];
    log('Testing ' + tests[0]);
  }
  else {
    log('Testing ' + buildDir + '/rita.js');
  }

  var testrunner = require("qunit");

  testrunner.setup({
    maxBlockDuration: 50000,
    coverage: true,
    log: {
      globalSummary: true,
      errors: true
    }
  });

  testrunner.run({
    code: buildDir + '/rita.js',
    deps: [ testDir + '/qunit-helpers.js' ],
    tests: tests
  }, function(err, report) {
    if (err) {
      console.error(err);
      console.error(report);
    }
  });
});

function log(msg) { console.log('[INFO] '+ msg); }

function sourceFiles(includeLex) {

  var src = [ srcDir + '/header.js', srcDir + '/rita.js' ];

  if (includeLex) {
    src.push(srcDir + '/rita_lts.js');
    src.push(srcDir + '/rita_dict.js');
    src.push(srcDir + '/rilexicon.js');
  }
  else {
    log('Build: ignoring RiLexicon');
  }

  src.push(srcDir + '/footer.js');
  return src;
}

// help is the default task
gulp.task('default', ['help']);
