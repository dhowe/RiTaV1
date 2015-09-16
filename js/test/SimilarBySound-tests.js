var runtests = function() {

  if (!RiLexicon.enabled) {
    console.warn("[INFO] SimilarBySound-tests: skipping ALL tests...");
    return;
  }

  var lex = new RiLexicon();

  QUnit.module("Similars", {
    setup: function() {},
    teardown: function() {}
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
}

if (typeof exports != 'undefined') runtests();
