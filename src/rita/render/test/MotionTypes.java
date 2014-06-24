package rita.render.test;

import processing.core.PApplet;
import rita.RiText;

public class MotionTypes extends PApplet
{

	/*
       An example demonstrating supported animation modes in RiTa,
       for use in RiText.moveTo() or RiText.moveBy() methods, etc.
       specified by calling RiText.setMotionType() with one of the
       the following constants:

            RiText.LINEAR                   (A)
            RiText.EASE_IN                  (B)
            RiText.EASE_OUT                 (C)
            RiText.EASE_IN_OUT              (D)
            RiText.EASE_IN_OUT_CUBIC        (E)
            RiText.EASE_IN_CUBIC            (F)
            RiText.EASE_OUT_CUBIC           (G)
            RiText.EASE_IN_OUT_QUARTIC      (H)
            RiText.EASE_IN_QUARTIC          (I)
            RiText.EASE_OUT_QUARTIC         (J)
            RiText.EASE_IN_OUT_SINE         (K)
            RiText.EASE_IN_SINE             (L)
            RiText.EASE_OUT_SINE            (M)
            RiText.EASE_IN_OUT_EXPO         (N)
            RiText.EASE_IN_EXPO             (O)
            RiText.EASE_OUT_EXPO            (P)
	 */

	boolean movingUp;
	RiText  rts[] = new RiText[16]; 

	int counter =0;
	boolean goBack = false;

	public void setup()
	{
		size(700, 700);
		smooth();


		RiText.defaultFont("Times",40);   
		//RiText.defaults.alignment=(LEFT);    


	}

	public void draw() 
	{
		background(255);
		RiText.drawAll();

		counter++;
		if(counter == 5 || counter == 305){
			goBack = true;
		}

		if(goBack){
			moveBack();
			goBack = false;
		}
	}

	public void moveBack()
	{
		movingUp = !movingUp;
		for (int i = 0; i < rts.length; i++)  {
			float xPos = 10+(width/(float)rts.length)*i;     
			if (rts[i] == null) {
				rts[i] = new RiText(this,(char)(i+65)+"", xPos, height); 
				rts[i].motionType(i);
			}
			rts[i].moveTo(xPos, (movingUp ? 30 : height), 3f);          
		}
	}

}
