
import rita.*;

size(400, 400);

RiTa.start(this);

RiText.defaultFontSize(64);
RiText.defaults.showBounds = true;

new RiText("Left", 200,  100);
new RiText("Center", 200, 200).align(CENTER);
new RiText("Right", 200, 300).align(RIGHT);

background(255);
line(200, 0, 200, 400);

RiText.drawAll();