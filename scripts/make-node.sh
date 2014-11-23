#/bin/sh

set -e # die on errors 

NPM=/usr/local/bin/npm

if [ $# -lt "1"  ]
then
    echo
    echo "  error:   tag or version required"
    echo
    echo "  usage:   make-node.sh [tag] [-p] [-f]"
    echo "           make-node.sh 1.0.63a"
    echo
    echo "  options:"
    echo "       -p = npm-publish after build "
    echo "       -f = npm-publish --force "
    exit
fi


VERSION=$1
DO_FORCE=0
DO_PUBLISH=0
INCLUDE_DOCS=1

LINE="------------------------------------------------------"

while [ $# -ge 1 ]; do
    case $1 in
        -D) INCLUDE_DOCS=0  ;;
    esac
    case $1 in
        -p) DO_PUBLISH=1  ;;
    esac
    case $1 in
        -f) DO_FORCE=1  ;;
    esac
    shift
done

#echo "DOCS=$INCLUDE_DOCS"
#echo "PUB=$DO_PUBLISH"

##############################################################

BUILD=/tmp/build
TEST=../test
NODE_RES=..
NODE_DIR=$BUILD/node
NODE_RITA=$NODE_DIR/rita
NODE_LIB=$NODE_DIR/rita/lib
NODE_DOC=$NODE_DIR/rita/doc
NODE_TEST=$NODE_DIR/rita/test
DL_DIR=$BUILD/www/download
DOC_DIR=$BUILD/www/reference
PKG_JSON=$NODE_RITA/package.json
TARBALL=rita-$VERSION.tgz
LATEST=../../latest

echo
echo "Building [Node] RiTaJS v$VERSION -----------------------"
echo

###COMPILE-JS###################################################

echo Re-creating $NODE_LIB 
rm -rf $NODE_DIR
mkdir -p $NODE_LIB || die
#mkdir -p $NODE_DOC || die
#mkdir -p $NODE_TEST || die


echo Copying $NODE_RES/package.json to $PKG_JSON
cp $NODE_RES/package.json $PKG_JSON
sed -i "" "s/##version##/${VERSION}/g" $PKG_JSON
#cat $PKG_JSON


echo 1: Copying $NODE_RES/README.md to $NODE_RITA
cp $NODE_RES/README.md $NODE_RITA/

#echo 2: Copying $NODE_RES/test-runner.js to $NODE_TEST
#cp $NODE_RES/test-runner.js $NODE_TEST/

echo 3: Copying $TEST to $NODE_TEST
#cp -r $TEST/*.js $NODE_TEST/

echo 4: Copying $DOC_DIR/* to $NODE_DOC
#cp -r $DOC_DIR/* $NODE_DOC/

echo 5: Copying $DL_DIR/rita-$VERSION.min.js to $NODE_LIB
# cp $DL_DIR/rita-$VERSION.min.js $NODE_LIB/rita.js

#minimize everything, no ritext

if true ### hack for set -e
then
  find $NODE_DIR -name 'CVS' | xargs rm -r
fi


echo $LINE
echo Generating NPM tarball in $LATEST
echo "CMD: $NPM pack $NODE_RITA from: "

pwd
echo

$NPM pack $NODE_RITA

echo Generated tarball 
mv $TARBALL $LATEST/$TARBALL



if [ $DO_PUBLISH = 1 ]
then
    if [ $DO_FORCE = 1 ]
    then
        echo Calling npm publish --force... 
        $NPM publish --force $LATEST/$TARBALL
    else
        echo Calling npm publish... 
        $NPM publish $LATEST/$TARBALL
    fi
    echo Done
else
    echo Done [use [-p] [-f] to publish]
fi


ls -l $LATEST/$TARBALL
#tar -tvf $LATEST/$TARBALL
