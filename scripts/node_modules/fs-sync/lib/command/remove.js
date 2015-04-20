// Delete folders and files recursively
sync.remove = function(filepath) {
    filepath = String(filepath);

    if (!sync.exists(filepath)) {
        return false;
    }

    rimraf.sync(filepath);

    return true;
};