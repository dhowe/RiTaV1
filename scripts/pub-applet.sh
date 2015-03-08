#!/bin/sh

# packages and publishes a sketch @ $SERVER/~www/sketches
# assumes the sketch is in $SKETCHBOOK, as defined below

if [ $# != 1 ]
then
  echo
  echo "sketch-name required!"
	echo
  echo  packages and publishes a sketch to rednoise/~www/sketches
  echo
  echo "usage: pub-applet.sh [sketch-name]"
  echo 
  echo "example: pub-applet.sh MySketch "
  exit
fi

TMP_DIR=/tmp
SERVER=${RED}
SKETCH_NAME=$1
SKETCHBOOK=~/Documents/Processing/$SKETCH_NAME/applet
#SKETCHBOOK=~/Documents/eclipse-workspace/$SKETCH_NAME/applet
ZIP_FILE=$SKETCH_NAME.zip
 
#cp ../rita-loading.gif applet/loading.gif

rm -rf $TMP_DIR/$SKETCH_NAME
cp -rf $SKETCHBOOK $TMP_DIR/$SKETCH_NAME

 
ls -l $TMP_DIR/$SKETCH_NAME

echo zipping: $ZIP_FILE

cd $TMP_DIR
jar cf $ZIP_FILE $SKETCH_NAME

ls -l $ZIP_FILE

jar tf $ZIP_FILE 

echo moving $ZIP_FILE

cat $ZIP_FILE | ssh ${USER}@${SERVER} "(cd /Library/WebServer/Documents/sketches; tar xf -; /bin/rm -rf $ZIP_FILE; chmod -R 775 $SKETCH_NAME; )"

echo cleaning up...

mv -f $SKETCH_NAME ~/.Trash
mv -f $ZIP_FILE ~/.Trash

cd -

echo Published to ${SERVER}/sketches/$SKETCH_NAME

exit
