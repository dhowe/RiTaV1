/*global console, test, throws, equal, fail, notEqual, expect, require, ok,
    QUnit, RiTa, RiTaEvent, RiString, RiGrammar, RiMarkov, RiLexicon */

var runtests = function() {

    var allowLocalWebServer = false;

    RiTa.SILENT = 1;

    var filePath = (typeof module != 'undefined' && module.exports) ? "./test/html/data/" : "./data/"

    QUnit.module("UrlLoading", {

      setup: function() {},
      teardown: function() {}
    });

    asyncTest("RiTa.loadString1(file)", function() {

        RiTa.loadString(filePath + "sentence1.json", function(s) {
            ok(s && s.length > 100);
            //console.log(s);
            ok(JSON.parse(s));
            start();
        });
    });

    asyncTest("RiTa.loadString2(file)", function() {

        RiTa.loadString(filePath + "sentence2.json", function(s) {
            ok(s && s.length > 100);
            ok(JSON.parse(s));
            start();
        });
    });

    asyncTest("RiTa.testLoadConcordance", function () {  // SLOW

        RiTa.loadString(filePath + "kafka.txt", function (txt) {

            // test with all false
            var args = {
                ignoreCase: false,
                ignorePunctuation: false,
                ignoreStopWords: false
            }
            data = RiTa.concordance(txt, args);
            ok(data["Gregor"] == 199);
            ok(data["Gregor"] + data["Gregor's"] == 298);
            ok(data["sister"] == 96);
            ok(data["sister"] + data["sister's"] == 101);
            ok(data["here"] == 19);
            ok(data["the"] == 1097);
            ok(data["The"] == 51);
            ok(data[","] == 1292);
            ok(data["."] == 737);


            // test all true
            var nUppercaseFather = data["Father"];
            var nLowercaseFather = data["father"];
            args.ignoreCase = true;
            args.ignorePunctuation = true;
            args.ignoreStopWords = true;
            data = RiTa.concordance(txt, args);
            ok(data["gregor"] + data["gregor's"] == 298);
            ok(data["sister"] + data["sister's"] == 101);
            equal(data["here"], null);
            equal(data["the"], null);
            equal(data[","], null);
            equal(data["."], null);
            ok(data["father"] == nUppercaseFather + nLowercaseFather);

            // test ignoreCase
            args.ignoreCase = true;
            args.ignorePunctuation = false;
            args.ignoreStopWords = false;
            data = RiTa.concordance(txt, args);
            ok(data["father"] == nUppercaseFather + nLowercaseFather);

            // test ignorePunctuation
            args.ignoreCase = false;
            args.ignorePunctuation = true;
            args.ignoreStopWords = false;
            data = RiTa.concordance(txt, args);
            equal(data[","], null);
            equal(data["."], null);

            // test ignoreStopWords
            args.ignoreCase = false;
            args.ignorePunctuation = false;
            args.ignoreStopWords = true;
            data = RiTa.concordance(txt, args);
            equal(data["here"], null);
            equal(data["the"], null);

            // test ignoreStopWords and ignorePunctuation
            args.ignoreCase = false;
            args.ignorePunctuation = true;
            args.ignoreStopWords = true;
            data = RiTa.concordance(txt, args);
            equal(data[","], null);
            equal(data["."], null);
            equal(data["here"], null);
            equal(data["the"], null);

            // test ignoreStopWords and ignoreCase
            args.ignoreCase = true;
            args.ignorePunctuation = false;
            args.ignoreStopWords = true;
            data = RiTa.concordance(txt, args);
            ok(data["father"] == nUppercaseFather + nLowercaseFather);
            equal(data["here"], null);
            equal(data["the"], null);

            // test ignorePunctuation and ignoreCase
            args.ignoreCase = true;
            args.ignorePunctuation = true;
            args.ignoreStopWords = false;
            data = RiTa.concordance(txt, args);
            ok(data["father"] == nUppercaseFather + nLowercaseFather);
            equal(data[","], null);
            equal(data["."], null);

            // test wordsToIgnore
            args.wordsToIgnore = ["father", "sister"];
            args.ignoreCase = false;
            args.ignorePunctuation = false;
            args.ignoreStopWords = false;
            data = RiTa.concordance(txt, args);
            equal(data["father"], null);
            equal(data["sister"], null);

            start();
        });
    });

    asyncTest("RiTa.testLoadKwicModel", function () {

        RiTa.loadString(filePath + "kafka.txt", function (txt) {

            var args = {
                ignoreCase: false,
                ignorePunctuation: false,
                ignoreStopWords: false
            }

            // test ignorePunctuation
            args.ignorePunctuation = false;
            lines = RiTa.kwic(txt, ",",  args);
            equal(lines.length, 1292);
            args.ignorePunctuation = true;
            lines = RiTa.kwic(txt, ",",  args);
            equal(lines.length, 0);

            // test ignoreCase
            args.wordCount = 4;
            args.ignoreCase = true;
            lines = RiTa.kwic(txt, "eventually",  args);
            equal(lines.length, 2);
            args.ignoreCase = false;
            lines = RiTa.kwic(txt, "eventually",  args);
            equal(lines.length, 1);

            // test ignoreStopWords
            lines = RiTa.kwic(txt, "here",  args);
            equal(lines.length, 19);
            args.ignoreStopWords = true;
            lines = RiTa.kwic(txt, "here",  args);
            equal(lines.length, 0);

            // test wordCount
            args.wordCount = 6;
            args.ignoreCase = false;
            lines = RiTa.kwic(txt, "sister",  args);
            for (var i = 0; i < lines.length; i++) {
                var length = RiTa.tokenize(lines[i]).length;
                equal(length, 6 + 1 + 6);
            }

            // test wordsToIgnore
            args.wordsToIgnore = ["father", "sister"];
            lines = RiTa.kwic(txt, "father",  args);
            equal(lines.length, 0);
            lines = RiTa.kwic(txt, "sister",  args);
            equal(lines.length, 0);

            start();
        });
    });

    // RiGrammar
    //////////////////////////////////////////////////////////////////////////////////////

    var sentenceGrammar = {
        "<start>" : "<noun_phrase> <verb_phrase>.",
        "<noun_phrase>" : "<determiner> <noun>",
        "<verb_phrase>" : "<verb> | <verb> <noun_phrase> [0.1]",
        "<determiner>" : "a [0.1] | the",
        "<noun>" : "woman | man",
        "<verb>" : "shoots"
    };

    var sentenceGrammar2 = {
        "<start>" : "<noun_phrase> <verb_phrase>.",
        "<noun_phrase>" : "<determiner> <noun>",
        "<determiner>" : [ "a [0.1]", "the" ],
        "<verb_phrase>" : [ "<verb> <noun_phrase> [0.1]", "<verb>" ],
        "<noun>" : ["woman", "man"],
        "<verb>" : "shoots"
    };

    asyncTest("RiGrammar.loadFrom(file)", function() {

        var rg1 = new RiGrammar();
        rg1.loadFrom(filePath + "sentence1.json");

        var rg2 = RiGrammar(JSON.stringify(sentenceGrammar));
        var rg3 = RiGrammar(JSON.stringify(sentenceGrammar2));

        var ts = +new Date();
        var id = setInterval(function() {

            if (rg1.ready()) {

                ok(rg1);
                deepEqual(rg1, rg2);
                deepEqual(rg1, rg3);
                start();
                clearInterval(id);
            }
            else {

                var now = +new Date();
                if (now - ts > 5000) {
                    equal("no result", 0);
                    start();
                    clearInterval(id);
                }
            }

        }, 50);
    });

    asyncTest("RiGrammar.loadFrom2(file)", function() {

        var rg1 = new RiGrammar();
        rg1.loadFrom(filePath + "sentence2.json");
        var rg2 = RiGrammar(JSON.stringify(sentenceGrammar));
        var rg3 = RiGrammar(JSON.stringify(sentenceGrammar2));

        var ts = +new Date();
        var id = setInterval(function() {

            if (rg1.ready()) {

                ok(rg1);
                deepEqual(rg1, rg2);
                deepEqual(rg1, rg3);
                start();
                clearInterval(id);
            }
            else {

                var now = +new Date();
                if (now - ts > 5000) {
                    equal("no result", 0);
                    start();
                    clearInterval(id);
                }
            }

        }, 50);
    });

    asyncTest("RiGrammar.loadFrom3(file)", function() {

        var rg1 = new RiGrammar();
        rg1.loadFrom(filePath + "sentence1.yaml");

        var rg2 = RiGrammar(JSON.stringify(sentenceGrammar));
        var rg3 = RiGrammar(JSON.stringify(sentenceGrammar2));

        var ts = +new Date();
        var id = setInterval(function() {

            if (rg1.ready()) {

                ok(rg1);
                deepEqual(rg1, rg2);
                deepEqual(rg1, rg3);
                start();
                clearInterval(id);
            }
            else {

                var now = +new Date();
                if (now - ts > 5000) {
                    equal("no result", 0);
                    start();
                    clearInterval(id);
                }
            }

        }, 50);
    });

    asyncTest("RiGrammar.loadFrom4(file)", function() {

        var rg1 = new RiGrammar();
        rg1.loadFrom(filePath + "sentence2.yaml");
        var rg2 = RiGrammar(JSON.stringify(sentenceGrammar));
        var rg3 = RiGrammar(JSON.stringify(sentenceGrammar2));

        var ts = +new Date();
        var id = setInterval(function() {

            if (rg1.ready()) {

                ok(rg1);
                deepEqual(rg1, rg2);
                deepEqual(rg1, rg3);

                start();
                clearInterval(id);
            }
            else {

                var now = +new Date();
                if (now - ts > 5000) {
                    equal("no result", 0);
                    start();
                    clearInterval(id);
                }
            }

        }, 50);
    });

    // RiMarkov
    //////////////////////////////////////////////////////////////////////////////////

    asyncTest("RiMarkov.loadFromFile", function() {

        var rm = new RiMarkov(2);
        rm.loadFrom(filePath + "kafka.txt");

        var ts = +new Date();
        var id = setInterval(function() {

            if (rm.ready()) {

                ok(rm.size());
                // TODO: 1 or 2 more better tests here

                clearInterval(id);
                start();

            } else {

                //console.log("waiting...");
                var now = +new Date();
                if (now - ts > 5000) {
                    equal("no result", 0);
                    start();
                    clearInterval(id);
                }
            }

        }, 50);
    });

    // SOME TESTS ON THE LOCAL WEBSERVER -- ignore unless allowRemote=true
    if (allowLocalWebServer) {

      asyncTest("RiTa.loadString1(url)", function() {

          RiTa.loadString("http://localhost/ritajs/test/data/sentence1.json", function(s) {

              ok(s && s.length > 100);
              ok(JSON.parse(s));
              start();
          });
      });

      asyncTest("RiTa.loadString2(url)", function() {

          RiTa.loadString("http://localhost/ritajs/test/data/kafka.txt", function(s) {
              ok(s && s.length > 65536);
              start();
          });
      });

      asyncTest("RiGrammar.loadFrom(Url)", function() {

          var grammar = new RiGrammar();
          grammar.loadFrom("http://localhost/ritajs/test/data/haikuGrammar.json");

          var ts = +new Date();
          var id = setInterval(function() {
              if (grammar.ready()) {
                  ok(grammar);
                  start();
                  clearInterval(id);
              } else {
                  var now = +new Date();
                  if (now - ts > 5000) {
                      equal("no result", 0);
                      start();
                      clearInterval(id);
                  }
              }

          }, 50);
      });

      asyncTest("RiMarkov.loadFromUrl", function() { // SLOW

          var rm = new RiMarkov(3);
          rm.loadFrom("http://localhost/ritajs/test/data/kafka.txt");

          var ts = +new Date();
          var id = setInterval(function() {
              if (rm.ready()) {
                  ok(rm.size());

                  // TODO: 1 or 2 more better tests here
                  clearInterval(id);
                  start();

              } else {

                  var now = +new Date();
                  if (now - ts > 5000) {
                      equal("no result", 0);
                      start();
                      clearInterval(id);
                  }
              }

          }, 50);
      });
    }
    else {
      //console.log("[INFO] UrlLoading-tests: skipping localhost tests");
    }

}// end runtests

if (typeof exports != 'undefined') {
  try {
    YAML = require('yamljs');
  }
  catch(e) {
    console.log("[WARN] require() didn't find a YAML parser");
  }
  runtests();
}
