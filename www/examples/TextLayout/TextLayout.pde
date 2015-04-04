import rita.*;

String poem = "A huge lizard was discovered drinking out of the fountain today. It was not menacing anyone, it was just very thirsty. A small crowd gathered and whispered to one another, as though the lizard would understand them if they spoke in normal voices. The lizard seemed not even a little perturbed by their gathering. It drank and drank, its long forked tongue was like a red river hypnotizing the people, keeping them in a trance-like state. 'It's like a different town,' one of them whispered. 'Change is good,' the other one whispered back."; 

public void setup() 
{
  size(300, 350);    

  // title & author 
  new RiText(this, "New Blood - by James Tate", 40, 40);

  // Start at (40, 60) & break 'poem' into  
  // lines, each no more than 220 pix wide
  RiText.createLines(this, poem, 40, 60, 220, 320);
}

public void draw() {
  background(255);
  RiText.drawAll();
}
