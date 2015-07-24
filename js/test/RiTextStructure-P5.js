/*global console, test, throws, equal, fail, notEqual, expect, require, ok,
    QUnit, RiTa, RiTaEvent, RiString, RiGrammar, RiMarkov, RiLexicon */

var runtests = function() {

  test("Constructors", function() {

    equal(typeof RiText, 'function');

    var rx = new RiText("hello");
    equal(typeof rx, 'object');

    var rx2 = RiText("hello");
    equal(typeof rx2, 'object');

    var rx3 = new RiText(new RiString("hello"));
    equal(typeof rx3, 'object');

    var rx3 = RiText(RiString("hello"));
    equal(typeof rx3, 'object');
  });

  test("Member Functions", function() {

    var rx = new RiText("hello");
    equal(typeof rx.toString, 'function');

    var rx = new RiText("hello");
    equal(typeof rx.draw, 'function');

    var rx = new RiText("hello");
    ok(rx.textWidth() > 0);

    var rs = RiString("hello");
    equal(typeof rs.init, 'function');
  });

  test("Public Constants", function() {

    ok(RiTa.PARAGRAPH_BREAK);
    ok(RiTa.LINE_BREAK);
    ok(RiTa.LEFT);
    ok(RiTa.CENTER);
    ok(RiTa.UNKNOWN);
    ok(RiTa.TIMER);
  });

  test("Public Statics", function() {

    //equal(typeof RiText.foreach, 'function');
    equal(typeof RiText.drawAll, 'function');
    equal(typeof RiText.defaultFill, 'function');
    ok(RiText.defaults);
    ok(RiText.defaults.fill);
    ok(RiText.defaults.paragraphLeading || RiText.defaults.paragraphLeading == 0);
  });

  test("Member Variables", function() {

    ok(RiText("yahdh").x);
    ok(new RiText("x").y);
    ok(RiText()._color);
    ok(new RiText()._scaleX);
    ok(typeof RiText()._rotateZ != 'undefined');
    ok(new RiText().y);
  });


  test("RiText.dispose[single]", function() {

    RiText.instances = [];
    equal(RiText.instances.length, 0);

    var r1 = new RiText("hello1");
    var r2 = new RiText("hello2");

    equal(RiText.instances.length, 2);
    RiText.dispose(r1);
    equal(RiText.instances.length, 1);
    RiText.dispose(r2);
    equal(RiText.instances.length, 0);
  });

  test("RiText.dispose[array]", function() {

    RiText.instances = [];
    equal(RiText.instances.length, 0);
    new RiText("extra");
    equal(RiText.instances.length, 1);

    var arr = [];
    for (var i = 0; i < 10; i++) {
      arr.push(new RiText("hello" + i));
    }

    ok(arr.length == 10);
    ok(RiText.instances.length == 11);

    RiText.dispose(arr);
    equal(RiText.instances.length, 1);

    RiText.dispose(arr);
    equal(RiText.instances.length, 1);
  });

  test("RiText.dispose[all]", function() {

    RiText.instances = [];
    equal(RiText.instances.length, 0);

    for (var i = 0; i < 10; i++) {
      new RiText("hello" + i);
    }

    equal(RiText.instances.length, 10);
    RiText.disposeAll();
    equal(RiText.instances.length, 0);

    RiText.disposeAll();
    equal(RiText.instances.length, 0);
  });

  test("Private Objects", function() {

    equal(typeof RegexRule, 'undefined');
    equal(typeof Conjugator, 'undefined');
    equal(typeof PosTagger, 'undefined');
    equal(typeof Analyzer, 'undefined');
    equal(typeof MinEditDist, 'undefined');
  });

  ////////////////////////////////////////////////////////////////////////////

  // test("Member Functions[compat]", function() {
  //
  // RiTa.p5Compatible(1);
  // ok(typeof RiText().fill == 'function');
  // ok(typeof RiText().textAlign == 'function');
  // ok(typeof RiText().textFont == 'function');
  // ok(typeof RiText().textSize == 'function');
  // ok(typeof RiText().setColor == 'function');
  // });

  test("Member Functions[!compat]", function() {

    //RiTa.p5Compatible(0);
    ok(typeof RiText().fill == 'function');
    ok(typeof RiText().textAlign == 'undefined');
    ok(typeof RiText().textFont == 'undefined');
    ok(typeof RiText().textSize == 'undefined');
    ok(typeof RiText().setColor == 'undefined');
    //RiTa.p5Compatible(1);
  });

  test("Global Objects[!compat]", function() {

    //RiTa.p5Compatible(0);
    ok(typeof size != 'function');
    ok(typeof background != 'function');
    ok(typeof random != 'function');
  });

  test("Public Statics[!compat]", function() {

    //RiTa.p5Compatible(0);
    ok(typeof RiText.setDefaultFont, 'undefined');
    ok(typeof RiText.setDefaultColor, 'undefined');
    //RiTa.p5Compatible(1);
  });
}
