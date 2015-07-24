var browserify = require('browserify');
var gulp = require('gulp');
var source = require('vinyl-source-stream');
var argv = require('yargs').argv;
var mocha = require('gulp-mocha');
var del = require('del');


var testDir = './test/',
  buildDir = 'dist',
  tmpDir = '/tmp';

gulp.task('clean', function(f) {
  del(buildDir, f);
});

gulp.task('build', function() {

  return browserify('./src/index.js', { standalone: 'rita' })
    .bundle() // to dist/bundle.js
    .pipe(source('bundle.js'))
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
          code: "dist/bundle.js",
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
