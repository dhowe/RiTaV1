RiLexicon.SILENCE_LTS = false;
RiLexicon.enabled = true;

RiLexicon.prototype = {

  init: function() {

    this.reload();
    //console.log("Init::Creating RiLexicon!");
  },

  clear: function() {

    this.data = {};
    this.keys = [];
  },

  reload: function() {

    this.data = _dict();
    this.keys = okeys(this.data);
  },

  addWord: function(word, pronunciationData, posData) {

    this.data[word.toLowerCase()] = [
      pronunciationData.toLowerCase(),
      posData.toLowerCase()
    ];
    this.keys = okeys(this.data);
    return this;
  },

  removeWord: function(word) {

    delete this.data[word.toLowerCase()];
    this.keys = okeys(this.data);
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

    for (var i = 0; i < this.keys.length; i++) {

      var entry = this.keys[i];

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

  similarBySound: function(input, minEditDist, minimumWordLen) {

    minEditDist = minEditDist || 1;

    var minVal = Number.MAX_VALUE,
      entry, result = [], minLen = minimumWordLen || 2,
      phonesArr, phones = RiTa.getPhonemes(input), med,
      targetPhonesArr = phones ? phones.split('-') : [],
      input_s = input + 's', input_es = input + 'es',
      lts = this._letterToSound();

    if (!targetPhonesArr[0] || !(input && input.length)) return EA;

    //console.log("TARGET "+targetPhonesArr);

    for (var i = 0; i < this.keys.length; i++) {

      entry = this.keys[i];

      if (entry.length < minLen) continue;

      // entry = entry.toLowerCase(); // all lowercase

      if (entry === input || entry === input_s || entry === input_es)
        continue;

      phones = this.data[entry][0];
      //if (i<10) console.log(phones+" :: "+);
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

  similarBySoundAndLetter: function(word) {

    var result = [], simSound, simLetter = this.similarByLetter(word);

    if (simLetter.length < 1)
      return result;

    simSound = this.similarBySound(word);

    if (simSound.length < 1)
      return result;

    return intersect(simSound, simLetter);
  },

  substrings: function(word, minLength) {

    minLength = minLength || (minLength === 0) || 4;

    var result = [];
    for (var i = 0; i < this.keys.length; i++) {

      if (this.keys[i] === word || this.keys[i].length < minLength)
        continue;
      if (word.indexOf(this.keys[i]) >= 0)
        result.push(this.keys[i]);
    }

    return result;
  },

  superstrings: function(word) {

    var result = [];

    for (var i = 0; i < this.keys.length; i++) {

      if (this.keys[i] === word) continue;
      if (this.keys[i].indexOf(word) >= 0)
        result.push(this.keys[i]);
    }

    return result;
  },

  words: function() {

    var a = arguments,
      shuffled = false,
      regex, wordArr = [];

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
          return a[0] ? shuffle(this.keys) : this.keys;
        }

        regex = (is(a[0], R)) ? a[0] : new RegExp(a[0]);

        break;

      case 0:

        return this.keys;
    }

    for (var i = 0; i < this.keys.length; i++) {

      if (regex.test(this.keys[i])) {

        wordArr.push(this.keys[i]);
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

    return (strOk(word) && this.data && this.data[word.toLowerCase()]);
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

      var p = this._lastStressedPhoneToEnd(word),
        phones, results = [];

      for (var i = 0; i < this.keys.length; i++) {

        if (this.keys[i] === word)
          continue;

        phones = this.data[this.keys[i]][0];

        if (endsWith(phones, p))
          results.push(this.keys[i]);
      }
      return (results.length > 0) ? results : EA;
    }

    return EA;
  },

  alliterations: function(word, matchMinLength, useLTS) {

    matchMinLength = matchMinLength || 4;

    var c2, results = [],
      c1 = this._firstConsonant(this._firstStressedSyllable(word, useLTS));

    for (var i = 0; i < this.keys.length; i++) {

      c2 = this._firstConsonant(
          this._firstStressedSyllable(this.keys[i], useLTS));

      if (c2 && c1 === c2 && this.keys[i].length > matchMinLength) {
        results.push(this.keys[i]);
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
      idx = -1, c, firstToEnd;

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

    return this.keys.length;
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

    var wordArr = RiTa.tokenize(word), raw = [];

    for (var i = 0; i < wordArr.length; i++)
      raw[i] = this._getRawPhones(wordArr[i]).replace(/\s/g, '/');

    return RiTa.untokenize(raw).replace(/1/g, E).trim();
  },

  _getPhonemes: function(word) {

    // TODO: use feature cache?

    if (!strOk(word)) return E;

    var wordArr = RiTa.tokenize(word), raw = [];

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

    var i, stresses = [], phones, raw = [],
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
      this.data = dictionaryDataObject;
      return this;
    }

    return this.data;
  },

  /*
   * Returns the raw (RiTa-format) dictionary entry for the given word
   * @returns {array} a 2-element array of strings,
   * the first is the stress and syllable data,
   * the 2nd is the pos data, or null if the word is not found
   */
  _lookupRaw: function(word) {

    word = word.toLowerCase();
    if (this.data && this.data[word])
      return this.data[word];
    //log("[RiTa] No lexicon entry for '" + word + "'");
  },

  _getRawPhones: function(word, useLTS) {

    var phones, rdata = this._lookupRaw(word);
    useLTS = useLTS || false;

    if (rdata && useLTS && !RiTa.SILENT && !RiLexicon.SILENCE_LTS) {
      log("[RiTa] Using letter-to-sound rules for: " + word);

      phones = this._letterToSound().getPhones(word);

      //console.log("phones="+RiTa.asList(phones));
      if (phones && phones.length)
        return RiString.syllabify(phones);

    }
    return (rdata && rdata.length === 2) ? rdata[0] : E;
  },

  _getPosData: function(word) {

    var rdata = this._lookupRaw(word);
    return (rdata && rdata.length === 2) ? rdata[1] : E;
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

    var i, j, rdata, numSyls,
      ran = Math.floor(Math.random() * this.keys.length),
      found = false, a = arguments, ranWordArr = this.keys;

    switch (a.length) {

      case 2: // a[0]=pos  a[1]=syllableCount

        a[0] = trim(a[0]).toLowerCase();
        for (i = 0; i < ranWordArr.length; i++) {
          j = (ran + i) % ranWordArr.length;
          rdata = this.data[ranWordArr[j]];
          numSyls = rdata[0].split(SP).length;
          if (numSyls === a[1] && a[0] === rdata[1].split(SP)[0]) {
            return ranWordArr[j];
          }
        }
        return E;

      case 1:

        if (is(a[0], S)) { // a[0] = pos

          a[0] = trim(a[0]).toLowerCase();
          for (i = 0; i < ranWordArr.length; i++) {
            j = (ran + i) % ranWordArr.length;
            rdata = this.data[ranWordArr[j]];
            if (a[0] === rdata[1].split(SP)[0]) {
              return ranWordArr[j];
            }
          }

        } else { // a[0] = syllableCount

          for (i = 0; i < ranWordArr.length; i++) {
            j = (ran + i) % ranWordArr.length;
            rdata = this.data[ranWordArr[j]];
            if (rdata[0].split(SP).length === a[0]) {
              return ranWordArr[j];
            }
          }
        }
        return E;

      case 0:
        return ranWordArr[ran];
    }
    return E;
  },

  _letterToSound: function() { // lazy load

    if (!this.lts)
      this.lts = new LetterToSound();
    return this.lts;
  }

};

// from: https://gist.github.com/lovasoa/3361645
function intersect() {
  var i, all, n, len, ret = [], obj={}, shortest = 0,
    nOthers = arguments.length-1, nShortest = arguments[0].length;
  for (i=0; i<=nOthers; i++){
    n = arguments[i].length;
    if (n<nShortest) {
      shortest = i;
      nShortest = n;
    }
  }
  for (i=0; i<=nOthers; i++) {
    n = (i===shortest)?0:(i||shortest);
    len = arguments[n].length;
    for (var j=0; j<len; j++) {
        var elem = arguments[n][j];
        if(obj[elem] === i-1) {
          if(i === nOthers) {
            ret.push(elem);
            obj[elem]=0;
          } else {
            obj[elem]=i;
          }
        }else if (i===0) {
          obj[elem]=0;
        }
    }
  }
  return ret;
}

/////////////////////////////////////////////////////////////////////////
// RiLetterToSound (adapted from FreeTTS text-to-speech)
/////////////////////////////////////////////////////////////////////////

var LetterToSound = makeClass();

LetterToSound.RULES = _RiTa_LTS;

/*
 * Entry in file represents the total number of states in the file. This
 * should be at the top of the file. The format should be "TOTAL n" where n is
 * an integer value.
 */
LetterToSound.TOTAL = "TOTAL";

/*
 * Entry in file represents the beginning of a new letter index. This should
 * appear before the list of a new set of states for a particular letter. The
 * format should be "INDEX n c" where n is the index into the state machine
 * array and c is the character.
 */
LetterToSound.INDEX = "INDEX";

/*
 * Entry in file represents a state. The format should be "STATE i c t f"
 * where 'i' represents an index to look at in the decision string, c is the
 * character that should match, t is the index of the state to go to if there
 * is a match, and f is the of the state to go to if there isn't a match.
 */
LetterToSound.STATE = "STATE";

/*
 * Entry in file represents a final state. The format should be "PHONE p"
 * where p represents a phone string that comes from the phone table.
 */
LetterToSound.PHONE = "PHONE";

/*
 * If true, the state string is tokenized when it is first read. The side
 * effects of this are quicker lookups, but more memory usage and a longer
 * startup time.
 */
LetterToSound.tokenizeOnLoad = true;

/*
 * If true, the state string is tokenized the first time it is referenced. The
 * side effects of this are quicker lookups, but more memory usage.
 */
LetterToSound.tokenizeOnLookup = false;

LetterToSound.WINDOW_SIZE = 4;

LetterToSound.prototype = {

  init: function() {

    /*
     * The indices of the starting points for letters in the state machine.
     */
    this.letterIndex = {};

    /*
     * An array of characters to hold a string for checking against a rule. This
     * will be reused over and over again, so the goal was just to have a single
     * area instead of new'ing up a new one for every word. The name choice is to
     * match that in Flite's <code>cst_lts.c</code>.
     */
    this.fval_buff = [];

    /*
     * The LTS state machine. Entries can be String or State. An ArrayList could
     * be used here -- I chose not to because I thought it might be quicker to
     * avoid dealing with the dynamic resizing.
     */
    this.stateMachine = null;

    /*
     * The number of states in the state machine.
     */
    this.numStates = 0;

    // add the rules to the object (static?)
    for (var i = 0; i < LetterToSound.RULES.length; i++) {

      this.parseAndAdd(LetterToSound.RULES[i]);
    }
  },

  _createState: function(type, tokenizer) {

    if (type === LetterToSound.STATE) {
      var index = parseInt(tokenizer.nextToken());
      var c = tokenizer.nextToken();
      var qtrue = parseInt(tokenizer.nextToken());
      var qfalse = parseInt(tokenizer.nextToken());

      return new DecisionState(index, c.charAt(0), qtrue, qfalse);
    } else if (type === LetterToSound.PHONE) {
      return new FinalState(tokenizer.nextToken());
    }

    throw Error("Unexpected type: " + type);
  },

  /* Creates a word from the given input line and add it to the state machine.
	   It expects the TOTAL line to come before any of the states.*/
  parseAndAdd: function(line) {

    var tokenizer = new StringTokenizer(line, SP);
    var type = tokenizer.nextToken();

    if (type == LetterToSound.STATE || type == LetterToSound.PHONE) {
      if (LetterToSound.tokenizeOnLoad) {
        this.stateMachine[this.numStates] = this._createState(type, tokenizer);
      } else {
        this.stateMachine[this.numStates] = line;
      }
      this.numStates++;
    } else if (type == LetterToSound.INDEX) {
      var index = parseInt(tokenizer.nextToken());
      if (index != this.numStates) {
        throw Error("Bad INDEX in file.");
      } else {
        var c = tokenizer.nextToken();
        this.letterIndex[c] = index;

      }
      //log(type+" : "+c+" : "+index + " "+this.letterIndex[c]);
    } else if (type == LetterToSound.TOTAL) {
      this.stateMachine = [];
      this.stateMachineSize = parseInt(tokenizer.nextToken());
    }
  },

  getPhones: function(input, delim) {

    var i, ph, result = [];

    delim = delim || '-';

    if (is(input, S)) {

      if (!input.length) return E;

      input = RiTa.tokenize(input);
    }

    for (i = 0; i < input.length; i++) {

      ph = this._computePhones(input[i]);
      result[i] = ph ? ph.join(delim) : E;
    }

    return result.join(delim);
  },

  _computePhones: function(word) {

    var dig, phoneList = [],
      full_buff, tmp, currentState, startIndex, stateIndex, c;

    if (!word || !word.length || RiTa.isPunctuation(word))
      return null;

    word = word.toLowerCase();

    if (isNum(word)) {

      word = (word.length > 1) ? word.split(E) : [word];

      for (var k = 0; k < word.length; k++) {

        dig = parseInt(word[k]);
        if (dig < 0 || dig > 9)
          throw Error("Attempt to pass multi-digit number to LTS: '" + word + "'");

        phoneList.push(RiString.phones.digits[dig]);
      }

      return phoneList;
    }

    // Create "000#word#000", uggh
    tmp = "000#" + word.trim() + "#000", full_buff = tmp.split(E);

    // For each character in the word, create a WINDOW_SIZE
    // context on each size of the character, and then ask the
    // state machine what's next
    for (var pos = 0; pos < word.length; pos++) {

      for (var i = 0; i < LetterToSound.WINDOW_SIZE; i++) {

        this.fval_buff[i] = full_buff[pos + i];
        this.fval_buff[i + LetterToSound.WINDOW_SIZE] =
          full_buff[i + pos + 1 + LetterToSound.WINDOW_SIZE];
      }

      c = word.charAt(pos);
      startIndex = this.letterIndex[c];

      // must check for null here, not 0 (and not ===)
      if (!isNum(startIndex)) {
        warn("Unable to generate LTS for '" + word + "'\n       No LTS index for character: '" +
          c + "', isDigit=" + isNum(c) + ", isPunct=" + RiTa.isPunctuation(c));
        return null;
      }

      stateIndex = parseInt(startIndex);

      currentState = this.getState(stateIndex);

      while (!(currentState instanceof FinalState)) {

        stateIndex = currentState.getNextState(this.fval_buff);
        currentState = this.getState(stateIndex);
      }

      currentState.append(phoneList);
    }

    return phoneList;
  },

  getState: function(i) {

    if (is(i, N)) {

      var state = null;

      // WORKING HERE: this check should fail :: see java
      if (is(this.stateMachine[i], S)) {

        state = this.getState(this.stateMachine[i]);
        if (LetterToSound.tokenizeOnLookup)
          this.stateMachine[i] = state;
      } else
        state = this.stateMachine[i];

      return state;
    } else {

      var tokenizer = new StringTokenizer(i, " ");
      return this.getState(tokenizer.nextToken(), tokenizer);
    }
  }
};

/////////////////////////////////////////////////////////////////////////
// DecisionState
/////////////////////////////////////////////////////////////////////////

var DecisionState = makeClass();

DecisionState.TYPE = 1;

DecisionState.prototype = {

  /*
   * Class constructor.
   *
   * @param index
   *          the index into a string for comparison to c
   * @param c
   *          the character to match in a string at index
   * @param qtrue
   *          the state to go to in the state machine on a match
   * @param qfalse
   *          the state to go to in the state machine on no match
   */
  init: function(index, c, qtrue, qfalse) {

    this.c = c;
    this.index = index;
    this.qtrue = qtrue;
    this.qfalse = qfalse;
  },

  type: function() {

    return "DecisionState";
  },

  /*
   * Gets the next state to go to based upon the given character sequence.
   *
   * @param chars the characters for comparison
   *
   * @returns an index into the state machine.
   */
  //public var getNextState(char[] chars)
  getNextState: function(chars) {

    return (chars[this.index] == this.c) ? this.qtrue : this.qfalse;
  },

  /*
   * Outputs this <code>State</code> as though it came from the text input
   * file.
   */
  toString: function() {
    return this.STATE + " " + this.index + " " + this.c + " " + this.qtrue + " " + this.qfalse;
  }

}; // end DecisionState

// ///////////////////////////////////////////////////////////////////////
// FinalState
// ///////////////////////////////////////////////////////////////////////

var FinalState = makeClass();

FinalState.TYPE = 2;

FinalState.prototype = {

  /*
   * Constructor: the string "epsilon" is used to indicate an empty list.
   */
  init: function(phones) {

    this.phoneList = [];

    if (phones === ("epsilon")) {
      this.phoneList = null;
    } else if (is(phones, A)) {

      this.phoneList = phones;
    } else {
      var i = phones.indexOf('-');
      if (i != -1) {
        this.phoneList[0] = phones.substring(0, i);
        this.phoneList[1] = phones.substring(i + 1);
      } else {
        this.phoneList[0] = phones;
      }
    }
  },

  type: function() {

    return "FinalState";
  },

  /*
   * Appends the phone list for this state to the given <code>ArrayList</code>.
   */
  append: function(array) {

    if (!this.phoneList) return;

    for (var i = 0; i < this.phoneList.length; i++)
      array.push(this.phoneList[i]);
  },

  /*
   * Outputs this <code>State</code> as though it came from the text input
   * file. The string "epsilon" is used to indicate an empty list.
   */
  toString: function() {

    if (!this.phoneList) {
      return LetterToSound.PHONE + " epsilon";
    } else if (this.phoneList.length == 1) {
      return LetterToSound.PHONE + " " + this.phoneList[0];
    } else {
      return LetterToSound.PHONE + " " + this.phoneList[0] + "-" + this.phoneList[1];
    }
  }
};
