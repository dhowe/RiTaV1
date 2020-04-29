(function(){/*

 Copyright The Closure Library Authors.
 SPDX-License-Identifier: Apache-2.0
*/
var n;function aa(a){var b=0;return function(){return b<a.length?{done:!1,value:a[b++]}:{done:!0}}}
var ba="function"==typeof Object.defineProperties?Object.defineProperty:function(a,b,c){a!=Array.prototype&&a!=Object.prototype&&(a[b]=c.value)};
function ca(a){a=["object"==typeof globalThis&&globalThis,a,"object"==typeof window&&window,"object"==typeof self&&self,"object"==typeof global&&global];for(var b=0;b<a.length;++b){var c=a[b];if(c&&c.Math==Math)return c}throw Error("Cannot find global object");}
var da=ca(this);function ea(){ea=function(){};
da.Symbol||(da.Symbol=fa)}
function ha(a,b){this.f=a;ba(this,"description",{configurable:!0,writable:!0,value:b})}
ha.prototype.toString=function(){return this.f};
var fa=function(){function a(c){if(this instanceof a)throw new TypeError("Symbol is not a constructor");return new ha("jscomp_symbol_"+(c||"")+"_"+b++,c)}
var b=0;return a}();
function ia(){ea();var a=da.Symbol.iterator;a||(a=da.Symbol.iterator=da.Symbol("Symbol.iterator"));"function"!=typeof Array.prototype[a]&&ba(Array.prototype,a,{configurable:!0,writable:!0,value:function(){return ja(aa(this))}});
ia=function(){}}
function ja(a){ia();a={next:a};a[da.Symbol.iterator]=function(){return this};
return a}
function p(a){var b="undefined"!=typeof Symbol&&Symbol.iterator&&a[Symbol.iterator];return b?b.call(a):{next:aa(a)}}
function ka(a){for(var b,c=[];!(b=a.next()).done;)c.push(b.value);return c}
var la="function"==typeof Object.create?Object.create:function(a){function b(){}
b.prototype=a;return new b},na;
if("function"==typeof Object.setPrototypeOf)na=Object.setPrototypeOf;else{var oa;a:{var pa={a:!0},qa={};try{qa.__proto__=pa;oa=qa.a;break a}catch(a){}oa=!1}na=oa?function(a,b){a.__proto__=b;if(a.__proto__!==b)throw new TypeError(a+" is not extensible");return a}:null}var ra=na;
function t(a,b){a.prototype=la(b.prototype);a.prototype.constructor=a;if(ra)ra(a,b);else for(var c in b)if("prototype"!=c)if(Object.defineProperties){var d=Object.getOwnPropertyDescriptor(b,c);d&&Object.defineProperty(a,c,d)}else a[c]=b[c];a.L=b.prototype}
function u(a,b){if(b){for(var c=da,d=a.split("."),e=0;e<d.length-1;e++){var f=d[e];f in c||(c[f]={});c=c[f]}d=d[d.length-1];e=c[d];f=b(e);f!=e&&null!=f&&ba(c,d,{configurable:!0,writable:!0,value:f})}}
function sa(a,b,c){if(null==a)throw new TypeError("The 'this' value for String.prototype."+c+" must not be null or undefined");if(b instanceof RegExp)throw new TypeError("First argument to String.prototype."+c+" must not be a regular expression");return a+""}
u("String.prototype.endsWith",function(a){return a?a:function(b,c){var d=sa(this,b,"endsWith");b+="";void 0===c&&(c=d.length);for(var e=Math.max(0,Math.min(c|0,d.length)),f=b.length;0<f&&0<e;)if(d[--e]!=b[--f])return!1;return 0>=f}});
u("String.prototype.startsWith",function(a){return a?a:function(b,c){var d=sa(this,b,"startsWith");b+="";for(var e=d.length,f=b.length,g=Math.max(0,Math.min(c|0,d.length)),h=0;h<f&&g<e;)if(d[g++]!=b[h++])return!1;return h>=f}});
function w(a,b){return Object.prototype.hasOwnProperty.call(a,b)}
var ta="function"==typeof Object.assign?Object.assign:function(a,b){for(var c=1;c<arguments.length;c++){var d=arguments[c];if(d)for(var e in d)w(d,e)&&(a[e]=d[e])}return a};
u("Object.assign",function(a){return a||ta});
u("WeakMap",function(a){function b(k){this.f=(h+=Math.random()+1).toString();if(k){k=p(k);for(var l;!(l=k.next()).done;)l=l.value,this.set(l[0],l[1])}}
function c(){}
function d(k){var l=typeof k;return"object"===l&&null!==k||"function"===l}
function e(k){if(!w(k,g)){var l=new c;ba(k,g,{value:l})}}
function f(k){var l=Object[k];l&&(Object[k]=function(m){if(m instanceof c)return m;e(m);return l(m)})}
if(function(){if(!a||!Object.seal)return!1;try{var k=Object.seal({}),l=Object.seal({}),m=new a([[k,2],[l,3]]);if(2!=m.get(k)||3!=m.get(l))return!1;m["delete"](k);m.set(l,4);return!m.has(k)&&4==m.get(l)}catch(q){return!1}}())return a;
var g="$jscomp_hidden_"+Math.random();f("freeze");f("preventExtensions");f("seal");var h=0;b.prototype.set=function(k,l){if(!d(k))throw Error("Invalid WeakMap key");e(k);if(!w(k,g))throw Error("WeakMap key fail: "+k);k[g][this.f]=l;return this};
b.prototype.get=function(k){return d(k)&&w(k,g)?k[g][this.f]:void 0};
b.prototype.has=function(k){return d(k)&&w(k,g)&&w(k[g],this.f)};
b.prototype["delete"]=function(k){return d(k)&&w(k,g)&&w(k[g],this.f)?delete k[g][this.f]:!1};
return b});
u("Map",function(a){function b(){var h={};return h.previous=h.next=h.head=h}
function c(h,k){var l=h.f;return ja(function(){if(l){for(;l.head!=h.f;)l=l.previous;for(;l.next!=l.head;)return l=l.next,{done:!1,value:k(l)};l=null}return{done:!0,value:void 0}})}
function d(h,k){var l=k&&typeof k;"object"==l||"function"==l?f.has(k)?l=f.get(k):(l=""+ ++g,f.set(k,l)):l="p_"+k;var m=h.g[l];if(m&&w(h.g,l))for(var q=0;q<m.length;q++){var v=m[q];if(k!==k&&v.key!==v.key||k===v.key)return{id:l,list:m,index:q,u:v}}return{id:l,list:m,index:-1,u:void 0}}
function e(h){this.g={};this.f=b();this.size=0;if(h){h=p(h);for(var k;!(k=h.next()).done;)k=k.value,this.set(k[0],k[1])}}
if(function(){if(!a||"function"!=typeof a||!a.prototype.entries||"function"!=typeof Object.seal)return!1;try{var h=Object.seal({x:4}),k=new a(p([[h,"s"]]));if("s"!=k.get(h)||1!=k.size||k.get({x:4})||k.set({x:4},"t")!=k||2!=k.size)return!1;var l=k.entries(),m=l.next();if(m.done||m.value[0]!=h||"s"!=m.value[1])return!1;m=l.next();return m.done||4!=m.value[0].x||"t"!=m.value[1]||!l.next().done?!1:!0}catch(q){return!1}}())return a;
ia();var f=new WeakMap;e.prototype.set=function(h,k){h=0===h?0:h;var l=d(this,h);l.list||(l.list=this.g[l.id]=[]);l.u?l.u.value=k:(l.u={next:this.f,previous:this.f.previous,head:this.f,key:h,value:k},l.list.push(l.u),this.f.previous.next=l.u,this.f.previous=l.u,this.size++);return this};
e.prototype["delete"]=function(h){h=d(this,h);return h.u&&h.list?(h.list.splice(h.index,1),h.list.length||delete this.g[h.id],h.u.previous.next=h.u.next,h.u.next.previous=h.u.previous,h.u.head=null,this.size--,!0):!1};
e.prototype.clear=function(){this.g={};this.f=this.f.previous=b();this.size=0};
e.prototype.has=function(h){return!!d(this,h).u};
e.prototype.get=function(h){return(h=d(this,h).u)&&h.value};
e.prototype.entries=function(){return c(this,function(h){return[h.key,h.value]})};
e.prototype.keys=function(){return c(this,function(h){return h.key})};
e.prototype.values=function(){return c(this,function(h){return h.value})};
e.prototype.forEach=function(h,k){for(var l=this.entries(),m;!(m=l.next()).done;)m=m.value,h.call(k,m[1],m[0],this)};
e.prototype[Symbol.iterator]=e.prototype.entries;var g=0;return e});
u("Object.entries",function(a){return a?a:function(b){var c=[],d;for(d in b)w(b,d)&&c.push([d,b[d]]);return c}});
u("Set",function(a){function b(c){this.f=new Map;if(c){c=p(c);for(var d;!(d=c.next()).done;)this.add(d.value)}this.size=this.f.size}
if(function(){if(!a||"function"!=typeof a||!a.prototype.entries||"function"!=typeof Object.seal)return!1;try{var c=Object.seal({x:4}),d=new a(p([c]));if(!d.has(c)||1!=d.size||d.add(c)!=d||1!=d.size||d.add({x:4})!=d||2!=d.size)return!1;var e=d.entries(),f=e.next();if(f.done||f.value[0]!=c||f.value[1]!=c)return!1;f=e.next();return f.done||f.value[0]==c||4!=f.value[0].x||f.value[1]!=f.value[0]?!1:e.next().done}catch(g){return!1}}())return a;
ia();b.prototype.add=function(c){c=0===c?0:c;this.f.set(c,c);this.size=this.f.size;return this};
b.prototype["delete"]=function(c){c=this.f["delete"](c);this.size=this.f.size;return c};
b.prototype.clear=function(){this.f.clear();this.size=0};
b.prototype.has=function(c){return this.f.has(c)};
b.prototype.entries=function(){return this.f.entries()};
b.prototype.values=function(){return this.f.values()};
b.prototype.keys=b.prototype.values;b.prototype[Symbol.iterator]=b.prototype.values;b.prototype.forEach=function(c,d){var e=this;this.f.forEach(function(f){return c.call(d,f,f,e)})};
return b});
u("String.prototype.includes",function(a){return a?a:function(b,c){return-1!==sa(this,b,"includes").indexOf(b,c||0)}});
u("Promise",function(a){function b(g){this.g=0;this.h=void 0;this.f=[];var h=this.i();try{g(h.resolve,h.reject)}catch(k){h.reject(k)}}
function c(){this.f=null}
function d(g){return g instanceof b?g:new b(function(h){h(g)})}
if(a)return a;c.prototype.g=function(g){if(null==this.f){this.f=[];var h=this;this.h(function(){h.j()})}this.f.push(g)};
var e=da.setTimeout;c.prototype.h=function(g){e(g,0)};
c.prototype.j=function(){for(;this.f&&this.f.length;){var g=this.f;this.f=[];for(var h=0;h<g.length;++h){var k=g[h];g[h]=null;try{k()}catch(l){this.i(l)}}}this.f=null};
c.prototype.i=function(g){this.h(function(){throw g;})};
b.prototype.i=function(){function g(l){return function(m){k||(k=!0,l.call(h,m))}}
var h=this,k=!1;return{resolve:g(this.A),reject:g(this.j)}};
b.prototype.A=function(g){if(g===this)this.j(new TypeError("A Promise cannot resolve to itself"));else if(g instanceof b)this.D(g);else{a:switch(typeof g){case "object":var h=null!=g;break a;case "function":h=!0;break a;default:h=!1}h?this.w(g):this.l(g)}};
b.prototype.w=function(g){var h=void 0;try{h=g.then}catch(k){this.j(k);return}"function"==typeof h?this.F(h,g):this.l(g)};
b.prototype.j=function(g){this.m(2,g)};
b.prototype.l=function(g){this.m(1,g)};
b.prototype.m=function(g,h){if(0!=this.g)throw Error("Cannot settle("+g+", "+h+"): Promise already settled in state"+this.g);this.g=g;this.h=h;this.B()};
b.prototype.B=function(){if(null!=this.f){for(var g=0;g<this.f.length;++g)f.g(this.f[g]);this.f=null}};
var f=new c;b.prototype.D=function(g){var h=this.i();g.aa(h.resolve,h.reject)};
b.prototype.F=function(g,h){var k=this.i();try{g.call(h,k.resolve,k.reject)}catch(l){k.reject(l)}};
b.prototype.then=function(g,h){function k(v,r){return"function"==typeof v?function(B){try{l(v(B))}catch(K){m(K)}}:r}
var l,m,q=new b(function(v,r){l=v;m=r});
this.aa(k(g,l),k(h,m));return q};
b.prototype["catch"]=function(g){return this.then(void 0,g)};
b.prototype.aa=function(g,h){function k(){switch(l.g){case 1:g(l.h);break;case 2:h(l.h);break;default:throw Error("Unexpected state: "+l.g);}}
var l=this;null==this.f?f.g(k):this.f.push(k)};
b.resolve=d;b.reject=function(g){return new b(function(h,k){k(g)})};
b.race=function(g){return new b(function(h,k){for(var l=p(g),m=l.next();!m.done;m=l.next())d(m.value).aa(h,k)})};
b.all=function(g){var h=p(g),k=h.next();return k.done?d([]):new b(function(l,m){function q(B){return function(K){v[B]=K;r--;0==r&&l(v)}}
var v=[],r=0;do v.push(void 0),r++,d(k.value).aa(q(v.length-1),m),k=h.next();while(!k.done)})};
return b});
var ua=function(){function a(){function c(){}
new c;Reflect.construct(c,[],function(){});
return new c instanceof c}
if("undefined"!=typeof Reflect&&Reflect.construct){if(a())return Reflect.construct;var b=Reflect.construct;return function(c,d,e){c=b(c,d);e&&Reflect.setPrototypeOf(c,e.prototype);return c}}return function(c,d,e){void 0===e&&(e=c);
e=la(e.prototype||Object.prototype);return Function.prototype.apply.call(c,e,d)||e}}();
u("Reflect.construct",function(){return ua});
var x=this||self;function y(a,b,c){a=a.split(".");c=c||x;a[0]in c||"undefined"==typeof c.execScript||c.execScript("var "+a[0]);for(var d;a.length&&(d=a.shift());)a.length||void 0===b?c[d]&&c[d]!==Object.prototype[d]?c=c[d]:c=c[d]={}:c[d]=b}
var va=/^[\w+/_-]+[=]{0,2}$/,wa=null;function z(a,b){for(var c=a.split("."),d=b||x,e=0;e<c.length;e++)if(d=d[c[e]],null==d)return null;return d}
function xa(){}
function ya(a){a.ia=void 0;a.getInstance=function(){return a.ia?a.ia:a.ia=new a}}
function za(a){var b=typeof a;if("object"==b)if(a){if(a instanceof Array)return"array";if(a instanceof Object)return b;var c=Object.prototype.toString.call(a);if("[object Window]"==c)return"object";if("[object Array]"==c||"number"==typeof a.length&&"undefined"!=typeof a.splice&&"undefined"!=typeof a.propertyIsEnumerable&&!a.propertyIsEnumerable("splice"))return"array";if("[object Function]"==c||"undefined"!=typeof a.call&&"undefined"!=typeof a.propertyIsEnumerable&&!a.propertyIsEnumerable("call"))return"function"}else return"null";
else if("function"==b&&"undefined"==typeof a.call)return"object";return b}
function Aa(a){return"array"==za(a)}
function Ba(a){var b=za(a);return"array"==b||"object"==b&&"number"==typeof a.length}
function A(a){return"function"==za(a)}
function Ca(a){var b=typeof a;return"object"==b&&null!=a||"function"==b}
function Da(a){return Object.prototype.hasOwnProperty.call(a,Ea)&&a[Ea]||(a[Ea]=++Fa)}
var Ea="closure_uid_"+(1E9*Math.random()>>>0),Fa=0;function Ga(a,b,c){return a.call.apply(a.bind,arguments)}
function Ha(a,b,c){if(!a)throw Error();if(2<arguments.length){var d=Array.prototype.slice.call(arguments,2);return function(){var e=Array.prototype.slice.call(arguments);Array.prototype.unshift.apply(e,d);return a.apply(b,e)}}return function(){return a.apply(b,arguments)}}
function C(a,b,c){Function.prototype.bind&&-1!=Function.prototype.bind.toString().indexOf("native code")?C=Ga:C=Ha;return C.apply(null,arguments)}
function Ja(a,b){var c=Array.prototype.slice.call(arguments,1);return function(){var d=c.slice();d.push.apply(d,arguments);return a.apply(this,d)}}
var D=Date.now||function(){return+new Date};
function Ka(a,b){y(a,b,void 0)}
function E(a,b){function c(){}
c.prototype=b.prototype;a.L=b.prototype;a.prototype=new c;a.prototype.constructor=a}
;function La(a,b){if(!a||/[?&]dsh=1(&|$)/.test(a))return null;if(/[?&]ae=1(&|$)/.test(a)){var c=/[?&]adurl=([^&]+)/.exec(a);if(!c)return null;var d=b?c.index:a.length;try{return{xa:a.slice(0,d)+"&act=1"+a.slice(d),za:decodeURIComponent(c[1])}}catch(f){return null}}if(/[?&]ae=2(&|$)/.test(a)){c=a;d="";if(b){var e=a.indexOf("&adurl=");0<e&&(c=a.slice(0,e),d=a.slice(e))}return{xa:c+"&act=1"+d,za:c+"&dct=1"+d}}return null}
;function F(a){if(Error.captureStackTrace)Error.captureStackTrace(this,F);else{var b=Error().stack;b&&(this.stack=b)}a&&(this.message=String(a))}
E(F,Error);F.prototype.name="CustomError";function Ma(a){var b=a.url,c=a.vb;this.j=!!a.bb;this.g=La(b,c);a=/[?&]dsh=1(&|$)/.test(b);this.h=!a&&/[?&]ae=1(&|$)/.test(b);this.i=!a&&/[?&]ae=2(&|$)/.test(b);this.f=/[?&]adurl=([^&]*)/.exec(b)}
;var Na=Array.prototype.indexOf?function(a,b){return Array.prototype.indexOf.call(a,b,void 0)}:function(a,b){if("string"===typeof a)return"string"!==typeof b||1!=b.length?-1:a.indexOf(b,0);
for(var c=0;c<a.length;c++)if(c in a&&a[c]===b)return c;return-1},G=Array.prototype.forEach?function(a,b,c){Array.prototype.forEach.call(a,b,c)}:function(a,b,c){for(var d=a.length,e="string"===typeof a?a.split(""):a,f=0;f<d;f++)f in e&&b.call(c,e[f],f,a)},Oa=Array.prototype.filter?function(a,b){return Array.prototype.filter.call(a,b,void 0)}:function(a,b){for(var c=a.length,d=[],e=0,f="string"===typeof a?a.split(""):a,g=0;g<c;g++)if(g in f){var h=f[g];
b.call(void 0,h,g,a)&&(d[e++]=h)}return d},Pa=Array.prototype.map?function(a,b){return Array.prototype.map.call(a,b,void 0)}:function(a,b){for(var c=a.length,d=Array(c),e="string"===typeof a?a.split(""):a,f=0;f<c;f++)f in e&&(d[f]=b.call(void 0,e[f],f,a));
return d},Qa=Array.prototype.reduce?function(a,b,c){return Array.prototype.reduce.call(a,b,c)}:function(a,b,c){var d=c;
G(a,function(e,f){d=b.call(void 0,d,e,f,a)});
return d};
function Ra(a,b){a:{var c=a.length;for(var d="string"===typeof a?a.split(""):a,e=0;e<c;e++)if(e in d&&b.call(void 0,d[e],e,a)){c=e;break a}c=-1}return 0>c?null:"string"===typeof a?a.charAt(c):a[c]}
function Sa(a,b){var c=Na(a,b);0<=c&&Array.prototype.splice.call(a,c,1)}
function Ta(a){var b=a.length;if(0<b){for(var c=Array(b),d=0;d<b;d++)c[d]=a[d];return c}return[]}
function Ua(a,b){for(var c=1;c<arguments.length;c++){var d=arguments[c];if(Ba(d)){var e=a.length||0,f=d.length||0;a.length=e+f;for(var g=0;g<f;g++)a[e+g]=d[g]}else a.push(d)}}
;function Va(a){var b=!1,c;return function(){b||(c=a(),b=!0);return c}}
;function Wa(a,b){for(var c in a)b.call(void 0,a[c],c,a)}
function Xa(a,b){var c=Ba(b),d=c?b:arguments;for(c=c?0:1;c<d.length;c++){if(null==a)return;a=a[d[c]]}return a}
function Ya(a){var b=Za,c;for(c in b)if(a.call(void 0,b[c],c,b))return c}
function $a(a){for(var b in a)return!1;return!0}
function ab(a,b){if(null!==a&&b in a)throw Error('The object already contains the key "'+b+'"');a[b]=!0}
function bb(a,b){for(var c in a)if(!(c in b)||a[c]!==b[c])return!1;for(var d in b)if(!(d in a))return!1;return!0}
function cb(a){var b={},c;for(c in a)b[c]=a[c];return b}
function db(a){var b=za(a);if("object"==b||"array"==b){if(A(a.clone))return a.clone();b="array"==b?[]:{};for(var c in a)b[c]=db(a[c]);return b}return a}
var eb="constructor hasOwnProperty isPrototypeOf propertyIsEnumerable toLocaleString toString valueOf".split(" ");function fb(a,b){for(var c,d,e=1;e<arguments.length;e++){d=arguments[e];for(c in d)a[c]=d[c];for(var f=0;f<eb.length;f++)c=eb[f],Object.prototype.hasOwnProperty.call(d,c)&&(a[c]=d[c])}}
;function gb(a,b){this.f=a===hb&&b||"";this.g=ib}
gb.prototype.J=!0;gb.prototype.I=function(){return this.f};
function jb(){var a=kb;return a instanceof gb&&a.constructor===gb&&a.g===ib?a.f:"type_error:Const"}
var ib={},hb={},kb=new gb(hb,"");function lb(a,b){this.f=a===mb&&b||"";this.g=nb}
lb.prototype.J=!0;lb.prototype.I=function(){return this.f.toString()};
lb.prototype.ha=!0;lb.prototype.ea=function(){return 1};
function ob(a){if(a instanceof lb&&a.constructor===lb&&a.g===nb)return a.f;za(a);return"type_error:TrustedResourceUrl"}
var nb={},mb={};var pb=String.prototype.trim?function(a){return a.trim()}:function(a){return/^[\s\xa0]*([\s\S]*?)[\s\xa0]*$/.exec(a)[1]};
function qb(a,b){if(b)a=a.replace(rb,"&amp;").replace(sb,"&lt;").replace(tb,"&gt;").replace(ub,"&quot;").replace(vb,"&#39;").replace(wb,"&#0;");else{if(!xb.test(a))return a;-1!=a.indexOf("&")&&(a=a.replace(rb,"&amp;"));-1!=a.indexOf("<")&&(a=a.replace(sb,"&lt;"));-1!=a.indexOf(">")&&(a=a.replace(tb,"&gt;"));-1!=a.indexOf('"')&&(a=a.replace(ub,"&quot;"));-1!=a.indexOf("'")&&(a=a.replace(vb,"&#39;"));-1!=a.indexOf("\x00")&&(a=a.replace(wb,"&#0;"))}return a}
var rb=/&/g,sb=/</g,tb=/>/g,ub=/"/g,vb=/'/g,wb=/\x00/g,xb=/[\x00&<>"']/;function H(a,b){this.f=a===yb&&b||"";this.g=zb}
H.prototype.J=!0;H.prototype.I=function(){return this.f.toString()};
H.prototype.ha=!0;H.prototype.ea=function(){return 1};
function Ab(a){if(a instanceof H&&a.constructor===H&&a.g===zb)return a.f;za(a);return"type_error:SafeUrl"}
var Bb=/^(?:(?:https?|mailto|ftp):|[^:/?#]*(?:[/?#]|$))/i;function Cb(a){if(a instanceof H)return a;a="object"==typeof a&&a.J?a.I():String(a);Bb.test(a)||(a="about:invalid#zClosurez");return new H(yb,a)}
var zb={},yb={};var Db;a:{var Eb=x.navigator;if(Eb){var Fb=Eb.userAgent;if(Fb){Db=Fb;break a}}Db=""}function I(a){return-1!=Db.indexOf(a)}
;function Gb(){this.f="";this.h=Hb;this.g=null}
Gb.prototype.ha=!0;Gb.prototype.ea=function(){return this.g};
Gb.prototype.J=!0;Gb.prototype.I=function(){return this.f.toString()};
function Ib(a){if(a instanceof Gb&&a.constructor===Gb&&a.h===Hb)return a.f;za(a);return"type_error:SafeHtml"}
var Hb={};function Jb(a,b){var c=new Gb;c.f=a;c.g=b;return c}
Jb("<!DOCTYPE html>",0);var Lb=Jb("",0);Jb("<br>",0);function Mb(a,b){var c=b instanceof H?b:Cb(b);a.href=Ab(c)}
function Nb(a,b){a.src=ob(b);if(null===wa)b:{var c=x.document;if((c=c.querySelector&&c.querySelector("script[nonce]"))&&(c=c.nonce||c.getAttribute("nonce"))&&va.test(c)){wa=c;break b}wa=""}c=wa;c&&a.setAttribute("nonce",c)}
;function Ob(a){return a=qb(a,void 0)}
function Pb(a){for(var b=0,c=0;c<a.length;++c)b=31*b+a.charCodeAt(c)>>>0;return b}
;var Qb=/^(?:([^:/?#.]+):)?(?:\/\/(?:([^\\/?#]*)@)?([^\\/?#]*?)(?::([0-9]+))?(?=[\\/?#]|$))?([^?#]+)?(?:\?([^#]*))?(?:#([\s\S]*))?$/;function J(a){return a?decodeURI(a):a}
function L(a,b){return b.match(Qb)[a]||null}
function Rb(a,b,c){if(Array.isArray(b))for(var d=0;d<b.length;d++)Rb(a,String(b[d]),c);else null!=b&&c.push(a+(""===b?"":"="+encodeURIComponent(String(b))))}
function Sb(a){var b=[],c;for(c in a)Rb(c,a[c],b);return b.join("&")}
function Tb(a,b){var c=Sb(b);if(c){var d=a.indexOf("#");0>d&&(d=a.length);var e=a.indexOf("?");if(0>e||e>d){e=d;var f=""}else f=a.substring(e+1,d);d=[a.substr(0,e),f,a.substr(d)];e=d[1];d[1]=c?e?e+"&"+c:c:e;c=d[0]+(d[1]?"?"+d[1]:"")+d[2]}else c=a;return c}
var Ub=/#|$/;function Vb(a,b){var c=a.search(Ub);a:{var d=0;for(var e=b.length;0<=(d=a.indexOf(b,d))&&d<c;){var f=a.charCodeAt(d-1);if(38==f||63==f)if(f=a.charCodeAt(d+e),!f||61==f||38==f||35==f)break a;d+=e+1}d=-1}if(0>d)return null;e=a.indexOf("&",d);if(0>e||e>c)e=c;d+=b.length+1;return decodeURIComponent(a.substr(d,e-d).replace(/\+/g," "))}
;var Wb=I("Opera"),Xb=I("Trident")||I("MSIE"),Yb=I("Edge"),Zb=I("Gecko")&&!(-1!=Db.toLowerCase().indexOf("webkit")&&!I("Edge"))&&!(I("Trident")||I("MSIE"))&&!I("Edge"),$b=-1!=Db.toLowerCase().indexOf("webkit")&&!I("Edge");function ac(){var a=x.document;return a?a.documentMode:void 0}
var bc;a:{var cc="",dc=function(){var a=Db;if(Zb)return/rv:([^\);]+)(\)|;)/.exec(a);if(Yb)return/Edge\/([\d\.]+)/.exec(a);if(Xb)return/\b(?:MSIE|rv)[: ]([^\);]+)(\)|;)/.exec(a);if($b)return/WebKit\/(\S+)/.exec(a);if(Wb)return/(?:Version)[ \/]?(\S+)/.exec(a)}();
dc&&(cc=dc?dc[1]:"");if(Xb){var ec=ac();if(null!=ec&&ec>parseFloat(cc)){bc=String(ec);break a}}bc=cc}var fc=bc,gc;if(x.document&&Xb){var hc=ac();gc=hc?hc:parseInt(fc,10)||void 0}else gc=void 0;var ic=gc;var jc={},kc=null;var M=window;function lc(a){var b=z("window.location.href");null==a&&(a='Unknown Error of type "null/undefined"');if("string"===typeof a)return{message:a,name:"Unknown error",lineNumber:"Not available",fileName:b,stack:"Not available"};var c=!1;try{var d=a.lineNumber||a.line||"Not available"}catch(f){d="Not available",c=!0}try{var e=a.fileName||a.filename||a.sourceURL||x.$googDebugFname||b}catch(f){e="Not available",c=!0}return!c&&a.lineNumber&&a.fileName&&a.stack&&a.message&&a.name?a:(b=a.message,null==b&&(a.constructor&&
a.constructor instanceof Function?(a.constructor.name?b=a.constructor.name:(b=a.constructor,mc[b]?b=mc[b]:(b=String(b),mc[b]||(c=/function\s+([^\(]+)/m.exec(b),mc[b]=c?c[1]:"[Anonymous]"),b=mc[b])),b='Unknown Error of type "'+b+'"'):b="Unknown Error of unknown type"),{message:b,name:a.name||"UnknownError",lineNumber:d,fileName:e,stack:a.stack||"Not available"})}
var mc={};function nc(a){this.f=a||{cookie:""}}
n=nc.prototype;n.isEnabled=function(){return navigator.cookieEnabled};
n.set=function(a,b,c){var d=!1;if("object"===typeof c){var e=c.xb;d=c.secure||!1;var f=c.domain||void 0;var g=c.path||void 0;var h=c.ma}if(/[;=\s]/.test(a))throw Error('Invalid cookie name "'+a+'"');if(/[;\r\n]/.test(b))throw Error('Invalid cookie value "'+b+'"');void 0===h&&(h=-1);c=f?";domain="+f:"";g=g?";path="+g:"";d=d?";secure":"";h=0>h?"":0==h?";expires="+(new Date(1970,1,1)).toUTCString():";expires="+(new Date(D()+1E3*h)).toUTCString();this.f.cookie=a+"="+b+c+g+h+d+(null!=e?";samesite="+e:
"")};
n.get=function(a,b){for(var c=a+"=",d=(this.f.cookie||"").split(";"),e=0,f;e<d.length;e++){f=pb(d[e]);if(0==f.lastIndexOf(c,0))return f.substr(c.length);if(f==a)return""}return b};
n.remove=function(a,b,c){var d=void 0!==this.get(a);this.set(a,"",{ma:0,path:b,domain:c});return d};
n.isEmpty=function(){return!this.f.cookie};
n.clear=function(){for(var a=(this.f.cookie||"").split(";"),b=[],c=[],d,e,f=0;f<a.length;f++)e=pb(a[f]),d=e.indexOf("="),-1==d?(b.push(""),c.push(e)):(b.push(e.substring(0,d)),c.push(e.substring(d+1)));for(a=b.length-1;0<=a;a--)this.remove(b[a])};
var oc=new nc("undefined"==typeof document?null:document);var pc=!Xb||9<=Number(ic);function qc(a,b){this.x=void 0!==a?a:0;this.y=void 0!==b?b:0}
n=qc.prototype;n.clone=function(){return new qc(this.x,this.y)};
n.equals=function(a){return a instanceof qc&&(this==a?!0:this&&a?this.x==a.x&&this.y==a.y:!1)};
n.ceil=function(){this.x=Math.ceil(this.x);this.y=Math.ceil(this.y);return this};
n.floor=function(){this.x=Math.floor(this.x);this.y=Math.floor(this.y);return this};
n.round=function(){this.x=Math.round(this.x);this.y=Math.round(this.y);return this};function rc(a,b){this.width=a;this.height=b}
n=rc.prototype;n.clone=function(){return new rc(this.width,this.height)};
n.aspectRatio=function(){return this.width/this.height};
n.isEmpty=function(){return!(this.width*this.height)};
n.ceil=function(){this.width=Math.ceil(this.width);this.height=Math.ceil(this.height);return this};
n.floor=function(){this.width=Math.floor(this.width);this.height=Math.floor(this.height);return this};
n.round=function(){this.width=Math.round(this.width);this.height=Math.round(this.height);return this};function sc(a){var b=document;return"string"===typeof a?b.getElementById(a):a}
function tc(a,b){Wa(b,function(c,d){c&&"object"==typeof c&&c.J&&(c=c.I());"style"==d?a.style.cssText=c:"class"==d?a.className=c:"for"==d?a.htmlFor=c:uc.hasOwnProperty(d)?a.setAttribute(uc[d],c):0==d.lastIndexOf("aria-",0)||0==d.lastIndexOf("data-",0)?a.setAttribute(d,c):a[d]=c})}
var uc={cellpadding:"cellPadding",cellspacing:"cellSpacing",colspan:"colSpan",frameborder:"frameBorder",height:"height",maxlength:"maxLength",nonce:"nonce",role:"role",rowspan:"rowSpan",type:"type",usemap:"useMap",valign:"vAlign",width:"width"};
function vc(a,b,c){var d=arguments,e=document,f=String(d[0]),g=d[1];if(!pc&&g&&(g.name||g.type)){f=["<",f];g.name&&f.push(' name="',Ob(g.name),'"');if(g.type){f.push(' type="',Ob(g.type),'"');var h={};fb(h,g);delete h.type;g=h}f.push(">");f=f.join("")}f=wc(e,f);g&&("string"===typeof g?f.className=g:Array.isArray(g)?f.className=g.join(" "):tc(f,g));2<d.length&&xc(e,f,d);return f}
function xc(a,b,c){function d(g){g&&b.appendChild("string"===typeof g?a.createTextNode(g):g)}
for(var e=2;e<c.length;e++){var f=c[e];!Ba(f)||Ca(f)&&0<f.nodeType?d(f):G(yc(f)?Ta(f):f,d)}}
function wc(a,b){b=String(b);"application/xhtml+xml"===a.contentType&&(b=b.toLowerCase());return a.createElement(b)}
function yc(a){if(a&&"number"==typeof a.length){if(Ca(a))return"function"==typeof a.item||"string"==typeof a.item;if(A(a))return"function"==typeof a.item}return!1}
function zc(a,b){for(var c=0;a;){if(b(a))return a;a=a.parentNode;c++}return null}
;function Ac(a){Bc();return new lb(mb,a)}
var Bc=xa;function Cc(a){var b=Dc;if(b)for(var c in b)Object.prototype.hasOwnProperty.call(b,c)&&a.call(void 0,b[c],c,b)}
function Ec(){var a=[];Cc(function(b){a.push(b)});
return a}
var Dc={cb:"allow-forms",eb:"allow-modals",fb:"allow-orientation-lock",gb:"allow-pointer-lock",hb:"allow-popups",ib:"allow-popups-to-escape-sandbox",jb:"allow-presentation",kb:"allow-same-origin",lb:"allow-scripts",mb:"allow-top-navigation",nb:"allow-top-navigation-by-user-activation"},Fc=Va(function(){return Ec()});
function Gc(){var a=wc(document,"IFRAME"),b={};G(Fc(),function(c){a.sandbox&&a.sandbox.supports&&a.sandbox.supports(c)&&(b[c]=!0)});
return b}
;function N(){this.g=this.g;this.B=this.B}
N.prototype.g=!1;N.prototype.dispose=function(){this.g||(this.g=!0,this.o())};
function Hc(a,b){a.g?b():(a.B||(a.B=[]),a.B.push(b))}
N.prototype.o=function(){if(this.B)for(;this.B.length;)this.B.shift()()};
function Ic(a){a&&"function"==typeof a.dispose&&a.dispose()}
function Jc(a){for(var b=0,c=arguments.length;b<c;++b){var d=arguments[b];Ba(d)?Jc.apply(null,d):Ic(d)}}
;/*
 Copyright (c) Microsoft Corporation. All rights reserved.
 Licensed under the Apache License, Version 2.0 (the "License"); you may not use
 this file except in compliance with the License. You may obtain a copy of the
 License at http://www.apache.org/licenses/LICENSE-2.0

 THIS CODE IS PROVIDED ON AN *AS IS* BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 KIND, EITHER EXPRESS OR IMPLIED, INCLUDING WITHOUT LIMITATION ANY IMPLIED
 WARRANTIES OR CONDITIONS OF TITLE, FITNESS FOR A PARTICULAR PURPOSE,
 MERCHANTABLITY OR NON-INFRINGEMENT.

 See the Apache Version 2.0 License for specific language governing permissions
 and limitations under the License.
*/
function Kc(a){"number"==typeof a&&(a=Math.round(a)+"px");return a}
;var Lc=(new Date).getTime();function Mc(a){if(!a)return"";a=a.split("#")[0].split("?")[0];a=a.toLowerCase();0==a.indexOf("//")&&(a=window.location.protocol+a);/^[\w\-]*:\/\//.test(a)||(a=window.location.href);var b=a.substring(a.indexOf("://")+3),c=b.indexOf("/");-1!=c&&(b=b.substring(0,c));a=a.substring(0,a.indexOf("://"));if("http"!==a&&"https"!==a&&"chrome-extension"!==a&&"file"!==a&&"android-app"!==a&&"chrome-search"!==a&&"chrome-untrusted"!==a&&"app"!==a)throw Error("Invalid URI scheme in origin: "+a);c="";var d=b.indexOf(":");
if(-1!=d){var e=b.substring(d+1);b=b.substring(0,d);if("http"===a&&"80"!==e||"https"===a&&"443"!==e)c=":"+e}return a+"://"+b+c}
;function Nc(){function a(){e[0]=1732584193;e[1]=4023233417;e[2]=2562383102;e[3]=271733878;e[4]=3285377520;m=l=0}
function b(q){for(var v=g,r=0;64>r;r+=4)v[r/4]=q[r]<<24|q[r+1]<<16|q[r+2]<<8|q[r+3];for(r=16;80>r;r++)q=v[r-3]^v[r-8]^v[r-14]^v[r-16],v[r]=(q<<1|q>>>31)&4294967295;q=e[0];var B=e[1],K=e[2],ma=e[3],Vc=e[4];for(r=0;80>r;r++){if(40>r)if(20>r){var Ia=ma^B&(K^ma);var Kb=1518500249}else Ia=B^K^ma,Kb=1859775393;else 60>r?(Ia=B&K|ma&(B|K),Kb=2400959708):(Ia=B^K^ma,Kb=3395469782);Ia=((q<<5|q>>>27)&4294967295)+Ia+Vc+Kb+v[r]&4294967295;Vc=ma;ma=K;K=(B<<30|B>>>2)&4294967295;B=q;q=Ia}e[0]=e[0]+q&4294967295;e[1]=
e[1]+B&4294967295;e[2]=e[2]+K&4294967295;e[3]=e[3]+ma&4294967295;e[4]=e[4]+Vc&4294967295}
function c(q,v){if("string"===typeof q){q=unescape(encodeURIComponent(q));for(var r=[],B=0,K=q.length;B<K;++B)r.push(q.charCodeAt(B));q=r}v||(v=q.length);r=0;if(0==l)for(;r+64<v;)b(q.slice(r,r+64)),r+=64,m+=64;for(;r<v;)if(f[l++]=q[r++],m++,64==l)for(l=0,b(f);r+64<v;)b(q.slice(r,r+64)),r+=64,m+=64}
function d(){var q=[],v=8*m;56>l?c(h,56-l):c(h,64-(l-56));for(var r=63;56<=r;r--)f[r]=v&255,v>>>=8;b(f);for(r=v=0;5>r;r++)for(var B=24;0<=B;B-=8)q[v++]=e[r]>>B&255;return q}
for(var e=[],f=[],g=[],h=[128],k=1;64>k;++k)h[k]=0;var l,m;a();return{reset:a,update:c,digest:d,ya:function(){for(var q=d(),v="",r=0;r<q.length;r++)v+="0123456789ABCDEF".charAt(Math.floor(q[r]/16))+"0123456789ABCDEF".charAt(q[r]%16);return v}}}
;function Oc(a,b,c){var d=[],e=[];if(1==(Aa(c)?2:1))return e=[b,a],G(d,function(h){e.push(h)}),Pc(e.join(" "));
var f=[],g=[];G(c,function(h){g.push(h.key);f.push(h.value)});
c=Math.floor((new Date).getTime()/1E3);e=0==f.length?[c,b,a]:[f.join(":"),c,b,a];G(d,function(h){e.push(h)});
a=Pc(e.join(" "));a=[c,a];0==g.length||a.push(g.join(""));return a.join("_")}
function Pc(a){var b=Nc();b.update(a);return b.ya().toLowerCase()}
;function Qc(a){var b=Mc(String(x.location.href)),c;(c=x.__SAPISID||x.__APISID||x.__OVERRIDE_SID)?c=!0:(c=new nc(document),c=c.get("SAPISID")||c.get("APISID")||c.get("__Secure-3PAPISID")||c.get("SID"),c=!!c);if(c&&(c=(b=0==b.indexOf("https:")||0==b.indexOf("chrome-extension:"))?x.__SAPISID:x.__APISID,c||(c=new nc(document),c=c.get(b?"SAPISID":"APISID")||c.get("__Secure-3PAPISID")),c)){b=b?"SAPISIDHASH":"APISIDHASH";var d=String(x.location.href);return d&&c&&b?[b,Oc(Mc(d),c,a||null)].join(" "):null}return null}
;function Rc(){this.g=[];this.f=-1}
Rc.prototype.set=function(a,b){b=void 0===b?!0:b;0<=a&&52>a&&0===a%1&&this.g[a]!=b&&(this.g[a]=b,this.f=-1)};
Rc.prototype.get=function(a){return!!this.g[a]};
function Sc(a){-1==a.f&&(a.f=Qa(a.g,function(b,c,d){return c?b+Math.pow(2,d):b},0));
return a.f}
;function Tc(a,b){this.h=a;this.i=b;this.g=0;this.f=null}
Tc.prototype.get=function(){if(0<this.g){this.g--;var a=this.f;this.f=a.next;a.next=null}else a=this.h();return a};
function Uc(a,b){a.i(b);100>a.g&&(a.g++,b.next=a.f,a.f=b)}
;function Wc(a){x.setTimeout(function(){throw a;},0)}
var Xc;
function Yc(){var a=x.MessageChannel;"undefined"===typeof a&&"undefined"!==typeof window&&window.postMessage&&window.addEventListener&&!I("Presto")&&(a=function(){var e=wc(document,"IFRAME");e.style.display="none";e.src=ob(new lb(mb,jb())).toString();document.documentElement.appendChild(e);var f=e.contentWindow;e=f.document;e.open();e.write(Ib(Lb));e.close();var g="callImmediate"+Math.random(),h="file:"==f.location.protocol?"*":f.location.protocol+"//"+f.location.host;e=C(function(k){if(("*"==h||
k.origin==h)&&k.data==g)this.port1.onmessage()},this);
f.addEventListener("message",e,!1);this.port1={};this.port2={postMessage:function(){f.postMessage(g,h)}}});
if("undefined"!==typeof a&&!I("Trident")&&!I("MSIE")){var b=new a,c={},d=c;b.port1.onmessage=function(){if(void 0!==c.next){c=c.next;var e=c.la;c.la=null;e()}};
return function(e){d.next={la:e};d=d.next;b.port2.postMessage(0)}}return function(e){x.setTimeout(e,0)}}
;function Zc(){this.g=this.f=null}
var ad=new Tc(function(){return new $c},function(a){a.reset()});
Zc.prototype.add=function(a,b){var c=ad.get();c.set(a,b);this.g?this.g.next=c:this.f=c;this.g=c};
Zc.prototype.remove=function(){var a=null;this.f&&(a=this.f,this.f=this.f.next,this.f||(this.g=null),a.next=null);return a};
function $c(){this.next=this.scope=this.f=null}
$c.prototype.set=function(a,b){this.f=a;this.scope=b;this.next=null};
$c.prototype.reset=function(){this.next=this.scope=this.f=null};function bd(a,b){cd||dd();ed||(cd(),ed=!0);fd.add(a,b)}
var cd;function dd(){if(x.Promise&&x.Promise.resolve){var a=x.Promise.resolve(void 0);cd=function(){a.then(gd)}}else cd=function(){var b=gd;
!A(x.setImmediate)||x.Window&&x.Window.prototype&&!I("Edge")&&x.Window.prototype.setImmediate==x.setImmediate?(Xc||(Xc=Yc()),Xc(b)):x.setImmediate(b)}}
var ed=!1,fd=new Zc;function gd(){for(var a;a=fd.remove();){try{a.f.call(a.scope)}catch(b){Wc(b)}Uc(ad,a)}ed=!1}
;function hd(){this.g=-1}
;function id(){this.g=64;this.f=[];this.l=[];this.m=[];this.i=[];this.i[0]=128;for(var a=1;a<this.g;++a)this.i[a]=0;this.j=this.h=0;this.reset()}
E(id,hd);id.prototype.reset=function(){this.f[0]=1732584193;this.f[1]=4023233417;this.f[2]=2562383102;this.f[3]=271733878;this.f[4]=3285377520;this.j=this.h=0};
function jd(a,b,c){c||(c=0);var d=a.m;if("string"===typeof b)for(var e=0;16>e;e++)d[e]=b.charCodeAt(c)<<24|b.charCodeAt(c+1)<<16|b.charCodeAt(c+2)<<8|b.charCodeAt(c+3),c+=4;else for(e=0;16>e;e++)d[e]=b[c]<<24|b[c+1]<<16|b[c+2]<<8|b[c+3],c+=4;for(e=16;80>e;e++){var f=d[e-3]^d[e-8]^d[e-14]^d[e-16];d[e]=(f<<1|f>>>31)&4294967295}b=a.f[0];c=a.f[1];var g=a.f[2],h=a.f[3],k=a.f[4];for(e=0;80>e;e++){if(40>e)if(20>e){f=h^c&(g^h);var l=1518500249}else f=c^g^h,l=1859775393;else 60>e?(f=c&g|h&(c|g),l=2400959708):
(f=c^g^h,l=3395469782);f=(b<<5|b>>>27)+f+k+l+d[e]&4294967295;k=h;h=g;g=(c<<30|c>>>2)&4294967295;c=b;b=f}a.f[0]=a.f[0]+b&4294967295;a.f[1]=a.f[1]+c&4294967295;a.f[2]=a.f[2]+g&4294967295;a.f[3]=a.f[3]+h&4294967295;a.f[4]=a.f[4]+k&4294967295}
id.prototype.update=function(a,b){if(null!=a){void 0===b&&(b=a.length);for(var c=b-this.g,d=0,e=this.l,f=this.h;d<b;){if(0==f)for(;d<=c;)jd(this,a,d),d+=this.g;if("string"===typeof a)for(;d<b;){if(e[f]=a.charCodeAt(d),++f,++d,f==this.g){jd(this,e);f=0;break}}else for(;d<b;)if(e[f]=a[d],++f,++d,f==this.g){jd(this,e);f=0;break}}this.h=f;this.j+=b}};
id.prototype.digest=function(){var a=[],b=8*this.j;56>this.h?this.update(this.i,56-this.h):this.update(this.i,this.g-(this.h-56));for(var c=this.g-1;56<=c;c--)this.l[c]=b&255,b/=256;jd(this,this.l);for(c=b=0;5>c;c++)for(var d=24;0<=d;d-=8)a[b]=this.f[c]>>d&255,++b;return a};function kd(a){return"string"==typeof a.className?a.className:a.getAttribute&&a.getAttribute("class")||""}
function ld(a,b){"string"==typeof a.className?a.className=b:a.setAttribute&&a.setAttribute("class",b)}
function md(a,b){if(a.classList)var c=a.classList.contains(b);else c=a.classList?a.classList:kd(a).match(/\S+/g)||[],c=0<=Na(c,b);return c}
function nd(){var a=document.body;a.classList?a.classList.remove("inverted-hdpi"):md(a,"inverted-hdpi")&&ld(a,Oa(a.classList?a.classList:kd(a).match(/\S+/g)||[],function(b){return"inverted-hdpi"!=b}).join(" "))}
;var od="StopIteration"in x?x.StopIteration:{message:"StopIteration",stack:""};function pd(){}
pd.prototype.next=function(){throw od;};
pd.prototype.G=function(){return this};
function qd(a){if(a instanceof pd)return a;if("function"==typeof a.G)return a.G(!1);if(Ba(a)){var b=0,c=new pd;c.next=function(){for(;;){if(b>=a.length)throw od;if(b in a)return a[b++];b++}};
return c}throw Error("Not implemented");}
function rd(a,b){if(Ba(a))try{G(a,b,void 0)}catch(c){if(c!==od)throw c;}else{a=qd(a);try{for(;;)b.call(void 0,a.next(),void 0,a)}catch(c){if(c!==od)throw c;}}}
function sd(a){if(Ba(a))return Ta(a);a=qd(a);var b=[];rd(a,function(c){b.push(c)});
return b}
;function td(a,b){this.h={};this.f=[];this.H=this.g=0;var c=arguments.length;if(1<c){if(c%2)throw Error("Uneven number of arguments");for(var d=0;d<c;d+=2)this.set(arguments[d],arguments[d+1])}else if(a)if(a instanceof td)for(c=ud(a),d=0;d<c.length;d++)this.set(c[d],a.get(c[d]));else for(d in a)this.set(d,a[d])}
function ud(a){vd(a);return a.f.concat()}
n=td.prototype;n.equals=function(a,b){if(this===a)return!0;if(this.g!=a.g)return!1;var c=b||wd;vd(this);for(var d,e=0;d=this.f[e];e++)if(!c(this.get(d),a.get(d)))return!1;return!0};
function wd(a,b){return a===b}
n.isEmpty=function(){return 0==this.g};
n.clear=function(){this.h={};this.H=this.g=this.f.length=0};
n.remove=function(a){return Object.prototype.hasOwnProperty.call(this.h,a)?(delete this.h[a],this.g--,this.H++,this.f.length>2*this.g&&vd(this),!0):!1};
function vd(a){if(a.g!=a.f.length){for(var b=0,c=0;b<a.f.length;){var d=a.f[b];Object.prototype.hasOwnProperty.call(a.h,d)&&(a.f[c++]=d);b++}a.f.length=c}if(a.g!=a.f.length){var e={};for(c=b=0;b<a.f.length;)d=a.f[b],Object.prototype.hasOwnProperty.call(e,d)||(a.f[c++]=d,e[d]=1),b++;a.f.length=c}}
n.get=function(a,b){return Object.prototype.hasOwnProperty.call(this.h,a)?this.h[a]:b};
n.set=function(a,b){Object.prototype.hasOwnProperty.call(this.h,a)||(this.g++,this.f.push(a),this.H++);this.h[a]=b};
n.forEach=function(a,b){for(var c=ud(this),d=0;d<c.length;d++){var e=c[d],f=this.get(e);a.call(b,f,e,this)}};
n.clone=function(){return new td(this)};
n.G=function(a){vd(this);var b=0,c=this.H,d=this,e=new pd;e.next=function(){if(c!=d.H)throw Error("The map has changed since the iterator was created");if(b>=d.f.length)throw od;var f=d.f[b++];return a?f:d.h[f]};
return e};function xd(a){var b=[];yd(new zd,a,b);return b.join("")}
function zd(){}
function yd(a,b,c){if(null==b)c.push("null");else{if("object"==typeof b){if(Array.isArray(b)){var d=b;b=d.length;c.push("[");for(var e="",f=0;f<b;f++)c.push(e),yd(a,d[f],c),e=",";c.push("]");return}if(b instanceof String||b instanceof Number||b instanceof Boolean)b=b.valueOf();else{c.push("{");e="";for(d in b)Object.prototype.hasOwnProperty.call(b,d)&&(f=b[d],"function"!=typeof f&&(c.push(e),Ad(d,c),c.push(":"),yd(a,f,c),e=","));c.push("}");return}}switch(typeof b){case "string":Ad(b,c);break;case "number":c.push(isFinite(b)&&
!isNaN(b)?String(b):"null");break;case "boolean":c.push(String(b));break;case "function":c.push("null");break;default:throw Error("Unknown type: "+typeof b);}}}
var Bd={'"':'\\"',"\\":"\\\\","/":"\\/","\b":"\\b","\f":"\\f","\n":"\\n","\r":"\\r","\t":"\\t","\x0B":"\\u000b"},Cd=/\uffff/.test("\uffff")?/[\\"\x00-\x1f\x7f-\uffff]/g:/[\\"\x00-\x1f\x7f-\xff]/g;function Ad(a,b){b.push('"',a.replace(Cd,function(c){var d=Bd[c];d||(d="\\u"+(c.charCodeAt(0)|65536).toString(16).substr(1),Bd[c]=d);return d}),'"')}
;function Dd(a){if(!a)return!1;try{return!!a.$goog_Thenable}catch(b){return!1}}
;function O(a){this.f=0;this.m=void 0;this.i=this.g=this.h=null;this.j=this.l=!1;if(a!=xa)try{var b=this;a.call(void 0,function(c){Ed(b,2,c)},function(c){Ed(b,3,c)})}catch(c){Ed(this,3,c)}}
function Fd(){this.next=this.context=this.onRejected=this.g=this.f=null;this.h=!1}
Fd.prototype.reset=function(){this.context=this.onRejected=this.g=this.f=null;this.h=!1};
var Gd=new Tc(function(){return new Fd},function(a){a.reset()});
function Hd(a,b,c){var d=Gd.get();d.g=a;d.onRejected=b;d.context=c;return d}
function Id(a){return new O(function(b,c){c(a)})}
O.prototype.then=function(a,b,c){return Jd(this,A(a)?a:null,A(b)?b:null,c)};
O.prototype.$goog_Thenable=!0;function Kd(a,b){return Jd(a,null,b,void 0)}
O.prototype.cancel=function(a){if(0==this.f){var b=new Ld(a);bd(function(){Md(this,b)},this)}};
function Md(a,b){if(0==a.f)if(a.h){var c=a.h;if(c.g){for(var d=0,e=null,f=null,g=c.g;g&&(g.h||(d++,g.f==a&&(e=g),!(e&&1<d)));g=g.next)e||(f=g);e&&(0==c.f&&1==d?Md(c,b):(f?(d=f,d.next==c.i&&(c.i=d),d.next=d.next.next):Nd(c),Od(c,e,3,b)))}a.h=null}else Ed(a,3,b)}
function Pd(a,b){a.g||2!=a.f&&3!=a.f||Qd(a);a.i?a.i.next=b:a.g=b;a.i=b}
function Jd(a,b,c,d){var e=Hd(null,null,null);e.f=new O(function(f,g){e.g=b?function(h){try{var k=b.call(d,h);f(k)}catch(l){g(l)}}:f;
e.onRejected=c?function(h){try{var k=c.call(d,h);void 0===k&&h instanceof Ld?g(h):f(k)}catch(l){g(l)}}:g});
e.f.h=a;Pd(a,e);return e.f}
O.prototype.w=function(a){this.f=0;Ed(this,2,a)};
O.prototype.A=function(a){this.f=0;Ed(this,3,a)};
function Ed(a,b,c){if(0==a.f){a===c&&(b=3,c=new TypeError("Promise cannot resolve to itself"));a.f=1;a:{var d=c,e=a.w,f=a.A;if(d instanceof O){Pd(d,Hd(e||xa,f||null,a));var g=!0}else if(Dd(d))d.then(e,f,a),g=!0;else{if(Ca(d))try{var h=d.then;if(A(h)){Rd(d,h,e,f,a);g=!0;break a}}catch(k){f.call(a,k);g=!0;break a}g=!1}}g||(a.m=c,a.f=b,a.h=null,Qd(a),3!=b||c instanceof Ld||Sd(a,c))}}
function Rd(a,b,c,d,e){function f(k){h||(h=!0,d.call(e,k))}
function g(k){h||(h=!0,c.call(e,k))}
var h=!1;try{b.call(a,g,f)}catch(k){f(k)}}
function Qd(a){a.l||(a.l=!0,bd(a.B,a))}
function Nd(a){var b=null;a.g&&(b=a.g,a.g=b.next,b.next=null);a.g||(a.i=null);return b}
O.prototype.B=function(){for(var a;a=Nd(this);)Od(this,a,this.f,this.m);this.l=!1};
function Od(a,b,c,d){if(3==c&&b.onRejected&&!b.h)for(;a&&a.j;a=a.h)a.j=!1;if(b.f)b.f.h=null,Td(b,c,d);else try{b.h?b.g.call(b.context):Td(b,c,d)}catch(e){Ud.call(null,e)}Uc(Gd,b)}
function Td(a,b,c){2==b?a.g.call(a.context,c):a.onRejected&&a.onRejected.call(a.context,c)}
function Sd(a,b){a.j=!0;bd(function(){a.j&&Ud.call(null,b)})}
var Ud=Wc;function Ld(a){F.call(this,a)}
E(Ld,F);Ld.prototype.name="cancel";function P(a){N.call(this);this.l=1;this.i=[];this.j=0;this.f=[];this.h={};this.m=!!a}
E(P,N);n=P.prototype;n.subscribe=function(a,b,c){var d=this.h[a];d||(d=this.h[a]=[]);var e=this.l;this.f[e]=a;this.f[e+1]=b;this.f[e+2]=c;this.l=e+3;d.push(e);return e};
function Vd(a,b,c,d){if(b=a.h[b]){var e=a.f;(b=Ra(b,function(f){return e[f+1]==c&&e[f+2]==d}))&&a.N(b)}}
n.N=function(a){var b=this.f[a];if(b){var c=this.h[b];0!=this.j?(this.i.push(a),this.f[a+1]=xa):(c&&Sa(c,a),delete this.f[a],delete this.f[a+1],delete this.f[a+2])}return!!b};
n.M=function(a,b){var c=this.h[a];if(c){for(var d=Array(arguments.length-1),e=1,f=arguments.length;e<f;e++)d[e-1]=arguments[e];if(this.m)for(e=0;e<c.length;e++){var g=c[e];Wd(this.f[g+1],this.f[g+2],d)}else{this.j++;try{for(e=0,f=c.length;e<f;e++)g=c[e],this.f[g+1].apply(this.f[g+2],d)}finally{if(this.j--,0<this.i.length&&0==this.j)for(;c=this.i.pop();)this.N(c)}}return 0!=e}return!1};
function Wd(a,b,c){bd(function(){a.apply(b,c)})}
n.clear=function(a){if(a){var b=this.h[a];b&&(G(b,this.N,this),delete this.h[a])}else this.f.length=0,this.h={}};
n.o=function(){P.L.o.call(this);this.clear();this.i.length=0};function Xd(a){this.f=a}
Xd.prototype.set=function(a,b){void 0===b?this.f.remove(a):this.f.set(a,xd(b))};
Xd.prototype.get=function(a){try{var b=this.f.get(a)}catch(c){return}if(null!==b)try{return JSON.parse(b)}catch(c){throw"Storage: Invalid value was encountered";}};
Xd.prototype.remove=function(a){this.f.remove(a)};function Yd(a){this.f=a}
E(Yd,Xd);function Zd(a){this.data=a}
function $d(a){return void 0===a||a instanceof Zd?a:new Zd(a)}
Yd.prototype.set=function(a,b){Yd.L.set.call(this,a,$d(b))};
Yd.prototype.g=function(a){a=Yd.L.get.call(this,a);if(void 0===a||a instanceof Object)return a;throw"Storage: Invalid value was encountered";};
Yd.prototype.get=function(a){if(a=this.g(a)){if(a=a.data,void 0===a)throw"Storage: Invalid value was encountered";}else a=void 0;return a};function ae(a){this.f=a}
E(ae,Yd);ae.prototype.set=function(a,b,c){if(b=$d(b)){if(c){if(c<D()){ae.prototype.remove.call(this,a);return}b.expiration=c}b.creation=D()}ae.L.set.call(this,a,b)};
ae.prototype.g=function(a){var b=ae.L.g.call(this,a);if(b){var c=b.creation,d=b.expiration;if(d&&d<D()||c&&c>D())ae.prototype.remove.call(this,a);else return b}};function be(){}
;function ce(){}
E(ce,be);ce.prototype.clear=function(){var a=sd(this.G(!0)),b=this;G(a,function(c){b.remove(c)})};function de(a){this.f=a}
E(de,ce);n=de.prototype;n.isAvailable=function(){if(!this.f)return!1;try{return this.f.setItem("__sak","1"),this.f.removeItem("__sak"),!0}catch(a){return!1}};
n.set=function(a,b){try{this.f.setItem(a,b)}catch(c){if(0==this.f.length)throw"Storage mechanism: Storage disabled";throw"Storage mechanism: Quota exceeded";}};
n.get=function(a){a=this.f.getItem(a);if("string"!==typeof a&&null!==a)throw"Storage mechanism: Invalid value was encountered";return a};
n.remove=function(a){this.f.removeItem(a)};
n.G=function(a){var b=0,c=this.f,d=new pd;d.next=function(){if(b>=c.length)throw od;var e=c.key(b++);if(a)return e;e=c.getItem(e);if("string"!==typeof e)throw"Storage mechanism: Invalid value was encountered";return e};
return d};
n.clear=function(){this.f.clear()};
n.key=function(a){return this.f.key(a)};function ee(){var a=null;try{a=window.localStorage||null}catch(b){}this.f=a}
E(ee,de);function fe(a,b){this.g=a;this.f=null;if(Xb&&!(9<=Number(ic))){ge||(ge=new td);this.f=ge.get(a);this.f||(b?this.f=document.getElementById(b):(this.f=document.createElement("userdata"),this.f.addBehavior("#default#userData"),document.body.appendChild(this.f)),ge.set(a,this.f));try{this.f.load(this.g)}catch(c){this.f=null}}}
E(fe,ce);var he={".":".2E","!":".21","~":".7E","*":".2A","'":".27","(":".28",")":".29","%":"."},ge=null;function ie(a){return"_"+encodeURIComponent(a).replace(/[.!~*'()%]/g,function(b){return he[b]})}
n=fe.prototype;n.isAvailable=function(){return!!this.f};
n.set=function(a,b){this.f.setAttribute(ie(a),b);je(this)};
n.get=function(a){a=this.f.getAttribute(ie(a));if("string"!==typeof a&&null!==a)throw"Storage mechanism: Invalid value was encountered";return a};
n.remove=function(a){this.f.removeAttribute(ie(a));je(this)};
n.G=function(a){var b=0,c=this.f.XMLDocument.documentElement.attributes,d=new pd;d.next=function(){if(b>=c.length)throw od;var e=c[b++];if(a)return decodeURIComponent(e.nodeName.replace(/\./g,"%")).substr(1);e=e.nodeValue;if("string"!==typeof e)throw"Storage mechanism: Invalid value was encountered";return e};
return d};
n.clear=function(){for(var a=this.f.XMLDocument.documentElement,b=a.attributes.length;0<b;b--)a.removeAttribute(a.attributes[b-1].nodeName);je(this)};
function je(a){try{a.f.save(a.g)}catch(b){throw"Storage mechanism: Quota exceeded";}}
;function ke(a,b){this.g=a;this.f=b+"::"}
E(ke,ce);ke.prototype.set=function(a,b){this.g.set(this.f+a,b)};
ke.prototype.get=function(a){return this.g.get(this.f+a)};
ke.prototype.remove=function(a){this.g.remove(this.f+a)};
ke.prototype.G=function(a){var b=this.g.G(!0),c=this,d=new pd;d.next=function(){for(var e=b.next();e.substr(0,c.f.length)!=c.f;)e=b.next();return a?e.substr(c.f.length):c.g.get(e)};
return d};function le(a,b){1<b.length?a[b[0]]=b[1]:1===b.length&&Object.assign(a,b[0])}
;var me=window.yt&&window.yt.config_||window.ytcfg&&window.ytcfg.data_||{};y("yt.config_",me,void 0);function Q(a){le(me,arguments)}
function R(a,b){return a in me?me[a]:b}
function ne(){return R("PLAYER_CONFIG",{})}
function oe(a){var b=me.EXPERIMENT_FLAGS;return b?b[a]:void 0}
;function pe(){var a=qe;z("yt.ads.biscotti.getId_")||y("yt.ads.biscotti.getId_",a,void 0)}
function re(a){y("yt.ads.biscotti.lastId_",a,void 0)}
;var se=[];function te(a){se.forEach(function(b){return b(a)})}
function ue(a){return a&&window.yterr?function(){try{return a.apply(this,arguments)}catch(b){ve(b),te(b)}}:a}
function ve(a){var b=z("yt.logging.errors.log");b?b(a,"ERROR",void 0,void 0,void 0):(b=R("ERRORS",[]),b.push([a,"ERROR",void 0,void 0,void 0]),Q("ERRORS",b))}
function we(a){var b=z("yt.logging.errors.log");b?b(a,"WARNING",void 0,void 0,void 0):(b=R("ERRORS",[]),b.push([a,"WARNING",void 0,void 0,void 0]),Q("ERRORS",b))}
;function xe(a){a=a.split("&");for(var b={},c=0,d=a.length;c<d;c++){var e=a[c].split("=");if(1==e.length&&e[0]||2==e.length)try{var f=decodeURIComponent((e[0]||"").replace(/\+/g," ")),g=decodeURIComponent((e[1]||"").replace(/\+/g," "));f in b?Aa(b[f])?Ua(b[f],g):b[f]=[b[f],g]:b[f]=g}catch(k){if("q"!=e[0]){var h=Error("Error decoding URL component");h.params={key:e[0],value:e[1]};ve(h)}}}return b}
function ye(a){var b=[];Wa(a,function(c,d){var e=encodeURIComponent(String(d)),f;Aa(c)?f=c:f=[c];G(f,function(g){""==g?b.push(e):b.push(e+"="+encodeURIComponent(String(g)))})});
return b.join("&")}
function ze(a){"?"==a.charAt(0)&&(a=a.substr(1));return xe(a)}
function Ae(a,b){return Be(a,b||{},!0)}
function Be(a,b,c){var d=a.split("#",2);a=d[0];d=1<d.length?"#"+d[1]:"";var e=a.split("?",2);a=e[0];e=ze(e[1]||"");for(var f in b)!c&&null!==e&&f in e||(e[f]=b[f]);return Tb(a,e)+d}
;function Ce(a){var b=De;a=void 0===a?z("yt.ads.biscotti.lastId_")||"":a;b=Object.assign(Ee(b),Fe(b));b.ca_type="image";a&&(b.bid=a);return b}
function Ee(a){var b={};b.dt=Lc;b.flash="0";a:{try{var c=a.f.top.location.href}catch(f){a=2;break a}a=c?c===a.g.location.href?0:1:2}b=(b.frm=a,b);b.u_tz=-(new Date).getTimezoneOffset();var d=void 0===d?M:d;try{var e=d.history.length}catch(f){e=0}b.u_his=e;b.u_java=!!M.navigator&&"unknown"!==typeof M.navigator.javaEnabled&&!!M.navigator.javaEnabled&&M.navigator.javaEnabled();M.screen&&(b.u_h=M.screen.height,b.u_w=M.screen.width,b.u_ah=M.screen.availHeight,b.u_aw=M.screen.availWidth,b.u_cd=M.screen.colorDepth);
M.navigator&&M.navigator.plugins&&(b.u_nplug=M.navigator.plugins.length);M.navigator&&M.navigator.mimeTypes&&(b.u_nmime=M.navigator.mimeTypes.length);return b}
function Fe(a){var b=a.f;try{var c=b.screenX;var d=b.screenY}catch(q){}try{var e=b.outerWidth;var f=b.outerHeight}catch(q){}try{var g=b.innerWidth;var h=b.innerHeight}catch(q){}b=[b.screenLeft,b.screenTop,c,d,b.screen?b.screen.availWidth:void 0,b.screen?b.screen.availTop:void 0,e,f,g,h];c=a.f.top;try{var k=(c||window).document,l="CSS1Compat"==k.compatMode?k.documentElement:k.body;var m=(new rc(l.clientWidth,l.clientHeight)).round()}catch(q){m=new rc(-12245933,-12245933)}k=m;m={};l=new Rc;x.SVGElement&&
x.document.createElementNS&&l.set(0);c=Gc();c["allow-top-navigation-by-user-activation"]&&l.set(1);c["allow-popups-to-escape-sandbox"]&&l.set(2);x.crypto&&x.crypto.subtle&&l.set(3);x.TextDecoder&&x.TextEncoder&&l.set(4);l=Sc(l);m.bc=l;m.bih=k.height;m.biw=k.width;m.brdim=b.join();a=a.g;return m.vis={visible:1,hidden:2,prerender:3,preview:4,unloaded:5}[a.visibilityState||a.webkitVisibilityState||a.mozVisibilityState||""]||0,m.wgl=!!M.WebGLRenderingContext,m}
var De=new function(){var a=window.document;this.f=window;this.g=a};
y("yt.ads_.signals_.getAdSignalsString",function(a){return ye(Ce(a))},void 0);D();function S(a){a=Ge(a);return"string"===typeof a&&"false"===a?!1:!!a}
function He(a,b){var c=Ge(a);return void 0===c&&void 0!==b?b:Number(c||0)}
function Ge(a){var b=R("EXPERIMENTS_FORCED_FLAGS",{});return void 0!==b[a]?b[a]:R("EXPERIMENT_FLAGS",{})[a]}
;var Ie=void 0!==XMLHttpRequest?function(){return new XMLHttpRequest}:void 0!==ActiveXObject?function(){return new ActiveXObject("Microsoft.XMLHTTP")}:null;
function Je(){if(!Ie)return null;var a=Ie();return"open"in a?a:null}
function Ke(a){switch(a&&"status"in a?a.status:-1){case 200:case 201:case 202:case 203:case 204:case 205:case 206:case 304:return!0;default:return!1}}
;function T(a,b){A(a)&&(a=ue(a));return window.setTimeout(a,b)}
function U(a){window.clearTimeout(a)}
;var Le={Authorization:"AUTHORIZATION","X-Goog-Visitor-Id":"SANDBOXED_VISITOR_ID","X-YouTube-Client-Name":"INNERTUBE_CONTEXT_CLIENT_NAME","X-YouTube-Client-Version":"INNERTUBE_CONTEXT_CLIENT_VERSION","X-YouTube-Device":"DEVICE","X-Youtube-Identity-Token":"ID_TOKEN","X-YouTube-Page-CL":"PAGE_CL","X-YouTube-Page-Label":"PAGE_BUILD_LABEL","X-YouTube-Variants-Checksum":"VARIANTS_CHECKSUM"},Me="app debugcss debugjs expflag force_ad_params force_viral_ad_response_params forced_experiments innertube_snapshots innertube_goldens internalcountrycode internalipoverride absolute_experiments conditional_experiments sbb sr_bns_address".split(" "),
Ne=!1;
function Oe(a,b){b=void 0===b?{}:b;if(!c)var c=window.location.href;var d=L(1,a),e=J(L(3,a));d&&e?(d=c,c=a.match(Qb),d=d.match(Qb),c=c[3]==d[3]&&c[1]==d[1]&&c[4]==d[4]):c=e?J(L(3,c))==e&&(Number(L(4,c))||null)==(Number(L(4,a))||null):!0;d=S("web_ajax_ignore_global_headers_if_set");for(var f in Le)e=R(Le[f]),!e||!c&&!Pe(a,f)||d&&void 0!==b[f]||(b[f]=e);if(c||Pe(a,"X-YouTube-Utc-Offset"))b["X-YouTube-Utc-Offset"]=String(-(new Date).getTimezoneOffset());(c||Pe(a,"X-YouTube-Time-Zone"))&&(f="undefined"!=typeof Intl?
(new Intl.DateTimeFormat).resolvedOptions().timeZone:null)&&(b["X-YouTube-Time-Zone"]=f);if(c||Pe(a,"X-YouTube-Ad-Signals"))b["X-YouTube-Ad-Signals"]=ye(Ce(void 0));return b}
function Qe(a){var b=window.location.search,c=J(L(3,a)),d=J(L(5,a));d=(c=c&&(c.endsWith("youtube.com")||c.endsWith("youtube-nocookie.com")))&&d&&d.startsWith("/api/");if(!c||d)return a;var e=ze(b),f={};G(Me,function(g){e[g]&&(f[g]=e[g])});
return Be(a,f||{},!1)}
function Pe(a,b){var c=R("CORS_HEADER_WHITELIST")||{},d=J(L(3,a));return d?(c=c[d])?0<=Na(c,b):!1:!0}
function Re(a,b){if(window.fetch&&"XML"!=b.format){var c={method:b.method||"GET",credentials:"same-origin"};b.headers&&(c.headers=b.headers);a=Se(a,b);var d=Te(a,b);d&&(c.body=d);b.withCredentials&&(c.credentials="include");var e=!1,f;fetch(a,c).then(function(g){if(!e){e=!0;f&&U(f);var h=g.ok,k=function(l){l=l||{};var m=b.context||x;h?b.onSuccess&&b.onSuccess.call(m,l,g):b.onError&&b.onError.call(m,l,g);b.ja&&b.ja.call(m,l,g)};
"JSON"==(b.format||"JSON")&&(h||400<=g.status&&500>g.status)?g.json().then(k,function(){k(null)}):k(null)}});
b.qa&&0<b.timeout&&(f=T(function(){e||(e=!0,U(f),b.qa.call(b.context||x))},b.timeout))}else Ue(a,b)}
function Ue(a,b){var c=b.format||"JSON";a=Se(a,b);var d=Te(a,b),e=!1,f,g=Ve(a,function(h){if(!e){e=!0;f&&U(f);var k=Ke(h),l=null,m=400<=h.status&&500>h.status,q=500<=h.status&&600>h.status;if(k||m||q)l=We(c,h,b.rb);if(k)a:if(h&&204==h.status)k=!0;else{switch(c){case "XML":k=0==parseInt(l&&l.return_code,10);break a;case "RAW":k=!0;break a}k=!!l}l=l||{};m=b.context||x;k?b.onSuccess&&b.onSuccess.call(m,h,l):b.onError&&b.onError.call(m,h,l);b.ja&&b.ja.call(m,h,l)}},b.method,d,b.headers,b.responseType,
b.withCredentials);
b.O&&0<b.timeout&&(f=T(function(){e||(e=!0,g.abort(),U(f),b.O.call(b.context||x,g))},b.timeout));
return g}
function Se(a,b){b.ub&&(a=document.location.protocol+"//"+document.location.hostname+(document.location.port?":"+document.location.port:"")+a);var c=R("XSRF_FIELD_NAME",void 0),d=b.ab;d&&(d[c]&&delete d[c],a=Ae(a,d));return a}
function Te(a,b){var c=R("XSRF_FIELD_NAME",void 0),d=R("XSRF_TOKEN",void 0),e=b.postBody||"",f=b.C,g=R("XSRF_FIELD_NAME",void 0),h;b.headers&&(h=b.headers["Content-Type"]);b.tb||J(L(3,a))&&!b.withCredentials&&J(L(3,a))!=document.location.hostname||"POST"!=b.method||h&&"application/x-www-form-urlencoded"!=h||b.C&&b.C[g]||(f||(f={}),f[c]=d);f&&"string"===typeof e&&(e=ze(e),fb(e,f),e=b.ra&&"JSON"==b.ra?JSON.stringify(e):Sb(e));f=e||f&&!$a(f);!Ne&&f&&"POST"!=b.method&&(Ne=!0,ve(Error("AJAX request with postData should use POST")));
return e}
function We(a,b,c){var d=null;switch(a){case "JSON":a=b.responseText;b=b.getResponseHeader("Content-Type")||"";a&&0<=b.indexOf("json")&&(d=JSON.parse(a));break;case "XML":if(b=(b=b.responseXML)?Xe(b):null)d={},G(b.getElementsByTagName("*"),function(e){d[e.tagName]=Ye(e)})}c&&Ze(d);
return d}
function Ze(a){if(Ca(a))for(var b in a){var c;(c="html_content"==b)||(c=b.length-5,c=0<=c&&b.indexOf("_html",c)==c);if(c){c=b;var d=Jb(a[b],null);a[c]=d}else Ze(a[b])}}
function Xe(a){return a?(a=("responseXML"in a?a.responseXML:a).getElementsByTagName("root"))&&0<a.length?a[0]:null:null}
function Ye(a){var b="";G(a.childNodes,function(c){b+=c.nodeValue});
return b}
function Ve(a,b,c,d,e,f,g){function h(){4==(k&&"readyState"in k?k.readyState:0)&&b&&ue(b)(k)}
c=void 0===c?"GET":c;d=void 0===d?"":d;var k=Je();if(!k)return null;"onloadend"in k?k.addEventListener("loadend",h,!1):k.onreadystatechange=h;S("debug_forward_web_query_parameters")&&(a=Qe(a));k.open(c,a,!0);f&&(k.responseType=f);g&&(k.withCredentials=!0);c="POST"==c&&(void 0===window.FormData||!(d instanceof FormData));if(e=Oe(a,e))for(var l in e)k.setRequestHeader(l,e[l]),"content-type"==l.toLowerCase()&&(c=!1);c&&k.setRequestHeader("Content-Type","application/x-www-form-urlencoded");k.send(d);
return k}
;var $e={},af=0;
function bf(a,b,c,d,e){e=void 0===e?"":e;a&&(c&&(c=Db,c=!(c&&0<=c.toLowerCase().indexOf("cobalt"))),c?a&&(a instanceof H||(a="object"==typeof a&&a.J?a.I():String(a),Bb.test(a)||(a="about:invalid#zClosurez"),a=new H(yb,a)),b=Ab(a),"about:invalid#zClosurez"===b?a="":(b instanceof Gb?a=b:(d="object"==typeof b,a=null,d&&b.ha&&(a=b.ea()),b=qb(d&&b.J?b.I():String(b)),a=Jb(b,a)),a=Ib(a).toString(),a=encodeURIComponent(String(xd(a)))),/^[\s\xa0]*$/.test(a)||(a=vc("IFRAME",{src:'javascript:"<body><img src=\\""+'+a+
'+"\\"></body>"',style:"display:none"}),(9==a.nodeType?a:a.ownerDocument||a.document).body.appendChild(a))):e?Ve(a,b,"POST",e,d):R("USE_NET_AJAX_FOR_PING_TRANSPORT",!1)||d?Ve(a,b,"GET","",d):cf(a,b)||df(a,b))}
function cf(a,b){if(!oe("web_use_beacon_api_for_ad_click_server_pings"))return!1;if(oe("use_sonic_js_library_for_v4_support")){a:{try{var c=new Ma({url:a,bb:!0});if(c.j?c.h&&c.f&&c.f[1]||c.i:c.g){var d=J(L(5,a));var e=!(!d||!d.endsWith("/aclk")||"1"!==Vb(a,"ri"));break a}}catch(f){}e=!1}if(!e)return!1}else if(e=J(L(5,a)),!e||-1==e.indexOf("/aclk")||"1"!==Vb(a,"ae")||"1"!==Vb(a,"act"))return!1;return ef(a)?(b&&b(),!0):!1}
function ef(a,b){try{if(window.navigator&&window.navigator.sendBeacon&&window.navigator.sendBeacon(a,void 0===b?"":b))return!0}catch(c){}return!1}
function df(a,b){var c=new Image,d=""+af++;$e[d]=c;c.onload=c.onerror=function(){b&&$e[d]&&b();delete $e[d]};
c.src=a}
;var ff=z("ytPubsubPubsubInstance")||new P;P.prototype.subscribe=P.prototype.subscribe;P.prototype.unsubscribeByKey=P.prototype.N;P.prototype.publish=P.prototype.M;P.prototype.clear=P.prototype.clear;y("ytPubsubPubsubInstance",ff,void 0);var gf=z("ytPubsubPubsubSubscribedKeys")||{};y("ytPubsubPubsubSubscribedKeys",gf,void 0);var hf=z("ytPubsubPubsubTopicToKeys")||{};y("ytPubsubPubsubTopicToKeys",hf,void 0);var jf=z("ytPubsubPubsubIsSynchronous")||{};y("ytPubsubPubsubIsSynchronous",jf,void 0);
function kf(a,b){var c=lf();if(c){var d=c.subscribe(a,function(){var e=arguments;var f=function(){gf[d]&&b.apply&&"function"==typeof b.apply&&b.apply(window,e)};
try{jf[a]?f():T(f,0)}catch(g){ve(g)}},void 0);
gf[d]=!0;hf[a]||(hf[a]=[]);hf[a].push(d);return d}return 0}
function mf(a){var b=lf();b&&("number"===typeof a?a=[a]:"string"===typeof a&&(a=[parseInt(a,10)]),G(a,function(c){b.unsubscribeByKey(c);delete gf[c]}))}
function nf(a,b){var c=lf();c&&c.publish.apply(c,arguments)}
function of(a){var b=lf();if(b)if(b.clear(a),a)pf(a);else for(var c in hf)pf(c)}
function lf(){return z("ytPubsubPubsubInstance")}
function pf(a){hf[a]&&(a=hf[a],G(a,function(b){gf[b]&&delete gf[b]}),a.length=0)}
;var qf=window,V=qf.ytcsi&&qf.ytcsi.now?qf.ytcsi.now:qf.performance&&qf.performance.timing&&qf.performance.now&&qf.performance.timing.navigationStart?function(){return qf.performance.timing.navigationStart+qf.performance.now()}:function(){return(new Date).getTime()};var rf=He("initial_gel_batch_timeout",1E3),sf=Math.pow(2,16)-1,tf=null,uf=0,vf=void 0,wf=0,xf=0,yf=0,zf=!0,Af=z("ytLoggingTransportLogPayloadsQueue_")||{};y("ytLoggingTransportLogPayloadsQueue_",Af,void 0);var Bf=z("ytLoggingTransportGELQueue_")||new Map;y("ytLoggingTransportGELQueue_",Bf,void 0);var Cf=z("ytLoggingTransportTokensToCttTargetIds_")||{};y("ytLoggingTransportTokensToCttTargetIds_",Cf,void 0);
function Df(){U(wf);U(xf);xf=0;vf&&vf.isReady()?(Ef(Bf),"log_event"in Af&&Ef(Object.entries(Af.log_event)),Bf.clear(),delete Af.log_event):Ff()}
function Ff(){S("web_gel_timeout_cap")&&!xf&&(xf=T(Df,6E4));U(wf);var a=R("LOGGING_BATCH_TIMEOUT",He("web_gel_debounce_ms",1E4));S("shorten_initial_gel_batch_timeout")&&zf&&(a=rf);wf=T(Df,a)}
function Ef(a){var b=vf,c=Math.round(V());a=p(a);for(var d=a.next();!d.done;d=a.next()){var e=p(d.value);d=e.next().value;var f=e.next().value;e=db({context:Gf(b.f||Hf())});e.events=f;(f=Cf[d])&&If(e,d,f);delete Cf[d];Jf(e,c);Kf(b,"log_event",e,{retry:!0,onSuccess:function(){uf=Math.round(V()-c)}});
zf=!1}}
function Jf(a,b){a.requestTimeMs=String(b);S("unsplit_gel_payloads_in_logs")&&(a.unsplitGelPayloadsInLogs=!0);var c=R("EVENT_ID",void 0);if(c){var d=R("BATCH_CLIENT_COUNTER",void 0)||0;!d&&S("web_client_counter_random_seed")&&(d=Math.floor(Math.random()*sf/2));d++;d>sf&&(d=1);Q("BATCH_CLIENT_COUNTER",d);c={serializedEventId:c,clientCounter:String(d)};a.serializedClientEventId=c;tf&&uf&&S("log_gel_rtt_web")&&(a.previousBatchInfo={serializedClientEventId:tf,roundtripMs:String(uf)});tf=c;uf=0}}
function If(a,b,c){if(c.videoId)var d="VIDEO";else if(c.playlistId)d="PLAYLIST";else return;a.credentialTransferTokenTargetId=c;a.context=a.context||{};a.context.user=a.context.user||{};a.context.user.credentialTransferTokens=[{token:b,scope:d}]}
;var Lf=He("initial_gel_batch_timeout",1E3),Mf=Math.pow(2,16)-1,Nf=null,Of=0,Pf={log_event:"events",log_interaction:"interactions"},Qf=new Set(["log_event"]),Rf={},Sf=0,Tf=0,Uf=0,Vf=!0,W=z("ytLoggingTransportLogPayloadsQueue_")||{};y("ytLoggingTransportLogPayloadsQueue_",W,void 0);var Wf=z("ytLoggingTransportTokensToCttTargetIds_")||{};y("ytLoggingTransportTokensToCttTargetIds_",Wf,void 0);
function Xf(){if(S("use_typescript_transport"))Df();else if(U(Sf),U(Tf),Tf=0,!$a(W)){for(var a in W){var b=Rf[a];if(b&&b.isReady()){var c=void 0,d=a,e=Pf[d],f=Math.round(V());for(c in W[d]){var g=db({context:Gf(b.f||Hf())});g[e]=Yf(d,c);var h=Wf[c];if(h)a:{var k=g,l=c;if(h.videoId)var m="VIDEO";else if(h.playlistId)m="PLAYLIST";else break a;k.credentialTransferTokenTargetId=h;k.context=k.context||{};k.context.user=k.context.user||{};k.context.user.credentialTransferTokens=[{token:l,scope:m}]}delete Wf[c];
h=g;h.requestTimeMs=f;S("unsplit_gel_payloads_in_logs")&&(h.unsplitGelPayloadsInLogs=!0);if(m=R("EVENT_ID",void 0))k=R("BATCH_CLIENT_COUNTER",void 0)||0,!k&&S("web_client_counter_random_seed")&&(k=Math.floor(Math.random()*Mf/2)),k++,k>Mf&&(k=1),Q("BATCH_CLIENT_COUNTER",k),m={serializedEventId:m,clientCounter:k},h.serializedClientEventId=m,Nf&&Of&&S("log_gel_rtt_web")&&(h.previousBatchInfo={serializedClientEventId:Nf,roundtripMs:Of}),Nf=m,Of=0;Kf(b,d,g,{retry:Qf.has(d),onSuccess:Ja(Zf,V())})}delete W[a];
Vf=!1}}$a(W)||$f()}}
function $f(){S("web_gel_timeout_cap")&&!Tf&&(Tf=T(Xf,6E4));U(Sf);var a=R("LOGGING_BATCH_TIMEOUT",He("web_gel_debounce_ms",1E4));S("shorten_initial_gel_batch_timeout")&&Vf&&(a=Lf);Sf=T(Xf,a)}
function Yf(a,b){b=void 0===b?"":b;W[a]=W[a]||{};W[a][b]=W[a][b]||[];return W[a][b]}
function Zf(a){Of=Math.round(V()-a)}
;var ag=0;y("ytDomDomGetNextId",z("ytDomDomGetNextId")||function(){return++ag},void 0);var bg={stopImmediatePropagation:1,stopPropagation:1,preventMouseEvent:1,preventManipulation:1,preventDefault:1,layerX:1,layerY:1,screenX:1,screenY:1,scale:1,rotation:1,webkitMovementX:1,webkitMovementY:1};
function cg(a){this.type="";this.state=this.source=this.data=this.currentTarget=this.relatedTarget=this.target=null;this.charCode=this.keyCode=0;this.metaKey=this.shiftKey=this.ctrlKey=this.altKey=!1;this.clientY=this.clientX=0;this.changedTouches=this.touches=null;try{if(a=a||window.event){this.event=a;for(var b in a)b in bg||(this[b]=a[b]);var c=a.target||a.srcElement;c&&3==c.nodeType&&(c=c.parentNode);this.target=c;var d=a.relatedTarget;if(d)try{d=d.nodeName?d:null}catch(e){d=null}else"mouseover"==
this.type?d=a.fromElement:"mouseout"==this.type&&(d=a.toElement);this.relatedTarget=d;this.clientX=void 0!=a.clientX?a.clientX:a.pageX;this.clientY=void 0!=a.clientY?a.clientY:a.pageY;this.keyCode=a.keyCode?a.keyCode:a.which;this.charCode=a.charCode||("keypress"==this.type?this.keyCode:0);this.altKey=a.altKey;this.ctrlKey=a.ctrlKey;this.shiftKey=a.shiftKey;this.metaKey=a.metaKey;this.f=a.pageX;this.g=a.pageY}}catch(e){}}
function dg(a){if(document.body&&document.documentElement){var b=document.body.scrollTop+document.documentElement.scrollTop;a.f=a.clientX+(document.body.scrollLeft+document.documentElement.scrollLeft);a.g=a.clientY+b}}
cg.prototype.preventDefault=function(){this.event&&(this.event.returnValue=!1,this.event.preventDefault&&this.event.preventDefault())};
cg.prototype.stopPropagation=function(){this.event&&(this.event.cancelBubble=!0,this.event.stopPropagation&&this.event.stopPropagation())};
cg.prototype.stopImmediatePropagation=function(){this.event&&(this.event.cancelBubble=!0,this.event.stopImmediatePropagation&&this.event.stopImmediatePropagation())};var Za=z("ytEventsEventsListeners")||{};y("ytEventsEventsListeners",Za,void 0);var eg=z("ytEventsEventsCounter")||{count:0};y("ytEventsEventsCounter",eg,void 0);
function fg(a,b,c,d){d=void 0===d?{}:d;a.addEventListener&&("mouseenter"!=b||"onmouseenter"in document?"mouseleave"!=b||"onmouseenter"in document?"mousewheel"==b&&"MozBoxSizing"in document.documentElement.style&&(b="MozMousePixelScroll"):b="mouseout":b="mouseover");return Ya(function(e){var f="boolean"===typeof e[4]&&e[4]==!!d,g=Ca(e[4])&&Ca(d)&&bb(e[4],d);return!!e.length&&e[0]==a&&e[1]==b&&e[2]==c&&(f||g)})}
var gg=Va(function(){var a=!1;try{var b=Object.defineProperty({},"capture",{get:function(){a=!0}});
window.addEventListener("test",null,b)}catch(c){}return a});
function X(a,b,c,d){d=void 0===d?{}:d;if(!a||!a.addEventListener&&!a.attachEvent)return"";var e=fg(a,b,c,d);if(e)return e;e=++eg.count+"";var f=!("mouseenter"!=b&&"mouseleave"!=b||!a.addEventListener||"onmouseenter"in document);var g=f?function(h){h=new cg(h);if(!zc(h.relatedTarget,function(k){return k==a}))return h.currentTarget=a,h.type=b,c.call(a,h)}:function(h){h=new cg(h);
h.currentTarget=a;return c.call(a,h)};
g=ue(g);a.addEventListener?("mouseenter"==b&&f?b="mouseover":"mouseleave"==b&&f?b="mouseout":"mousewheel"==b&&"MozBoxSizing"in document.documentElement.style&&(b="MozMousePixelScroll"),gg()||"boolean"===typeof d?a.addEventListener(b,g,d):a.addEventListener(b,g,!!d.capture)):a.attachEvent("on"+b,g);Za[e]=[a,b,c,g,d];return e}
function hg(a){a&&("string"==typeof a&&(a=[a]),G(a,function(b){if(b in Za){var c=Za[b],d=c[0],e=c[1],f=c[3];c=c[4];d.removeEventListener?gg()||"boolean"===typeof c?d.removeEventListener(e,f,c):d.removeEventListener(e,f,!!c.capture):d.detachEvent&&d.detachEvent("on"+e,f);delete Za[b]}}))}
;var ig=window.ytcsi&&window.ytcsi.now?window.ytcsi.now:window.performance&&window.performance.timing&&window.performance.now&&window.performance.timing.navigationStart?function(){return window.performance.timing.navigationStart+window.performance.now()}:function(){return(new Date).getTime()};function jg(a){this.w=a;this.f=null;this.j=0;this.m=null;this.l=0;this.h=[];for(a=0;4>a;a++)this.h.push(0);this.i=0;this.D=X(window,"mousemove",C(this.F,this));a=C(this.A,this);A(a)&&(a=ue(a));this.K=window.setInterval(a,25)}
E(jg,N);jg.prototype.F=function(a){void 0===a.f&&dg(a);var b=a.f;void 0===a.g&&dg(a);this.f=new qc(b,a.g)};
jg.prototype.A=function(){if(this.f){var a=ig();if(0!=this.j){var b=this.m,c=this.f,d=b.x-c.x;b=b.y-c.y;d=Math.sqrt(d*d+b*b)/(a-this.j);this.h[this.i]=.5<Math.abs((d-this.l)/this.l)?1:0;for(c=b=0;4>c;c++)b+=this.h[c]||0;3<=b&&this.w();this.l=d}this.j=a;this.m=this.f;this.i=(this.i+1)%4}};
jg.prototype.o=function(){window.clearInterval(this.K);hg(this.D)};function kg(){}
function lg(a,b){return mg(a,1,b)}
;function ng(){}
t(ng,kg);function mg(a,b,c){isNaN(c)&&(c=void 0);var d=z("yt.scheduler.instance.addJob");return d?d(a,b,c):void 0===c?(a(),NaN):T(a,c||0)}
ng.prototype.start=function(){var a=z("yt.scheduler.instance.start");a&&a()};
ng.prototype.pause=function(){var a=z("yt.scheduler.instance.pause");a&&a()};
ya(ng);ng.getInstance();var og={};
function pg(a){var b=void 0===a?{}:a;a=void 0===b.Ea?!0:b.Ea;b=void 0===b.Pa?!1:b.Pa;if(null==z("_lact",window)){var c=parseInt(R("LACT"),10);c=isFinite(c)?D()-Math.max(c,0):-1;y("_lact",c,window);y("_fact",c,window);-1==c&&qg();X(document,"keydown",qg);X(document,"keyup",qg);X(document,"mousedown",qg);X(document,"mouseup",qg);a&&(b?X(window,"touchmove",function(){rg("touchmove",200)},{passive:!0}):(X(window,"resize",function(){rg("resize",200)}),X(window,"scroll",function(){rg("scroll",200)})));
new jg(function(){rg("mouse",100)});
X(document,"touchstart",qg,{passive:!0});X(document,"touchend",qg,{passive:!0})}}
function rg(a,b){og[a]||(og[a]=!0,lg(function(){qg();og[a]=!1},b))}
function qg(){null==z("_lact",window)&&pg();var a=D();y("_lact",a,window);-1==z("_fact",window)&&y("_fact",a,window);(a=z("ytglobal.ytUtilActivityCallback_"))&&a()}
function sg(){var a=z("_lact",window);return null==a?-1:Math.max(D()-a,0)}
;var tg=z("ytLoggingGelSequenceIdObj_")||{};y("ytLoggingGelSequenceIdObj_",tg,void 0);
function ug(a,b,c,d){d=void 0===d?{}:d;var e={};e.eventTimeMs=Math.round(d.timestamp||V());e[a]=b;e.context={lastActivityMs:String(d.timestamp?-1:sg())};S("log_sequence_info_on_gel_web")&&d.P&&(a=e.context,b=d.P,tg[b]=b in tg?tg[b]+1:0,a.sequence={index:tg[b],groupKey:b},d.sb&&delete tg[d.P]);d=d.da;S("use_typescript_transport")?(a="",d&&(a={},d.videoId?a.videoId=d.videoId:d.playlistId&&(a.playlistId=d.playlistId),Cf[d.token]=a,a=d.token),d=Bf.get(a)||[],Bf.set(a,d),d.push(e),c&&(vf=new c),c=He("web_logging_max_batch")||
100,e=V(),d.length>=c?Df():10<=e-yf&&(Ff(),yf=e)):(d?(a={},d.videoId?a.videoId=d.videoId:d.playlistId&&(a.playlistId=d.playlistId),Wf[d.token]=a,d=Yf("log_event",d.token)):d=Yf("log_event"),d.push(e),c&&(Rf.log_event=new c),c=He("web_logging_max_batch")||100,e=V(),d.length>=c?Xf():10<=e-Uf&&($f(),Uf=e))}
;function vg(){for(var a={},b=p(Object.entries(ze(R("DEVICE","")))),c=b.next();!c.done;c=b.next()){var d=p(c.value);c=d.next().value;d=d.next().value;"cbrand"===c?a.deviceMake=d:"cmodel"===c?a.deviceModel=d:"cbr"===c?a.browserName=d:"cbrver"===c?a.browserVersion=d:"cos"===c?a.osName=d:"cosver"===c?a.osVersion=d:"cplatform"===c&&(a.platform=d)}return a}
;function wg(){return"INNERTUBE_API_KEY"in me&&"INNERTUBE_API_VERSION"in me}
function Hf(){return{innertubeApiKey:R("INNERTUBE_API_KEY",void 0),innertubeApiVersion:R("INNERTUBE_API_VERSION",void 0),Fa:R("INNERTUBE_CONTEXT_CLIENT_CONFIG_INFO"),Ga:R("INNERTUBE_CONTEXT_CLIENT_NAME","WEB"),innertubeContextClientVersion:R("INNERTUBE_CONTEXT_CLIENT_VERSION",void 0),Ia:R("INNERTUBE_CONTEXT_HL",void 0),Ha:R("INNERTUBE_CONTEXT_GL",void 0),Ja:R("INNERTUBE_HOST_OVERRIDE",void 0)||"",Ka:!!R("INNERTUBE_USE_THIRD_PARTY_AUTH",!1)}}
function Gf(a){a={client:{hl:a.Ia,gl:a.Ha,clientName:a.Ga,clientVersion:a.innertubeContextClientVersion,configInfo:a.Fa}};var b=window.devicePixelRatio;b&&1!=b&&(a.client.screenDensityFloat=String(b));b=R("EXPERIMENTS_TOKEN","");""!==b&&(a.client.experimentsToken=b);b=[];var c=R("EXPERIMENTS_FORCED_FLAGS",{});for(d in c)b.push({key:d,value:String(c[d])});var d=R("EXPERIMENT_FLAGS",{});for(var e in d)e.startsWith("force_")&&void 0===c[e]&&b.push({key:e,value:String(d[e])});0<b.length&&(a.request={internalExperimentFlags:b});
R("DELEGATED_SESSION_ID")&&!S("pageid_as_header_web")&&(a.user={onBehalfOfUser:R("DELEGATED_SESSION_ID")});S("enable_device_forwarding_from_xhr_client")&&(a.client=Object.assign(a.client,vg()));return a}
function xg(a,b,c){c=void 0===c?{}:c;var d={"X-Goog-Visitor-Id":c.visitorData||R("VISITOR_DATA","")};if(b&&b.includes("www.youtube-nocookie.com"))return d;(b=c.pb||R("AUTHORIZATION"))||(a?b="Bearer "+z("gapi.auth.getToken")().ob:b=Qc([]));b&&(d.Authorization=b,d["X-Goog-AuthUser"]=R("SESSION_INDEX",0),S("pageid_as_header_web")&&(d["X-Goog-PageId"]=R("DELEGATED_SESSION_ID")));return d}
function yg(a){a=Object.assign({},a);delete a.Authorization;var b=Qc();if(b){var c=new id;c.update(R("INNERTUBE_API_KEY",void 0));c.update(b);b=c.digest();c=3;Ba(b);void 0===c&&(c=0);if(!kc){kc={};for(var d="ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789".split(""),e=["+/=","+/","-_=","-_.","-_"],f=0;5>f;f++){var g=d.concat(e[f].split(""));jc[f]=g;for(var h=0;h<g.length;h++){var k=g[h];void 0===kc[k]&&(kc[k]=h)}}}c=jc[c];d=[];for(e=0;e<b.length;e+=3){var l=b[e],m=(f=e+1<b.length)?
b[e+1]:0;k=(g=e+2<b.length)?b[e+2]:0;h=l>>2;l=(l&3)<<4|m>>4;m=(m&15)<<2|k>>6;k&=63;g||(k=64,f||(m=64));d.push(c[h],c[l],c[m]||"",c[k]||"")}a.hash=d.join("")}return a}
;function zg(a,b,c,d){oc.set(""+a,b,{ma:c,path:"/",domain:void 0===d?"youtube.com":d,secure:!1})}
;function Ag(){var a=new ee;(a=a.isAvailable()?new ke(a,"yt.innertube"):null)||(a=new fe("yt.innertube"),a=a.isAvailable()?a:null);this.f=a?new ae(a):null;this.g=document.domain||window.location.hostname}
Ag.prototype.set=function(a,b,c,d){c=c||31104E3;this.remove(a);if(this.f)try{this.f.set(a,b,D()+1E3*c);return}catch(f){}var e="";if(d)try{e=escape(xd(b))}catch(f){return}else e=escape(b);zg(a,e,c,this.g)};
Ag.prototype.get=function(a,b){var c=void 0,d=!this.f;if(!d)try{c=this.f.get(a)}catch(e){d=!0}if(d&&(c=oc.get(""+a,void 0))&&(c=unescape(c),b))try{c=JSON.parse(c)}catch(e){this.remove(a),c=void 0}return c};
Ag.prototype.remove=function(a){this.f&&this.f.remove(a);var b=this.g;oc.remove(""+a,"/",void 0===b?"youtube.com":b)};var Bg=new Ag;function Cg(a,b,c,d){if(d)return null;d=Bg.get("nextId",!0)||1;var e=Bg.get("requests",!0)||{};e[d]={method:a,request:b,authState:yg(c),requestTime:Math.round(V())};Bg.set("nextId",d+1,86400,!0);Bg.set("requests",e,86400,!0);return d}
function Dg(a){var b=Bg.get("requests",!0)||{};delete b[a];Bg.set("requests",b,86400,!0)}
function Eg(a){var b=Bg.get("requests",!0);if(b){for(var c in b){var d=b[c];if(!(6E4>Math.round(V())-d.requestTime)){var e=d.authState,f=yg(xg(!1));bb(e,f)&&(e=d.request,"requestTimeMs"in e&&(e.requestTimeMs=Math.round(V())),Kf(a,d.method,e,{}));delete b[c]}}Bg.set("requests",b,86400,!0)}}
;function Fg(a){var b=this;this.f=null;a?this.f=a:wg()&&(this.f=Hf());mg(function(){Eg(b)},0,5E3)}
Fg.prototype.isReady=function(){!this.f&&wg()&&(this.f=Hf());return!!this.f};
function Kf(a,b,c,d){!R("VISITOR_DATA")&&"visitor_id"!==b&&.01>Math.random()&&we(Error("Missing VISITOR_DATA when sending innertube request."));var e={headers:{"Content-Type":"application/json"},method:"POST",C:c,ra:"JSON",O:function(){d.O()},
qa:d.O,onSuccess:function(v,r){if(d.onSuccess)d.onSuccess(r)},
oa:function(v){if(d.onSuccess)d.onSuccess(v)},
onError:function(v,r){if(d.onError)d.onError(r)},
wb:function(v){if(d.onError)d.onError(v)},
timeout:d.timeout,withCredentials:!0},f="",g=a.f.Ja;g&&(f=g);g=a.f.Ka||!1;var h=xg(g,f,d);Object.assign(e.headers,h);e.headers.Authorization&&!f&&(e.headers["x-origin"]=window.location.origin);var k=Ae(""+f+("/youtubei/"+a.f.innertubeApiVersion+"/"+b),{alt:"json",key:a.f.innertubeApiKey}),l;if(d.retry&&S("retry_web_logging_batches")&&"www.youtube-nocookie.com"!=f&&(l=Cg(b,c,h,g))){var m=e.onSuccess,q=e.oa;e.onSuccess=function(v,r){Dg(l);m(v,r)};
c.oa=function(v,r){Dg(l);q(v,r)}}try{S("use_fetch_for_op_xhr")?Re(k,e):(e.method="POST",e.C||(e.C={}),Ue(k,e))}catch(v){if("InvalidAccessError"==v)l&&(Dg(l),l=0),we(Error("An extension is blocking network request."));
else throw v;}l&&mg(function(){Eg(a)},0,5E3)}
;function Gg(a,b,c){c=void 0===c?{}:c;var d=Fg;R("ytLoggingEventsDefaultDisabled",!1)&&Fg==Fg&&(d=null);ug(a,b,d,c)}
;function Hg(a,b){for(var c=[],d=1;d<arguments.length;++d)c[d-1]=arguments[d];d=Error.call(this,a);this.message=d.message;"stack"in d&&(this.stack=d.stack);this.args=[].concat(c instanceof Array?c:ka(p(c)))}
t(Hg,Error);var Ig=new Set,Jg=0;function Kg(a){Lg(a,"WARNING")}
function Lg(a,b,c,d,e){e=void 0===e?{}:e;e.name=c||R("INNERTUBE_CONTEXT_CLIENT_NAME",1);e.version=d||R("INNERTUBE_CONTEXT_CLIENT_VERSION",void 0);c=e||{};b=void 0===b?"ERROR":b;b=void 0===b?"ERROR":b;d=window&&window.yterr||!1;if(a&&d&&!(5<=Jg)&&(S("console_log_js_exceptions")&&(d=[],d.push("Name: "+a.name),d.push("Message: "+a.message),a.hasOwnProperty("params")&&d.push("Error Params: "+JSON.stringify(a.params)),d.push("File name: "+a.fileName),d.push("Stacktrace: "+a.stack),window.console.log(d.join("\n"),
a)),0!==a.f)){d=a.g;e=a.columnNumber;if(a.args&&a.args.length)for(var f=0,g=0;g<a.args.length;g++){var h=a.args[g],k="params."+g;f+=k.length;if(h)if(Array.isArray(h))for(var l=c,m=0;m<h.length&&!(h[m]&&(f+=Mg(m,h[m],k,l),500<f));m++);else if("object"===typeof h)for(l in l=void 0,m=c,h){if(h[l]&&(f+=Mg(l,h[l],k,m),500<f))break}else c[k]=String(JSON.stringify(h)).substring(0,500),f+=c[k].length;else c[k]=String(JSON.stringify(h)).substring(0,500),f+=c[k].length;if(500<=f)break}else if(a.hasOwnProperty("params"))if(h=
a.params,"object"===typeof a.params)for(g in k=0,h){if(h[g]&&(f="params."+g,l=String(JSON.stringify(h[g])).substr(0,500),c[f]=l,k+=f.length+l.length,500<k))break}else c.params=String(JSON.stringify(h)).substr(0,500);a=lc(a);(d=d||a.stack)||(d="Not available");h={stackTrace:d};a.fileName&&(h.filename=a.fileName);g=a.lineNumber.toString();isNaN(g)||!e||isNaN(e)||(h.lineNumber=Number(g),h.columnNumber=Number(e),g=g+":"+e);window.yterr&&A(window.yterr)&&(a.params=c,window.yterr(a));if(!(Ig.has(a.message)||
0<=d.indexOf("/YouTubeCenter.js")||0<=d.indexOf("/mytube.js"))){if(S("kevlar_gel_error_routing")){k=b;e={level:"ERROR_LEVEL_UNKNOWN",message:a.message};"ERROR"===k?e.level="ERROR_LEVEL_ERROR":"WARNING"===k&&(e.level="ERROR_LEVEL_WARNNING");h={isObfuscated:!0,browserStackInfo:h};k={pageUrl:window.location.href,kvPairs:[]};f=p(Object.keys(c));for(l=f.next();!l.done;l=f.next())l=l.value,k.kvPairs.push({key:"client."+l,value:String(c[l])});Gg("clientError",{errorMetadata:k,stackTrace:h,logMessage:e});
Xf()}b={ab:{a:"logerror",t:"jserror",type:a.name,msg:a.message.substr(0,250),line:g,level:b,"client.name":c.name},C:{url:R("PAGE_NAME",window.location.href),file:a.fileName},method:"POST"};c.version&&(b["client.version"]=c.version);if(b.C){d&&(b.C.stack=d);d=p(Object.keys(c));for(e=d.next();!e.done;e=d.next())e=e.value,b.C["client."+e]=c[e];if(c=R("LATEST_ECATCHER_SERVICE_TRACKING_PARAMS",void 0))for(d=p(Object.keys(c)),e=d.next();!e.done;e=d.next())e=e.value,b.C[e]=c[e]}Ue(R("ECATCHER_REPORT_HOST",
"")+"/error_204",b);Ig.add(a.message);Jg++}}}
function Mg(a,b,c,d){c+="."+a;a=String(JSON.stringify(b)).substr(0,500);d[c]=a;return c.length+a.length}
;function Ng(a,b,c,d,e,f){Lg(a,void 0===b?"ERROR":b,c,d,f)}
;var Og=window.yt&&window.yt.msgs_||window.ytcfg&&window.ytcfg.msgs||{};y("yt.msgs_",Og,void 0);function Pg(a){le(Og,arguments)}
;function Qg(a){a&&(a.dataset?a.dataset[Rg("loaded")]="true":a.setAttribute("data-loaded","true"))}
function Sg(a,b){return a?a.dataset?a.dataset[Rg(b)]:a.getAttribute("data-"+b):null}
var Tg={};function Rg(a){return Tg[a]||(Tg[a]=String(a).replace(/\-([a-z])/g,function(b,c){return c.toUpperCase()}))}
;var Ug=/\.vflset|-vfl[a-zA-Z0-9_+=-]+/,Vg=/-[a-zA-Z]{2,3}_[a-zA-Z]{2,3}(?=(\/|$))/;function Wg(a,b,c){c=void 0===c?null:c;if(window.spf&&spf.script){c="";if(a){var d=a.indexOf("jsbin/"),e=a.lastIndexOf(".js"),f=d+6;-1<d&&-1<e&&e>f&&(c=a.substring(f,e),c=c.replace(Ug,""),c=c.replace(Vg,""),c=c.replace("debug-",""),c=c.replace("tracing-",""))}spf.script.load(a,c,b)}else Xg(a,b,c)}
function Xg(a,b,c){c=void 0===c?null:c;var d=Yg(a),e=document.getElementById(d),f=e&&Sg(e,"loaded"),g=e&&!f;f?b&&b():(b&&(f=kf(d,b),b=""+Da(b),Zg[b]=f),g||(e=$g(a,d,function(){Sg(e,"loaded")||(Qg(e),nf(d),T(Ja(of,d),0))},c)))}
function $g(a,b,c,d){d=void 0===d?null:d;var e=wc(document,"SCRIPT");e.id=b;e.onload=function(){c&&setTimeout(c,0)};
e.onreadystatechange=function(){switch(e.readyState){case "loaded":case "complete":e.onload()}};
d&&e.setAttribute("nonce",d);Nb(e,Ac(a));a=document.getElementsByTagName("head")[0]||document.body;a.insertBefore(e,a.firstChild);return e}
function ah(a){a=Yg(a);var b=document.getElementById(a);b&&(of(a),b.parentNode.removeChild(b))}
function bh(a,b){if(a&&b){var c=""+Da(b);(c=Zg[c])&&mf(c)}}
function Yg(a){var b=document.createElement("a");Mb(b,a);a=b.href.replace(/^[a-zA-Z]+:\/\//,"//");return"js-"+Pb(a)}
var Zg={};var ch=[],dh=!1;function eh(){if("1"!=Xa(ne(),"args","privembed")){var a=function(){dh=!0;"google_ad_status"in window?Q("DCLKSTAT",1):Q("DCLKSTAT",2)};
Wg("//static.doubleclick.net/instream/ad_status.js",a);ch.push(lg(function(){dh||"google_ad_status"in window||(bh("//static.doubleclick.net/instream/ad_status.js",a),dh=!0,Q("DCLKSTAT",3))},5E3))}}
function fh(){return parseInt(R("DCLKSTAT",0),10)}
;function gh(){this.g=!1;this.f=null}
gh.prototype.initialize=function(a,b,c,d,e,f){var g=this;f=void 0===f?!1:f;b?(this.g=!0,Wg(b,function(){g.g=!1;window.botguard?hh(g,c,d,f):(ah(b),Kg(new Hg("Unable to load Botguard","from "+b)))},e)):a&&(eval(a),window.botguard?hh(this,c,d,f):Kg(Error("Unable to load Botguard from JS")))};
function hh(a,b,c,d){if(d)try{a.f=new window.botguard.bg(b,c?function(){return c(b)}:xa)}catch(e){Kg(e)}else{try{a.f=new window.botguard.bg(b)}catch(e){Kg(e)}c&&c(b)}}
gh.prototype.dispose=function(){this.f=null};var ih=new gh,jh=!1,kh=0,lh="";function mh(a){S("botguard_periodic_refresh")?kh=V():S("botguard_always_refresh")&&(lh=a)}
function nh(a){if(a){if(ih.g)return!1;if(S("botguard_periodic_refresh"))return 72E5<V()-kh;if(S("botguard_always_refresh"))return lh!=a}else return!1;return!jh}
function oh(){return!!ih.f}
function ph(a){a=void 0===a?{}:a;a=void 0===a?{}:a;return ih.f?ih.f.invoke(void 0,void 0,a):null}
;var qh=D().toString();
function rh(){a:{if(window.crypto&&window.crypto.getRandomValues)try{var a=Array(16),b=new Uint8Array(16);window.crypto.getRandomValues(b);for(var c=0;c<a.length;c++)a[c]=b[c];var d=a;break a}catch(e){}d=Array(16);for(a=0;16>a;a++){b=D();for(c=0;c<b%23;c++)d[a]=Math.random();d[a]=Math.floor(256*Math.random())}if(qh)for(a=1,b=0;b<qh.length;b++)d[a%16]=d[a%16]^d[(a-1)%16]/4^qh.charCodeAt(b),a++}a=[];for(b=0;b<d.length;b++)a.push("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-_".charAt(d[b]&63));
return a.join("")}
;var sh=z("ytLoggingDocDocumentNonce_")||rh();y("ytLoggingDocDocumentNonce_",sh,void 0);var th=1;function uh(a){this.f=a}
function vh(a){var b={};void 0!==a.f.trackingParams?b.trackingParams=a.f.trackingParams:(b.veType=a.f.veType,void 0!==a.f.veCounter&&(b.veCounter=a.f.veCounter),void 0!==a.f.elementIndex&&(b.elementIndex=a.f.elementIndex));void 0!==a.f.dataElement&&(b.dataElement=vh(a.f.dataElement));void 0!==a.f.youtubeData&&(b.youtubeData=a.f.youtubeData);return b}
uh.prototype.toString=function(){return JSON.stringify(vh(this))};function wh(a){a=void 0===a?0:a;return 0==a?"client-screen-nonce":"client-screen-nonce."+a}
function xh(a){a=void 0===a?0:a;return 0==a?"ROOT_VE_TYPE":"ROOT_VE_TYPE."+a}
function yh(a){return R(xh(void 0===a?0:a),void 0)}
y("yt_logging_screen.getRootVeType",yh,void 0);function zh(){var a=yh(0),b;a?b=new uh({veType:a,youtubeData:void 0}):b=null;return b}
function Ah(){var a=R("csn-to-ctt-auth-info");a||(a={},Q("csn-to-ctt-auth-info",a));return a}
function Bh(a){a=void 0===a?0:a;var b=R(wh(a));if(!b&&!R("USE_CSN_FALLBACK",!0))return null;b||0!=a||(S("kevlar_client_side_screens")||S("c3_client_side_screens")?b="UNDEFINED_CSN":b=R("EVENT_ID"));return b?b:null}
y("yt_logging_screen.getCurrentCsn",Bh,void 0);function Ch(a,b,c){var d=Ah();(c=Bh(c))&&delete d[c];b&&(d[a]=b)}
function Dh(a){return Ah()[a]}
y("yt_logging_screen.getCttAuthInfo",Dh,void 0);function Eh(a,b,c,d){c=void 0===c?0:c;if(a!==R(wh(c))||b!==R(xh(c)))if(Ch(a,d,c),Q(wh(c),a),Q(xh(c),b),0==c||S("web_screen_associated_all_layers"))b=function(){setTimeout(function(){a&&ug("foregroundHeartbeatScreenAssociated",{clientDocumentNonce:sh,clientScreenNonce:a},Fg)},0)},"requestAnimationFrame"in window?window.requestAnimationFrame(b):b()}
y("yt_logging_screen.setCurrentScreen",Eh,void 0);function Fh(a,b,c){b=void 0===b?{}:b;c=void 0===c?!1:c;var d=R("EVENT_ID");d&&(b.ei||(b.ei=d));if(b){d=a;var e=void 0===e?!0:e;var f=R("VALID_SESSION_TEMPDATA_DOMAINS",[]),g=J(L(3,window.location.href));g&&f.push(g);g=J(L(3,d));if(0<=Na(f,g)||!g&&0==d.lastIndexOf("/",0))if(S("autoescape_tempdata_url")&&(f=document.createElement("a"),Mb(f,d),d=f.href),d){g=d.match(Qb);d=g[5];f=g[6];g=g[7];var h="";d&&(h+=d);f&&(h+="?"+f);g&&(h+="#"+g);d=h;f=d.indexOf("#");if(d=0>f?d:d.substr(0,f))if(e&&!b.csn&&(b.itct||
b.ved)&&(b=Object.assign({csn:Bh()},b)),k){var k=parseInt(k,10);isFinite(k)&&0<k&&(e=b,b="ST-"+Pb(d).toString(36),e=e?Sb(e):"",zg(b,e,k||5))}else k=b,e="ST-"+Pb(d).toString(36),k=k?Sb(k):"",zg(e,k,5)}}if(c)return!1;if((window.ytspf||{}).enabled)spf.navigate(a);else{var l=void 0===l?{}:l;var m=void 0===m?"":m;var q=void 0===q?window:q;c=q.location;a=Tb(a,l)+m;a=a instanceof H?a:Cb(a);c.href=Ab(a)}return!0}
;function Gh(a,b){this.version=a;this.args=b}
;function Hh(a,b){this.topic=a;this.f=b}
Hh.prototype.toString=function(){return this.topic};var Ih=z("ytPubsub2Pubsub2Instance")||new P;P.prototype.subscribe=P.prototype.subscribe;P.prototype.unsubscribeByKey=P.prototype.N;P.prototype.publish=P.prototype.M;P.prototype.clear=P.prototype.clear;y("ytPubsub2Pubsub2Instance",Ih,void 0);var Jh=z("ytPubsub2Pubsub2SubscribedKeys")||{};y("ytPubsub2Pubsub2SubscribedKeys",Jh,void 0);var Kh=z("ytPubsub2Pubsub2TopicToKeys")||{};y("ytPubsub2Pubsub2TopicToKeys",Kh,void 0);var Lh=z("ytPubsub2Pubsub2IsAsync")||{};y("ytPubsub2Pubsub2IsAsync",Lh,void 0);
y("ytPubsub2Pubsub2SkipSubKey",null,void 0);function Mh(a,b){var c=Nh();c&&c.publish.call(c,a.toString(),a,b)}
function Oh(a){var b=Ph,c=Nh();if(!c)return 0;var d=c.subscribe(b.toString(),function(e,f){var g=z("ytPubsub2Pubsub2SkipSubKey");g&&g==d||(g=function(){if(Jh[d])try{if(f&&b instanceof Hh&&b!=e)try{var h=b.f,k=f;if(!k.args||!k.version)throw Error("yt.pubsub2.Data.deserialize(): serializedData is incomplete.");try{if(!h.H){var l=new h;h.H=l.version}var m=h.H}catch(q){}if(!m||k.version!=m)throw Error("yt.pubsub2.Data.deserialize(): serializedData version is incompatible.");try{f=Reflect.construct(h,
Ta(k.args))}catch(q){throw q.message="yt.pubsub2.Data.deserialize(): "+q.message,q;}}catch(q){throw q.message="yt.pubsub2.pubsub2 cross-binary conversion error for "+b.toString()+": "+q.message,q;}a.call(window,f)}catch(q){ve(q)}},Lh[b.toString()]?z("yt.scheduler.instance")?lg(g):T(g,0):g())});
Jh[d]=!0;Kh[b.toString()]||(Kh[b.toString()]=[]);Kh[b.toString()].push(d);return d}
function Qh(){var a=Rh,b=Oh(function(c){a.apply(void 0,arguments);Sh(b)});
return b}
function Sh(a){var b=Nh();b&&("number"===typeof a&&(a=[a]),G(a,function(c){b.unsubscribeByKey(c);delete Jh[c]}))}
function Nh(){return z("ytPubsub2Pubsub2Instance")}
;function Th(a){Gh.call(this,1,arguments);this.csn=a}
t(Th,Gh);var Ph=new Hh("screen-created",Th),Uh=[],Vh=0;function Wh(a,b,c){var d=S("use_default_events_client")?void 0:Fg;b={csn:a,parentVe:vh(b),childVes:Pa(c,function(f){return vh(f)})};
c=p(c);for(var e=c.next();!e.done;e=c.next())e=vh(e.value),($a(e)||!e.trackingParams&&!e.veType)&&Ng(Error("Child VE logged with no data"),"WARNING");c={da:Dh(a),P:a};"UNDEFINED_CSN"==a?Xh("visualElementAttached",b,c):d?ug("visualElementAttached",b,d,c):Gg("visualElementAttached",b,c)}
function Xh(a,b,c){Uh.push({payloadName:a,payload:b,options:c});Vh||(Vh=Qh())}
function Rh(a){if(Uh){for(var b=p(Uh),c=b.next();!c.done;c=b.next())c=c.value,c.payload&&(c.payload.csn=a.csn,ug(c.payloadName,c.payload,null,c.options));Uh.length=0}Vh=0}
;function Yh(a){a=a||{};var b={},c={};this.url=a.url||"";this.args=a.args||cb(b);this.assets=a.assets||{};this.attrs=a.attrs||cb(c);this.fallback=a.fallback||null;this.fallbackMessage=a.fallbackMessage||null;this.html5=!!a.html5;this.disable=a.disable||{};this.loaded=!!a.loaded;this.messages=a.messages||{}}
Yh.prototype.clone=function(){var a=new Yh,b;for(b in this)if(this.hasOwnProperty(b)){var c=this[b];"object"==za(c)?a[b]=cb(c):a[b]=c}return a};function Zh(){N.call(this);this.f=[]}
t(Zh,N);Zh.prototype.o=function(){for(;this.f.length;){var a=this.f.pop();a.target.removeEventListener(a.name,a.qb)}N.prototype.o.call(this)};var $h=/cssbin\/(?:debug-)?([a-zA-Z0-9_-]+?)(?:-2x|-web|-rtl|-vfl|.css)/;function ai(a){a=a||"";if(window.spf){var b=a.match($h);spf.style.load(a,b?b[1]:"",void 0)}else bi(a)}
function bi(a){var b=ci(a),c=document.getElementById(b),d=c&&Sg(c,"loaded");d||c&&!d||(c=di(a,b,function(){Sg(c,"loaded")||(Qg(c),nf(b),T(Ja(of,b),0))}))}
function di(a,b,c){var d=document.createElement("link");d.id=b;d.onload=function(){c&&setTimeout(c,0)};
a=Ac(a);d.rel="stylesheet";d.href=ob(a).toString();(document.getElementsByTagName("head")[0]||document.body).appendChild(d);return d}
function ci(a){var b=wc(document,"A");Mb(b,new H(yb,a));a=b.href.replace(/^[a-zA-Z]+:\/\//,"//");return"css-"+Pb(a)}
;function ei(a,b,c,d){N.call(this);var e=this;this.m=this.X=a;this.U=b;this.w=!1;this.api={};this.V=this.D=null;this.F=new P;Hc(this,Ja(Ic,this.F));this.j={};this.R=this.W=this.h=this.ca=this.f=null;this.K=!1;this.l=this.A=null;this.Y={};this.ua=["onReady"];this.ba=null;this.ka=NaN;this.S={};this.i=d;fi(this);this.Z("WATCH_LATER_VIDEO_ADDED",this.Ma.bind(this));this.Z("WATCH_LATER_VIDEO_REMOVED",this.Na.bind(this));this.Z("onAdAnnounce",this.wa.bind(this));this.va=new Zh(this);Hc(this,Ja(Ic,this.va));
this.T=0;c?this.T=T(function(){e.loadNewVideoConfig(c)},0):d&&(gi(this),hi(this))}
t(ei,N);n=ei.prototype;n.getId=function(){return this.U};
n.loadNewVideoConfig=function(a){if(!this.g){this.T&&(U(this.T),this.T=0);a instanceof Yh||(a=new Yh(a));this.ca=a;this.f=a.clone();gi(this);this.W||(this.W=ii(this,this.f.args.jsapicallback||"onYouTubePlayerReady"));this.f.args.jsapicallback=null;if(a=this.f.attrs.width)this.m.style.width=Kc(Number(a)||a);if(a=this.f.attrs.height)this.m.style.height=Kc(Number(a)||a);hi(this);this.w&&ji(this)}};
function gi(a){var b;a.i?b=a.i.rootElementId:b=a.f.attrs.id;a.h=b||a.h;"video-player"==a.h&&(a.h=a.U,a.f.attrs.id=a.U);a.m.id==a.h&&(a.h+="-player",a.f.attrs.id=a.h)}
n.Ba=function(){return this.ca};
function ji(a){a.f&&!a.f.loaded&&(a.f.loaded=!0,"0"!=a.f.args.autoplay?a.api.loadVideoByPlayerVars(a.f.args):a.api.cueVideoByPlayerVars(a.f.args))}
function ki(a){var b=!0,c=li(a);c&&a.f&&(a=mi(a),b=Sg(c,"version")===a);return b&&!!z("yt.player.Application.create")}
function hi(a){if(!a.g&&!a.K){var b=ki(a);if(b&&"html5"==(li(a)?"html5":null))a.R="html5",a.w||ni(a);else if(oi(a),a.R="html5",b&&a.l)a.X.appendChild(a.l),ni(a);else{a.f&&(a.f.loaded=!0);var c=!1;a.A=function(){c=!0;if(a.i)var d=a.i.serializedExperimentFlags;else a.f&&a.f.args&&(d=a.f.args.fflags);d="true"==xe(d||"").player_bootstrap_method?z("yt.player.Application.createAlternate")||z("yt.player.Application.create"):z("yt.player.Application.create");var e=a.f?a.f.clone():void 0;d(a.X,e,a.i);ni(a)};
a.K=!0;b?a.A():(Wg(mi(a),a.A),(b=a.i?a.i.cssUrl:a.f.assets.css)&&ai(b),pi(a)&&!c&&y("yt.player.Application.create",null,void 0))}}}
function li(a){var b=sc(a.h);!b&&a.m&&a.m.querySelector&&(b=a.m.querySelector("#"+a.h));return b}
function ni(a){if(!a.g){var b=li(a),c=!1;b&&b.getApiInterface&&b.getApiInterface()&&(c=!0);c?(a.K=!1,b.isNotServable&&a.f&&b.isNotServable(a.f.args.video_id)||qi(a)):a.ka=T(function(){ni(a)},50)}}
function qi(a){fi(a);a.w=!0;var b=li(a);b.addEventListener&&(a.D=ri(a,b,"addEventListener"));b.removeEventListener&&(a.V=ri(a,b,"removeEventListener"));var c=b.getApiInterface();c=c.concat(b.getInternalApiInterface());for(var d=0;d<c.length;d++){var e=c[d];a.api[e]||(a.api[e]=ri(a,b,e))}for(var f in a.j)a.D(f,a.j[f]);ji(a);a.W&&a.W(a.api);a.F.M("onReady",a.api)}
function ri(a,b,c){var d=b[c];return function(){try{return a.ba=null,d.apply(b,arguments)}catch(e){"sendAbandonmentPing"!=c&&(e.params=c,a.ba=e,we(e))}}}
function fi(a){a.w=!1;if(a.V)for(var b in a.j)a.V(b,a.j[b]);for(var c in a.S)U(parseInt(c,10));a.S={};a.D=null;a.V=null;for(var d in a.api)a.api[d]=null;a.api.addEventListener=a.Z.bind(a);a.api.removeEventListener=a.Ra.bind(a);a.api.destroy=a.dispose.bind(a);a.api.getLastError=a.Ca.bind(a);a.api.getPlayerType=a.Da.bind(a);a.api.getCurrentVideoConfig=a.Ba.bind(a);a.api.loadNewVideoConfig=a.loadNewVideoConfig.bind(a);a.api.isReady=a.La.bind(a)}
n.La=function(){return this.w};
n.Z=function(a,b){var c=this,d=ii(this,b);if(d){if(!(0<=Na(this.ua,a)||this.j[a])){var e=si(this,a);this.D&&this.D(a,e)}this.F.subscribe(a,d);"onReady"==a&&this.w&&T(function(){d(c.api)},0)}};
n.Ra=function(a,b){if(!this.g){var c=ii(this,b);c&&Vd(this.F,a,c)}};
function ii(a,b){var c=b;if("string"==typeof b){if(a.Y[b])return a.Y[b];c=function(){var d=z(b);d&&d.apply(x,arguments)};
a.Y[b]=c}return c?c:null}
function si(a,b){var c="ytPlayer"+b+a.U;a.j[b]=c;x[c]=function(d){var e=T(function(){if(!a.g){a.F.M(b,d);var f=a.S,g=String(e);g in f&&delete f[g]}},0);
ab(a.S,String(e))};
return c}
n.wa=function(a){nf("a11y-announce",a)};
n.Ma=function(a){nf("WATCH_LATER_VIDEO_ADDED",a)};
n.Na=function(a){nf("WATCH_LATER_VIDEO_REMOVED",a)};
n.Da=function(){return this.R||(li(this)?"html5":null)};
n.Ca=function(){return this.ba};
function oi(a){a.cancel();fi(a);a.R=null;a.f&&(a.f.loaded=!1);var b=li(a);b&&(ki(a)||!pi(a)?a.l=b:(b&&b.destroy&&b.destroy(),a.l=null));for(a=a.X;b=a.firstChild;)a.removeChild(b)}
n.cancel=function(){this.A&&bh(mi(this),this.A);U(this.ka);this.K=!1};
n.o=function(){oi(this);if(this.l&&this.f&&this.l.destroy)try{this.l.destroy()}catch(b){ve(b)}this.Y=null;for(var a in this.j)x[this.j[a]]=null;this.ca=this.f=this.api=null;delete this.X;delete this.m;N.prototype.o.call(this)};
function pi(a){return a.f&&a.f.args&&a.f.args.fflags?-1!=a.f.args.fflags.indexOf("player_destroy_old_version=true"):!1}
function mi(a){return a.i?a.i.jsUrl:a.f.assets.js}
;var ti={},ui="player_uid_"+(1E9*Math.random()>>>0);function vi(a){delete ti[a.getId()]}
;function wi(a,b){Gh.call(this,1,arguments)}
t(wi,Gh);function xi(a,b){Gh.call(this,1,arguments)}
t(xi,Gh);var yi=new Hh("aft-recorded",wi),zi=new Hh("timing-sent",xi);var Ai=window;function Bi(){this.timing={};this.clearResourceTimings=function(){};
this.webkitClearResourceTimings=function(){};
this.mozClearResourceTimings=function(){};
this.msClearResourceTimings=function(){};
this.oClearResourceTimings=function(){}}
var Ci=Ai.performance||Ai.mozPerformance||Ai.msPerformance||Ai.webkitPerformance||new Bi;var Di=!1;function Ei(a){var b=Fi(a);if(b.aft)return b.aft;a=R((a||"")+"TIMING_AFT_KEYS",["ol"]);for(var c=a.length,d=0;d<c;d++){var e=b[a[d]];if(e)return e}return NaN}
function Gi(a){var b;(b=z("ytcsi."+(a||"")+"data_"))||(b={tick:{},info:{}},Ka("ytcsi."+(a||"")+"data_",b));return b}
function Hi(a){a=Gi(a);a.info||(a.info={});return a.info}
function Fi(a){a=Gi(a);a.tick||(a.tick={});return a.tick}
function Ii(a){var b=Gi(a).nonce;b||(b=rh(),Gi(a).nonce=b);return b}
function Ji(a){var b=Fi(a||""),c=Ei(a);c&&!Di&&(Mh(yi,new wi(Math.round(c-b._start),a)),Di=!0)}
;function Ki(a,b){for(var c=[],d=1;d<arguments.length;++d)c[d-1]=arguments[d];if(!Li(a)||c.some(function(e){return!Li(e)}))throw Error("Only objects may be merged.");
c=p(c);for(d=c.next();!d.done;d=c.next())Mi(a,d.value);return a}
function Mi(a,b){for(var c in b)if(Li(b[c])){if(c in a&&!Li(a[c]))throw Error("Cannot merge an object into a non-object.");c in a||(a[c]={});Mi(a[c],b[c])}else if(Ni(b[c])){if(c in a&&!Ni(a[c]))throw Error("Cannot merge an array into a non-array.");c in a||(a[c]=[]);Oi(a[c],b[c])}else a[c]=b[c];return a}
function Oi(a,b){for(var c=p(b),d=c.next();!d.done;d=c.next())d=d.value,Li(d)?a.push(Mi({},d)):Ni(d)?a.push(Oi([],d)):a.push(d);return a}
function Li(a){return"object"===typeof a&&!Array.isArray(a)}
function Ni(a){return"object"===typeof a&&Array.isArray(a)}
;function Pi(){var a=z("ytcsi.debug");a||(a=[],y("ytcsi.debug",a,void 0),y("ytcsi.reference",{},void 0));return a}
function Qi(a){a=a||"";var b=z("ytcsi.reference");b||(Pi(),b=z("ytcsi.reference"));if(b[a])return b[a];var c=Pi(),d={timerName:a,info:{},tick:{}};c.push(d);return b[a]=d}
;var Ri=z("ytLoggingLatencyUsageStats_")||{};y("ytLoggingLatencyUsageStats_",Ri,void 0);function Si(){this.f=0}
function Ti(){Si.instance||(Si.instance=new Si);return Si.instance}
Si.prototype.tick=function(a,b,c){Ui(this,"tick_"+a+"_"+b)||Gg("latencyActionTicked",{tickName:a,clientActionNonce:b},{timestamp:c})};
Si.prototype.info=function(a,b){var c=Object.keys(a).join("");Ui(this,"info_"+c+"_"+b)||(a.clientActionNonce=b,Gg("latencyActionInfo",a))};
function Ui(a,b){Ri[b]=Ri[b]||{count:0};var c=Ri[b];c.count++;c.time=V();a.f||(a.f=mg(function(){var d=V(),e;for(e in Ri)Ri[e]&&6E4<d-Ri[e].time&&delete Ri[e];a&&(a.f=0)},0,5E3));
return 5<c.count?(6===c.count&&1>1E5*Math.random()&&(c=new Hg("CSI data exceeded logging limit with key",b),0===b.indexOf("info")?Kg(c):Lg(c)),!0):!1}
;var Y={},Vi=(Y.ad_allowed="adTypesAllowed",Y.yt_abt="adBreakType",Y.ad_cpn="adClientPlaybackNonce",Y.ad_docid="adVideoId",Y.yt_ad_an="adNetworks",Y.ad_at="adType",Y.browse_id="browseId",Y.p="httpProtocol",Y.t="transportProtocol",Y.cpn="clientPlaybackNonce",Y.ccs="creatorInfo.creatorCanaryState",Y.cseg="creatorInfo.creatorSegment",Y.csn="clientScreenNonce",Y.docid="videoId",Y.GetHome_rid="requestIds",Y.GetSearch_rid="requestIds",Y.GetPlayer_rid="requestIds",Y.GetWatchNext_rid="requestIds",Y.GetBrowse_rid=
"requestIds",Y.GetLibrary_rid="requestIds",Y.is_continuation="isContinuation",Y.is_nav="isNavigation",Y.b_p="kabukiInfo.browseParams",Y.is_prefetch="kabukiInfo.isPrefetch",Y.is_secondary_nav="kabukiInfo.isSecondaryNav",Y.prev_browse_id="kabukiInfo.prevBrowseId",Y.query_source="kabukiInfo.querySource",Y.voz_type="kabukiInfo.vozType",Y.yt_lt="loadType",Y.mver="creatorInfo.measurementVersion",Y.yt_ad="isMonetized",Y.nr="webInfo.navigationReason",Y.nrsu="navigationRequestedSameUrl",Y.ncnp="webInfo.nonPreloadedNodeCount",
Y.pnt="performanceNavigationTiming",Y.prt="playbackRequiresTap",Y.plt="playerInfo.playbackType",Y.pis="playerInfo.playerInitializedState",Y.paused="playerInfo.isPausedOnLoad",Y.yt_pt="playerType",Y.fmt="playerInfo.itag",Y.yt_pl="watchInfo.isPlaylist",Y.yt_pre="playerInfo.preloadType",Y.yt_ad_pr="prerollAllowed",Y.pa="previousAction",Y.yt_red="isRedSubscriber",Y.rce="mwebInfo.responseContentEncoding",Y.scrh="screenHeight",Y.scrw="screenWidth",Y.st="serverTimeMs",Y.aq="tvInfo.appQuality",Y.br_trs="tvInfo.bedrockTriggerState",
Y.kebqat="kabukiInfo.earlyBrowseRequestInfo.abandonmentType",Y.kebqa="kabukiInfo.earlyBrowseRequestInfo.adopted",Y.label="tvInfo.label",Y.is_mdx="tvInfo.isMdx",Y.preloaded="tvInfo.isPreloaded",Y.upg_player_vis="playerInfo.visibilityState",Y.query="unpluggedInfo.query",Y.upg_chip_ids_string="unpluggedInfo.upgChipIdsString",Y.yt_vst="videoStreamType",Y.vph="viewportHeight",Y.vpw="viewportWidth",Y.yt_vis="isVisible",Y.rcl="mwebInfo.responseContentLength",Y.GetSettings_rid="requestIds",Y.GetTrending_rid=
"requestIds",Y.GetMusicSearchSuggestions_rid="requestIds",Y.REQUEST_ID="requestIds",Y),Wi="isContinuation isNavigation kabukiInfo.earlyBrowseRequestInfo.adopted kabukiInfo.isPrefetch kabukiInfo.isSecondaryNav isMonetized navigationRequestedSameUrl performanceNavigationTiming playerInfo.isPausedOnLoad prerollAllowed isRedSubscriber tvInfo.isMdx tvInfo.isPreloaded isVisible watchInfo.isPlaylist playbackRequiresTap".split(" "),Xi={},Yi=(Xi.ccs="CANARY_STATE_",Xi.mver="MEASUREMENT_VERSION_",Xi.pis="PLAYER_INITIALIZED_STATE_",
Xi.yt_pt="LATENCY_PLAYER_",Xi.pa="LATENCY_ACTION_",Xi.yt_vst="VIDEO_STREAM_TYPE_",Xi),Zi="all_vc ap c cver cbrand cmodel cplatform ctheme ei l_an l_mm plid srt yt_fss yt_li vpst vpni2 vpil2 icrc icrt pa GetAccountOverview_rid GetHistory_rid cmt d_vpct d_vpnfi d_vpni nsru pc pfa pfeh pftr pnc prerender psc rc start tcrt tcrc ssr vpr vps yt_abt yt_fn yt_fs yt_pft yt_pre yt_pt yt_pvis ytu_pvis yt_ref yt_sts tds".split(" ");function $i(a){return!!R("FORCE_CSI_ON_GEL",!1)||S("csi_on_gel")||!!Gi(a).useGel}
function aj(a){a=Gi(a);if(!("gel"in a))a.gel={gelTicks:{},gelInfos:{}};else if(a.gel){var b=a.gel;b.gelInfos||(b.gelInfos={});b.gelTicks||(b.gelTicks={})}return a.gel}
;function bj(a,b,c){if(null!==b)if(Hi(c)[a]=b,$i(c)){var d=b;b=aj(c);if(b.gelInfos)b.gelInfos["info_"+a]=!0;else{var e={};b.gelInfos=(e["info_"+a]=!0,e)}if(a in Vi){if(a.match("_rid")){var f=a.split("_rid")[0];a="REQUEST_ID"}b=Vi[a];0<=Na(Wi,b)&&(d=!!d);a in Yi&&"string"===typeof d&&(d=Yi[a]+d.toUpperCase());a=d;d=b.split(".");for(var g=e={},h=0;h<d.length-1;h++){var k=d[h];g[k]={};g=g[k]}g[d[d.length-1]]="requestIds"===b?[{id:a,endpoint:f}]:a;f=Ki({},e)}else 0<=Na(Zi,a)||Kg(new Hg("Unknown label logged with GEL CSI",
a)),f=void 0;f&&$i(c)&&(b=Qi(c||""),Ki(b.info,f),b=aj(c),"gelInfos"in b||(b.gelInfos={}),Ki(b.gelInfos,f),c=Ii(c),Ti().info(f,c))}else Qi(c||"").info[a]=b}
function cj(a,b,c){var d=Fi(c);if(S("use_first_tick")&&dj(a,c))return d[a];if(!b&&"_"!==a[0]){var e=a;Ci.mark&&(0==e.lastIndexOf("mark_",0)||(e="mark_"+e),c&&(e+=" ("+c+")"),Ci.mark(e))}e=b||V();d[a]=e;e=aj(c);e.gelTicks&&(e.gelTicks["tick_"+a]=!0);c||b||V();if($i(c)){Qi(c||"").tick[a]=b||V();e=Ii(c);if("_start"===a){var f=Ti();Ui(f,"baseline_"+e)||Gg("latencyActionBaselined",{clientActionNonce:e},{timestamp:b})}else Ti().tick(a,e,b);Ji(c);e=!0}else e=!1;if(!e){if(!z("yt.timing."+(c||"")+"pingSent_")&&
(f=R((c||"")+"TIMING_ACTION",void 0),e=Fi(c),z("ytglobal.timing"+(c||"")+"ready_")&&f&&dj("_start")&&Ei(c)))if(Ji(c),c)ej(c);else{f=!0;var g=R("TIMING_WAIT",[]);if(g.length)for(var h=0,k=g.length;h<k;++h)if(!(g[h]in e)){f=!1;break}f&&ej(c)}Qi(c||"").tick[a]=b||V()}return d[a]}
function dj(a,b){var c=Fi(b);return a in c}
function ej(a){if(!$i(a)){var b=Fi(a),c=Hi(a),d=b._start,e=R("CSI_SERVICE_NAME","youtube"),f={v:2,s:e,action:R((a||"")+"TIMING_ACTION",void 0)},g=c.srt;void 0!==b.srt&&delete c.srt;b.aft=Ei(a);var h=Fi(a),k=h.pbr,l=h.vc;h=h.pbs;k&&l&&h&&k<l&&l<h&&Hi(a).yt_pvis&&"youtube"===e&&(bj("yt_lt","hot_bg",a),e=b.vc,k=b.pbs,delete b.aft,c.aft=Math.round(k-e));for(var m in c)"_"!==m.charAt(0)&&(f[m]=c[m]);b.ps=V();m={};e=[];for(var q in b)"_"!==q.charAt(0)&&(k=Math.round(b[q]-d),m[q]=k,e.push(q+"."+k));f.rt=
e.join(",");b=!!c.ap;S("debug_csi_data")&&(c=z("yt.timing.csiData"),c||(c=[],Ka("yt.timing.csiData",c)),c.push({page:location.href,time:new Date,args:f}));c="";for(var v in f)f.hasOwnProperty(v)&&(c+="&"+v+"="+f[v]);f="/csi_204?"+c.substring(1);if(window.navigator&&window.navigator.sendBeacon&&b){var r=void 0===r?"":r;ef(f,r)||bf(f,void 0,void 0,void 0,r)}else bf(f);y("yt.timing."+(a||"")+"pingSent_",!0,void 0);Mh(zi,new xi(m.aft+(Number(g)||0),a))}}
if(S("overwrite_polyfill_on_logging_lib_loaded")){var fj=window;fj.ytcsi&&(fj.ytcsi.info=bj)};function gj(a,b,c){return cj(a,b,c)}
C(Ci.clearResourceTimings||Ci.webkitClearResourceTimings||Ci.mozClearResourceTimings||Ci.msClearResourceTimings||Ci.oClearResourceTimings||xa,Ci);var hj=window;S("csi_on_gel")&&hj.ytcsi&&(hj.ytcsi.tick=gj);function ij(a){return(0===a.search("cue")||0===a.search("load"))&&"loadModule"!==a}
function jj(a,b,c){"string"===typeof a&&(a={mediaContentUrl:a,startSeconds:b,suggestedQuality:c});a:{if((b=a.mediaContentUrl)&&(b=/\/([ve]|embed)\/([^#?]+)/.exec(b))&&b[2]){b=b[2];break a}b=null}a.videoId=b;return kj(a)}
function kj(a,b,c){if("string"===typeof a)return{videoId:a,startSeconds:b,suggestedQuality:c};b=["endSeconds","startSeconds","mediaContentUrl","suggestedQuality","videoId"];c={};for(var d=0;d<b.length;d++){var e=b[d];a[e]&&(c[e]=a[e])}return c}
function lj(a,b,c,d){if(Ca(a)&&!Aa(a)){b="playlist list listType index startSeconds suggestedQuality".split(" ");c={};for(d=0;d<b.length;d++){var e=b[d];a[e]&&(c[e]=a[e])}return c}b={index:b,startSeconds:c,suggestedQuality:d};"string"===typeof a&&16===a.length?b.list="PL"+a:b.playlist=a;return b}
;function mj(a){a=void 0===a?!1:a;N.call(this);this.f=new P(a);Hc(this,Ja(Ic,this.f))}
E(mj,N);mj.prototype.subscribe=function(a,b,c){return this.g?0:this.f.subscribe(a,b,c)};
mj.prototype.j=function(a,b){this.g||this.f.M.apply(this.f,arguments)};function nj(a,b,c){mj.call(this);this.h=a;this.i=b;this.l=c}
t(nj,mj);function oj(a,b,c){if(!a.g){var d=a.h;d.g||a.i!=d.f||(a={id:a.l,command:b},c&&(a.data=c),d.f.postMessage(xd(a),d.i))}}
nj.prototype.o=function(){this.i=this.h=null;mj.prototype.o.call(this)};function pj(a){N.call(this);this.f=a;this.f.subscribe("command",this.sa,this);this.h={};this.j=!1}
t(pj,N);n=pj.prototype;n.start=function(){this.j||this.g||(this.j=!0,oj(this.f,"RECEIVING"))};
n.sa=function(a,b,c){if(this.j&&!this.g){var d=b||{};switch(a){case "addEventListener":"string"===typeof d.event&&(a=d.event,a in this.h||(c=C(this.Ta,this,a),this.h[a]=c,this.addEventListener(a,c)));break;case "removeEventListener":"string"===typeof d.event&&qj(this,d.event);break;default:this.i.isReady()&&this.i.isExternalMethodAvailable(a,c||null)&&(b=rj(a,b||{}),c=this.i.handleExternalCall(a,b,c||null),(c=sj(a,c))&&this.j&&!this.g&&oj(this.f,a,c))}}};
n.Ta=function(a,b){this.j&&!this.g&&oj(this.f,a,this.fa(a,b))};
n.fa=function(a,b){if(null!=b)return{value:b}};
function qj(a,b){b in a.h&&(a.removeEventListener(b,a.h[b]),delete a.h[b])}
n.o=function(){var a=this.f;a.g||Vd(a.f,"command",this.sa,this);this.f=null;for(var b in this.h)qj(this,b);N.prototype.o.call(this)};function tj(a,b){pj.call(this,b);this.i=a;this.start()}
t(tj,pj);tj.prototype.addEventListener=function(a,b){this.i.addEventListener(a,b)};
tj.prototype.removeEventListener=function(a,b){this.i.removeEventListener(a,b)};
function rj(a,b){switch(a){case "loadVideoById":return b=kj(b),[b];case "cueVideoById":return b=kj(b),[b];case "loadVideoByPlayerVars":return[b];case "cueVideoByPlayerVars":return[b];case "loadPlaylist":return b=lj(b),[b];case "cuePlaylist":return b=lj(b),[b];case "seekTo":return[b.seconds,b.allowSeekAhead];case "playVideoAt":return[b.index];case "setVolume":return[b.volume];case "setPlaybackQuality":return[b.suggestedQuality];case "setPlaybackRate":return[b.suggestedRate];case "setLoop":return[b.loopPlaylists];
case "setShuffle":return[b.shufflePlaylist];case "getOptions":return[b.module];case "getOption":return[b.module,b.option];case "setOption":return[b.module,b.option,b.value];case "handleGlobalKeyDown":return[b.keyCode,b.shiftKey,b.ctrlKey,b.altKey,b.metaKey,b.key,b.code]}return[]}
function sj(a,b){switch(a){case "isMuted":return{muted:b};case "getVolume":return{volume:b};case "getPlaybackRate":return{playbackRate:b};case "getAvailablePlaybackRates":return{availablePlaybackRates:b};case "getVideoLoadedFraction":return{videoLoadedFraction:b};case "getPlayerState":return{playerState:b};case "getCurrentTime":return{currentTime:b};case "getPlaybackQuality":return{playbackQuality:b};case "getAvailableQualityLevels":return{availableQualityLevels:b};case "getDuration":return{duration:b};
case "getVideoUrl":return{videoUrl:b};case "getVideoEmbedCode":return{videoEmbedCode:b};case "getPlaylist":return{playlist:b};case "getPlaylistIndex":return{playlistIndex:b};case "getOptions":return{options:b};case "getOption":return{option:b}}}
tj.prototype.fa=function(a,b){switch(a){case "onReady":return;case "onStateChange":return{playerState:b};case "onPlaybackQualityChange":return{playbackQuality:b};case "onPlaybackRateChange":return{playbackRate:b};case "onError":return{errorCode:b}}return pj.prototype.fa.call(this,a,b)};
tj.prototype.o=function(){pj.prototype.o.call(this);delete this.i};function uj(a,b,c){N.call(this);var d=this;c=c||R("POST_MESSAGE_ORIGIN",void 0)||window.document.location.protocol+"//"+window.document.location.hostname;this.h=b||null;this.w="*";this.i=c;this.sessionId=null;this.channel="widget";this.A=!!a;this.m=function(e){a:if(!("*"!=d.i&&e.origin!=d.i||d.h&&e.source!=d.h||"string"!==typeof e.data)){try{var f=JSON.parse(e.data)}catch(g){break a}if(!(null==f||d.A&&(d.sessionId&&d.sessionId!=f.id||d.channel&&d.channel!=f.channel))&&f)switch(f.event){case "listening":"null"!=
e.origin&&(d.i=d.w=e.origin);d.h=e.source;d.sessionId=f.id;d.f&&(d.f(),d.f=null);break;case "command":d.j&&(!d.l||0<=Na(d.l,f.func))&&d.j(f.func,f.args,e.origin)}}};
this.l=this.f=this.j=null;window.addEventListener("message",this.m)}
t(uj,N);uj.prototype.sendMessage=function(a,b){var c=b||this.h;if(c){this.sessionId&&(a.id=this.sessionId);this.channel&&(a.channel=this.channel);try{var d=JSON.stringify(a);c.postMessage(d,this.w)}catch(e){we(e)}}};
uj.prototype.o=function(){window.removeEventListener("message",this.m);N.prototype.o.call(this)};function vj(){var a=this.g=new uj(!!R("WIDGET_ID_ENFORCE")),b=C(this.Qa,this);a.j=b;a.l=null;this.g.channel="widget";if(a=R("WIDGET_ID"))this.g.sessionId=a;this.i=[];this.l=!1;this.j={}}
n=vj.prototype;n.Qa=function(a,b,c){"addEventListener"==a&&b?(a=b[0],this.j[a]||"onReady"==a||(this.addEventListener(a,wj(this,a)),this.j[a]=!0)):this.na(a,b,c)};
n.na=function(){};
function wj(a,b){return C(function(c){this.sendMessage(b,c)},a)}
n.addEventListener=function(){};
n.Aa=function(){this.l=!0;this.sendMessage("initialDelivery",this.ga());this.sendMessage("onReady");G(this.i,this.ta,this);this.i=[]};
n.ga=function(){return null};
function xj(a,b){a.sendMessage("infoDelivery",b)}
n.ta=function(a){this.l?this.g.sendMessage(a):this.i.push(a)};
n.sendMessage=function(a,b){this.ta({event:a,info:void 0==b?null:b})};
n.dispose=function(){this.g=null};function yj(a){vj.call(this);this.f=a;this.h=[];this.addEventListener("onReady",C(this.Oa,this));this.addEventListener("onVideoProgress",C(this.Xa,this));this.addEventListener("onVolumeChange",C(this.Ya,this));this.addEventListener("onApiChange",C(this.Sa,this));this.addEventListener("onPlaybackQualityChange",C(this.Ua,this));this.addEventListener("onPlaybackRateChange",C(this.Va,this));this.addEventListener("onStateChange",C(this.Wa,this));this.addEventListener("onWebglSettingsChanged",C(this.Za,
this))}
t(yj,vj);n=yj.prototype;n.na=function(a,b,c){if(this.f.isExternalMethodAvailable(a,c)){b=b||[];if(0<b.length&&ij(a)){var d=b;if(Ca(d[0])&&!Aa(d[0]))d=d[0];else{var e={};switch(a){case "loadVideoById":case "cueVideoById":e=kj.apply(window,d);break;case "loadVideoByUrl":case "cueVideoByUrl":e=jj.apply(window,d);break;case "loadPlaylist":case "cuePlaylist":e=lj.apply(window,d)}d=e}b.length=1;b[0]=d}this.f.handleExternalCall(a,b,c);ij(a)&&xj(this,this.ga())}};
n.Oa=function(){var a=C(this.Aa,this);this.g.f=a};
n.addEventListener=function(a,b){this.h.push({eventType:a,listener:b});this.f.addEventListener(a,b)};
n.ga=function(){if(!this.f)return null;var a=this.f.getApiInterface();Sa(a,"getVideoData");for(var b={apiInterface:a},c=0,d=a.length;c<d;c++){var e=a[c];if(0===e.search("get")||0===e.search("is")){var f=0;0===e.search("get")?f=3:0===e.search("is")&&(f=2);f=e.charAt(f).toLowerCase()+e.substr(f+1);try{var g=this.f[e]();b[f]=g}catch(h){}}}b.videoData=this.f.getVideoData();b.currentTimeLastUpdated_=D()/1E3;return b};
n.Wa=function(a){a={playerState:a,currentTime:this.f.getCurrentTime(),duration:this.f.getDuration(),videoData:this.f.getVideoData(),videoStartBytes:0,videoBytesTotal:this.f.getVideoBytesTotal(),videoLoadedFraction:this.f.getVideoLoadedFraction(),playbackQuality:this.f.getPlaybackQuality(),availableQualityLevels:this.f.getAvailableQualityLevels(),currentTimeLastUpdated_:D()/1E3,playbackRate:this.f.getPlaybackRate(),mediaReferenceTime:this.f.getMediaReferenceTime()};this.f.getVideoUrl&&(a.videoUrl=
this.f.getVideoUrl());this.f.getVideoContentRect&&(a.videoContentRect=this.f.getVideoContentRect());this.f.getProgressState&&(a.progressState=this.f.getProgressState());this.f.getPlaylist&&(a.playlist=this.f.getPlaylist());this.f.getPlaylistIndex&&(a.playlistIndex=this.f.getPlaylistIndex());this.f.getStoryboardFormat&&(a.storyboardFormat=this.f.getStoryboardFormat());xj(this,a)};
n.Ua=function(a){xj(this,{playbackQuality:a})};
n.Va=function(a){xj(this,{playbackRate:a})};
n.Sa=function(){for(var a=this.f.getOptions(),b={namespaces:a},c=0,d=a.length;c<d;c++){var e=a[c],f=this.f.getOptions(e);b[e]={options:f};for(var g=0,h=f.length;g<h;g++){var k=f[g],l=this.f.getOption(e,k);b[e][k]=l}}this.sendMessage("apiInfoDelivery",b)};
n.Ya=function(){xj(this,{muted:this.f.isMuted(),volume:this.f.getVolume()})};
n.Xa=function(a){a={currentTime:a,videoBytesLoaded:this.f.getVideoBytesLoaded(),videoLoadedFraction:this.f.getVideoLoadedFraction(),currentTimeLastUpdated_:D()/1E3,playbackRate:this.f.getPlaybackRate(),mediaReferenceTime:this.f.getMediaReferenceTime()};this.f.getProgressState&&(a.progressState=this.f.getProgressState());xj(this,a)};
n.Za=function(){var a={sphericalProperties:this.f.getSphericalProperties()};xj(this,a)};
n.dispose=function(){vj.prototype.dispose.call(this);for(var a=0;a<this.h.length;a++){var b=this.h[a];this.f.removeEventListener(b.eventType,b.listener)}this.h=[]};function zj(a,b,c){N.call(this);this.f=a;this.i=c;this.j=X(window,"message",C(this.l,this));this.h=new nj(this,a,b);Hc(this,Ja(Ic,this.h))}
t(zj,N);zj.prototype.l=function(a){var b;if(b=!this.g)if(b=a.origin==this.i)a:{b=this.f;do{b:{var c=a.source;do{if(c==b){c=!0;break b}if(c==c.parent)break;c=c.parent}while(null!=c);c=!1}if(c){b=!0;break a}b=b.opener}while(null!=b);b=!1}if(b&&(b=a.data,"string"===typeof b)){try{b=JSON.parse(b)}catch(d){return}b.command&&(c=this.h,c.g||c.j("command",b.command,b.data,a.origin))}};
zj.prototype.o=function(){hg(this.j);this.f=null;N.prototype.o.call(this)};function Aj(){var a=cb(Bj),b;return Kd(new O(function(c,d){a.onSuccess=function(e){Ke(e)?c(e):d(new Cj("Request failed, status="+(e&&"status"in e?e.status:-1),"net.badstatus",e))};
a.onError=function(e){d(new Cj("Unknown request error","net.unknown",e))};
a.O=function(e){d(new Cj("Request timed out","net.timeout",e))};
b=Ue("//googleads.g.doubleclick.net/pagead/id",a)}),function(c){c instanceof Ld&&b.abort();
return Id(c)})}
function Cj(a,b){F.call(this,a+", errorCode="+b);this.errorCode=b;this.name="PromiseAjaxError"}
t(Cj,F);function Dj(){this.g=0;this.f=null}
Dj.prototype.then=function(a,b,c){return 1===this.g&&a?(a=a.call(c,this.f),Dd(a)?a:Ej(a)):2===this.g&&b?(a=b.call(c,this.f),Dd(a)?a:Fj(a)):this};
Dj.prototype.getValue=function(){return this.f};
Dj.prototype.$goog_Thenable=!0;function Fj(a){var b=new Dj;a=void 0===a?null:a;b.g=2;b.f=void 0===a?null:a;return b}
function Ej(a){var b=new Dj;a=void 0===a?null:a;b.g=1;b.f=void 0===a?null:a;return b}
;function Gj(a){F.call(this,a.message||a.description||a.name);this.isMissing=a instanceof Hj;this.isTimeout=a instanceof Cj&&"net.timeout"==a.errorCode;this.isCanceled=a instanceof Ld}
t(Gj,F);Gj.prototype.name="BiscottiError";function Hj(){F.call(this,"Biscotti ID is missing from server")}
t(Hj,F);Hj.prototype.name="BiscottiMissingError";var Bj={format:"RAW",method:"GET",timeout:5E3,withCredentials:!0},Ij=null;function qe(){if("1"===Xa(ne(),"args","privembed"))return Id(Error("Biscotti ID is not available in private embed mode"));Ij||(Ij=Kd(Aj().then(Jj),function(a){return Kj(2,a)}));
return Ij}
function Jj(a){a=a.responseText;if(0!=a.lastIndexOf(")]}'",0))throw new Hj;a=JSON.parse(a.substr(4));if(1<(a.type||1))throw new Hj;a=a.id;re(a);Ij=Ej(a);Lj(18E5,2);return a}
function Kj(a,b){var c=new Gj(b);re("");Ij=Fj(c);0<a&&Lj(12E4,a-1);throw c;}
function Lj(a,b){T(function(){Kd(Aj().then(Jj,function(c){return Kj(b,c)}),xa)},a)}
function Mj(){try{var a=z("yt.ads.biscotti.getId_");return a?a():qe()}catch(b){return Id(b)}}
;function Nj(a){if("1"!==Xa(ne(),"args","privembed")){a&&pe();try{Mj().then(function(){},function(){}),T(Nj,18E5)}catch(b){ve(b)}}}
;var Z=z("ytglobal.prefsUserPrefsPrefs_")||{};y("ytglobal.prefsUserPrefsPrefs_",Z,void 0);function Oj(){this.f=R("ALT_PREF_COOKIE_NAME","PREF");var a=oc.get(""+this.f,void 0);if(a){a=decodeURIComponent(a).split("&");for(var b=0;b<a.length;b++){var c=a[b].split("="),d=c[0];(c=c[1])&&(Z[d]=c.toString())}}}
n=Oj.prototype;n.get=function(a,b){Pj(a);Qj(a);var c=void 0!==Z[a]?Z[a].toString():null;return null!=c?c:b?b:""};
n.set=function(a,b){Pj(a);Qj(a);if(null==b)throw Error("ExpectedNotNull");Z[a]=b.toString()};
n.remove=function(a){Pj(a);Qj(a);delete Z[a]};
n.save=function(){zg(this.f,this.dump(),63072E3)};
n.clear=function(){for(var a in Z)delete Z[a]};
n.dump=function(){var a=[],b;for(b in Z)a.push(b+"="+encodeURIComponent(String(Z[b])));return a.join("&")};
function Qj(a){if(/^f([1-9][0-9]*)$/.test(a))throw Error("ExpectedRegexMatch: "+a);}
function Pj(a){if(!/^\w+$/.test(a))throw Error("ExpectedRegexMismatch: "+a);}
function Rj(a){a=void 0!==Z[a]?Z[a].toString():null;return null!=a&&/^[A-Fa-f0-9]+$/.test(a)?parseInt(a,16):null}
ya(Oj);var Sj=null,Tj=null,Uj=null,Vj={};function Wj(a){var b=a.id;a=a.ve_type;var c=th++;a=new uh({veType:a,veCounter:c,elementIndex:void 0,dataElement:void 0,youtubeData:void 0});Vj[b]=a;b=Bh();c=zh();b&&c&&Wh(b,c,[a])}
function Xj(a){var b=a.csn;a=a.root_ve_type;if(b&&a&&(Eh(b,a),a=zh()))for(var c in Vj){var d=Vj[c];d&&Wh(b,a,[d])}}
function Yj(a){Vj[a.id]=new uh({trackingParams:a.tracking_params})}
function Zj(a){var b=Bh(),c=Vj[a.id];if(b&&c){a=S("use_default_events_client")?void 0:Fg;c={csn:b,ve:vh(c),gestureType:"INTERACTION_LOGGING_GESTURE_TYPE_GENERIC_CLICK"};var d={da:Dh(b),P:b};"UNDEFINED_CSN"==b?Xh("visualElementGestured",c,d):a?ug("visualElementGestured",c,a,d):Gg("visualElementGestured",c,d)}}
function ak(a){a=a.ids;var b=Bh();if(b)for(var c=0;c<a.length;c++){var d=Vj[a[c]];if(d){var e=b,f=S("use_default_events_client")?void 0:Fg;d={csn:e,ve:vh(d),eventType:1};var g={da:Dh(e),P:e};"UNDEFINED_CSN"==e?Xh("visualElementShown",d,g):f?ug("visualElementShown",d,f,g):Gg("visualElementShown",d,g)}}}
;y("yt.setConfig",Q,void 0);y("yt.config.set",Q,void 0);y("yt.setMsg",Pg,void 0);y("yt.msgs.set",Pg,void 0);y("yt.logging.errors.log",Ng,void 0);
y("writeEmbed",function(){var a=R("PLAYER_CONFIG",void 0);Nj(!0);"gvn"==a.args.ps&&(document.body.style.backgroundColor="transparent");var b=document.referrer,c=R("POST_MESSAGE_ORIGIN");window!=window.top&&b&&b!=document.URL&&(a.args.loaderUrl=b);R("LIGHTWEIGHT_AUTOPLAY")&&(a.args.autoplay="1");b="player";var d=void 0===d?!0:d;b="string"===typeof b?sc(b):b;var e=ui+"_"+Da(b),f=ti[e];f&&d?a&&a.args&&a.args.fflags&&a.args.fflags.includes("web_player_remove_playerproxy=true")?f.api.loadVideoByPlayerVars(a.args||
null):f.loadNewVideoConfig(a):(f=new ei(b,e,a,void 0),ti[e]=f,nf("player-added",f.api),Hc(f,Ja(vi,f)));a=f.api;Sj=a;a.addEventListener("onScreenChanged",Xj);a.addEventListener("onLogClientVeCreated",Wj);a.addEventListener("onLogServerVeCreated",Yj);a.addEventListener("onLogVeClicked",Zj);a.addEventListener("onLogVesShown",ak);d=R("POST_MESSAGE_ID","player");R("ENABLE_JS_API")?Uj=new yj(a):R("ENABLE_POST_API")&&"string"===typeof d&&"string"===typeof c&&(Tj=new zj(window.parent,d,c),Uj=new tj(a,Tj.h));
c=R("BG_P",void 0);nh(c)&&(R("BG_I")||R("BG_IU"))&&(jh=!0,ih.initialize(R("BG_I",null),R("BG_IU",null),c,mh,void 0,!!R("BG_CE",!1)));eh()},void 0);
y("yt.www.watch.ads.restrictioncookie.spr",function(a){bf(a+"mac_204?action_fcts=1");return!0},void 0);
var bk=ue(function(){cj("ol",void 0,void 0);var a=Oj.getInstance(),b=!!((Rj("f"+(Math.floor(119/31)+1))||0)&67108864),c=1<window.devicePixelRatio;if(document.body&&md(document.body,"exp-invert-logo"))if(c&&!md(document.body,"inverted-hdpi")){var d=document.body;if(d.classList)d.classList.add("inverted-hdpi");else if(!md(d,"inverted-hdpi")){var e=kd(d);ld(d,e+(0<e.length?" inverted-hdpi":"inverted-hdpi"))}}else!c&&md(document.body,"inverted-hdpi")&&nd();b!=c&&(b="f"+(Math.floor(119/31)+1),d=Rj(b)||
0,d=c?d|67108864:d&-67108865,0==d?delete Z[b]:(c=d.toString(16),Z[b]=c.toString()),a.save())}),ck=ue(function(){var a=Sj;
a&&a.sendAbandonmentPing&&a.sendAbandonmentPing();R("PL_ATT")&&ih.dispose();a=0;for(var b=ch.length;a<b;a++){var c=ch[a];if(!isNaN(c)){var d=z("yt.scheduler.instance.cancelJob");d?d(c):U(c)}}ch.length=0;ah("//static.doubleclick.net/instream/ad_status.js");dh=!1;Q("DCLKSTAT",0);Jc(Uj,Tj);if(a=Sj)a.removeEventListener("onScreenChanged",Xj),a.removeEventListener("onLogClientVeCreated",Wj),a.removeEventListener("onLogServerVeCreated",Yj),a.removeEventListener("onLogVeClicked",Zj),a.removeEventListener("onLogVesShown",
ak),a.destroy();Vj={}});
window.addEventListener?(window.addEventListener("load",bk),window.addEventListener("unload",ck)):window.attachEvent&&(window.attachEvent("onload",bk),window.attachEvent("onunload",ck));Ka("yt.abuse.player.botguardInitialized",z("yt.abuse.player.botguardInitialized")||oh);Ka("yt.abuse.player.invokeBotguard",z("yt.abuse.player.invokeBotguard")||ph);Ka("yt.abuse.dclkstatus.checkDclkStatus",z("yt.abuse.dclkstatus.checkDclkStatus")||fh);
Ka("yt.player.exports.navigate",z("yt.player.exports.navigate")||Fh);Ka("yt.util.activity.init",z("yt.util.activity.init")||pg);Ka("yt.util.activity.getTimeSinceActive",z("yt.util.activity.getTimeSinceActive")||sg);Ka("yt.util.activity.setTimestamp",z("yt.util.activity.setTimestamp")||qg);}).call(this);
