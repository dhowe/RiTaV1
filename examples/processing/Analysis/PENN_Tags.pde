String matchPennTags(String penn){
StringDict pennTags;
pennTags = new StringDict();
pennTags.set("cc","Coordinating conjunction");
pennTags.set("cd","Cardinal number");
pennTags.set("dt","Determiner");
pennTags.set("ex","Existential there");
pennTags.set("fw","Foreign word");
pennTags.set("in","Preposition");
pennTags.set("jj","Adjective");
pennTags.set("jjr","Adjective");
pennTags.set("ls","List item marker");
pennTags.set("md","Modal");
pennTags.set("nn","Noun");
pennTags.set("nns","Noun");
pennTags.set("nnp","Proper Noun");
pennTags.set("nnps","Proper Noun");
pennTags.set("pdt","Predeterminer");
pennTags.set("pos","Possessive ending");
pennTags.set("prp","Pronoun");
pennTags.set("prp$","Pronoun");
pennTags.set("rb","Adverb");
pennTags.set("rbr","Adverb");
pennTags.set("rbs","Adverb");
pennTags.set("rp","Particle");
pennTags.set("sym","Symbol");
pennTags.set("to","to");
pennTags.set("uh","interjection");
pennTags.set("vb","Verb");
pennTags.set("vbd","Verb");
pennTags.set("vbg","Verb");
pennTags.set("vbn","Verb");
pennTags.set("vbp","Verb");
pennTags.set("vbz","Verb");
pennTags.set("wdt","Wh-determiner");
pennTags.set("wp","Wh-pronoun");
pennTags.set("wp$","Wh-pronoun");
pennTags.set("wrb","Wh-adverb");

String result = pennTags.get(penn);
return result;

}