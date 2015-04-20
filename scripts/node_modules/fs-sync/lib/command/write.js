// Write a file.
sync.write = function(filepath, contents, options) {
    if (!options) {
       options = {};
    }

    // Create path, if necessary.
    sync.mkdir(node_path.dirname(filepath));

    // If contents is already a Buffer, don't try to encode it. If no encoding
    // was specified, use the default.
    if (!Buffer.isBuffer(contents)) {
        contents = iconv.encode(contents, options.encoding || sync.defaultEncoding);
    }

    node_fs.writeFileSync(filepath, contents, options);

    return true;
};