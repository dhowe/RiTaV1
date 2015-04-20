
var glob = require('glob');

// Return an array of all file paths that match the given wildcard patterns.
sync.expand = function(patterns, options) {

    // Use the first argument if it's an Array, otherwise convert the arguments
    // object to an array and use that.
    patterns = Array.isArray(patterns) ? patterns : [patterns];

    return patterns.length === 0 ? [] :

        processPatterns(patterns, function(pattern) {

            // Find all matching files for this pattern.
            return glob.sync(pattern, options);
        });
};