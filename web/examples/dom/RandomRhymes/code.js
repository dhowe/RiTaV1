$(document).ready(function () {

//initialize lexicon
var lexicon = new RiLexicon();

var syns,word;
generateEvent();
setInterval(generateEvent, 2000); // called every 2 sec by timer

function generateEvent() { 

  var tmp = "";
  while (tmp.length < 3) {

    word = lexicon.randomWord();
    tmp = lexicon.rhymes(word);
  }    
  
  var arr = tmp.slice(0, Math.min(tmp.length, 13));
  syns = arr.join("<br>"); 
  
  $('#word').html(word);
  $('#rhyme').html(syns);
}


});//jquery wrapper
