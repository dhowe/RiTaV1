FPATH=/Users/dhowe/Documents/eclipse-workspace/RiTa/large_files.txt

git filter-branch --tree-filter 'rm -rf `cat $FPATH | cut -d " " -f 2` ' --prune-empty master

echo done
