//TODO: add getJSON error handling...
(function(window, undefined) {

	var RiWordNet = makeClass();

	RiWordNet.PATH = '/rita/remote/RiWordNet/';

	RiWordNet.prototype = {

		init : function(host, port) {

			this.port = port;
			this.host = host;
		},

		getSynonyms : function(query, pos, cb) {
			this._getStringArray('getSynonyms/' + query + '/' + pos, cb);
		},

		getAllSynonyms : function(query, pos, cb) {
			this._getStringArray('getAllSynonyms/' + query + '/' + pos, cb);
		},

		getAllCoordinates : function(query, pos, cb) {
			this._getStringArray('getAllCoordinates/' + query + '/' + pos, cb);
		},

		getAllDerivedTerms : function(query, pos, cb) {
			this._getStringArray('getAllDerivedTerms/' + query + '/' + pos, cb);
		},
		
		getBestPos : function(query, cb) {
			this._getString('getBestPos/' + query, cb);
		},
		
		getRandomWord : function(pos, cb) {
			this._getString('getRandomWord/' + pos, cb);
		},

		exists : function(query, cb) {
			this._getBoolean('exists/' + query, cb);
		},

		isAdverb : function(query, cb) {
			this._getBoolean('isAdverb/' + query, cb);
		},

		isVerb : function(query, cb) {
			this._getBoolean('isVerb/' + query, cb);
		},

		isNoun : function(query, cb) {
			this._getBoolean('isNoun/' + query, cb);
		},

		isAdjective : function(query, cb) {
			this._getBoolean('isAdjective/' + query, cb);
		},

		_getBoolean : function(path, cb) {

			var url = 'http://' + this.host + ':' + this.port;
			url += RiWordNet.PATH + path.replace(/ /, '_') + '?callback=?';
			serverCall(url, cb);
		},

		_getStringArray : function(path, cb) {
			
			var url = 'http://' + this.host + ':' + this.port;
			url += RiWordNet.PATH + path.replace(/ /, '_') + '?callback=?';
			serverCall(url, cb);
		},
		
		_getString : function(path, cb) {
			
			var url = 'http://' + this.host + ':' + this.port;
			url += RiWordNet.PATH + path.replace(/ /, '_') + '?callback=?';
			serverCall(url, cb);
		}
	};
	
	function serverCall(url, cb) {
		
			$.getJSON(url, function(data) {
				
				if (data.error.length)
					throw Error(data.error);
					
				cb && cb(data.result);
				
			}).fail(function(jqXHR, status, error) {
				
		   		console.log('RiWordNet.'+((status != 'parseerror') ? "Error:" 
		   			: "ParseError:") + (status+" / "+error+" / "+jqXHR));
			});
	}

	function makeClass() {// By John Resig (MIT Licensed)

		return function(args) {

			if (this instanceof arguments.callee) {

				if ( typeof this.init == 'function') {

					this.init.apply(this, args && args.callee ? args : arguments);
				}
			} else {
				return new arguments.callee(arguments);
			}
		};
	}

	if (window) {// for browser

		window['RiWordNet'] = RiWordNet;
		
	} else if ( typeof module != 'undefined' && module.exports) {// for node

		module.exports['RiWordNet'] = RiWordNet;
	}

})( typeof window !== 'undefined' ? window : null);
