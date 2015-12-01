import rita.*;

void setup()
  {

    size(400, 400);
    background(255);

    RiText.defaultFont("Times", 64);

   for (int i = 0; i < 11; i++)
      new RiText(this, "alpha " + (i * 10), 100, (i + 1) * 38).alpha(i * 10).draw();
  

}

