var dbug = false, ALL_PHONES = ['aa','ae','ah','ao','aw','ay','b','ch','d','dh','eh','er','ey','f','g','hh','ih','iy','jh', 'k','l', 'm','n','ng','ow','oy','p','r','s','sh','t','th','uh', 'uw','v','w','y','z','zh'];

$(document).ready(function () {

  var word, lexicon = new RiLexicon();
  var sy, ph, ss, hues = colorGradient();

  clearBubble();
  selectWord();
  setInterval(selectWord, 4000); // every 4 sec

  function selectWord() {

    // random word with <= 12 letters
    do {
      word = lexicon.randomWord();
    } while (word && word.length > 12);

    // get various features
    sy = RiTa.getSyllables(word);
    ph = RiTa.getPhonemes(word);
    ss = RiTa.getStresses(word);

    dbug && console.log(sy);

    var tags = RiTa.getPosTags(word, true);
    var pos = tagName(tags[0]);
    var ipaPhones = ipaPhones = arpaToIPA(lexicon._getRawPhones(word));

    $('#word').text(word);
    $('#pos').text(pos);
    $('#ipa').text("/" + ipaPhones + "/");

    refreshBubble(ph.split('-'));

    setTimeout(drop, 2000);
    setTimeout(clearBubble, 3500);
  }

  function clearBubble() {
    dbug && console.log("clear");

    $('.bubbles').children().each(function (i, val) {
       // reset stress
      if( $(this).hasClass("stressed"))
        $(this).removeClass("stressed");

      //reset position
      $(this).css({
        'margin-top': ' 5px'
      });

      // clear the content
      $(this).text("");
      $(this).css("background-color", "transparent");

    });
  }

  function refreshBubble(phs) {

    dbug && console.log("refresh");

    $('.bubbles').children().each(function (i, val) {

      // change the phones and color
      if (i < phs.length) {

        $(this).text(phs[i]);
        $(this).css("background-color", "hsla(" + phonemeColor(phs[i]) + ", 90%, 45%, 0.6)");
        addStress(ss, sy);
        addSyllables(sy);

      }

    });
  }

  function drop() {

    $('.bubbles').children().each(function (index) {
      (function (that, i) {
        var t = setTimeout(function () {
          $(that).animate({
            'margin-top': 180,
          }, "slow");
        }, 40 * i);
      })(this, index);
    });
  }

  function addSyllables(syllables) {

    dbug && console.log("addSyllables");

    var syllable = syllables.split("/");
    for (var i = 0, past = 0; i < syllable.length; i++) {
      var phs = syllable[i].split("-");
      //add extra space between each syllables
      $('.bubbles').children().eq(past).css("margin-left", "10px");

      for (var j = 1; j < phs.length; j++) {
        (function (j) {
          var bubble = $('.bubbles').children().eq(j + past);
          if(bubble.hasClass('stressed'))
             bubble.css("margin-left", "-20px");
           else
            bubble.css("margin-left", "-15px");
        })(j);
      }
      past += phs.length;
    }
  }

  function addStress(stresses, syllables, bubbles) {
    dbug && console.log("addStress");
    // Split stresses and syllables
    var stress = stresses.split('/'), syllable = syllables.split('/');

    for (var i = 0, past = 0; i < stress.length; i++) {

      var phs = syllable[i].split('-');

      // if the syllable is stressed, grow its bubbles
      if (parseInt(stress[i]) == 1) {
        for (var j = 0; j < phs.length; j++) {
          (function (j) {
            $('.bubbles').children().eq(j + past).addClass("stressed");
          })(j);
        }
      }
      past += phs.length;
    }
  }

  function tagName(tag) {
    if (tagsDict == null) {
      var tagsDict = {
        'n': 'Noun',
        'v': 'Verb',
        'r': 'Adverb',
        'a': 'Adjective'
      };
    }
    return tag != null ? tagsDict[tag] : null;
  }

  function phonemeColor(phoneme) {
    var idx = ALL_PHONES.indexOf(phoneme);
    return idx > -1 ? hues[idx] : 0;
  }

  function colorGradient() {
    var tmp = [];
    for (var i = 0; i < ALL_PHONES.length; i++) {
      var h = Math.floor(map(i, 0, ALL_PHONES.length, .2 * 360, .8 * 360));
      tmp[i] = h;
    }
    return tmp;
  }

  function map(value, low1, high1, low2, high2) {
    return low2 + (high2 - low2) * (value - low1) / (high1 - low1);
  }

}); //jquery wrapper
