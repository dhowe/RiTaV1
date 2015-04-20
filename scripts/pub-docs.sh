#!/bin/sh

# Apr 19, 2015
# need to do 'ant build' first

set -e

if [ $# != 1 ]
then
  echo
  echo "publish docs to SERVER:/rita/reference/" 
  echo
  echo "usage: pub-docs.sh [tag]"
  exit
fi

TMP_DIR="/tmp"
DST_DIR="/Library/WebServer/Documents/rita"
SRC_PATH="../dist"
REF_PATH="../dist/RiTa"
ZIP_FILE="docs-$1.zip"
SERVER="rednoise.org"

echo
echo creating reference zip: $ZIP_FILE
echo

cd $REF_PATH
jar cf $ZIP_FILE reference
jar tf $ZIP_FILE #| less

echo
echo created doc zip: $REF_PATH/$ZIP_FILE
echo

echo publishing $REF_PATH/$ZIP_FILE 
echo         to $RED:$DST_DIR/
echo

cat $ZIP_FILE | /usr/bin/ssh ${USER}@${SERVER} "(cd ${DST_DIR} && /bin/rm -f $ZIP_FILE && cat - > $ZIP_FILE && jar xf $ZIP_FILE && mv $ZIP_FILE archived_docs && ls -l reference)" 
