/*jslint node: true*/
/*global equal, expect, require, ok, QUnit */

/*
 * loads the JSON doc-file and checks that each described field actually exists in the object
 */
QUnit.checkAPI = function(className, Class, obj, pathToDocs) {

  var eles = QUnit.propertiesFromAPI(pathToDocs, className);

  if (!eles) {

    expect(0);
    console.warn("[WARN] Skipped checkAPI() test for '" + className + "'");
    return;
  }

  for (var i = 0; eles && i < eles.length; i++) {

    //console.log("Checking "+eles[i].name);

    if (!eles[i] || eles[i] === 'undefined') {

      console.log("Null element in " + className.json);
      continue;
    }

    if (eles[i].isVar) {

      ok(obj.hasOwnProperty(eles[i].name), 'property: ' + eles[i].name);

    } else if (eles[i].isStatic) {

      equal(typeof Class[eles[i].name], 'function', 'static-function: ' + eles[i].name + '()');

    } else {

      equal(typeof obj[eles[i].name], 'function', 'function: ' + eles[i].name + '()');
    }
  }
};

/*
 * loads the JSON doc-file and populates and array of 'field' objects, where a field
 * contains a 'name' string, and 2 booleans: 'isVar' and 'isStatic'.
 */
QUnit.propertiesFromAPI = function(pathToDocs, className) {

  var jsonf, fields, elements = [];

  // for now, this only works in Node (and not from the browser OR an NPM package)
  if (typeof exports != 'undefined') {

    jsonf = pathToDocs + className;
    try {
      fields = require(jsonf).fields;
    } catch (e) {

      console.warn("[WARN] No json file at '" + jsonf + ".json', or perhaps the JSON is invalid?");
      return;
    }

    for (var i = 0, j = fields.length; i < j; i++) {

      if (fields[i].hidden) continue;

      var isVar = fields[i].variable || false,
        isStatic = (new RegExp("^" + className + ".").test(fields[i].name)),
        name = isStatic ? fields[i].name.substring(className.length + 1) : fields[i].name;

      elements.push({
        name: name,
        isVar: isVar,
        isStatic: isStatic
      });
    }
  }

  return elements;
};

// Adds some logging to the command-line (Node) test script
QUnit.moduleStart(function(n) {
  console.log("[INFO] Testing " + n.name);
});

// Adds some logging to the command-line (Node) test script
QUnit.moduleDone(function(n) {
  //console.log("[INFO] Completed "+n.name);
});
