
var syns, word;

function setup() 
{
  createCanvas(300, 300);  
  fill(255);
  textFont("Georgia");
  textSize(36);
  
  lexicon = new RiLexicon();
  generateEvent();
  setInterval(generateEvent, 2000); 
}

function draw() 
{  
  background(40);

  textAlign(RIGHT);
  textSize(36);
  text(word, 280, 40);

  textSize(14);
  textLeading(17);
  textAlign(LEFT);
  text(syns, 30, 73);
}

function generateEvent() { // valled by timer

  var tmp="";
  while (tmp.length < 3) {

    word = lexicon.randomWord();
    tmp = lexicon.rhymes(word);
  }    
  
  text(word, 280, 40); // max of 13 words
  var arr = subset(tmp, 0, min(tmp.length, 13));
  syns = arr.join("\n"); 
}