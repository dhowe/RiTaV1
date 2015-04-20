// Are descendant path(s) contained within ancestor path? Note: does not test
// if paths actually exist.
sync.doesPathContain = function(ancestor) {
    ancestor = node_path.resolve(ancestor);
    
    var relative;
    var i = 1;

    for (; i < arguments.length; i ++) {
        relative = node_path.relative(node_path.resolve(arguments[i]), ancestor);
        if (relative === '' || /\w+/.test(relative)) {
            return false;
        }
    }

    return true;
};