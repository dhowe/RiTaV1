#!/bin/sh

if [ $# != 1 ]
then
  echo
  echo "publish docs to server://rita/reference/" 
  echo
  echo "usage: pub-refs.sh [tag]"
  exit
fi

TMP_DIR="/tmp"
DST_DIR="/Library/WebServer/Documents/rita"
SRC_PATH="../dist"
REF_PATH="../dist/RiTa"
ZIP_FILE="docs-$1.zip"

echo
echo creating reference zip: $ZIP_FILE
echo

cd $REF_PATH
jar cf $ZIP_FILE reference
#jar tf $ZIP_FILE

echo publishing $ZIP_FILE
echo         to $RED:$DST_DIR/
echo

cat $TMP_DIR/$ZIP_FILE | /usr/bin/ssh ${USER}@${RED} "(cd ${DST_DIR} && /bin/rm -f $ZIP_FILE && cat - > $ZIP_FILE && jar xf $ZIP_FILE && mv $ZIP_FILE archived_docs && ls -l reference)" 
