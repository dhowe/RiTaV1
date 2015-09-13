/*global console, test, throws, equal, fail, deepEqual, notEqual, expect, require, ok,
    QUnit, RiTa, RiTaEvent, RiString, RiGrammar, RiMarkov, RiLexicon */

/*jshint loopfunc: true */

var testResults = [{
  testName: 'testGetSyllables',
  testMethod: 'RiTa.getSyllables', // static
  testClass: 'RiTa',
  assertion: 'equal',
  tests: [{
    input: '',
    output: ''
  }, {
    input: 'The Laggin Dragon',
    output: 'dh-ax l-ae/g-ih-n d-r-ae-g/aa-n'
  }, {
    input: 'The emperor had no clothes on.',
    output: 'dh-ax eh-m-p/er/er hh-ae-d n-ow k-l-ow-dh-z aa-n .'
  }, {
    input: 'The dog ran faster than the other dog. But the other dog was prettier.',
    output: 'dh-ax d-ao-g r-ae-n f-ae-s/t-er dh-ae-n dh-ax ah-dh/er d-ao-g . b-ah-t dh-ax ah-dh/er d-ao-g w-aa-z p-r-ih-t/iy/er .'
  }, {
    input: '@#$%&*()',
    output: '@ # $ % ae-n-d * ( )'
  }]
}];

var runtests = function() {

    RiTa.SILENT = 1;

    var filePath = (typeof module != 'undefined' && module.exports) ? "./test/html/data/" : "./data/"

    QUnit.module("RiTa", {

      setup: function() {},
      teardown: function() {}
    });

    // ------------------------------------------------------------------------
    test("testGetSyllables", function() { // new-style

      if (noLexicon()) return;

      var func = RiTa.getSyllables,
        tests = testResults[0].tests;

      for (var i = 0, len = tests.length; i < len; i++) {
        equal(func(tests[i].input), tests[i].output);
      }
    });

    test("testGetSyllablesOrig", function() { // old-style

      var result, txt, answer;

      result = RiTa.getSyllables('');
      answer = '';
      equal(result, answer);

      if (noLexicon()) return;

      txt = 'The dog ran faster than the other dog. But the other dog was prettier.';
      result = RiTa.getSyllables(txt);
      answer = 'dh-ax d-ao-g r-ae-n f-ae-s/t-er dh-ae-n dh-ax ah-dh/er d-ao-g . b-ah-t dh-ax ah-dh/er d-ao-g w-aa-z p-r-ih-t/iy/er .';
      equal(result, answer);

      txt = 'The emperor had no clothes on.';
      result = RiTa.getSyllables(txt);
      answer = 'dh-ax eh-m-p/er/er hh-ae-d n-ow k-l-ow-dh-z aa-n .';
      equal(result, answer);

      txt = 'The Laggin Dragon';
      result = RiTa.getSyllables(txt);
      answer = 'dh-ax l-ae/g-ih-n d-r-ae-g/aa-n';
      equal(result, answer);

      result = RiTa.getSyllables('@#$%&*()');
      answer = '@ # $ % ae-n-d * ( )';
      equal(result, answer);
    });

    // ------------------------------------------------------------------------

    test("testConstants", function() {

      ok(RiTa.VERSION);
    });

    test("testMinEditDistance", function() {

      // testMinEditDistanceArray()
      var arr1 = ['The', 'dog', 'ate'], arr2 = ['The', 'cat', 'ate'];
      equal(RiTa.minEditDistance(arr1, arr2, false), 1);
      equal(RiTa.minEditDistance(arr1, arr2, true), 1 / 3.0);

      var arr1 = ['The', 'dog', 'ate'], arr2 = [];
      equal(RiTa.minEditDistance(arr1, arr2, false), 3);
      equal(RiTa.minEditDistance(arr1, arr2, true), 1);

      arr1 = ["fefnction", "intention", "ate"];
      arr2 = ["faunctional", "execution", "ate"];
      equal(RiTa.minEditDistance(arr1, arr2, false), 2);
      equal(RiTa.minEditDistance(arr1, arr2, true), 2 / 3);

      // test testMinEditDistanceString()
      var arr1 = 'The dog', arr2 = 'The cat';
      equal(RiTa.minEditDistance(arr1, arr2, false), 3);
      equal(RiTa.minEditDistance(arr1, arr2, true), 3 / 7);

      var arr1 = 'The dog', arr2 = '';
      equal(RiTa.minEditDistance(arr1, arr2, false), 7);
      equal(RiTa.minEditDistance(arr1, arr2, true), 1);

      arr1 = "fefnction"; arr2 = "faunctional";
      equal(RiTa.minEditDistance(arr1, arr2, false), 4);
      equal(RiTa.minEditDistance(arr1, arr2, true), 4 / 11);

      arr1 = "intention"; arr2 = "execution";
      equal(RiTa.minEditDistance(arr1, arr2, false), 5);
      equal(RiTa.minEditDistance(arr1, arr2, true), 5 / 9);
    });

    test("testEnv", function() {
      var mode = RiTa.env();
      var inNode = (typeof module != 'undefined' && module.exports);
      inNode && ok(mode == RiTa.NODE);

      var inBrowser = typeof window != 'undefined';
      inBrowser && ok(mode == RiTa.JS);
    });

    test("testIsAbbreviation", function() {

      ok(RiTa.isAbbreviation("Dr."));
      ok(RiTa.isAbbreviation("dr."));
      //T in processing

      ok(!RiTa.isAbbreviation("DR."));
      // F in Processing.lowercase is true but uppercase is false
      ok(!RiTa.isAbbreviation("Dr. "));
      //space
      ok(!RiTa.isAbbreviation(" Dr."));
      //space
      ok(!RiTa.isAbbreviation("  Dr."));
      //double space
      ok(!RiTa.isAbbreviation("Dr.  "));
      //double space
      ok(!RiTa.isAbbreviation("   Dr."));
      //tab space
      ok(!RiTa.isAbbreviation("Dr.    "));
      //tab space
      ok(!RiTa.isAbbreviation("Dr"));
      ok(!RiTa.isAbbreviation("Doctor"));
      ok(!RiTa.isAbbreviation("Doctor."));

      ok(RiTa.isAbbreviation("Prof."));
      ok(RiTa.isAbbreviation("prof."));
      //T in processing
      ok(!RiTa.isAbbreviation("PRFO."));
      //  F in Processing. lowercase is true but uppercase is false
      ok(!RiTa.isAbbreviation("PrFo."));
      //  F in Processing. lowercase is true but uppercase is false
      ok(!RiTa.isAbbreviation("Professor"));
      ok(!RiTa.isAbbreviation("professor"));
      ok(!RiTa.isAbbreviation("PROFESSOR"));
      ok(!RiTa.isAbbreviation("Professor."));

      ok(!RiTa.isAbbreviation("@#$%^&*()"));

      ok(!RiTa.isAbbreviation(""));
      ok(!RiTa.isAbbreviation(null));
      ok(!RiTa.isAbbreviation(undefined));
      ok(!RiTa.isAbbreviation(1));
    });

    test("testIsQuestion", function() {

      ok(RiTa.isQuestion("what"));
      ok(RiTa.isQuestion("what"));
      ok(RiTa.isQuestion("what is this"));
      ok(RiTa.isQuestion("what is this?"));
      ok(RiTa.isQuestion("Does it?"));
      ok(RiTa.isQuestion("Is this yours?"));

      ok(RiTa.isQuestion("Are you done?"));
      // if "is" is true, "Are" should also be True (NICE!)

      ok(RiTa.isQuestion("what is  this?"));
      //extra space
      ok(RiTa.isQuestion(" what is this? "));
      //extra space
      ok(RiTa.isQuestion("what is   this?"));
      //extra double space
      ok(RiTa.isQuestion("what    is  this?"));
      //extra tab
      ok(RiTa.isQuestion("what is this? , where is that?"));
      ok(!RiTa.isQuestion("That is not a toy This is an apple"));
      ok(!RiTa.isQuestion("string"));
      ok(!RiTa.isQuestion("?"));
      ok(!RiTa.isQuestion(""));
    });


    test("testIsSentenceEnd", function() {

      var words = 'The dog ate the small baby. Then it threw up.'.split(' ');
      ok(RiTa.isSentenceEnd(words[5], words[6])); // true
      ok(!RiTa.isSentenceEnd(words[3], words[4])); // false
      ok(!RiTa.isSentenceEnd(words[6], words[7])); // false
      ok(!RiTa.isSentenceEnd('', '')); // false

      // TODO: needs more tests
    });


    test("testIsW_Question", function() {

      ok(RiTa.isW_Question("What the"));
      ok(RiTa.isW_Question("What is it"));
      ok(RiTa.isW_Question("how is it?"));
      ok(RiTa.isW_Question("will is it."));
      ok(RiTa.isW_Question("Where is it?"));
      ok(RiTa.isW_Question("How is it."));

      ok(!RiTa.isW_Question("Does it?"));
      ok(!RiTa.isW_Question("Is this yours?"));
      ok(!RiTa.isW_Question("Are you done?"));
      ok(!RiTa.isW_Question(""));

    });


    test("testRandomItem", function() {

      var toks = RiTa.tokenize("The boy, dressed in red, ate an apple.!?");
      for (var i = 0; i < toks.length * 2; i++) {
        ok(RiTa.randomItem(toks));
      }

      toks = RiTa.tokenize("The quick brown fox jumps over the lazy dog.");
      for (var i = 0; i < toks.length * 2; i++) {
        ok(RiTa.randomItem(toks));
      }

      toks = RiTa.tokenize("123 123 1 2 3 1,1 1.1 23.45.67 22/05/2012 12th May,2012");
      for (var i = 0; i < toks.length * 2; i++) {
        ok(RiTa.randomItem(toks));
      }
    });

    test("testRandomOrdering", function() {

      var result = RiTa.randomOrdering(5);
      equal(result.length, 5);

      var result = RiTa.randomOrdering(50);
      equal(result.length, 50);

      var obj = {};
      for (var i = 0; i < result.length; i++) {
        ok(!obj.hasOwnProperty(i + ''));
        obj[i + ''] = i + '';
      }
    });

    test("testSplitSentences", function() {

      // TODO: check Penn-Treebank splitting rules
      var input = "Stealth's Open Frame, OEM style LCD monitors are designed for special mounting applications. The slim profile packaging provides an excellent solution for building into kiosks, consoles, machines and control panels. If you cannot find an off the shelf solution call us today about designing a custom solution to fit your exact needs.";
      var expected = ["Stealth's Open Frame, OEM style LCD monitors are designed for special mounting applications.", "The slim profile packaging provides an excellent solution for building into kiosks, consoles, machines and control panels.", "If you cannot find an off the shelf solution call us today about designing a custom solution to fit your exact needs."];
      var output = RiTa.splitSentences(input);
      deepEqual(output, expected);

      var input = "\"The boy went fishing.\", he said. Then he went away.";
      var expected = ["\"The boy went fishing.\", he said.", "Then he went away."];
      var output = RiTa.splitSentences(input);
      deepEqual(output, expected);

      var input = "The dog";
      var output = RiTa.splitSentences(input);
      deepEqual(output, [input]);

      var input = "I guess the dog ate the baby.";
      var output = RiTa.splitSentences(input);
      deepEqual(output, [input]);

      var input = "Oh my god, the dog ate the baby!";
      var output = RiTa.splitSentences(input);
      var expected = ["Oh my god, the dog ate the baby!"];
      deepEqual(output, expected);

      var input = "Which dog ate the baby?"
      var output = RiTa.splitSentences(input);
      var expected = ["Which dog ate the baby?"];
      deepEqual(output, expected);

      var input = "'Yes, it was a dog that ate the baby', he said."
      var output = RiTa.splitSentences(input);
      var expected = ["\'Yes, it was a dog that ate the baby\', he said."];
      deepEqual(output, expected);

      deepEqual(RiTa.splitSentences(""), [""]);
    });


    test("testStripPunctuation", function() {

      //strip/trimPunctuation "����������`',;:!?)([].#\"\\!@$%&}<>|+=-_\\/*{^
      var res = RiTa.stripPunctuation("$%He%^&ll,o,");
      equal(res, "Hello");

      equal(RiTa.stripPunctuation(""), "");

      equal(RiTa.stripPunctuation("Hel_lo"), "Hello");
      equal(RiTa.stripPunctuation("Hel;lo"), "Hello");
      equal(RiTa.stripPunctuation("Hel:lo"), "Hello");
      equal(RiTa.stripPunctuation("Hel'lo"), "Hello");
      equal(RiTa.stripPunctuation("Hel/lo"), "Hello");
      equal(RiTa.stripPunctuation("Hel\"lo"), "Hello");
      equal(RiTa.stripPunctuation("Hel-lo"), "Hello");
      equal(RiTa.stripPunctuation("Hel`lo"), "Hello");
      equal(RiTa.stripPunctuation("Hel?lo"), "Hello");
      equal(RiTa.stripPunctuation("Hel.lo"), "Hello");
      equal(RiTa.stripPunctuation("Hel+lo"), "Hello");
      equal(RiTa.stripPunctuation("Hel*lo"), "Hello");
      equal(RiTa.stripPunctuation("Hel&lo"), "Hello");
      equal(RiTa.stripPunctuation("Hel$lo"), "Hello");
      equal(RiTa.stripPunctuation("Hel(lo"), "Hello");
      equal(RiTa.stripPunctuation("Hel)lo"), "Hello");
      equal(RiTa.stripPunctuation("Hel@lo"), "Hello");
      equal(RiTa.stripPunctuation("Hel[lo"), "Hello");
      equal(RiTa.stripPunctuation("Hel]lo"), "Hello");
      equal(RiTa.stripPunctuation("Hel{lo"), "Hello");
      equal(RiTa.stripPunctuation("Hel}lo"), "Hello");
      equal(RiTa.stripPunctuation("Hel\\lo"), "Hello");
      equal(RiTa.stripPunctuation("Hel%lo"), "Hello");
      equal(RiTa.stripPunctuation("Hel~lo"), "Hello");
      equal(RiTa.stripPunctuation("Hel:lo"), "Hello");
      equal(RiTa.stripPunctuation("Hel;lo"), "Hello");
      equal(RiTa.stripPunctuation("Hel<lo"), "Hello");
      equal(RiTa.stripPunctuation("Hel>lo"), "Hello");
      equal(RiTa.stripPunctuation("Hel^lo"), "Hello");
      equal(RiTa.stripPunctuation("Hel|lo"), "Hello");

      var res = RiTa.stripPunctuation("\"\\!@$%&}<>|+=-_\\/*{^He&^ll,o\!@$%&}<>|+=-_\\/*{^");
      equal(res, "Hello");

    });


    test("testTrimPunctuation", function() {

      var res = RiTa.trimPunctuation("$%He&^ll,o,");
      equal(res, "He&^ll,o");

      // fix these strange characters
      var res = RiTa.trimPunctuation("����������`He&^ll,o\!@$%&}<>|+=-_\\/*{^");
      equal(res, "He&^ll,o");

      var res = RiTa.trimPunctuation("\"\\!@$%&}<>|+=-_\\/*{^He&^ll,o\!@$%&}<>|+=-_\\/*{^");
      equal(res, "He&^ll,o");

      deepEqual(RiTa.trimPunctuation(""), "");

      deepEqual(RiTa.trimPunctuation(1234), 1234);
    });

    test("testIsPunctuation", function() {

      ok(!RiTa.isPunctuation("What the"));
      ok(!RiTa.isPunctuation("What ! the"));
      ok(!RiTa.isPunctuation(".#\"\\!@i$%&}<>"));

      ok(RiTa.isPunctuation("!"));

      ok(!RiTa.isPunctuation("! "));
      //space
      ok(!RiTa.isPunctuation(" !"));
      //space
      ok(!RiTa.isPunctuation("!  "));
      //double space
      ok(!RiTa.isPunctuation("  !"));
      //double space
      ok(!RiTa.isPunctuation("!  "));
      //tab space
      ok(!RiTa.isPunctuation("   !"));
      //tab space
      ok(RiTa.isPunctuation("?"));
      ok(RiTa.isPunctuation("?!"));
      ok(RiTa.isPunctuation("."));
      ok(RiTa.isPunctuation(".."));
      ok(RiTa.isPunctuation("..."));
      ok(RiTa.isPunctuation("...."));
      ok(RiTa.isPunctuation("%..."));
      var punct = '$%&^,';
      for (var i = 0; i < punct.length; i++) {
        ok(RiTa.isPunctuation(punct[i]));
      }

      var punct = ",;:!?)([].#\"\\!@$%&}<>|+=-_\\/*{^";
      for (var i = 0; i < punct.length; i++) {
        ok(RiTa.isPunctuation(punct[i]), punct[i]);
      }

      // TODO: also test multiple characters strings here ****
      var punct = "\"��������`'";
      for (var i = 0; i < punct.length; i++) {
        ok(RiTa.isPunctuation(punct[i]), punct[i]);
      }

      var punct = "\"��������`',;:!?)([].#\"\\!@$%&}<>|+=-_\\/*{^";
      for (var i = 0; i < punct.length; i++) {
        ok(RiTa.isPunctuation(punct[i]), punct[i]);
      }

      // TODO: and here...
      var nopunct = 'Helloasdfnals  FgG   \t kjdhfakjsdhf askjdfh aaf98762348576';
      for (var i = 0; i < nopunct.length; i++) {
        ok(!RiTa.isPunctuation(nopunct[i]), nopunct[i]);
      }

      ok(!RiTa.isPunctuation(""));

    });

    test("testTokenize", function() {

      var input = "The boy, dressed in red, ate an apple.";
      var expected = ["The", "boy", ",", "dressed", "in", "red", ",", "ate", "an", "apple", "."];
      var output = RiTa.tokenize(input);
      deepEqual(output, expected);

      var input = "The boy screamed, 'Where is my apple?'";
      //'Where is one word here, same result as Processing (TODO: THIS IS INCORRECT IN BOTH)
      var expected = ["The", "boy", "screamed", ",", "'Where", "is", "my", "apple", "?", "'"];
      var output = RiTa.tokenize(input);
      deepEqual(output, expected);

      var input = "why? Me?huh?!";
      var expected = ["why", "?", "Me", "?", "huh", "?", "!"];
      var output = RiTa.tokenize(input);
      deepEqual(output, expected);

      var input = "123 123 1 2 3 1,1 1.1 23.45.67 22/05/2012 12th May,2012";
      var expected = ["123", "123", "1", "2", "3", "1", ",", "1", "1", ".", "1", "23", ".", "45", ".", "67", "22/05/2012", "12th", "May", ",", "2012"];
      var output = RiTa.tokenize(input);
      deepEqual(output, expected);

      // TODO: check Penn-Treebank tokenizer rules & add some more edge cases
      var inputs = ["A simple sentence.", "that's why this is our place).", ];
      var outputs = [
        ["A", "simple", "sentence", "."],
        ["that's", "why", "this", "is", "our", "place", ")", "."],
      ];

      ok(inputs.length == outputs.length);

      for (var i = 0; i < inputs.length; i++) {
        var result = RiTa.tokenize(inputs[i]);
        deepEqual(result, outputs[i]);
      }

      deepEqual(RiTa.tokenize(""), [""]);

      var input = "Dr. Chan is talking slowly with Mr. Cheng, and they're friends."; // strange but same as RiTa-java
      //var expected = ["Dr.", "Chan", "is", "talking", "slowly", "with", "Mr.", "Cheng", ",", "and", "they're", "friends", "."];
      var expected = ["Dr", ".", "Chan", "is", "talking", "slowly", "with", "Mr", ".", "Cheng", ",", "and", "they're", "friends", "."];
      var output = RiTa.tokenize(input);
      deepEqual(output, expected);

      var txt = "The dog";
      var words = RiTa.tokenize(txt);
      deepEqual(words, ["The", "dog"]);

      var words = RiTa.tokenize("closed");
      deepEqual(words, ["closed"]);
    });

    test("testDistance", function() {

      equal(1, RiTa.distance(1, 3, 2, 3));
      equal(28, RiTa.distance(30, 1, 2, 1));
      equal(5.656854249492381, RiTa.distance(0, 0, 4, 4));
      equal(5.0990195135927845, RiTa.distance(3, 3, 8, 4));
    });

    test("testRandom", function() {

      // float random()
      var answer = RiTa.random();
      ok(answer >= 0, answer);
      var answer2 = RiTa.random();
      ok(answer2 < 1, answer2);

      // int random(int max)
      var answer = RiTa.random(50);
      ok(answer >= 0);
      var answer2 = RiTa.random(50);
      ok(answer2 < 50);

      answer = RiTa.random(1);
      ok(answer >= 0);
      answer2 = RiTa.random(1);
      ok(answer2 < 1);

      answer = RiTa.random(2);
      ok(answer >= 0);
      answer2 = RiTa.random(2);
      ok(answer2 < 2);

      answer = RiTa.random(0);
      ok(answer == 0);

      // int random(int min, int max)
      var answer = RiTa.random(10, 34);
      ok(answer >= 10);
      var answer2 = RiTa.random(10, 34);
      ok(answer2 < 34);

      answer = RiTa.random(1, 2);
      ok(answer >= 1);
      answer2 = RiTa.random(1, 2);
      ok(answer2 < 2);

      answer = RiTa.random(1, 3);
      ok(answer >= 1);
      answer2 = RiTa.random(1, 3);
      ok(answer2 < 3);

      answer = RiTa.random(0, 0);
      ok(answer == 0);

      answer = RiTa.random(5, 1); //"min > max"
      ok(answer >= 1);
      answer2 = RiTa.random(0, 0);
      ok(answer2 < 5);

      // float random(float max)
      var answer = RiTa.random(12.3);
      ok(answer >= 0);
      var answer2 = RiTa.random(12.3);
      ok(answer2 < 12.3);

      answer = RiTa.random(1.1);
      ok(answer >= 0);
      answer2 = RiTa.random(1.1);
      ok(answer2 < 1.1);

      answer = RiTa.random(1.2);
      ok(answer >= 0);
      answer2 = RiTa.random(1.2);
      ok(answer2 < 1.2);

      answer = RiTa.random(0);
      ok(answer == 0);

      // float random(float min, float max)

      var answer = RiTa.random(3.4, 3.6);
      ok(answer >= 3.4);
      var answer2 = RiTa.random(3.4, 3.6);
      ok(answer2 < 3.6);

      answer = RiTa.random(1.1, 1.2);
      ok(answer >= 1.1);
      answer2 = RiTa.random(1.1, 1.2);
      ok(answer2 < 1.2);

      answer = RiTa.random(1.1, 1.3);
      ok(answer >= 1.1);
      answer2 = RiTa.random(1.1, 1.3);
      ok(answer2 < 1.3);

      answer = RiTa.random(0, 0);
      ok(answer == 0);

      answer = RiTa.random(5.1, 1.1); //TODO   "min > max"
      ok(answer >= 1.1);
      answer2 = RiTa.random(0, 0);
      ok(answer2 < 5.1);
    });

    test("testGetPhonemes", function() {

      var result = RiTa.getPhonemes("");
      var answer = "";
      equal(result, answer);

      if (noLexicon()) return;

      var result = RiTa.getPhonemes("The");
      var answer = "dh-ax";
      equal(result, answer);

      var result = RiTa.getPhonemes("The.");
      var answer = "dh-ax .";
      equal(result, answer);

      var result = RiTa.getPhonemes("The boy jumped over the wild dog.");
      var answer = "dh-ax b-oy jh-ah-m-p-t ow-v-er dh-ax w-ay-l-d d-ao-g .";
      equal(result, answer);

      var result = RiTa.getPhonemes("The boy ran to the store.");
      var answer = "dh-ax b-oy r-ae-n t-uw dh-ax s-t-ao-r .";
      equal(result, answer);

      var txt = "The dog ran faster than the other dog.  But the other dog was prettier.";
      var result = RiTa.getPhonemes(txt);
      var answer = "dh-ax d-ao-g r-ae-n f-ae-s-t-er dh-ae-n dh-ax ah-dh-er d-ao-g . b-ah-t dh-ax ah-dh-er d-ao-g w-aa-z p-r-ih-t-iy-er .";
      equal(result, answer);
    });


    test("testGetPosTags", function() {

      var result = RiTa.getPosTags("mammal");
      var answer = ["nn"];
      deepEqual(result, answer);


      var result = RiTa.getPosTags("asfaasd");
      var answer = ["nn"];
      deepEqual(result, answer);

      var result = RiTa.getPosTags("innings");
      var answer = ["nns"];
      deepEqual(result, answer);

      var result = RiTa.getPosTags("clothes");
      var answer = ["nns"];
      deepEqual(result, answer);

      if (noLexicon()) return;

      var result = RiTa.getPosTags("the boy dances");
      var answer = ["dt", "nn", "vbz"];
      deepEqual(result, answer);

      var result = RiTa.getPosTags("he dances");
      var answer = ["prp", "vbz"];
      deepEqual(result, answer);

      var result = RiTa.getPosTags("Dave dances");
      var answer = ["nnp", "vbz"];
      deepEqual(result, answer);

      var result = RiTa.getPosTags("running");
      var answer = ["vbg"];
      deepEqual(result, answer);

      var result = RiTa.getPosTags("asserting");
      var answer = ["vbg"];
      deepEqual(result, answer);

      var result = RiTa.getPosTags("assenting");
      var answer = ["vbg"];
      deepEqual(result, answer);

      var result = RiTa.getPosTags("Dave");
      var answer = ["nnp"];
      deepEqual(result, answer);

      var result = RiTa.getPosTags("There is a cat.");
      var answer = ["ex", "vbz", "dt", "nn", "."];
      deepEqual(result, answer);

      var result = RiTa.getPosTags("The boy, dressed in red, ate an apple.");
      var answer = ["dt", "nn", ",", "vbn", "in", "jj", ",", "vbd", "dt", "nn", "."];
      deepEqual(result, answer);

      var txt = "The dog ran faster than the other dog.  But the other dog was prettier.";
      var result = RiTa.getPosTags(txt);
      var answer = ["dt", "nn", "vbd", "rbr", "in", "dt", "jj", "nn", ".", "cc", "dt", "jj", "nn", "vbd", "jjr", "."];
      deepEqual(result, answer);

      var result = RiTa.getPosTags("");
      var answer = [];
      deepEqual(result, answer);

    });

    test("testGetPosTags(sns)", function() {

      if (noLexicon()) return;

      var checks = ["emphasis", "stress", "discus", "colossus", "fibrosis", "digitalis", "pettiness", "mess", "cleanliness", "orderliness", "bronchitis", "preparedness", "highness"];
      for (var i = 0, j = checks.length; i < j; i++) {
        if (RiTa.getPosTags(checks[i])[0] !== 'nn')
          console.log(checks[i] + ": " + RiTa.getPosTags(checks[i])[0]);
        deepEqual(RiTa.getPosTags(checks[i]), ["nn"]);
      }
    });

    test("testGetPosTagsInline", function() {

      var result = RiTa.getPosTagsInline("");
      var answer = "";
      deepEqual(result, answer);

      var result = RiTa.getPosTagsInline("asdfaasd");
      var answer = "asdfaasd/nn";
      deepEqual(result, answer);

      var result = RiTa.getPosTagsInline("clothes");
      var answer = "clothes/nns";
      deepEqual(result, answer);

      if (noLexicon()) return;

      var result = RiTa.getPosTagsInline("There is a cat.");
      var answer = "There/ex is/vbz a/dt cat/nn .";
      deepEqual(result, answer);

      var result = RiTa.getPosTagsInline("The boy, dressed in red, ate an apple.");
      var answer = "The/dt boy/nn , dressed/vbn in/in red/jj , ate/vbd an/dt apple/nn .";
      deepEqual(result, answer);

      var txt = "The dog ran faster than the other dog.  But the other dog was prettier.";
      var result = RiTa.getPosTagsInline(txt);
      var answer = "The/dt dog/nn ran/vbd faster/rbr than/in the/dt other/jj dog/nn . But/cc the/dt other/jj dog/nn was/vbd prettier/jjr .";
      equal(result, answer);
    });

    test("testGetStresses", function() {

      var result = RiTa.getStresses("");
      var answer = "";
      equal(result, answer);

      if (noLexicon()) return;

      var result = RiTa.getStresses("The emperor had no clothes on");
      var answer = "0 1/0/0 1 1 1 1";
      equal(result, answer);

      var result = RiTa.getStresses("The emperor had no clothes on.");
      var answer = "0 1/0/0 1 1 1 1 .";
      equal(result, answer);

      var result = RiTa.getStresses("The emperor had no clothes on. The King is fat.");
      var answer = "0 1/0/0 1 1 1 1 . 0 1 1 1 .";
      equal(result, answer);

      var result = RiTa.getStresses("to preSENT, to exPORT, to deCIDE, to beGIN");
      var answer = "1 0/1 , 1 0/1 , 1 0/1 , 1 0/1";
      equal(result, answer);

      var result = RiTa.getStresses("to present, to export, to decide, to begin");
      var answer = "1 0/1 , 1 0/1 , 1 0/1 , 1 0/1";
      equal(result, answer);

      var txt = "The dog ran faster than the other dog.  But the other dog was prettier.";
      var result = RiTa.getStresses(txt);
      var answer = "0 1 1 1/0 1 0 1/0 1 . 1 0 1/0 1 1 1/0/0 .";
      equal(result, answer);
    });

    test("testGetWordCount", function() {

      var result = RiTa.getWordCount("123 1231 hi");
      deepEqual(result, 3);

      var result = RiTa.getWordCount("The boy screamed, 'Where is my apple?'");
      deepEqual(result, 10);

      var result = RiTa.getWordCount("one two three.");
      deepEqual(result, 4);

      var result = RiTa.getWordCount("I guess the dog ate the baby.");
      deepEqual(result, 8);

      var result = RiTa.getWordCount("Oh my god, the dog ate the baby!");
      deepEqual(result, 10);

      var result = RiTa.getWordCount("Which dog ate the baby?");
      deepEqual(result, 6);

      var result = RiTa.getWordCount("\'Yes, it was a dog that ate the baby\', he said.");
      deepEqual(result, 15);
    });

    /*test("testPosToWordNet", function() {

        var result = RiTa.posToWordNet("nn");
        equal("n", result);

        var result = RiTa.posToWordNet("nns");
        equal("n", result);

        var result = RiTa.posToWordNet("vbz");
        equal("v", result);

        var result = RiTa.posToWordNet("vbz!");
        equal("-", result);

        var result = RiTa.posToWordNet("aa");
        equal("-", result);

        var result = RiTa.posToWordNet("rb");
        equal("r", result);

        var result = RiTa.posToWordNet("rb ");
        //space
        equal("-", result);

        var result = RiTa.posToWordNet(" rb ");
        //space
        equal("-", result);

        var result = RiTa.posToWordNet(" rb  ");
        //double space
        equal("-", result);

        var result = RiTa.posToWordNet("  rb");
        //double space
        equal("-", result);

        var result = RiTa.posToWordNet("rb    ");
        //tab space
        equal("-", result);

        var result = RiTa.posToWordNet("  rb");
        //tab space
        equal("-", result);

        var result = RiTa.posToWordNet("");
        equal("", result);
    });*/

    test("testStem(lancaster)", function() {

      if (!RiTa.stem_Lancaster) {
        console.warn("[INFO] RiTa-tests: skipping lancaster tests");
        return ok(1);
      }

      var type = 'Lancaster';

      var tests = ["run", "runs", "running"];
      for (var i = 0; i < tests.length; i++) {
        equal(RiTa.stem(tests[i], type), "run");
      }

      tests = ["hide", "hides", "hiding"];
      for (var i = 0; i < tests.length; i++) {
        equal(RiTa.stem(tests[i], type), "hid");
      }

      tests = ["take", "takes", "taking"];
      for (var i = 0; i < tests.length; i++) {
        equal(RiTa.stem(tests[i], type), "tak");
      }

      tests = ["become", "becomes", "becoming"];
      for (var i = 0; i < tests.length; i++) {
        equal(RiTa.stem(tests[i], type), "becom");
      }

      equal(RiTa.stem("gases", type), "gas");
      equal(RiTa.stem("buses", type), "bus");
      equal(RiTa.stem("happiness", type), "happy");
      equal(RiTa.stem("terrible", type), "terr");

      var test = 'Stemming is funnier than a bummer says the sushi loving computer';
      var result = 'stem is funny than a bum say the sush lov comput';
      equal(RiTa.stem(test, type), result);

      equal(RiTa.stem("cakes", type), "cak");
    });

    test("testStem(porter)", function() {

      var type = 'Porter';

      equal(RiTa.stem("cakes", type), "cake");

      var tests = ["run", "runs", "running"];
      for (var i = 0; i < tests.length; i++) {
        equal(RiTa.stem(tests[i], type), "run");
      }

      tests = ["hide", "hides", "hiding"];
      for (var i = 0; i < tests.length; i++) {
        equal(RiTa.stem(tests[i], type), "hide");
      }

      tests = ["take", "takes", "taking"];
      for (var i = 0; i < tests.length; i++) {
        equal(RiTa.stem(tests[i], type), "take");
      }

      equal(RiTa.stem("gases", type), "gase");
      equal(RiTa.stem("buses", type), "buse");
      equal(RiTa.stem("happiness", type), "happi");
      equal(RiTa.stem("joyful", type), "joy");
      equal(RiTa.stem("terrible", type), "terribl");

      var test = 'Stemming is funnier than a bummer';
      var result = 'Stem is funnier than a bummer';
      equal(RiTa.stem(test, type), result);
    });

    test("testStem(pling)", function() {

      var type = 'Pling';

      equal(RiTa.stem("cakes", type), "cake");

      equal(RiTa.stem("run", type), "run");
      equal(RiTa.stem("runs", type), "run");
      equal(RiTa.stem("running", type), "running");

      equal(RiTa.stem("take", type), "take");
      equal(RiTa.stem("takes", type), "take");
      equal(RiTa.stem("taking", type), "taking");

      equal(RiTa.stem("hide", type), "hide");
      equal(RiTa.stem("hides", type), "hide");
      equal(RiTa.stem("hiding", type), "hiding");

      equal(RiTa.stem("become", type), "become");
      equal(RiTa.stem("becomes", type), "become");
      equal(RiTa.stem("becoming", type), "becoming");

      equal(RiTa.stem("gases", type), "gas");
      equal(RiTa.stem("buses", type), "bus");
      equal(RiTa.stem("happiness", type), "happiness");
      equal(RiTa.stem("terrible", type), "terrible");

      var test =  "Stemming is funnier than a bummer";
      var result = "stemming is funnier than a bummer";
      // TODO: RiTa.stem(pling) JS decapitalizes input whereas the java version does not
      equal(RiTa.stem(test, type), result);
    });

    test("testLTSEngine", function() {

      if (noLexicon()) return;

      //getPhonemes
      var result = RiTa.getPhonemes("asdfgasdasdasdasdsadasf");
      var answer = "ae-s-d-f-g-ax-s-d-ae-s-d-ae-s-d-ae-s-d-s-ao-d-ae-s-f";
      equal(result, answer);

      var result = RiTa.getPhonemes("HTERE ^%&*^ IS ^(*^*() fsfa");
      var answer = "t-ih-r ^ % ae-n-d *^ ih-z ^ ( *^* ( ) f-s-f-ax";
      equal(result, answer);

      //getStresses
      var result = RiTa.getStresses("deMOcracy, dependaBIlity, phoTOgraphy, geOLogy");
      var answer = "0/1/0/0 , 0/1/0/1/0/0 , 0/1/0/0 , 0/1/0/0";
      equal(result, answer);

      var result = RiTa.getStresses("@#$%^&*()");
      var answer = "@ # $ % ^ 1 * ( )";
      equal(result, answer);

      var result = RiTa.getStresses("asdfgasdasdasdasdsadasf");
      var answer = "1/0/1/1/1/1/1";
      equal(result, answer);

      var result = RiTa.getStresses("HTERE ^%&*^ IS ^(*^*() fsfa");
      var answer = "1 ^ % 1 *^ 1 ^ ( *^* ( ) 0";
      equal(result, answer);

      //getSyllables
      var result = RiTa.getSyllables("one two three four five");
      // numbers are not working also
      var answer = "w-ah-n t-uw th-r-iy f-ao-r f-ay-v";
      equal(result, answer);

      var result = RiTa.getSyllables("12345");
      // numbers are not working also
      var answer = "w-ah-n/t-uw/th-r-iy/f-ao-r/f-ay-v";
      equal(result, answer);

      var result = RiTa.getSyllables("1 2 3 4 5 ");
      // numbers are not working also
      var answer = "w-ah-n t-uw th-r-iy f-ao-r f-ay-v";
      equal(result, answer);

      var result = RiTa.getPosTags("2");
      // numbers are not working also
      var answer = "cd";
      equal(result, answer);

      var result = RiTa.getSyllables("The three emperor had no clothes on.");
      var answer = "dh-ax th-r-iy eh-m-p/er/er hh-ae-d n-ow k-l-ow-dh-z aa-n .";
      equal(result, answer);

      var result = RiTa.getSyllables("The 3 emperor had no clothes on.");
      var answer = "dh-ax th-r-iy eh-m-p/er/er hh-ae-d n-ow k-l-ow-dh-z aa-n .";
      equal(result, answer);

      var result = RiTa.getSyllables("The 3 emperor! Had no clothes, on.");
      var answer = "dh-ax th-r-iy eh-m-p/er/er ! hh-ae-d n-ow k-l-ow-dh-z , aa-n .";
      equal(result, answer);

      var result = RiTa.getSyllables("asdfgasdasdasdasdsadasf");
      var answer = "ae-s-d-f/g-ax-s/d-ae-s/d-ae-s/d-ae-s-d/s-ao/d-ae-s-f";
      equal(result, answer);

      var result = RiTa.getSyllables("HTERE ^%&*^ IS ^(*^*() fsfa");
      var answer = "t-ih-r ^ % ae-n-d *^ ih-z ^ ( *^* ( ) f-s-f-ax";
      equal(result, answer);
    });

    test("testPluralize", function() {

      equal("blondes", RiTa.pluralize("blonde"));
      equal("eyes", RiTa.pluralize("eye"));
      equal("blondes", RiTa.pluralize("blond"));

      equal("dogs", RiTa.pluralize("dog"));
      equal("feet", RiTa.pluralize("foot"));
      equal("men", RiTa.pluralize("man"));

      equal("beautifuls", RiTa.pluralize("beautiful"));
      equal("teeth", RiTa.pluralize("tooth"));
      equal("cakes", RiTa.pluralize("cake"));
      equal("kisses", RiTa.pluralize("kiss"));
      equal("children", RiTa.pluralize("child"));

      equal("randomwords", RiTa.pluralize("randomword"));
      equal("lice", RiTa.pluralize("louse"));

      equal("sheep", RiTa.pluralize("sheep"));
      equal("shrimps", RiTa.pluralize("shrimp"));
      equal("series", RiTa.pluralize("series"));
      equal("mice", RiTa.pluralize("mouse"));

      equal("", RiTa.pluralize(""));

      equal(RiTa.pluralize("tomato"), "tomatoes");
      equal(RiTa.pluralize("toe"), "toes");

      equal(RiTa.pluralize("deer"), "deer");
      equal(RiTa.pluralize("ox"), "oxen");

      equal(RiTa.pluralize("tobacco"), "tobacco");
      equal(RiTa.pluralize("cargo"), "cargo");
      equal(RiTa.pluralize("golf"), "golf");
      equal(RiTa.pluralize("grief"), "grief");
      equal(RiTa.pluralize("wildlife"), "wildlife");
      equal(RiTa.pluralize("taxi"), "taxis");
      equal(RiTa.pluralize("Chinese"), "Chinese");
      equal(RiTa.pluralize("bonsai"), "bonsai");

      equal(RiTa.pluralize("gas"), "gases");
      equal(RiTa.pluralize("bus"), "buses");

      equal("crises", RiTa.pluralize("crisis"));
      equal("theses", RiTa.pluralize("thesis"));
      equal("apotheses", RiTa.pluralize("apothesis"));
      equal("stimuli", RiTa.pluralize("stimulus"));
      equal("alumni", RiTa.pluralize("alumnus"));
      equal("corpora", RiTa.pluralize("corpus"));
    });

    test("testSingularize", function() {

      equal("blonde", RiTa.singularize("blondes"));
      equal("eye", RiTa.singularize("eyes"));
      equal(RiTa.singularize("bonsai"), "bonsai");
      equal(RiTa.singularize("taxis"), "taxi");
      equal(RiTa.singularize("chiefs"), "chief");
      equal(RiTa.singularize("monarchs"), "monarch");
      equal(RiTa.singularize("lochs"), "loch");
      equal(RiTa.singularize("stomachs"), "stomach");

      equal(RiTa.singularize("Chinese"), "Chinese");

      equal(RiTa.singularize("people"), "person");
      equal(RiTa.singularize("monies"), "money");
      equal(RiTa.singularize("vertebrae"), "vertebra");
      equal(RiTa.singularize("humans"), "human");
      equal(RiTa.singularize("germans"), "german");
      equal(RiTa.singularize("romans"), "roman");

      equal(RiTa.singularize("memoranda"), "memorandum");
      equal(RiTa.singularize("data"), "datum");
      equal(RiTa.singularize("appendices"), "appendix");
      equal(RiTa.singularize("theses"), "thesis");
      equal(RiTa.singularize("alumni"), "alumnus");

      equal(RiTa.singularize("solos"), "solo");
      equal(RiTa.singularize("music"), "music");

      equal(RiTa.singularize("oxen"), "ox");
      equal(RiTa.singularize("solos"), "solo");
      equal(RiTa.singularize("music"), "music");

      equal(RiTa.singularize("tobacco"), "tobacco");
      equal(RiTa.singularize("cargo"), "cargo");
      equal(RiTa.singularize("golf"), "golf");
      equal(RiTa.singularize("grief"), "grief");

      equal(RiTa.singularize("cakes"), "cake");

      equal("dog", RiTa.singularize("dogs"));
      equal("foot", RiTa.singularize("feet"));
      equal("tooth", RiTa.singularize("teeth"));
      equal("kiss", RiTa.singularize("kisses"));
      equal("child", RiTa.singularize("child"));
      equal("randomword", RiTa.singularize("randomwords"));
      equal("deer", RiTa.singularize("deer"));
      equal("sheep", RiTa.singularize("sheep"));
      equal("shrimp", RiTa.singularize("shrimps"));

      equal(RiTa.singularize("tomatoes"), "tomato");
      equal(RiTa.singularize("photos"), "photo");

      equal(RiTa.singularize("toes"), "toe");

      equal("series", RiTa.singularize("series"));
      equal("ox", RiTa.singularize("oxen"));
      equal("man", RiTa.singularize("men"));
      equal("mouse", RiTa.singularize("mice"));
      equal("louse", RiTa.singularize("lice"));
      equal("child", RiTa.singularize("children"));

      equal(RiTa.singularize("gases"), "gas");
      equal(RiTa.singularize("buses"), "bus");
      equal(RiTa.singularize("happiness"), "happiness");

      equal(RiTa.singularize("crises"), "crisis");
      equal(RiTa.singularize("theses"), "thesis");
      equal(RiTa.singularize("apotheses"), "apothesis");
      equal(RiTa.singularize("stimuli"), "stimulus");
      equal(RiTa.singularize("alumni"), "alumnus");
      equal(RiTa.singularize("corpora"), "corpus");

      equal("", RiTa.singularize(""));
    });

    test("testGetPastParticiple", function() {

      equal(RiTa.getPastParticiple("sleep"), "slept");
      equal(RiTa.getPastParticiple("withhold"), "withheld");

      equal(RiTa.getPastParticiple("cut"), "cut");
      equal(RiTa.getPastParticiple("go"), "gone");
      equal(RiTa.getPastParticiple("swim"), "swum");
      equal(RiTa.getPastParticiple("would"), "would");
      equal(RiTa.getPastParticiple("might"), "might");
      equal(RiTa.getPastParticiple("run"), "run");
      equal(RiTa.getPastParticiple("speak"), "spoken");
      equal(RiTa.getPastParticiple("break"), "broken");
      equal(RiTa.getPastParticiple(""), "");

      // PROBLEMS

      equal(RiTa.getPastParticiple("awake"), "awoken");
      equal(RiTa.getPastParticiple("become"), "became");
      equal(RiTa.getPastParticiple("drink"), "drunk");
      equal(RiTa.getPastParticiple("plead"), "pled");
      equal(RiTa.getPastParticiple("run"), "run");
      equal(RiTa.getPastParticiple("shine"), "shone");
      // or shined
      equal(RiTa.getPastParticiple("shrink"), "shrunk");
      // or shrunken
      equal(RiTa.getPastParticiple("stink"), "stunk");
      equal(RiTa.getPastParticiple("study"), "studied");
    });

    test("testGetPresentParticiple", function() {

      equal(RiTa.getPresentParticiple("sleep"), "sleeping");
      equal(RiTa.getPresentParticiple("withhold"), "withholding");

      equal(RiTa.getPresentParticiple("cut"), "cutting");
      equal(RiTa.getPresentParticiple("go"), "going");
      equal(RiTa.getPresentParticiple("run"), "running");
      equal(RiTa.getPresentParticiple("speak"), "speaking");
      equal(RiTa.getPresentParticiple("break"), "breaking");
      equal(RiTa.getPresentParticiple("become"), "becoming");
      equal(RiTa.getPresentParticiple("plead"), "pleading");
      equal(RiTa.getPresentParticiple("awake"), "awaking");
      equal(RiTa.getPresentParticiple("study"), "studying");

      equal(RiTa.getPresentParticiple("lie"), "lying");
      equal(RiTa.getPresentParticiple("swim"), "swimming");
      equal(RiTa.getPresentParticiple("run"), "running");
      equal(RiTa.getPresentParticiple("dig"), "digging");
      equal(RiTa.getPresentParticiple("set"), "setting");
      equal(RiTa.getPresentParticiple("speak"), "speaking");
      equal(RiTa.getPresentParticiple("bring"), "bringing");
      equal(RiTa.getPresentParticiple("speak"), "speaking");

      equal(RiTa.getPresentParticiple("study "), "studying");
      //space
      equal(RiTa.getPresentParticiple(" study"), "studying");
      //space
      equal(RiTa.getPresentParticiple("study  "), "studying");
      //double space
      equal(RiTa.getPresentParticiple("  study"), "studying");
      //double space
      equal(RiTa.getPresentParticiple("study    "), "studying");
      //tab space
      equal(RiTa.getPresentParticiple(" study"), "studying");
      //tab space
      equal(RiTa.getPresentParticiple(""), "");

    });

    test("testUntokenize", function() {

      equal(RiTa.untokenize([""]), "");

      var expected = "The boy, dressed in red, ate an apple.";
      var input = ["The", "boy", ",", "dressed", "in", "red", ",", "ate", "an", "apple", "."];
      var output = RiTa.untokenize(input);
      deepEqual(output, expected);


      var expected = "The boy screamed, 'Where is my apple?'";
      var input = ["The", "boy", "screamed", ",", "'Where", "is", "my", "apple", "?", "'"];
      var output = RiTa.untokenize(input);
      deepEqual(output, expected);

      var outputs = ["A simple sentence.",
        "that's why this is our place).",
      ];

      var inputs = [
        ["A", "simple", "sentence", "."],
        ["that's", "why", "this", "is", "our", "place", ")", "."],
      ];

      ok(inputs.length == outputs.length);

      for (var i = 0; i < inputs.length; i++) {
        var result = RiTa.untokenize(inputs[i]);
        deepEqual(result, outputs[i]);
      }

      var expected = "Dr. Chan is talking slowly with Mr. Cheng, and they're friends."; // strange but same as RiTa-java
      var input = ["Dr", ".", "Chan", "is", "talking", "slowly", "with", "Mr", ".", "Cheng", ",", "and", "they're", "friends", "."];
      var output = RiTa.untokenize(input);
      deepEqual(output, expected);

      var input = ["why", "?", "Me", "?", "huh", "?", "!"];
      var expected = "why? Me? huh?!";
      var output = RiTa.untokenize(input);
      deepEqual(output, expected);

      var input = ["123", "123", "1", "2", "3", "1", ",", "1", "1", ".", "1", "23", ".", "45", ".", "67", "22/05/2012", "12th", "May", ",", "2012"];
      var expected = "123 123 1 2 3 1, 1 1. 1 23. 45. 67 22/05/2012 12th May, 2012";
      var output = RiTa.untokenize(input);
      deepEqual(output, expected);
    });

    test("testConcordance", function() {

      var data = RiTa.concordance("The dog ate the cat");

      equal( Object.keys(data).length, 5);
      equal(data["the"],1);
      equal(data["The"],1);
      equal(data["THE"],undefined);

      data = RiTa.concordance("The dog ate the cat", {
        ignoreCase: false,
        ignoreStopWords: false,
        ignorePunctuation: false,
      });

      equal( Object.keys(data).length, 5); // same results
      equal(data["the"],1);
      equal(data["The"],1);
      equal(data["THE"],undefined);

      data = RiTa.concordance("The dog ate the cat", {
        ignoreCase: true
      });

      equal( Object.keys(data).length, 4);
      equal(data["the"],2);
      equal(data["The"],undefined);
      equal(data["THE"],undefined);
    });

    test("testKwic", function() {
      var s = "The dog ate the cat. The bear Ate the honey";
      var lines = RiTa.kwic(s,"ate");
      equal(lines.length,1);
      var opts = { ignoreCase: true };
      lines = RiTa.kwic(s,"ate",opts);
      equal(lines.length,2);
    });

    test("testConjugate", function() {

      // TODO: Check against RiTa-java (why are these all doubling?)

      var args = {
        tense: RiTa.PRESENT_TENSE,
        number: RiTa.SINGULAR,
        person: RiTa.THIRD_PERSON
      };

      s = ["swim", "need", "open"];
      a = ["swims", "needs", "opens"];

      for (var i = 0; i < s.length; i++) {
        c = RiTa.conjugate(s[i], args);
        equal(c, a[i]);
      }

      var args = {
        tense: RiTa.PRESENT_TENSE,
        number: RiTa.SINGULAR,
        person: RiTa.THIRD_PERSON,
        passive: true
      };

      a = ["is swum", "is needed", "is opened"];
      for (var i = 0; i < s.length; i++) {
        c = RiTa.conjugate(s[i], args);
        equal(c, a[i]);
      }

      /////////////////////////////////////////////////

      var args = {
        number: RiTa.SINGULAR,
        person: RiTa.FIRST_PERSON,
        tense: RiTa.PAST_TENSE
      };

      var c = RiTa.conjugate("swim", args);
      equal(c, "swam");

      var s = ["swim", "need", "open", ""];
      var a = ["swam", "needed", "opened", ""];

      ok(a.length === s.length);

      for (var i = 0; i < s.length; i++) {
        var c = RiTa.conjugate(s[i], args);
        equal(c, a[i]);
      }

      equal("swum", RiTa.getPastParticiple("swim"));

      var args = {
        number: RiTa.PLURAL,
        person: RiTa.SECOND_PERSON,
        tense: RiTa.PAST_TENSE
      };

      var a = ["swam", "needed", "opened", ""];
      ok(a.length === s.length);


      for (var i = 0; i < s.length; i++) {
        var c = RiTa.conjugate(s[i], args);
        equal(c, a[i]);
      }

      var args = {
        number: RiTa.PLURAL,
        person: RiTa.SECOND_PERSON,
        tense: RiTa.FUTURE_TENSE
      };
      a = ["will swim", "will need", "will open", ""];
      ok(a.length === s.length);

      for (var i = 0; i < s.length; i++) {
        c = RiTa.conjugate(s[i], args);
        equal(c, a[i]);
      }

      var args = {
        tense: RiTa.PAST_TENSE,
        number: RiTa.SINGULAR,
        person: RiTa.THIRD_PERSON
      };
      a = ["swam", "needed", "opened", ""];
      ok(a.length === s.length);
      for (var i = 0; i < s.length; i++) {
        c = RiTa.conjugate(s[i], args);
        equal(c, a[i]);
      }

      var args = {
        tense: RiTa.PAST_TENSE,
        number: RiTa.SINGULAR,
        person: RiTa.THIRD_PERSON,
        form: RiTa.INFINITIVE
      };
      a = ["to swim", "to need", "to open", ""];
      ok(a.length === s.length);
      for (var i = 0; i < s.length; i++) {
        c = RiTa.conjugate(s[i], args);
        equal(c, a[i]);
      }

      var args = {
        tense: RiTa.PAST_TENSE,
        number: RiTa.SINGULAR,
        person: RiTa.THIRD_PERSON,
        passive: true
      };

      s = ["scorch", "burn", "hit", ""];
      a = ["was scorched", "was burned", "was hit", ""];
      ok(a.length === s.length);
      for (var i = 0; i < s.length; i++) {
        c = RiTa.conjugate(s[i], args);
        equal(c, a[i]);
      }

      s = ["swim", "need", "open", ""];
      var args = {
        tense: RiTa.PRESENT_TENSE,
        number: RiTa.SINGULAR,
        person: RiTa.THIRD_PERSON,
        form: RiTa.INFINITIVE,
        progressive: true
      };
      a = ["to be swimming", "to be needing", "to be opening", ""];
      ok(a.length === s.length);
      for (var i = 0; i < s.length; i++) {
        c = RiTa.conjugate(s[i], args);
        equal(c, a[i]);
      }

      var args = {
        tense: RiTa.PRESENT_TENSE,
        number: RiTa.SINGULAR,
        person: RiTa.THIRD_PERSON,
        form: RiTa.INFINITIVE,
        perfect: true
      };
      a = ["to have swum", "to have needed", "to have opened", ""];
      ok(a.length === s.length);
      for (var i = 0; i < s.length; i++) {
        c = RiTa.conjugate(s[i], args);
        equal(c, a[i]);
      }

      var args = {
        number: RiTa.PLURAL,
        person: RiTa.SECOND_PERSON,
        tense: RiTa.PAST_TENSE
      };
      equal(RiTa.conjugate("barter", args), "bartered");
      equal(RiTa.conjugate("run", args), "ran");

      s = ["compete", "complete", "eject"];
      a = ["competed", "completed", "ejected"];
      ok(a.length === s.length);
      for (var i = 0; i < s.length; i++) {
        c = RiTa.conjugate(s[i], args);
        equal(c, a[i]);
      }
    });

    function noLexicon() {
      if (!RiLexicon.enabled) {
        if (!lexWarningRiTa) {
          lexWarningRiTa = true;
          console.warn('[INFO] RiTa-tests: skipping lexicon-required tests');
        }
        ok(1);
        return true;
      }
    } var lexWarningRiTa = false;

  } // end runtests


if (typeof exports != 'undefined') runtests();
