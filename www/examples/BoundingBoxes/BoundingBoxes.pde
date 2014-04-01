
import rita.*;

size(400, 400);

RiText.defaults.showBounds = true;
RiText.defaultFont("Times", 64);

RiText rt = new RiText(this, "Left", 200, 100);
new RiText(this, "Center", 200, 200).align(CENTER);
new RiText(this, "Right", 200, 300).align(RIGHT);

background(255);
line(200, 0, 200, 400);
RiText.drawAll();
