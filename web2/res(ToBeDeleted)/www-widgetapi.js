(function(){var k,l=this||self;function m(a){return"string"==typeof a}
function n(a){a=a.split(".");for(var b=l,c=0;c<a.length;c++)if(b=b[a[c]],null==b)return null;return b}
function aa(){}
function q(a){var b=typeof a;if("object"==b)if(a){if(a instanceof Array)return"array";if(a instanceof Object)return b;var c=Object.prototype.toString.call(a);if("[object Window]"==c)return"object";if("[object Array]"==c||"number"==typeof a.length&&"undefined"!=typeof a.splice&&"undefined"!=typeof a.propertyIsEnumerable&&!a.propertyIsEnumerable("splice"))return"array";if("[object Function]"==c||"undefined"!=typeof a.call&&"undefined"!=typeof a.propertyIsEnumerable&&!a.propertyIsEnumerable("call"))return"function"}else return"null";
else if("function"==b&&"undefined"==typeof a.call)return"object";return b}
function t(a){var b=typeof a;return"object"==b&&null!=a||"function"==b}
var v="closure_uid_"+(1E9*Math.random()>>>0),ba=0;function ca(a,b,c){return a.call.apply(a.bind,arguments)}
function ea(a,b,c){if(!a)throw Error();if(2<arguments.length){var d=Array.prototype.slice.call(arguments,2);return function(){var e=Array.prototype.slice.call(arguments);Array.prototype.unshift.apply(e,d);return a.apply(b,e)}}return function(){return a.apply(b,arguments)}}
function w(a,b,c){Function.prototype.bind&&-1!=Function.prototype.bind.toString().indexOf("native code")?w=ca:w=ea;return w.apply(null,arguments)}
var fa=Date.now||function(){return+new Date};
function x(a,b){var c=a.split("."),d=l;c[0]in d||"undefined"==typeof d.execScript||d.execScript("var "+c[0]);for(var e;c.length&&(e=c.shift());)c.length||void 0===b?d[e]&&d[e]!==Object.prototype[e]?d=d[e]:d=d[e]={}:d[e]=b}
function y(a,b){function c(){}
c.prototype=b.prototype;a.J=b.prototype;a.prototype=new c;a.prototype.constructor=a;a.L=function(d,e,f){for(var g=Array(arguments.length-2),h=2;h<arguments.length;h++)g[h-2]=arguments[h];return b.prototype[e].apply(d,g)}}
;var ha=Array.prototype.indexOf?function(a,b){return Array.prototype.indexOf.call(a,b,void 0)}:function(a,b){if(m(a))return m(b)&&1==b.length?a.indexOf(b,0):-1;
for(var c=0;c<a.length;c++)if(c in a&&a[c]===b)return c;return-1},z=Array.prototype.forEach?function(a,b,c){Array.prototype.forEach.call(a,b,c)}:function(a,b,c){for(var d=a.length,e=m(a)?a.split(""):a,f=0;f<d;f++)f in e&&b.call(c,e[f],f,a)};
function ia(a,b){a:{var c=a.length;for(var d=m(a)?a.split(""):a,e=0;e<c;e++)if(e in d&&b.call(void 0,d[e],e,a)){c=e;break a}c=-1}return 0>c?null:m(a)?a.charAt(c):a[c]}
function ja(a){return Array.prototype.concat.apply([],arguments)}
function ka(a){var b=a.length;if(0<b){for(var c=Array(b),d=0;d<b;d++)c[d]=a[d];return c}return[]}
;function la(a,b){this.f=a;this.g=b;this.b=0;this.a=null}
la.prototype.get=function(){if(0<this.b){this.b--;var a=this.a;this.a=a.next;a.next=null}else a=this.f();return a};function ma(a){var b=C,c;for(c in b)if(a.call(void 0,b[c],c,b))return c}
;function D(a,b){this.a=a===na&&b||"";this.b=oa}
D.prototype.toString=function(){return"Const{"+this.a+"}"};
var oa={},na={},E=new D(na,"");function F(){this.a="";this.b=pa}
var pa={};var qa=/&/g,ra=/</g,sa=/>/g,ta=/"/g,ua=/'/g,va=/\x00/g,wa=/[\x00&<>"']/;var G;a:{var xa=l.navigator;if(xa){var ya=xa.userAgent;if(ya){G=ya;break a}}G=""};function H(){this.a="";this.b=za}
var za={};function I(a){var b=new H;b.a=a;return b}
I("<!DOCTYPE html>");var J=I("");I("<br>");function Aa(a){var b=new F;b.a=E instanceof D&&E.constructor===D&&E.b===oa?E.a:"type_error:Const";a.src=(b instanceof F&&b.constructor===F&&b.b===pa?b.a:"type_error:TrustedResourceUrl").toString()}
;function Ba(a){l.setTimeout(function(){throw a;},0)}
var K;
function Ca(){var a=l.MessageChannel;"undefined"===typeof a&&"undefined"!==typeof window&&window.postMessage&&window.addEventListener&&-1==G.indexOf("Presto")&&(a=function(){var e=document.createElement("IFRAME");e.style.display="none";Aa(e);document.documentElement.appendChild(e);var f=e.contentWindow;e=f.document;e.open();e.write(J instanceof H&&J.constructor===H&&J.b===za?J.a:"type_error:SafeHtml");e.close();var g="callImmediate"+Math.random(),h="file:"==f.location.protocol?"*":f.location.protocol+"//"+
f.location.host;e=w(function(p){if(("*"==h||p.origin==h)&&p.data==g)this.port1.onmessage()},this);
f.addEventListener("message",e,!1);this.port1={};this.port2={postMessage:function(){f.postMessage(g,h)}}});
if("undefined"!==typeof a&&-1==G.indexOf("Trident")&&-1==G.indexOf("MSIE")){var b=new a,c={},d=c;b.port1.onmessage=function(){if(void 0!==c.next){c=c.next;var e=c.F;c.F=null;e()}};
return function(e){d.next={F:e};d=d.next;b.port2.postMessage(0)}}return"undefined"!==typeof document&&"onreadystatechange"in document.createElement("SCRIPT")?function(e){var f=document.createElement("SCRIPT");
f.onreadystatechange=function(){f.onreadystatechange=null;f.parentNode.removeChild(f);f=null;e();e=null};
document.documentElement.appendChild(f)}:function(e){l.setTimeout(e,0)}}
;function L(){this.b=this.a=null}
var Da=new la(function(){return new M},function(a){a.reset()});
L.prototype.add=function(a,b){var c=Da.get();c.set(a,b);this.b?this.b.next=c:this.a=c;this.b=c};
L.prototype.remove=function(){var a=null;this.a&&(a=this.a,this.a=this.a.next,this.a||(this.b=null),a.next=null);return a};
function M(){this.next=this.b=this.a=null}
M.prototype.set=function(a,b){this.a=a;this.b=b;this.next=null};
M.prototype.reset=function(){this.next=this.b=this.a=null};function Ea(a){N||Fa();Ga||(N(),Ga=!0);Ha.add(a,void 0)}
var N;function Fa(){if(l.Promise&&l.Promise.resolve){var a=l.Promise.resolve(void 0);N=function(){a.then(Ia)}}else N=function(){var b=Ia,c;
!(c="function"!=q(l.setImmediate))&&(c=l.Window&&l.Window.prototype)&&(c=-1==G.indexOf("Edge")&&l.Window.prototype.setImmediate==l.setImmediate);c?(K||(K=Ca()),K(b)):l.setImmediate(b)}}
var Ga=!1,Ha=new L;function Ia(){for(var a;a=Ha.remove();){try{a.a.call(a.b)}catch(c){Ba(c)}var b=Da;b.g(a);100>b.b&&(b.b++,a.next=b.a,b.a=a)}Ga=!1}
;function O(){this.f=this.f;this.g=this.g}
O.prototype.f=!1;O.prototype.dispose=function(){this.f||(this.f=!0,this.A())};
O.prototype.A=function(){if(this.g)for(;this.g.length;)this.g.shift()()};function Ja(a,b){var c,d;var e=document;e=b||e;if(e.querySelectorAll&&e.querySelector&&a)return e.querySelectorAll(a?"."+a:"");if(a&&e.getElementsByClassName){var f=e.getElementsByClassName(a);return f}f=e.getElementsByTagName("*");if(a){var g={};for(c=d=0;e=f[c];c++){var h=e.className,p;if(p="function"==typeof h.split)p=0<=ha(h.split(/\s+/),a);p&&(g[d++]=e)}g.length=d;return g}return f}
function Ka(a,b){for(var c=0;a;){if(b(a))return a;a=a.parentNode;c++}return null}
;var La=l.JSON.stringify;function P(a){O.call(this);this.m=1;this.h=[];this.i=0;this.a=[];this.b={};this.o=!!a}
y(P,O);k=P.prototype;k.subscribe=function(a,b,c){var d=this.b[a];d||(d=this.b[a]=[]);var e=this.m;this.a[e]=a;this.a[e+1]=b;this.a[e+2]=c;this.m=e+3;d.push(e);return e};
function Ma(a,b,c){var d=Q;if(a=d.b[a]){var e=d.a;(a=ia(a,function(f){return e[f+1]==b&&e[f+2]==c}))&&d.D(a)}}
k.D=function(a){var b=this.a[a];if(b){var c=this.b[b];if(0!=this.i)this.h.push(a),this.a[a+1]=aa;else{if(c){var d=ha(c,a);0<=d&&Array.prototype.splice.call(c,d,1)}delete this.a[a];delete this.a[a+1];delete this.a[a+2]}}return!!b};
k.H=function(a,b){var c=this.b[a];if(c){for(var d=Array(arguments.length-1),e=1,f=arguments.length;e<f;e++)d[e-1]=arguments[e];if(this.o)for(e=0;e<c.length;e++){var g=c[e];Na(this.a[g+1],this.a[g+2],d)}else{this.i++;try{for(e=0,f=c.length;e<f;e++)g=c[e],this.a[g+1].apply(this.a[g+2],d)}finally{if(this.i--,0<this.h.length&&0==this.i)for(;c=this.h.pop();)this.D(c)}}return 0!=e}return!1};
function Na(a,b,c){Ea(function(){a.apply(b,c)})}
k.clear=function(a){if(a){var b=this.b[a];b&&(z(b,this.D,this),delete this.b[a])}else this.a.length=0,this.b={}};
k.A=function(){P.J.A.call(this);this.clear();this.h.length=0};var Oa=/^(?:([^:/?#.]+):)?(?:\/\/(?:([^/?#]*)@)?([^/#?]*?)(?::([0-9]+))?(?=[/#?]|$))?([^?#]+)?(?:\?([^#]*))?(?:#([\s\S]*))?$/;function Pa(a){var b=a.match(Oa);a=b[1];var c=b[2],d=b[3];b=b[4];var e="";a&&(e+=a+":");d&&(e+="//",c&&(e+=c+"@"),e+=d,b&&(e+=":"+b));return e}
function Qa(a,b,c){if("array"==q(b))for(var d=0;d<b.length;d++)Qa(a,String(b[d]),c);else null!=b&&c.push(a+(""===b?"":"="+encodeURIComponent(String(b))))}
function Ra(a){var b=[],c;for(c in a)Qa(c,a[c],b);return b.join("&")}
var Sa=/#|$/;var R=window.yt&&window.yt.config_||window.ytcfg&&window.ytcfg.data_||{};x("yt.config_",R);function Ta(a){var b=arguments;if(1<b.length)R[b[0]]=b[1];else{b=b[0];for(var c in b)R[c]=b[c]}}
;function Ua(a){return a&&window.yterr?function(){try{return a.apply(this,arguments)}catch(b){Va(b)}}:a}
function Va(a,b){var c=n("yt.logging.errors.log");c?c(a,b,void 0,void 0,void 0):(c=[],c="ERRORS"in R?R.ERRORS:c,c.push([a,b,void 0,void 0,void 0]),Ta("ERRORS",c))}
;var Wa=0;x("ytDomDomGetNextId",n("ytDomDomGetNextId")||function(){return++Wa});var Xa={stopImmediatePropagation:1,stopPropagation:1,preventMouseEvent:1,preventManipulation:1,preventDefault:1,layerX:1,layerY:1,screenX:1,screenY:1,scale:1,rotation:1,webkitMovementX:1,webkitMovementY:1};
function S(a){this.type="";this.state=this.source=this.data=this.currentTarget=this.relatedTarget=this.target=null;this.charCode=this.keyCode=0;this.metaKey=this.shiftKey=this.ctrlKey=this.altKey=!1;this.clientY=this.clientX=0;this.changedTouches=this.touches=null;if(a=a||window.event){this.a=a;for(var b in a)b in Xa||(this[b]=a[b]);(b=a.target||a.srcElement)&&3==b.nodeType&&(b=b.parentNode);this.target=b;if(b=a.relatedTarget)try{b=b.nodeName?b:null}catch(c){b=null}else"mouseover"==this.type?b=a.fromElement:
"mouseout"==this.type&&(b=a.toElement);this.relatedTarget=b;this.clientX=void 0!=a.clientX?a.clientX:a.pageX;this.clientY=void 0!=a.clientY?a.clientY:a.pageY;this.keyCode=a.keyCode?a.keyCode:a.which;this.charCode=a.charCode||("keypress"==this.type?this.keyCode:0);this.altKey=a.altKey;this.ctrlKey=a.ctrlKey;this.shiftKey=a.shiftKey;this.metaKey=a.metaKey}}
S.prototype.preventDefault=function(){this.a&&(this.a.returnValue=!1,this.a.preventDefault&&this.a.preventDefault())};
S.prototype.stopPropagation=function(){this.a&&(this.a.cancelBubble=!0,this.a.stopPropagation&&this.a.stopPropagation())};
S.prototype.stopImmediatePropagation=function(){this.a&&(this.a.cancelBubble=!0,this.a.stopImmediatePropagation&&this.a.stopImmediatePropagation())};var C=n("ytEventsEventsListeners")||{};x("ytEventsEventsListeners",C);var Ya=n("ytEventsEventsCounter")||{count:0};x("ytEventsEventsCounter",Ya);
function Za(a,b,c,d){d=void 0===d?{}:d;a.addEventListener&&("mouseenter"!=b||"onmouseenter"in document?"mouseleave"!=b||"onmouseenter"in document?"mousewheel"==b&&"MozBoxSizing"in document.documentElement.style&&(b="MozMousePixelScroll"):b="mouseout":b="mouseover");return ma(function(e){var f="boolean"==typeof e[4]&&e[4]==!!d,g;if(g=t(e[4])&&t(d))a:{g=e[4];for(var h in g)if(!(h in d)||g[h]!==d[h]){g=!1;break a}for(h in d)if(!(h in g)){g=!1;break a}g=!0}return!!e.length&&e[0]==a&&e[1]==b&&e[2]==c&&
(f||g)})}
function $a(a){a&&("string"==typeof a&&(a=[a]),z(a,function(b){if(b in C){var c=C[b],d=c[0],e=c[1],f=c[3];c=c[4];d.removeEventListener?ab()||"boolean"==typeof c?d.removeEventListener(e,f,c):d.removeEventListener(e,f,!!c.capture):d.detachEvent&&d.detachEvent("on"+e,f);delete C[b]}}))}
var ab=function(a){var b=!1,c;return function(){b||(c=a(),b=!0);return c}}(function(){var a=!1;
try{var b=Object.defineProperty({},"capture",{get:function(){a=!0}});
window.addEventListener("test",null,b)}catch(c){}return a});
function bb(a,b,c){var d=void 0===d?{}:d;if(a&&(a.addEventListener||a.attachEvent)){var e=Za(a,b,c,d);if(!e){e=++Ya.count+"";var f=!("mouseenter"!=b&&"mouseleave"!=b||!a.addEventListener||"onmouseenter"in document);var g=f?function(h){h=new S(h);if(!Ka(h.relatedTarget,function(p){return p==a}))return h.currentTarget=a,h.type=b,c.call(a,h)}:function(h){h=new S(h);
h.currentTarget=a;return c.call(a,h)};
g=Ua(g);a.addEventListener?("mouseenter"==b&&f?b="mouseover":"mouseleave"==b&&f?b="mouseout":"mousewheel"==b&&"MozBoxSizing"in document.documentElement.style&&(b="MozMousePixelScroll"),ab()||"boolean"==typeof d?a.addEventListener(b,g,d):a.addEventListener(b,g,!!d.capture)):a.attachEvent("on"+b,g);C[e]=[a,b,c,g,d]}}}
;function cb(a){"function"==q(a)&&(a=Ua(a));return window.setInterval(a,250)}
;var db={};function eb(a){return db[a]||(db[a]=String(a).replace(/\-([a-z])/g,function(b,c){return c.toUpperCase()}))}
;var T={},fb=[],Q=new P,gb={};function hb(){z(fb,function(a){a()})}
function ib(a,b){b||(b=document);var c=ka(b.getElementsByTagName("yt:"+a)),d="yt-"+a,e=b||document;d=ka(e.querySelectorAll&&e.querySelector?e.querySelectorAll("."+d):Ja(d,b));return ja(c,d)}
function U(a,b){var c;"yt:"==a.tagName.toLowerCase().substr(0,3)?c=a.getAttribute(b):c=a?a.dataset?a.dataset[eb(b)]:a.getAttribute("data-"+b):null;return c}
function jb(a,b){Q.H.apply(Q,arguments)}
;function kb(a){this.b=a||{};this.f=this.a=!1;a=document.getElementById("www-widgetapi-script");if(this.a=!!("https:"==document.location.protocol||a&&0==a.src.indexOf("https:"))){a=[this.b,window.YTConfig||{}];for(var b=0;b<a.length;b++)a[b].host&&(a[b].host=a[b].host.replace("http://","https://"))}}
var V=null;function W(a,b){for(var c=[a.b,window.YTConfig||{}],d=0;d<c.length;d++){var e=c[d][b];if(void 0!=e)return e}return null}
function lb(a,b,c){V||(V={},bb(window,"message",w(a.g,a)));V[c]=b}
kb.prototype.g=function(a){if(a.origin==W(this,"host")||a.origin==W(this,"host").replace(/^http:/,"https:")){try{var b=JSON.parse(a.data)}catch(c){return}this.f=!0;this.a||0!=a.origin.indexOf("https:")||(this.a=!0);if(a=V[b.id])a.B=!0,a.B&&(z(a.u,a.C,a),a.u.length=0),a.I(b)}};function X(a,b,c){this.i=this.a=this.b=null;this.h=this[v]||(this[v]=++ba);this.f=0;this.B=!1;this.u=[];this.g=null;this.m=c;this.o={};c=document;if(a=m(a)?c.getElementById(a):a)if(c="iframe"==a.tagName.toLowerCase(),b.host||(b.host=c?Pa(a.src):"https://www.youtube.com"),this.b=new kb(b),c||(b=mb(this,a),this.i=a,(c=a.parentNode)&&c.replaceChild(b,a),a=b),this.a=a,this.a.id||(a=b=this.a,a=a[v]||(a[v]=++ba),b.id="widget"+a),T[this.a.id]=this,window.postMessage){this.g=new P;nb(this);b=W(this.b,"events");
for(var d in b)b.hasOwnProperty(d)&&this.addEventListener(d,b[d]);for(var e in gb)ob(this,e)}}
k=X.prototype;k.setSize=function(a,b){this.a.width=a;this.a.height=b;return this};
k.K=function(){return this.a};
k.I=function(a){this.s(a.event,a)};
k.addEventListener=function(a,b){var c=b;"string"==typeof b&&(c=function(){window[b].apply(window,arguments)});
if(!c)return this;this.g.subscribe(a,c);pb(this,a);return this};
function ob(a,b){var c=b.split(".");if(2==c.length){var d=c[1];a.m==c[0]&&pb(a,d)}}
k.destroy=function(){this.a.id&&(T[this.a.id]=null);var a=this.g;a&&"function"==typeof a.dispose&&a.dispose();if(this.i){a=this.a;var b=a.parentNode;b&&b.replaceChild(this.i,a)}else(a=this.a)&&a.parentNode&&a.parentNode.removeChild(a);V&&(V[this.h]=null);this.b=null;a=this.a;for(var c in C)C[c][0]==a&&$a(c);this.i=this.a=null};
k.v=function(){return{}};
function qb(a,b,c){c=c||[];c=Array.prototype.slice.call(c);b={event:"command",func:b,args:c};a.B?a.C(b):a.u.push(b)}
k.s=function(a,b){if(!this.g.f){var c={target:this,data:b};this.g.H(a,c);jb(this.m+"."+a,c)}};
function mb(a,b){for(var c=document.createElement("iframe"),d=b.attributes,e=0,f=d.length;e<f;e++){var g=d[e].value;null!=g&&""!=g&&"null"!=g&&c.setAttribute(d[e].name,g)}c.setAttribute("frameBorder",0);c.setAttribute("allowfullscreen",1);c.setAttribute("allow","accelerometer; autoplay; encrypted-media; gyroscope; picture-in-picture");c.setAttribute("title","YouTube "+W(a.b,"title"));(d=W(a.b,"width"))&&c.setAttribute("width",d);(d=W(a.b,"height"))&&c.setAttribute("height",d);var h=a.v();h.enablejsapi=
window.postMessage?1:0;window.location.host&&(h.origin=window.location.protocol+"//"+window.location.host);h.widgetid=a.h;window.location.href&&z(["debugjs","debugcss"],function(p){var u=window.location.href;var da=u.search(Sa);b:{var r=0;for(var A=p.length;0<=(r=u.indexOf(p,r))&&r<da;){var B=u.charCodeAt(r-1);if(38==B||63==B)if(B=u.charCodeAt(r+A),!B||61==B||38==B||35==B)break b;r+=A+1}r=-1}if(0>r)u=null;else{A=u.indexOf("&",r);if(0>A||A>da)A=da;r+=p.length+1;u=decodeURIComponent(u.substr(r,A-r).replace(/\+/g,
" "))}null===u||(h[p]=u)});
c.src=W(a.b,"host")+a.w()+"?"+Ra(h);return c}
k.G=function(){this.a&&this.a.contentWindow?this.C({event:"listening"}):window.clearInterval(this.f)};
function nb(a){lb(a.b,a,a.h);a.f=cb(w(a.G,a));bb(a.a,"load",w(function(){window.clearInterval(this.f);this.f=cb(w(this.G,this))},a))}
function pb(a,b){a.o[b]||(a.o[b]=!0,qb(a,"addEventListener",[b]))}
k.C=function(a){a.id=this.h;a.channel="widget";a=La(a);var b=this.b;var c=Pa(this.a.src);b=0==c.indexOf("https:")?[c]:b.a?[c.replace("http:","https:")]:b.f?[c]:[c,c.replace("http:","https:")];if(!this.a.contentWindow)throw Error("The YouTube player is not attached to the DOM.");for(c=0;c<b.length;c++)try{this.a.contentWindow.postMessage(a,b[c])}catch(d){if(d.name&&"SyntaxError"==d.name)Va(d,"WARNING");else throw d;}};function rb(a){return(0==a.search("cue")||0==a.search("load"))&&"loadModule"!=a}
function sb(a){return 0==a.search("get")||0==a.search("is")}
;function Y(a,b){if(!a)throw Error("YouTube player element ID required.");var c={title:"video player",videoId:"",width:640,height:360};if(b)for(var d in b)c[d]=b[d];X.call(this,a,c,"player");this.j={};this.l={}}
y(Y,X);function tb(a){if("iframe"!=a.tagName.toLowerCase()){var b=U(a,"videoid");b&&(b={videoId:b,width:U(a,"width"),height:U(a,"height")},new Y(a,b))}}
k=Y.prototype;k.w=function(){return"/embed/"+W(this.b,"videoId")};
k.v=function(){var a=W(this.b,"playerVars");if(a){var b={},c;for(c in a)b[c]=a[c];a=b}else a={};window!=window.top&&document.referrer&&(a.widget_referrer=document.referrer.substring(0,256));if(c=W(this.b,"embedConfig")){if(t(c))try{c=La(c)}catch(d){console.error("Invalid embed config JSON",d)}a.embed_config=c}return a};
k.I=function(a){var b=a.event;a=a.info;switch(b){case "apiInfoDelivery":if(t(a))for(var c in a)this.j[c]=a[c];break;case "infoDelivery":ub(this,a);break;case "initialDelivery":window.clearInterval(this.f);this.l={};this.j={};vb(this,a.apiInterface);ub(this,a);break;default:this.s(b,a)}};
function ub(a,b){if(t(b))for(var c in b)a.l[c]=b[c]}
function vb(a,b){z(b,function(c){this[c]||("getCurrentTime"==c?this[c]=function(){var d=this.l.currentTime;if(1==this.l.playerState){var e=(fa()/1E3-this.l.currentTimeLastUpdated_)*this.l.playbackRate;0<e&&(d+=Math.min(e,1))}return d}:rb(c)?this[c]=function(){this.l={};
this.j={};qb(this,c,arguments);return this}:sb(c)?this[c]=function(){var d=0;
0==c.search("get")?d=3:0==c.search("is")&&(d=2);return this.l[c.charAt(d).toLowerCase()+c.substr(d+1)]}:this[c]=function(){qb(this,c,arguments);
return this})},a)}
k.getVideoEmbedCode=function(){var a=parseInt(W(this.b,"width"),10);var b=parseInt(W(this.b,"height"),10),c=W(this.b,"host")+this.w();wa.test(c)&&(-1!=c.indexOf("&")&&(c=c.replace(qa,"&amp;")),-1!=c.indexOf("<")&&(c=c.replace(ra,"&lt;")),-1!=c.indexOf(">")&&(c=c.replace(sa,"&gt;")),-1!=c.indexOf('"')&&(c=c.replace(ta,"&quot;")),-1!=c.indexOf("'")&&(c=c.replace(ua,"&#39;")),-1!=c.indexOf("\x00")&&(c=c.replace(va,"&#0;")));a='<iframe width="'+a+'" height="'+b+'" src="'+c+'" frameborder="0" allow="accelerometer; autoplay; encrypted-media; gyroscope; picture-in-picture" allowfullscreen></iframe>';
return a};
k.getOptions=function(a){return this.j.namespaces?a?this.j[a].options||[]:this.j.namespaces||[]:[]};
k.getOption=function(a,b){if(this.j.namespaces&&a&&b)return this.j[a][b]};function Z(a,b){var c={title:"Thumbnail",videoId:"",width:120,height:68};if(b)for(var d in b)c[d]=b[d];X.call(this,a,c,"thumbnail")}
y(Z,X);function wb(a){if("iframe"!=a.tagName.toLowerCase()){var b=U(a,"videoid");if(b){b={videoId:b,events:{},width:U(a,"width"),height:U(a,"height"),thumbWidth:U(a,"thumb-width"),thumbHeight:U(a,"thumb-height"),thumbAlign:U(a,"thumb-align")};var c=U(a,"onclick");c&&(b.events.onClick=c);new Z(a,b)}}}
Z.prototype.w=function(){return"/embed/"+W(this.b,"videoId")};
Z.prototype.v=function(){return{player:0,thumb_width:W(this.b,"thumbWidth"),thumb_height:W(this.b,"thumbHeight"),thumb_align:W(this.b,"thumbAlign")}};
Z.prototype.s=function(a,b){Z.J.s.call(this,a,b?b.info:void 0)};x("YT.PlayerState.UNSTARTED",-1);x("YT.PlayerState.ENDED",0);x("YT.PlayerState.PLAYING",1);x("YT.PlayerState.PAUSED",2);x("YT.PlayerState.BUFFERING",3);x("YT.PlayerState.CUED",5);x("YT.get",function(a){return T[a]});
x("YT.scan",hb);x("YT.subscribe",function(a,b,c){Q.subscribe(a,b,c);gb[a]=!0;for(var d in T)ob(T[d],a)});
x("YT.unsubscribe",function(a,b,c){Ma(a,b,c)});
x("YT.Player",Y);x("YT.Thumbnail",Z);X.prototype.destroy=X.prototype.destroy;X.prototype.setSize=X.prototype.setSize;X.prototype.getIframe=X.prototype.K;X.prototype.addEventListener=X.prototype.addEventListener;Y.prototype.getVideoEmbedCode=Y.prototype.getVideoEmbedCode;Y.prototype.getOptions=Y.prototype.getOptions;Y.prototype.getOption=Y.prototype.getOption;fb.push(function(a){a=ib("player",a);z(a,tb)});
fb.push(function(){var a=ib("thumbnail");z(a,wb)});
"undefined"!=typeof YTConfig&&YTConfig.parsetags&&"onload"!=YTConfig.parsetags||hb();var xb=n("onYTReady");xb&&xb();var yb=n("onYouTubeIframeAPIReady");yb&&yb();var zb=n("onYouTubePlayerAPIReady");zb&&zb();}).call(this);
