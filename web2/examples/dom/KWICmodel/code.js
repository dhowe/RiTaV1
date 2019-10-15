const LEFT = "LEFT", CENTER = "CENTER",
  RIGHT = "RIGHT", WIDTH = 800, HEIGHT = 500;

var buttons = [
  "Gregor", "Samsa", "family", "being",
  "clerk", "room", "violin", "window"
],
  word = buttons[7],  fillColor, kafkaString, textAlignment = LEFT,
  $container, kwic, opts, $btns;

$(document).ready(function () {

  $btns = $(".cssBtns");
  
  assignButtons();
  $($btns[7]).css("color", "red");

  $container = $("#container");
  $container.width(WIDTH).height(HEIGHT)
    .css("background-color", "rgb(250, 250, 250)");
  $("#textInput").attr("placeholder", word);

  RiTa.loadString('../../data/kafka.txt', function (data) {

    kafkaString = data;
    kwic = RiTa.kwic(kafkaString, word, {
      ignorePunctuation: true,
      ignoreStopWords: true,
      wordCount: 6
    });

    drawText();
  });
});

function assignButtons() {

  for (var i = 0; i < $btns.length; i++)
    $($btns[i]).html(buttons[i]);
  
  $btns.click(function() {
    
    word = $(this).text();
    
    kwic = RiTa.kwic(kafkaString, word, opts);
    drawText();
    
    $btns.css("color", "black");
    $(this).css("color", "red");
  });
}

function drawText() {

  $("#container").empty();
  $("#container").css('background-color', 'rgb(250, 250, 250)');

  if (kwic.length == 0) {

    textAlign(CENTER);
    text("Word not found", WIDTH / 2, HEIGHT / 2);

  } else {

    var tw = textWidth(word) / 2;

    for (var i = 0; i < kwic.length; i++) {

      var parts = kwic[i].split(word);
      var x = WIDTH / 2,
        y = i * 20 + 65;

      if (y > HEIGHT - 30) return;

      fill(0);
      textAlign(RIGHT);
      text(parts[0], x - tw + 5, y);

      fill(200, 0, 0);
      textAlign(CENTER);
      text(word, x, y);

      fill(0);
      textAlign(LEFT);
      text(parts[1], x + tw, y);
    }
  }
}

function textWidth(s) {

  var $span = $('<span>', {
    html: s + '&nbsp;',
    css: {
      position: "absolute",
      top: "-100px",
      left: "-100px",
    }
  }).appendTo($('body'));

  var width = $span.width();
  $span.remove();

  return width;
}

function fill(a, b, c) {

  fillColor = (arguments.length == 1) ? toHex(a) :
    "rgb(" + a + "," + b + "," + c + ")";

  function toHex(color) {
    var intValue = Math.floor(color);
    return "rgb(" + intValue + "," + intValue + "," + intValue + ")";
  }
}

function textAlign(direction) {

  textAlignment = direction;
}

function text(s, x, y) {

  function convertPosition(innerX) {

    // position the strings according to alignment
    if (textAlignment == LEFT) {

      var ONLY_PUNCT = /^[^0-9A-Za-z\s]/;
      var regex = new RegExp(ONLY_PUNCT);
      if (regex.test(s))
        x = innerX - textWidth("");

    } else if (textAlignment == CENTER) {
      x = innerX - textWidth(s) / 2;

    } else if (textAlignment == RIGHT) {
      x = innerX - textWidth(s);
    }
  }

  convertPosition(x);

  var $span = $('<span/>', {
    html: s,
    css: {
      position: 'absolute',
      left: x, top: y,
      color: fillColor
    }
  }).appendTo($container);
}
