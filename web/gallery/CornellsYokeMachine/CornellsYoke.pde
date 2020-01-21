import rita.*;

/* @pjs font="adler.ttf" */
/* @pjs preload="yokedtrees.jpg,kitchen.jpg,attic.jpg,bedroom.jpg,diner.jpg" */

RiText[] rts;
RiGrammar grammar;
boolean hasClicked, noSwap=true;
PImage images[], current;
int clicks=0;

String[] imgNames = { "yokedtrees.jpg","kitchen.jpg","attic.jpg","bedroom.jpg","diner.jpg" };

void setup() {
  
  size(1000, 600);

  images = new PImage[imgNames.length];
  for (int i = 0; i < imgNames.length; i++)
    images[i] = loadImage(imgNames[i]);
  
  // create the grammar 
  grammar = new RiGrammar();
  grammar.loadFrom("newgrammar.json", this);
  //grammar.openEditor(width, height); 

  RiText.defaultFont("adler.ttf", 36);
  if (RiTa.env() == RiTa.JAVA)
    RiText.defaultFont("Adler-36.vlw");
  
  RiText.defaultFill(193,0,0);
  RiText.defaults.alignment = CENTER;
  
  rts = new RiText[13];
  rts[0] = new RiText(this, "Cornell's Yoke Machine", width/2, 40);
  rts[1] = new RiText(this, "click to generate a room...", width/2, 70);

  RiText.defaults.alignment = LEFT;
  
  RiText.defaultFont("adler.ttf", 18);
  if (RiTa.env() == RiTa.JAVA)
    RiText.defaultFont("Adler-18.vlw");
    
  int[] pos = { 75,100,125,175,200,225,275,300,325,375,400};
  for (int i = 2; i < rts.length; i++)
   rts[i] = new RiText(this, "", 70, pos[i-2] + 70);

  current = images[0];
  RiTa.timer(this, .2);
}

void draw() {
  
  rect(0, 0, width, height);
  image(current, 0, 0, width,height);
  RiText.drawAll();
}

void mouseClicked() {
  
  // pick an image
  current = images[(int)random(0,images.length)];

  // generate the text
  String result = grammar.expand(false);
  String[] lines = result.split("%");
  for (int i = 0; i < rts.length; i++) 
  {
    rts[i].text(lines[i].trim());
    String linesFromGrammar = lines[i];
    for (int j=0; j < linesFromGrammar.length(); j++)
      noSwap = linesFromGrammar.startsWith("@");
  }
  hasClicked = true;

  if (++clicks > 1) {
    
    noSwap = true; // stop the flipping 
  }
  if (clicks == 3) { // if we click again, start the flipping 
  
    clicks = 1; //so we change the click back to as if it is 1-click
    hasClicked = true; 
    noSwap = false;
  }
}

void onRiTaEvent(RiTaEvent rt) {
  
  if (hasClicked && !noSwap) {
    
    String result = grammar.expand(false);
    String[] lines = result.split("%");
    for (int i = 2; i < rts.length; i++) 
      rts[i].text(lines[i].trim());
  }
}



