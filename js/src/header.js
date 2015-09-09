/*jshint browser:true, -W100:true, evil:true */

(function(window, undefined) {

var E= '', SP= ' ', EA= [], N= 'number', S= 'string', O= 'object',
  A= 'array', B= 'boolean', R= 'regexp', F= 'function';

function makeClass() { // from: Resig, TODO: make work with strict
  return function(args) {
    if (this instanceof arguments.callee) {
      if (typeof this.init == "function") {
        this.init.apply(this, args && args.callee ? args : arguments);
      }
    } else return new arguments.callee(arguments);
  };
}

// Returns true if the object is of type 'type', otherwise false
function is(obj, type) {
  return get(obj) === type;
}

// Throws TypeError if not the correct type, else returns true
function ok(obj, type) {
  if (get(obj) != type) {
    throw TypeError('Expected ' + (type ? type.toUpperCase() : type + E) +
      ", but received " + (obj ? get(obj).toUpperCase() : obj + E));
  }
  return true;
}

function isNum(n) {
  return !isNaN(parseFloat(n)) && isFinite(n);
}

function okeys(obj) {
  var keys = []; // replaces Object.keys();
  for (var k in obj) keys.push(k);
  return keys;
}

function err() {
  var msg = "[RiTa] " + arguments[0];
  for (var i = 1; i < arguments.length; i++)
    msg += '\n' + arguments[i];
  throw Error(msg);
}

function warn() {
  if (RiTa.SILENT || !console) return;
  if (arguments && arguments.length) {
    console.warn("[WARN] " + arguments[0]);
    for (var i = 1; i < arguments.length; i++)
      console.warn(arguments[i]);
  }
}

function log() {
  if (RiTa.SILENT || !console) return;
  console.log.apply(console, arguments);
}

function strOk(str) {
  return (typeof str === S && str.length > 0);
}

function trim(str) {
  if (!strOk(str)) return str;
  // from: http://blog.stevenlevithan.com/archives/faster-trim-javascript
  return str.replace(/^\s\s*/, '').replace(/\s\s*$/, '');
}

function last(word) { // last char of string
  if (!word || !word.length) return E;
  return word.charAt(word.length - 1);
}

function extend(l1, l2) { // python extend
  for (var i = 0; i < l2.length; i++)
    l1.push(l2[i]);
}

function endsWith(str, ending) {
  if (!is(str, S)) return false;
  return new RegExp(ending + '$').test(str);
}

function startsWith(str, starting) {
  if (!is(str, S)) return false;
  return new RegExp('^' + starting).test(str);
}

function equalsIgnoreCase(str1, str2) {
  return (is(str1, S) && is(str2, S)) ?
    (str1.toLowerCase() === str2.toLowerCase()) : false;
}

// Returns true if NodeJS is the current environment
function isNode() {
  return (typeof module != 'undefined' && module.exports);
}

function shuffle(oldArray) { // shuffle array
  var newArray = oldArray.slice(),
    len = newArray.length,
    i = len;
  while (i--) {
    var p = parseInt(Math.random() * len),
      t = newArray[i];
    newArray[i] = newArray[p];
    newArray[p] = t;
  }
  return newArray;
}

function inArray(array, val) {
  return (!array) ? false : array.indexOf(val) > -1;
}

function escapeRegExp(string) {
  return string.replace(/([.*+?^=!:${}()|\[\]\/\\])/g, "\\$1");
}

// From: http://javascriptweblog.wordpress.com/2011/08/08/fixing-the-javascript-typeof-operator/
function get(obj) {
  if (typeof obj != 'undefined') // else return undef
    return ({}).toString.call(obj).match(/\s([a-zA-Z]+)/)[1].toLowerCase();
}
