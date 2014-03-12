#!/bin/sh

if [ $# != 1 ]
then
  echo
  echo "publish RiTa-XXX.zip to server//rita/download/" 
  echo "usage: pub-zip-only.sh [tag]"
  exit
fi

USR="dhowe"
ZIP_DIR="/Library/WebServer/Documents/rita/"
ZIP_PATH="../distribution/RiTa-$1/download"
ZIP_FILE="RiTa-$1.zip"
ZIP_TTS_FILE="RiTaTTS$1.zip"

#cat $ZIP_TTS_FILE | /usr/bin/ssh ${USR}@${RED} "(cd ${ZIP_DIR} && /bin/rm -f $ZIP_TTS_FILE && cat - > $ZIP_TTS_FILE && ln -fs $ZIP_TTS_FILE RiTaTTS.zip)" 

echo publishing $ZIP_PATH/$ZIP_FILE
echo         to $RED:$ZIP_DIR...

cat $ZIP_PATH/$ZIP_FILE | /usr/bin/ssh ${USR}@${RED} "(cd ${ZIP_DIR} && /bin/rm -f $ZIP_FILE && cat - > $ZIP_FILE && ln -fs $ZIP_FILE RiTa.zip && ls -l)" 

# mv $ZIP_FILE zips
