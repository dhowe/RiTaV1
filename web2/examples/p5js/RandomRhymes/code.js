
var rhymes, word;

function setup()
{
  createCanvas(300, 300);
  fill(255);
  textFont("Georgia");

  lexicon = new RiLexicon();
  findRhymes();

  setInterval(findRhymes, 2000);
}

function draw()
{
  background(100,0,100);

  textAlign(RIGHT);
  textSize(36);
  text(word, 280, 40);

  textAlign(LEFT);
  textSize(14);
  textLeading(17);
  text(rhymes, 30, 73);
}

function findRhymes() { // called by timer

  var tmp = '';
  do {
    word = lexicon.randomWord();
    tmp = lexicon.rhymes(word);
  } while ( word && tmp.length < 3) 

  var arr = subset(tmp, 0, min(tmp.length, 13)); // max of 13 words
  rhymes = arr.join("\n");
}
