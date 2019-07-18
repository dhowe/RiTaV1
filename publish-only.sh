#!/bin/sh

# after making, only publish

set -e

BUILDPROPS=resources/build.properties
VERSION=`sed -n 's/^project.version=\(.*\)$/\1/p' $BUILDPROPS`

echo "Version: $VERSION"

read -p "Do you really want to publish this version? " -n 1 -r
echo
if [[ $REPLY =~ ^[Yy]$ ]]
then
      # pull from github and link rita.zip
      echo Updating remote server...
      ssh $RED "cd ~/git/RiTa && git stash && git pull && ./create-symlinks.sh ${VERSION}"
fi
