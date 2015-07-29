//var browserify = require('browserify');
var gulp = require('gulp');
var jscs = require('gulp-jscs');
var source = require('vinyl-source-stream');
var argv = require('yargs').argv;
//var mocha = require('gulp-mocha');
var del = require('del');
var concat = require('gulp-concat');
var uglify = require('gulp-uglify');
var replace = require('gulp-replace');
var jshint = require('gulp-jshint');

//var watch = require('gulp-watch');
var pjson = require('./package.json'),
  version = pjson.version;

var testDir = './test/',
  buildDir = 'dist',
  tmpDir = '/tmp';

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
//      "src/rita_lancaster.js",
      "test/qunit-helpers.js"
    ],
    tests: tests
  }, function(err, report) {
    if (err) console.error(err);
  });
});

  //console.log("Version: "+version);
