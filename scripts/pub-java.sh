#!/bin/sh

# assumes rita zip/jar have already been created in ../dist

if [ $# != 1 ]
then
  echo
  echo "publish RiTa-XXX.zip and rita-XXX.jar to server://rita/download/" 
  echo "  links to RiTa-latest.zip and rita-latest.jar"
  echo
  echo "usage: pub-java.sh [tag]"
  exit
fi

DST_DIR="/Library/WebServer/Documents/rita/download"
SRC_PATH="../dist"
ZIP_FILE="RiTa-$1.zip"
JAR_FILE="rita-$1.jar"

echo
echo publishing $SRC_PATH/$JAR_FILE
echo         to $RED:$DST_DIR/
echo

cat $SRC_PATH/$JAR_FILE | /usr/bin/ssh ${USER}@${RED} "(cd ${DST_DIR} && /bin/rm -f $JAR_FILE && cat - > $JAR_FILE && ln -fs $JAR_FILE rita-latest.jar)" 

echo publishing $SRC_PATH/$ZIP_FILE
echo         to $RED:$DST_DIR/
echo

cat $SRC_PATH/$ZIP_FILE | /usr/bin/ssh ${USER}@${RED} "(cd ${DST_DIR} && /bin/rm -f $ZIP_FILE && cat - > $ZIP_FILE && ln -fs $ZIP_FILE RiTa-latest.zip && ls -l *-latest.* *-$1.*)" 
