class Bubble {

  int r; // radius of circle
  String ph; // phoneme
  color c; // color
  int t; // timer
  float ypos, xpos, gravity = 0.5, speed = 0, sz;

  Bubble() {
    this("", 0);
  }

  Bubble (String phone, float x) {
    xpos = x;
    ypos = 150;
    r = 40;
    ph = phone;
    t = 0;
    sz = 0;
    c = phonemeColor(ph);
  }

  void reset() {
    ph = "";
    t = 0;
    sz = 0;
    speed = 0;
  }

  void update(String phoneme, float x) {
    ph = phoneme;
    xpos = x;
    ypos = 150;
    r = 40;
    c = phonemeColor(ph);
  }

  // adjust distance according to syllable
  void adjustDistance(int dis) {

    xpos += (r == 40) ? dis : 0.7 * dis;
  }

  // adjust the size of the circle
  void grow() {

    r = 41;
    sz = 0.5;
  }

  void draw(int i) {

    if (ph.length() < 1) return;

    // draw the background circle
    fill(c);
    ellipse(xpos, ypos, r+sz, r+sz);

    // display the phoneme
    fill(255);
    textSize(18);
    textAlign(CENTER, CENTER);
    text(ph, xpos, ypos-5);

    if (sz < 10) sz *= 1.1;

    if (++t > 100 + 2 * i) {
      speed += gravity;
      ypos += speed;
    }
  }
}
