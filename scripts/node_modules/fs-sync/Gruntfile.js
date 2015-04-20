"use strict";


module.exports = function( grunt ) {

    grunt.initConfig({
        build: {
            normal: {
                dest: 'dist/index.js'
            }
        }
    });

    grunt.registerMultiTask(
        'build',
        'build files',
        function() {
            var dest = this.data.dest;
            var compiled;

            var files = ['lib/index.js'].concat( grunt.file.expand('lib/command/*.js') );

            compiled = files.reduce(function(compiled, filepath) {
                return compiled + grunt.file.read( filepath ) + '\n\n';

            }, '');

            // Write concatenated source to file
            grunt.file.write( dest, compiled );

            // Fail task if errors were logged.
            if ( this.errorCount ) {
                return false;
            }

            // Otherwise, print a success message.
            grunt.log.writeln( 'File ' + dest + ' created.' );
        }
    );

    // grunt.loadNpmTasks('grunt-contrib-jshint');
    // grunt.loadNpmTasks("grunt-mocha");
    // grunt.loadNpmTasks('grunt-contrib-uglify');

    // grunt.registerTask('default', ['build', 'jshint', /* 'mocha' */, 'uglify']);
    grunt.registerTask('default', ['build']);

};