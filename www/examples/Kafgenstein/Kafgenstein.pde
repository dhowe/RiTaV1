import rita.*;

RiMarkov markov;

void setup()
{    
  size(500, 500);
  
  RiText.defaultFontSize(18);

  new RiText(this, "click to (re)generate!");

  // create a markov model w' n=3
  markov = new RiMarkov(4);  
  
  // load files into the model
  markov.loadFrom(new String[] { "wittgenstein.txt", "kafka.txt" }, this);    
}

void draw()
{
  background(255);
  RiText.drawAll();
}

void mouseClicked() 
{   
  if (!markov.ready()) return; 
  
  RiText.disposeAll(); // clean-up old data

  String[] lines = markov.generateSentences(10);

  // lay out in rect (x=50 y=50, w=400, h=400)
  RiText.createLines(this, lines, 50, 50, 400, 400);
}
