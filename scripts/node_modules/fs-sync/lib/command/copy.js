

// @param {Object} options {
//        force: {boolean} force to overridding
// }
sync.copy = function(file, dest, options) {
    if(!options){
        options = {};
    }

    // 'abc/' -> '/xxxx/xxx/abc'
    // 'abc.js' -> '/xxx/xxx/abc.js'
    file = node_path.resolve(file);

    if(sync.isFile(file)){
        var content = sync.read(file);

        if(options.force || !sync.exists(dest)){
            return sync.write(dest, content, options);
        }

        return false;

    }else if(sync.isDir(file)){

        var dir = file;
        dest = node_path.resolve(dest);

        sync.expand('**', {

            // to get relative paths to dir
            cwd: dir

        }).forEach(function(path) {
            var full_path = node_path.join(dir, path);

            if(sync.isFile(full_path)){
                sync.copy(full_path, node_path.join(dest, path), options);
            }
        });
    }
};