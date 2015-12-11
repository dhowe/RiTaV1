// JavaScript Document

	
	// ////////////////////////////////////////////////////////////
	// RiGrammar
	// ////////////////////////////////////////////////////////////

	/**
	 * @name RiGrammar
	 * @class A probabilistic context-free grammar with literary extensions designed for text-generation
	 * <pre> 
		rg = new RiGrammar("mygrammar.g");
		System.out.println(rg.expand());

		</pre>
	 * 
	 * RiTa grammar files are JSON-formatted text files that follow the format below:
	 *  <pre>   

		  "&lt;start&gt;": "&lt;rule1&gt; | &lt;rule2&gt; | &lt;rule3&gt;"
	
		  "&lt;rule2&gt;": "terminal1 |  terminal2 | &lt;rule1&gt;"
		
		   ...
		</pre>   
	 * <b>Primary methods of interest:</b>
	 * <ul>
	 * <li><code>expand()</code> which simply begins at the &lt;start&gt; state and 
	 * generates a string of terminals from the grammar.<p>
	 * <li><code>expandFrom(String)</code> which begins with the argument
	 * String (which can consist of both non-terminals and terminals,) 
	 * and expands from there. Notice that <code>expand()</code> is simply
	 * a convenient version of <code>expandFrom("&lt;start&gt;");</code>.<p>
	 * <li><code>expandWith(String, String)</code> takes 2 String arguments, the 1st 
	 * (a terminal) is guaranteed to be substituted for the 2nd (a non-terminal). Once this 
	 * substitution is made, the algorithm then works backwards (up the tree from the leaf)
	 * ensuring that the terminal (terminal1) appears in the output string. 
	 * For example, with the grammar fragment above, one might call:<p>
	   <pre>
			grammar.expandWith(terminal1, "&lt;rule2&gt;");
	  </pre>
	 * assuring not only that <code>&lt;rule2&gt;</code>will be used at least 
	 * once in the generation process, but that when it is, it will be replaced 
	 * by the terminal "hello".
	 *</ul>
	 *
	 *<li>A RiGrammar object will assign (by default) equal weights to all choices in a rule. 
	 *One can adjust the weights by adding 'multipliers' as follows: (in the rule below,
	 * 'terminal1' will be chosen twice as often as the 2 other choices.
	 * <pre>   
		 "&lt;rule2&gt;": "[2] terminal1 | terminal2 | &lt;rule1&gt;" 
	   </pre>
		
	 *<li>The RiGrammar object supports callbacks, from your grammar, back into your code.
	 * To generate a callback, add the method call in your grammar, surrounded by back-ticks, as follows:
	 * <pre>   
	 *     
	 *       "&lt;rule2&gt;": "The cat ran after the `pluralize('cat');` | \
	 *       The &lt;noun&gt; ran after the `pluralize(&lt;noun&gt;);`" 
	 *     </pre>
	 *     
	 * Any number of arguments may be passed in a callback, but for each call,
	 * there must be a corresponding method in the sketch, e.g.,
	 * 
	 * <pre>
	 *    function pluralize(theString) {
	 *      ...
	 *    }
	 * </pre>
	 * 
	 * @author dhowe 
	 */
	var RiGrammar = makeClass();
	
	RiGrammar.START_RULE = "<start>";
	RiGrammar.OPEN_RULE_CHAR = "<";
	RiGrammar.CLOSE_RULE_CHAR = ">";
	RiGrammar.PROB_PATT = /(.*[^\s])\s*\[([0-9.]+)\](.*)/;
	RiGrammar.OR_PATT = /\s*\|\s*/;
	RiGrammar.EXEC_PATT = /`[^`]+`/g;
	RiGrammar.STRIP_TICKS = /`([^`]*)`/g
	
	/**
	 * Set/gets the execDisabled flag. Set to true (default=false) 
	 * if you don't want to use the exec mechanism for callbacks. Useful if you want
	 * to include backticks or method calls as terminals in your grammar.
	 */
	RiGrammar._execDisabled = function(disableExec)
	{
		if (arguments.length==1) {
			RiGrammar._execDisabled = disableExec;
		}
		return RiGrammar._execDisabled;
	}    

	RiGrammar.prototype = {

		/**
		 * Initializes a grammar, optionally accepting an object or JSON string containing the rules
		 * 
		 * @param  {none | string | object } grammar containing the grammar rules
		 */
		init : function(grammar) {
			
			(arguments.length == 0 || is(grammar,S) || ok(grammar, O)); 
			
			this._rules = {};
			this._execDisabled = false;
			grammar && this.setGrammar(grammar);  
		},
	
		/**
		 * Loads a JSON grammar via AJAX call to 'url', replacing any existing grammar. 
		 * @param {string} url of JSON file containing the grammar rules
		 * @returns {object} this RiGrammar
		 */
		_load : function(url) {
			
			this.reset();
			
			err("Implement me!");
			
			return this;
			
		},
	
		/**
		 * inits a grammar from an object or JSON string containing the rules (rather than a file)
		 * and replacing any existing grammar. 
		 * @param  {string | object} grammar containing the grammar rules
		 * @returns {object} this RiGrammar
		 */
		setGrammar : function(grammar) {
			
			this.reset();
			
			grammar = (typeof grammar == S) ?  JSON.parse(grammar) : grammar 
			
			for (var rule in grammar) 
				this.addRule(rule, grammar[rule]);
			
			return this;
			
		},
		
		
		/**
		 * Deletes the named rule from the grammar
		 * @returns {object} this RiGrammar
		 */ 
		removeRule : function(name)  {
			
			name = this._normalizeRuleName(name);
			delete this._rules[name];
			return this;
			
		},

		_clone: function() {  // NIAPI
			var tmp = RiGrammar();
			for(var name in this._rules) {
				tmp._rules[name] = this._rules[name];
			}
			return tmp;

		},
		
		/**
		 * Adds a rule to the existing grammar, replacing any existing rule with the same name 
		 * @param {string} name
		 * @param {string} ruleStr
		 * @param {number} weight
		 * @returns {object} this RiGrammar
		 */
		addRule : function(name, ruleStr, weight) 
		{
			var dbug = false;
	
			weight = (undef(weight) ? 1.0 : weight); // default

			name = this._normalizeRuleName(name);

			if (dbug) log("addRule: "+name+ " -> "+ruleStr+" ["+weight+"]");
			
			var ruleset = ruleStr.split(RiGrammar.OR_PATT);
			//ruleset = "<noun-phrase> <verb-phrase>";
	
			for ( var i = 0; i < ruleset.length; i++) {
				
				var rule = ruleset[i];
				var prob = weight;
				var m = RiGrammar.PROB_PATT.exec(rule);
	
				if (m != null) // found weighting
				{
					if (dbug) {
						log("Found weight for " + rule);
						for (i = 0; i < m.length; i++)
							log("  " + i + ") '" + m[i] + "'");
					}
					rule = m[1] + m[3];
					prob = m[2];
					if (dbug) log("weight=" + prob + " rule='" + rule + "'");
				}
	
				if (this.hasRule(name)) {
					if (dbug)log("rule exists");
					var temp = this._rules[name];
					temp[rule] = prob;
				} 
				else {
					
					// log("new rule");
					var temp2 = {};
					temp2[rule] = prob;
					this._rules[name] = temp2;
					if (dbug)log("added rule: "+name);
				}
			}
			return this;
			
		},
	  
		/**
		 * Clears all rules in the current grammar
		 * @returns {object} this RiGrammar
		 */
		reset : function() {
			
		   this._rules = {};
		   return this;
		   
		},
			  
		/**
		 * Returns the requested rule
		 * @param {string} rule name
		 * @returns {string} the rule
		 */
		getRule : function(pre) {
			
			pre = this._normalizeRuleName(pre);
	   
			// log("getRule("+pre+")");
			var tmp = this._rules[pre];
			var name, cnt = 0;
			
			for (name in tmp) cnt++; // count the matching rules
			
			if (cnt == 1) {
				return name;
			} 
			else if (cnt > 1) {
				
				var sr = this._getStochasticRule(tmp);
				return sr;
			}
			else {
				err("No rule found for: "+pre);
			}  
		},
		
		/**
		 * Prints the grammar rules to the console in human-readable format (useful for debugging) 
		 * @returns {object} this RiGrammar
		 */
		print : function() { //TODO: compare to RiTa
			
			if (console) {
				console.log("Grammar----------------");
				for ( var name in this._rules) {
					
					console.log("  '" + name + "' -> ");
					var choices = this._rules[name];
					for ( var p in choices) {
						console.log("    '" + p + "' [" + choices[p] + "]");
					}
				}
				console.log("-----------------------");
			}
			return this;
			
		},
		
		/**
		 * Returns true if the requested rule exists in the grammar, else false
		 * @param {string} the rule name
		 * @returns {boolean} true if the rule exists in the grammar, else false
		 */
		hasRule : function(name) {
			
			//log("hasRule("+name+")");
			name = this._normalizeRuleName(name);
			return (typeof this._rules[name] !== 'undefined');
			
		},
		
		/**
		 * Expands the grammar after replacing an instance of the non-terminal
		 * 'symbol' with the String in 'literal'.
		 * <P>
		 * Guarantees that 'literal' will be in the final expanded String, 
		 * assuming at least one instance of 'symbol' in the Grammar.
		 * 
		 * @param literal
		 * @param symbol
		 * 
		 * @returns {string} expanded text
		 */
		expandWith : function(literal, symbol) { // TODO: finish 

			var gr = this._clone();
			
			var match = false;
			for ( var name in gr._rules) {
				if (name===symbol) {
					var obj = {};
					obj[literal] = 1.0;
					gr._rules[name] = obj;
					match = true;
				}
			}
			if (!match) 
				err("Rule '"+symbol+"' not found in grammar");

			// TODO: tmp, awful hack, write this correctly
			var tries, maxTries = 1000;
			for (tries = 0 ;tries < maxTries; tries++) {
				var s = gr.expand();
				if (s.indexOf(literal)>-1)
					return s;
			}
			err("\n[WARN] RiGrammar failed to complete after "+tries+" tries\n");
			
		},
		
		/**
		 * @param input
		 * @returns this
		 */
		_handleExec : function(input) { // TODO: private
					   
			//console.log("handleExec: "+input);
			
			if (!input || !input.length) return E;
			
			// strip backticks and eval
			var exec = input.replace(RiGrammar.STRIP_TICKS, "$1");
			
			try {
				input = RiTa._eval(exec);
			}
			catch (e) {
				
				warn("RiGrammar._handleExec failed on '"+input+"'\n  -> "+e.message);
				
				// if (is(exports, O)) { // we are running in node
					// console.log("found node! "+typeof module.vm);
					// module.vm.runInThisContext(exec);
					// //eval("function adj(){return 56;}") 
				// }
			}
			
			return input;
		},
		
				
		/**
		 * Expands a grammar from its '<start>' symbol
		 * @param {string} One or more function to be added to the current context BEFORE 
		 * executing the expand() call. Useful for defining functions referenced in back-ticked rules.
		 * @returns {string}
		 */
		expand : function(funs) {

			funs && RiTa._eval(funs);
			
			return this.expandFrom(RiGrammar.START_RULE);
		}, 
		// TODO: reconsider
		
		/**
		 * Expands the grammar, starting from the given symbol.
		 * RiGrammar.expand() is equivalent to RiGrammar.expandFrom('').
		 * 
		 * @param {string} rule
		 * @returns {string}
		 * 
		 */
		expandFrom : function(rule) {
			
			//console.log("no-exec; "+this._execDisabled);
			
			if (!this.hasRule(rule)) {
				warn("Rule not found: " + rule + "\nRules: ");
				(!RiTa.SILENT) && this.print();
			}
	
			var iterations = 0;
			var maxIterations = 1000;
			while (++iterations < maxIterations) {
				
				var next = this._expandRule(rule);
				if (!next) {

					//  we're done, check for back-ticked strings to eval
					(!this._execDisabled && (rule = rule.replace(RiGrammar.EXEC_PATT, this._handleExec)));
								 
					break;
				} 
				rule = next;
			}
	
			if (iterations >= maxIterations)
				warn("max number of iterations reached: " + maxIterations);
	
			return rule;
			
		},
			
		// Privates (can we hide these?) ----------------

		_expandRule : function(prod) { //private
			
			var dbug = false;
			if (dbug) log("_expandRule(" + prod + ")");
			
			for ( var name in this._rules) {
				
				var entry = this._rules[name];
				if (dbug) log("  name=" + name+"  entry=" + entry+"  prod=" + prod+"  idx=" + idx);
				var idx = prod.indexOf(name);
				
				if (idx >= 0) {
					
					var pre = prod.substring(0, idx);
					var expanded = this.getRule(name);
					var post = prod.substring(idx + name.length);
					
					if (dbug) log("  pre=" + pre+"  expanded=" + expanded+"  post=" + post+"  result=" + pre + expanded + post);
					
					return (pre + expanded + post);
				}
				
				// do the exec check here, in while loop()
			}
			// what happens if we get here? no expansions left, return?
		},
		
		_normalizeRuleName : function(pre) {
			
			if (!strOk(pre)) return pre;
			
			if (!startsWith(pre, RiGrammar.OPEN_RULE_CHAR))
				pre = RiGrammar.OPEN_RULE_CHAR + pre;
			
			if (!endsWith(pre,RiGrammar.CLOSE_RULE_CHAR))
				pre += RiGrammar.CLOSE_RULE_CHAR;

			if (pre.indexOf('>>')>0) err(">>");
			
			return pre;
			
		},
		
		// private?? (add structure test case)
		_getStochasticRule : function(temp)    { // map
	 
			var dbug = false;
			
			if (dbug) log("_getStochasticRule(" + temp + ")");
			
			var p = Math.random();
			var result, total = 0;
			for ( var name in temp) {
				total += parseFloat(temp[name]);
			}
			
			if (dbug) log("total=" + total+"p=" + p);
			
			 for ( var name in temp) {
				if (dbug) log("  name=" + name);
				var amt = temp[name] / total;
				
				if (dbug) log("amt=" + amt);
				
				if (p < amt) {
					result = name;
					if (dbug)log("hit!=" + name);
					break;
				} else {
					p -= amt;
				}
			}
			return result;
		}
		
	
	} // end RiGrammar
	
	
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
	
	
	/**  @private Simple type-checking functions */ 
	var Type = {
		
		N : 'number', S : 'string', O : 'object', A :'array', B : 'boolean', R : 'regexp', F : 'function',
		
		/**
		 * From: http://javascriptweblog.wordpress.com/2011/08/08/fixing-the-javascript-typeof-operator/
		 */
		get : function(obj) {
			
			return ({}).toString.call(obj).match(/\s([a-zA-Z]+)/)[1].toLowerCase();
		},
		
		/**
		 * Returns true if the object is of type 'type', otherwise false
		 */
		is : function(obj,type) {
			
			return Type.get(obj) === type;
		},
		
		/**
		 * Throws TypeError if not the correct type, else returns true
		 */
		ok : function(obj,type) {
			
			if (Type.get(obj)!=type) {
				
				throw TypeError('Expected '+(type ? type.toUpperCase() : type+E) + ", received "+(obj ? Type.get(obj).toUpperCase() : obj+E));
			}
			
			return true;
		}
		
	}; // end Type
	
	var is = Type.is, ok = Type.ok; // alias
	var SP = ' ', E = '', N = Type.N, S = Type.S, O = Type.O, A = Type.A, B = Type.B, R = Type.R, F = Type.F;
	
	
	
	//////// Utility functions ///////////////////////////////////////////////////////
		
	function isNum(n) {
		
	  return !isNaN(parseFloat(n)) && isFinite(n);
	}
	
	function okeys(obj) {
	
		
		var keys = [];  // replaces Object.keys();
		for(var k in obj) keys.push(k);
		return keys;
	}

   
	function dump(obj) {

		var properties = "";
		for ( var propertyName in obj) {

			properties += propertyName + ": ";

			// Check if its NOT a function
			if (!(obj[propertyName] instanceof Function)) {
				properties += obj.propertyName;
			} else {
				properties += "function()";
			}
			properties += ", ";
		}
		return properties;
	}
	
	function asList(array) {
		
		var s="[";
		for ( var i = 0; i < array.length; i++) {
			var el = array[i];
			if (array[i] instanceof Array)
				el = asList(array[i]);
			s += el;
			if (i < array.length-1) s += ", ";
		}
		return s+"]";
	}

	function undef(obj) {
		
		return (typeof obj === 'undefined' || obj === null);
	}

	function err(msg) {
		
		//console.log("err(msg) :: "+RiTa.SILENT);
		(!RiTa.SILENT) && console && console.trace(this);
		
		throw Error("[RiTa] " + msg);
	}
	
	function warn() {
		
		if (RiTa.SILENT || !console) return;
		
		for ( var i = 0; i < arguments.length; i++) 
			console.warn(arguments[i]);
	}
 
	function log() {
	
		if (RiTa.SILENT || !console) return;        
		
		for ( var i = 0; i < arguments.length; i++) 
			console.log(arguments[i]);
	}

	function strOk(str) {
		
		return (typeof str === S && str.length > 0);
	}

	function trim(str) {
		
		// faster version from: http://blog.stevenlevithan.com/archives/faster-trim-javascript
		return str.replace(/^\s\s*/, '').replace(/\s\s*$/, ''); 
		//return str.replace(/^\s*(\S*(?:\s+\S+)*)\s*$/, "$1");
	}

	function last(word) { // last char of string
		
		if (!word || !word.length) return E;
		return word.charAt(word.length-1);
	}

	function extend(l1,l2) { // python extend
		
		for (var i = 0; i < l2.length; i++) {
		 
			l1.push(l2[i]);
		}
	}

	function replaceAll(theText, replace, withThis) {
		
		return theText && is(replace, S) ?  
			theText.replace(new RegExp(replace, 'g'), withThis) : theText;
	}

	function endsWith(str, ending) { 
		
		if (!is(str,S)) return false;
		return str.slice(-ending.length) == ending;
	}
	
	function startsWith(text, substr) {

		if (!is(text,S)) return false;
		return text.slice(0, substr.length) == substr;
	}
	
	function equalsIgnoreCase(str1, str2) {
		
		return (is(str1,S) && is(str2,S)) ?
			(str1.toLowerCase() === str2.toLowerCase()) : false;
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
	
	function parseColor() {
   
		var a = arguments, len = a.length;
		
		var color = { r: 0, g: 0, b: 0, a: 255 };

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
	
	function addSpaces(str, num) {
		
		for ( var i = 0; i < num; i++)
			str += " ";
		return str;
	}

	// Arrays ////////////////////////////////////////////////////////////////////////
	
	function shuffle(oldArray) {
		var newArray = oldArray.slice();
		var len = newArray.length;
		var i = len;
		 while (i--) {
			var p = parseInt(Math.random()*len);
			var t = newArray[i];
			newArray[i] = newArray[p];
			newArray[p] = t;
		}
		return newArray; 
	}
	
	// Array Remove - from John Resig (MIT Licensed)
	function remove(array, from, to) {
		
	  // remove? only used once
	  var rest = array.slice((to || from) + 1 || array.length);
	  array.length = from < 0 ? array.length + from : from;
	  return array.push.apply(array, rest);
	}
 
	function insert(array, item, idx) {
		
	  array.slice(idx,0,item);
	  return array;
	}
	
	function removeFromArray(items, element) {
		
		while (items.indexOf(element) !== -1) {
			items.splice(items.indexOf(element), 1);
		}
	}
	
	function inArray(array, val) {
		if (!array) return false;
		return array.indexOf(val)>-1;
	}
