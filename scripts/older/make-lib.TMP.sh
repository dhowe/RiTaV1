#/bin/sh

set -e # die on errors 

if [ $# -lt "1"  ]
then
    echo
    echo "  error:   tag or version required"
    echo
    echo "  usage:   make-lib.sh [tag] [-p -D -M] "
    echo "           make-lib.sh 1.0.63a -p -D -M"
    echo
    echo "  options:"
    echo "       -p = publish-after-build"
    echo "       -b = update bower.json file"
    echo "       -D = dont-make-docs"
    echo "       -M = dont-minimize-lib"
    echo "       -Z = dont-make-zip"
    echo "       -F = do fake-minimize"
    echo "       -c = -DMZ (just combine js)" 
    exit
fi

VERSION=$1
MAKE_ZIP=1
DO_PUBLISH=0
INCLUDE_DOCS=1
UPDATE_BOWER=0
MINIMIZE_SRC=1
FAKE_MINIMIZE=0
LINE="------------------------------------------------------"

while [ $# -ge 1 ]; do
    #echo arg: $1
    case $1 in
        -D) INCLUDE_DOCS=0  ;;
    esac
    case $1 in
        -Z) MAKE_ZIP=0  ;;
    esac
    case $1 in
        -b) UPDATE_BOWER=1  ;;
    esac
    case $1 in
        -p) DO_PUBLISH=1  ;;
    esac
    case $1 in
        -M) MINIMIZE_SRC=0  ;;
    esac
    case $1 in
        -F) FAKE_MINIMIZE=1  ;;
    esac
    case $1 in
        -c) MINIMIZE_SRC=0  
            INCLUDE_DOCS=0
            MAKE_ZIP=0  
            ;;
    esac
    shift
done
#echo "DOCS=$INCLUDE_DOCS"
#echo "PUB=$DO_PUBLISH"
#echo "MIN=$MINIMIZE_SRC"

##############################################################

BUILD=../build
DOWNLOAD_INDEX=$BUILD/www/download/index.html
COMPILE="java -jar ../tools/compiler-latest/compiler.jar"

JSDOC=../tools/jsdoc-toolkit
SRC_VERSIONED=/tmp/rita-versioned.js
ALL_SRC="../src/rita_dict.js ../src/rita_lts.js ${SRC_VERSIONED} ../src/ritext.js"

WWW_DIR=../../www
RES_DIR=../../resources
LICENSE=$RES_DIR/license.txt
DOWNLOAD_DIR=$WWW_DIR/download
LIB_DIR=$WWW_DIR/js
EX_LIB_DIR=$WWW_DIR/examples/lib
DIST_DIR=../dist
REF_DIR=$WWW_DIR/reference
RITA_CODE=$DOWNLOAD_DIR/rita-$VERSION.js
RITA_CODE_MIN=$DOWNLOAD_DIR/rita-$VERSION.min.js
RITA_CODE_MICRO=$DOWNLOAD_DIR/rita-$VERSION.micro.js
BOWER_JSON=../resources/bower/bower.json

ZIP_TMP=/tmp/rita-$VERSION
ZIP_FILE=ritajs-full-$VERSION.zip

P5_LIB=~/Documents/Processing/libraries/RiTa/library/rita.js
LATEST=~/Documents/eclipse-workspace/RiTa/latest/rita.js

echo
echo "Building RiTaJS v$VERSION ------------------------------"
echo

###COMPILE-JS###################################################

echo Removing rita.js 
echo   from $P5_LIB
rm -f $P5_LIB
echo   from $LATEST
rm -f $LATEST
echo

echo Copying rita.js to $SRC_VERSIONED
cp ../src/rita.js $SRC_VERSIONED
sed -i "" "s/##version##/${VERSION}/g" $SRC_VERSIONED

if [ $UPDATE_BOWER = 1 ]
then
    echo "Updating bower version to ${VERSION}" 
    cp $BOWER_JSON ../bower.json
    sed -i "" "s/##version##/${VERSION}/g" ../bower.json
    #sed -i "" "s/##version##/${VERSION}/g" $SRC_VERSIONED
else
    echo "*** Not updating bower ***" 
fi

rm -f $RITA_CODE
echo "Combining src/*.js as ${RITA_CODE}" 
cat $ALL_SRC >> $RITA_CODE
echo

rm -f $RITA_CODE_MIN
rm -f $RITA_CODE_MICRO
if [ $MINIMIZE_SRC = 1 ]
then
    if [ $FAKE_MINIMIZE = 1 ]
    then
        FILE=/tmp/rita-$VERSION.min.js
        echo "Copying $FILE to ${RITA_CODE}"
        cp $FILE $RITA_CODE_MIN || exit !?
        cp ../src/rita.js $RITA_CODE_MICRO || exit !?
    else
        echo "Compiling rita-*.js as ${RITA_CODE_MIN}"; 
        $COMPILE --js  ${ALL_SRC} --js_output_file $RITA_CODE_MIN --summary_detail_level 3 --compilation_level SIMPLE_OPTIMIZATIONS  #ADVANCED_OPTIMIZATIONS
        echo "Compiling rita.js as ${RITA_CODE_MICRO}"; 
        $COMPILE --js  ../src/rita.js --js_output_file $RITA_CODE_MICRO --summary_detail_level 3 --compilation_level SIMPLE_OPTIMIZATIONS  #ADVANCED_OPTIMIZATIONS
    fi
else
    echo
    echo Skipping minimization
fi

###MAKE-DOCS#######################################################

if [ $INCLUDE_DOCS = 1 ]
then
    echo $LINE
    echo Building js-docs...
    #rm -rf $REF_DIR/*
    #java -Xmx512m -jar $JSDOC/jsrun.jar $JSDOC/app/run.js -d=$REF_DIR -a \
    #    -t=$JSDOC/templates/ritajs -D="status:alpha" -D="version:$VERSION" $SRC > docs-err.txt 
   ./make-docs.sh 
else
    echo $LINE
    echo Skipping docs...
fi


###EXAMPLES##########################################################

#echo
#echo Copying examples
#echo

###JS_FULL_ZIP#######################################################

#if [ $INCLUDE_DOCS = 1 ]
#then
#    echo Making complete zip 
#    rm -rf $ZIP_TMP
#    mkdir $ZIP_TMP
#    cd ../www
#    cp -r examples js download/*.js reference tutorial css $ZIP_TMP

#    ### make the zip
#    cd - 
#    cd $ZIP_TMP
#    jar cf $ZIP_FILE *
#    cd -
#    mv $ZIP_TMP/$ZIP_FILE $DOWNLOAD_DIR
#    rm -rf $ZIP_TMP
#    echo $LINE
#fi

###COPY->BUILD#######################################################

echo Copying into $BUILD 

rm -rf $BUILD
mkdir $BUILD
cp -r $WWW_DIR $BUILD
cp -r $LICENSE $BUILD/www/
sed -i "" "s/##version##/${VERSION}/g" $DOWNLOAD_INDEX 
#ls -l $BUILD

###COPY-P5_LIB#######################################################

####### 2 jars here? ################################################

############# STOPPING HERE #############
############# STOPPING HERE #############

#RITA_CODE=$DOWNLOAD_DIR/rita-$VERSION.js
#RITA_CODE_MIN=$DOWNLOAD_DIR/rita-$VERSION.min.js
#RITA_CODE_MICRO=$DOWNLOAD_DIR/rita-$VERSION.micro.js

############# STOPPING HERE #############
############# STOPPING HERE #############

echo $LINE
echo Copying $RITA_CODE 
echo " -> $P5_LIB"  # libraries dir
cp $RITA_CODE $P5_LIB

###COPY-RITA.LATEST##################################################
if [ $MINIMIZE_SRC = 1 ]
then
    echo " -> $LATEST" 
    cp $RITA_CODE_MIN $LATEST
    echo " -> $DIST_DIR"  

    # should remove rita-renderer from here
    cp $RITA_CODE_MIN $DIST_DIR/rita.js
    #less $RITA_CODE_MIN
fi

###COPY-www/js(for RiTaBanner#########################################
#echo " -> $LIB_DIR/"
#cp $RITA_CODE_MIN $LIB_DIR/rita.js
#echo $LINE

###COPY-www/examples/lib (for examples #########################################
echo " -> $EX_LIB_DIR/"
cp $RITA_CODE_MIN $EX_LIB_DIR/rita.js
echo $LINE

######################################################################

#ls ../build/www/js/

echo
echo Done [use pub-jslib.sh [-r] to publish]

if [ $DO_PUBLISH = 1 ]
then
./pub-jslib.sh $VERSION
fi

