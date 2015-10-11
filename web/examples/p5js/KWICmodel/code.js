var word = 'window', data, kwic, input;

function preload() {

  data = loadStrings('../../data/kafka.txt');
}

function setup() {

  createCanvas(800, 600);
  textFont('Times');

  input = createInput([word]);
  input.size(70);
  input.position(width / 2 - 50, 15);

  var button = createButton('UPDATE');
  button.position(width / 2 + 30, 15);
  button.mousePressed(newWord);

  input.elt.focus();
  newWord();
}

function updateKWIC() {

  kwic = RiTa.kwic(data.join('\n'), word, {
    ignorePunctuation: true,
    ignoreStopWords: true,
    wordCount: 6
  });

  background(250);
  fill(0);
  textSize(14);
  text('search:', 290, 21);

  textSize(18);

  if (kwic.length == 0) {

    textAlign(CENTER);
    text("Word not found", width / 2, height / 2);

  } else {

    var tw = textWidth(word) / 2;

    for (var i = 0; i < kwic.length; i++) {

      //console.log(display[i]);
      var parts = kwic[i].split(word);
      var x = width / 2,
        y = i * 20 + 50;

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

function newWord() {

  word = input.value();
  input.elt.placeholder = word;
  updateKWIC();
  input.value('');
}

function keyPressed() {

  if (keyCode == 13) newWord();
}
