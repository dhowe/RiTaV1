import ddf.minim.*;
import rita.*;

String s = "This is a typewriter typing a line.";

int idx;
RiText rt;
RiSample key, bell;

void setup() 
{ 
  size(400,400);

  RiText.createDefaultFont("arial", 24);
  rt = new RiText(this, s.charAt(idx++), 20, 30);

  key = RiTa.loadSample(this, "key.wav");
  bell = RiTa.loadSample(this, "bell.wav");

  RiTa.setCallbackTimer(this, "keytimer", .1f);
}

void onRiTaEvent(RiTaEvent re) { 
  
  rt.setText(s.substring(0, idx)); // add a letter
  
  if (idx++ == s.length())       // start a new line
  {
    RiTa.pauseCallbackTimer(this, "keytimer", .5f); // pause
    rt = new RiText(this,"", 20, rt.y + 30);
    bell.play();
    idx = 0; // reset letter counter
  }   
  else if (!rt.getText().endsWith(" ")) 
    key.play();  // unless its a space  
}

void draw() {
  background(255);
}

