function zuckify(text, replacer, thresh, pos_to_replace) {
  function toTitleCase(str) {
    return str.replace(/\w+/g, function(txt) {
      return txt.charAt(0).toUpperCase() + txt.substr(1).toLowerCase();
    });
  }


  var suffixes = ["ac", "acity", "ocity", "ade", "age", "aholic", "oholic", "al", "algia", "an", "ian", "ance", "ant", "ar", "ard", "arian", "arium", "orium", "ary", "ate", "ation", "ative", "cide", "cracy", "crat", "cule", "cy", "cycle", "dom", "dox", "ectomy", "ed", "ee", "eer", "emia", "en", "ence", "ency", "ent", "er", "ern", "escence", "ese", "esque", "ess", "est", "etic", "ette", "ful", "fy", "gam", "gamy", "gon", "gonic", "hood", "ial", "ian", "iasis", "iatric", "ible", "ic", "ical", "ile", "ily", "ine", "ing", "ion", "ious", "ish", "ism", "ist", "ite", "itis", "ity", "ive", "ization", "ize", "less", "let", "like", "ling", "loger", "logist", "log", "ly", "ment", "ness", "oid", "ology", "oma", "onym", "opia", "opsy", "or", "ory", "osis", "ostomy", "otomy", "ous", "path", "pathy", "phile", "phobia", "phone", "phyte", "plegia", "plegic", "pnea", "scopy", "scope", "scribe", "script", "sect", "ship", "sion", "some", "sophy", "sophic", "th", "tion", "tome", "tomy", "trophy", "tude", "ty", "ular", "uous", "ure", "ward", "ware", "wise"];

  if (typeof pos_to_replace === 'undefined') {
    pos_to_replace = ['nnp', 'nn', 'nns', 'vb', 'vbp', 'vbg', 'rbr', 'jj'];
  }

  var graphs = text.split('\n');

  graphs = graphs.map(function(text) {
    var pos = RiTa.getPosTags(text);
    var tokens = RiTa.tokenize(text);

    var out = '';

    for (var i = 0; i < tokens.length; i++) {
      var word = tokens[i];
      var tag = pos[i];

      // skip contractions
      if (word.indexOf("'") > -1) {
        out += ' ' + word;
        continue;
      }

      // skip punctuation
      if (RiTa.isPunctuation(word)) {
        out += word;
        continue;
      }

      var new_word = word;

      if (pos_to_replace.indexOf(tag) > -1) {
        new_word = replacer;
        for (var j = 0; j < suffixes.length; j++) {
          if (word.search(suffixes[j] + '$') > -1) new_word += suffixes[j];
        }
      }

      if (word.charAt(0).toUpperCase() === word.charAt(0)) {
        new_word = toTitleCase(new_word);
      }

      if (new_word != word && Math.random() > thresh) {
        out += ' ' + new_word;
      } else {
        out += ' ' + word;
      }

    }

    return out;

  });

  return graphs.join('\n');

}
