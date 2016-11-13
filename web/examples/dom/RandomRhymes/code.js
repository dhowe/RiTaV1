$(document).ready(function () {

  //initialize lexicon
  var lexicon = new RiLexicon();

  findRhymes();
  setInterval(findRhymes, 2000); // called every 2 sec by timer

  function findRhymes() {

    var word, tmp = '';
    while (tmp.length < 3) {

      word = lexicon.randomWord();
      tmp = lexicon.rhymes(word);
    }

    var rhymes = tmp.slice(0, Math.min(tmp.length, 13));

    $('#word').html(word);
    $('#rhyme').html(rhymes.join("<br>"));
  }
});
