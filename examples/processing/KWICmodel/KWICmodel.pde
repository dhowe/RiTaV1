import rita.*;

HashMap args;
String word = "window";
String buttons[] = {
  "Gregor", 
  "Samsa", 
  "family", 
  "being", 
  "clerk", 
  "room", 
  "violin", 
  "window"
};

void setup() {
  size(600, 600);

  textFont(createFont("times", 18));

  args = new HashMap();
  args.put("ignorePunctuation", true);
  args.put("ignoreStopWords", true);
  args.put("wordCount", 6);

  updateKWIC();
}

void updateKWIC() {
  String kwic[] = RiTa.kwic(RiTa.loadString("../data/kafka.txt", this), word, args);

  background(255);

  // draw buttons
  fill(200, 0, 0);
  float posX = 50;
  for (int i = 0; i < buttons.length; i++) {
    text(buttons[i], posX, 40);
    posX += (textWidth(buttons[i]) + 15);
  }

  float tw = textWidth(word) / 2f;

  for (int i = 0; i < kwic.length; i++) {

    String[] parts = kwic[i].split(word);
    float x = width / 2f, y = i * 20 + 75;

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

// needs draw() for mouseClicked() to work
void draw() {
}

void mouseClicked() {

  float posX = 50;
  for (int i = 0; i < buttons.length; i++) {
    if (mouseX >= posX && mouseX <= posX + textWidth(buttons[i]) && mouseY >= 20 && mouseY <= 40) {
      word = buttons[i];
      updateKWIC();
      break;
    }
    posX += (textWidth(buttons[i]) + 15);
  }
}