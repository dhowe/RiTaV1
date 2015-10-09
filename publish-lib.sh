#!/bin/sh

# first update version numbers:
# resources/build.properties && js/package.json)

set -e

if [ $# != 1 ]
then
  echo
  echo "publish RiTa resources to npm and rednoise"
  echo
  echo "usage: publish-lib.sh [tag]"
  exit
fi

ant -f resources/build.xml build.js
ant -f resources/build.xml build
~/bin/git-tag.sh $1
cd js && ~/bin/git-tag.sh $1 && cd ..
ant -f resources/build.xml npm.publish
ssh $RED "cd ~/git/RiTa && git pull" 
