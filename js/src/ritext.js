/*
 * Integrate P5.js
 * 	1) define the API (check with ReadersJS)

		RiText: _type, _push, _pop, _getBoundingBox, _translate, _rotate, _fill, _noFill, _stroke, _noStroke, _strokeWeight, _textFont, _textAlign, _textAscent, _textDescent, _textWidth, _showBounds, _rect, _createFont, _getGraphics
		ReadersJS: _width

 * 	2) remove all other methods from both renderers
 *  3) perhaps remove renderer classes, and just inject functions into global scope
 *
 */
(function(window, undefined) {

    /*global RiTa:0, console:0, RiText:true, RiTaEvent:0, RiString:0, Processing:0, window:0 */

	/////////////////////////////////////////////////////
	// private helpers
	/////////////////////////////////////////////////////

	// TODO: remove methods that are duplicated in rita.js

	function toColArr(obj, overrideAlpha) {

		var a = (typeof overrideAlpha === 'undefined') ? obj.a || 255 : overrideAlpha;
		return [ obj.r, obj.g, obj.b, a ];
	}

	function parseColor() {

		var a = arguments, len = a.length;

		var alpha = (this && this.alpha) ? this.alpha() : 255;

		var color = { r: 0, g: 0, b: 0, a: alpha };

		if (!len) return color;

		if (len == 1 && is(a[0],A)) {
			return parseColor.apply(this, a[0]);
		}

		if (len >= 3) {
			color.r = a[0];
			color.g = a[1];
			color.b = a[2];
		}
		if (len == 4) {
			color.a = a[3];
		}
		if (len <= 2) {
			color.r = a[0];
			color.g = a[0];
			color.b = a[0];
		}
		if (len == 2) {
			color.a = a[1];
		}

		return color;
	}

	function err(msg) {

		console && console.log("err(msg) :: "+msg);

		(!RiTa.SILENT) && (console && console.trace(this));

		throw Error("[RiTa] " + msg);
	}

	function warn() {

		if (RiTa.SILENT || !console) return;

		if (arguments && arguments.length) {
			console.warn("[WARN] "+arguments[0]);
			for (var i = 1; i < arguments.length; i++)
				console.warn('  '+arguments[i]);
		}
	}

	function log() {

		if (RiTa.SILENT || !console) return;

		for ( var i = 0; i < arguments.length; i++)
			console.log(arguments[i]);
	}
	function isNode() {

		return (typeof module != 'undefined' && module.exports);
	}

	function makeClass() { // By John Resig (MIT Licensed)

		return function(args) {

			if (this instanceof arguments.callee) {

				if (typeof this.init == "function") {

					this.init.apply(this, args && args.callee ? args : arguments);
				}
			}
			else {
				return new arguments.callee(arguments);
			}
		};
	}

	function endsWith(str, ending) {

		if (!is(str,S)) return false;
		return new RegExp(ending+'$').test(str);
		//return str.slice(-ending.length) == ending;
	}

	function trim(str) {

		// faster version from: http://blog.stevenlevithan.com/archives/faster-trim-javascript
		return str.replace(/^\s\s*/, '').replace(/\s\s*$/, '');
	}

	//////////////////////////////////////////////////////////////////////
	// RiText statics
	//////////////////////////////////////////////////////////////////////

	isNode() && (require('./rita'));

	RiText = makeClass();

  RiTa.LEFT = 37;
  RiTa.UP = 38;
  RiTa.RIGHT = 39;
  RiTa.DOWN = 40;
  RiTa.CENTER = 3;
	RiTa.MOVE_TO= "MoveTo";
	RiTa.COLOR_TO= "ColorTo";
	RiTa.FADE_IN= "FadeIn";
	RiTa.FADE_OUT= "FadeOut";
	RiTa.TEXT_TO= "TextTo";
	RiTa.TIMER= "Timer";
	RiTa.SCALE_TO= "ScaleTo";
	RiTa.ROTATE_TO= "RotateTo";
	RiTa.TEXT_ENTERED= "TextEntered";
	RiTa.BOUNDING_ALPHA= "BoundingAlpha";
	RiTa.LERP= "Lerp";

	/* Static container for properties related to the update/render loop */
	RiText._animator = {

		loopId : -1,
		actualFPS : 0,
		targetFPS : 60,
		isLooping : false,
		frameCount : 0,
		loopStarted : false,
		framesSinceLastFPS : 0,
		callbackDisabled : false,
		timeSinceLastFPS : Date.now()
	};

	var Easing = { // Penner's canonical set

		Linear: {

			None: function ( k ) {

				return k;
			}
		},

		Quadratic: {

			In: function ( k ) {

				return k * k;
			},

			Out: function ( k ) {

				return k * ( 2 - k );
			},

			InOut: function ( k ) {

				if ( ( k *= 2 ) < 1 ) return 0.5 * k * k;
				return - 0.5 * ( --k * ( k - 2 ) - 1 );
			}
		},

		Cubic: {

			In: function ( k ) {

				return k * k * k;
			},

			Out: function ( k ) {

				return --k * k * k + 1;
			},

			InOut: function ( k ) {

				if ( ( k *= 2 ) < 1 ) return 0.5 * k * k * k;
				return 0.5 * ( ( k -= 2 ) * k * k + 2 );
			}
		},

		Quartic: {

			In: function ( k ) {

				return k * k * k * k;
			},

			Out: function ( k ) {

				return 1 - (--k) * k * k * k;
			},

			InOut: function ( k ) {

				if ( ( k *= 2 ) < 1) return 0.5 * k * k * k * k;
				return - 0.5 * ( ( k -= 2 ) * k * k * k - 2 );
			}
		},

		Quintic: {

			In: function ( k ) {

				return k * k * k * k * k;
			},

			Out: function ( k ) {

				return --k * k * k * k * k + 1;
			},

			InOut: function ( k ) {

				if ( ( k *= 2 ) < 1 ) return 0.5 * k * k * k * k * k;
				return 0.5 * ( ( k -= 2 ) * k * k * k * k + 2 );
			}
		},

		Sinusoidal: {

			In: function ( k ) {

				return 1 - Math.cos( k * Math.PI / 2 );
			},

			Out: function ( k ) {

				return Math.sin( k * Math.PI / 2 );
			},

			InOut: function ( k ) {

				return 0.5 * ( 1 - Math.cos( Math.PI * k ) );
			}
		},

		Exponential: {

			In: function ( k ) {

				return k === 0 ? 0 : Math.pow( 1024, k - 1 );
			},

			Out: function ( k ) {

				return k === 1 ? 1 : 1 - Math.pow( 2, - 10 * k );
			},

			InOut: function ( k ) {

				if ( k === 0 ) return 0;
				if ( k === 1 ) return 1;
				if ( ( k *= 2 ) < 1 ) return 0.5 * Math.pow( 1024, k - 1 );
				return 0.5 * ( - Math.pow( 2, - 10 * ( k - 1 ) ) + 2 );
			}
		},

		Circular: {

			In: function ( k ) {

				return 1 - Math.sqrt( 1 - k * k );
			},

			Out: function ( k ) {

				return Math.sqrt( 1 - (--k) * k );
			},

			InOut: function ( k ) {

				if ( ( k *= 2 ) < 1) return - 0.5 * ( Math.sqrt( 1 - k * k) - 1);
				return 0.5 * ( Math.sqrt( 1 - ( k -= 2) * k) + 1);
			}
		},

		Elastic: {

			In: function ( k ) {

				var s, a = 0.1, p = 0.4;
				if ( k === 0 ) return 0;
				if ( k === 1 ) return 1;
				if ( !a || a < 1 ) { a = 1; s = p / 4; }
				else s = p * Math.asin( 1 / a ) / ( 2 * Math.PI );
				return - ( a * Math.pow( 2, 10 * ( k -= 1 ) ) * Math.sin( ( k - s ) * ( 2 * Math.PI ) / p ) );
			},

			Out: function ( k ) {

				var s, a = 0.1, p = 0.4;
				if ( k === 0 ) return 0;
				if ( k === 1 ) return 1;
				if ( !a || a < 1 ) { a = 1; s = p / 4; }
				else s = p * Math.asin( 1 / a ) / ( 2 * Math.PI );
				return ( a * Math.pow( 2, - 10 * k) * Math.sin( ( k - s ) * ( 2 * Math.PI ) / p ) + 1 );
			},

			InOut: function ( k ) {

				var s, a = 0.1, p = 0.4;
				if ( k === 0 ) return 0;
				if ( k === 1 ) return 1;
				if ( !a || a < 1 ) { a = 1; s = p / 4; }
				else s = p * Math.asin( 1 / a ) / ( 2 * Math.PI );
				if ( ( k *= 2 ) < 1 ) return - 0.5 * ( a * Math.pow( 2, 10 * ( k -= 1 ) ) * Math.sin( ( k - s ) * ( 2 * Math.PI ) / p ) );
				return a * Math.pow( 2, -10 * ( k -= 1 ) ) * Math.sin( ( k - s ) * ( 2 * Math.PI ) / p ) * 0.5 + 1;
			}
		},

		Back: {

			In: function ( k ) {

				var s = 1.70158;
				return k * k * ( ( s + 1 ) * k - s );
			},

			Out: function ( k ) {

				var s = 1.70158;
				return --k * k * ( ( s + 1 ) * k + s ) + 1;
			},

			InOut: function ( k ) {

				var s = 1.70158 * 1.525;
				if ( ( k *= 2 ) < 1 ) return 0.5 * ( k * k * ( ( s + 1 ) * k - s ) );
				return 0.5 * ( ( k -= 2 ) * k * ( ( s + 1 ) * k + s ) + 2 );
			}
		},

		Bounce: {

			In: function ( k ) {

				return 1 - Easing.Bounce.Out( 1 - k );
			},

			Out: function ( k ) {

				if ( k < ( 1 / 2.75 ) ) {

					return 7.5625 * k * k;

				} else if ( k < ( 2 / 2.75 ) ) {

					return 7.5625 * ( k -= ( 1.5 / 2.75 ) ) * k + 0.75;

				} else if ( k < ( 2.5 / 2.75 ) ) {

					return 7.5625 * ( k -= ( 2.25 / 2.75 ) ) * k + 0.9375;

				} else {

					return 7.5625 * ( k -= ( 2.625 / 2.75 ) ) * k + 0.984375;

				}
			},

			InOut: function ( k ) {

				if ( k < 0.5 ) return Easing.Bounce.In( k * 2 ) * 0.5;
				return Easing.Bounce.Out( k * 2 - 1 ) * 0.5 + 0.5;
			}
		}
	};

	var Interpolation = {

		Linear: function ( v, k ) {

			var m = v.length - 1, f = m * k, i = Math.floor( f ), fn = Interpolation.Utils.Linear;

			if ( k < 0 ) return fn( v[ 0 ], v[ 1 ], f );
			if ( k > 1 ) return fn( v[ m ], v[ m - 1 ], m - f );

			return fn( v[ i ], v[ i + 1 > m ? m : i + 1 ], f - i );
		},

		Bezier: function ( v, k ) {

			var b = 0, n = v.length - 1, pw = Math.pow, bn = Interpolation.Utils.Bernstein, i;

			for ( i = 0; i <= n; i++ ) {
				b += pw( 1 - k, n - i ) * pw( k, i ) * v[ i ] * bn( n, i );
			}
			return b;

		},

		CatmullRom: function ( v, k ) {

			var m = v.length - 1, f = m * k, i = Math.floor( f ), fn = Interpolation.Utils.CatmullRom;

			if ( v[ 0 ] === v[ m ] ) {

				if ( k < 0 ) i = Math.floor( f = m * ( 1 + k ) );

				return fn( v[ ( i - 1 + m ) % m ], v[ i ], v[ ( i + 1 ) % m ], v[ ( i + 2 ) % m ], f - i );
			}
			else {

				if ( k < 0 ) return v[ 0 ] - ( fn( v[ 0 ], v[ 0 ], v[ 1 ], v[ 1 ], -f ) - v[ 0 ] );
				if ( k > 1 ) return v[ m ] - ( fn( v[ m ], v[ m ], v[ m - 1 ], v[ m - 1 ], f - m ) - v[ m ] );

				return fn( v[ i ? i - 1 : 0 ], v[ i ], v[ m < i + 1 ? m : i + 1 ], v[ m < i + 2 ? m : i + 2 ], f - i );
			}
		},

		Utils: {

			Linear: function ( p0, p1, t ) {

				return ( p1 - p0 ) * t + p0;
			},

			Bernstein: function ( n , i ) {

				var fc = Interpolation.Utils.Factorial;
				return fc( n ) / fc( i ) / fc( n - i );
			},

			Factorial: ( function () {

				var a = [ 1 ];

				return function ( n ) {

					var s = 1, i;
					if ( a[ n ] ) return a[ n ];
					for ( i = n; i > 1; i-- ) s *= i;

					a[ n ] = s;
                    return a[n];
				};
			})(),

			CatmullRom: function ( p0, p1, p2, p3, t ) {

				var v0 = ( p2 - p0 ) * 0.5, v1 = ( p3 - p1 ) * 0.5, t2 = t * t, t3 = t * t2;
				return ( 2 * p1 - 2 * p2 + v0 + v1 ) * t3 + ( - 3 * p1 + 3 * p2 - 2 * v0 - v1 ) * t2 + v0 * t + p1;
			}
		}
	};

	//////////////////////////////////////////////////////////////////////////////////////
	// adapted from: https://github.com/sole/tween.js
	//////////////////////////////////////////////////////////////////////////////////////

	// ==== Animation types =========

	RiText.LINEAR = Easing.Linear.None;

	RiText.EASE_IN =  Easing.Exponential.In;
	RiText.EASE_OUT =  Easing.Exponential.Out;
	RiText.EASE_IN_OUT =  Easing.Exponential.InOut;

	RiText.EASE_IN_EXPO =  Easing.Exponential.In;
	RiText.EASE_OUT_EXPO =  Easing.Exponential.Out;
	RiText.EASE_IN_OUT_EXPO =  Easing.Exponential.InOut;

	RiText.EASE_IN_SINE = Easing.Sinusoidal.In;
	RiText.EASE_OUT_SINE = Easing.Sinusoidal.Out;
	RiText.EASE_IN_OUT_SINE = Easing.Sinusoidal.InOut;

	RiText.EASE_IN_CUBIC =  Easing.Cubic.In;
	RiText.EASE_OUT_CUBIC = Easing.Cubic.Out;
	RiText.EASE_IN_OUT_CUBIC =  Easing.Cubic.InOut;

	RiText.EASE_IN_QUARTIC =  Easing.Quartic.In;
	RiText.EASE_OUT_QUARTIC =  Easing.Quartic.Out;
	RiText.EASE_IN_OUT_QUARTIC =  Easing.Quartic.InOut;

	RiText.EASE_IN_QUINTIC = Easing.Quintic.In;
	RiText.EASE_OUT_QUINTIC = Easing.Quintic.Out;
	RiText.EASE_IN_OUT_QUINTIC = Easing.Quintic.InOut;

	RiText.BACK_IN = Easing.Back.In;
	RiText.BACK_OUT = Easing.Back.Out;
	RiText.BACK_IN_OUT = Easing.Back.InOut;

	RiText.BOUNCE_IN = Easing.Bounce.In;
	RiText.BOUNCE_OUT = Easing.Bounce.Out;
	RiText.BOUNCE_IN_OUT = Easing.Bounce.InOut;

	RiText.CIRCULAR_IN = Easing.Circular.In;
	RiText.CIRCULAR_OUT = Easing.Circular.Out;
	RiText.CIRCULAR_IN_OUT = Easing.Circular.InOut;

	RiText.ELASTIC_IN = Easing.Elastic.In;
	RiText.ELASTIC_OUT = Easing.Elastic.Out;
	RiText.ELASTIC_IN_OUT = Easing.Elastic.InOut;

	RiText._defaults = {

		// TODO(v2.0): change fontSize to _fontSize;
		fill : { r : 0, g : 0, b : 0, a : 255 }, fontFamily: 'Times New Roman',
		alignment : RiTa.LEFT, motionType : RiText.LINEAR, _font: null, fontSize: 14,
		paragraphLeading : 0, paragraphIndent: 30, indentFirstParagraph : false,
		boundingStroke : null, boundingStrokeWeight : 1, showBounds : false,  boundingFill:null,
		leadingFactor: 1.2, rotateX:0, rotateY:0, rotateZ:0, scaleX:1, scaleY:1, scaleZ:1,
		metrics : {"name":"Times New Roman","size":14,"ascent":9.744,"descent":3.024,"widths":
		{ "0":7,"1":7,"2":7,"3":7,"4":7,"5":7,"6":7,"7":7,"8":7,"9":7,"!":5,"\"":6,"#":7,"$":7,
		"%":12,"&":11,"'":3,"(":5,")":5,"*":7,"+":8,",":4,"-":5,".":4,"/":4,":":4,";":4,"<":8,
		"=":8,">":8,"?":6,"@":13,"A":10,"B":9,"C":9,"D":10,"E":9,"F":8,"G":10,"H":10,"I":5,
		"J":5,"K":10,"L":9,"M":12,"N":10,"O":10,"P":8,"Q":10,"R":9,"S":8,"T":9,"U":10,"V":10,
		"W":13,"X":10,"Y":10,"Z":9,"[":5,"\\":4,"]":5,"^":7,"_":7,"`":5,"a":6,"b":7,"c":6,"d":7,
		"e":6,"f":5,"g":7,"h":7,"i":4,"j":4,"k":7,"l":4,"m":11,"n":7,"o":7,"p":7,"q":7,"r":5,
		"s":5,"t":4,"u":7,"v":7,"w":10,"x":7,"y":7,"z":6,"{":7,"|":3,"}":7," ":4 } }
	};

	var TextBehavior = function (rt, object) {

		var _parent = rt;
		var _object = object || _parent;
		var _valuesStart = {};
		var _valuesEnd = {};
		var _duration = 1000;
		var _delayTime = 0;
		var _startTime = null;
		var _easingFunction = Easing.Linear.None;
		var _interpolationFunction = Interpolation.Linear;
		var _chainedTween = null;
		var _onUpdateCallback = null;
		var _onCompleteCallback = null;

		this.to = function ( properties, duration ) {

			if ( duration !== null ) {

				_duration = duration;
			}

			_valuesEnd = properties;

			return this;
		};

		this.start = function ( time ) {

			if (_parent)
				_parent._addBehavior( this );
			else
				err('Unable to add tween');

			_startTime = time !== undefined ? time : Date.now();
			_startTime += _delayTime;

			for ( var property in _valuesEnd ) {

				// This prevents the engine from interpolating null values
				if ( _object[ property ] === null ) {
					console.error('null value in interpolater for: '+property);
					continue;

				}

				// check if an Array was provided as property value
				if ( _valuesEnd[ property ] instanceof Array ) {

					if ( _valuesEnd[ property ].length === 0 ) {
						continue;
					}

					// create a local copy of the Array with the start value at the front
					_valuesEnd[ property ] = [ _object[ property ] ].concat( _valuesEnd[ property ] );
				}

				_valuesStart[ property ] = _object[ property ];
			}

			return this;
		};

		this.stop = function () {

			if (_parent) _parent.stopBehavior( this );
			return this;
		};

		this.delay = function ( amount ) {

			_delayTime = amount;
			return this;
		};

		this.easing = function ( easing ) {
			if (!easing) err('null easing!!');
			_easingFunction = easing;
			return this;
		};

		this.interpolation = function ( interpolation ) {

			_interpolationFunction = interpolation;
			return this;
		};

		this.chain = function ( chainedTween ) {

			_chainedTween = chainedTween;
			return this;
		};

		this.onUpdate = function ( onUpdateCallback ) {

			_onUpdateCallback = onUpdateCallback;
			return this;

		};

		this.onComplete = function ( onCompleteCallback ) {

			_onCompleteCallback = onCompleteCallback;
			return this;
		};

		this.update = function ( time ) {

			if ( time < _startTime ) return true;

			var elapsed = ( time - _startTime ) / _duration;
			elapsed = elapsed > 1 ? 1 : elapsed;

			var value = _easingFunction( elapsed );

			for ( var property in _valuesStart ) {

				var start = _valuesStart[ property ];
				var end = _valuesEnd[ property ];

				if ( end instanceof Array ) {

					_object[ property ] = _interpolationFunction( end, value );

				} else {

					_object[ property ] = start + ( end - start ) * value;

				}
			}

			if (_onUpdateCallback) {

				_onUpdateCallback.call( _object, value );
			}

			if ( elapsed == 1 ) {

				if ( _onCompleteCallback !== null ) {

					_onCompleteCallback.call( _object );
				}

				if ( _chainedTween !== null ) {

					_chainedTween.start();
				}

				return false;

			}

			return true;
		};

	}; // end TextBehavior

	/*  @private Simple type-checking functions */
	var Type = {

		N : 'number', S : 'string', O : 'object', A :'array', B : 'boolean', R : 'regexp', F : 'function',

		// From: http://javascriptweblog.wordpress.com/2011/08/08/fixing-the-javascript-typeof-operator/
		get : function(obj) {

			if (typeof obj == 'undefined') return null;
			return ({}).toString.call(obj).match(/\s([a-zA-Z]+)/)[1].toLowerCase();
		},

		// Returns true if the object is of type 'type', otherwise false

		is : function(obj,type) {

			return Type.get(obj) === type;
		},

		// Throws TypeError if not the correct type, else returns true
		ok : function(obj,type) {

			if (Type.get(obj) != type) {

				throw TypeError('Expected '+(type ? type.toUpperCase() : type+'') +
                    ", but received "+(obj ? Type.get(obj).toUpperCase() : obj+''));
			}

			return true;
		}

	},  is = Type.is, ok = Type.ok; // alias

	var SP = ' ', E = '', N = Type.N, S = Type.S, O = Type.O,
		A = Type.A, B = Type.B, R = Type.R, F = Type.F;


	//////////////////////////////////////////////////////////////////////
	//  RiText
	//////////////////////////////////////////////////////////////////////

	/* Returns the current graphics context, either a canvas 2d-context or ProcessingJS instance */
	RiText._graphics = function() {

		return RiText.renderer ? RiText.renderer._getGraphics() : null;
	};

	RiText.loop = function(callbackFun, fps) {   // TODO: REMOVE?

		var a = arguments, type,
			g = RiText.renderer,
			an = RiText._animator,
			callback = (typeof window != 'undefined') ? window.draw : null;

		if (g._type() === 'Processing') return; // let P5 do its own loop

		if (an.loopStarted) return;

		switch (a.length) {

			case 1:

				if (a[0]) {
					type = Type.get(a[0]);
					if (type == F) {
						callback = a[0];
					}
					else if (type == N) {
						an.targetFPS = a[0];
					}
				}
				break;

			case 2:

				if (a[0]) {

					type = Type.get(a[0]);
					if (type == F) {
						callback = a[0];
					}
					type = Type.get(a[1]);
					if (type == N) {
						an.targetFPS = a[1];
					}
				}
				break;
		}

		an.timeSinceLastFPS = Date.now(), an.framesSinceLastFPS = 0;
		var mps =  1E3 / an.targetFPS;

		if (callback && !an.callbackDisabled && window) {

			an.loopId = window.setInterval(function() {

				try {

					callback();

					var sec = (Date.now() - an.timeSinceLastFPS) / 1E3;
					var fps = ++an.framesSinceLastFPS / sec;

					if (sec > 0.5) {

						an.timeSinceLastFPS = Date.now();
						an.framesSinceLastFPS = 0;
						an.actualFPS = fps;
					}
					an.frameCount++;

				} catch(ex) {

					if (!an.callbackDisabled) {
						warn("Unable to invoke callback: " + callback);
						an.callbackDisabled = true;
					}

					window.clearInterval(an.loopId);
					console.trace(this);
					throw ex;
				}

			}, mps);

			an.isLooping = true;
			an.loopStarted = true;
		}
    };


	RiText.randomColor = function(min,max,includeAlpha) {

		min = min || 0, max = max || 256;
		var col = [RiText.random(min,max),RiText.random(min,max),RiText.random(min,max)];
		if (includeAlpha) col.push(RiText.random(min,max));
		return col;
	};

	RiText.random = function() {

		return RiTa.random.apply(this ,arguments);
	};

	RiText.picked = function(x, y) {

	  var hits = [];
	  for (var i = 0; i < RiText.instances.length; i++)
	  {
		var rt = RiText.instances[i];
		rt.contains(x, y) && hits.push(rt);
	  }
	  return hits;
	};

	RiText._disposeOne = function(toDelete) {

		var items = RiText.instances;

		while (items.indexOf(toDelete) !== -1) {
			items.splice(items.indexOf(toDelete), 1);
		}

		if (toDelete) {

			delete(toDelete.rs);
			toDelete = {};
			toDelete._rs = {};
		}
	};

	RiText._disposeArray = function(toDelete) {

		for ( var i = 0; i < toDelete.length; i++) {

			RiText._disposeOne(toDelete[i]);
		}

		toDelete = [];
	};

	RiText.dispose = function(toDelete) {

	   is(toDelete,A) && RiText._disposeArray(toDelete);
	   is(toDelete,O) && RiText._disposeOne(toDelete);
	};

	RiText.disposeAll = function() {

		if (arguments.length) {

			RiText.dispose(arguments[0]);
		}
		else {

			RiText._disposeArray(RiText.instances);
			RiText.instances = [];
		}
	};

	RiText.createWords = function(txt, x, y, w, h, fontObj, leading) {

		return RiText._createRiTexts(txt, x, y, w, h, fontObj, leading, RiText.prototype.splitWords);
 	};

	RiText.createLetters = function(txt, x, y, w, h, fontObj, leading) {

		return RiText._createRiTexts(txt, x, y, w, h, fontObj, leading, RiText.prototype.splitLetters);
	};

	RiText.defaultFontSize = function(size) {

		if (!arguments.length)
			return RiText.defaults.fontSize;

    	if (RiText.defaults.fontSize != size) {

			RiText.defaults.fontSize = size;
			RiText.defaults._font = null;
		}
	};

	RiText.defaultFont = function(font, size) {

		var a = arguments;

		if (a.length > 1 && !Number(a[1])) a = [a[0]]; // if 0 or undefined or NaN, ignore.

		if (a.length == 1) { // 1-arg

			// Case:  RiText.defaultFont(name);
			if (is(a[0],O)) {

				if (isNode() && a[0].widths) {// use no-op

					RiText.renderer._textFont(a[0]);
				}

				if (a[0].name)
					RiText.defaults.fontFamily = a[0].name;

			  	RiText.defaults._font = a[0];
			}

			// Case:  RiText.defaultFont(name);
			if (is(a[0],S)) {

				if (/\.vlw$/.test(a[0])) {
					warn(".vlw fonts not supported in RiTaJS! Ignoring: '"+a[0]+"'");
					a[0] = RiText.defaults.fontFamily;
				}

				RiText.defaults.fontFamily = a[0];
				RiText.defaults._font = RiText.renderer._createFont(a[0], RiText.defaults.fontSize);
			}
		}

		// Case: RiText.defaultFont(name, size);
		else if (a.length > 1) { // > 1 args

		 	// Case: RiText.defaultFont(name, size);
			is(a[0],O) && (a[0] = a[0].name);// if an obj, grab the name

			if (is(a[0],S)) {

			  	RiText.defaults.fontFamily = a[0];
			  	RiText.defaults._font = RiText.renderer._createFont(a[0], a[1]);
			}
			else {

				err("Unexpected type: "+(typeof a[0]));
			}
		}

		// RiText.defaultFont();
		else if (a.length === 0 && !RiText.defaults._font) { // 0-args

			// TODO: What if defaults.fontSize has changed since defaults.font was created?
			RiText.defaults._font = isNode() ? RiText.defaults.metrics
				: RiText.renderer._createFont(RiText.defaults.fontFamily, RiText.defaults.fontSize);
		}

		return RiText.defaults._font;
	};

	/*
	 * Returns json-formatted string representing the font metrics for the default font,
	 *  with the following fields: { name, size, ascent, descent, widths }
	 *
	 * @param chars (optional) array or string, characters for which widths should be calculated
	 */
	RiText._fontMetrics = function(chars) {

		var i, j, c, gwidths={}, pf=RiText.defaultFont();

		if (!(chars && chars.length)) {
	    	chars = [];
	    	for (j = 33; j < 126; j++) {
	      		chars.push(String.fromCharCode(j));
			}
	    }

	    if (is(chars, S)) chars = chars.split(E); // split into array

		for (i = 0; i < chars.length; i++) {
	      //log(c +" -> "+pf.measureTextWidth(c))
	      c = chars[i];
	      gwidths[c] = pf.measureTextWidth(c);
	    }

	    gwidths[SP] = pf.measureTextWidth(SP);

	    return  { name: pf.name, size: pf.size,
	    	ascent: pf.ascent,  descent: pf.descent, widths: gwidths
	   	};
	};

	RiText._createFont = function(fontName, fontSize) {

		if (!fontName) err('RiText._createFont requires fontName');

		fontSize = fontSize || RiText.defaults.fontSize;

		return RiText.renderer._createFont(fontName, fontSize);
	};

	RiText.drawAll = function(array) {

		if (arguments.length == 1 && is(array,A)) {
			for ( var i = 0; i < array.length; i++)
				array[i] && array[i].draw();
		}
		else {
			for ( var j = 0; j < RiText.instances.length; j++)
				RiText.instances[j] && RiText.instances[j].draw();
		}
	};

	RiText.defaultFill = function(r, g, b, a) {

		if (arguments.length) {
			RiText.defaults.fill = parseColor.apply(null, arguments);
		}
		return toColArr(RiText.defaults.fill);
	};


		// private statics ///////////////////////////////////////////////////////////////

	RiText._layoutArray = function(lines, x, y, w, h, pfont, leading) {

		if (!is(arguments[0], A)) { // ignore first (PApplet) argument
	    	var a = arguments;
	    	lines = a[1], x = a[2], y = a[3], w = a[4], h = a[5], pfont = a[6], leading = a[7];
	    }

		var ritexts = [];
	    if (!lines || !lines.length) return ritexts;

	    for (var i = 0; i < lines.length; i++)
	      ritexts.push(RiText(lines[i], x+1, y).font(pfont));

	    return RiText._constrainLines(ritexts, y, h, leading);
	};

	RiText._constrainLines = function(ritexts, y, h, leading) {

		var ascent = ritexts[0].textAscent();
	    var descent = ritexts[0].textDescent();
	    var lastOk, next, maxY = y + h, currentY = y + ascent + 1;

	    // set y-pos for those that fit
	    for (lastOk = 0; lastOk < ritexts.length; lastOk++)
	    {
	      next = ritexts[lastOk];
	      next.y = currentY;
	      //log(lastOk+") "+currentY);
	      if (!RiText._withinBoundsY(currentY, leading, maxY, descent))
	        break;
	      currentY += leading;
	    }

	    var toKill = ritexts.slice(lastOk+1);

	    // and delete the rest
	    RiText.dispose(toKill);

	 	var result = ritexts.slice(0, lastOk);

	 	//log("lastOk="+lastOk+"/"+ritexts.length + " toKill="+toKill.length+" result="+result.length);

	 	return result;
	};

	RiText.resetDefaults = function() {

		RiText.defaults = RiText._defaults;
	};

	RiText.boundingBox = function(ritexts) { // add-to-api?

		var rts = ritexts,
			minX=Number.MAX_VALUE,
			maxX=-Number.MAX_VALUE,
			minY=Number.MAX_VALUE,
			maxY=-Number.MAX_VALUE;

		if (!is(rts, A)) rts = [ ritexts ];

		for (var i = 0, j = rts.length; i < j; i++) {

			var bb = rts[i].boundingBox();
			if (bb[0] < minX) minX = bb[0];
			if (bb[1] < minY) minY = bb[1];
			if (bb[0]+bb[2] > maxX) maxX = bb[0]+bb[2];
			if (bb[1]+bb[3] > maxY) maxY = bb[1]+bb[3];
		}

		return [ minX, minY, maxX-minX, maxY-minY ];
	};

	RiText.createLines = function(txt, x, y, w, h, pfont, leading) {

		var a = arguments, t = Type.get(a[0]), g = RiText.renderer;

		if (t != S && t != A) { // ignore first (PApplet/window) argument, shift

			txt = a[1], x = a[2], y = a[3], w = a[4],
			h = a[5], pfont = a[6], leading = a[7];
		}

		if (!txt || !txt.length) return [];

		h = (h && h > 0) ? h : Number.MAX_VALUE;
		pfont = pfont || RiText.defaultFont();

		leading = leading || pfont.size * RiText.defaults.leadingFactor;

		if (is(txt, A)) {

			if (!w) { // no width, so respect the line-breaks in the array

				return RiText._layoutArray(txt, x, y, w, h, pfont, leading);
			}
			txt = txt.join(SP); // else join into single string
		}

		if (!w && g && g.width) w = g.width - x; // TODO: how to get width of sketch in Node renderer?

		if (w < 0) w = Number.MAX_VALUE;

		var ascent, descent, startX = x, currentX, currentY,
			rlines = [], sb = E, words = [], next, yPos = 0, rt = null,
			newParagraph = false, forceBreak = false, firstLine = true,
			maxW = x + w, maxH = y + h;

		// for ascent/descent in canvas renderer
		if (!g || !g.p) rt = RiText(SP, 0, 0, pfont);

		// remove line breaks & add spaces around html
		txt = txt.replace(/&gt;/g, '>');
		txt = txt.replace(/&lt;/g, '<');
		txt = txt.replace(/ ?(<[^>]+>) ?/g, " $1 ");
		txt = txt.replace(/[\r\n]/g, SP);

		// split into reversed array of words
		RiText._addToStack(txt, words);
		if (!words.length)
			return RiText.EMPTY_ARRAY;

		//log("txt.len="+txt.length+" x="+x+" y="+y+" w="+w+" h="+h+" lead="+leading);log(pfont);

		g._textFont(pfont);

		// for ascent & descent
		ascent = g.p ? g.p.textAscent() : g._textAscent(rt, true);
		descent = g.p ? g.p.textDescent() : g._textDescent(rt, true);

		//log(g._type()+'.ascent/descent='+ascent+'/'+descent);

		currentY = y + ascent;

		if (RiText.defaults.indentFirstParagraph)
			startX += RiText.defaults.paragraphIndent;

		while (words.length > 0) {

			next = words.pop();

			if (!next.length) continue;

			// check for HTML-style tags
			if (/<[^>]+>/.test(next)) {
				if (next == RiText.NON_BREAKING_SPACE) {
					sb += SP;
				}
				else if (next == RiText.PARAGRAPH_BREAK) {
					newParagraph = true;
				}
				else if (next == RiText.LINE_BREAK) {
					forceBreak = true;
				}
				continue;
			}

			// re-calculate our X position
			currentX = startX + g._textWidth(pfont, sb + next);

			// check it against the line-width
			if (!newParagraph && !forceBreak && currentX < maxW) {
				sb += next + SP;
				// add-word
			}
			else {

				// check yPosition to see if its ok for another line?
				if (RiText._withinBoundsY(currentY, leading, maxH, descent, firstLine)) {

					yPos = firstLine ? currentY : currentY + leading;
					rt = RiText._newRiTextLine(sb, pfont, startX, yPos);
					rlines.push(rt);

					currentY = newParagraph ? rt.y + RiText.defaults.paragraphLeading : rt.y;
					startX = x;
					// reset

					if (newParagraph)
						startX += RiText.defaults.paragraphIndent;

					sb = next + SP;
					// reset with next word

					newParagraph = false;
					forceBreak = false;
					firstLine = false;
				}
				else {

					// we've run out of y-space, break the loop and finish
					words.push(next);
					// not needed
					break;
				}
			}
		}

		// check if leftover words can make a new line
		if (RiText._withinBoundsY(currentY, leading, maxH, descent, firstLine)) {

			// TODO: what if there is are tags in here -- is it possible?)
			yPos = firstLine ? currentY : currentY + leading;
			rlines.push(RiText._newRiTextLine(sb, pfont, x, yPos));
			sb = E;
		}
		else {

			RiText._addToStack(sb, words);
			// save for next (not needed?)
		}

		return rlines;
	};

	RiText._withinBoundsY = function(currentY, leading, maxY, descent, firstLine) {

    	if (!firstLine)
    		return currentY + leading <= maxY - descent;
		return currentY <= maxY - descent;
  	};

	RiText._addToStack = function(txt, words) {

		var tmp = txt.split(SP);
		for ( var i = tmp.length - 1; i >= 0; i--)
			words.push(tmp[i]);
	};

	RiText._newRiTextLine = function(s, pf, xPos, nextY) {

	    // strip trailing spaces
        while (s && s.length > 0 && endsWith(s, SP))
            s = s.substring(0, s.length - 1);

        //s = s.replace(/ *$/,''); TODO: use RE instead

		return new RiText(s, xPos, nextY, pf);
	};

	RiText._createRiTexts = function(txt, x, y, w, h, fontObj, lead, splitFun) {

		var rlines = RiText.createLines(txt, x, y, w, h, fontObj, lead);
		if (!rlines || rlines.length < 1) return [];

		var result = [];
		var font = rlines[0].font();
		for (var i = 0; i < rlines.length; i++) {

			var rts = splitFun.call(rlines[i]);
			for (var j = 0; j < rts.length; j++) {

				result.push(rts[j].font(fontObj));
			}

			RiText.dispose(rlines[i]);
		}

		return result;
	};

	// Returns the pixel x-offset for the word at 'wordIdx'
	RiText._wordOffsetFor = function(rt, words, wordIdx) {

		if (wordIdx < 0 || wordIdx >= words.length)
			throw new Error("Bad wordIdx=" + wordIdx + " for " + words);

		rt.g._push();

		var xPos = rt.x;

		if (wordIdx > 0) {

			var pre = words.slice(0, wordIdx);
			var preStr = '';
			for ( var i = 0; i < pre.length; i++) {
				preStr += pre[i] + ' ';
			}

			var tw = rt.g._textWidth(rt._font, preStr);

			//log("x="+xPos+" pre='"+preStr+"' tw=" + tw);

			switch (rt._alignment) {
				case RiTa.LEFT:
					xPos = rt.x + tw;
					break;
				case RiTa.RIGHT:
					xPos = rt.x - tw;
					break;
				case RiTa.CENTER:
					warn("TODO: test center-align here");
					xPos = rt.x; // ?
					break;
			}
		}
		rt.g._pop();

		return xPos;
	};

	RiText._handleLeading = function(fontObj, rts, startY) {

		if(!rts || !rts.length) return;

		fontObj = fontObj || RiText._getDefaultFont();

		//log('handleLeading: '+fontObj.leading);

		var nextHeight = startY;
		rts[0].font(fontObj);
		for(var i = 0; i < rts.length; i++) {

			//if(fontObj) rts[i].font(fontObj); // set the font
			rts[i].y = nextHeight; // adjust y-pos
			nextHeight += fontObj.leading;
		}

		return rts;
	};

	// TODO: test this font default across all platforms and browsers

	RiText._getDefaultFont = function() {

		//log("RiText._getDefaultFont1: "+RiText.defaults._font+","+RiText.defaults.fontFamily+","+RiText.defaults.fontSize);

		RiText.defaults._font = RiText.defaults._font ||
			RiText.renderer._createFont(RiText.defaults.fontFamily, RiText.defaults.fontSize);

		return RiText.defaults._font;
	};

	// PUBLIC statics   ///////////////////////////////////////////

  RiText.instances = [];
	RiText.defaults = RiText._defaults;

	RiText.prototype = {

		init : function(text, x, y, font) {

			var bbs, screenH, args;

			if (!RiText.renderer)
				err("No graphics context, RiText unavailable");

			this._color = {
				r : RiText.defaults.fill.r,
				g : RiText.defaults.fill.g,
				b : RiText.defaults.fill.b,
				a : RiText.defaults.fill.a
			};

			this._boundingStrokeWeight = RiText.defaults.boundingStrokeWeight;

			bbs = RiText.defaults.boundingStroke;
			this._boundingStroke = {
				r : (bbs && bbs.r) || this._color.r,
				g : (bbs && bbs.g) || this._color.g,
				b : (bbs && bbs.b) || this._color.b,
				a : this._color.a
			};

			this._boundingFill = RiText.defaults.boundingFill;

			this._showBounds = RiText.defaults.showBounds;
			this._motionType = RiText.defaults.motionType;
			this._alignment = RiText.defaults.alignment;

			this._rotateX = RiText.defaults.rotateX;
			this._rotateY = RiText.defaults.rotateY;
			this._rotateZ = RiText.defaults.rotateZ;

			this._scaleX = RiText.defaults.scaleX;
			this._scaleY = RiText.defaults.scaleY;
			this._scaleZ = 1;

			this._behaviors = [];

			this.g = RiText.renderer;

			// handle the arguments
			args = this._initArgs.apply(this, arguments);

			this.font(args[3]);
			this.text(args[0]);

			// center by default
			this.x = is(args[1], N) ? args[1] : this._screenCenterX();
			screenH =  (this.g && this.g.p) ? (this.g.p.height / 2) : 50;  // TODO: what to do for Node?
			this.y = is(args[2],N) ? args[2] : screenH + (this.textHeight() / 2.0) ;
			this.z = 0;

			//log('RiText: '+this._rs._text +"("+this.x+","+this.y+")"+" / "+ this._font.name);

			RiText.instances.push(this);
		},

		_initArgs : function() {

			var a = arguments, t = Type.get(a[0]);

			//console.error("a[0]="+t+" a.length="+a.length+" type="+t+" analyze="+typeof a[0].text);

			if (a.length && (t===O || t==='global' || t==='window') && typeof a[0].analyze != F) {

				// recurse, ignore 'this'
				var shifted = Array.prototype.slice.call(a, 1);

				return this._initArgs.apply(this, shifted);
			}

			var parsed = [E, null, null, null];
			if (a.length) {

				if (is(a[0], S))   // String
					parsed[0] = a[0];

				else if (is(a[0], O) && typeof a[0].text == F)
					parsed[0] = a[0].text(); // RiString

				else if (is(a[0], N)) // Number
					parsed[0] = String.fromCharCode(a[0]);

				else if (!RiTa.SILENT)
				  console.error("Unexpected arg in RiText("+a[0]+" [type="+(typeof a[0])+"])");
			}

			if (a.length > 1) parsed[1] = a[1];
			if (a.length > 2) parsed[2] = a[2];
			if (a.length > 3) parsed[3] = a[3];

			return parsed;
		},

		_screenCenterX : function() {  // to match java

			var scx = (this.g && this.g.p) ? this.g.p.width / 2.0 : -1; // TODO: what to do for Node?

			if (this._alignment == RiTa.LEFT)
	  			scx -= (this.textWidth() / 2.0);
			else if (this._alignment == RiTa.RIGHT)
	  			scx += (this.textWidth() / 2.0);

	  		return scx;
		},

		get : function(fn) {

			return this._rs.get(fn);
		},

		features : function() {

		   return this._rs.features();
		},

		draw : function() {

		  this._update()._render();
		  if (this.textToCopy)
			  this.textToCopy.draw();
		  return this;
		},

		_update : function() {

			if (this._behaviors.length)
				this._updateBehaviors();
			return this;
		},

		_render : function() {

			var g = this.g;

			if (!g) err('no-renderer');

			if (this._rs && this._rs.length) {

				g._push();

				var bb = g._getBoundingBox(this); // cache this!

				g._translate(this.x, this.y);
				g._translate(bb.width/2, bb.height/-4);
				g._rotate(this._rotateZ);
				g._translate(bb.width/-2, bb.height/4);
				g._scale(this._scaleX, this._scaleY, this._scaleZ);

				// Set color
				g._fill(this._color.r, this._color.g, this._color.b, this._color.a);

				// Set font params
				g._textFont(this._font);
				g._textAlign(this._alignment);

				// Draw text
				g._text(this._rs._text, 0, 0);

				// And the bounding box
				if (this._showBounds) {

					// push/popStyle
					if (!this._boundingFill)// &&this._boundingFill.r < 0 && this._boundingFill.g < 0 && this._boundingFill.b < 0)
						g._noFill();
					else
						g._fill(this._boundingFill.r, this._boundingFill.g, this._boundingFill.b, this._color.a);

					g._stroke(this._boundingStroke.r, this._boundingStroke.g,
							this._boundingStroke.b, this._color.a);

					g._strokeWeight(this._boundingStrokeWeight);

					// shift bounds based on alignment
					switch(this._alignment) { // this should be part of updateBoundingBox() (see Java)

						case RiTa.RIGHT:
							g._translate(-bb.width,0);
							break;
						case RiTa.CENTER:
							g._translate(-bb.width/2,0);
							break;
					}
					g._rect(0, bb.y, bb.width, bb.height); // what??
				}

				g._pop();
			}

			return this;
		},

		///////////////////////////////// Text Behaviors ////////////////////////////////////

		motionType : function (motionType) {
			if (arguments.length) {
				this._motionType = motionType;
				return this;
			}
			return this._motionType;
		},

		fadeIn : function(seconds, delay, callback) {

			return this.colorTo(toColArr(this._color, 255),
				seconds, delay, null, RiTa.FADE_IN, false);
		},

		fadeOut : function(seconds, delay, callback, destroyOnComplete) {

			destroyOnComplete = destroyOnComplete || false;

			return this.colorTo(toColArr(this._color, 0),
				seconds, delay, null, RiTa.FADE_OUT, destroyOnComplete);
		},

		scaleTo : function(theScale, seconds, delay, callback) {

			var rt = this;
			delay = delay || 0;
			seconds = seconds || 1.0;

			var id = setTimeout(function() {

				var tb = new TextBehavior(rt)
					.to( { _scaleX: theScale, _scaleY: theScale }, seconds*1000)
					.easing(rt._motionType)
					.onUpdate( function () {
						rt._scaleX = this._scaleX;
						rt._scaleY = this._scaleY;
					})
					//.delay(delay*1000)
					.onComplete(
						function () {
						   RiTaEvent(rt, RiTa.SCALE_TO)._fire(callback);
					});

				tb.start();

			}, delay*1000);

			return id;
		},

		rotateTo : function(angleInRadians, seconds, delay, callback) {

			var rt = this;
			delay = delay || 0;
			seconds = seconds || 1.0;

			var id = setTimeout(function() {

				var tb = new TextBehavior(rt)
					.to( { _rotateZ: angleInRadians  }, seconds*1000)
					.easing(rt._motionType)
					.onUpdate( function () {

						rt._rotateZ = this._rotateZ;
					})
					//.delay(delay*1000)
					.onComplete(
						function () {
						   RiTaEvent(rt, RiTa.ROTATE_TO)._fire(callback);
					});

				tb.start();

			}, delay*1000);

			return id;
		},

		textTo: function(newText, seconds, startTime, callback) {

			// grab the start alpha if needed
			var c = this._color, startAlpha = 0, endAlpha = c.a;

			if (this.textToCopy) {

				startAlpha = this.textToCopy.alpha();
				RiText.dispose(this.textToCopy);
			}

			// use the copy to fade out
			this.textToCopy = this.copy();
			this.textToCopy.colorTo(toColArr(c,0),
				 seconds/2.0, startTime, null, RiTa.INTERNAL, true); // fade-out

			RiText.dispose(this.textToCopy.textToCopy);	// no turtles [js-only]

			// and use 'this' to fade in
			this.text(newText).alpha(startAlpha);

			return this.colorTo(toColArr(c, endAlpha), seconds * 0.95,
				startTime, callback, RiTa.TEXT_TO, false);
		},

		colorTo : function(colors, seconds, delay, callback, _type, _destroyOnComplete) {

			// DH: omitting last 2 args from docs as they are private

			if (!is(colors,A))  err('arg#1 to colorTo() must be an array');

			//log(colors[0], g: colors[1], b: colors[2], a: colors[3], seconds);

			delay = delay || 0;
			seconds = seconds || 1.0;
			colors = parseColor.apply(this, colors);
			_type = _type || RiTa.COLOR_TO;

			var rt = this, id = setTimeout(function() {

				new TextBehavior(rt, rt._color)
					.to( { r: colors.r, g: colors.g, b: colors.b, a: colors.a }, seconds*1000)
					.easing(rt._motionType)
					.onUpdate( function () {
					   rt._color.r = this.r;
					   rt._color.g = this.g;
					   rt._color.b = this.b;
					   rt._color.a = this.a;
					   rt._boundingStroke.a = this.a;
					})
					.onComplete(

						function () {

							if (_type != RiTa.INTERNAL)
								RiTaEvent(rt, _type)._fire(callback);

							if (_destroyOnComplete) {
								RiText.dispose(rt);

							}
						})
					.start();

			}, delay*1000);

			return id;
		},

		moveTo : function(newX,newY,seconds,delay,callback) {

			var rt = this;

			delay = delay || 0;
			seconds = seconds || 1.0;

			var id = setTimeout(function() {

				new TextBehavior(rt)
					.to( { x: newX, y: newY }, seconds*1000)
					.easing(rt._motionType)
					.onUpdate( function () {
						rt.x = this.x ;
						rt.y = this.y ;
					})
					.delay(delay).onComplete(
						function () {
							RiTaEvent(rt, RiTa.MOVE_TO)._fire(callback);
						})
					.start();

			}, delay*1000);

			return id;
		},

		analyze : function() {

			this._rs.analyze();
			return this;
		},

		text : function(txt) {

			if (arguments.length == 1) {

				var theType = Type.get(txt);

				if (theType == N) {
					txt = String.fromCharCode(txt);
				}
				else if (theType == O && typeof txt.text == F) {
					txt = txt.text();
				}
				this._rs = (this._rs) ? this._rs.text(txt) : new RiString(txt);

				return this;
			}

			return this._rs._text;
		},

		match : function(pattern) {

			return this._rs.match(pattern);

		},

		charAt : function(index) {

			return this._rs.charAt(index);

		},

		concat : function(riText) {

			return this._rs._text.concat(riText.text());

		},

		containsWord : function(text) {

			return this._rs.indexOf(text) > -1;

		},

		endsWith : function(substr) {

			return endsWith(this._rs._text, substr);

		},

		equals : function(RiText) {

			return RiText._rs._text === this._rs._text;

		},

		equalsIgnoreCase : function(str) {

			if (typeof str === S) {

				return str.toLowerCase() === this._rs._text.toLowerCase();
			}
			else {

				return str.text().toLowerCase() === this._rs._text.toLowerCase();
			}

		},

		indexOf : function(searchstring, start) {

			return this._rs._text.indexOf(searchstring, start);

		},

		lastIndexOf : function(searchstring, start) {

			return this._rs._text.lastIndexOf(searchstring, start);

		},

		length : function() {

			return this._rs._text.length;

		},

		pos : function() {

			var words = RiTa.tokenize((this._rs._text)); // was getPlaintext()
				var tags = RiTa.getPosTags(words);

				for ( var i = 0, l = tags.length; i < l; i++) {
					if (!(typeof tags[i] === S && tags[i].length > 0))
						err("RiText: can't parse pos for:" + words[i]);
			}

			return tags;

		},

		posAt : function(index) {

			var tags = this._rs.pos();

			if (!tags || !tags.length || index < 0 || index >= tags.length)
				return E;

			return tags[index];

		},

		insertChar : function(ind, theChar) {

			this._rs.insertChar.apply(this._rs, arguments);
			return this;

		},

		removeChar : function(ind) {

			this._rs.removeChar.apply(this._rs, arguments);
			return this;

		},

		replaceChar : function(idx, replaceWith) {

			this._rs.replaceChar.apply(this._rs, arguments);
			return this;
		},

		replaceFirst : function(regex, replaceWith) {

			this._rs.replaceFirst.apply(this._rs, arguments);
			return this;
		},

		replaceAll : function(pattern, replacement) {

			this._rs.replaceAll.apply(this._rs, arguments);
			return this;
		},

		replaceWord : function(wordIdx, newWord) {

			this._rs.replaceWord.apply(this._rs, arguments);
			return this; // TODO: check that all RiText methods use the delegate
						//  (like above) for methods that exist in RiString
		},

		removeWord : function(wordIdx) {

			this._rs.removeWord.apply(this._rs, arguments);
			return this;
		},

		insertWord : function(wordIdx, newWord) {

			this._rs.insertWord.apply(this._rs, arguments);
			return this;
		},

		slice : function(begin, end) {

			var res = this._rs._text.slice(begin, end) || E;
			return this._rs.text(res);

		},

		startsWith : function(substr) {

			return this._rs.indexOf(substr) === 0;
		},

		substring : function(from, to) {

			return this._rs.text(this._rs._text.substring(from, to));
		},

		substr : function(start, length) {

			var res = this._rs._text.substr(start, length);
			return this._rs.text(res);
		},

		toLowerCase : function() {

			return this._rs.text(this._rs._text.toLowerCase());
		},

		toUpperCase : function() {

			return this._rs.text(this._rs._text.toUpperCase());
		},

		trim : function() {

			return this._rs.text(trim(this._rs._text));
		},

		wordAt : function(index) {

			var words = RiTa.tokenize((this._rs._text));
			if (index < 0 || index >= words.length)
				return E;
			return words[index];
		},

		wordCount : function() {

			if (!this._rs._text.length) return 0;
			return this.words().length;
		},

		words : function() {

			return RiTa.tokenize(this._rs._text);
		},

		distanceTo : function(a,b) {

	      var p2x, p2y, p2, p1 = this.center();

	      if (a.length == 1 && is(a.center,F)) {
		     p2 = a.center();
		     p2x = p2[0];
		     p2y = p2[1];
		  }
		  else {
		  	 p2x = a;
		     p2y = b;
		  }

		  return RiTa.distance(p1[0], p1[1],  p2x,  p2y);
		},

		center : function() {

			var bb = this.boundingBox(); // note: this is different than RiTa (TODO: sync)
			return [ bb[0] + bb[2]/2.0, bb[1]  + bb[3]/2.0 ];
		},

		splitWords : function(regex) {

			regex = regex || SP;

			(typeof regex == S) && (regex = new RegExp(regex));

			var l = [];
			var txt = this._rs._text;
			var words = txt.split(regex);

			for ( var i = 0; i < words.length; i++) {
				if (words[i].length < 1) continue;
				var tmp = this.copy();
				tmp.text(words[i]);
				var mx = RiText._wordOffsetFor(this, words, i);
				tmp.position(mx, this.y);
				l.push(tmp);
			}

			return l;
		},

		splitLetters : function() {

			var l = [];
			var chars = [];
			var txt = this.text();
			var len = txt.length;
			for (var t = 0; t < len; t++) {
				chars[t] = txt.charAt(t);
			}

			for ( var i = 0; i < chars.length; i++) {
				if (chars[i] == SP) continue;
				var tmp = this.copy();
				tmp.text(chars[i]);
				var mx = this.charOffset(i);
				tmp.position(mx, this.y);
				l.push(tmp);
			}

			return l;
		},

		contains : function(mx, my) {

		   var bb = this.boundingBox(true);
		   bb[0] += this.x;
		   bb[1] += this.y;

		   return (!(mx<bb[0] || mx > bb[0]+bb[2] || my < bb[1] || my > bb[1]+bb[3]));
		},

		copy : function() {

			var c = new RiText(this.text(), this.x, this.y, this._font);
			c.fill(this._color.r, this._color.g, this._color.b, this._color.a);

			for (var prop in this) {
				if (typeof this[prop] ==  F || typeof this[prop] ==  O)
					continue;
				c[prop] = this[prop];
			}

			return c;
		},

		align : function(align) {
			if (arguments.length) {
				this._alignment = align;
				return this;
			}
			return this._alignment;
		},

		font : function(font, size) { // TODO: cases for when arg1 is object & string

			var a = arguments;

			if (a.length == 1) {

				if (is(font,S)) {

					if (/\.vlw$/.test(font)) {

						warn(".vlw fonts not supported in RiTaJS! Ignoring: '"+font+"'");
						this._font = RiText._getDefaultFont();
						return this;
					}

					this._font = RiText.renderer._createFont(font, RiText.defaults.fontSize);
					return this;
				}

				this._font = font || RiText._getDefaultFont();
				return this;
			}
			else if (a.length == 2) {

				this._font = RiText.renderer._createFont(a[0], a[1]);
				return this;
			}

			return this._font;
		},

		showBounds : function(trueOrFalse) {

		   if (arguments.length == 1) {

			   this._showBounds = trueOrFalse;
			   return this;
		   }

		   return this._showBounds;
		},

		fill : function(cr, cg, cb, ca) {

			if (arguments.length === 0)
				return this._color;
			this._color = parseColor.apply(this, arguments);
			return this;
		},

		boundingFill : function(cr, cg, cb, ca) {

 			if (arguments.length === 0)
				return this._boundingFill;
			this._boundingFill = parseColor.apply(this, arguments);
			return this;
		},

		boundingStroke : function(cr, cg, cb, ca) {

 			if (arguments.length === 0)
				return this._boundingStroke;
			this._boundingStroke = parseColor.apply(this, arguments);
			return this;
		},

		isVisible : function() {

			if (arguments.length)
				 err('isVisible() takes no arguments');

			return this._color.a > 0;
		},

		alpha : function(a) {
			if (arguments.length==1) {
				this._color.a = a;
				return this;
			}
			else return this._color.a;
		},

		position : function(x, y) {

			//TODO: add Z

			if (!arguments.length)
				return [ this.x, this.y ];
			this.x = x;
			this.y = y;
			return this;
		},

		rotate : function(rotate) {

			//TODO: add X,Y ??
		  if (!arguments.length)
			  return [this._rotateZ];
		  this._rotateZ = rotate;
		  return this;
		},

		scale : function(theScaleX, theScaleY) {

			if (!arguments.length) return { x:this._scaleX, y:this._scaleY }; //TODO: add Z

			if (arguments.length == 1) theScaleY = theScaleX;

			this._scaleX = theScaleX;
			this._scaleY = theScaleY;

			return this;
		},

		charOffset : function(charIdx) {

			var theX = this.x;

			if (charIdx > 0) {

				var txt = this.text();

				var len = txt.length;
				if (charIdx > len) // -1?
				charIdx = len;

				var sub = txt.substring(0, charIdx);
				theX = this.x + this.g._textWidth(this._font, sub);
			}

			return theX;
		},

		wordOffset : function(wordIdx) {

			return RiText._wordOffsetFor(this, this.words(), wordIdx);
		},

		boundingBox : function(noTransform) { // argument is not part of api

			var g = this.g, bb = this.g._getBoundingBox(this);

			g._push(); // really, do we need all this here?

			// make function (here and in draw)
			g._translate(this.x, this.y);
			g._translate(bb.width/2, bb.height/-4);
			g._rotate(this._rotateZ);
			g._translate(bb.width/-2, bb.height/4);
			g._scale(this._scaleX, this._scaleY, this._scaleZ);

			// Set font params
			g._textFont(this._font);
			g._textAlign(this._alignment);

			if (!noTransform) {
				bb.x += this.x;
				bb.y += this.y;
				bb.width *= this._scaleX;
				bb.height *= this._scaleY;
			}
			g._pop();

			return [bb.x,bb.y,bb.width,bb.height];
		},

		textWidth : function() {

			return this.g._textWidth(this._font,this._rs._text);
		},

		textHeight : function() {

			return this.g._textHeight(this);
		},

		fontSize : function(f) {

			if (!arguments.length) {

				return this._font ? this._font.size : -1;
			}

			// DCH: changed:
			//return (arguments.length) ? this.scale( sz / this._font.size)
				//: (this._font.size * this._scaleX);

			if (this._font && (this._font.size == f))
				return this; // no-op

	    	var name = RiText.defaults.fontFamily;
	    	if (this._font && this._font.name)
	    		name = this._font.name;

	    	this.font(name, f);	// recreate	from name/sz

		    return this;
		},

		textAscent : function() {

			return this.g._textAscent(this);
		},

		textDescent : function() {

			return this.g._textDescent(this);
		},

		/*
		 * Adds a new text behavior to the object
		 * @returns {array}
		 */
		_addBehavior: function ( behavior ) {

			this._behaviors.push( behavior );

		},

		/*
		 * Returns the specified text behavior
		 * @param {number} the behavior id
		 */
		_getBehavior: function ( id ) {

			for (var i = 0; i < this._behaviors.length; i++) {
				if (this._behaviors[i].id == id)
					return this._behaviors[i].id;
			}

			return null;
		},

		stopBehavior: function ( id ) {

			var behavior = this._getBehavior(id);

			if (behavior) {
				var i = this._behaviors.indexOf(behavior);
				if ( i !== -1 ) {

					this._behaviors.splice( i, 1 );

				}
			}
			return this;
		},

		stopBehaviors: function () {

			this._behaviors = [];
			return this;
		},

		/*
		 * Updates existing text behaviors for the object
		 * @param {string} the behaviors
		 */
		_updateBehaviors: function (theTime) {

			var i = 0;
			var num = this._behaviors.length;
			var time = theTime || Date.now();

			while ( i < num ) {

				if (this._behaviors[i].update(time) ) {
					i++;

				} else {

					this._behaviors.splice(i, 1);
					num--;

				}
			}
		},

		toString : function() {

			var s =  (this._rs && this._rs._text) || 'undef';
			return '['+Math.round(this.x)+","+Math.round(this.y)+",'"+s+"']";
		}
	};

	var RiText_P5js = makeClass();

	RiText_P5js.prototype = {

		init : function(p5) {

			this.p = p5;
		},

		_getGraphics : function() {

			return this.p;
		},

		_push : function() {
			push();
			return this;
		},

		_pop : function() {
			pop();
			return this;
		},

		_textAlign : function(align) {
			textAlign(align);
			return this;
		},

		_scale : function(sx, sy) {
			scale(sx, sy);
			return this;
		},

		_translate : function(tx, ty) {
			translate(tx, ty);
			return this;
		},

		_rotate : function(zRot) {
			rotate(zRot);
			return this;
		},

		_text : function(str, x, y) {
			text(str, x, y);
			return this;
		},

		_fill : function(r,g,b,a) {
			fill(r,g,b,a);
			return this;
		},

		_stroke : function(r,g,b,a) {
			stroke(r,g,b,a);
			return this;
		},

		_noFill : function() {
			noFill();
			return this;
		},

		_noStroke : function() {
			noStroke();
			return this;
		},

		_strokeWeight : function(sw) {
			strokeWeight(sw);
			return this;
		},

		// actual creation: only called from RiText.createDefaultFont();!
		_createFont : function(fontName, fontSize) { // p5js

			textFont(fontName);
			textSize(fontSize);

			this.font = {
			  "name": fontName,
			  "size": fontSize
			  // add textAscent,textDescent,widths?
			};

			return this.font;
		},

		_rect : function(x,y,w,h) {

			rect(x,y,w,h);
			return this;
		},

		_textFont : function(fontObj) {

			if (!is(fontObj, O))
				err("_textFont takes object!");

			textFont(fontObj, fontObj.size);
		},

		_textSize : function(sz) {

			textSize(sz);
		},

		_textWidth : function(fontObj, str) {

			this._push();
			textFont(this.font);
			textSize(this.fontSize);
			var tw = textWidth(str);
			this._pop();
			//log(str+" -> "+tw);
			return tw;
		},

		_textHeight : function(rt) {

			this._push();
			var h = this._getBoundingBox(rt).height;
			this._pop();
			return h;
		},

		_textAscent : function(rt,ignoreContext) {

			ignoreContext = ignoreContext || false;

			if (!ignoreContext) {

			  this._push();
			  textFont(rt._font, rt._font.size);
			}

			var asc = textAscent();

			if (!ignoreContext) {

			  this._pop();
			}

			return asc;
		},

		_textDescent : function(rt) {

			this._push();
			textFont(rt._font, rt._font.size);
			var dsc = textDescent();
			this._pop();
			return dsc;
		},

		// TODO: what about scale?
		_getBoundingBox : function(rt) {

			this._push();
			textFont(rt._font);
			textSize(rt._font.size);

			var ascent  =   textAscent(),
				descent =   textDescent(),
				width   =   textWidth(rt.text());

			this._pop();

			return { x: 0, y: -ascent-1, width: width,
				height: (ascent+descent+1) };
		},
		_width : function() {

			throw Error('no width in node');
		},

		_type : function() {

			return "P5js";
		},

		toString : function() {

			return "RiText_"+this._type();
		}
	};

	var RiText_Node = makeClass();

	RiText_Node.prototype = {  // TODO: get rid of all no-op methods...

		init : function(metrics) {

			this.font = metrics;
		},

		_getGraphics : function() {

			warn("NodeRenderer._getGraphics() returning null graphics context!");
			return null;
		},

		_push : function() {
			// no-op
			return this;
		},

		_pop : function() {
			// no-op
			return this;
		},

		_textAlign : function(align) {
			// no-op
			return this;
		},

		_scale : function(sx, sy) {
			//warn("scale("+sx+","+sy+") not yet implemented");
		},

		_translate : function(tx, ty) {
			//warn("translate("+tx+","+ty+") not yet implemented");
		},

		_rotate : function(zRot) {
			//warn("rotate() not yet implemented");
		},

		_text : function(str, x, y) {
			// no-op
		},

		_fill : function(r,g,b,a) {
			// no-op
		},

		_stroke : function(r,g,b,a) {
			// no-op
		},

		_noFill : function() {
			// no-op
		},

		_strokeWeight : function() {
			// no-op
		},

		// actual creation: only called from RiText.createDefaultFont();!
		_createFont : function(fontName, fontSize) {

			return this.font;
		},

		_rect : function(x,y,w,h) {
			// no-op
		},

		_textFont : function(fontObj) {

			// TODO: apply one of the (cached?) fonts?
			this.font = fontObj;
		},

		_textWidth : function(fontObj, str) {

			var w = 0;
			var def = this.font.widths.i;
			if (str && str.length) {
				for (var i = 0; i < str.length; i++)  {
					var c = str.charAt(i);
					if (c == '\n' || c == '\r') continue;
					var k = this.font.widths[c];
					if (!k) {
					  warn("No glyph for \""+c+"\"in word: "+str);
					  k = def;
					}
					w += k;
				}
			}
			return w;
		},

		_textHeight : function(rt) {

			return this._textAscent() + this._textDescent();
		},

		_textAscent : function(rt,ignoreContext) {

			return this.font.ascent;
		},

		_textDescent : function(rt) {

			return this.font.descent;
		},

		// TODO: what about scale/rotate?
		_getBoundingBox : function(rt) {

			// bc of no translate(), we use the actual x,y
			return {
				x: rt.x,
				y: rt.y - this._textAscent() - 1,
				width: this._textWidth(),
				height: this._textAscent() + this._textDescent() + 1
			};
		},

		_width : function() {

			throw Error('no width in node');
		},

		_type : function() {

			return "Node";
		},

		toString : function() {

			return "RiText_"+this._type();
		}
	};

	var RiText_P5 = makeClass();

	RiText_P5.prototype = {

		init : function(p, ctx) {

			this.p = p;
			//console.log(p);
			this.ctx = ctx;
		},

		_getGraphics : function() {

			return this.p;
		},

		_push : function() {

			if (this.ctx)
				this.ctx.save();
			else
				push(); // injected by p5.js

			return this;
		},

		_pop : function() {

			if (this.ctx)
				this.ctx.restore();
			else
				pop(); // injected by p5.js

			return this;
		},

		_textAlign : function(align) {

			this.p.textAlign.apply(this,arguments);
			return this;
		},

		_scale : function(sx, sy) {

			sy = sy || 1;
			this.p.scale(sx, sy, 1);
		},

		_translate : function(tx, ty) {

			ty = ty || 0;
			this.p.translate(tx, ty, 0);
		},

		_rotate : function(zRot) {

			this.p.rotate(zRot);
		},

		_text : function(str, x, y) {

			this.p.text.apply(this, arguments);
		},

		_fill : function(r,g,b,a) {

			this.p.fill.apply(this,arguments);
		},

		_stroke : function(r,g,b,a) {

			this.p.stroke.apply(this,arguments);
		},

		_noFill : function() {

			this.p.noFill();
		},

		_rect : function(x,y,w,h) {

			this.p.rect.apply(this, arguments);
		},

		_strokeWeight : function(f) {

			this.p.strokeWeight.apply(this,arguments);
		},

		_background : function(r,g,b,a) {

			this.p.background.apply(this,arguments);
		},

		// actual creation: only called from RiText.defaultFont()!
		_createFont : function(fontName, fontSize) {

			//log("[P5] Creating font: "+fontName+"-"+fontSize);

			return this.p.createFont && this.p.createFont(fontName, fontSize);
		},

		_textFont : function(fontObj) {

			if (!is(fontObj,O))
				err("_textFont takes object!");
			this.p.textFont(fontObj, fontObj.size);
		},

		_textSize : function(sz) {

			this.p.textSize(sz);
		},

		_textWidth : function(fontObj, str) {

			this._push();
			this.p.textFont(fontObj, fontObj.size); // was _textFont
			var tw = this.p.textWidth(str);
			this._pop();
			//log(str+" -> "+tw);
			return tw;
		},

		_textHeight : function(rt) {

			this._push();
			var h = this._getBoundingBox(rt).height;
			this._pop();
			return h;
		},

		_textAscent : function(rt,ignoreContext) {

			ignoreContext = ignoreContext || false;

			if (!ignoreContext) {

			  this._push();
			  this.p.textFont(rt._font, rt._font.size);
			}

			var asc = this.p.textAscent();

			if (!ignoreContext) {

			  this._pop();
			}

			return asc;
		},

		_textDescent : function(rt) {

			this._push();
			this.p.textFont(rt._font, rt._font.size);
			var dsc = this.p.textDescent();
			this._pop();
			return dsc;
		},

		// TODO: what about scale?
		_getBoundingBox : function(rt) {

			//console.log(this.ctx.measureText(rt.text()).height);

			this._push();
			this.p.textFont(rt._font, rt._font.size);

			var ascent  =   this.p.textAscent(),
				descent =   this.p.textDescent(),
				width   =   this.p.textWidth(rt.text());

			this._pop();

			return { x: 0, y: -ascent-1, width: width,
				height: (ascent+descent+1) };
		},

		_width : function() {

			return this.p.width;
		},

		_type : function() {

			return "Processing";
		},

		toString : function() {

			return "RiText_"+this._type();
		}
	};

	////////////////////////////////////////////////////////////////////////////////
	// Renderer setup
	////////////////////////////////////////////////////////////////////////////////

	var context2d,
		hasP5js = (typeof p5 !== 'undefined'),
		hasProcessing = (typeof Processing !== 'undefined');

	if (hasProcessing) {

		Processing.registerLibrary("RiTa", {

			attach : function(p5) {

                if (p5 && p5.externals && p5.externals.canvas)
    				context2d = p5.externals.canvas.getContext("2d");

				RiText.renderer = new RiText_P5(p5, context2d);
			}
		});
	}
	else if (hasP5js) { // in p5.js

		console.warn("No support yet for p5.js");
		//RiText.renderer = new RiText_P5js(p5);
		//console.log("Renderer: p5.js");
		//context2d = p5.externals.canvas.getContext("2d");
		//RiText.renderer = new RiText_P5(p5);
	}
	else {

		if (!isNode() && !window)
			warn('Env. is not Processing(JS)/p5.js, Node, or Android; rendering unavailable');

		RiText.renderer = RiText_Node(RiText.defaults.metrics);
	}


    /*jshint -W069 */

	// inject into appropriate global scope
	window && (window['RiText'] = RiText);
	isNode() && (module.exports['RiText'] = RiText);
	//listFuncs(RiText);
    /*jshint +W069 */

})(typeof window !== 'undefined' ? window : null);


function listFuncs(obj) {
	for (var p in obj) {
		if ( typeof obj[p] === "function")
			console.log(obj[p]);
	}
}
