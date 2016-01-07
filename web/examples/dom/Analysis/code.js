$(document).ready(function () {

var pos = " ",word,sy,ph,ss;
var maxWordLength = 12;

// initialize color palette
var hues = colorGradient();
//initialize lexicon
var lexicon = new RiLexicon(); 

generateEvent();
setInterval(generateEvent, 4000); // called every 4 sec by timer

function generateEvent() { 
  // random word with <= 12 letters
  do {
    word = lexicon.randomWord();
  } while (word.length > maxWordLength);
  
  // get various features
  sy = RiTa.getSyllables(word);
  ph = RiTa.getPhonemes(word);
  ss = RiTa.getStresses(word);
 
   // get (WordNet-style) pos-tags
  var tags = RiTa.getPosTags(word,true);
  pos = tagName(tags[0]);
  
  var phs = ph.split("-");

    $('#word').text(word);
    $('#pos').text(pos);

    refreshBubble(phs);
    addSyllables(sy); 
    setTimeout(drop,2000);

}

function refreshBubble(phs){
      $('.bubbles').children().each(function( i, val ){
      //reset the bubbles
      $(this).css({
                 'border-radius' : '20px',
                 'width' : '40px',
                 'height' : '40px',
                 'line-height' : '40px',
                 'margin-left':' 5px',
                 'margin-top':' 5px'
              });

    if(i<phs.length){//change the phs and color
      $(this).text(phs[i]);
      $(this).css("background-color","hsla(" + phonemeColor(phs[i]) + ", 90%, 45%, 0.6)");
      addStress(ss, sy);
    }
    else{//clear the useless bubbles
      $(this).text("");
      $(this).css("background-color","transparent");
    }

});
}

function drop() {
    $( '.bubbles' ).children().each(function(index) {        
        (function(that, i) { 
            var t = setTimeout(function() {  
              $(that).animate({ 
              'margin-top' : 150,
              }, "slow");
         }, 40 * i);
        })(this, index);
    });
}


function addSyllables(syllables)  {
   //Split each syllable
   var syllable = syllables.split("/");
   //record the past phonemes number
   var past = 0;
   
  for (var i = 0; i < syllable.length; i++) {
     var phs = syllable[i].split("-");
         for (var j = 1; j < phs.length; j++){
            (function(j){ 
              $( '.bubbles' ).children().eq(j+past).css("margin-left","-15px");
            })(j);
         }
      past += phs.length;
  }
}


function addStress(stresses,syllables,bubbles){

  //Split each stress
   var stress = stresses.split("/");
  //Split each syllable
   var syllable = syllables.split("/");
  // Count phonemes in each syllable
   var phslength = [syllable.length];
   //Record the previous phoneme count
   var past = 0;
   
  for (var i = 0; i < stress.length; i++) {

     var phs = syllable[i].split("-");

     // if the syllable is stressed, grow its bubbles
     if (parseInt(stress[i]) == 1) {
          for (var j = 0; j < phs.length; j++){
            (function(j){ 
             $( '.bubbles' ).children().eq(j+past).css({
                 'border-radius' : '25px',
                 'width' : '50px',
                 'height' : '50px',
                 'line-height' : '50px',
                 'margin-top' : '0px'
              });
            })(j);
         }       
     }
     past += phs.length;
  }
}

function tagName(tag) {

  if (tagsDict == null) {
    var tagsDict = {"n" : "Noun", "v" : "Verb", "r" : "Adverb","a":"Adjective"};
  }

  return tag == null ? null : tagsDict[tag];
}


function phonemeColor(phoneme) {

  var idx = RiTa.ALL_PHONES.indexOf(phoneme);
  return idx > -1 ? hues[idx] : 0;
}

function colorGradient() {
   var tmp = [];
   for (var i = 0; i < RiTa.ALL_PHONES.length; i++) {
    var h = Math.floor(map(i,0,RiTa.ALL_PHONES.length, .2*360, .8*360));
    tmp[i] = h;
  }
  return tmp;
}

function map(value, low1, high1, low2, high2) {
    return low2 + (high2 - low2) * (value - low1) / (high1 - low1);
}

});//jquery wrapper
