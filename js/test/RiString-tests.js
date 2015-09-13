/*global console, test, throws, equal, fail, deepEqual, notEqual, expect, require, ok,
    QUnit, RiTa, RiTaEvent, RiString, RiGrammar, RiMarkov, RiLexicon */

/*jshint loopfunc: true */

var runtests = function () {

  QUnit.module("RiString", {
    setup: function () {
      RiTa.SILENT = true;
      // UNCOMMENT TO TEST WITHOUT LEXICON, USING ONLY LTS
      //RiTa.USE_LEXICON = false;
    },
    teardown: function () {}
  });

  test("testRiString", function () {

    ok(RiString('hello'));
    ok(new RiString('hello'));
    ok(RiString(''));
    ok(new RiString(''));
    ok(new RiString(64));
    ok(RiString(64));

    var BAD = [null, undefined];

    for (var i = 0; i < BAD.length; i++) {

      throws(function () {

        try {
          new RiString(BAD[i]);
          fail("no exception");
        } catch (e) {
          throw e;
        }
      });
      throws(function () {

        try {
          RiString(BAD[i]);
          fail("no exception");
        } catch (e) {
          throw e;
        }
      });
    }
  });

  test("testAnalyze", function () { // same tests as testFeatures() below

    if (noLexicon()) return;

    var features = RiString("Mom & Dad, waiting for the car, ate a steak.").analyze().features();
    ok(features);

    equal(features.phonemes, "m-aa-m ae-n-d d-ae-d , w-ey-t-ih-ng f-ao-r dh-ax k-aa-r , ey-t ey s-t-ey-k .");
    equal(features.syllables, "m-aa-m ae-n-d d-ae-d , w-ey-t/ih-ng f-ao-r dh-ax k-aa-r , ey-t ey s-t-ey-k .");
    equal(features.stresses, "1 1 1 , 1/0 1 0 1 , 1 1 1 .");

    var features = RiString("123").analyze().features();
    ok(features);
    equal(features.phonemes, "w-ah-n-t-uw-th-r-iy");
    equal(features.syllables, "w-ah-n/t-uw/th-r-iy");
    equal(features.stresses, "0/0/0");

    features = RiString("The dog ran faster than the other dog.  But the other dog was prettier.").analyze().features();
    ok(features);
    equal(features.phonemes, "dh-ax d-ao-g r-ae-n f-ae-s-t-er dh-ae-n dh-ax ah-dh-er d-ao-g . b-ah-t dh-ax ah-dh-er d-ao-g w-aa-z p-r-ih-t-iy-er .");
    equal(features.syllables, "dh-ax d-ao-g r-ae-n f-ae-s/t-er dh-ae-n dh-ax ah-dh/er d-ao-g . b-ah-t dh-ax ah-dh/er d-ao-g w-aa-z p-r-ih-t/iy/er .");
    equal(features.stresses, "0 1 1 1/0 1 0 1/0 1 . 1 0 1/0 1 1 1/0/0 .");

    features = RiString("The laggin dragon").analyze().features();
    ok(features);
    equal(features.phonemes, "dh-ax l-ae-g-ih-n d-r-ae-g-aa-n");
    equal(features.syllables, "dh-ax l-ae/g-ih-n d-r-ae-g/aa-n");
    equal(features.stresses, "0 1/1 1/0");

    features = RiString(".").analyze().features();
    ok(features);
    equal(features.phonemes, ".");
    equal(features.syllables, ".");
    equal(features.stresses, ".");

    features = RiString("1 2 7").analyze().features();
    ok(features);
    equal(features.phonemes, "w-ah-n t-uw s-eh-v-ax-n");
    equal(features.syllables, "w-ah-n t-uw s-eh/v-ax-n");
    equal(features.stresses, "0 0 1/0");

    features = RiString("*").analyze().features();
    ok(features);
    equal(features.phonemes, "*");
    equal(features.syllables, "*");
    equal(features.stresses, "*");
  });

  test("testFeatures", function () {

    var txt = "Returns the array of words.";
    var rs = RiString(txt), feats = rs.features();
    ok(feats);
    ok(feats[RiTa.POS]);
    equal(feats[RiTa.TEXT], txt);
    deepEqual(feats[RiTa.TOKENS], RiTa.tokenize(txt).join(' '));

    if (noLexicon()) return;

    txt = "Returns the array of words.";
    rs = RiString(txt).analyze();
    feats = rs.features();
    ok(feats);

    equal(feats[RiTa.TEXT], txt);
    deepEqual(feats[RiTa.TOKENS], RiTa.tokenize(txt).join(' '));

    ok(feats[RiTa.SYLLABLES]);
    ok(feats[RiTa.PHONEMES]);
    ok(feats[RiTa.STRESSES]);
    ok(feats[RiTa.POS]);
  });

  test("testCharAt", function () {

    var rs = new RiString("The dog was white");

    var result = rs.charAt(0);
    equal(result, "T");

    result = rs.charAt(5);
    notEqual(result, "O");

    result = rs.charAt(5);
    notEqual(result, '*');

    result = rs.charAt(200); //out of range character
    equal(result, "");
  });

  test("testConcat", function () {

    var rs = new RiString("The dog was white");
    var rs2 = new RiString("The dog was not white");
    var result = rs.concat(rs2);
    equal(result, "The dog was whiteThe dog was not white");

    rs = new RiString(" The dog was white ");
    rs2 = new RiString("The dog was not white ");
    result = rs.concat(rs2);
    equal(result, " The dog was white The dog was not white ");

    rs = new RiString("#$#@#$@#");
    rs2 = new RiString("The dog was not white ");
    result = rs.concat(rs2);
    equal(result, "#$#@#$@#The dog was not white ");

  });

  test("testCopy", function () {

    var rs = new RiString("copy cat");
    var rs2 = rs.copy();
    deepEqual(rs2, rs);

    rs = new RiString("copy dogs.");
    rs2 = rs.copy();
    deepEqual(rs, rs2);

    rs = new RiString("cOPy dOgs.");
    rs2 = rs.copy();
    deepEqual(rs, rs2);

    rs = new RiString("!@#$%^&*()_+");
    rs2 = rs.copy();
    deepEqual(rs, rs2);

    rs = new RiString("");
    rs2 = rs.copy();
    deepEqual(rs, rs2);

    rs = new RiString("!@#$sadas*()_+");
    rs2 = rs.copy();
    deepEqual(rs, rs2);

    rs = new RiString("copy cat");
    rs.set("myFeatureName", "myFeatureValue");
    rs2 = rs.copy();
    equal(rs.get("myFeatureName"), rs2.get("myFeatureName"));

    if (noLexicon()) return;

    rs = new RiString("copy cat");
    rs.analyze();
    rs2 = rs.copy();
    deepEqual(rs.features(), rs2.features());

  });

  test("testEndsWith", function () {

    // check that these are ok --------------

    var rs = new RiString("girls");
    var result = rs.endsWith("s");
    ok(result);

    rs = new RiString("closed");
    result = rs.endsWith("ed");
    ok(result);

    rs = new RiString("The dog was white");
    result = rs.endsWith("white");
    ok(result);

    rs = new RiString("");
    result = rs.endsWith("");
    ok(result);

  });

  test("testEquals", function () { // compare Object

    // check that these are ok ---------------

    var rs = new RiString("closed");
    var rs2 = new RiString("closed");
    var result = rs.equals(rs2);
    ok(result);

    rs = new RiString("closed");
    rs2 = new RiString("Closed");
    result = rs.equals(rs2);
    ok(!result);

    rs = new RiString("clOsed");
    rs2 = new RiString("closed");
    result = rs.equals(rs2);
    ok(!result);

    rs = new RiString("There is a cat.");
    rs2 = new RiString("There is a cat.");
    result = rs.equals(rs2);
    ok(result);

    rs = new RiString("There is a cat.");
    rs2 = new RiString("There is a cat. ");
    result = rs.equals(rs2);
    ok(!result);

    rs = new RiString("There is a cat.");
    rs2 = new RiString("There is a cat");
    result = rs.equals(rs2);
    ok(!result);

    rs = new RiString("There is a cat.");
    rs2 = new RiString("");
    result = rs.equals(rs2);
    ok(!result);

    rs = new RiString("");
    rs2 = new RiString("");
    result = rs.equals(rs2);
    ok(result);

    // ---------------

    rs = new RiString("closed");
    result = rs.equals("closed");
    ok(result);

    rs = new RiString("closed");
    result = rs.equals("Closed");
    ok(!result);

    rs = new RiString("clOsed");
    result = rs.equals("closed");
    ok(!result);

    rs = new RiString("There is a cat.");
    result = rs.equals("There is a cat.");
    ok(result);

    rs = new RiString("There is a cat.");
    result = rs.equals("There is a cat. ");
    ok(!result);

    rs = new RiString("There is a cat.");
    result = rs.equals("There is a cat");
    ok(!result);

    rs = new RiString("There is a cat.");
    result = rs.equals("");
    ok(!result);
  });

  test("testEqualsIgnoreCase", function () {

    // check that these are ok ---------------

    var rs = new RiString("closed");
    var result = rs.equalsIgnoreCase("Closed");
    ok(result);

    rs = new RiString("There is a cat.");
    result = rs.equalsIgnoreCase("TheRe Is a cAt.");
    ok(result);

    rs = new RiString("THere iS a Cat.");
    result = rs.equalsIgnoreCase("TheRe Is a cAt.");
    ok(result);

    rs = new RiString("THere iS a Cat.");
    var rs2 = new RiString("THere iS a Cat.");
    result = rs.equalsIgnoreCase(rs2);
    ok(result);

    rs = new RiString("THere iS a Cat.");
    rs2 = new RiString("THere iS not a Cat.");
    result = rs.equalsIgnoreCase(rs2);
    ok(!result);

    rs = new RiString("");
    result = rs.equalsIgnoreCase("");
    ok(result);
  });

  test("testIndexOf", function () {

    // check that these are ok ---------------
    var rs = new RiString("Returns the array of words.");
    var result = rs.indexOf("e");
    equal(result, 1);

    rs = new RiString("Returns the array of words .");
    result = rs.indexOf("a");
    equal(result, 12);

    rs = new RiString("s ."); //space
    result = rs.indexOf(" ");
    equal(result, 1);

    rs = new RiString("s  ."); //double space
    result = rs.indexOf("  ");
    equal(result, 1);

    rs = new RiString("s    ."); //tab space
    result = rs.indexOf("   ");
    equal(result, 1);

    rs = new RiString(" abc"); //space
    result = rs.indexOf(" ");
    equal(result, 0);

    rs = new RiString("  abc"); //double space
    result = rs.indexOf("  ");
    equal(result, 0);

    rs = new RiString(" abc"); //tab space
    result = rs.indexOf("   ");
    equal(result, -1);

    rs = new RiString("Returns the array of words .");
    result = rs.indexOf("array");
    equal(result, 12);

    rs = new RiString("Returns the array of words.");
    result = rs.indexOf(",");
    equal(result, -1);

    rs = new RiString("Returns the array of words. Returns the array of words.");
    result = rs.indexOf("a", 13);
    equal(result, 15);

    rs = new RiString("Returns the array of words. Returns the array of words?");
    result = rs.indexOf("array", 13);
    equal(result, 40);

    rs = new RiString("Returns the array of words. Returns the array of words.");
    result = rs.indexOf("");
    equal(result, 0);

  });

  test("testInsertWord", function () {

    var rs = new RiString("Inserts at wordIdx and shifts each subsequent word accordingly.");
    var result = rs.insertWord(4, "then");
    equal(result.text(), "Inserts at wordIdx and then shifts each subsequent word accordingly.");

    rs = new RiString("inserts at wordIdx and shifts each subsequent word accordingly.");
    rs.insertWord(0, "He");
    equal(rs.text(), "He inserts at wordIdx and shifts each subsequent word accordingly.");

    rs = new RiString("Inserts at wordIdx and shifts each subsequent word accordingly.");
    rs.insertWord(1, "newWord");
    var rs2 = new RiString(
      "Inserts newWord at wordIdx and shifts each subsequent word accordingly.");
    equal(rs.text(), rs2.text());

    rs = new RiString("Inserts at wordIdx and shifts each subsequent word accordingly.");
    rs.insertWord(1, "newWord and newWords");
    rs2 = new RiString(
      "Inserts newWord and newWords at wordIdx and shifts each subsequent word accordingly.");
    equal(rs.text(), rs2.text());

    rs = new RiString("Inserts at wordIdx and shifts each subsequent word accordingly.");
    rs.insertWord(5, "");

    rs2 = "Inserts at wordIdx and shifts each subsequent word accordingly.";
    equal(rs.text(), rs2);

    rs = new RiString("Inserts at wordIdx and shifts each subsequent word accordingly.");
    rs.insertWord(5, "**");
    equal(rs.text(), "Inserts at wordIdx and shifts ** each subsequent word accordingly.");

    rs = new RiString("Inserts at wordIdx shifting each subsequent word accordingly.");
    rs.insertWord(3, ",");
    rs2 = new RiString(
      "Inserts at wordIdx , shifting each subsequent word accordingly.");
    equal(rs.text(), rs2.text());

    rs = new RiString("Inserts at wordIdx and shifts each subsequent word accordingly.");
    rs.insertWord(-2, "newWord");
    equal(rs.text(), "Inserts at wordIdx and shifts each subsequent word newWord accordingly.");
  });

  test("testLastIndexOf", function () {

    // check that these are ok --- ------------
    var rs = new RiString("Start at first character. Start at last character.");
    var result = rs.lastIndexOf("r");
    equal(result, 48);

    rs = new RiString("Start at first character. Start at last character.");
    result = rs.lastIndexOf("Start");
    equal(result, 26);

    rs = new RiString("Start at first character. Start at last character.");
    result = rs.lastIndexOf("start");
    equal(result, -1);

    rs = new RiString("Start at first character. Start at last character.");
    result = rs.lastIndexOf("a", 12);
    equal(result, 6);

    rs = new RiString("Start at first character. Start at last character.");
    result = rs.lastIndexOf("at", 12);
    equal(result, 6);

    rs = new RiString("Start at first character. Start at last character.");
    result = rs.lastIndexOf("");
    equal(result, rs.length()); // should be 50 or -1? 50(DCH)
  });

  test("testLength", function () {

    var rs = new RiString("S");
    var result = rs.length();
    equal(result, 1);

    rs = new RiString("s "); //space
    result = rs.length();
    equal(result, 2);

    rs = new RiString("s" + '\t'); //tab space
    result = rs.length();
    equal(result, 2);

    rs = new RiString(" s "); //2 space
    result = rs.length();
    equal(result, 3);

    rs = new RiString('\t' + "s" + '\t'); // 2 tab space
    result = rs.length();
    equal(result, 3);

    rs = new RiString("s b");
    result = rs.length();
    equal(result, 3);

    rs = new RiString("s b.");
    result = rs.length();
    equal(result, 4);

    rs = new RiString("s b ."); //space
    result = rs.length();
    equal(result, 5);

    rs = new RiString("><><><#$!$@$@!$");
    result = rs.length();
    equal(result, 15);

    rs = new RiString("");
    result = rs.length();
    equal(result, 0);
  });

  test("testMatch", function () {

    var rs = new RiString("The rain in SPAIN stays mainly in the plain");
    var result = rs.match(/ain/g);
    deepEqual(result, ["ain", "ain", "ain"]);

    rs = new RiString("The rain in SPAIN stays mainly in the plain");
    result = rs.match(/ain/gi);
    deepEqual(result, ["ain", "AIN", "ain", "ain"]);

    rs = new RiString("Watch out for the rock!");
    result = rs.match(/r?or?/g);
    deepEqual(result, ["o", "or", "ro"]);

    rs = new RiString("abc!");
    result = rs.match(/r?or?/g);
    deepEqual(result, []);

    rs = new RiString("Letter !>D? hello 213331123");
    result = rs.match(/[A-Za-z]/g);
    deepEqual(result, ["L", "e", "t", "t", "e", "r", "D", "h", "e", "l", "l", "o"]);

    rs = new RiString("Letter !>D? hello 213331123");
    result = rs.match(/\W/g);
    deepEqual(result, [" ", "!", ">", "?", " ", " "]);

    rs = new RiString("Letter !>D? hello 213331123");
    result = rs.match(/[^0-9]/g);
    deepEqual(result, ["L", "e", "t", "t", "e", "r", " ", "!", ">", "D", "?", " ", "h", "e", "l", "l", "o", " "]);

    rs = new RiString("!@#$%^&*()__+");
    result = rs.match(/X|Z/g);
    deepEqual(result, []);

    rs = new RiString("!@#$%^&*()__+");
    result = rs.match(/!|Z/g);
    deepEqual(result, ["!"]);

    rs = new RiString("The rain in SPAIN stays mainly in the plain");
    result = rs.match(/ain/g);
    deepEqual(result, ["ain", "ain", "ain"]);

    //case-insensitive tests
    rs = new RiString("The rain in SPAIN stays mainly in the plain");
    result = rs.match(/ain/gi);
    deepEqual(result, ["ain", "AIN", "ain", "ain"]);
  });

  test("testPos", function () {

    // check that these are ok ---------------

    var rs = new RiString("asdfaasd");
    var result = rs.pos();
    deepEqual(result, ["nn"]);

    rs = new RiString("clothes");
    result = rs.pos();
    deepEqual(result, ["nns"]);

    if (noLexicon()) return;

    rs = new RiString("There is a cat.");
    result = rs.pos();
    deepEqual(result, ["ex", "vbz", "dt", "nn", "."]);

    rs = new RiString("The boy, dressed in red, ate an apple.");
    result = rs.pos();
    deepEqual(result, ["dt", "nn", ",", "vbn", "in", "jj", ",", "vbd", "dt", "nn", "."]);

  });

  test("testPosAt", function () {

    // check that these are ok ---------------

    var rs = new RiString("The emperor had no clothes on.");
    var result = rs.posAt(4);
    equal("nns", result);

    rs = new RiString("There is a cat.");
    result = rs.posAt(3);
    equal("nn", result);

    if (noLexicon()) return;

    rs = new RiString("There is a cat.");
    result = rs.posAt(2);
    equal("dt", result);

    // out of range tests

    rs = new RiString("There is a cat.");
    result = rs.posAt(-3);
    equal("dt", result);

    rs = new RiString("There is a cat.");
    result = rs.posAt(-1);
    equal(".", result);

    rs = new RiString("There is a cat.");
    result = rs.posAt(300);
    // console.log("res="+result);
    equal(".", result);
  });

  test("testRemoveChar", function () {

    var rs = new RiString("The dog was white");
    rs.removeChar(1);
    equal(rs.text(), "Te dog was white");

    rs = new RiString("The dog was white");
    rs.removeChar(rs.length() - 1);
    equal(rs.text(), "The dog was whit");

    rs = new RiString("The dog was white");
    rs.removeChar(rs.length());
    equal(rs.text(), "The dog was white");

    rs = new RiString("The dog was white");
    rs.removeChar(0);
    equal(rs.text(), "he dog was white");

    rs = new RiString("The dog was white");
    rs.removeChar(-1);
    equal(rs.text(), "The dog was whit");

    rs = new RiString("The dog was white");
    rs.removeChar(1000);
    equal(rs.text(), "The dog was white");

    rs = new RiString("The dog was white.");
    rs.removeChar(rs.length() - 1);
    equal(rs.text(), "The dog was white");
  });

  test("testInsertChar", function () {

    var rs = new RiString("Who are you?");
    rs.insertChar(2, "");
    equal(rs.text(), "Who are you?");

    rs = new RiString("Who are you?");
    rs.insertChar(2, "e");
    equal(rs.text(), "Wheo are you?");

    rs = new RiString("Who are you?");
    rs.insertChar(2, "ere");
    equal(rs.text(), "Whereo are you?");

    rs = new RiString("Who are you?");
    rs.insertChar(11, "!!");
    equal(rs.text(), "Who are you!!?");

    rs = new RiString("Who are you?");
    rs.insertChar(0, "me");
    equal(rs.text(), "meWho are you?");

    rs = new RiString("Who?");
    rs.insertChar(rs.length() - 1, "!");
    equal(rs.text(), "Who!?");

    rs = new RiString("Who?");
    rs.insertChar(rs.length(), "!");
    equal(rs.text(), "Who?!");

    rs = new RiString("Who are you");
    rs.insertChar(-1, "?");
    equal(rs.text(), "Who are yo?u");
  });

  test("testReplaceChar", function () {

    var rs = new RiString("Who are you?");
    rs.replaceChar(2, "");
    equal(rs.text(), "Wh are you?");

    rs = new RiString("Who are you?");
    rs.replaceChar(2, "e");
    equal(rs.text(), "Whe are you?");

    rs = new RiString("Who are you?");
    rs.replaceChar(2, "ere");
    equal(rs.text(), "Where are you?");

    rs = new RiString("Who are you?");
    rs.replaceChar(11, "!!");
    equal(rs.text(), "Who are you!!");

    rs = new RiString("Who are you?");
    rs.replaceChar(0, "me");
    equal(rs.text(), "meho are you?");

    rs = new RiString("Who are you?");
    rs.replaceChar(-1, "me");
    equal(rs.text(), "Who are youme");

    rs = new RiString("Who are you?");
    rs.replaceChar(10000, "me");
    equal(rs.text(), "Who are you?");

  });

  test("testReplaceFirst", function () {

    // TODO: check against Java tests (should work the same) [C]

    var rs = new RiString("Who are you?");
    rs.replaceFirst("e", "E");
    equal(rs.text(), "Who arE you?");

    rs = new RiString("Who are you?");
    rs.replaceFirst("o", "O");
    equal(rs.text(), "WhO are you?");

    rs = new RiString("Who are you?");
    rs.replaceFirst("Who", "Where");
    equal(rs.text(), "Where are you?");

    rs = new RiString("Who are you?");
    rs.replaceFirst("notExist", "Exist");
    equal(rs.text(), "Who are you?");

    rs = new RiString("Who are you?");
    rs.replaceFirst("Who are", "Dare");
    equal(rs.text(), "Dare you?");

    rs = new RiString("Who are you?");
    rs.replaceFirst("Who aRe", "Dare");
    equal(rs.text(), "Who are you?");

    rs = new RiString("Who are you? Who are you?");
    rs.replaceFirst("Who are", "Dare");
    equal(rs.text(), "Dare you? Who are you?");

    rs = new RiString("Who are you?");
    rs.replaceFirst("", "");
    equal(rs.text(), "Who are you?");

    //regex

    rs = new RiString("The rain in SPAIN stays mainly in the plain");
    rs.replaceFirst(/ain/, "ane");
    equal(rs.text(), "The rane in SPAIN stays mainly in the plain");

    rs = new RiString("The rain in SPAIN stays mainly in the plain");
    rs.replaceFirst(/ain/i, "oll");
    equal(rs.text(), "The roll in SPAIN stays mainly in the plain");

    rs = new RiString("Watch out for the rock!");
    rs.replaceFirst(/r?or?/, "a");
    equal(rs.text(), "Watch aut for the rock!");

    rs = new RiString("The rain in SPAIN stays mainly in the plain");
    rs.replaceFirst(/in/, "");
    equal(rs.text(), "The ra in SPAIN stays mainly in the plain");

    rs = new RiString("Who are you?");
    rs.replaceFirst("?", "?!");
    equal(rs.text(), "Who are you?!");

    // global ('g') should be ignored
    rs = new RiString("The rain in SPAIN stays mainly in the plain");
    rs.replaceFirst(/ain/g, "ane");
    equal(rs.text(), "The rane in SPAIN stays mainly in the plain");

    rs = new RiString("The rain in SPAIN stays mainly in the plain");
    rs.replaceFirst(/ain/gi, "oll");
    equal(rs.text(), "The roll in SPAIN stays mainly in the plain");

    rs = new RiString("Watch out for the rock!");
    rs.replaceFirst(/r?or?/g, "a");
    equal(rs.text(), "Watch aut for the rock!");

    rs = new RiString("The rain in SPAIN stays mainly in the plain");
    rs.replaceFirst(/in/g, "");
    equal(rs.text(), "The ra in SPAIN stays mainly in the plain");
  });

  test("testReplaceAll", function () {

    // TODO: check against Java tests (should work the same) [C]

    var rs = new RiString("Who are you? Who is he? Who is it?");
    equal(rs.replaceAll("e", "E").text(), "Who arE you? Who is hE? Who is it?");

    rs = new RiString("Who are you? Who is he? Who is it?");
    equal(rs.replaceAll("Who", "O").text(), "O are you? O is he? O is it?");

    rs = new RiString("Whom is he? Where is he? What is it?");
    equal(rs.replaceAll("Wh*", "O").text(), "Whom is he? Where is he? What is it?");

    rs = new RiString("%^&%&?");
    equal(rs.replaceAll("%^&%&?", "!!!").text(), "!!!");

    rs = new RiString("Who are you?");
    equal(rs.replaceAll("notExist", "Exist").text(), "Who are you?");

    rs = new RiString("Who are you?");
    equal(rs.replaceAll("", "").text(), "Who are you?");

    rs = new RiString("");
    equal(rs.replaceAll("", "").text(), "");

    // regex tests (global flag: should have same result with/w'out global flag)

    rs = new RiString("The rain in SPAIN stays mainly in the plain");
    rs.replaceAll(/ain/, "ane");
    equal(rs.text(), "The rane in SPAIN stays manely in the plane");
    rs.replaceAll(/ain/g, "ane");
    equal(rs.text(), "The rane in SPAIN stays manely in the plane");

    rs = new RiString("The rain in SPAIN stays mainly in the plain");
    rs.replaceAll(/ain/i, "ane");
    equal(rs.text(), "The rane in SPane stays manely in the plane");

    rs = new RiString("Watch out for the rock!");
    rs.replaceAll(/ ?r/, "wood");
    equal(rs.text(), "Watch out fowood thewoodock!");

    rs = new RiString("The rain in SPAIN stays mainly in the plain");
    rs.replaceAll(/in/, "");
    equal(rs.text(), "The ra  SPAIN stays maly  the pla");

    rs = new RiString("Who wuz you?");
    rs.replaceAll(/ou?/, "?!");
    equal(rs.text(), "Wh?! wuz y?!?");

    rs = new RiString("Who wuz you?");
    rs.replaceAll("ou?", "?!");
    equal(rs.text(), "Who wuz y?!");

    rs = new RiString("Who are you{1,}");
    rs.replaceAll("{1,}", "!");
    equal(rs.text(), "Who are you!");

    rs = new RiString("Who are you*");
    rs.replaceAll("*", "!");
    equal(rs.text(), "Who are you!");

    rs = new RiString("Who are you+");
    rs.replaceAll("+", "!");
    equal(rs.text(), "Who are you!");

    rs = new RiString("Who are you?");
    rs.replaceAll("?", "?!");
    equal(rs.text(), "Who are you?!");
  });

  test("testRemoveWord", function () {

    var rs = new RiString("Who are you?");
    rs.removeWord(2);
    equal(rs.text(), "Who are?");

    rs = new RiString("Who are you?");
    rs.removeWord(3);
    equal(rs.text(), "Who are you");

    rs = new RiString("Who are you?");
    rs.removeWord(20);
    equal(rs.text(), "Who are you?");

    rs = new RiString("Who are you?");
    equal(rs.removeWord(0).text(), "are you?");

    rs = new RiString("Who are you?");
    rs.removeWord(-1);
    equal(rs.text(), "Who are you"); // TODO: should go from back
  });

  test("testReplaceWord", function () {

    var rs = new RiString("Who are you?");
    rs.replaceWord(2, ""); // nice! this too...
    equal(rs.text(), "Who are?"); // strange case, not sure
    // could also be: equal(rs.text(), "Who are ?");

    rs = new RiString("Who are you?");
    equal("Who are What?", rs.replaceWord(2, "What").text());

    rs = new RiString("Who are you?");
    equal(rs.replaceWord(0, "What").text(), "What are you?");

    rs = new RiString("Who are you?");
    rs.replaceWord(3, "!!");
    equal(rs.text(), "Who are you!!"); // nice! this is a strange one...

    rs = new RiString("Who are you?");
    rs.replaceWord(-1, ".");
    equal(rs.text(), "Who are you.");

    rs = new RiString("Who are you?");
    rs.replaceWord(20, "asfasf");
    equal(rs.text(), "Who are you?");
  });

  test("testSlice", function () {

    var rs = new RiString(
      "The Australian Pavilion at the Venice Biennale is getting a much-needed facelift.");
    equal(rs.slice(1, 3), "he");

    rs = new RiString(
      "The Australian Pavilion at the Venice Biennale is getting a much-needed facelift.");
    equal(rs.slice(-3, -2), "f");

    rs = new RiString(
      "The Australian Pavilion at the Venice Biennale is getting a much-needed facelift.");
    equal(rs.slice(15, 500),
      "Pavilion at the Venice Biennale is getting a much-needed facelift.");

    rs = new RiString("!@#$%^&**()");
    equal(rs.slice(2, 5), "#$%");

    rs = new RiString(
      "The Australian Pavilion at the Venice Biennale is getting a much-needed facelift.");
    equal(rs.slice(15, 500),
      "Pavilion at the Venice Biennale is getting a much-needed facelift.");

    rs = new RiString("The Australian");
    equal(rs.slice(-5, -3), "al");

    rs = new RiString(
      "The Australian Pavilion at the Venice Biennale is getting a much-needed facelift.");
    equal(rs.slice(500, 501), "");

    rs = new RiString(
      "The Australian Pavilion at the Venice Biennale is getting a much-needed facelift.");
    equal(rs.slice(10, 10), "");

    rs = new RiString(
      "The Australian Pavilion at the Venice Biennale is getting a much-needed facelift.");
    equal(rs.slice(3, 1), "");

    rs = new RiString(
      "The Australian Pavilion at the Venice Biennale is getting a much-needed facelift.");
    equal(rs.slice(-2, 3), "");

    rs = new RiString(
      "The Australian Pavilion at the Venice Biennale is getting a much-needed facelift.");
    equal(rs.slice(-2, -3), "");

  });

  test("testSplit", function () {

    var rs = new RiString("Who are you?");
    var result = rs.split("?");
    var answer = [RiString("Who are you")];
    deepEqual(result, answer);

    rs = new RiString("Who are you?");
    result = rs.split("\\?");
    answer = [RiString("Who are you?")];
    deepEqual(result, answer);

    rs = new RiString("Who are you?");
    result = rs.split();
    answer = [RiString("Who are you?")];
    deepEqual(result, answer);

    rs = new RiString("Who are you?");
    result = rs.split(" ");
    answer = [RiString("Who"), RiString("are"), RiString("you?")];
    deepEqual(result, answer);

    rs = new RiString("Who are you?");
    result = rs.split("are");
    answer = [RiString("Who "), RiString(" you?")];
    deepEqual(result, answer);

    rs = new RiString("Who are you?");
    result = rs.split("W");
    //console.log(result);
    answer = [RiString("ho are you?")];
    deepEqual(result, answer);

    rs = new RiString("Who are you?");
    result = rs.split("abc");
    answer = [RiString("Who are you?")];
    deepEqual(result, answer);

    rs = new RiString("Who are you?");
    result = rs.split("");
    answer = [];
    var chars = ["W", "h", "o", " ", "a", "r", "e", " ", "y", "o", "u", "?"];
    for (var i = 0; i < chars.length; i++) {
      answer[i] = RiString(chars[i]);
    }
    deepEqual(result, answer);

    rs = new RiString("Who are you?");
    result = rs.split("", 3);
    answer = [RiString("W"), RiString("h"), RiString("o")];
    deepEqual(result, answer);

    rs = new RiString("Who are you?");
    result = rs.split("", 0);
    answer = [];
    deepEqual(result, answer);

    rs = new RiString("Who are you?");
    result = rs.split("", 100);
    answer = [];
    chars = ["W", "h", "o", " ", "a", "r", "e", " ", "y", "o", "u", "?"];
    for (i = 0; i < chars.length; i++) {
      answer[i] = RiString(chars[i]);
    }
    deepEqual(result, answer);

  });

  test("testStartsWith", function () {

    var rs = new RiString(
      "The Australian Pavilion at the Venice Biennale is getting a much-needed facelift.");
    ok(rs.startsWith("T"));

    rs = new RiString(
      "The Australian Pavilion at the Venice Biennale is getting a much-needed facelift.");
    ok(rs.startsWith("The"));

    rs = new RiString(
      "The Australian Pavilion at the Venice Biennale is getting a much-needed facelift.");
    ok(!rs.startsWith("Aus"));

    rs = new RiString(
      "The Australian Pavilion at the Venice Biennale is getting a much-needed facelift.");
    ok(!rs.startsWith("*"));

    rs = new RiString(
      "*The Australian Pavilion at the Venice Biennale is getting a much-needed facelift.");
    ok(rs.startsWith("*"));

    rs = new RiString(
      " The Australian Pavilion at the Venice Biennale is getting a much-needed facelift.");
    ok(rs.startsWith(" ")); //single space

    rs = new RiString(
      "  The Australian Pavilion at the Venice Biennale is getting a much-needed facelift.");
    ok(rs.startsWith("  ")); //double space

    rs = new RiString(
      " The Australian Pavilion at the Venice Biennale is getting a much-needed facelift.");
    ok(!rs.startsWith("  ")); // tab space
  });

  test("testSubstring", function () {

    var rs = new RiString(
      "The Australian Pavilion at the Venice Biennale is getting a much-needed facelift.");
    equal(rs.substring(1, 3), "he");

    rs = new RiString(
      "The Australian Pavilion at the Venice Biennale is getting a much-needed facelift.");
    equal(rs.substring(3, 1), "he");

    rs = new RiString(
      "The Australian Pavilion at the Venice Biennale is getting a much-needed facelift.");
    equal(rs.substring(-2, 3), "The");

    rs = new RiString(
      "The Australian Pavilion at the Venice Biennale is getting a much-needed facelift.");
    equal(rs.substring(15, 500),
      "Pavilion at the Venice Biennale is getting a much-needed facelift.");

    rs = new RiString(
      "The Australian Pavilion at the Venice Biennale is getting a much-needed facelift.");
    equal(rs.substring(500, 501), "");

    rs = new RiString(
      "The Australian Pavilion at the Venice Biennale is getting a much-needed facelift.");
    equal(rs.substring(10, 10), "");

    rs = new RiString("!@#$%^&**()");
    equal(rs.substring(2, 5), "#$%");

    rs = new RiString("Start at first character.");
    equal(rs.substring(1, 5), "tart");

    rs = new RiString("Start at first character.");
    equal(rs.substring(0, 1), "S");

    rs = new RiString("Start at first character.");
    equal(rs.substring(0, 1), "S");

    rs = new RiString("Start at first character.");
    equal(rs.substring(3), "rt at first character.");

  });

  test("testSubstr", function () {

    var rs = new RiString(
      "The Australian Pavilion at the Venice Biennale is getting a much-needed facelift.");
    equal(rs.substr(1, 3), "he ");

    rs = new RiString(
      "The Australian Pavilion at the Venice Biennale is getting a much-needed facelift.");
    equal(rs.substr(-2, 3), "t.");

    rs = new RiString(
      "The Australian Pavilion at the Venice Biennale is getting a much-needed facelift.");
    equal(rs.substr(15, 500),
      "Pavilion at the Venice Biennale is getting a much-needed facelift.");

    rs = new RiString(
      "The Australian Pavilion at the Venice Biennale is getting a much-needed facelift.");
    equal(rs.substr(500, 501), "");

    rs = new RiString(
      "The Australian Pavilion at the Venice Biennale is getting a much-needed facelift.");
    equal(rs.substr(10, 10), "lian Pavil");

    rs = new RiString("!@#$%^&**()");
    equal(rs.substr(2, 5), "#$%^&");

  });

  test("testText", function () {

    // check that these are ok ---------------
    var rs = new RiString("this door is closed");
    var result = rs.text();
    equal(result, "this door is closed");

    rs = new RiString("this door, is closed.*&)*^");
    result = rs.text();
    equal(result, "this door, is closed.*&)*^");

    rs = new RiString("   this door    , is closed.");
    result = rs.text();
    equal(result, "   this door    , is closed.");

    rs = new RiString("this Door is closed");
    result = rs.text();
    notEqual(result, "this door is closed");

    rs = new RiString("");
    result = rs.text();
    equal(result, "");

    // no error checks needed
  });

  test("testToCharArray()", function () {
    ok(1);
    // no error checks needed
  });

  test("testToLowerCase", function () {

    var rs = new RiString("The Australian Pavilion.");
    rs.toLowerCase();
    equal("the australian pavilion.", rs.text());

    rs = new RiString("the Australian pavilion.");
    rs.toLowerCase();
    equal("the australian pavilion.", rs.text());

    rs = new RiString(")(*(&^%%$!#$$%%^))");
    rs.toLowerCase();
    equal(")(*(&^%%$!#$$%%^))", rs.text());

    // no error checks needed
  });

  test("testToUpperCase", function () {

    var rs = new RiString("The Australian Pavilion.");
    equal("THE AUSTRALIAN PAVILION.", rs.toUpperCase().text());

    rs = new RiString(")(*(&^%%$!#$$%%^))");
    rs.toUpperCase();
    equal(")(*(&^%%$!#$$%%^))", rs.text());

    // no error checks needed
  });

  test("testTrim", function () {

    // check that these are ok ---------------
    var rs = new RiString("Start at first character. ");
    equal(rs.trim().text(), "Start at first character.");

    rs = new RiString(" Start at first character.");
    equal(rs.trim().text(), "Start at first character.");

    rs = new RiString("     Start at first character.   "); // tabs
    equal(rs.trim().text(), "Start at first character.");

    rs = new RiString("     Start at first character.    "); // spaces/tabs
    equal(rs.trim().text(), "Start at first character.");

    rs = new RiString("Start at first character.\t"); //\t
    equal(rs.trim().text(), "Start at first character.");

    rs = new RiString("\t\t\tStart at first character.\t"); //\t
    equal(rs.trim().text(), "Start at first character.");
  });

  test("testWordAt", function () {

    var rs = new RiString("Returns the word at wordIdx using the default WordTokenizer.");
    var result = rs.wordAt(0);
    equal(result, "Returns");

    result = rs.wordAt(1);
    equal(result, "the");

    result = rs.wordAt(9);
    equal(result, ".");

    result = rs.wordAt(500);
    equal(result, "");

    result = rs.wordAt(-5);
    equal(result, "");

    rs = new RiString("");
    result = rs.wordAt(0);
    equal(result, "");

  });

  test("testWordCount", function () {

    // check that these are ok --- ------------
    var rs = new RiString("Returns the word at wordIdx using the default WordTokenizer.");
    var result = rs.wordCount();
    equal(result, 10); // correct, according to WordTokenizer, need to try with RegexTokenizer

    rs = new RiString("Returns the word.Returns the word. Returns the word .");
    result = rs.wordCount();
    equal(result, 12);

    rs = new RiString("   Returns the word at wordIdx , using the default WordTokenizer."); //space
    result = rs.wordCount();
    equal(result, 11);

    rs = new RiString(" Returns the word at wordIdx , using the default WordTokenizer.  "); //tab space
    result = rs.wordCount();
    equal(result, 11);

    rs = new RiString("");
    result = rs.wordCount();
    equal(result, 0);

  });

  test("testWords", function () {

    // check that these are ok ---------------
    var rs = new RiString("Returns the array of words.");
    var result = rs.words();
    var answer = ["Returns", "the", "array", "of", "words", "."];
    deepEqual(result, answer);

    rs = new RiString("The boy, dressed in red, ate an array.");
    result = rs.words();
    answer = ["The", "boy", ",", "dressed", "in", "red", ",", "ate", "an", "array", "."];
    deepEqual(result, answer);

    rs = new RiString("Returns the array of words .");
    result = rs.words();
    answer = ["Returns", "the", "array", "of", "words", "."];
    deepEqual(result, answer);

    rs = new RiString("The boy, dressed in red , ate an array?");
    result = rs.words();
    answer = ["The", "boy", ",", "dressed", "in", "red", ",", "ate", "an", "array", "?"];
    deepEqual(result, answer);

    rs = new RiString("");
    result = rs.words();
    answer = [""];
    deepEqual(result, answer);

    // no error checks needed
  });

  test("testGet", function () {

    var rs = RiString("The laggin dragon").analyze();
    ok(rs);
    ok(rs.features());

    if (noLexicon()) return;

    var ph = rs.get(RiTa.PHONEMES);
    var sy = rs.get(RiTa.SYLLABLES);
    var st = rs.get(RiTa.STRESSES);
    ok(ph && sy && st);


    equal(ph, "dh-ax l-ae-g-ih-n d-r-ae-g-aa-n");
    equal(sy, "dh-ax l-ae/g-ih-n d-r-ae-g/aa-n");
    equal(st, "0 1/1 1/0");
  });

  test("testSet", function () {

    var rs = new RiString("Mom & Dad");
    rs.set("Id", "1000"); // TODO: test that this does not create default features
    equal(rs.get("Id"), "1000");

    //console.log(rs.get(RiTa.PHONEMES));

    var features = rs.features();
    //console.log(features);

    ok(features[RiTa.PHONEMES] !== null);

    rs.text("Dad & Mom"); // reset all original features, but not those set() by user

    equal(features[RiTa.PHONEMES], null); // OK: has been reset
    equal(rs.get("Id"), "1000"); // OK:  has not been reset
  });

  // private tests, no need to extract results

  test("test_syllabifyString", function () {

    var test = "ao2-r-g-ah0-n-ah0-z-ey1-sh-ah0-n-z";
    var expected = "ao2-r g-ah0 n-ah0 z-ey1 sh-ah0-n-z";
    var result = RiString._syllabify(test);
    deepEqual(result, expected);

    var data = [
      ['d-eh1-n-l-ih0-n-jh-er0', 'd-eh1-n l-ih0-n jh-er0'],
      ['d-uw1-ah0-l', 'd-uw1 ah0-l'],
      ['d-ih2-s-ah0-l-aw1-d', 'd-ih2 s-ah0 l-aw1-d'],
      ['d-aa1-d-z', 'd-aa1-d-z'],
      ['d-r-ao1-l-z', 'd-r-ao1-l-z'],
      ['d-ay0-ae1-n-ah0', 'd-ay0 ae1 n-ah0'],
      ['ey1-t-f-ow2-l-d', 'ey1-t f-ow2-l-d'],
      ['eh1-m-t-iy0-d', 'eh1-m t-iy0-d'],
      ['ih0-r-ey1-s', 'ih0 r-ey1-s'],
      ['eh1-v-r-ah0-n', 'eh1-v r-ah0-n'],
      ['f-ae1-l-k', 'f-ae1-l-k'],
      ['f-eh1-n-w-ey2', 'f-eh1-n w-ey2'],
      ['f-ih1-sh-k-ih2-l', 'f-ih1-sh k-ih2-l'],
      ['f-ao1-r-b-ih0-d-ah0-n', 'f-ao1-r b-ih0 d-ah0-n'],
      ['f-r-eh1-n-t-s', 'f-r-eh1-n-t-s'],
      ['g-ae1-l-b-r-ey2-th', 'g-ae1-l b-r-ey2-th'],
      ['zh-ih0-l-eh1-t', 'zh-ih0 l-eh1-t'],
      ['jh-ih1-n-iy0', 'jh-ih1 n-iy0'],
      ['g-aa0-n-z-aa1-l-ah0-z', 'g-aa0-n z-aa1 l-ah0-z'],
      ['g-r-iy1-n-f-iy2-l-d', 'g-r-iy1-n f-iy2-l-d'],
      ['g-ih0-t-aa1-r-z', 'g-ih0 t-aa1-r-z'],
      ['hh-ae1-m-er0-ih0-ng', 'hh-ae1 m-er0 ih0-ng'],
      ['hh-ae1-t-ih0-n-d-ao0-r-f', 'hh-ae1 t-ih0-n d-ao0-r-f'],
      ['hh-eh1-m-ih0-ng-w-ey2', 'hh-eh1 m-ih0-ng w-ey2'],
      ['hh-ih1-ng-k-m-ah0-n', 'hh-ih1-ng-k m-ah0-n'],
      ['hh-ow1-n-eh0-k', 'hh-ow1 n-eh0-k'],
      ['hh-ah1-l-d', 'hh-ah1-l-d'],
      ['ih0-l-uw1-zh-ah0-n', 'ih0 l-uw1 zh-ah0-n'],
      ['ih0-n-f-ae2-ch-uw0-ey1-sh-ah0-n', 'ih0-n f-ae2 ch-uw0 ey1 sh-ah0-n'],
      ['ih0-n-t-er1-n-ah0-l-ay2-z', 'ih0-n t-er1 n-ah0 l-ay2-z'],
      ['ih0-z-ae1-n-s-k-iy0-z', 'ih0 z-ae1-n s-k-iy0-z'],
      ['jh-ow0-hh-ae1-n-ah0-s', 'jh-ow0 hh-ae1 n-ah0-s'],
      ['k-ae1-r-ah0-m', 'k-ae1 r-ah0-m'],
      ['k-aa1-k-iy0', 'k-aa1 k-iy0'],
      ['n-ey1-v', 'n-ey1-v']
    ];
    for (var i = 0, l = data.length; i < l; i++) {
      var res = RiString._syllabify(data[i][0]);
      equal(res, data[i][1]);
    }
  });

  test("test_stringify", function () {

    var data = [
      [
        [2],
        [],
        ['ao'],
        ['r']
      ],
      [
        [0],
        ['g'],
        ['ah'],
        []
      ],
      [
        [0],
        ['n'],
        ['ah'],
        []
      ],
      [
        [1],
        ['z'],
        ['ey'],
        []
      ],
      [
        [0],
        ['sh'],
        ['ah'],
        ['n', 'z']
      ]
    ];
    var out = "ao2-r g-ah0 n-ah0 z-ey1 sh-ah0-n-z";
    equal(RiString._stringify(data), out);
  });

  test("test_syllabifyArray", function () {

    var test = "ao2-r-g-ah0-n-ah0-z-ey1-sh-ah0-n-z".split('-');
    var expected = "ao2-r g-ah0 n-ah0 z-ey1 sh-ah0-n-z";
    var result = RiString._syllabify(test);
    deepEqual(result, expected);
  });

  function noLexicon() {
    if (!RiLexicon.enabled) {
      if (!lexWarningRiString) {
        lexWarningRiString = true;
        console.warn('[INFO] RiString-tests: skipping lexicon-required tests');
      }
      ok(1);
      return true;
    }
  } var lexWarningRiString = false;
};

if (typeof exports != 'undefined') {
  runtests();
}
