var exists = node_fs.existsSync ?
    function (file) {
        return node_fs.existsSync(file);
    } :

    // if node -v <= 0.6, there's no fs.existsSync method.
    function (file) {
        try {
            node_fs.statSync(file);
            return true;
        } catch(e) {
            return false;
        }
    };


// @returns {boolean} true if the file path exists.
sync.exists = function() {
    var filepath = node_path.join.apply(node_path, arguments);
    return exists(filepath);
};


//@returns true if the file is a symbolic link.
sync.isLink = function() {
      var filepath = node_path.join.apply(node_path, arguments);
      return exists(filepath) && node_fs.lstatSync(filepath).isSymbolicLink();
};

// @returns {boolean} true if the path is a directory.
sync.isDir = function() {
      var filepath = node_path.join.apply(node_path, arguments);
      return exists(filepath) && node_fs.statSync(filepath).isDirectory();
};

// @returns {boolean} true if the path is a file.
sync.isFile = function() {
    var filepath = node_path.join.apply(node_path, arguments);
      return exists(filepath) && node_fs.statSync(filepath).isFile();
};

sync.isPathAbsolute = function() {
      var filepath = node_path.join.apply(node_path, arguments);
      return node_path.resolve(filepath) === filepath.replace(/[\/\\]+$/, '');
};