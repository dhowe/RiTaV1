#!/bin/sh 
set -e
cd web
ln -fs download.php download.html
ln -fs examples.php examples.html
ln -fs gallery.php gallery.html
ln -fs index.php index.html
ln -fs quickstart.php quickstart.html
if [ -z "$1" ] 
  then
    echo
else
    ln -fs RiTa-$1.zip rita.zip
    ln -fs RiTa-$1.zip RiTa.zip
    ls -l rita.zip
fi
