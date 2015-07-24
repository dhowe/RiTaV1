
function makeClass() {

  return function(args) {
    if (this instanceof arguments.callee) {
      if (typeof this.init == "function") {
        this.init.apply(this, args && args.callee ? args : arguments);
      }
    } else {
      return new arguments.callee(arguments);
    }
  };
}

'use strict';

var logDate = function() {
  console.log(new Date().getDate());
};

function logMonth() {
    console.log(new Date().getMonth());
};

var RiTa = makeClass();
RiTa.prototype = {
  init: function() {
    this.hello = "hello";
  },
  go: function() {
    console.log(this.hello);
  }
}
var RiTa2 = makeClass();
RiTa2.prototype = {
  init: function() {
    this.goodbye = "goodbye";
  },
  go: function() {
    console.log(this.goodbye);
  }
}
module.exports.logDate = function() { return 3; };
module.exports.logMonth = logMonth;
module.exports.RiTa = RiTa;
module.exports.RiTa2 = RiTa2;
