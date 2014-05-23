import rita.*;

void setup() {
    
    size(800, 500);

    RiTa.start(this);

    RiText.defaults.showBounds = true;
    RiText.defaults.fontSize = 64;

    new RiText("Default", 200,  100);
    new RiText("Center", 200, 200).align(CENTER);
    new RiText("Right", 200, 300).align(RIGHT);
    new RiText("Left", 200, 400).align(LEFT);

    background(255);
    line(200, 0, 200, height);


    RiText.defaultFont("Times", 32);

    for (int i = 0; i < 10; i++)
      new RiText(this, "alpha " + (i * 10), 400, 
          (i + 1) * 38).alpha(i * 10).draw();

    RiText.defaults.showBounds = false;

    RiText.defaultFont("Times",32);

    for (int i = 0; i < 10; i++)
      new RiText(this, "alpha " + (i * 10), 600, 
          (i + 1) * 38).alpha(i * 10).draw();


    RiText.drawAll();
  }

