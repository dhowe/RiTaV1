String tagName(String tag) {

  if (tagsDict == null) {
    tagsDict = new StringDict();
    tagsDict.set("n", "Noun");
    tagsDict.set("v", "Verb");
    tagsDict.set("r", "Adverb");
    tagsDict.set("a", "Adjective");
  }

  return tag == null ? null : tagsDict.get(tag);
}

void addSyllables(String sylls, Bubble[] bubbles)  {

   // split each syllable
   String[] syllables = sylls.split("/");

   // record how many phonemes are in each syllable
   int[] phslength = new int[syllables.length];

   // record the past phonemes number
   int past = 0;

    for (int i = 0; i < syllables.length; i++) {

      String[] phs = syllables[i].split("-");
      for (int j = 1; j < phs.length; j++)
        bubbles[past+j].adjustDistance(-20 * j);

      past += phs.length;
    }
}

void addStresses(String stresses, String syllables, Bubble[] bubbles) {

   // Split each stress
   String[] stress = stresses.split("/");

   // Split each syllable
   String[] syllable = syllables.split("/");

   // Count phonemes in each syllable
   int[] phslength = new int[syllable.length];

   // Record the previous phoneme count
   int past = 0;

   for (int i = 0; i < stress.length; i++) {

     String[] phs = syllable[i].split("-");

     // if the syllable is stressed, grow its bubbles
     if (Integer.parseInt(stress[i]) == 1) {
       for (int j = 0; j < phs.length; j++)
         bubbles[past+j].grow();
     }

     past += phs.length;
   }
}

color phonemeColor(String phoneme) {

  int idx = java.util.Arrays.asList(RiTa.ALL_PHONES).indexOf(phoneme);
  return idx > -1 ? colors[idx] : 0;
}

color[] colorGradient() {

  colorMode(HSB, 1,1,1,1);
  color[] tmp = new color[RiTa.ALL_PHONES.length];
  for (int i = 0; i < tmp.length; i++) {
    float h = map(i, 0, tmp.length, .2, .8);
    tmp[i] = color(h,.9,.9,.6);
  }
  colorMode(RGB,255,255,255,255);
  return tmp;
}
