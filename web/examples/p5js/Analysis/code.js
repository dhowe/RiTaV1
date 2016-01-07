var pos = " ",word,sy,ph,ss;
var bubbles = [];
var maxWordLength = 12;
var colors = [];
var tagsDict;

function setup() {

  createCanvas(600, 300);
  smooth();
  noStroke();

  // load Lexicon
  lexicon = new RiLexicon(); 

  // initialize color palette
  colors = colorGradient();

  //initialize bubbles
  for (var i = 0; i < maxWordLength; i++){
    bubbles[i] = new Bubble("", 0);
  }

  //set the timer
  generateEvent();
  setInterval(generateEvent, 4000); 
}

function draw() {
 
  background(255);

  //The word
  fill(56,66,90);
  textAlign(LEFT);
  textSize(36);
  text(word, 80, 100);

  //pos Tag
  textSize(18);
  text(pos.toUpperCase(), 20, 30);
  
  for (var i = 0; i < bubbles.length; i++)
    bubbles[i].draw(i);
}

function generateEvent() { // called every 4 sec by timer

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

  //reset the bubbles
  for (var i = 0; i < bubbles.length; i++) {
    bubbles[i].reset();
  }

  //update the bubbles to the new word
  var phs = ph.split("-");
  for (var i = 0; i < phs.length; i++) {
    console.log(phs[i]);
    bubbles[i].update(phs[i], i*50+100,phonemeColor(phs[i]));
  }
  
  addStress(ss, sy, bubbles);
  addSyllables(sy, bubbles);  
}

function Bubble(phoneme,x) {

  this.r = 40;// radius of the circle
  this.ph = phoneme;//phoneme
  this.c;
  this.t = 0;//timer
  this.xpos = x;
  this.ypos = 150;
  this.gravity = 0.5;
  this.speed = 0;
  this.sz = 0;//speed of increasing radius

  this.reset=function() {
    this.ph = "";
    this.t = 0;
    this.sz = 0;
    this.speed = 0;
  }

  this.update=function(phoneme, x,col) {

    this.ph = phoneme;
    this.xpos = x;
    this.ypos = 150;
    this.r = 40;
    this.c = col;

  }

  //adjust Distance according to Syllabus
  this.adjustDistance = function(dis) {
   this.xpos += (this.r == 40) ? dis : 0.7 * dis;
  }

  //adjust the size of the circle
  this.grow = function() {
    this.r = 41;
    this.sz = 0.5;
  }

  this.draw = function(i) { 

    if (this.ph.length < 1) return;

    // draw the background circle
    fill(this.c);
    ellipse(this.xpos, this.ypos, this.r+this.sz, this.r+this.sz);

    // display the phoneme
    fill(255);
    textSize(18);
    textAlign(CENTER, CENTER);
    text(this.ph, this.xpos, this.ypos-1);

    if (this.sz < 10) this.sz *= 1.1;

    if (++this.t > 100 + 2 * i) {
      this.speed += this.gravity;
      this.ypos += this.speed;
    }
  }
}

function addSyllables(syllables,bubbles)  {

   //Split each syllable
   var syllable = syllables.split("/");

   //record the past phonemes number
   var past = 0;
   
    for (var i = 0; i < syllable.length; i++) {
     var phs=syllable[i].split("-");

      for (var j = 1; j < phs.length; j++)
        bubbles[past+j].adjustDistance(-20*j);

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
       for (var j = 0; j < phs.length; j++)
         bubbles[past+j].grow();
     }

     past += phs.length;
  }
}

function tagName(tag) {

  if (!tagsDict) {
    tagsDict = {"n" : "Noun", "v" : "Verb", "r" : "Adverb","a":"Adjective"};
  }

  return tag == null ? null : tagsDict[tag];
}


function phonemeColor(phoneme) {

  var idx = RiTa.ALL_PHONES.indexOf(phoneme);
  console.log(idx,RiTa.ALL_PHONES.length);
  return idx > -1 ? colors[idx] : 0;
}

function colorGradient() {

   colorMode(HSB, 1,1,1,1);
   var tmp = [];
  for (var i = 0; i < RiTa.ALL_PHONES.length; i++) {
    var h = map(i, 0, RiTa.ALL_PHONES.length, .2, .8);
    tmp[i] = color(h,.9,.9,.6);
  }
   colorMode(RGB,255,255,255,255);
  return tmp;
}
