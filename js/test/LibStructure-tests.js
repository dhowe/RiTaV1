/*global console, test, throws, equal, notEqual, expect, require, ok,
    QUnit, RiTa, RiTaEvent, RiString, RiGrammar, RiMarkov, RiLexicon */

var runtests = function() {

  QUnit.module("LibraryStructure", {
    setup: function() {},
    teardown: function() {}
  });

  /*test("Public Api Functions", function() {

    if (QUnit.checkAPI) {

      var pathToDocs = '../../RiTa/docs/json/';
      QUnit.checkAPI('RiTaEvent', RiTaEvent, new RiTaEvent(this), pathToDocs);
      QUnit.checkAPI('RiTa', RiTa, RiTa, pathToDocs);
      QUnit.checkAPI('RiString', RiString, new RiString(''), pathToDocs);
      QUnit.checkAPI('RiGrammar', RiGrammar, new RiGrammar(), pathToDocs);
      QUnit.checkAPI('RiMarkov', RiMarkov, new RiMarkov(2), pathToDocs);
      QUnit.checkAPI('RiLexicon', RiLexicon, RiLexicon(), pathToDocs);

    } else {

      console.log("[TEST] Skipping checkAPI() for LibraryStructure (node-only)");
      ok("Only check in Node");
    }
  });*/

  test("Public Objects", function() {

    ok(typeof RiTa != 'undefined');
    ok(typeof RiString != 'undefined');
    ok(typeof RiLexicon != 'undefined');
    ok(typeof RiGrammar != 'undefined');
    ok(typeof RiMarkov != 'undefined');
    ok(typeof RiTaEvent != 'undefined');
    ok(typeof RiWordNet != 'undefined');
    if (typeof window != 'undefined' && window) {
      ok(typeof RiText != 'undefined');
    }
  });

  test("Private Statics (funs)", function() {

    if (typeof window != 'undefined' && window) {

      //equal(typeof RiText._handleLeading, 'function');
      //equal(typeof RiText._disposeOne, 'function');

      // these fail in phantom outside the conditional ??
      equal(typeof is, 'undefined');
    }
    equal(typeof _wordOffsetFor, 'undefined');
    equal(typeof _handleLeading, 'undefined');
    equal(typeof _disposeOne, 'undefined');

  });

  test("Private Statics (vars)", function() {

    equal(typeof _VERSION_, 'undefined');
    equal(typeof RiTa._VERSION_, 'undefined');
    equal(typeof timers, 'undefined');
    equal(typeof RiTa.timers, 'undefined');
  });

  test("Member Functions", function() {

    var rx = new RiString("hello");
    equal(typeof rx.charAt, 'function');
    var rs = RiString("hello");
    equal(typeof rs.charAt, 'function');
    var rg = RiMarkov(3);
    equal(typeof rg.loadTokens, 'function');
    rg = new RiMarkov(2);
    equal(typeof rg.loadTokens, 'function');
  });

  test("Member Variables", function() {

    ok(RiString("x").text);
    ok(new RiString("x").text);
    ok(RiGrammar()._rules);
    ok(new RiGrammar()._rules);
  });

  test("Public Statics", function() {

    equal(typeof RiTa.splitSentences, 'function');
    equal(typeof RiTa.getPhonemes, 'function');
    equal(typeof RiTa.random, 'function');
  });

  test("Public Constants", function() {

    ok(RiTa.VERSION);
    ok(RiTa.PRESENT_TENSE);
    ok(RiTa.SECOND_PERSON);
    ok(RiGrammar.START_RULE);
  });

  test("Internal Classes", function() {

    equal(typeof Type, 'undefined', 'Type is visible!');
    equal(typeof Timer, 'undefined', 'Timer is visible!');
    equal(typeof Phones, 'undefined', 'Phones is visible!');
    equal(typeof Conjugator, 'undefined', 'Conjugator');
    equal(typeof MinEditDist, 'undefined', 'MinEditDist');
    equal(typeof PosTagger, 'undefined', 'PosTagger');
    equal(typeof RegexRule, 'undefined', 'RegexRule');
    equal(typeof Easing, 'undefined', 'Easing');
    equal(typeof Interpolation, 'undefined', 'Interpolation');
    equal(typeof TextBehavior, 'undefined', 'TextBehavior');
    equal(typeof RiText_Canvas, 'undefined', 'RiText_Canvas');
    equal(typeof RiText_P5, 'undefined', 'RiText_P5');
  });

  test("Private Constants", function() {

    equal(typeof SP, 'undefined');
    equal(typeof A, 'undefined');
    equal(typeof SECOND_PERSON, 'undefined');
    equal(typeof PLURAL_RULES, 'undefined');
    equal(typeof PAST_PARTICIPLE_RULESET, 'undefined');
  });

  test("Constructors", function() {

    equal(typeof RiString, 'function'); // pub
    equal(typeof RiGrammar, 'function');
    equal(typeof RiLexicon, 'function');

    notEqual(typeof RiTa, 'function'); // prv

    var rx = new RiString("hello");
    equal(typeof rx, 'object');

    var rs = RiString("hello");
    equal(typeof rx, 'object');

    var lr = RiGrammar();
    equal(typeof lr, 'object');

    var lg = new RiGrammar();
    equal(typeof lg, 'object');

    if (typeof window != 'undefined') {

      equal(typeof RiText, 'function');

    }

    throws(function() {
      RiTa.SILENT = 1;
      try {
        new RiTa();
      } catch (e) {
        throw Error("no constructor");
      }
      RiTa.SILENT = 0;
    });

    throws(function() {
      RiTa.SILENT = 1;
      try {
        RiTa();
      } catch (e) {
        throw Error("no constructor");
      }
      RiTa.SILENT = 0;
    });
  });

  test("Private Functions", function() {

    equal(typeof startsWith, 'undefined');
    equal(typeof isNull, 'undefined');
    equal(typeof makeClass, 'undefined');
    equal(typeof handleLeading, 'undefined');
    equal(typeof disposeOne, 'undefined');
  });

};

if (typeof exports != 'undefined') runtests();
