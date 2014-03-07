#!/bin/sh
# publishes and links the specified version as RiTa-latest-unstable.zip

if [ $# != 1 ]
then
  echo
  echo "publish RiTa-XXX.zip to server//rita/download/RiTa-latest-unstable.zip" 
  echo "usage: pub-zip-only.sh [tag]"
  exit
fi

USR="dhowe"
ZIP_DIR="/Library/WebServer/Documents/rita/download/"
ZIP_PATH="../distribution/RiTa-$1/download"
ZIP_FILE="RiTa-$1.zip"
LINK="RiTa-latest-unstable.zip"


echo publishing $ZIP_PATH/$ZIP_FILE
echo         to $RED:$ZIP_DIR/RiTa-latest-unstable.zip

cat $ZIP_PATH/$ZIP_FILE | /usr/bin/ssh ${USR}@${RED} "(cd ${ZIP_DIR} && /bin/rm -f $ZIP_FILE && cat - > $ZIP_FILE && ln -fs $ZIP_FILE $LINK && ls -l)" 

# mv $ZIP_FILE zips
