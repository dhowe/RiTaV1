#!/usr/bin/env node

// External modules
var node_path = require('path');
var node_fs = require('fs');
var _ = require('lodash');
var iconv = require('iconv-lite');
var rimraf = require('rimraf');


var sync = module.exports = {};

// explode built-in fs methods to `fs-more`
// sync.__proto__ = node_fs;


// Process specified wildcard glob patterns or filenames against a
// callback, excluding and uniquing files in the result set.
function processPatterns(patterns, fn) {

    // Filepaths to return.
    var result = [];
    // Iterate over flattened patterns array.
    _.flatten(patterns).forEach(function(pattern) {
        // If the first character is ! it should be omitted
        var exclusion = pattern.indexOf('!') === 0;
        // If the pattern is an exclusion, remove the !
        if (exclusion) {
            pattern = pattern.slice(1);
        }
        // Find all matching files for this pattern.
        var matches = fn(pattern);

        if (exclusion) {
            // If an exclusion, remove matching files.
            result = _.difference(result, matches);
        } else {
            // Otherwise add matching files.
            result = _.union(result, matches);
        }
    });
    
    return result;
};
