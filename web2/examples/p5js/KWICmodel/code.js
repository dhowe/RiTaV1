var buttons = [
  "Gregor", "Samsa", "family", "being",
  "clerk", "room", "violin", "window"
],
  word = buttons[7], buttonX = 150, over, data, kwic, input;

function preload() {

  data = loadStrings('../../data/kafka.txt');
}

function setup() {

  createCanvas(800, 500);
  textFont('Times');
  textSize(18);
  fill(0);

  updateKWIC();
}

function updateKWIC() {

  kwic = RiTa.kwic(data.join('\n'), word, {
    ignorePunctuation: true,
    ignoreStopWords: true,
    wordCount: 6
  });

  background(250);

  drawButtons();

  if (kwic.length == 0) {

    textAlign(CENTER);
    text("Word not found", width / 2, height / 2);

  } else {

    var tw = textWidth(word) / 2;

    for (var i = 0; i < kwic.length; i++) {

      //console.log(display[i]);
      var parts = kwic[i].split(word);
      var x = width / 2,
        y = i * 20 + 75;

      if (y > height - 20) return;

      fill(0);
      textAlign(RIGHT);
      text(parts[0], x - tw, y);

      fill(200, 0, 0);
      textAlign(CENTER);
      text(word, x, y);

      fill(0);
      textAlign(LEFT);
      text(parts[1], x + tw, y);
    }
  }
}

function drawButtons() {

  var posX = buttonX, posY = 40;

  for (var i = 0; i < buttons.length; i++) {

    stroke(200);
    var on = word == (buttons[i]) ? true : false;
    var tw = textWidth(buttons[i]);
    fill(!on && buttons[i] == over ? 235 : 255);
    rect(posX - 5, 24, tw + 10, 20, 7);
    fill((on ? 255 : 0), 0, 0);
    text(buttons[i], posX, 40);
    posX += tw + 20;
  }
}

function inside(mx, my, posX, tw) {

  return (mx >= posX - 5 && mx <= posX + tw + 5 && my >= 25 && my <= 44);
}

function mouseMoved() {

  over = null;
  var posX = buttonX, tw;

  for (var i = 0; i < buttons.length; i++) {

    tw = textWidth(buttons[i]);

    if (inside(mouseX, mouseY, posX, tw)) {

      over = buttons[i];
      break;
    }
    posX += tw + 20;
  }
}

function mouseClicked() {

  var posX = buttonX, tw;

  for (var i = 0; i < buttons.length; i++) {

    tw = textWidth(buttons[i]);

    if (inside(mouseX, mouseY, posX, tw)) {

      word = buttons[i];
      kwic = null;
      updateKWIC();
      break;
    }
    posX += tw + 20;
  }
}