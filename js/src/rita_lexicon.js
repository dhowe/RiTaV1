var RiTa = require('./rita').RiTa;
var RiLexicon = RiTa._makeClass();

RiLexicon.data; // shared static
RiLexicon.emittedWarning = false;

RiLexicon.prototype = {

  init: function() {
    !RiLexicon.data && this._load();
  },

  _load: function() {

    if (typeof _RiTa_DICT != 'undefined' && RiTa.USE_LEXICON) {

      RiLexicon.data = {};
      for (var word in _RiTa_DICT)
        RiLexicon.data[word] = _RiTa_DICT[word]; // needed?
    }
    else {
      if (!RiLexicon.emittedWarning)
        warn('RiTa lexicon appears to be missing! '+
          'Part-of-speech tagging (at least) will be inaccurate');
      RiLexicon.emittedWarning = true
    }
  },

  reload: function() {

    this.clear();
    this._load();
  },

  clear: function() {

    RiLexicon.data = undefined;

    for (var word in RiLexicon.data)
      delete RiLexicon.data[word];
  },

  addWord: function(word, pronunciationData, posData) {

    RiLexicon.data[word.toLowerCase()] = [pronunciationData.toLowerCase(), posData.toLowerCase()];
    return this;
  },

  removeWord: function(word) {

    delete RiLexicon.data[word.toLowerCase()];
    return this;
  },

  similarByLetter: function(input, minAllowedDist, preserveLength) {

    var minVal = Number.MAX_VALUE,
      minLen = 2,
      result = [];

    if (!(input && input.length)) return EA;

    input = input.toLowerCase();
    minAllowedDist = minAllowedDist || 1;
    preserveLength = preserveLength || false;

    var med, inputS = input + 's',
      inputES = input + 'es',
      inputLen = input.length;

    for (var entry in RiLexicon.data) {

      if (entry.length < minLen)
        continue;

      if (preserveLength && (entry.length != inputLen))
        continue;

      if (entry === input || entry === inputS || entry === inputES)
        continue;

      med = MinEditDist.computeRaw(entry, input);

      // we found something even closer
      if (med >= minAllowedDist && med < minVal) {

        minVal = med;
        result = [entry];
      }

      // we have another best to add
      else if (med === minVal) {

        result.push(entry);
      }
    }

    return result;
  },

  similarBySound: function(input, minEditDist) {

    minEditDist = minEditDist || 1;

    var minVal = Number.MAX_VALUE,
      entry, result = [],
      minLen = 2,
      phonesArr, phones = RiTa.getPhonemes(input),
      med,
      targetPhonesArr = phones ? phones.split('-') : [],
      input_s = input + 's',
      input_es = input + 'es',
      lts = LetterToSound();

    if (!targetPhonesArr[0] || !(input && input.length)) return EA;

    //console.log("TARGET "+targetPhonesArr);

    for (entry in RiLexicon.data) {

      if (entry.length < minLen) continue;

      // entry = entry.toLowerCase(); // all lowercase

      if (entry === input || entry === input_s || entry === input_es)
        continue;

      phones = this._getRawPhones(entry);
      phonesArr = phones.replace(/1/g, E).replace(/ /g, '-').split('-');

      med = MinEditDist.computeRaw(phonesArr, targetPhonesArr);

      // found something even closer
      if (med >= minEditDist && med < minVal) {

        minVal = med;
        result = [entry];
        //console.log("BEST "+entry + " "+med + " "+phonesArr);
      }

      // another best to add
      else if (med === minVal) {

        //console.log("TIED "+entry + " "+med + " "+phonesArr);
        result.push(entry);
      }
    }

    return result;
  },

  substrings: function(word, minLength) {

    minLength = minLength || (minLength === 0) || 4;

    var entry, result = [];
    for (entry in RiLexicon.data) {
      if (entry === word || entry.length < minLength) continue;
      if (word.indexOf(entry) >= 0) result.push(entry);
    }

    return result;
  },

  superstrings: function(word) {

    var entry, result = [];

    for (entry in RiLexicon.data) {
      if (entry === word) continue;
      if (entry.indexOf(word) >= 0)
        result.push(entry);
    }

    return result;
  },

  similarBySoundAndLetter: function(word) {

    var result = [],
      simSound = this.similarBySound(word),
      simLetter = this.similarByLetter(word);

    if (!simSound || simSound.length < 1 || !simLetter || simLetter.length < 1)
      return result;

    // union of two sets
    for (var i = 0; i < simSound.length; i++) {

      if (simLetter.indexOf(simSound[i]) > -1)
        result.push(simSound[i]);
    }

    return result;
  },

  words: function() {

    var a = arguments,
      shuffled = false,
      regex,
      wordArr = [],
      words = okeys(RiLexicon.data);

    switch (a.length) {

      case 2:

        if (is(a[0], B)) {

          shuffled = a[0];
          regex = (is(a[1], R)) ? a[1] : new RegExp(a[1]);
        } else {

          shuffled = a[1];
          regex = (is(a[0], R)) ? a[0] : new RegExp(a[0]);
        }

        break;

      case 1:

        if (is(a[0], B)) {
          return a[0] ? shuffle(words) : words;
        }

        regex = (is(a[0], R)) ? a[0] : new RegExp(a[0]);

        break;

      case 0:

        return words;
    }

    for (var i = 0; i < words.length; i++) {

      if (regex.test(words[i])) {

        wordArr.push(words[i]);
      }
    }

    return shuffled ? shuffle(wordArr) : wordArr;
  },

  _isVowel: function(c) {

    return (strOk(c) && RiTa.VOWELS.indexOf(c) > -1);
  },

  _isConsonant: function(p) {

    return (typeof p === S && p.length === 1 &&
      RiTa.VOWELS.indexOf(p) < 0 && /^[a-z\u00C0-\u00ff]+$/.test(p));
  },

  containsWord: function(word) {

    return (strOk(word) && RiLexicon.data && RiLexicon.data[word.toLowerCase()]);
  },

  isRhyme: function(word1, word2, useLTS) {

    if (!strOk(word1) || !strOk(word2) || equalsIgnoreCase(word1, word2))
      return false;

    var p1 = this._lastStressedPhoneToEnd(word1, useLTS),
      p2 = this._lastStressedPhoneToEnd(word2, useLTS);

    return (strOk(p1) && strOk(p2) && p1 === p2);
  },

  rhymes: function(word) {

    if (this.containsWord(word)) {

      var p = this._lastStressedPhoneToEnd(word);
      var entry, entryPhones, results = [];

      for (entry in RiLexicon.data) {
        if (entry === word)
          continue;
        entryPhones = this._getRawPhones(entry);

        if (strOk(entryPhones) && endsWith(entryPhones, p)) {
          results.push(entry);
        }
      }
      return (results.length > 0) ? results : EA;
    }

    return EA;
  },

  alliterations: function(word, matchMinLength, useLTS) {

    matchMinLength = matchMinLength || 4;

    var c2, entry, results = [];
    var c1 = this._firstConsonant(this._firstStressedSyllable(word, useLTS));

    for (entry in RiLexicon.data) {

      c2 = this._firstConsonant(this._firstStressedSyllable(entry, useLTS));

      if (c2 && c1 === c2 && entry.length > matchMinLength) {
        results.push(entry);
      }
    }
    return results;
  },

  isAlliteration: function(word1, word2, useLTS) {

    if (!strOk(word1) || !strOk(word2)) return false;

    if (equalsIgnoreCase(word1, word2)) return true;

    var c1 = this._firstConsonant(this._firstStressedSyllable(word1, useLTS)),
      c2 = this._firstConsonant(this._firstStressedSyllable(word2, useLTS));

    return (strOk(c1) && strOk(c2) && c1 === c2);
  },

  _firstStressedSyllable: function(word, useLTS) {

    var raw = this._getRawPhones(word, useLTS),
      idx = -1,
      c, firstToEnd;

    if (!strOk(raw)) return E; // return null?

    idx = raw.indexOf(RiTa.STRESSED);

    if (idx < 0) return E; // no stresses... return null?

    c = raw.charAt(--idx);

    while (c != ' ') {
      if (--idx < 0) {
        // single-stressed syllable
        idx = 0;
        break;
      }
      c = raw.charAt(idx);
    }

    firstToEnd = idx === 0 ? raw : trim(raw.substring(idx));
    idx = firstToEnd.indexOf(' ');

    return idx < 0 ? firstToEnd : firstToEnd.substring(0, idx);
  },

  isVerb: function(word) {

    return this._checkType(word, PosTagger.VERBS);
  },

  isNoun: function(word) {

    return this._checkType(word, PosTagger.NOUNS);
  },

  isAdverb: function(word) {

    return this._checkType(word, PosTagger.ADV);
  },

  isAdjective: function(word) {

    return this._checkType(word, PosTagger.ADJ);
  },

  size: function() {

    return RiLexicon.data ? okeys(RiLexicon.data).length : 0;
  },

  _checkType: function(word, tagArray) {

    if (word && word.indexOf(SP) != -1)
      throw Error("[RiTa] _checkType() expects a single word, found: " + word);

    var psa = this._getPosArr(word);
    for (var i = 0; i < psa.length; i++) {
      if (tagArray.indexOf(psa[i]) > -1)
        return true;
    }

    return false;
  },

  /*
   * Returns a String containing the phonemes for each syllable of each word of the input text,
   * delimited by dashes (phonemes) and semi-colons (words).
   * For example, the 4 syllables of the phrase
   * 'The dog ran fast' are "dh-ax:d-ao-g:r-ae-n:f-ae-s-t".
   * @returns {string} the phonemes for each syllable of each word
   */
  _getSyllables: function(word) {

    // TODO: use feature cache?

    if (!strOk(word)) return E;

    var wordArr = RiTa.tokenize(word),
      raw = [];

    for (var i = 0; i < wordArr.length; i++) {

      raw[i] = this._getRawPhones(wordArr[i]).replace(/\s/g, '/');
    }

    return RiTa.untokenize(raw).replace(/1/g, E).trim();
  },

  _getPhonemes: function(word) {

    // TODO: use feature cache?

    if (!strOk(word)) return E;

    var wordArr = RiTa.tokenize(word),
      raw = [];

    for (var i = 0; i < wordArr.length; i++) {

      if (RiTa.isPunctuation(wordArr[i])) continue;

      // raw[i] = wordArr[i].length
      raw[i] = this._getRawPhones(wordArr[i]);

      if (!raw[i].length) return E;
      //err("Unable to lookup (need LTSEngine): "+wordArr[i]);

      raw[i] = raw[i].replace(/ /g, "-");
    }

    return RiTa.untokenize(raw).replace(/1/g, E).trim();
  },

  _getStresses: function(word) {

    var i, stresses = [],
      phones, raw = [],
      wordArr = is(word, A) ? word : RiTa.tokenize(word);

    if (!strOk(word)) return E;

    for (i = 0; i < wordArr.length; i++) {

      if (!RiTa.isPunctuation(wordArr[i]))
        raw[i] = this._getRawPhones(wordArr[i]);
    }

    for (i = 0; i < raw.length; i++) {

      if (raw[i]) { // ignore undefined array items (eg Punctuation)

        phones = raw[i].split(SP);
        for (var j = 0; j < phones.length; j++) {

          var isStress = (phones[j].indexOf(RiTa.STRESSED) > -1) ?
            RiTa.STRESSED : RiTa.UNSTRESSED;

          if (j > 0) isStress = "/" + isStress;

          stresses.push(isStress);
        }
      }
    }

    return stresses.join(SP).replace(/ \//g, "/");
  },

  lexicalData: function(dictionaryDataObject) {


    if (arguments.length === 1) {
      RiLexicon.data = dictionaryDataObject;
      return this;
    }

    return RiLexicon.data;
  },

  /*
   * Returns the raw (RiTa-format) dictionary entry for the given word
   * @returns {array} a 2-element array of strings,
   * the first is the stress and syllable data,
   * the 2nd is the pos data, or null if the word is not found
   */
  _lookupRaw: function(word) {

    word = word.toLowerCase();
    if (RiLexicon.data && RiLexicon.data[word])
      return RiLexicon.data[word];

    //log("[RiTa] No lexicon entry for '" + word + "'");
    return null;
  },

  _getRawPhones: function(word, useLTS) {

    var phones, data = this._lookupRaw(word);
    useLTS = useLTS || false;

    if (data && useLTS) {
      log("[RiTa] Using letter-to-sound rules for: " + word);

      phones = LetterToSound.getInstance().getPhones(word);

      //console.log("phones="+RiTa.asList(phones));
      if (phones && phones.length)
        return RiString.syllabify(phones);

    }
    return (data && data.length === 2) ? data[0] : E;
  },

  _getPosData: function(word) {

    var data = this._lookupRaw(word);
    return (data && data.length === 2) ? data[1] : E;
  },


  _getPosArr: function(word) {

    var pl = this._getPosData(word);
    if (!strOk(pl)) return EA;
    return pl.split(SP);
  },

  _getBestPos: function(word) {

    var pl = this._getPosArr(word);
    return (pl.length > 0) ? pl[0] : [];
  },

  _firstConsonant: function(rawPhones) {

    if (!strOk(rawPhones)) return E;

    var phones = rawPhones.split(RiTa.PHONEME_BOUNDARY);

    if (phones) {

      for (var j = 0; j < phones.length; j++) {
        if (this._isConsonant(phones[j].charAt(0))) // first letter only
          return phones[j];
      }
    }
    return E; // return null?
  },

  _lastStressedPhoneToEnd: function(word, useLTS) {

    if (!strOk(word)) return E; // return null?

    var idx, c, result;
    var raw = this._getRawPhones(word, useLTS);

    if (!strOk(raw)) return E; // return null?

    idx = raw.lastIndexOf(RiTa.STRESSED);

    if (idx < 0) return E; // return null?

    c = raw.charAt(--idx);
    while (c != '-' && c != ' ') {
      if (--idx < 0) {
        return raw; // single-stressed syllable
      }
      c = raw.charAt(idx);
    }
    result = raw.substring(idx + 1);

    return result;
  },

  randomWord: function() { // takes nothing, pos, syllableCount, or both

    var found = false, a = arguments, wordArr = okeys(RiLexicon.data),
      ran = Math.floor(Math.random() * okeys(RiLexicon.data).length),
      ranWordArr = shuffle(wordArr), i, j, data;

    switch (a.length) {

      case 2: //a[0]=pos  a[1]=syllableCount

        a[0] = trim(a[0]);
        for (j = 0; j < PosTagger.TAGS.length; j++) {
          if (PosTagger.TAGS[j] === a[0]) found = true;
        }

        if (found) {
          a[0] = a[0].toLowerCase();
          for (i = 0; i < ranWordArr.length; i++) {
            data = this._lookupRaw(ranWordArr[i]);
            var posTag = RiTa.getPosTags(ranWordArr[i]);
            if (data[0].split(SP).length === a[1] && a[0] ===
              this._getBestPos(ranWordArr[i]))
            {
              return ranWordArr[i];
            }
          }
        }
        return E;

      case 1:

        if (is(a[0], S)) { // a[0] = pos

          a[0] = trim(a[0]);
          for (j = 0; j < PosTagger.TAGS.length; j++) {
            if (PosTagger.TAGS[j] === a[0]) found = true;
          }

          if (found) {
            a[0] = a[0].toLowerCase();
            for (i = 0; i < ranWordArr.length; i++) {
              var thePos = this._getBestPos(ranWordArr[i]);
              if (a[0] === thePos) {
                return ranWordArr[i];
              }
            }
          }
        } else { // a[0] = syllableCount

          for (i = 0; i < ranWordArr.length; i++) {
            data = this._lookupRaw(ranWordArr[i]);
            if (data[0].split(SP).length === a[0]) {
              return ranWordArr[i];
            }
          }
        }
        break;

      case 0:
        return wordArr[ran];
    }
    return E;
  }
};

module.exports = RiLexicon;
