const fs = require('fs');
const RiTa = require('../src/rita');
const Dict = require('../src/rita_dict');
let count = 0;

function removeTag(toBeRemoved, word, tags) {
  if (tags.indexOf(toBeRemoved) > -1) {

    const stem = RiTa.stem(word);
    // if we can find the stem for the same verb & there is only vbx tag
    if (RiTa.hasWord(stem) && tags.length == 1) {
      count ++;
      delete Dict[word];
      //console.log("Remove", word, tags);
    } else {
      console.log(word, tags)
    }

  }
}

function saveToFile(dict, name) {

  let file = "module && (module.exports = " + JSON.stringify(dict) + ");";
  // formatting
  file = file.replace(/],/g, '],\n  ');
  file = file.replace(/'/g, '\\\'');
  file = file.replace(/"/g, '\'');

  fs.writeFile(name, file, err => {});
}

for (const word in Dict) {

  const tags = Dict[word][1].split(" ");
  //removeTag('vbz', word, tags);
  removeTag('vbg', word, tags);

}

// End of for loop
console.log("Total Removed:", count);
saveToFile(Dict, 'new_dict.js');
