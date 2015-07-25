var Util = {

  SP: ' ', E: '', EA: [],

  N: 'number', S: 'string', O: 'object', A: 'array', B: 'boolean', R: 'regexp', F: 'function',

  // Returns true if the object is of type 'type', otherwise false
  is: function(obj, type) {
    return Util.get(obj) === type;
  },

  // Throws TypeError if not the correct type, else returns true
  ok: function(obj, type) {
    if (Util.get(obj) != type) {
      throw TypeError('Expected ' + (type ? type.toUpperCase() : type + E) +
        ", but received " + (obj ? Util.get(obj).toUpperCase() : obj + E));
    }
    return true;
  },

  last: function(word) { // last char of string
    if (!word || !word.length) return E;
    return word.charAt(word.length - 1);
  },

  isNum: function(n) {
    return !isNaN(parseFloat(n)) && isFinite(n);
  },

  okeys: function(obj) {
    var keys = []; // replaces Object.keys();
    for (var k in obj) keys.push(k);
    return keys;
  },

  err: function() {
    var msg = "[RiTa] " + arguments[0];
    for (var i = 1; i < arguments.length; i++)
      msg += '\n' + arguments[i];
    throw Error(msg);
  },

  warn: function() {
    if (RiTa.SILENT || !console) return;
    if (arguments && arguments.length) {
      console.warn("[WARN] " + arguments[0]);
      for (var i = 1; i < arguments.length; i++)
        console.warn(arguments[i]);
    }
  },

  log: function() {
    if (RiTa.SILENT || !console) return;
    console.log.apply(console, arguments);
  },

  strOk: function(str) {
    return (typeof str === Util.S && str.length > 0);
  },

  trim: function(str) {
    if (!Util.strOk(str)) return str;
    // from: http://blog.stevenlevithan.com/archives/faster-trim-javascript
    return str.replace(/^\s\s*/, '').replace(/\s\s*$/, '');
  },

  last: function(word) { // last char of string
    if (!word || !word.length) return E;
    return word.charAt(word.length - 1);
  },

  extend: function(l1, l2) { // python extend
    for (var i = 0; i < l2.length; i++)
      l1.push(l2[i]);
  },

  endsWith: function(str, ending) {
    if (!is(str, Util.S)) return false;
    return new RegExp(ending + '$').test(str);
  },

  startsWith: function(str, starting) {
    if (!is(str, Util.S)) return false;
    return new RegExp('^' + starting).test(str);
  },

  equalsIgnoreCase: function(str1, str2) {
    return (is(str1, Util.S) && is(str2, Util.S)) ?
      (str1.toLowerCase() === str2.toLowerCase()) : false;
  },

  // Returns true if NodeJS is the current environment
  isNode: function() {
    return (typeof module != 'undefined' && module.exports);
  },

  shuffle: function(oldArray) { // shuffle array
    var newArray = oldArray.slice(), len = newArray.length, i = len;
    while (i--) {
      var p = parseInt(Math.random() * len), t = newArray[i];
      newArray[i] = newArray[p];
      newArray[p] = t;
    }
    return newArray;
  },

  inArray: function(array, val) {
    return (!array) ? false : array.indexOf(val) > -1;
  },

  escapeRegExp: function(string) {
    return string.replace(/([.*+?^=!:${}()|\[\]\/\\])/g, "\\$1");
  },

  // From: http://javascriptweblog.wordpress.com/2011/08/08/fixing-the-javascript-typeof-operator/
  get: function(obj) {
    if (typeof obj != 'undefined') // else return undef
      return ({}).toString.call(obj).match(/\s([a-zA-Z]+)/)[1].toLowerCase();
  }
};

module.exports = Util;
