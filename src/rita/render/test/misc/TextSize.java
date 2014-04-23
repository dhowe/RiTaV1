package rita.render.test.misc;

import processing.core.PApplet;
import processing.core.PFont;
import rita.*;

public class TextSize extends PApplet {
	RiText rt;
	float tw, th;
	float scale = 1.4f;

	public void setup() {
		size(200, 200);

		rt = new RiText(this, "This is not the Text", 20, 55);
		rt.scale(scale);

		tw = rt.textWidth() * scale;
		th = rt.textHeight() * scale;

		// console.log('[P5] tw='+tw+" th="+th);
	}

	public void draw() {
		background(255);

		rt.draw();

		line(rt.x, rt.y + 2, rt.x + tw, rt.y + 2);

		if (++rt.y == height)
			rt.y = 0;
	}

}
