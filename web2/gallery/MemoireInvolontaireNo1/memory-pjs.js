// file:///Users/dhowe/Documents/javascript-workspace/MemoryJS
// Test-wn: http://localhost:8094/rita/remote/RiWordNet/getAllSynonyms/window/n

var host="lab-lamp.scm.cityu.edu.hk", port=8094;

var CLKTEST = 0;

var words = ['A', 'raw', 'memory', '.', 'Church', '.', 'A', 'loud', 'room', 'with', 'children', 'playing', ',', 'thoughtlessly', '.', 'Wandering', 'wildly', '.', 'I', 'stand', 'small', 'and', 'young', 'within', 'a', 'chaotic', 'garden', 'of', 'little', 'ideas', 'and', 'unaware', ',', 'tiny', 'minds', '.', 'Colorful', 'toys', 'litter', 'the', 'ground', 'and', 'posters', 'of', 'silent', 'saints', 'loom', '.', 'My', 'mother', 'rises', 'tall', 'and', 'aware', '.', 'She', 'departs', 'gracefully', '.', 'I', 'pull', 'a', 'blue', ',', 'plastic', 'bucket', 'to', 'the', 'door', 'and', 'climb', 'it', '.', 'Staring', 'through', 'the', 'window', '.', 'Bells', 'ringing', '.', 'My', 'mom', 'is', 'walking', 'down', 'a', 'long', 'hall', ',', 'bright', 'with', 'holy', 'light', '.', 'I', 'am', 'trembling', ',', 'abiding', 'while', 'the', 'adults', 'pray', '.', 'I', 'play', ',', 'barely', ',', 'with', 'a', 'little', 'red', 'ambulance', ',', 'watching', 'the', 'empty', 'corridor', '.'];
var allpos = ['-', 'a', 'nn', '-', 'nn', '-', '-', 'a', 'nn', '-', 'nns', 'vbg', '-', 'r', '-', 'vbg', 'r', '-', '-', 'vb', 'a', '-', 'a', '-', '-', 'a', 'nn', '-', 'a', 'nns', '-', 'a', '-', 'a', 'nns', '-', 'a', 'nns', 'vb', '-', 'nn', '-', 'nns', '-', 'a', 'nns', 'vb', '-', '-', 'nn', 'vbz', 'a', '-', 'a', '-', '-', 'vbz', 'r', '-', '-', 'vb', '-', 'a', '-', 'a', 'nn', '-', '-', 'nn', '-', 'vb', '-', '-', 'vbg', '-', '-', 'nn', '-', 'nns', 'vbg', '-', '-', 'nn', '-', 'vbg', '-', '-', 'a', 'nn', '-', 'a', '-', 'a', 'nn', '-', '-', 'vb', 'vbg', '-', 'vbg', '-', '-', 'nns', 'vb', '-', '-', 'vb', '-', 'r', '-', '-', '-', 'a', 'a', 'nn', '-', 'vbg', '-', 'a', 'nn', '-'];
var commons = ['.', ',', 'one', 'I', 'play', 'pull', 'all', 'a', 'an', 'and', 'is', 'it', 'about', 'above', 'across', 'after', 'against', 'around', 'at', 'before', 'behind', 'below', 'beneath', 'beside', 'besides', 'between', 'beyond', 'but', 'by', 'each', 'down', 'during', 'except', 'for', 'from', 'in', 'inside', 'into', 'there', 'like', 'my', 'near', 'of', 'off', 'on', 'out', 'outside', 'over', 'since', 'the', 'through', 'throughout', 'till', 'to', 'toward', 'under', 'until', 'wait', 'stand', 'plus', 'up', 'upon', 'with', 'without', 'according', 'because', 'way', 'addition', 'front', 'regard', 'instead', 'account'];
var ignores = 'womb-to-tomb|hearable|lav|bimester|quadripara|quintipara|lebensraum|ells|chutzpanik|free-lances|puerpera|inspissate|pyrolatries|inexperient|primipara|nonesuches|jimhickeys|brainpowers|cacodaemons|fakirs|kalifahs|nonsuches';

var p, rts, wordnet, arrayLists = [], colorVals = [], dbug = 0;
var timeStamp = 0, bgTimeStamp = 0, mode = CLKTEST, stepMs = 1000;
var bgColor = 0, fontColor = 255.0, bgCycleUp = true;

function _setup(parent) {
	
	p = parent;
	
	RiText.defaultFontSize(48);
	RiText.defaultFont('LondonBetween.ttf');

	wordnet = new RiWordNet(host, 8094);
	for (var i = 0; i < words.length; i++) {
		
		arrayLists[i] = [words[i]]; 
		colorVals[i] = 255.0;
	}
	
	reformat();
}

function _draw() {
	
	if (CLKTEST && mode == 0) return;

	if (p.millis() - bgTimeStamp > 100) {
		
		bgColor += bgCycleUp ? .42 : -.42;
		bgCycleUp = (bgColor > 254) ? false : (bgColor < 1) ? true : bgCycleUp;
		fontColor = fontColor - 1.0;
		bgTimeStamp = p.millis();
	}

	tryReplacement();

	p.background(bgColor);
	RiText.drawAll();
}

function _onclick() {
 
 	
	if (CLKTEST) {
		
      mode = CLKTEST;
      timeStamp = -stepMs;
    }
}

////////////////////////////////////////////////////////////////////////

function reformat() {

	RiText.disposeAll();
	
	// get rid of current ritexts
	rts = RiText.createWords(RiTa.untokenize(words),
		 30, 60, p.width - 50, p.height - 100);
	
	for (var i = 0; i < rts.length; i++)
		rts[i].fill(colorVals[i]);
			
	timeStamp = p.millis();
	//console.log("unbusy @"+p.millis()); 
}
  
function isCommon(word) {

    var test = word.replace(/\.$/, '');
    for (var i = 0; i < commons.length; i++) {
      if (commons[i] === test)
        return true;
    }
    return false;
}

function tryReplacement() {

	if (p.millis() - timeStamp > stepMs) {// only change if stepMs has elapsed
		
		// count is set to random so loop begins randomly in paragraph
		var replaceIdx = Math.floor(p.random(1, words.length)); 
		var toChange = words[replaceIdx];

		if (toChange.length < 2 || isCommon(toChange))
			return;

		// once two minutes pass, 120000ms, then the text goes into
		// "remembering" state where it is more likely to remember a
		// replacement that it has already made then to make a new replacement

		if (!CLKTEST) {
			
 			var now = p.millis(), max = (now-timeStamp > 120000) ? 10 : 5;
  			mode  = Math.floor(p.random(1, max));
  		}
  		
		timeStamp = p.millis();
		//console.log("TRY: " + p.millis() +" NEXT: "+(timeStamp+stepMs));
  		
  		//busy = 1;
	       
		// switch statement which either changes verbs, nouns,
		// adverbs, or adjectives; or remembers an older word
		switch (mode) {
			case 1:
				if (allpos[replaceIdx].indexOf("a") == 0)
					replaceAdj(toChange, replaceIdx);
				break;
			case 2:
				if (allpos[replaceIdx].indexOf("n") == 0)
					replaceNoun(toChange, replaceIdx);
				break;
			case 3:
				if (allpos[replaceIdx].indexOf("r") == 0)
					replaceAdv(toChange, replaceIdx);
				break;
			case 4:
				if (allpos[replaceIdx].indexOf("v") == 0)
					replaceVerb(toChange, replaceIdx);
				break;
			case 9:
				replaceIdx = remember(toChange, replaceIdx);
				break;
		}
	}
}

  // here are the methods for retrieving words from wordNet and making
  // appropriate changes to their structure if needed. 
  
function replaceAdj(toChange, replaceIdx) {

	wordnet.getAllSynonyms(toChange, "a", function(asyns) {

		if (asyns.length) {
			
			var newWord = asyns[Math.floor(p.random(asyns.length))];
			if (dbug) console.log("replaceAdj(): '" + toChange + "' -> " + newWord);
			fireReplaceEvent(replaceIdx, newWord);
		}
	}); 

}  
  
function replaceAdv(toChange, replaceIdx) {
 
 	//timeStamp = p.millis();if (CLKTEST) mode = 0; // tmp-remove
 	
	wordnet.getAllDerivedTerms(toChange, "r", function(dts) {
		
		if (dts.length > 0) {
			
			var randAdj = dts[Math.floor(p.random(dts.length))];
			wordnet.getAllSynonyms(randAdj, "a", function(asyns) {
	
				if (asyns.length > 0) {
					
					var adj = asyns[Math.floor(p.random(asyns.length))];
					var newStr = toAdverb(adj);					
					if (dbug) console.log("Adverbify: '" + adj + "' -> " + newStr);
					
					wordnet.exists(newStr, function(yes) {
						
						if (yes) {
							if (dbug) console.log("replaceAdv(): '"+toChange+"' -> "+newStr);
							fireReplaceEvent(replaceIdx, newStr);
						}
						else {
							console.log("wn.reject: "+newStr);
						}
					});
				}
			});
		}
	});
}    
  
function replaceNoun(toChange, replaceIdx) { 

	wordnet.getAllSynonyms(toChange, "n", function(nsyns) {

		if (nsyns.length > 0) {
			
			var newStr = nsyns[Math.floor(p.random(nsyns.length))];
			
			if (!endsWith(newStr, "ing")) {
				
				if (allpos[replaceIdx] === "nns") {
					
					var orig = newStr; // tmp-remove
					newStr = RiTa.pluralize(newStr);
					if (dbug) console.log("Pluralizing: '" + orig + "' -> " + newStr + " [orig=" + words[replaceIdx]+"]");
	
					wordnet.exists(newStr, function(yes) { // Does this happen?
						
						if (!yes) {
							console.log("WN-plural-rejection***: '" + newStr);
							return;
						}
					
						if (dbug) console.log("replaceNoun(): '" + toChange + "' -> " + newStr);
			
						fireReplaceEvent(replaceIdx, newStr);
					});
				}        var args = { tense: RiTa.PRESENT_TENSE, number: RiTa.SINGULAR, person: RiTa.THIRD_PERSON };

			} 
			else {
				if (dbug) console.log("Ignoring -ing noun! -> " + newStr);
			}
		}
	});
}
  
function replaceVerb(toChange, replaceIdx) {
	
    if (replaceIdx < 2) return;
    
    if (equalsIgnoreCase(toChange, "am")) {
    	
        fireReplaceEvent(replaceIdx, "was");
        return;
    }
    else if (equalsIgnoreCase(toChange,"was")) {
    	
        fireReplaceEvent(replaceIdx, "am");
        return;
    }

	wordnet.getAllCoordinates(toChange, "v", function(vsyns) {
		
		if (vsyns.length > 0) {
			
			var newStr = vsyns[Math.floor(p.random(vsyns.length))];
			var orig = newStr;
			// tmp-remove
	
			if (allpos[replaceIdx] === "vbg") {
	
				newStr = RiTa.getPresentParticiple(newStr);
				if (dbug) console.log("Adding-ing: " + orig + " -> " + newStr);
			} 
			else if (allpos[replaceIdx] === "vbz") {
	
		        var args = { tense: RiTa.PRESENT_TENSE, number: RiTa.SINGULAR, person: RiTa.THIRD_PERSON };

				newStr = RiTa.conjugate(newStr, args);
				if (dbug) console.log("Conjugate(3rd)***: " + orig + " -> " + newStr);
			}
	
			if (dbug) console.log("replaceVerb(): '" + toChange + "' -> " + newStr);
	
			fireReplaceEvent(replaceIdx, newStr);
		}
	});
}

function remember(toChange, idx) {
	
	for (var t = 0; t < arrayLists.length; t++) {
		
		if (idx >= arrayLists.length) idx = 0;

		if (arrayLists[idx].length == 1) {
			
			idx++; // update counter and continue if an array of size 1 is found
			continue;
		}

		var lastWord;
		if (arrayLists[idx].length > 2) {
			
			// remove the last item in the array
			arrayLists[idx].pop();

			// current one which is now at the end 
			lastWord = arrayLists[idx][arrayLists[idx].length - 1];

			// check the word against common words to ignore
			if (!isCommon(lastWord)) {
				
				if (dbug) console.log("remember(): '" + toChange + "' -> " + lastWord);
				fireReplaceEvent(idx, lastWord);
				break;
			}
		}
		idx++; // update counter if a word is not returned
	}   

    return idx;
}
  
function onReplaceEvent(re) {
    
	var sub = re.data();
	var newWord = sub.word;

	if (newWord != null) {
		
		//timeStamp = p.millis();
		if (CLKTEST) mode = 0;
		makeSubstitution(newWord, sub.idx);
	}
}
  
function makeSubstitution(newWord, idx) {
	    
	var all = RiText.instances;

	for (var t = 0; t < all.length; t++) {
		
		var riString = all[t].text();
		if (equalsIgnoreCase(riString, words[idx]) ||
			 equalsIgnoreCase(riString, words[idx] + ".") ||
			  equalsIgnoreCase(riString, words[idx] + ",")) 
		{
			//  change the color of text if needed
			changeTextColor(mode, t);
		}
	}

    adjustDeterminer(newWord, idx); // may change words[idx-1]
  
  	console.log(words[idx] + "-> "+newWord+ " @"+p.millis());
  
    // here is where the replacement is actually made
    words[idx] = newWord;
          
    // following 5 lines needed to add the new word to the
    // arrays of words that keep track of the history of replacements
    var tempArray = arrayLists[Math.min(idx, arrayLists.length-1)];
    if (mode <= 4) tempArray.push(newWord);
    

    reformat(); // reformat the screen
    
    //timeStamp = p.millis();
}
  
function adjustDeterminer(newWord, idx) {
	
  	//console.log("adjustDeterminer: "+newWord);

    if (idx < 1) return;
    
    var firstLetter = newWord.charAt(0);
    
    if (equalsIgnoreCase(words[idx-1], "a") && /[aeiou]/.test(firstLetter)) { 

      	words[idx-1] = checkCase(idx-1, "an");
    } 
    else if (equalsIgnoreCase(words[idx-1], "an") && /[^aeiou]/.test(firstLetter)) { 

      	words[idx-1] = checkCase(idx-1,"a");
    } 
}    
  
function fireReplaceEvent(replaceIdx, newWord) {
	
	// if (!busy) {
		// console.log("BUSY: Ignoring fireReplaceEvent()");
		// return;
	// }
	//timeStamp = p.millis();

    if (ignores.indexOf(newWord)>-1) return;
    
    //busy = 1;
    
    onReplaceEvent(new RiTaEvent(wordnet, null, 
        new Sub(checkCase(replaceIdx, newWord), replaceIdx)));
}  
   
function toAdverb(nStr) {
		
	if (endsWith(nStr, "y")) 
		return nStr.substring(0, nStr.length - 1) + "ily";
	
	return nStr + (endsWith(nStr, "ic") ? "ally" : "ly");
}

var Sub = makeClass();

Sub.prototype = {
	
	init: function(s, index) {
		this.word = s;
		this.idx = index;
	}
};
 
////////////////////////////////////////////////////////////////////////

function isPlural(s) { return (!s.equals(RiTa.stem(s))); }

function endsWith(str, ending) { 
	
	return str.slice(-ending.length) == ending;
}

function checkCase(idx, word)
{
	var first = words[idx].charAt(0);
    if (idx < 1 || first == first.toUpperCase()) {
        
        word = RiTa.upperCaseFirst(word);
        if (dbug) console.log("Capitalizing: "+word+" [orig="+words[idx]+"]");
    }

    return word;
}
  
function changeTextColor(num, t) {

	if (num <= 4) 
		colorVals[t] = Math.max(0, colorVals[t] - 50);
	else 
		colorVals[t] = Math.min(255, colorVals[t] + 50);
}

function equalsIgnoreCase(a,b) {
	
	return a && b && (a.toUpperCase() === b.toUpperCase());
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