color getPhoColor(String phoneme) {
   int index=0;
   
  //an array of phoneme
String[] phonemes = {
    "aa","ae","ah","ao","aw","ax","axr","ay","b","ch","d","dh",
    "dx","eh","el","em","en","er","ey","f","g","hh","hv","ih",
    "iy","jh","k","l","m","n","nx","ng","ow","oy","p","r","s",
    "sh","t","th","uh","uw","v","w","y","z","zh","pau","h#","brth"
  };
  println(phonemes.length);
  //generate an array of colors
  color[] colors =new color[phonemes.length];
  for (int i=0; i<colors.length; i++)
    colors[i]=color(random(100)+100, random(100)+100, random(100)+100, 170);
    
  //find the index of current phoneme
  for (int i=0; i<phonemes.length; i++)  
  if (phoneme==phonemes[i]) index=i;
  
  //return the color
  return colors[index];
}