package rita.render.examples;

import java.util.HashMap;

import processing.core.PApplet;
import rita.RiTa;

public class KWIKmodel extends PApplet {

  String word = "door";
  String display[];
  int wordCount = 4;

  @Override
  public void setup() {
    size(600, 900);
    HashMap args = new HashMap();
    args.put("ignorePunctuation", true);
    args.put("ignoreStopWords", true);
    args.put("wordCount", 5);
    display = RiTa.kwic(RiTa.loadString("kafka.txt"), word, args);
  }

  public void draw() {

    background(255);
    fill(0);

    float tw = textWidth(word) / 2f;

    for (int i = 0; i < display.length; i++) {

      System.out.println(display[i]);
      String[] parts = display[i].split(word);
      float x = width / 2f, y = i * 20 + 25;

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
    noLoop();
  }
}
