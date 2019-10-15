var ALL_PHONES = ['aa','ae','ah','ao','aw','ay','b','ch','d','dh','eh','er','ey','f','g','hh','ih','iy','jh', 'k','l', 'm','n','ng','ow','oy','p','r','s','sh','t','th','uh', 'uw','v','w','y','z','zh'];

var tagsDict, word, sy, ph, ss;
var bubbles = [], colors = [];
var pos = '', maxWordLength = 12;

function setup() {

  createCanvas(600, 300);
  noStroke();

  // load Lexicon
  lexicon = new RiLexicon();

  // initialize color palette
  colors = colorGradient();

  // initialize bubbles
  for (var i = 0; i < maxWordLength; i++) {
    bubbles[i] = new Bubble('', 0);
  }

  selectWord();
  setInterval(selectWord, 4000);
}

function draw() {

  background(255);

  // word
  fill(56, 66, 90);
  textAlign(LEFT);
  textSize(36);
  text(word, 80, 50);


   //IPA
  textSize(18);
  ipaPhones = arpaToIPA(lexicon._getRawPhones(word));
  text("/" + ipaPhones + "/", 80, 75);
  // pos-tag
  textSize(14);
  textStyle(ITALIC);
  text(pos.toLowerCase(), 80, 105);

  for (var i = 0; i < bubbles.length; i++)
    bubbles[i].draw(i);
}

function selectWord() { // called every 4 sec by timer

  // random word with <= 12 letters
  do {
    word = lexicon.randomWord();
  } while (word && word.length > maxWordLength);

  // get various features
  sy = RiTa.getSyllables(word);
  ph = RiTa.getPhonemes(word);
  ss = RiTa.getStresses(word);

  // get (WordNet-style) pos-tags
  var tags = RiTa.getPosTags(word, true);
  pos = tagName(tags[0]);

  // reset the bubbles
  for (var i = 0; i < bubbles.length; i++) {
    bubbles[i].reset();
  }

  // update the bubbles for the new word
  var phs = ph.split('-');
  for (var i = 0; i < phs.length; i++) {
    bubbles[i].update(phs[i], i * 50 + 100, phonemeColor(phs[i]));
  }

  addStress(ss, sy, bubbles);
  addSyllables(sy, bubbles);
}

function Bubble(phoneme, x) {

  this.r = 40; // radius of the circle
  this.ph = phoneme; //phoneme
  this.t = 0; // timer
  this.xpos = x;
  this.ypos = 150;
  this.gravity = 0.5;
  this.speed = 0;
  this.sz = 0; // grow speed

  this.reset = function () {
    this.ph = '';
    this.t = 0;
    this.sz = 0;
    this.speed = 0;
  }

  this.update = function (phoneme, x, col) {
    this.ph = phoneme;
    this.xpos = x;
    this.ypos = 150;
    this.r = 40;
    this.c = col;
  }

  this.adjustDistance = function (dis) {
    this.xpos += (this.r == 40) ? dis : 0.7 * dis;
  }

  this.grow = function () {
    this.r = 41;
    this.sz = 0.5;
  }

  this.draw = function (i) {

    if (this.ph.length < 1) return;

    // draw background circle
    fill(this.c);
    ellipse(this.xpos, this.ypos, this.r + this.sz, this.r + this.sz);

    // display the phoneme
    fill(255);
    textSize(18);
    textAlign(CENTER, CENTER);
    text(this.ph, this.xpos, this.ypos - 1);

    if (this.sz < 10) this.sz *= 1.1;

    if (++this.t > 100 + 2 * i) {
      this.speed += this.gravity;
      this.ypos += this.speed;
    }
  }
}

function addSyllables(syllables, bubbles) {

  // Split each syllable
  var syllable = syllables.split('/');

  // Record the past phonemes number
  for (var i = 0, past = 0; i < syllable.length; i++) {
    var phs = syllable[i].split('-');

    for (var j = 1; j < phs.length; j++)
      bubbles[past + j].adjustDistance(-20 * j);

    past += phs.length;
  }
}

function addStress(stresses, syllables, bubbles) {

  // Split stresses & syllables
  var stress = stresses.split('/'),
    syllable = syllables.split('/');

  // Record the previous phoneme count
  for (var i = 0, past = 0; i < stress.length; i++) {

    var phs = syllable[i].split('-');

    // if the syllable is stressed, grow its bubbles
    if (parseInt(stress[i]) == 1) {
      for (var j = 0; j < phs.length; j++)
        bubbles[past + j].grow();
    }

    past += phs.length;
  }
}

function tagName(tag) {

  if (!tagsDict) {
    tagsDict = {
      'n': 'Noun',
      'v': 'Verb',
      'r': 'Adverb',
      'a': 'Adjective'
    };
  }

  return tag != null ? tagsDict[tag] : null;
}

function phonemeColor(phoneme) {

  var idx = ALL_PHONES.indexOf(phoneme);
  return idx > -1 ? colors[idx] : 0;
}

function colorGradient() {

  colorMode(HSB, 1, 1, 1, 1);
  var tmp = [], apl = ALL_PHONES.length;
  for (var i = 0; i < apl; i++) {
    var h = map(i, 0, apl, .2, .8);
    tmp[i] = color(h, .9, .9, .6);
  }
  colorMode(RGB, 255, 255, 255, 255);
  return tmp;
}
