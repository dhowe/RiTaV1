import rita.*;

RiMarkov markov;
String line = "click to (re)generate!";
int x = 160, y = 240;

void setup()
{
  size(500, 500);

  fill(0);
  textFont(createFont("times", 16));

  // create a markov model w' n=3
  markov = new RiMarkov(4);

  // load files into the model
  markov.loadFrom(new String[] {
    "../data/wittgenstein.txt", "../data/kafka.txt" }, 
    this);
}

void draw()
{
  background(250);
  text(line, x, y, 400, 400);
}

void mouseClicked()
{
  if (!markov.ready()) return;

  x = y = 50;
  String[] lines = markov.generateSentences(10);
  line = RiTa.join(lines, " ");
}