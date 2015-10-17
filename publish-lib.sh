#!/bin/sh

# first update version numbers:
# resources/build.properties && js/package.json)

set -e

BUILDPROPS=resources/build.properties
VERSION=`sed -n 's/^project.version=\(.*\)$/\1/p' $BUILDPROPS`
echo "Version: $VERSION"
WEB_ONLY=0

while [ $# -ge 1 ]; do
    #echo arg: $1
    case $1 in
        -w) 
          WEB_ONLY=1  
          echo "Web-only: true"
          ;;
    esac
    shift
done
echo
ssh $RED "cd ~/git/RiTa && git pull && ln -fs RiTa-${VERSION}.zip rita.zip && ls -l rita.zip web/download"  # update server
exit

ant -f resources/build.xml build.js
ant -f resources/build.xml build
if [ $WEB_ONLY = 1 ]
then
    echo "*** Updating web only (no tags or npm) ***" 
    git commit -am "Update to v$VERSION"
    git push
else
    echo "Updating tags and NPM to ${VERSION}"
    exit;
    ~/bin/git-tag.sh ${VERSION}
    cd js && ~/bin/git-tag.sh ${VERSION} && cd ..
    ant -f resources/build.xml npm.publish
fi
ssh $RED "cd ~/git/RiTa && git pull && ln -fs RiTa-${VERSION}.zip rita.zip && ls -l"  # update server
