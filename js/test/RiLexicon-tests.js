var testResults = [{

  testName: 'testRhymes',
  testClass: 'RiLexicon',
  testMethod: 'rhymes',
  assertion: 'deepEqual',
  tests: [{
    input: '',
    output: []
  }, {
    input: 'apple.',
    output: []
  }, {
    input: 'goxgle',
    output: []
  }, {
    input: 'happens in here',
    output: []
  }, {
    input: 'apple',
    output: ['chapel', 'grapple', 'pineapple']
  }, {
    input: 'bible.',
    output: ['libel', 'tribal']
  }, ]
}];

var runtests = function() {

  if (!RiLexicon.enabled) {
    console.warn("[INFO] RiLexicon-tests: skipping ALL tests...");
    return;
  }

  var lex = new RiLexicon();

  QUnit.module("RiLexicon", {
    setup: function() {},
    teardown: function() {}
  });

  test("testSize", function() {

    ok(lex.size() > 30000);
  });

  test("testContainsWord", function() {

    ok(lex.containsWord("cat"));
    ok(lex.containsWord("cats"));
    ok(!lex.containsWord("cated"));
    ok(lex.containsWord("funny"));
    ok(lex.containsWord("shit"));
    ok(!lex.containsWord("123"));
    ok(!lex.containsWord("hellx"));
    ok(lex.containsWord("hello"));
    ok(lex.containsWord("HeLLo"));
    ok(lex.containsWord("HELLO"));

    var tmp = lex.data['hello'];
    lex.data['hello'] = undefined;
    ok(!lex.containsWord("hello"));
    lex.data['hello'] = tmp; // restore data
  });

  test("testAlliterations", function() {

    var result = lex.alliterations("cat");
    ok(result.length > 2000);

    var result = lex.alliterations("dog");
    ok(result.length > 1000);

    var result = lex.alliterations("URL");
    ok(result.length < 1);

    var result = lex.alliterations("no stress");
    ok(result.length < 1);

    var result = lex.alliterations("#$%^&*");
    ok(result.length < 1);

    var result = lex.alliterations("");
    ok(result.length < 1);

    // TODO: better tests
  });

  test("testAlliterations(int)", function() {

    var result = lex.alliterations("dog", 15);
    ok(result.length == 3);

    var result = lex.alliterations("cat", 16);
    //for (var i = 0; i < result.length; i++)
      //console.log(i + ") " + result[i]);

    ok(result.length == 7); // TODO: check this

    // TODO: better tests
  });

  // TESTS: randomWord(), randomWord(targetLength),
  // randomWord(pos), randomWord(pos, targetLength)

  test("testRandomWord(1)", function() {

    var result = lex.randomWord();
    ok(result.length > 0, "randomWord: " + result);

    result = lex.randomWord("nn");
    ok(result.length > 0, "randomWord nn: " + result);

    result = lex.randomWord("nns");
    ok(result.length > 0, "randomWord nns: " + result);

    result = lex.randomWord(3);
    ok(result.length > 0, "3 syllableCount: " + result);

    result = lex.randomWord(5);
    ok(result.length > 0, "5 syllableCount: " + result);

    var result = lex.randomWord("nns", 3);
    ok(result.length > 0, "3 syllableCount + nns: " + result);
  });

  test("testRandomWord(2)", function() {

    var pos = ["nn", "nns", "jj", "jjr", "wp"];
    for (var j = 0; j < pos.length; j++) {
      for (var i = 0; i < 5; i++) {
        var result = lex.randomWord(pos[j]);
        var best = lex._getBestPos(result);
        //console.log(result+": "+pos[j]+" ?= "+best);
        equal(pos[j], best, result);
      }
    }
  });

  test("testRandomWord(3)", function() {

    for (var i = 0; i < 10; i++) {
      var result = lex.randomWord(3);
      var syllables = RiTa.getSyllables(result);
      var num = syllables.split(RiTa.SYLLABLE_BOUNDARY).length;
      ok(result.length > 0);
      ok(num == 3, result + ": " + syllables); // "3 syllableCount: "
    }
  });

  test("testRandomWord(4)", function() {

    for (var i = 0; i < 10; i++) {
      var result = lex.randomWord(5);
      var syllables = RiTa.getSyllables(result);
      var num = syllables.split(RiTa.SYLLABLE_BOUNDARY).length;
      ok(result.length > 0); // "3 syllableCount: "
      ok(num == 5); // "3 syllableCount: "
    }

    // TODO: more tests with both count and pos
  });

  test("testWords", function() {

    var result = lex.words();
    ok(result.length > 1000);

    var result2 = lex.words(false);
    var result1 = lex.words(true);
    ok(result1.length > 1000);
    ok(result2.length > 1000);
    notDeepEqual(result1, result2);

    var result = lex.words("colou*r");
    ok(result.length > 5);

    var result = lex.words("[^A-M]in");
    ok(result.length > 5);

    var result1 = lex.words("colou*r", false);
    ok(result1.length > 5);

    var result2 = lex.words(false, "colou*r");
    ok(result2.length > 5);

    // make sure they are
    deepEqual(result1, result2);

    var result1 = lex.words("colou*r", true);
    ok(result1.length > 5);

    var result2 = lex.words(true, "colou*r");
    ok(result2.length > 5);

    // make sure they are
    notDeepEqual(result1, result2);
  });

  test("testIsAdverb", function() {

    ok(!lex.isAdverb("swim"));
    ok(!lex.isAdverb("walk"));
    ok(!lex.isAdverb("walker"));
    ok(!lex.isAdverb("beautiful"));
    ok(!lex.isAdverb("dance"));
    ok(!lex.isAdverb("dancing"));
    ok(!lex.isAdverb("dancer"));

    //verb
    ok(!lex.isAdverb("wash"));
    ok(!lex.isAdverb("walk"));
    ok(!lex.isAdverb("play"));
    ok(!lex.isAdverb("throw"));
    ok(!lex.isAdverb("drink"));
    ok(!lex.isAdverb("eat"));
    ok(!lex.isAdverb("chew"));

    //adj
    ok(!lex.isAdverb("wet"));
    ok(!lex.isAdverb("dry"));
    ok(!lex.isAdverb("furry"));
    ok(!lex.isAdverb("sad"));
    ok(!lex.isAdverb("happy"));

    //n
    ok(!lex.isAdverb("dogs"));
    ok(!lex.isAdverb("wind"));
    ok(!lex.isAdverb("dolls"));
    ok(!lex.isAdverb("frogs"));
    ok(!lex.isAdverb("ducks"));
    ok(!lex.isAdverb("flowers"));
    ok(!lex.isAdverb("fish"));

    //adv
    ok(lex.isAdverb("truthfully"));
    ok(lex.isAdverb("kindly"));
    ok(lex.isAdverb("bravely"));
    ok(lex.isAdverb("doggedly"));
    ok(lex.isAdverb("sleepily"));
    ok(lex.isAdverb("excitedly"));
    ok(lex.isAdverb("energetically"));
    ok(lex.isAdverb("hard")); // +adj

    ok(!lex.isAdverb(""));

    throws(function() {
      RiTa.SILENT = 1;
      try {
        lex.isAdverb("banana split");
        ok(!"failed");
      } catch (e) {
        throw e;
      }
      RiTa.SILENT = 0;
    });
  });

  test("testIsNoun", function() {

    ok(lex.isNoun("swim"));
    ok(lex.isNoun("walk"));
    ok(lex.isNoun("walker"));
    ok(lex.isNoun("dance"));
    ok(lex.isNoun("dancing"));
    ok(lex.isNoun("dancer"));

    //verb
    ok(lex.isNoun("wash")); //"TODO:also false in processing -> nn" shoulbe be both Verb and Noun
    ok(lex.isNoun("walk"));
    ok(lex.isNoun("play"));
    ok(lex.isNoun("throw"));
    ok(lex.isNoun("drink")); //TODO:"also false in processing -> nn" shoulbe be both Verb and Noun
    ok(!lex.isNoun("eat"));
    ok(!lex.isNoun("chew"));

    //adj
    ok(!lex.isNoun("hard"));
    ok(!lex.isNoun("dry"));
    ok(!lex.isNoun("furry"));
    ok(!lex.isNoun("sad"));
    ok(!lex.isNoun("happy"));
    ok(!lex.isNoun("beautiful"));

    //n
    ok(lex.isNoun("dogs"));
    ok(lex.isNoun("wind"));
    ok(lex.isNoun("dolls"));
    ok(lex.isNoun("frogs"));
    ok(lex.isNoun("ducks"));
    ok(lex.isNoun("flowers"));
    ok(lex.isNoun("fish"));
    ok(lex.isNoun("wet")); //+v/adj

    //adv
    ok(!lex.isNoun("truthfully"));
    ok(!lex.isNoun("kindly"));
    ok(!lex.isNoun("bravely"));
    ok(!lex.isNoun("scarily"));
    ok(!lex.isNoun("sleepily"));
    ok(!lex.isNoun("excitedly"));
    ok(!lex.isNoun("energetically"));

    ok(!lex.isNoun(""));

    throws(function() {
      RiTa.SILENT = 1;
      try {
        lex.isNoun("banana split");

      } catch (e) {
        throw e;
      }
      RiTa.SILENT = 0;
    });
  });

  test("testIsVerb", function() {

    ok(lex.isVerb("dance"));
    ok(lex.isVerb("swim"));
    ok(lex.isVerb("walk"));
    ok(!lex.isVerb("walker"));
    ok(!lex.isVerb("beautiful"));

    ok(lex.isVerb("dancing"));
    ok(!lex.isVerb("dancer"));

    //verb
    ok(lex.isVerb("eat"));
    ok(lex.isVerb("chew"));

    ok(lex.isVerb("throw")); // +n
    ok(lex.isVerb("walk")); // +n
    ok(lex.isVerb("wash")); // +n
    ok(lex.isVerb("drink")); // +n
    ok(lex.isVerb("ducks")); // +n
    ok(lex.isVerb("fish")); // +n
    ok(lex.isVerb("dogs")); // +n
    ok(lex.isVerb("wind")); // +n
    ok(lex.isVerb("wet")); // +adj
    ok(lex.isVerb("dry")); // +adj

    //adj
    ok(!lex.isVerb("hard"));
    ok(!lex.isVerb("furry"));
    ok(!lex.isVerb("sad"));
    ok(!lex.isVerb("happy"));

    //n
    ok(!lex.isVerb("dolls"));
    ok(!lex.isVerb("frogs"));
    ok(!lex.isVerb("flowers"));

    //adv
    ok(!lex.isVerb("truthfully"));
    ok(!lex.isVerb("kindly"));
    ok(!lex.isVerb("bravely"));
    ok(!lex.isVerb("scarily"));
    ok(!lex.isVerb("sleepily"));
    ok(!lex.isVerb("excitedly"));
    ok(!lex.isVerb("energetically"));

    throws(function() {
      RiTa.SILENT = 1;
      try {
        lex.isVerb("banana split");
      } catch (e) {
        throw e;
      }
      RiTa.SILENT = 0;

    });
  });


  test("testIsAdjective", function() {

    ok(!lex.isAdjective("swim"));
    ok(!lex.isAdjective("walk"));
    ok(!lex.isAdjective("walker"));
    ok(lex.isAdjective("beautiful"));
    ok(!lex.isAdjective("dance"));
    ok(!lex.isAdjective("dancing"));
    ok(!lex.isAdjective("dancer"));

    //verb
    ok(!lex.isAdjective("wash"));
    ok(!lex.isAdjective("walk"));
    ok(!lex.isAdjective("play"));
    ok(!lex.isAdjective("throw"));
    ok(!lex.isAdjective("drink"));
    ok(!lex.isAdjective("eat"));
    ok(!lex.isAdjective("chew"));

    //adj
    ok(lex.isAdjective("hard"));
    ok(lex.isAdjective("wet"));
    ok(lex.isAdjective("dry"));
    ok(lex.isAdjective("furry"));
    ok(lex.isAdjective("sad"));
    ok(lex.isAdjective("happy"));
    ok(lex.isAdjective("kindly")); //+adv

    //n
    ok(!lex.isAdjective("dogs"));
    ok(!lex.isAdjective("wind"));
    ok(!lex.isAdjective("dolls"));
    ok(!lex.isAdjective("frogs"));
    ok(!lex.isAdjective("ducks"));
    ok(!lex.isAdjective("flowers"));
    ok(!lex.isAdjective("fish"));

    //adv
    ok(!lex.isAdjective("truthfully"));
    ok(!lex.isAdjective("bravely"));
    ok(!lex.isAdjective("scarily"));
    ok(!lex.isAdjective("sleepily"));
    ok(!lex.isAdjective("excitedly"));
    ok(!lex.isAdjective("energetically"));


    throws(function() {
      RiTa.SILENT = 1;

      try {
        lex.isAdjective("banana split");

      } catch (e) {
        throw e;
      }
      RiTa.SILENT = 0;

    });
  });

  test("testIsAlliteration", function() {

    lex = RiLexicon();

    ok(lex.isAlliteration("apple", "polo"));
    ok(lex.isAlliteration("polo", "apple")); // swap position
    ok(lex.isAlliteration("POLO", "apple")); // CAPITAL LETTERS
    ok(lex.isAlliteration("POLO", "APPLE")); // CAPITAL LETTERS
    ok(lex.isAlliteration("polo", "APPLE")); // CAPITAL LETTERS
    ok(!lex.isAlliteration("polo ", "APPLE")); // Word with space False
    ok(!lex.isAlliteration("polo    ", "APPLE")); // Word with tab space False
    ok(lex.isAlliteration("this", "these"));
    ok(!lex.isAlliteration("solo", "tomorrow"));
    ok(!lex.isAlliteration("solo", "yoyo"));
    ok(!lex.isAlliteration("yoyo", "jojo"));
    ok(!lex.isAlliteration("", ""));
  });


  test("testIsRhyme", function() {

    ok(!lex.isRhyme("solo ", "tomorrow"));
    ok(!lex.isRhyme("apple", "polo"));
    ok(!lex.isRhyme("this", "these"));

    ok(lex.isRhyme("solo", "tomorrow"));
    ok(lex.isRhyme("tomorrow", "solo"));
    ok(lex.isRhyme("SOLO", "tomorrow"));
    ok(lex.isRhyme("solo", "TomorRow"));
    ok(lex.isRhyme("soLo", "toMORrow"));

    ok(lex.isRhyme("cat", "hat"));
    ok(lex.isRhyme("yellow", "mellow"));
    ok(lex.isRhyme("toy", "boy"));
    ok(lex.isRhyme("sieve", "give"));

    ok(!lex.isRhyme("solo   ", "tomorrow")); // Word with tab space
    ok(!lex.isRhyme("solo", "yoyo"));
    ok(!lex.isRhyme("yoyo", "jojo"));
    ok(!lex.isRhyme("", ""));

  });

  test("testSimilarByLetter", function() {

    var result = lex.similarByLetter("banana");
    deepEqual(result, ["banal", "bonanza", "cabana", "lantana", "manna", "wanna"]);

    result = lex.similarByLetter("banana", 1, true);
    deepEqual(result, ["cabana"]);

    result = lex.similarByLetter("banana", 1, false);
    deepEqual(result, ["banal", "bonanza", "cabana", "lantana", "manna", "wanna"]);

    result = lex.similarByLetter("tornado");
    deepEqual(result, ["torpedo"]);

    result = lex.similarByLetter("ice");
    deepEqual(result, ["ace", "dice", "iced", "icy", "ire", "lice", "mice", "nice", "rice", "vice"]);

    result = lex.similarByLetter("ice", 1);
    deepEqual(result, ["ace", "dice", "iced", "icy", "ire", "lice", "mice", "nice", "rice", "vice"]);

    result = lex.similarByLetter("ice", 2, true);
    ok(result.length > 10);

    result = lex.similarByLetter("ice", 0, true); // defaults to 1
    deepEqual(result, ["ace", "icy", "ire"]);

    result = lex.similarByLetter("ice", 1, true);
    deepEqual(result, ["ace", "icy", "ire"]);

    result = lex.similarByLetter("worngword");
    deepEqual(result, ["foreword", "wormwood"]);

    result = lex.similarByLetter("123");
    ok(result.length > 400);

    result = lex.similarByLetter("");
    deepEqual(result, []);
  });

  test("testSimilarBySound", function() {

    var result = lex.similarBySound("tornado");
    deepEqual(result, ["torpedo"]);

    result = lex.similarBySound("try");
    var answer = ["cry", "dry", "fry", "pry", "rye", "tie", "tray", "tree", "tribe", "tried", "tries", "tripe", "trite", "true", "wry"];
    deepEqual(result, answer);

    result = lex.similarBySound("try", 2);
    ok(result.length > answer.length); // more

    result = lex.similarBySound("happy");
    answer = ["happier", "hippie"];
    deepEqual(result, answer);

    result = lex.similarBySound("happy", 2);
    ok(result.length > answer.length); // more

    result = lex.similarBySound("cat");
    answer = ["at", "bat", "cab", "cache", "calf", "can", "cant", "cap", "capped", "cash", "cashed", "cast", "caste", "catch", "catty", "caught", "chat", "coat", "cot", "curt", "cut", "fat", "hat", "kit", "kite", "mat", "matt", "matte", "pat", "rat", "sat", "tat", "that"];
    deepEqual(result, answer);

    result = lex.similarBySound("cat", 2);
    ok(result.length > answer.length);

    var result = lex.similarBySound("worngword");
    deepEqual(result, ["watchword", "wayward", "wormwood"]);
  });

  test("testSimilarBySoundAndLetter", function() {

    var result = lex.similarBySoundAndLetter("try");
    deepEqual(result, ["cry", "dry", "fry", "pry", "tray", "wry"]);

    result = lex.similarBySoundAndLetter("daddy");
    deepEqual(result, ["dandy"]);

    result = lex.similarBySoundAndLetter("banana");
    deepEqual(result, ["bonanza"]);

    result = lex.similarBySoundAndLetter("worngword");
    deepEqual(result, ["wormwood"]);

    result = lex.similarBySoundAndLetter("");
    deepEqual(result, []);
  });

  test("testSubstrings", function() {

    var result = lex.substrings("thousand");
    var answer = ["sand", "thou"];
    deepEqual(result, answer);

    var result = lex.substrings("thousand", 2);
    var answer = ["an", "and", "ho", "sand", "thou", "us"];
    deepEqual(result, answer);

    var result = lex.substrings("banana", 1);
    var answer = ["a", "an", "b", "ban", "n", "na"];
    deepEqual(result, answer);

    var result = lex.substrings("thousand", 3);
    var answer = ["and", "sand", "thou"];
    deepEqual(result, answer);

    var result = lex.substrings("thousand"); // min-length=4
    var answer = ["sand", "thou"];
    deepEqual(result, answer);

    var result = lex.substrings("");
    var answer = [];
    deepEqual(result, answer);

  });

  test("testSuperstrings", function() {

    var result = lex.superstrings("superm");
    var answer = ["supermarket", "supermarkets"];
    deepEqual(result, answer);

    var result = lex.superstrings("puni");
    var answer = ["impunity", "punish", "punishable", "punished", "punishes", "punishing", "punishment", "punishments", "punitive", "unpunished"];
    deepEqual(result, answer);

    var result = lex.superstrings("");
    var answer = [""];
    ok(result.length > 1000);

  });

  test("testGetPosData", function() {

    var result = lex._getPosData("box");
    deepEqual(result, "nn vb");

    var result = lex._getPosData("there");
    deepEqual(result, "ex rb uh");

    var result = lex._getPosData("is");
    deepEqual(result, "vbz rb nns vbp");

    var result = lex._getPosData("a");
    deepEqual(result, "dt");

    var result = lex._getPosData("beautiful");
    deepEqual(result, "jj");

    //Empty String
    var result = lex._getPosData(".");
    deepEqual(result, "");

    var result = lex._getPosData("beautiful guy");
    deepEqual(result, "");
  });

  test("testIsVowel", function() {

    ok(lex._isVowel("a"));
    ok(lex._isVowel("e"));
    ok(lex._isVowel("i"));
    ok(lex._isVowel("o"));
    ok(lex._isVowel("u"));

    ok(!lex._isVowel("y"));
    ok(!lex._isVowel("v"));
    ok(!lex._isVowel("3"));
    ok(!lex._isVowel(""));
  });

  test("testIsConsonant", function() {

    ok(!lex._isConsonant("vv"));
    ok(!lex._isConsonant(""));

    ok(lex._isConsonant("v"));
    ok(lex._isConsonant("b"));
    ok(lex._isConsonant("d"));
    ok(lex._isConsonant("q"));


    ok(!lex._isConsonant("a"));
    ok(!lex._isConsonant("e"));
    ok(!lex._isConsonant("i"));
    ok(!lex._isConsonant("o"));
    ok(!lex._isConsonant("uu"));

    ok(!lex._isConsonant("3"));

  });

  test("testLookupRaw", function() {

    var result = lex._lookupRaw("banana");
    deepEqual(result, ["b-ax-n ae1-n ax", "nn"]);

    var result = lex._lookupRaw("sand");
    deepEqual(result, ["s-ae1-n-d", "nn"]);

    var result = lex._lookupRaw("hex");
    deepEqual(result, undefined);

    var result = lex._lookupRaw("there is");
    deepEqual(result, undefined);

    var result = lex._lookupRaw("ajj");
    deepEqual(result, undefined);
  });

  //For RiTa.getPhonemes() NOT IN RiTa-Java

  test("testGetPhonemes", function() {


    var result = lex._getPhonemes("The");
    var answer = "dh-ax";
    equal(result, answer);

    var result = lex._getPhonemes("The.");
    var answer = "dh-ax";
    equal(result, answer);

    var result = lex._getPhonemes("The boy jumped over the wild dog.");
    var answer = "dh-ax b-oy jh-ah-m-p-t ow-v-er dh-ax w-ay-l-d d-ao-g";
    equal(result, answer);

    var result = lex._getPhonemes("The boy ran to the store.");
    var answer = "dh-ax b-oy r-ae-n t-uw dh-ax s-t-ao-r";
    equal(result, answer);

    var result = lex._getPhonemes("");
    var answer = "";
    equal(result, answer);

  });

  //For RiTa.getStresses() NOT IN RiTa-Java

  test("testGetStresses", function() {

    var result = lex._getStresses("The emperor had no clothes on");
    var answer = "0 1/0/0 1 1 1 1";
    equal(result, answer);

    var result = lex._getStresses("The emperor had no clothes on.");
    var answer = "0 1/0/0 1 1 1 1";
    equal(result, answer);

    var result = lex._getStresses("The emperor had no clothes on. The King is fat.");
    var answer = "0 1/0/0 1 1 1 1 0 1 1 1";
    equal(result, answer);

    var result = lex._getStresses("to preSENT, to exPORT, to deCIDE, to beGIN");
    var answer = "1 0/1 1 0/1 1 0/1 1 0/1";
    equal(result, answer);

    var result = lex._getStresses("to present, to export, to decide, to begin");
    var answer = "1 0/1 1 0/1 1 0/1 1 0/1";
    equal(result, answer);

    var result = lex._getStresses("");
    var answer = "";
    equal(result, answer);
  });

  //For RiTa.getSyllables() NOT IN RiTa-Java

  test("testGetSyllables", function() {

    var result = lex._getSyllables("The emperor had no clothes on.");
    var answer = "dh-ax eh-m-p/er/er hh-ae-d n-ow k-l-ow-dh-z aa-n";
    equal(result, answer);

    var result = lex._getSyllables("@#$%*()");
    var answer = "";
    equal(result, answer);

    var result = lex._getSyllables("");
    var answer = "";
    equal(result, answer);
  });

  test("RiLexicon-lookups", function() {

    ok(typeof lex.data != 'undefined');

    ok(lex._getPhonemes('gonna'));
    ok(lex._getPhonemes('gotta'));

    ok(lex.isRhyme("cat", "hat"));
    ok(!lex.isRhyme("cat", "dog"));
    ok(lex.isAlliteration("cat", "kill"));
    ok(!lex.isAlliteration("cat", "dog"));
  });

  test("RiLexicon-gets", function() {

    var word = "aberrations";
    var output1 = lex._getSyllables(word);
    var output2 = lex._getPhonemes(word);
    var output3 = lex._getStresses(word);

    var expected1 = "ae-b/er/ey-sh/ax-n-z";
    equal(output1, expected1);

    var expected2 = "ae-b-er-ey-sh-ax-n-z";
    equal(output2, expected2);

    var expected3 = "1/0/1/0";
    equal(output3, expected3);
  });

  // below modify the lexicon.data field ================================

  test("testAddWord", function() {

    var result = lex.addWord("bananana", "b-ax-n ae1-n ax ax", "nn");
    ok(lex.containsWord("bananana"));

    lex.addWord("hehehe", "hh-ee1 hh-ee1 hh-ee1", "uh");
    ok(lex.containsWord("hehehe"));

    equal(lex._getPhonemes("hehehe"), "hh-ee-hh-ee-hh-ee");

    lex.addWord("HAHAHA", "hh-aa1 hh-aa1 hh-aa1", "uh");
    ok(lex.containsWord("HAHAHA"));

    lex.addWord("HAHAHA", "hh-aa1 hh-aa1 hh-aa1", "uh");
    equal(lex._getPhonemes("HAHAHA"), "hh-aa-hh-aa-hh-aa");

    lex = RiLexicon(); // restore global
  });

  test("testClear", function() {

    ok(lex.containsWord("banana"));
    lex.removeWord("banana");

    ok(!lex.containsWord("banana"));

    var obj = {};
    obj["wonderfullyy"] = ["w-ah1-n-d er-f ax-l iy", "rb"];
    lex.lexicalData(obj);
    var result = lex.lexicalData();
    deepEqual(result, obj)

    lex.clear();
    ok(lex.size() < 1);

    lex = RiLexicon(); // restore global
  });

  test("testRemoveWord", function() {

    ok(lex.containsWord("banana"));
    lex.removeWord("banana");
    ok(!lex.containsWord("banana"));

    lex.removeWord("a");
    ok(!lex.containsWord("a"));
    ok(lex.containsWord("are")); //check that others r still there
    lex.removeWord("aaa");
    ok(!lex.containsWord("aaa"));

    lex.removeWord("");

    ok(!lex._getPhonemes("banana"));

    lex = RiLexicon(); // restore global
  });

  test("testReload", function() {

    lex = RiLexicon();
    var originalSize = lex.size();

    ok(lex.containsWord("are"));
    lex.removeWord("are");
    ok(!lex.containsWord("are"));
    var removeOneWordSize = lex.size();
    ok(lex.size()===originalSize-1);

    lex.reload();
    ok(lex.size() === originalSize);

    ok(lex.containsWord("cat"));
    lex.removeWord("cat");
    ok(!lex.containsWord("cat"));
    ok(lex.size()===originalSize-1);

    ok(lex.containsWord("are"));
    lex.removeWord("are");
    ok(!lex.containsWord("are"));
    ok(lex.size()===originalSize-2);

    lex.reload();
    ok(lex.size() === originalSize);
    ok(lex.containsWord("are"));
    ok(lex.containsWord("cat"));

    lex = RiLexicon(); // restore global
  });

  test("testLexicalData", function() {

    var result = lex.lexicalData();
    ok(Object.keys(result).length > 1000);

    var re = lex.lexicalData();
    result = re.a;
    var answer = ["ey1", "dt"];

    deepEqual(result, answer);

    re = lex.lexicalData();
    result = re.the;
    answer = ["dh-ax", "dt"];

    deepEqual(result, answer);

    var obj = {};
    obj["wonderfully"] = ["w-ah1-n-d er-f ax-l iy", "rb"];
    result = lex.lexicalData(obj);
    deepEqual(result.lexicalData(), obj);

    obj = {};
    obj["wonderfullyy"] = ["w-ah1-n-d er-f ax-l iy-y", "rb"];
    result = lex.lexicalData(obj);
    deepEqual(result.lexicalData().wonderfullyy, ["w-ah1-n-d er-f ax-l iy-y", "rb"]);
  });
}

if (typeof exports != 'undefined') runtests();
