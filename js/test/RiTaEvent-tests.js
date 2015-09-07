/*global console, test, throws, equal, fail, notEqual, expect, require, ok,
    QUnit, RiTa, RiTaEvent, RiString, RiGrammar, RiMarkov, */

/*jshint loopfunc: true */

// Note: Don't need to extract test-results
var runtests = function() {

  QUnit.module("RiTaEvent", {
    setup: function() {},
    teardown: function() {}
  });

  test("testConstructor", function() {

    ok(RiTaEvent(this));
    ok(new RiTaEvent(this));
    ok(RiTaEvent(this, "test"));
    ok(new RiTaEvent(this, "test"));

    throws(function() {
      RiTa.SILENT = 1;
      try {

        new RiTaEvent();
        fail("no exception");
      } catch (e) {
        throw e;
      }
      RiTa.SILENT = 0;
    });

    throws(function() {
      RiTa.SILENT = 1;
      try {
        RiTaEvent();
        fail("no exception");
      } catch (e) {
        throw e;
      }
      RiTa.SILENT = 0;
    });

    var BAD = [null, undefined];

    for (var i = 0; i < BAD.length; i++) {

      throws(function() {
        RiTa.SILENT = 1;
        try {
          new RiTaEvent(BAD[i]);
          fail("no exception");
        } catch (e) {
          throw e;
        }
        RiTa.SILENT = 0;
      }, BAD[i]);

      throws(function() {
        RiTa.SILENT = 1;
        try {
          RiTaEvent(BAD[i]);
          fail("no exception");
        } catch (e) {
          throw e;
        }
        RiTa.SILENT = 0;
      }, BAD[i]);
    }
  });
  //
  // test("testSource", function() {
  //
  //   equal(RiTaEvent(this).source(), this);
  //   equal(new RiTaEvent(this, RiTa.TEXT_TO).source(), this);
  //   equal(RiTaEvent(this, RiTa.COLOR_TO).source(), this);
  //   equal(new RiTaEvent(this, RiTa.FADE_OUT).source(), this);
  // });
  //
  // test("testData", function() {
  //
  //   equal(RiTaEvent(this).data(), null);
  //   equal(RiTaEvent(this, RiTa.TEXT_TO).data(), null);
  //   equal(RiTaEvent(this, null, this).data(), this);
  //   equal(new RiTaEvent(this, RiTa.TEXT_TO, this).data(), this);
  //   equal(RiTaEvent(this, RiTa.COLOR_TO, this).data(), this);
  //   equal(new RiTaEvent(this, RiTa.FADE_OUT, this).data(), this);
  // });
  //
  // test("testType", function() {
  //
  //   equal(RiTaEvent(this).type(), RiTa.UNKNOWN);
  //   equal(new RiTaEvent(this, RiTa.TEXT_TO).type(), RiTa.TEXT_TO);
  //   equal(RiTaEvent(this, RiTa.COLOR_TO).type(), RiTa.COLOR_TO);
  //   equal(new RiTaEvent(this, RiTa.FADE_OUT).type(), RiTa.FADE_OUT);
  // });
  //
  test("testIsType", function() {

    equal(RiTaEvent(this).isType(RiTa.UNKNOWN), true);
    equal(new RiTaEvent(this, RiTa.INTERNAL).isType(RiTa.INTERNAL), true);
    equal(RiTaEvent(this, RiTa.DATA_LOADED).isType(RiTa.DATA_LOADED), true);
    equal(new RiTaEvent(this, RiTa.DATA_LOADED).isType(RiTa.INTERNAL), false);
  });

  test("testToString", function() {

    ok(RiTaEvent(this).toString()); //TODO: compare to RiTa
  });
};

if (typeof exports != 'undefined') runtests();
