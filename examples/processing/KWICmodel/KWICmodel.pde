import rita.*;

HashMap args;
float buttonX = 150;
String[] kwic, buttons = {
  "Gregor", "Samsa",  "family",  "being", 
  "clerk",  "room",  "violin",  "window"
};
String data, over, word = buttons[7];

void setup() {
  size(800, 500);

  textFont(createFont("times", 18));

  args = new HashMap();
  args.put("ignorePunctuation", true);
  args.put("ignoreStopWords", true);
  args.put("wordCount", 6);

  data = RiTa.loadString("kafka.txt", this);
}

void draw() {

  if (kwic == null)
    kwic = RiTa.kwic(data, word, args);
    
  background(255);
  drawButtons();

  float tw = textWidth(word) / 2f;

  for (int i = 0; i < kwic.length; i++) {

    String[] parts = kwic[i].split(word);
    float x = width / 2f, y = i * 20 + 75;

    if (y > height - 20) return;
    
    fill(0);
    textAlign(RIGHT);
    text(parts[0], x - tw, y);

    fill(200, 0, 0);
    textAlign(CENTER);
    text(word, x, y);

    fill(0);
    textAlign(LEFT);
    text(parts[1], x + tw, y);
  }
}

void drawButtons() {

  float posX = buttonX, posY = 40;
  for (int i = 0; i < buttons.length; i++) {
    stroke(200);
    boolean on = word.equals(buttons[i]);
    float tw = textWidth(buttons[i]);
    fill(!on && buttons[i].equals(over) ? 235 : 255);
    rect(posX-5, 44, tw+10, -20, 7);
    fill((on ? 255 : 0), 0, 0);
    text(buttons[i], posX, 40);
    posX += tw + 20;
  }
}

void mouseReleased() {

  float posX = buttonX, tw;
  for (int i = 0; i < buttons.length; i++) {
    tw = textWidth(buttons[i]);
    if (inside(mouseX, mouseY, posX, tw)) {
      word = buttons[i];
      kwic = null;
      break;
    }
    posX += tw + 20;
  }
}

void mouseMoved() {
  over = null;
  float posX = buttonX, tw;
  for (int i = 0; i < buttons.length; i++) {
    tw = textWidth(buttons[i]);
    if (inside(mouseX, mouseY, posX, tw)) {
      over = buttons[i];
      break;
    }
    posX += tw + 20;
  }
}

boolean inside(float mx, float my, float posX, float tw) {

  return (mx >= posX-5 && mx <= posX + tw+5 && my >= 25 && my <= 44);
}
 