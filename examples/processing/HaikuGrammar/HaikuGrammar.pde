import rita.*;

RiGrammar grammar;
String[] lines = {"click to", "generate", "a haiku"};

void setup()
{
  size(650, 200);

  fill(0);
  textSize(30);
  textAlign(CENTER);

  grammar = new RiGrammar(this);
  grammar.loadFrom("haiku.yaml");
}

void draw()
{
  background(230, 240, 255);
  for (int i = 0; i < lines.length; i++)
    text(lines[i], width / 2, 75 + i * 35);
}

void mouseReleased()
{
  String result = grammar.expand();
  String[] haiku = result.split("%");
  for (int i = 0; i < lines.length; i++)
    lines[i] = haiku[i];
}