sync.defaultEncoding = 'utf-8';

sync.read = function(filepath, options) {
    if (!options) {
        options = {};
       }

    var contents;

    contents = node_fs.readFileSync(String(filepath));
    // If encoding is not explicitly null, convert from encoded buffer to a
    // string. If no encoding was specified, use the default.
    if (options.encoding !== null) {
        contents = iconv.decode(contents, options.encoding || sync.defaultEncoding);
        // Strip any BOM that might exist.
        if (contents.charCodeAt(0) === 0xFEFF) {
            contents = contents.substring(1);
        }
    }

    return contents;
};


sync.readJSON = function(filepath, options) {
    var src = sync.read(filepath, options);
      return JSON.parse(src);
};