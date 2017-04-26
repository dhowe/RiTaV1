#!/bin/sh

# DO FIRST:
# 1. pull any changes from rita/rita.js
# 2. update version numbers: (git status should show 2 files changed:
#      resources/build.properties && js/package.json
# 3. run this script (it invokes ant to do the builds)

set -e

BUILDPROPS=resources/build.properties
VERSION=`sed -n 's/^project.version=\(.*\)$/\1/p' $BUILDPROPS`

#TODO: check RiTaJS/package.json and make sure versions match

echo "Version: $VERSION"
WEB_ONLY=0
DRY_RUN=0

#TODO: does WEB_ONLY option still make sense?

while [ $# -ge 1 ]; do
    #echo arg: $1
    case $1 in
        -w)
          WEB_ONLY=1
          echo "Web-only: true"
          ;;
        -d)
          DRY_RUN=1
          echo "Dry-run: no git or publish"
          ;;
    esac
    shift
done
echo

ant -f resources/build.xml build.js
ant -f resources/build.xml build

if [ $DRY_RUN = 1 ]
then
  echo
  echo "Exiting dry-run: no commits, tags or publish"
  echo
  exit
fi

git add -u                       # add all tracked files
git add web/RiTa-${VERSION}.zip  # add newly created zip file

if [ $WEB_ONLY = 1 ]
then
    echo "*** Updating web only (no tags or npm) ***"
    git commit -am "Update to v$VERSION"
    git push
else
    echo "Updating tags and NPM to ${VERSION}"
    ~/bin/git-tag.sh ${VERSION}
    cd js && ~/bin/git-tag.sh ${VERSION} && cd ..
    ant -f resources/build.xml npm.publish
fi

read -p "Do you really want to publish this version? " -n 1 -r
echo
if [[ $REPLY =~ ^[Yy]$ ]]
then
      # pull from github and link rita.zip
      echo Updating remote server...
      ssh $RED "cd ~/git/RiTa && git stash && git pull && ./create-symlinks.sh ${VERSION}"
fi
