var lines, markov;

$(document).ready(function () {

  markov = new RiMarkov(4);

  RiTa.loadString('../../data/kafka.txt', function (data1) {
    RiTa.loadString('../../data/wittgenstein.txt', function (data2) {
      markov.loadText(data1);
      markov.loadText(data2);
    });
  });

  $('span').text("click to (re)generate!");
  $('div').click(generate);
});

function generate() {

  if (!markov.ready()) return;

  lines = markov.generateSentences(10);

  $('span').text(lines.join(' '));
  $('span').css('align-items', 'stretch');
}
