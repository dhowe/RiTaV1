var grammar, lines;

$(document).ready(function () {

  lines = [ $('#line1'), $('#line2'), $('#line3') ];

  grammar = RiGrammar();
  grammar.loadFrom('../../data/haiku.yaml', function() {

    lines[0].text("click to");
    lines[1].text("generate");
    lines[2].text("a haiku");

    $('#hdiv').click(createHaiku);
  });
});

function createHaiku() {
  
  var result = grammar.expand();
  var haiku = result.split("%");
  for (var i = 0; i < lines.length; i++) {
    lines[i].text(haiku[i]);
  }
}
