class Bubble {
  float ypos, xpos;//position
  int r;// radius of the circle
  String ph;//phoneme
  color c;//color
  int t;//timer
  float gravity=0.5;
  float speed=0;
  float x;//speed of increasing radius

  Bubble (String phoneme, float x) {  
    xpos=x;
    ypos=150;
    r=40;
    ph=phoneme;
    t=0;
    x=0;
    c=color(random(150)+100, random(150)+100, random(150)+100, 170);
  } 

  void restart() {
    ph="";
    c=color(255, 255, 255);
    t=0;
    x=0;
    speed=0;
  }

  void update(String phoneme, float x) {
    ph=phoneme;
    xpos=x;
    ypos=150;
    r=40;
    c=color(random(150)+100, random(150)+100, random(150)+100, 170);
  }

  //adjusting Distance according to Syllabus
  void adjustDistance(int dis) {
    if (r==40)
      xpos+=dis;
    else
      xpos+=0.7*dis;
  }
  //adjusting the size of the circle
  void increaseR() {
    r=41;
    x=0.5;
  }

  void draw() { 
    //draw the background circle
    noStroke();
    fill(c);
    ellipse(xpos, ypos, r+x, r+x);
    //display the phoneme
    fill(255);
    textAlign(CENTER, CENTER);
    text(ph, xpos, ypos-5);
    t++;
    if (x<10)
      x+=00.1*x;
    fill(0);
  }

  void fall() {
    speed+=gravity;
    ypos+=speed;
  }
}