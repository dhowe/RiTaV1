// NEXT: deal with package.json in parent dir

var gulp = require('gulp'),
  sourcemaps = require('gulp-sourcemaps'),
  argv = require('yargs').argv,
  del = require('del'),
  concat = require('gulp-concat'),
  uglify = require('gulp-uglify'),
  replace = require('gulp-replace'),
  jshint = require('gulp-jshint'),
  tasks = require('gulp-task-listing'),
  //var watch = require('gulp-watch'),
  pjson = require('./package.json'),
  rename = require('gulp-rename'),
  version = pjson.version;

var testDir = 'test',
  buildDir = 'dist',
  tmpDir = '/tmp',
  srcDir = 'src',
  includeLex = false;

// list all the defined tasks
gulp.task('help', tasks);

// clean out the build-dir
gulp.task('clean', function(f) {

  del(buildDir, f)
});

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

// build to the 'dist' folder
gulp.task('build', ['clean'], function() {

  var result = gulp.src([
      srcDir + '/header.js',
      srcDir + '/rita.js',
      srcDir + '/rita_lexicon.js',
      srcDir + '/footer.js' ])
    .pipe(replace('##version##', version))
    //.pipe(sourcemaps.init())

    // complete lib concatenated
    .pipe(concat('rita.js'))
    .pipe(gulp.dest(buildDir))

    // complete lib raw minified
    .pipe(rename('rita.min.js'))
    .pipe(uglify())
    //.pipe(sourcemaps.write('./'))
    .pipe(gulp.dest('dist'));

    log('Writing [ rita-core.js, rita-core.min.js ] to '+buildDir);

    return result;
});

// build to the 'dist' folder
gulp.task('build-full', ['clean'], function() {

  gulp.src([
      srcDir + '/header.js',
      srcDir + '/rita_lts.js',
      srcDir + '/rita_dict.js',
      srcDir + '/rita.js',
      //srcDir + '/rita_lexicon.js',
      srcDir + '/footer.js' ])
    .pipe(replace('##version##', version))
    .pipe(sourcemaps.init())

    // complete lib concatenated
    .pipe(concat('rita-full.js'))
    .pipe(gulp.dest(buildDir))

    // complete lib raw minified
    .pipe(rename('rita-full.min.js'))
    .pipe(uglify())
    .pipe(sourcemaps.write('./'))
    .pipe(gulp.dest('dist'));

    log('Writing [ rita-full.js, rita-full.min.js ] to '+buildDir);
});


// run tests: gulp test (all) || gulp test --name RiString
gulp.task('test', ['build'], function(cb) {

  var tests = [
    testDir + '/LibStructure-tests',
    testDir + '/RiTaEvent-tests',
    testDir + '/RiString-tests',
    testDir + '/RiTa-tests',
    testDir + '/RiGrammar-tests',
    testDir + '/RiMarkov-tests',
    testDir + '/RiLexicon-tests',
    testDir + '/UrlLoading-tests'
  ];

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
      testing: true,
      errors: true
    }
  });

  testrunner.run({
    code: buildDir + '/rita.js',
    deps: [
      testDir + '/qunit-helpers.js'
    ],
    tests: tests

  }, function(err, report) {
    if (err) {
      console.error(err);
      console.error(report);
    }
  });
});

function log(msg) { console.log('[INFO] '+ msg); }

// help is the default task
gulp.task('default', ['help']);
