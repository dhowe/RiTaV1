var runtests = function() {

	// TODO: make sure we test: allowDuplicates and sentenceAware flags to the constructor

	QUnit.module("RiMarkov", {
		setup : function() { },
		teardown : function() { }
	});

	var sample = "One reason people lie is to achieve personal power. Achieving personal power is helpful for one who pretends to be more confident than he really is. For example, one of my friends threw a party at his house last month. He asked me to come to his party and bring a date. However, I did not have a girlfriend. One of my other friends, who had a date to go to the party with, asked me about my date. I did not want to be embarrassed, so I claimed that I had a lot of work to do. I said I could easily find a date even better than his if I wanted to. I also told him that his date was ugly. I achieved power to help me feel confident; however, I embarrassed my friend and his date. Although this lie helped me at the time, since then it has made me look down on myself.", SP = ' ', E = ' ';
	var sample2 = "One reason people lie is to achieve personal power. " + "Achieving personal power is helpful for one who pretends to " + "be more confident than he really is. For example, one of my " + "friends threw a party at his house last month. He asked me to " + "come to his party and bring a date. However, I did not have a " + "girlfriend. One of my other friends, who had a date to go to the " + "party with, asked me about my date. I did not want to be embarrassed, " + "so I claimed that I had a lot of work to do. I said I could easily find" + " a date even better than his if I wanted to. I also told him that his " + "date was ugly. I achieved power to help me feel confident; however, I " + "embarrassed my friend and his date. Although this lie helped me at the " + "time, since then it has made me look down on myself. After all, I did " + "occasionally want to be embarrassed.";

	test("RiMarkov()", function() {

		ok(RiMarkov(4));
		ok(new RiMarkov(3));
		var BAD = [null, undefined, "3"];
		for (var i = 0; i < BAD.length; i++) {
			throws(function() {
				//RiTa.SILENT = 1;
				try {
					new RiMarkov(BAD[i]);
				} catch (e) {
					throw e;
				}
				RiTa.SILENT = 0;
			});
			throws(function() {
				//RiTa.SILENT = 1;
				try {
					RiMarkov(BAD[i]);
				} catch (e) {
					throw e;
				}
				RiTa.SILENT = 0;
			});
		}
	});

	test("TextNode.lookup", function() {
		var root = RiMarkov(3).root;
		var i = root.addChild("I");
		var j = root.addChild("J");
		equal(root.lookup("J"), root.lookup(j));
		equal(root.lookup("I"), root.lookup(i));
	});

	test("TextNode.childCount", function() {
		var root = RiMarkov(3).root;
		var i = root.addChild("I");
		var i2 = root.addChild("I");
		var j = root.addChild("J");
		equal(root.childCount(), 3);
	});

	test("TextNode.siblingCount", function() {

		var root = RiMarkov(3).root;
		var i = root.addChild("I");
		var i2 = root.addChild("I");
		var j = root.addChild("J");
		equal(i.siblingCount(), 3);
		equal(i2.siblingCount(), 3);
		equal(j.siblingCount(), 3);

		throws(function() {
			RiTa.SILENT = 1;
			try {
				root.siblingCount();
			} catch (e) {
				throw e;
			}
			RiTa.SILENT = 0;
		});

	});

	test("TextNode.probability", function() {

		var root = RiMarkov(3).root;
		var i = root.addChild("I");
		var i2 = root.addChild("I");
		var j = root.addChild("J");
		equal(i.probability(), 2 / 3);
		equal(i2.probability(), 2 / 3);
		equal(j.probability(), 1 / 3);
		throws(function() {
			RiTa.SILENT = 1;
			try {
				root.probability();
			} catch (e) {
				throw e;
			}
			RiTa.SILENT = 0;
		});
	});

	test("testIsRoot", function() {
		var rm = new RiMarkov(3);
		ok(rm.root.isRoot());
		var node = rm.root.addChild('aChild');
		ok(!node.isRoot());
	});

	test("testIsLeaf", function() {
		var rm = new RiMarkov(3);
		ok(rm.root.isLeaf());
		var node = rm.root.addChild('aChild');
		ok(!rm.root.isLeaf());
		ok(node.isLeaf());
	});

	test("testLoadTokens", function() {

		var tokens = RiTa.tokenize(sample);
		var rm = new RiMarkov(3);
		rm.loadTokens(tokens);
		ok(rm.root.count == tokens.length);
		ok(rm.size() == tokens.length);
	});

	test("testFindNode", function() {

		var tokens = RiTa.tokenize('the dog ate the boy the');
		var rm = new RiMarkov(3);
		rm.loadTokens(tokens);

		var s = rm._findNode("the".split(SP));
		equal(s.probability(), .5)
		//console.log(s.toString());

		s = rm._findNode("dog".split(SP));
		equal(s.probability(), 1 / 6)
		//console.log(s.toString());

		s = rm._findNode("cat".split(SP));
		equal(s, null);
		//console.log(s);

		s = rm._findNode("the dog".split(SP));
		equal(s.probability(), 1 / 3)
		//console.log(s.toString());

		s = rm._findNode("dog ate".split(SP));
		equal(s.probability(), 1)
		//console.log(s.toString());

		s = rm._findNode("the cat".split(SP));
		equal(s, null)
		//console.log(s);

		s = rm._findNode("the dog ate".split(SP));
		equal(s.probability(), 1);
		//console.log(s.toString());

		s = rm._findNode("the boy".split(SP));
		equal(s.probability(), 1 / 3);
		//console.log(s.toString());

		s = rm._findNode("the boy the".split(SP));
		equal(s.probability(), 1);
		//console.log(s.toString());
	});

	//------------------------API TESTS--------------------------

	test("testGenerateTokens(a)", function() {

		var rm = new RiMarkov(4);
		rm.loadTokens(RiTa.tokenize(sample));
		for (var i = 0; i < 10; i++) {
			var arr = rm.generateTokens(5);
			var res = RiTa.untokenize(arr);
			equal(arr.length, 5);
		}
	});

	test("testGenerateTokens(b)", function() {

		var rm = new RiMarkov(4);
		rm.loadTokens(RiTa.tokenize(sample));
		for (var i = 0; i < 10; i++) {
			var arr = rm.generateTokens(4);
			for (var j = 0; j < arr.length; j++) {
				ok(arr[j] && arr[j].length);
			}
			var res = RiTa.untokenize(arr);
			ok(sample.indexOf(res) > -1, res);
		}
	});

	test("testGetSentenceStarts()", function() {
		var rm = new RiMarkov(4);
		rm.loadText(sample);
		for (var i = 0; i < 10; i++)
			ok(rm._getSentenceStart());
	});

	test("testLoadText(sentences)", function() {

		var rm = new RiMarkov(4, true, false);
		var sents = rm.loadText(sample).sentenceList;
		ok(sents && sents.length);

		var rm = new RiMarkov(4, false, false);
		var sents = rm.loadText(sample).sentenceList;
		ok(!sents.length);

		//ok(!"need more tests","need more tests"); // TODO

		var words = "The dog ate the cat";
		var rm = new RiMarkov(3, false);
		rm.loadText(words);
		//rm.print();
		equal(rm.getProbability("The"), 0.2);

		var words = "the dog ate the cat";

		var rm = new RiMarkov(3, false);
		rm.loadText(words);
		//rm.print();
		equal(rm.getProbability("the"), 0.4);

		var words = "The dog ate the cat.";

		var rm = new RiMarkov(3, true);
		rm.loadText(words);
		//rm.print();
		equal(rm.getProbability("The"), 1 / 6);

		var words = "The dog ate the cat. A boy ate the hat.";

		var rm = new RiMarkov(3, true);
		rm.loadText(words);
		equal(rm.getProbability("the"), 1 / 6);
	});

	test("testValidateSentence()", function() {

		var rm = new RiMarkov(4, true);
		var goods = ["The dog ate the cat.", "The dog ate the cat!", "The dog ate the cat?", 'However, I did not have a girlfriend.'];
		for (var i = 0; i < goods.length; i++) {
			ok(rm._validateSentence(goods[i]));
		}
		var bads = ["The dog ate the cat", "the dog ate the cat!"];
		for (var i = 0; i < bads.length; i++) {
			ok(!rm._validateSentence(bads[i]));
		}
	});

	test("testGenerateSentences()", function() {

		var dbug = 0;

		var rm = new RiMarkov(4, true, true);
		rm.loadText(sample);
		for (var i = 0; i < 3; i++) {
			var sents = rm.generateSentences(3);
			for (var j = 0; j < sents.length; j++) {
				if (dbug)console.log(i + "." + j + ") " + sents[j]);
				ok(sents);
			}
			ok(sents.length == 3);
		}

		var rm = new RiMarkov(4, true, true);
		rm.loadText(sample);
		for (var i = 0; i < 10; i++) {
			var sent = rm.generateSentences(1)[0];
			if (dbug)console.log(i + ") " + sent);
			ok(sent);
		}

		var rm = new RiMarkov(4, true, false);
		rm.loadText(sample);
		for (var i = 0; i < 10; i++) {
			var sent = rm.generateSentences(1)[0];
			if (dbug)console.log(i + ") " + sent);
			ok(sent);
		}

		throws(function() {

			var tmp, rm = new RiMarkov(4, false);

			tmp = RiTa.SILENT;
			RiTa.SILENT = 1;
			try {
				rm.generateSentences(10);
				ok(!"FAIL!!!");
			} catch (e) {
				throw e;
			}
			RiTa.SILENT = tmp;
		});

	});

	test("testGenerateUntil()", function() {

		var rm = new RiMarkov(3);
		rm.loadTokens(RiTa.tokenize(sample));

		for (var i = 0; i < 10; i++) {
			var arr = rm.generateUntil('[\.\?!]', 4, 20);
			var res = RiTa.untokenize(arr);

			ok(arr.length >= 4 && arr.length <= 20, res + '  (length=' + arr.length + ")");

			var n = rm.getN();
			for (var j = 0; j < arr.length - n; j++) {
				var partial = arr.slice(j, j + n);
				//console.log(partial);
				partial = RiTa.untokenize(partial);
				ok(sample.indexOf(partial) > -1, partial)
			}
		}

		throws(function() {
			try {
				rm.generateUntil('_NOT_IN_TEXT_', 4, 20);
			} catch (e) {
				throw e;
			}
		});

	});

	test("testGetN()", function() {//TODO

		for (var i = 1; i < 5; i++) {
			var rm = RiMarkov(i);
			equal(rm.getN(), i);
		}
	});

	// WORKING HERE

	test("testGetProbabilities[single]", function() {

		var rm = new RiMarkov(3);
		rm.loadTokens(RiTa.tokenize(sample));

		var checks = ["reason", "people", "personal", "the", "is"];
		var expec = [{
			people : 1.0
		}, {
			lie : 1.0
		}, {
			power : 1.0
		}, {
			time : 0.5,
			party : 0.5
		}, {
			to : 0.33333334,
			'.' : 0.33333334,
			helpful : 0.33333334
		}];

		var keys = Object.keys(expec);

		for (var i = 0; i < checks.length; i++) {

			var res = rm.getProbabilities(checks[i]);
			//console.log(checks[i]+":");

			equal(Object.keys(res).length, Object.keys(expec[i]).length);

			var answer = [];
			for (var key in res) {
				answer.push(key);
				//console.log("  "+key+" -> "+res[key]);
			}
			deepEqual(Object.keys(res), answer);
		}

		var res = rm.getProbabilities("XXX");
		deepEqual(res, {});
	});

	test("testGetProbabilities[array]", function() {

		var rm = new RiMarkov(4);
		rm.loadTokens(RiTa.tokenize(sample2));

		var res = rm.getProbabilities("the".split(" "));
		var expec = {
			time : 0.5,
			party : 0.5
		};
		deepEqual(res, expec);

		var res = rm.getProbabilities("people lie is".split(" "));
		var expec = {
			to : 1.0
		};
		deepEqual(res, expec);

		var res = rm.getProbabilities("is");
		var expec = {
			to : 0.3333333333333333,
			'.' : 0.3333333333333333,
			helpful : 0.3333333333333333
		};
		deepEqual(res, expec);

		var res = rm.getProbabilities("personal power".split(' '));
		var expec = {
			'.' : 0.5,
			is : 0.5
		};
		deepEqual(res, expec);

		var res = rm.getProbabilities("personal power".split(' '));

		var res = rm.getProbabilities(['to', 'be', 'more']);
		var expec = {
			confident : 1.0
		};
		deepEqual(res, expec);

		var res = rm.getProbabilities("XXX");
		var expec = {};
		deepEqual(res, expec);

		var res = rm.getProbabilities(["personal", "XXX"]);
		var expec = {};
		deepEqual(res, expec);

		var res = rm.getProbabilities(['I', 'did']);
		var expec = {
			"not" : 0.6666666666666666,
			"occasionally" : 0.3333333333333333
		};
		deepEqual(res, expec);

	});

	test("testGetProbability[single]", function() {

		var tokens = RiTa.tokenize('the dog ate the boy the');
		var rm = new RiMarkov(3);
		rm.loadTokens(tokens);
		//rm.print();

		equal(rm.getProbability("the"), .5);
		equal(rm.getProbability("dog"), 1 / 6);
		equal(rm.getProbability("cat"), 0);

		var tokens = RiTa.tokenize('the dog ate the boy that the dog found.');
		var rm = new RiMarkov(3);
		rm.loadTokens(tokens);
		//rm.print();

		equal(rm.getProbability("the"), .3);
		equal(rm.getProbability("dog"), .2);
		equal(rm.getProbability("cat"), 0);

		var rm = new RiMarkov(3);
		rm.loadTokens(RiTa.tokenize(sample));
		equal(rm.getProbability("power"), 0.017045454545454544);
	});

	test("testGetProbability[array]", function() {

		var rm = new RiMarkov(3);
		rm.loadTokens(RiTa.tokenize(sample));

		var check = 'personal power is'.split(SP);
		equal(rm.getProbability(check), 1 / 3);

		check = 'personal powXer is'.split(SP);
		equal(rm.getProbability(check), 0);

		check = 'someone who pretends'.split(SP);
		equal(rm.getProbability(check), 1 / 2);

		equal(rm.getProbability([]), 0);
	});

	test("testSize", function() {

		var tokens = RiTa.tokenize(sample);
		var sents = RiTa.splitSentences(sample);

		var rm = new RiMarkov(3);
		rm.loadTokens(tokens);
		ok(rm.root.count == tokens.length);
		equal(rm.size(), tokens.length);

		var rm = new RiMarkov(3, true);
		rm.loadText(sample);
		equal(rm.size(), tokens.length);

		var rm = new RiMarkov(3, false);
		rm.loadText(sample);
		equal(rm.size(), tokens.length);
	});

	test("testGetCompletions(a)", function() {//TODO:

		var rm = new RiMarkov(4);
		rm.loadTokens(RiTa.tokenize(sample));

		var res = rm.getCompletions("people lie is".split(' '));
		deepEqual(res, ["to"]);

		var res = rm.getCompletions("One reason people lie is".split(' '));
		deepEqual(res, ["to"]);

		var res = rm.getCompletions("personal power".split(' '));
		deepEqual(res, ['.', 'is']);

		var res = rm.getCompletions(['to', 'be', 'more']);
		deepEqual(res, ['confident']);

		var res = rm.getCompletions("I");
		// testing the sort
		var expec = ["did", "claimed", "had", "said", "could", "wanted", "also", "achieved", "embarrassed"];
		deepEqual(res, expec);

		var res = rm.getCompletions("XXX");
		deepEqual(res, []);
	});

	test("testGetCompletions(b)", function() {//TODO:

		var rm = new RiMarkov(4);
		rm.loadTokens(RiTa.tokenize(sample2));

		var res = rm.getCompletions(['I'], ['not']);
		deepEqual(res, ["did"]);

		var res = rm.getCompletions(['achieve'], ['power']);
		deepEqual(res, ["personal"]);

		var res = rm.getCompletions(['to', 'achieve'], ['power']);
		deepEqual(res, ["personal"]);

		var res = rm.getCompletions(['achieve'], ['power']);
		deepEqual(res, ["personal"]);

		var res = rm.getCompletions(['I', 'did']);
		deepEqual(res, ["not", "occasionally"]);
		// sort

		var res = rm.getCompletions(['I', 'did'], ['want']);
		deepEqual(res, ["not", "occasionally"]);
	});

	test("testLoadTokens", function() {//TODO: revise tests

		var words = 'The dog ate the cat'.split(' ');

		var rm = new RiMarkov(3);
		rm.loadTokens(words);
		equal(rm.getProbability("The"), 0.2);

		var rm = new RiMarkov(3);
		rm.loadTokens(words);
		equal(rm.getProbability("dog"), 0.2);

		var rm = new RiMarkov(3);
		rm.loadTokens(words);
		equal(rm.getProbability("Dhe"), 0);

		var rm = new RiMarkov(3);
		rm.loadTokens(words);
		equal(rm.getProbability("Dog"), 0);

		var rm = new RiMarkov(3);
		rm.loadTokens(words);
		equal(rm.getProbability(""), 0);

		var rm = new RiMarkov(3);
		rm.loadTokens(words);
		equal(rm.getProbability(" "), 0);

		var rm2 = new RiMarkov(3);
		rm2.loadTokens(RiTa.tokenize(sample));
		notEqual(rm2.getProbability("One"), rm.getProbability("one"));
	});

	test("testLoadText(tokens)", function() {//TODO: revise tests

		var words = 'The dog ate the cat';

		var rm = new RiMarkov(3, false);
		rm.loadText(words);
		//rm.print();
		equal(rm.getProbability("The"), 0.2);

		words = 'the dog ate the cat';
		var rm = new RiMarkov(3, false);
		rm.loadText(words);
		//rm.print();
		equal(rm.getProbability("the"), 0.4);
	});

	/*
	1] p(One) = 0.010810811
	2] p(the | giraffes) = 0.06666667
	3] map(the | before) = {fog=1.0}
	4] next(the | before) = [fog]
	5] next(of | one) = [my, the]
	6] map(of | one) = {the=0.5, my=0.5}
	7] getCompletions(walked ? the) = [toward, by]

	1] p(One) = 0.016216217
	2] p(the | giraffes) = 0.06666667
	3] map(the | before) = {fog=1.0}
	4] next(the | before) = [fog]
	5] next(of | one) = [the, my]
	6] map(of | one) = {the=0.6666667, my=0.33333334}
	7] getCompletions(walked ? the) = [toward, by]

	*/

	//           test("testIgnoreCase()", function () {
	//
	//                 var words = 'The dog ate the cat'.split(' ');
	//
	//                 var rm = new RiMarkov(3);
	//                 rm.loadTokens(words);
	//                 equal(rm.getProbability("The"), 0.4);
	//
	//                 var rm = new RiMarkov(3);
	//                 rm.loadTokens(words);
	//                 equal(rm.getProbability("The"), 0.2);
	//
	//                 var rm = new RiMarkov(3);
	//                 rm.loadTokens(words);
	//                 equal(rm.getProbability("the"), 0.4);
	//
	//                 var rm = new RiMarkov(3);
	//                 rm.loadTokens(words);
	//                 equal(rm.getProbability("the"), 0.2);
	//
	//                 var rm2 = new RiMarkov(3);
	//                 rm2.loadTokens(RiTa.tokenize(sample));
	//                 notEqual(rm2.getProbability("One"), rm.getProbability("one"));
	//             });    */

	test("testPrint()", function() { //TODO: how to test this?

		var words = 'The dog ate the cat'.split(' ');
		var rm = new RiMarkov(3);
		rm.loadTokens(words);
		ok(typeof rm.print == 'function');
		equal(rm.getProbability("The"), 0.2);
	});

	test("testUseSmoothing()", function() {

		var rm = new RiMarkov(3);
		rm.useSmoothing(false);
		rm.loadTokens(RiTa.tokenize(sample));

		var rm2 = new RiMarkov(3);
		rm2.useSmoothing(true);
		rm2.loadTokens(RiTa.tokenize(sample));
		notEqual(rm2.getProbability("one"), rm.getProbability("one"));

		var p1 = rm.getProbability("personal");
		var p2 = rm2.getProbability("personal");
		// console.log(p1 + " ?= "+p2);
		notEqual(p1, p2);

		// more tests?? yes
	});

	test("testSentenceAware()", function() {

		var rm = new RiMarkov(3, false);
		var x = rm.sentenceAware();
		equal(x, false);

		var rm = new RiMarkov(3, true);
		var x = rm.sentenceAware();
		equal(x, true);
	});
}

if (typeof exports != 'undefined') runtests(); //exports.unwrap = runtests;
