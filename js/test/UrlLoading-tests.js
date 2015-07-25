/*global console, test, throws, equal, fail, notEqual, expect, require, ok,
    QUnit, RiTa, RiTaEvent, RiString, RiGrammar, RiMarkov, RiLexicon */

var runtests = function() {

    var filePath = (typeof module != 'undefined' && module.exports) ?
      "./test/data/" : "../data/", silentOrig;

    QUnit.module("UrlLoading", {
        setup : function() {
          silentOrig = RiTa.SILENT;
          RiTa.SILENT = true;
        },
        teardown : function() {
          RiTa.SILENT = silentOrig;
        }
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

    asyncTest("RiTa.loadString1(url)", function() {

        RiTa.loadString("http://localhost/ritajs/test/data/sentence1.json", function(s) {

            ok(s && s.length > 100);
            ok(JSON.parse(s));
            start();
        });
    });

    asyncTest("RiTa.loadString2(url)", function() {

        RiTa.loadString("http://localhost/ritajs/test/data/sentence2.json", function(s) {
            ok(s && s.length > 100);
            ok(JSON.parse(s));
            start();
        });
    });

    asyncTest("RiTa.loadString3(url)", function() {

        RiTa.loadString("http://localhost/ritajs/test/data/kafka.txt", function(s) {
            ok(s && s.length > 65536);
            start();
        });
    });

    asyncTest("testConcordanceLoad", function() {

      RiTa.loadString(filePath + "kafka.txt", function(s) {
        //console.log('kafka: '+s.length);
        data = RiTa.concordance(s, {
          ignoreStopWords: true,
          ignorePunctuation: true
        });
        ok(data["Gregor"] == 199);
        ok(data["Gregor"] > data["sister"]);
        start();
      });
    });

    // RiGrammar
    //////////////////////////////////////////////////////////////////////////////////////

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

    asyncTest("RiMarkov.loadFromUrl", function() {

        if (RiTa.env() == RiTa.NODE) {
            ok("Not for Node");
            start();
            return;
        }

        var rm = new RiMarkov(3);
        rm.loadFrom("http://localhost/ritajs/test/data/kafka.txt");

        var ts = +new Date();
        var id = setInterval(function() {

            if (rm.ready()) {

                ok(rm.size());
                start();
                clearInterval(id);
            } else {

                console.log("waiting...");
                var now = +new Date();
                if (now - ts > 5000) {
                    equal("no result", 0);
                    start();
                    clearInterval(id);
                }
            }

        }, 50);
    });


    asyncTest("RiMarkov.loadFromFile", function() {

        var rm = new RiMarkov(3);
        rm.loadFrom(filePath + "kafka.txt");

        var ts = +new Date();
        var id = setInterval(function() {

            if (rm.ready()) {

                ok(rm.size());
                start();
                clearInterval(id);
            } else {

                console.log("waiting...");
                var now = +new Date();
                if (now - ts > 5000) {
                    equal("no result", 0);
                    start();
                    clearInterval(id);
                }
            }

        }, 50);
    });

}// end runtests
if ( typeof exports != 'undefined')
    runtests();
