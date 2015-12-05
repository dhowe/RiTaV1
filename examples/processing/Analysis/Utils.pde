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

  // find the index of current phoneme
  for (int i = 0; i < RiTa.ALL_PHONES.length; i++) {
    if (phoneme.equals(RiTa.ALL_PHONES[i]))
      return colors[i];
  }

  return 0;
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


color[] randomColors() {

  float[] mix = { 105, 153, 247	};
  color[] cols = new color[RiTa.ALL_PHONES.length];
  float gap = (256/(float)cols.length);

  for (int i = 0; i < cols.length; i++) {

    //float r = random(256), g = random(256), b = random(256);
    float r = gap * i;
    float b = 0;
    float g = 0;//i%8 * 255/8.0, g = 255-b;//(gap * i * gap) % 255;

    // mix the color
    r = (r + mix[0]) / 2;
    g = (g + mix[1]) / 2;
    b = (b + mix[2]) / 2;

    cols[i] = color(r, g, b, 255);
  }

  return cols;
}

color tagColor(String tag) {

  switch(tag) {
    case "Noun":
      return #CC881B; // brown
    case "Verb":
      return #1B60CC; // blue
    case "Adverb":
      return #CC1BB8; // violet
    case "Adjective":
      return #1BCC2F; // green
  }

  return #FFCC00;
}
