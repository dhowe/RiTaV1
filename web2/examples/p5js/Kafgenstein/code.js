var lines, markov, data1, data2, x = 160, y = 240;

function preload() {

  data1 = loadStrings('../../data/wittgenstein.txt');
  data2 = loadStrings('../../data/kafka.txt');
}

function setup() {

  createCanvas(500, 500);
  textFont('times', 16);
  textAlign(LEFT);

  lines = ["click to (re)generate!"];

  // create a markov model w' n=4
  markov = new RiMarkov(4);

  // load text into the model
  markov.loadText(data1.join(' '));
  markov.loadText(data2.join(' '));

  drawText();
}

function drawText() {

  background(250);
  text(lines.join(' '), x, y, 400, 400);
}

function mouseClicked() {

  x = y = 50;
  lines = markov.generateSentences(10);
  drawText();
}
