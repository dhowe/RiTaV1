String matchTags(String tag) {
  
  StringDict tagsDict;
  tagsDict = new StringDict();
  tagsDict.set("n", "Noun");
  tagsDict.set("v", "Verb");
  tagsDict.set("r", "Adverb");
  tagsDict.set("a", "Adjective");
  String result = tagsDict.get(tag);
  return result;
  
}


color getTagColor(String tag) {
  
  color c=#FFCC00;
  switch (tag) {
  case "None":  
    c = #CC881B;//brown
    break;
  case "Verb":  
    c = #1B60CC;//blue
    break;
  case "Adverb":  
    c = #CC1BB8;//violet
    break;
  case "Adjective":  
    c = #1BCC2F;//green
    break;
  }
  return c;
  
}