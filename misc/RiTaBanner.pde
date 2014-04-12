import rita.*;

RiText rt;
int tid, idx;	
String s = "If, on the other hand, we";//  denounce with righteous indignation and dislike those who are so beguiled and demoralized by the";// charms of pleasure of the moment, so blinded by desire, that they cannot foresee the pain and trouble that are bound to ensue; and equal blame belongs to those who fail in their duty through weakness of will...";

void setup() 
{ 
  size(940, 78);

  RiText.defaultFill(255);
  RiText.defaultFont("courier", 12);
  
  rt = new RiText("", 10, 15);
  tid = RiTa.timer(.1);
}

void onRiTaEvent(re) { 
	
	  rt.text(s.substring(0, idx));
	  
	  background(0);
	  RiText.drawAll();
	  
	  if (idx++ == s.length) { // new line
	  	
	    println(tid=RiTa.pauseTimer(tid, .4));
	    rt = new RiText("", rt.x+10, rt.y+15);
	    idx = 0;
	    
	    if (rt.y > height/2) {
	      println("cancel!!!");
	      RiTa.stopTimer(tid);
	    }
	  }   
}


