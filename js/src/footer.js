
/*jshint -W069 */

// TODO: compress

if (window) { // for browser


  if (is(RiTa,O))       window['RiTa'] = RiTa;
  if (is(RiString,F))   window['RiString'] = RiString;
  if (is(RiGrammar,F))  window['RiGrammar'] = RiGrammar;
  if (is(RiMarkov,F))   window['RiMarkov'] = RiMarkov;
  if (is(RiWordNet,F))  window['RiWordNet'] = RiWordNet;
  if (is(RiLexicon,F))  window['RiLexicon'] = RiLexicon;
  if (is(RiTaEvent,F))  window['RiTaEvent'] = RiTaEvent;

} else if (typeof module != 'undefined') { // for node

  if (is(RiTa,O))       module.exports['RiTa'] = RiTa;
  if (is(RiString,F))   module.exports['RiString'] = RiString;
  if (is(RiGrammar,F))  module.exports['RiGrammar'] = RiGrammar;
  if (is(RiMarkov,F))   module.exports['RiMarkov'] = RiMarkov;
  if (is(RiWordNet,F))  module.exports['RiWordNet'] = RiWordNet;
  if (is(RiLexicon,F))  module.exports['RiLexicon'] = RiLexicon;
  if (is(RiTaEvent,F))  module.exports['RiTaEvent'] = RiTaEvent;
}

/*jshint +W069 */

})(typeof window !== 'undefined' ? window : null);
