cd ..

FPATH=/Users/dhowe/Documents/eclipse-workspace/RiTa/scripts/large_files.txt

git filter-branch -f --tree-filter 'rm -rf `cat $FPATH | cut -d " " -f 2` ' --prune-empty master

echo done
