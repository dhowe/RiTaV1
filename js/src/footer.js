
/*jshint -W069 */

// TODO: compress

if (window) { // for browser

  if (typeof RiTa===O)      window['RiTa'] = RiTa;
  if (typeof RiString===F)  window['RiString'] = RiString;
  if (typeof RiGrammar===F) window['RiGrammar'] = RiGrammar;
  if (typeof RiMarkov===F)  window['RiMarkov'] = RiMarkov;
  if (typeof RiWordNet===F) window['RiWordNet'] = RiWordNet;
  if (typeof RiLexicon===F) window['RiLexicon'] = RiLexicon;
  if (typeof RiTaEvent===F) window['RiTaEvent'] = RiTaEvent;

} else if (typeof module !== 'undefined') { // for node

  if (typeof RiTa===O)      module.exports['RiTa'] = RiTa;
  if (typeof RiString===F)  module.exports['RiString'] = RiString;
  if (typeof RiGrammar===F) module.exports['RiGrammar'] = RiGrammar;
  if (typeof RiMarkov===F)  module.exports['RiMarkov'] = RiMarkov;
  if (typeof RiWordNet===F) module.exports['RiWordNet'] = RiWordNet;
  if (typeof RiLexicon===F) module.exports['RiLexicon'] = RiLexicon;
  if (typeof RiTaEvent===F) module.exports['RiTaEvent'] = RiTaEvent;

  // var cons = [ RiString, RiGrammar, RiMarkov, RiWordNet, RiLexicon, RiTaEvent ];
  // var tags = [ 'RiString', 'RiGrammar', 'RiMarkov', 'RiWordNet', 'RiLexicon', 'RiTaEvent' ];
  // for (var i = 0; i < cons.length; i++) {
  //   if (typeof cons[i]===F)
  //     module.exports[tags[i]] = cons[i];
  // }
}

/*jshint +W069 */

})(typeof window !== 'undefined' ? window : null);
