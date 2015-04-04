import rita.*;

size(600, 600);
background(255);
RiText.defaults.alignment = CENTER;
println(RiText.defaults.alignment);
RiText.defaultFont("Times", 18);

String input = "My big black dog is hungry";

String result = RiTa.getPosTagsInline(input);
new RiText(this, result);

RiLexicon lexicon = new RiLexicon();
String test = "savage";
String[] s = lexicon.rhymes(test);
result =  test + ": ("+RiTa.asList(s)+")";
new RiText(this, result).y+= 30;

result = "No WordNet in JS";
/*if (RiTa.env() == RiTa.JAVA) {

  RiWordnet w = new RiWordnet("/WordNet-3.1");
  test = "night";
  s = w.getAntonyms(test, "n");
  result =  test + " is the opposite of "+s[0];
}*/
new RiText(this, result).y+= 100;


RiText.drawAll();
