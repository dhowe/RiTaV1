git stash && git pull
rsync -avz --exclude 'download.php' --exclude '*.zip' --exclude 'RiTa-.*' --exclude 'dist' --exclude 'download' --exclude 'rita.*' web/ ../RiTa/web/
