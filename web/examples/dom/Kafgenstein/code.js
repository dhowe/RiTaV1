var lines, markov;

$(document).ready(function () {

  markov = new RiMarkov(4);

  loadMarkovFromFile('../../data/kafka.txt')
  .then(loadMarkovFromFile('../../data/wittgenstein.txt'), failureCallback);

  $('.textarea').text("click to (re)generate!");
  $('div').click(generate);
});

function loadMarkovFromFile(file) {
  
  var promise = new Promise(function(resolve, reject) {
    RiTa.loadString(file, function (data) {
        markov.loadText(data);
    });
  });

  return promise;
}

function failureCallback() {
  console.log("Failed to load text to RiMarkov.")
}

function generate() {

  if (!markov.ready()) return;

  lines = markov.generateSentences(10);

  $('.textarea').text(lines.join(' '));
  $('.textarea').css('align-items', 'stretch');
}
