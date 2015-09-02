var gulp = require('gulp'),
  jscs = require('gulp-jscs'),
  source = require('vinyl-source-stream'),
  argv = require('yargs').argv,
  del = require('del'),
  concat = require('gulp-concat'),
  uglify = require('gulp-uglify'),
  replace = require('gulp-replace'),
  jshint = require('gulp-jshint'),
  tasks = require('gulp-task-listing'),
  //var watch = require('gulp-watch'),
  pjson = require('./package.json'),
  version = pjson.version;

var testDir = './test/',
  buildDir = 'dist',
  tmpDir = '/tmp';

// list all the defined tasks
gulp.task('help', tasks);

gulp.task('clean', function(f) {
  del(buildDir, f)
});

gulp.task('lint', function() {
  var opts = {
    asi: 1,
    expr: 1,
    laxbreak: 1
  };
  return gulp.src('dist/rita.js')
    .pipe(jshint(opts))
    .pipe(jshint.reporter('default'));
})

gulp.task('watch', function() {
  gulp.watch('src/*.js', ['build']);
});

gulp.task('build', ['clean'], function() {

  gulp.src(['./src/header.js',
      './src/rita_lts.js',
      './src/rita_dict.js',
      './src/rita.js',
      './src/rita_lexicon.js',
      './src/footer.js' ])
    .pipe(replace('##version##', version))
    .pipe(concat('rita.js'))
    //.pipe(uglify())
    .pipe(gulp.dest(buildDir));
});

gulp.task('test', ['build'], function(cb) {

  var tests = [
    'test/LibStructure-tests',
    'test/RiTaEvent-tests',
    'test/RiString-tests',
    'test/RiTa-tests',
    'test/RiGrammar-tests',
    'test/RiMarkov-tests',
    'test/RiLexicon-tests',
    'test/UrlLoading-tests'
  ];

  if (argv.name) {

    tests = [testDir + argv.name + '-tests'];
    console.log('[INFO] Testing ' + tests[0]);
  }

  var testrunner = require("qunit");
  testrunner.setup({
    maxBlockDuration: 20000,
    log: {
      globalSummary: true,
      errors: true
    }
  });

  testrunner.run({
    code: "dist/rita.js",
    deps: [
      "src/rita_lts.js",
      "src/rita_dict.js",
      "test/qunit-helpers.js"
    ],
    tests: tests
  }, function(err, report) {
    if (err) console.error(err);
  });
});

// help is the default task
gulp.task('default', ['help']);

//console.log("Version: "+version);
