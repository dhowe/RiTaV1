/*global console, test, throws, equal, fail, notEqual, expect, require, ok,
    QUnit, RiTa, RiTaEvent, RiString, RiGrammar, RiMarkov, RiLexicon */

var runtests = function() {

    if (typeof YAML == 'undefined') YAML = require('yamljs');

    var SILENT = true,
        WITHOUT_YAML = typeof YAML == 'undefined';

    QUnit.module("RiGrammar", {
            setup: function() {},
            teardown: function() {},
            temp: function() {
                return Math.random() < 0.5 ? 'hot' : 'cold';
            }
        });

    var sentenceGrammarJSON = {
        "<start>": "<noun_phrase> <verb_phrase>.",
        "<noun_phrase>": "<determiner> <noun>",
        "<verb_phrase>": "<verb> | <verb> <noun_phrase> [.1]",
        "<determiner>": "a [.1] | the",
        "<noun>": "woman | man",
        "<verb>": "shoots"
    };

    var sentenceGrammarJSON2 = {
        "<start>": "<noun_phrase> <verb_phrase>.",
        "<noun_phrase>": "<determiner> <noun>",
        "<determiner>": ["a [.1]", "the"],
        "<verb_phrase>": ["<verb> <noun_phrase> [.1]", "<verb>"],
        "<noun>": ["woman", "man"],
        "<verb>": "shoots"
    };

    var sentenceGrammarYAML = "<start> : <noun_phrase> <verb_phrase>.\n<noun_phrase>: <determiner> <noun>\n<verb_phrase> : <verb> | <verb> <noun_phrase> [.1]\n<noun>: woman | man\n<determiner>: a [.1] | the\n<verb>: shoots";
    var sentenceGrammarYAML2 = "<start> : <noun_phrase> <verb_phrase>.\n<noun_phrase>: <determiner> <noun>\n<verb_phrase> : \n  - <verb> \n  - <verb> <noun_phrase> [.1]\n<noun>: \n  - woman\n  - man\n<determiner>: \n  - a [.1] \n  - the\n<verb>: shoots";
    var sentenceGrammarYAML3 = "--- <determiner>: a [.1] | the\n#I am a comment\n<noun>: woman | man\n<noun_phrase>: <determiner> <noun>\n<start>: <noun_phrase> <verb_phrase>.\n<verb>: shoots\n<verb_phrase>: <verb> | <verb> <noun_phrase> [.1]\n<multiline>: >\n  This is\n  my very long string\n  that wraps three lines\n";

    var sentenceGrammars = [sentenceGrammarJSON, sentenceGrammarJSON2, sentenceGrammarYAML, sentenceGrammarYAML2];
    //var sentenceGrammarFiles = [ "sentence1.json", "sentence2.json", "sentence1.yaml", "sentence2.yaml" ];
    //var haikuGrammarFiles = [ "haikuGrammar.json", "haikuGrammar2.json", "haikuGrammar.yaml", "haikuGrammar2.yaml" ];

    if (WITHOUT_YAML) {

        console.warn("[WARN] YAML parser not found -- skipping all YAML tests");

        sentenceGrammars = [sentenceGrammarJSON, sentenceGrammarJSON2];
        //sentenceGrammarFiles = [ "sentence1.json", "sentence2.json" ];
        //haikuGrammarFiles = [ "haikuGrammar.json", "haikuGrammar2.json" ];
    }

    test("testYaml", function() {

            if (!WITHOUT_YAML) {
                ok(YAML.parse(sentenceGrammarYAML));
                ok(YAML.parse(sentenceGrammarYAML2));
                ok(YAML.parse(sentenceGrammarYAML3));
            } else ok(1);
        });

    test("testInit", function() {

            var rg = RiGrammar();
            ok(rg._rules); // empty
            ok(typeof rg._rules['<start>'] === 'undefined');
            ok(typeof rg._rules['<noun_phrase>'] === 'undefined');

            var rg1 = RiGrammar(sentenceGrammarJSON);
            ok(rg1._rules);
            ok(rg1._rules['<start>']);
            ok(rg1._rules['<noun_phrase>']);

            var rg2 = RiGrammar(JSON.stringify(sentenceGrammarJSON));
            ok(rg2._rules);
            ok(rg2._rules['<start>']);
            ok(rg2._rules['<noun_phrase>']);

            var rg3 = RiGrammar(sentenceGrammarJSON2);
            ok(rg3._rules);
            ok(rg3._rules['<start>']);
            ok(rg3._rules['<noun_phrase>']);

            deepEqual(rg1, rg2);
            deepEqual(rg2, rg3);
            deepEqual(rg1, rg3);

            if (!WITHOUT_YAML) { // TEST the YAML grammars

                var rg4 = RiGrammar(sentenceGrammarYAML);
                ok(rg4._rules);
                ok(rg4._rules['<start>']);
                ok(rg4._rules['<noun_phrase>']);

                var rg5 = RiGrammar(sentenceGrammarYAML2);
                ok(rg5._rules);
                ok(rg5._rules['<start>']);
                ok(rg5._rules['<noun_phrase>']);

                deepEqual(rg1, rg4);
                deepEqual(rg1, rg5);
            }

            var BAD = ["{a : 1}", "hello"];
            for (var i = 0; i < BAD.length; i++) {
                throws(function() {

                        try {
                            RiGrammar(BAD[i]);
                            fail("no exception");
                        } catch (e) {
                            throw e;
                        }
                    });
            }
        });

    test("testLoad", function() {

            var rg = new RiGrammar();
            ok(rg._rules);
            ok(typeof rg._rules['<start>'] === 'undefined');
            ok(typeof rg._rules['<noun_phrase>'] === 'undefined');

            rg.load(JSON.stringify(sentenceGrammarJSON));
            ok(rg._rules);
            ok(typeof rg._rules['<start>'] !== 'undefined');
            ok(typeof rg._rules['<noun_phrase>'] !== 'undefined');

            rg.load(JSON.stringify(sentenceGrammarJSON2));
            ok(rg._rules);
            ok(typeof rg._rules['<start>'] !== 'undefined');
            ok(typeof rg._rules['<noun_phrase>'] !== 'undefined');
        });

    test("testAddRule", function() {

            var rg = new RiGrammar();
            rg.addRule("<start>", "<pet>");
            ok(rg._rules["<start>"]);
            ok(rg.hasRule("<start>"));
            rg.addRule("<start>", "<dog>", .3);
            ok(rg._rules["<start>"]);
            ok(rg.hasRule("<start>"));
        });

    test("testExpand()", function() {

            for (var j = 0; j < sentenceGrammars.length; j++) {
                var rg = new RiGrammar(sentenceGrammars[j]);
                for (var i = 0; i < 10; i++)
                    ok(rg.expand());
            }

            var rg = new RiGrammar();
            rg.addRule("<start>", "pet");
            equal(rg.expand(), "pet");

            rg.addRule("<start>", "pet", 1);
            equal(rg.expand(), "pet");
            rg.addRule("<start>", "pet", 2);
            equal(rg.expand(), "pet");

            rg.reset();
            rg.addRule("<start>", "<pet>", 1);
            rg.addRule("<pet>", "dog", 1);
            equal(rg.expand(), "dog");

            /////////////////////////////////////////////////////////////////

            rg.reset();
            rg.addRule("<start>", "<rule1>", 1);
            rg.addRule("<rule1>", "cat", .4);
            rg.addRule("<rule1>", "dog", .6);
            rg.addRule("<rule1>", "boy", .2);
            ok(rg.hasRule("<rule1>"));

            var found1 = false,
                found2 = false,
                found3 = false;
            for (var i = 0; i < 100; i++) {
                var res = rg.expand();

                ok(res === ("cat") || res === ("dog") || res === ("boy"));

                if (res === ("cat"))
                    found1 = true;
                else if (res === ("dog"))
                    found2 = true;
                else if (res === ("boy"))
                    found3 = true;
            }
            ok(found1 && found2 && found3); // found all

            /////////////////////////////////////////////////////////////////

            rg.reset();
            rg.addRule("<start>", "<rule1>", 1);
            rg.addRule("<rule1>", "cat | dog | boy");
            ok(rg.hasRule("<rule1>"));

            found1 = false;
            found2 = false;
            found3 = false;
            for (var i = 0; i < 100; i++) {
                var res = rg.expand();

                ok(res === ("cat") || res === ("dog") || res === ("boy"));

                if (res === ("cat"))
                    found1 = true;
                else if (res === ("dog"))
                    found2 = true;
                else if (res === ("boy"))
                    found3 = true;
            }
            ok(found1 && found2 && found3); // found all

            /////////////////////////////////////////////////////////////////

            rg.reset();
            rg.addRule("<start>", "pet", 1);
            equal(rg.expand(), "pet");

            rg.reset();
            rg.addRule("<start>", "the <pet> ran.", 1);
            rg.addRule("<pet>", "dog", .7);
            for (var i = 0; i < 10; i++)
                equal(rg.expand(), "the dog ran.");

            rg.reset();
            rg.addRule("<start>", "the <pet>.", 1);
            rg.addRule("<pet>", "dog", .7);
            rg.addRule("<pet>", "cat", .3);

            var d = 0,
                c = 0;
            for (var i = 0; i < 100; i++) {
                var r = rg.expand();
                if (r === "the dog.")
                    d++;
                if (r === "the cat.")
                    c++;
            }
            ok(d > 50); // d + ""
            ok(d < 90); // d + ""
            ok(c > 10); // g + ""
            ok(c < 50); // g + ""
        });

    /*test("testExpandOld()", function() {

        var s, rg = new RiGrammar();

        rg.addRule("<start>", "pet");
        equal(s=rg.expand(), "pet");

		//console.log(s);

        rg.reset();
        rg.addRule("<start>", "<pet>").addRule("<pet>", "dog");
        equal(s=rg.expand(), "dog");

        rg.reset();
	    rg.addRule("<start>", "the <pet> ran.");
	    rg.addRule("<pet>", "dog");
	    s = rg.expand();
	    equal(s, "the dog ran.");

        rg.reset();
        rg.addRule("<start>", "<rule1>");
        rg.addRule("<rule1>", "cat", .4);
        rg.addRule("<rule1>", "dog", .6);
        rg.addRule("<rule1>", "boy", .2);

		ok(rg.hasRule("<rule1>"));

        var found1 = false, found2 = false, found3 = false;
        for ( var i = 0; i < 20; i++) {
            var res = rg.expand();
            ok(res === "cat" || res === 'dog' || res === 'boy');
            if (res === "cat")
                found1 = true;
            else if (res === "dog")
                found2 = true;
            else if (res === "boy")
            	found3 = true;
        }

        ok(found1);
        ok(found2);
        ok(found3);

        var fail = false;
        for ( var i = 0; i < 20; i++) {
            var res = rg.expand()
            if (!res) fail = true;
        }
        ok(!fail);

        rg.reset();
        rg.addRule("<start>", "pet");
        equal(rg.expand(), "pet");

	    rg.reset();
	    rg.addRule("<start>", "the <pet> ran.", 1);
	    rg.addRule("<pet>", "dog", .7);
	    for (var i = 0; i < 10; i++)
	      equal(rg.expand(), "the dog ran.");

	        rg.reset();
        rg.addRule("<start>", "the <pet>.");
        rg.addRule("<pet>", "dog", .7);
        rg.addRule("<pet>", "cat", .3);

        var d = 0, g = 0;
        for ( var i = 0; i < 100; i++) {
            var r = rg.expand();
            if (r == 'the dog.') d++;
            if (r == 'the cat.') g++;
        }

        // delta=20%
        ok(d > 50 && d < 100, d + "%  (dog =~ 70%)");
        ok(d < 90 && d > 0,   d + "% (dog =~ 70%)");
        ok(g > 10 && g < 100, g + "% (cat =~ 30%)");
        ok(g < 50 && g > 0,   g + "% (cat =~ 30%)");
    });*/

    test("testExpandFrom", function() {

            var rg = new RiGrammar();

            rg.reset();
            rg.addRule("<start>", "<pet>");
            rg.addRule("<pet>", "<bird> | <mammal>");
            rg.addRule("<bird>", "hawk | crow");
            rg.addRule("<mammal>", "dog");

            equal(rg.expandFrom("<mammal>"), "dog");

            for (var i = 0; i < 100; i++) {
                var res = rg.expandFrom("<bird>");
                ok(res === "hawk" || res === 'crow');
            }

            throws(function() {
                    try {
                        rg.expandFrom("wrongName")
                    } catch (e) {
                        throw e;
                    }
                });

        });

    test("testExpandFrom(Weighted)", function() {

            var rg = new RiGrammar();

            rg.reset();
            rg.addRule("<start>", "<pet>");
            rg.addRule("<pet>", "<bird> [9] | <mammal>");
            rg.addRule("<bird>", "hawk");
            rg.addRule("<mammal>", "dog [2]");

            equal(rg.expandFrom("<mammal>"), "dog");

            var hawks = 0,
                dogs = 0;
            for (var i = 0; i < 100; i++) {
                var res = rg.expandFrom("<pet>");
                ok(res === "hawk" || res === 'dog');
                if (res == "dog") dogs++;
                if (res == "hawk") hawks++;
            }
            ok(hawks > dogs * 2);
        });

    test("testGetGrammar", function() {

            var rg = new RiGrammar(sentenceGrammarJSON);
            var rg2 = new RiGrammar(sentenceGrammarJSON2);
            deepEqual(rg, rg2)

            var e = "<start>\n  '<noun_phrase> <verb_phrase>.' [1]\n<noun_phrase>\n  '<determiner> <noun>' [1]\n<verb_phrase>\n  '<verb>' [1]\n  '<verb> <noun_phrase>' [.1]\n<determiner>\n  'a' [.1]\n  'the' [1]\n<noun>\n  'woman' [1]\n  'man' [1]\n<verb>\n  'shoots' [1]";
            equal(rg.getGrammar(), e);
        });

    test("testHasRule", function() {

            var g = [new RiGrammar(sentenceGrammarJSON), new RiGrammar(sentenceGrammarJSON2)];

            for (var i = 0; i < g.length; i++) {

                var rg = g[i];
                ok(rg.hasRule("<start>"));
                ok(!rg.hasRule("start"));

                rg.reset();
                ok(!rg.hasRule("start"));
                rg.addRule("<rule1>", "<pet>");
                ok(rg.hasRule("<rule1>"));
                ok(!rg.hasRule("rule1"));

                rg.reset();

                rg.addRule("<rule1>", "cat", .4);
                rg.addRule("<rule1>", "dog", .6);
                rg.addRule("<rule1>", "boy", .2);
                ok(rg.hasRule("<rule1>"));
                ok(!rg.hasRule("rule1"));

                ok(!rg.hasRule("badname"));

                rg.reset();

                rg.addRule("rule1", "<pet>");
                ok(!rg.hasRule("<rule1>"));
                ok(rg.hasRule("rule1"));

                ok(!rg.hasRule(null));
                ok(!rg.hasRule(undefined));
                ok(!rg.hasRule(1));
            }
        });

    test("testReset", function() {

            var rg = new RiGrammar();
            ok(rg._rules);
            rg.load(JSON.stringify(sentenceGrammarJSON));
            rg.reset();
            deepEqual(rg._rules, {});
            deepEqual(rg, RiGrammar());
        });

    test("testRemoveRule", function() {

            var rg1 = new RiGrammar(sentenceGrammarJSON);

            ok(rg1._rules['<start>']);
            ok(rg1._rules['<noun_phrase>']);

            rg1.removeRule('<noun_phrase>');
            ok(!rg1._rules['<noun_phrase>']);

            rg1.removeRule('<start>');
            ok(!rg1._rules['<start>']);

            rg1.removeRule('');
            rg1.removeRule('bad-name');
            rg1.removeRule(null);
            rg1.removeRule(undefined);

            rg1 = new RiGrammar(sentenceGrammarJSON2);

            ok(rg1._rules['<start>']);
            ok(rg1._rules['<noun_phrase>']);

            rg1.removeRule('<noun_phrase>');
            ok(!rg1._rules['<noun_phrase>']);

            rg1.removeRule('<start>');
            ok(!rg1._rules['<start>']);

            rg1.removeRule('');
            rg1.removeRule('bad-name');
            rg1.removeRule(null);
            rg1.removeRule(undefined);
        });

    test("testPrint", function() {

            var rg = new RiGrammar();
            rg.reset();
            rg.addRule("<start>", "<first> | <second>", 1);
            rg.addRule("<first>", "the <pet> <action> of ...", 1);
            rg.addRule("<second>", "the <action> of the <pet> were ...", 1);
            rg.addRule("<pet>", "<bird> | <mammal>", 1);
            rg.addRule("<bird>", "hawks | crows", 1);
            rg.addRule("<mammal>", "dogs", 1);
            rg.addRule("<action>", "cries | screams | falls", 1);
            //rg.print();
            ok(typeof rg.print === 'function');
        });

    test("testExpandWith", function() {

            var rg = new RiGrammar();
            rg.addRule("<start>", "the <pet> | the <action> of the <pet>");
            rg.addRule("<pet>", "<bird> | <mammal>");
            rg.addRule("<bird>", "hawk | crow | screamer");
            rg.addRule("<mammal>", "dog");
            rg.addRule("<action>", "cries | screams | falls");

            var r = rg.expandWith("screams", "<action>");

            var str = "",
                missed = false;
            for (var i = 0; i < 100; i++) {
                var r = rg.expandWith("screams", "<action>");
                if (r.indexOf("screams") < 1) {
                    str = r;
                    // console.log("error: " + r);
                    missed = true;
                }
            }
            equal(missed, false);

            str = "", missed = false;
            for (var i = 0; i < 100; i++) {
                var r = rg.expandWith("dog", "<pet>");
                if (r.indexOf("dog") < 1) {
                    str = r;
                    // console.log("error: " + r);
                    missed = true;
                }
            }
            equal(missed, false);


            //equal("TODO: MORE TESTS HERE");
        });


    test("testSpecialChars", function() {

            var rg, res, s

                s = "{ \"<start>\": \"hello &#124; name\" }";
            rg = new RiGrammar(s);
            res = rg.expand();
            //console.log(res);
            ok(res === "hello | name");

            s = "{ \"<start>\": \"hello: name\" }";
            rg = new RiGrammar(s);
            res = rg.expand();
            ok(res === "hello: name");

            s = "{ \"<start>\": \"&#8220;hello!&#8221;\" }";
            rg = new RiGrammar(s);
            //rule = rg.getRule("<start>");
            //ok(rule==="&#8220;hello!&#8221;");
            //ok("fails b/c of editor?");
            //res = rg.expand();
            //console.log(res+'=“hello!”');
            // ok(res==='“hello!”'); // fails bc of editor

            s = "{ \"<start>\": \"&lt;start&gt;\" }";
            rg = new RiGrammar(s);
            res = rg.expand();
            //console.log(res);
            ok(res === "<start>");

            s = "{ \"<start>\": \"I don&#96;t want it.\" }";
            rg = new RiGrammar(s);
            res = rg.expand();
            //console.log(res);
            ok(res === "I don`t want it.");

            s = "{ \"<start>\": \"&#39;I really don&#39;t&#39;\" }";
            rg = new RiGrammar(s);
            res = rg.expand();
            ok(res === "'I really don't'");

            s = "{ \"<start>\": \"hello | name\" }";
            rg = new RiGrammar(s);
            for (var i = 0; i < 10; i++) {
                res = rg.expand();
                ok(res === "hello" || res === "name");
            }

        });

    test("testExecIgnore", function() {

            var rg = new RiGrammar(); // do nothing
            rg.execDisabled = false;

            rg.addRule("<start>", "<first> | <second>");
            rg.addRule("<first>", "the <pet> <action> were 'adj()'");
            rg.addRule("<second>", "the <action> of the 'adj()' <pet>");
            rg.addRule("<pet>", "<bird> | <mammal>");
            rg.addRule("<bird>", "hawk | crow");
            rg.addRule("<mammal>", "dog");
            rg.addRule("<action>", "cries | screams | falls");

            for (var i = 0; i < 10; i++) {
                var res = rg.expand();
                //console.log(i+") "+res);
                ok(res != null && res.length > 0);
                ok(res.indexOf("'adj()'") > -1);
            }

            rg.reset();

            rg.addRule("<start>", "<first> | <second>");
            rg.addRule("<first>", "the <pet> <action> were `adj()'");
            rg.addRule("<second>", "the <action> of the `adj()' <pet>");
            rg.addRule("<pet>", "<bird> | <mammal>");
            rg.addRule("<bird>", "hawk | crow");
            rg.addRule("<mammal>", "dog");
            rg.addRule("<action>", "cries | screams | falls");

            for (var i = 0; i < 10; i++) {
                var res = rg.expand();
                //console.log(i+") "+res);
                ok(res != null && res.length > 0);
                ok(res.indexOf("`adj()'") > -1);
            }


            rg.reset();

            rg.addRule("<start>", "<first> | <second>");
            rg.addRule("<first>", "the <pet> <action> were `nofun()`");
            rg.addRule("<second>", "the <action> of the `nofun()` <pet>");
            rg.addRule("<pet>", "<bird> | <mammal>");
            rg.addRule("<bird>", "hawk | crow");
            rg.addRule("<mammal>", "dog");
            rg.addRule("<action>", "cries | screams | falls");

            var tmp = RiTa.SILENT;
            RiTa.SILENT = true;
            for (var i = 0; i < 5; i++) {
                var res = rg.expand();
                //console.log(i+") "+res);
                ok(res != null && res.length > 0 && res.indexOf("`nofun()`") > -1);
            }

            for (var i = 0; i < 5; i++) {
                var res = rg.expand(this);
                //console.log(i+") "+res);
                ok(res != null && res.length > 0 && res.indexOf("`nofun()`") > -1);
            }

            RiTa.SILENT = tmp;
        });

    test("testExecRE", function() {

            var str, res, re = RiGrammar.EXEC_PATT;

            str = "`hello()`";
            res = re.exec(str);

            for (var i = 0; i < res.length; i++)
                if (!SILENT) console.log("'" + res[i] + "'");
            res.splice(0, 1);
            deepEqual(res, ["", "`hello()`", ""]);

            if (!SILENT) console.log("===========================");

            str = "`hello(and)`";
            res = re.exec(str);

            for (var i = 0; i < res.length; i++)
                if (!SILENT) console.log("'" + res[i] + "'");
            res.splice(0, 1);
            deepEqual(res, ["", "`hello(and)`", ""]);

            if (!SILENT) console.log("===========================");

            str = "`hello('and')`";
            res = re.exec(str);

            for (var i = 0; i < res.length; i++)
                if (!SILENT) console.log("'" + res[i] + "'");
            res.splice(0, 1);
            deepEqual(res, ["", "`hello('and')`", ""]);

            if (!SILENT) console.log("===========================");

            str = '`hello("and")`';
            res = re.exec(str);

            for (var i = 0; i < res.length; i++)
                if (!SILENT) console.log("'" + res[i] + "'");
            res.splice(0, 1);
            deepEqual(res, ["", '`hello("and")`', ""]);

            if (!SILENT) console.log("===========================");

            str = "and `hello()` there";
            res = re.exec(str);

            for (var i = 0; i < res.length; i++)
                if (!SILENT) console.log("'" + res[i] + "'");
            res.splice(0, 1);
            deepEqual(res, ["and ", "`hello()`", " there"]);

            if (!SILENT) console.log("===========================");

            str = "and `hello()` there `you()`";
            res = re.exec(str);
            for (var i = 0; i < res.length; i++)
                if (!SILENT) console.log("'" + res[i] + "'");
            res.splice(0, 1);
            deepEqual(res, ["and ", "`hello()`", " there `you()`"]);

            if (!SILENT) console.log("===========================");

            str = "and `hello()`";
            res = re.exec(str);
            for (var i = 0; i < res.length; i++)
                if (!SILENT) console.log("'" + res[i] + "'");
            res.splice(0, 1);
            deepEqual(res, ["and ", "`hello()`", ""]);

            if (!SILENT) console.log("===========================");

            str = "`hello()` there `you()`";
            res = re.exec(str);
            for (var i = 0; i < res.length; i++)
                if (!SILENT) console.log("'" + res[i] + "'");
            res.splice(0, 1);
            deepEqual(res, ["", "`hello()`", " there `you()`"]);

            if (!SILENT) console.log("===========================");

            str = "`hello();`";
            res = re.exec(str);

            for (var i = 0; i < res.length; i++)
                if (!SILENT) console.log("'" + res[i] + "'");
            res.splice(0, 1);
            deepEqual(res, ["", "`hello();`", ""]);

            if (!SILENT) console.log("===========================");

            str = "`hello(and);`";
            res = re.exec(str);

            for (var i = 0; i < res.length; i++)
                if (!SILENT) console.log("'" + res[i] + "'");
            res.splice(0, 1);
            deepEqual(res, ["", "`hello(and);`", ""]);

            if (!SILENT) console.log("===========================");

            str = "`hello('and');`";
            res = re.exec(str);

            for (var i = 0; i < res.length; i++)
                if (!SILENT) console.log("'" + res[i] + "'");
            res.splice(0, 1);
            deepEqual(res, ["", "`hello('and');`", ""]);

            if (!SILENT) console.log("===========================");

            str = '`hello("and");`';
            res = re.exec(str);

            for (var i = 0; i < res.length; i++)
                if (!SILENT) console.log("'" + res[i] + "'");
            res.splice(0, 1);
            deepEqual(res, ["", '`hello("and");`', ""]);

            if (!SILENT) console.log("===========================");

            str = "and `hello();` there";
            res = re.exec(str);

            for (var i = 0; i < res.length; i++)
                if (!SILENT) console.log("'" + res[i] + "'");
            res.splice(0, 1);
            deepEqual(res, ["and ", "`hello();`", " there"]);

            if (!SILENT) console.log("===========================");

            str = "and `hello();` there `you();`";
            res = re.exec(str);
            for (var i = 0; i < res.length; i++)
                if (!SILENT) console.log("'" + res[i] + "'");
            res.splice(0, 1);
            deepEqual(res, ["and ", "`hello();`", " there `you();`"]);

            if (!SILENT) console.log("===========================");

            str = "and `hello();`";
            res = re.exec(str);
            for (var i = 0; i < res.length; i++)
                if (!SILENT) console.log("'" + res[i] + "'");
            res.splice(0, 1);
            deepEqual(res, ["and ", "`hello();`", ""]);

            if (!SILENT) console.log("===========================");

            str = "`hello();` there `you();`";
            res = re.exec(str);
            for (var i = 0; i < res.length; i++)
                if (!SILENT) console.log("'" + res[i] + "'");
            res.splice(0, 1);
            deepEqual(res, ["", "`hello();`", " there `you();`"]);

        });

    test("testExec1", function() {

            var rg = new RiGrammar();
            rg.execDisabled = false;
            ok(rg);

            if (typeof module == 'undefined') { // for node-issue #9

                rg.addRule("<start>", "<first> | <second>");
                rg.addRule("<first>", "the <pet> <action> were `temp()`");
                rg.addRule("<second>", "the <action> of the `temp()` <pet>");
                rg.addRule("<pet>", "<bird> | <mammal>");
                rg.addRule("<bird>", "hawk | crow");
                rg.addRule("<mammal>", "dog");
                rg.addRule("<action>", "cries | screams | falls");

                for (var i = 0; i < 10; i++) {

                    // TODO: fails in NODE  ??
                    // The "this" value passed to eval must be the global object from which eval originated ?

                    var res = rg.expand();
                    //console.log(res);
                    ok(res && !res.match("`") && res.match(/(hot|cold)/));
                }
            }
        });

    var newruleg = {
        '<start>': 'The <noun> chased the `newrule("<noun>")`.',
        '<noun>': 'dog | cat | mouse',
        '<verb>': 'rhino'
    };

    // TODO: fails in NODE/phantomJS ?
    test("testExec2", function() {

            var rg = new RiGrammar(newruleg);
            rg.execDisabled = false;
            ok(rg);

            if (typeof module == 'undefined') { // for node-issue #9

                for (var i = 0; i < 10; i++) {
                    var res = rg.expand();
                    ok(res && res.match(/ chased the rhino\./g));
                }
            }
        });

    test("testExecArgs", function() {

            var res, i, rg = new RiGrammar(newruleg);
            rg.execDisabled = false;
            ok(rg);

            if (typeof module == 'undefined') { // for node-issue #9

                rg.addRule("<start>", "`getFloat()`");
                for (i = 0; i < 10; i++) {

                    res = rg.expandFrom("<start>", this);
                    ok(res && res.length && parseFloat(res));
                }

                rg.reset();
                rg.addRule("<start>", "`adj(2)`");
                for (i = 0; i < 10; i++) {

                    res = rg.expandFrom("<start>", this);
                    ok(res && res.length && res === "number");
                }

                rg.reset();
                rg.addRule("<start>", "`adj(true)`");
                for (i = 0; i < 10; i++) {

                    res = rg.expandFrom("<start>", this);
                    //System.out.println(i + ")" + res);
                    ok(res === "boolean");
                }
            }

        });
};

if (typeof exports != 'undefined') runtests();
