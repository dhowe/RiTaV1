package rita.test;

import static org.junit.Assert.assertEquals;
import static rita.support.QUnitStubs.deepEqual;
import static rita.support.QUnitStubs.equal;
import static rita.support.QUnitStubs.ok;
import static rita.support.QUnitStubs.println;
import static rita.support.QUnitStubs.setContains;
import static rita.support.QUnitStubs.setEqual;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import rita.RiTa;
import rita.RiWordNet;
import rita.wordnet.WordnetUtil;

/*
 * Compare results to: http://wordnetweb.princeton.edu/perl/webwn
 *
 */
public class RiWordNetTest {

  static String pathWordNet = "/WordNet-3.1";
  
  static final String[] EMPTY = new String[0];

  // /////////////////////////////////////////////////////////////////////////////
  // Each of these must test all 4 permutations of ignoreUpperCaseWords and
  // ignoreCompundWords and verify the original search term is not in the result
  // /////////////////////////////////////////////////////////////////////////////

  static RiWordNet w;
  static boolean preloadFilters;

  static {
    
    RiTa.SILENT = true;
    long ts = System.currentTimeMillis();
    w = new RiWordNet(pathWordNet);
    storeDefaults();
    
    if (preloadFilters) {
      String[] pos = { "n", "a", "r", "v", };
      for (int i = 0; i < pos.length; i++)
	w.iterator(pos[i]); // force load filters, so slow (TODO: optimize!)
    }
    
    System.out.println("[INFO] Loaded in "+(System.currentTimeMillis()-ts)+"ms");
  }
  
  static void storeDefaults() {
    defaultIgnoreCompoundWords = w.ignoreCompoundWords();
    defaultIgnoreUpperCaseWords = w.ignoreUpperCaseWords();
    defaultRandomizeResults = w.randomizeResults();
  }
  static boolean defaultIgnoreCompoundWords,defaultIgnoreUpperCaseWords,defaultRandomizeResults;

  @Before
  public void resetDefaults() { // run before each test to restore defaults
    w.ignoreCompoundWords(defaultIgnoreCompoundWords);
    w.ignoreUpperCaseWords(defaultIgnoreUpperCaseWords);
    w.randomizeResults(defaultRandomizeResults);
  }
  
  @Test
  public void testGetSynonymsInt() {

    String[] expected3 = {};
    // printArr( w.getSynonyms(9109));
    setEqualMulti(expected3, "getSynonyms", 9101429);

    String[] expected = { "scour", "grub", "antique", "comparison-shop",
	"hunt", "drag", "shop", "dowse", "browse", "seek", "scrabble",
	"quest after", "search", "fish", "pursue", "angle", "shell", "want",
	"surf", "seek out", "window-shop", "look for", "divine", "grope",
	"leave no stone unturned", "go after", "gather", "grope for",
	"quest for", "feel", "fumble", "dredge", "finger" };
    // printArr( w.getSynonyms(81318273));
    setEqualMulti(expected, "getSynonyms", 81318273);

    String[] expected2 = { "paterfamilias", "old man", "clotheshorse",
	"widower", "father-figure", "greybeard", "fellow", "Esq", "shaver",
	"stiff", "stud", "he-man", "divorced man", "ironside", "unmarried man",
	"old boy", "beau", "gallant", "iron man", "bull", "ponce", "boyfriend",
	"bachelor", "Samson", "signor", "ex-husband", "bey", "young buck",
	"Methuselah", "Esquire", "sir", "ex-boyfriend", "strapper", "dandy",
	"galoot", "posseman", "boy", "buster", "Herr", "ex", "signore",
	"sheik", "sod", "Peter Pan", "philanderer", "wonder boy", "ironman",
	"bozo", "grass widower", "dude", "eunuch", "Monsieur", "gentleman",
	"father surrogate", "hunk", "signior", "ejaculator", "swell", "Tarzan",
	"babu", "bruiser", "geezer", "golden boy", "father figure",
	"middle-aged man", "womaniser", "womanizer", "fop", "Senhor",
	"patriarch", "macho-man", "widowman", "swain", "inamorato",
	"graybeard", "hombre", "cat", "fashion plate", "housefather", "adonis",
	"guy", "Hooray Henry", "baboo", "young man", "castrate", "white man" };
    // printArr( w.getSynonyms(910172934));
    setEqualMulti(expected2, "getSynonyms", 910172934);
  }

  @Test
  public void testGetSynonymsIntInt() {
    String[] expected = { "scour", "grub", "antique", "comparison-shop",
	"hunt", "drag", "shop", "dowse", "browse", "seek", "scrabble",
	"quest after", "search", "fish", "pursue", "angle", "shell", "want",
	"surf", "seek out", "window-shop", "look for", "divine", "grope",
	"leave no stone unturned", "go after", "gather", "grope for",
	"quest for", "feel", "fumble", "dredge", "finger" };
    setContainsMulti(expected, "getSynonyms", 81318273, 4);

    String[] expected2 = { "paterfamilias", "old man", "clotheshorse",
	"widower", "father-figure", "greybeard", "fellow", "Esq", "shaver",
	"stiff", "stud", "he-man", "divorced man", "ironside", "unmarried man",
	"old boy", "beau", "gallant", "iron man", "bull", "ponce", "boyfriend",
	"bachelor", "Samson", "signor", "ex-husband", "bey", "young buck",
	"Methuselah", "Esquire", "sir", "ex-boyfriend", "strapper", "dandy",
	"galoot", "posseman", "boy", "buster", "Herr", "ex", "signore",
	"sheik", "sod", "Peter Pan", "philanderer", "wonder boy", "ironman",
	"bozo", "grass widower", "dude", "eunuch", "Monsieur", "gentleman",
	"father surrogate", "hunk", "signior", "ejaculator", "swell", "Tarzan",
	"babu", "bruiser", "geezer", "golden boy", "father figure",
	"middle-aged man", "womaniser", "womanizer", "fop", "Senhor",
	"patriarch", "macho-man", "widowman", "swain", "inamorato",
	"graybeard", "hombre", "cat", "fashion plate", "housefather", "adonis",
	"guy", "Hooray Henry", "baboo", "young man", "castrate", "white man" };
    setContainsMulti(expected2, "getSynonyms", 910172934, 4);

    String[] expected3 = {};
    // printArr( w.getSynonyms(9101729));
    setContainsMulti(expected3, "getSynonyms", 9101729, 2);
  }

  @Test
  public void testGetSynonymsStringString() {
    String[] expected = { "shop", "grope", "seek", "want", "fumble", "scour",
	"grub", "gather", "seek out", "leave no stone unturned", "divine",
	"hunt", "quest after", "feel", "angle", "go after", "fish", "browse",
	"quest for", "finger", "dredge", "look for", "surf", "drag", "pursue", };
    // println(w.getSynonyms("search", "v"), true);
    setEqualMulti(expected, "getSynonyms", "search", "v");

    String[] expected2 = { "endeavor", "variation", "concealing", "protection",
	"utilisation", "mourning", "works", "wastefulness", "ceremony",
	"seeking", "instruction", "continuance", "buzz", "provision",
	"control", "misconduct", "practice", "measuring", "acting", "variance",
	"market", "space walk", "deeds", "playacting", "didactics",
	"wrongdoing", "precedency", "forage", "line", "occupation", "assist",
	"creative activity", "demand", "pedagogy", "animation", "help",
	"disassembly", "use", "ransacking", "burst", "market place",
	"supporting", "laughter", "standardization", "organization", "leading",
	"playing", "procedure", "wrongful conduct", "activation", "puncture",
	"leadership", "measurement", "recreation", "frisking", "conduct",
	"music", "perturbation", "grouping", "activating", "line of work",
	"calibration", "operation", "attempt", "try", "measure", "update",
	"job", "dish", "education", "representation", "sensory activity",
	"concealment", "foraging", "locating", "training", "service",
	"teaching", "process", "performing", "military operation",
	"exploration", "turn", "cup of tea", "standardisation", "disturbance",
	"release", "outlet", "last", "assistance", "looking for",
	"negotiation", "supply", "support", "emplacement", "scouring",
	"marketplace", "liveliness", "hunt", "readying", "energizing", "work",
	"enjoyment", "supplying", "doings", "solo", "employment",
	"mystification", "play", "followup", "mensuration", "quest",
	"precedence", "placement", "location", "fun", "manhunt",
	"organisation", "continuation", "dismantlement", "fit", "obfuscation",
	"pattern", "preparation", "verbalization", "delectation",
	"utilization", "timekeeping", "politics", "dismantling", "diversion",
	"position", "vent", "bag", "aid", "creation", "hiding", "positioning",
	"looking", "pleasure", "behaviour", "effort", "grooming", "exercise",
	"committal to writing", "worship", "game", "precession", "shakedown",
	"writing", "domesticity", "rummage", "endeavour", "follow-up", "role",
	"hunting", "lamentation", "frisk", "actus reus", "business", "usage",
	"waste", "behavior", "verbalisation", "educational activity",
	"dissipation", };
    // println(w.getSynonyms("search", "n"), true);
    setEqualMulti(expected2, "getSynonyms", "search", "n");

    // println(w.getSynonyms("search", "r"), true);
    setEqualMulti(EMPTY, "getSynonyms", "search", "r");

    // println(w.getSynonyms("search", "a"), true);
    setEqualMulti(EMPTY, "getSynonyms", "search", "a");

    String[] expected5 = { "contented", "content", "bright", "riant", "elated",
	"blissful", "joyful", "euphoric", "cheerful", "laughing", "golden",
	"joyous", "felicitous", "halcyon", "glad", "prosperous", "blessed", };
    // println(w.getSynonyms("happy", "a"), true);
    setEqualMulti(expected5, "getSynonyms", "happy", "a");

    // println(w.getSynonyms("happyyyyyyyy", "a"), true);
    setEqualMulti(EMPTY, "getSynonyms", "happyyyyyyyy", "a");

    setEqual(w.getSynonyms("search", "j"), EMPTY);
  }

  @Test
  public void testGetSynonymsStringStringInt() {

    String[] expected = { "shop", "grope", "seek", "want", "fumble", "scour",
	"grub", "gather", "seek out", "leave no stone unturned", "divine",
	"hunt", "quest after", "feel", "angle", "go after", "fish", "browse",
	"quest for", "finger", "dredge", "look for", "surf", "drag", "pursue", };
    // println(w.getSynonyms("search", "v"), true);
    setContainsMulti(expected, "getSynonyms", "search", "v", 5);

    String[] expected2 = { "endeavor", "variation", "concealing", "protection",
	"utilisation", "mourning", "works", "wastefulness", "ceremony",
	"seeking", "instruction", "continuance", "buzz", "provision",
	"control", "misconduct", "practice", "measuring", "acting", "variance",
	"market", "space walk", "deeds", "playacting", "didactics",
	"wrongdoing", "precedency", "forage", "line", "occupation", "assist",
	"creative activity", "demand", "pedagogy", "animation", "help",
	"disassembly", "use", "ransacking", "burst", "market place",
	"supporting", "laughter", "standardization", "organization", "leading",
	"playing", "procedure", "wrongful conduct", "activation", "puncture",
	"leadership", "measurement", "recreation", "frisking", "conduct",
	"music", "perturbation", "grouping", "activating", "line of work",
	"calibration", "operation", "attempt", "try", "measure", "update",
	"job", "dish", "education", "representation", "sensory activity",
	"concealment", "foraging", "locating", "training", "service",
	"teaching", "process", "performing", "military operation",
	"exploration", "turn", "cup of tea", "standardisation", "disturbance",
	"release", "outlet", "last", "assistance", "looking for",
	"negotiation", "supply", "support", "emplacement", "scouring",
	"marketplace", "liveliness", "hunt", "readying", "energizing", "work",
	"enjoyment", "supplying", "doings", "solo", "employment",
	"mystification", "play", "followup", "mensuration", "quest",
	"precedence", "placement", "location", "fun", "manhunt",
	"organisation", "continuation", "dismantlement", "fit", "obfuscation",
	"pattern", "preparation", "verbalization", "delectation",
	"utilization", "timekeeping", "politics", "dismantling", "diversion",
	"position", "vent", "bag", "aid", "creation", "hiding", "positioning",
	"looking", "pleasure", "behaviour", "effort", "grooming", "exercise",
	"committal to writing", "worship", "game", "precession", "shakedown",
	"writing", "domesticity", "rummage", "endeavour", "follow-up", "role",
	"hunting", "lamentation", "frisk", "actus reus", "business", "usage",
	"waste", "behavior", "verbalisation", "educational activity",
	"dissipation", };
    setContainsMulti(expected2, "getSynonyms", "search", "n", 20);

    String[] expected3 = {};
    setContainsMulti(expected3, "getSynonyms", "search", "r", 1);

    String[] expected4 = {};
    setContainsMulti(expected4, "getSynonyms", "search", "a", 5);

    String[] expected5 = { "contented", "content", "bright", "riant", "elated",
	"blissful", "joyful", "euphoric", "cheerful", "laughing", "golden",
	"joyous", "felicitous", "halcyon", "glad", "prosperous", "blessed", };
    setContainsMulti(expected5, "getSynonyms", "happy", "a", 3);

    String[] expected6 = {};
    setContainsMulti(expected6, "getSynonyms", "happyyyyyyyy", "a", 2);

    setEqual(w.getSynonyms("search", "j", 4), EMPTY);
  }

  @Test
  public void testGetAllSynonymsStringString() {

    String[] expected = { "check", "pursue", "experiment", "re-explore",
	"grub", "research", "peruse", "prospect", "mapquest", "look for",
	"comb", "skim", "nose", "explore", "glance over", "look", "cruise",
	"poke", "hunt", "scan", "candle", "drag", "seek", "angle", "browse",
	"take stock", "x-ray", "autopsy", "fumble", "want", "cast around",
	"quest after", "rake", "size up", "examine", "strip-search", "divine",
	"frisk", "inspect", "gather", "horn in", "beat about", "run down",
	"rifle", "cast about", "fish", "google", "dredge", "raid", "intrude",
	"go", "grope", "rummage", "scour", "ransack", "probe", "scrutinise",
	"survey", "pry", "scrutinize", "shop", "seek out", "auscultate",
	"finger", "surf", "go after", "quest for", "feel",
	"leave no stone unturned", };
    // println(w.getAllSynonyms("search", "v"), true);
    setEqualMulti(expected, "getAllSynonyms", "search", "v");

    String[] expected2 = { "pleasure", "liveliness", "fit", "space walk",
	"readying", "checkup", "post-mortem examination", "pursuit",
	"creation", "practice", "thought", "off-line operation", "rummage",
	"frisking", "procedure", "locating", "vent", "precedency", "deciding",
	"worship", "count", "lookup", "sort", "bank examination",
	"mensuration", "analysis", "activating", "binary operation", "study",
	"tabulation", "use", "misconduct", "follow-up", "supporting",
	"recreation", "last", "actus reus", "unary operation", "politics",
	"comparison", "educational activity", "utilization", "forage",
	"assistance", "supplying", "reexamination", "support", "tally",
	"hiding", "pedagogy", "grouping", "writing", "sorting", "business",
	"shakedown", "foraging", "dyadic operation", "verbalisation",
	"necropsy", "intellection", "puncture", "audit", "fun", "attempt",
	"scrutiny", "activation", "wiretap", "medical", "disassembly",
	"computer operation", "empiricism", "scouring",
	"time and motion study", "printing operation", "variance",
	"concurrent operation", "timekeeping", "dissipation",
	"creative activity", "solo", "manhunt", "turn", "endeavour",
	"variation", "once-over", "enjoyment", "memory access",
	"thought process", "boolean operation", "conduct", "performing",
	"police investigation", "survey", "waste", "perturbation", "looking",
	"role", "testing", "burst", "aid", "disturbance",
	"committal to writing", "rhinoscopy", "buzz", "control function",
	"delectation", "positioning", "going-over", "knowing", "research",
	"effort", "postmortem examination", "market", "instruction", "deeds",
	"exercise", "asynchronous operation", "synchronous operation",
	"post-mortem", "hunt", "logic operation", "assist", "grooming", "dish",
	"examination", "didactics", "animation", "utilisation",
	"linguistic process", "control operation", "representation",
	"police work", "simultaneous operation", "music", "hunting",
	"time study", "ophthalmoscopy", "parallel operation",
	"standardisation", "organisation", "placement", "process", "behaviour",
	"usage", "tactual exploration", "numeration", "behavior", "training",
	"standardization", "obfuscation", "employment", "looking for",
	"ransacking", "precedence", "supply", "teaching", "works",
	"cup of tea", "bag", "doings", "mourning", "time-and-motion study",
	"quest", "inquiry", "palpation", "enumeration", "leading",
	"medical examination", "counting", "multiplex operation", "reckoning",
	"job", "line of work", "organization", "lamentation",
	"machine operation", "fine-toothed comb", "threshold operation",
	"medical checkup", "enquiry", "look-over", "play", "operation",
	"wastefulness", "PM", "decision making", "binary arithmetic operation",
	"endoscopy", "diversion", "seeking", "access", "ceremony", "line",
	"mentation", "playing", "autopsy", "exploration", "game", "precession",
	"continuation", "help", "keratoscopy", "preparation", "concealment",
	"laughter", "serial operation", "emplacement", "market place",
	"sensory activity", "measuring", "tap", "scan",
	"consecutive operation", "release", "pattern", "domesticity",
	"sequential operation", "comparing", "logical operation",
	"dismantlement", "language", "cerebration", "demand", "occupation",
	"verbalization", "continuance", "wrongful conduct", "leadership",
	"auxiliary operation", "measurement", "location", "wrongdoing",
	"inspection", "endeavor", "suggestion", "work", "motion study",
	"review", "calibration", "outlet", "military operation",
	"mystification", "medical exam", "monadic operation", "frisk",
	"control", "health check", "update", "provision", "thinking",
	"concealing", "education", "negotiation", "protection", "position",
	"try", "fixed-cycle operation", "gonioscopy", "acting", "followup",
	"measure", "dismantling", "playacting", "fine-tooth comb", "pursuance",
	"energizing", "time-motion study", "work study", "marketplace",
	"postmortem", "service", };
    // println(w.getAllSynonyms("search", "n"), true);
    setEqualMulti(expected2, "getAllSynonyms", "search", "n");

    String[] expected3 = {};
    // println(w.getAllSynonyms("search", "r"), true);
    setEqualMulti(expected3, "getAllSynonyms", "search", "r");

    String[] expected4 = {};
    // println(w.getAllSynonyms("search", "a"), true);
    setEqualMulti(expected4, "getAllSynonyms", "search", "a");

    String[] expected5 = { "contented", "euphoric", "joyous", "elated",
	"cheerful", "laughing", "felicitous", "content", "fortunate",
	"willing", "golden", "glad", "halcyon", "bright", "blissful", "joyful",
	"blessed", "riant", "prosperous", "well-chosen", };
    // println(w.getAllSynonyms("happy", "a"), true);
    setEqualMulti(expected5, "getAllSynonyms", "happy", "a");

    String[] expected6 = {};
    // println(w.getAllSynonyms("happyyyyyyyy", "a"), true);
    setEqualMulti(expected6, "getAllSynonyms", "happyyyyyyyy", "a");

    setEqual(w.getAllSynonyms("search", "j", 4), EMPTY);

  }

  @Test
  public void testGetAllSynonymsStringStringInt() {

    String[] expected = { "check", "pursue", "experiment", "re-explore",
	"grub", "research", "peruse", "prospect", "mapquest", "look for",
	"comb", "skim", "nose", "explore", "glance over", "look", "cruise",
	"poke", "hunt", "scan", "candle", "drag", "seek", "angle", "browse",
	"take stock", "x-ray", "autopsy", "fumble", "want", "cast around",
	"quest after", "rake", "size up", "examine", "strip-search", "divine",
	"frisk", "inspect", "gather", "horn in", "beat about", "run down",
	"rifle", "cast about", "fish", "google", "dredge", "raid", "intrude",
	"go", "grope", "rummage", "scour", "ransack", "probe", "scrutinise",
	"survey", "pry", "scrutinize", "shop", "seek out", "auscultate",
	"finger", "surf", "go after", "quest for", "feel",
	"leave no stone unturned", };
    setContainsMulti(expected, "getAllSynonyms", "search", "v", 10);

    String[] expected2 = { "pleasure", "liveliness", "fit", "space walk",
	"readying", "checkup", "post-mortem examination", "pursuit",
	"creation", "practice", "thought", "off-line operation", "rummage",
	"frisking", "procedure", "locating", "vent", "precedency", "deciding",
	"worship", "count", "lookup", "sort", "bank examination",
	"mensuration", "analysis", "activating", "binary operation", "study",
	"tabulation", "use", "misconduct", "follow-up", "supporting",
	"recreation", "last", "actus reus", "unary operation", "politics",
	"comparison", "educational activity", "utilization", "forage",
	"assistance", "supplying", "reexamination", "support", "tally",
	"hiding", "pedagogy", "grouping", "writing", "sorting", "business",
	"shakedown", "foraging", "dyadic operation", "verbalisation",
	"necropsy", "intellection", "puncture", "audit", "fun", "attempt",
	"scrutiny", "activation", "wiretap", "medical", "disassembly",
	"computer operation", "empiricism", "scouring",
	"time and motion study", "printing operation", "variance",
	"concurrent operation", "timekeeping", "dissipation",
	"creative activity", "solo", "manhunt", "turn", "endeavour",
	"variation", "once-over", "enjoyment", "memory access",
	"thought process", "boolean operation", "conduct", "performing",
	"police investigation", "survey", "waste", "perturbation", "looking",
	"role", "testing", "burst", "aid", "disturbance",
	"committal to writing", "rhinoscopy", "buzz", "control function",
	"delectation", "positioning", "going-over", "knowing", "research",
	"effort", "postmortem examination", "market", "instruction", "deeds",
	"exercise", "asynchronous operation", "synchronous operation",
	"post-mortem", "hunt", "logic operation", "assist", "grooming", "dish",
	"examination", "didactics", "animation", "utilisation",
	"linguistic process", "control operation", "representation",
	"police work", "simultaneous operation", "music", "hunting",
	"time study", "ophthalmoscopy", "parallel operation",
	"standardisation", "organisation", "placement", "process", "behaviour",
	"usage", "tactual exploration", "numeration", "behavior", "training",
	"standardization", "obfuscation", "employment", "looking for",
	"ransacking", "precedence", "supply", "teaching", "works",
	"cup of tea", "bag", "doings", "mourning", "time-and-motion study",
	"quest", "inquiry", "palpation", "enumeration", "leading",
	"medical examination", "counting", "multiplex operation", "reckoning",
	"job", "line of work", "organization", "lamentation",
	"machine operation", "fine-toothed comb", "threshold operation",
	"medical checkup", "enquiry", "look-over", "play", "operation",
	"wastefulness", "PM", "decision making", "binary arithmetic operation",
	"endoscopy", "diversion", "seeking", "access", "ceremony", "line",
	"mentation", "playing", "autopsy", "exploration", "game", "precession",
	"continuation", "help", "keratoscopy", "preparation", "concealment",
	"laughter", "serial operation", "emplacement", "market place",
	"sensory activity", "measuring", "tap", "scan",
	"consecutive operation", "release", "pattern", "domesticity",
	"sequential operation", "comparing", "logical operation",
	"dismantlement", "language", "cerebration", "demand", "occupation",
	"verbalization", "continuance", "wrongful conduct", "leadership",
	"auxiliary operation", "measurement", "location", "wrongdoing",
	"inspection", "endeavor", "suggestion", "work", "motion study",
	"review", "calibration", "outlet", "military operation",
	"mystification", "medical exam", "monadic operation", "frisk",
	"control", "health check", "update", "provision", "thinking",
	"concealing", "education", "negotiation", "protection", "position",
	"try", "fixed-cycle operation", "gonioscopy", "acting", "followup",
	"measure", "dismantling", "playacting", "fine-tooth comb", "pursuance",
	"energizing", "time-motion study", "work study", "marketplace",
	"postmortem", "service", };
    // println(w.getAllSynonyms("search", "n"), true);
    setContainsMulti(expected2, "getAllSynonyms", "search", "n", 5);

    String[] expected3 = {};
    // println(w.getAllSynonyms("search", "r"), true);
    setContainsMulti(expected3, "getAllSynonyms", "search", "r", 5);

    String[] expected4 = {};
    // println(w.getAllSynonyms("search", "a"), true);
    setContainsMulti(expected4, "getAllSynonyms", "search", "a", 9);

    String[] expected5 = { "contented", "euphoric", "joyous", "elated",
	"cheerful", "laughing", "felicitous", "content", "fortunate",
	"willing", "golden", "glad", "halcyon", "bright", "blissful", "joyful",
	"blessed", "riant", "prosperous", "well-chosen", };
    // println(w.getAllSynonyms("happy", "a"), true);
    setContainsMulti(expected5, "getAllSynonyms", "happy", "a", 1);

    // println(w.getAllSynonyms("happyyyyyyyy", "a"), true);
    setContainsMulti(EMPTY, "getAllSynonyms", "happyyyyyyyy", "a", 4);

    w.ignoreCompoundWords(false);
    w.ignoreUpperCaseWords(false);

    String[] exp = { "angle", "browse", "divine", "drag", "dredge", "feel",
	"finger", "fish", "fumble", "gather", "go after", "grope", "grub",
	"hunt", "leave no stone unturned", "look for", "pursue", "quest after",
	"quest for", "scour", "seek", "seek out", "shop", "surf", "want", };
    // println(w.getSynonyms("search", "v",-1), true);
    setEqual(w.getSynonyms("search", "v", -1), exp);

    setEqual(w.getSynonyms("search", "j", 6), EMPTY);
  }

  // ///////////////////////////////////////////////////////////////////////////////////////

  @Test
  public void testExistsString() {

    RiWordNet.useMorphologicalProcessor = true;

    ok(w.exists("medicare"));
    ok(w.exists("MedIcare"));
    ok(w.exists("exists"));
    ok(!w.exists("Medicare"));

    RiWordNet.useMorphologicalProcessor = false;

    ok(!w.exists("exists"));

    ok(!w.exists("healthXXX"));
    ok(w.exists("health"));

    ok(w.exists("medicare"));
    ok(!w.exists("MedIcare"));
    ok(!w.exists("Medicare"));

    ok(!w.exists("medicare "));
    ok(!w.exists(" medicare"));
    ok(!w.exists(" medicare "));
    ok(!w.exists("medicare	"));
    ok(w.exists("health insurance"));
    ok(!w.exists("health ignorance"));
    ok(!w.exists("health XXX"));

    ok(w); // see above
    ok(w.exists("hello"));
    ok(!w.exists("caz"));

    ok(!w.exists(""));
    ok(!w.exists("||"));
    ok(!w.exists("!@#$%^&*("));

    w.ignoreCompoundWords(true);
    w.ignoreUpperCaseWords(true);

    ok(!w.exists("healthXXX"));
    ok(w.exists("health"));

    ok(w.exists("medicare"));
    ok(w.exists("health insurance"));
    ok(!w.exists("health ignorance"));
    ok(!w.exists("health XXX"));

    ok(w); // see above
    ok(w.exists("hello"));
    ok(!w.exists("caz"));

    ok(!w.exists(""));
    ok(!w.exists("||"));
    ok(!w.exists("!@#$%^&*("));
  }

  @Test
  public void testGetSenseIdsStringString() {

    int[] expected = { 92124272, 910172934, 99919605, 93614083, 92989061,
	92986962, 92130460, 9903174 };
    int[] result = w.getSenseIds("cat", "n");
    deepEqual(expected, result);

    int[] expected1 = { 913367788 };
    int[] result1 = w.getSenseIds("health insurance", "n");
    // //println(result1);
    deepEqual(expected1, result1);

    int[] expected13 = { 81414524, 876153 };
    int[] result13 = w.getSenseIds("cat", "v");
    // //println(result13);
    deepEqual(expected13, result13);

    int[] expected3 = {};
    int[] result3 = w.getSenseIds("health insurance", "v");
    // //println(result3);
    deepEqual(expected3, result3);

    int[] expected4 = {};
    int[] result4 = w.getSenseIds("health insurance", "a");
    // //println(result4);
    deepEqual(expected3, result4);

    int[] expected5 = {};
    int[] result5 = w.getSenseIds("health insurance", "r");
    // //println(result5);
    deepEqual(expected5, result5);

    int[] expected2 = {};
    deepEqual(w.getSenseIds("caz", "n"), expected2);

    w.ignoreCompoundWords(true);
    w.ignoreUpperCaseWords(true);

    int[] expected6 = { 92124272, 910172934, 99919605, 93614083, 92989061,
	92986962, 92130460, 9903174 };
    int[] result6 = w.getSenseIds("cat", "n");
    deepEqual(expected6, result6);

    int[] expected7 = { 913367788 };
    int[] result7 = w.getSenseIds("health insurance", "n");
    deepEqual(expected7, result7);

    int[] expected12 = { 81414524, 876153 };
    int[] result12 = w.getSenseIds("cat", "v");
    // //println(result12);
    deepEqual(expected12, result12);

    int[] expected9 = {};
    int[] result9 = w.getSenseIds("health insurance", "v");
    // //println(result9);
    deepEqual(expected9, result9);

    int[] expected10 = {};
    int[] result10 = w.getSenseIds("health insurance", "a");
    // //println(result10);
    deepEqual(expected10, result10);

    int[] expected11 = {};
    int[] result11 = w.getSenseIds("health insurance", "r");
    // //println(result11);
    deepEqual(expected11, result11);

    int[] expected8 = {};
    int[] result8 = w.getSenseIds("caz", "n");
    deepEqual(expected8, result8);
  }

  @Test
  public void testGetHypernymsStringString() {

    String[] expected = { "root" };
    setEqualMulti(expected, "getHypernyms", "carrot", "n");

    String[] expected2 = { "domestic animal", "domesticated animal", "canid",
	"canine" };
    setEqualMulti(expected2, "getHypernyms", "dog", "n");

    String[] expected3 = { "bush", "shrub" };
    setEqualMulti(expected3, "getHypernyms", "rose", "n");

    String[] expected4 = { "plant organ" };
    setEqualMulti(expected4, "getHypernyms", "root", "n");

    String[] expected7 = { "grow" };
    setEqualMulti(expected7, "getHypernyms", "root", "v");

    String[] expected8 = {};
    setEqualMulti(expected8, "getHypernyms", "root", "r");

    String[] expected9 = {};
    setEqualMulti(expected9, "getHypernyms", "root", "a");

    String[] expected5 = {};
    setEqualMulti(expected5, "getHypernyms", "rootttt", "n");

    setEqual(w.getHypernyms("root", "j"), EMPTY);
  }

  @Test
  public void testGetAllGlosses() {

    String[] expected12 = {};
    String[] result12 = w.getAllGlosses("grow", "r");
    // println(result12,true);
    setEqual(expected12, result12);

    String[] expected2 = {
	"with sadness; in a sad manner",
	"in an unfortunate way",
	"in an unfortunate or deplorable manner", };
    String[] result2 = w.getAllGlosses("sadly", "r");
    setEqual(expected2, result2);

    String[] expected = {
	"impairment resulting from long use",
	"the act of having on your person as a covering or adornment",
	"a covering designed to be worn on a person's body", };
    String[] result = w.getAllGlosses("wear", "n");
    // println(result,true);
    setEqual(expected, result);

    String[] expected3 = {
	"feeling happy appreciation",
	"cheerful and bright",
	"eagerly disposed to act or to be of service",
	"showing or causing joy and pleasure; especially made happy", };
    String[] result3 = w.getAllGlosses("glad", "a");
    // println(result3,true);
    setEqual(expected3, result3);

    String[] expected4 = {
	"cause to grow or develop",
	"come to have or undergo a change of (physical features and attributes)",
	"pass into a condition gradually, take on a specific property or attribute; become",
	"cultivate by growing, often involving improvements by means of agricultural techniques",
	"develop and reach maturity; undergo maturation",
	"become larger, greater, or bigger; expand or gain",
	"come into existence; take on form or shape",
	"grow emotionally or mature",
	"become attached by or as if by the process of growth",
	"increase in size by natural process", };
    String[] result4 = w.getAllGlosses("grow", "v");
    // println(result4,true);
    setEqual(expected4, result4);

    String[] expected11 = {};
    String[] result11 = w.getAllGlosses("growwwwww", "v");
    // println(result9,true);
    setEqual(expected11, result11);

    // DO THE SAME TESTS AGAIN WITH BOTH TRUE (SHOULD BE SAME RESULT FOR GLOSSES
    // AND EXAMPLES)

    w.ignoreUpperCaseWords(true);
    w.ignoreCompoundWords(true);

    String[] expected5 = {
	"with sadness; in a sad manner",
	"in an unfortunate way",
	"in an unfortunate or deplorable manner", };
    String[] result5 = w.getAllGlosses("sadly", "r");
    setEqual(expected5, result5);

    String[] expected6 = {
	"impairment resulting from long use",
	"the act of having on your person as a covering or adornment",
	"a covering designed to be worn on a person's body", };
    String[] result6 = w.getAllGlosses("wear", "n");
    // println(result6,true);
    setEqual(expected6, result6);

    String[] expected7 = {
	"feeling happy appreciation",
	"cheerful and bright",
	"eagerly disposed to act or to be of service",
	"showing or causing joy and pleasure; especially made happy", };
    String[] result7 = w.getAllGlosses("glad", "a");
    // println(result7,true);
    setEqual(expected7, result7);

    String[] expected8 = {
	"cause to grow or develop",
	"come to have or undergo a change of (physical features and attributes)",
	"pass into a condition gradually, take on a specific property or attribute; become",
	"cultivate by growing, often involving improvements by means of agricultural techniques",
	"develop and reach maturity; undergo maturation",
	"become larger, greater, or bigger; expand or gain",
	"come into existence; take on form or shape",
	"grow emotionally or mature",
	"become attached by or as if by the process of growth",
	"increase in size by natural process", };
    String[] result8 = w.getAllGlosses("grow", "v");
    // println(result8,true);
    setEqual(expected8, result8);

    String[] expected9 = {};
    String[] result9 = w.getAllGlosses("growwwwww", "v");
    // println(result9,true);
    setEqual(expected9, result9);

    String[] expected13 = {};
    String[] result13 = w.getAllGlosses("grow", "r");
    // //println(result13,true);
    setEqual(expected13, result13);

    setContains(w.getAllGlosses("grow", "j"), EMPTY);
    setContains(w.getAllGlosses("growwww", "j"), EMPTY);
  }

  @Test
  public void testGetGlossInt() {
  
    String expected = "feline mammal usually having thick soft fur and no ability to roar: domestic cats; wildcats";
    String result = w.getGloss(92124272);
    // //println(result);
    assertEquals(expected, result);

    String expected2 = "try to locate or discover, or try to establish the existence of";
    String result2 = w.getGloss(81318273);
    // //println(result2);
    assertEquals(expected2, result2);

    String expected3 = "a whip with nine knotted cords";
    String result3 = w.getGloss(92989061);
    // //println(result3);
    assertEquals(expected3, result3);

    ok(w.getGloss(123213123) == null);

    w.ignoreUpperCaseWords(true);
    w.ignoreCompoundWords(true);
    String expected4 = "feline mammal usually having thick soft fur and no ability to roar: domestic cats; wildcats";
    String result4 = w.getGloss(92124272);
    // //println(result);
    assertEquals(expected4, result4);

    String expected5 = "try to locate or discover, or try to establish the existence of";
    String result5 = w.getGloss(81318273);
    // //println(result2);
    assertEquals(expected5, result5);

    String expected6 = "a whip with nine knotted cords";
    String result6 = w.getGloss(92989061);
    // //println(result6);
    assertEquals(expected6, result6);

    ok(w.getGloss(123213123) == null);

  }

  @Test
  public void testGetGlossStringString() {

    String expected = "feline mammal usually having thick soft fur and no ability to roar: domestic cats; wildcats";
    String result = w.getGloss("cat", "n");
    println("testGetGlossStringString" + result);
    assertEquals(expected, result);

    String expected2 = "change location; move, travel, or proceed, also metaphorically";
    String result2 = w.getGloss("move", "v");
    // //println(result2);
    assertEquals(expected2, result2);

    String expected3 = "having an (over)abundance of flesh";
    String result3 = w.getGloss("fat", "a");
    // //println(result3);
    assertEquals(expected3, result3);

    String expected4 = "to a severe or serious degree";
    String result4 = w.getGloss("badly", "r");
    // //println(result4);
    assertEquals(expected4, result4);

    ok(w.getGloss("badly", "u") == null);

    w.ignoreUpperCaseWords(true);
    w.ignoreCompoundWords(true);

    String expected5 = "feline mammal usually having thick soft fur and no ability to roar: domestic cats; wildcats";
    String result5 = w.getGloss("cat", "n");
    // //println(result);
    assertEquals(expected5, result5);

    String expected6 = "change location; move, travel, or proceed, also metaphorically";
    String result6 = w.getGloss("move", "v");
    // //println(result2);
    assertEquals(expected6, result6);

    String expected7 = "having an (over)abundance of flesh";
    String result7 = w.getGloss("fat", "a");
    // //println(result3);
    assertEquals(expected7, result7);

    String expected8 = "to a severe or serious degree";
    String result8 = w.getGloss("badly", "r");
    // //println(result4);
    assertEquals(expected8, result8);

    String expected9 = null;
    String result9 = w.getGloss("badlyyyyy", "r");
    // println(result9);
    assertEquals(expected9, result9);

    ok(w.getGloss("badly", "u") == null);

  }

  @Test
  public void testGetExamplesStringString() {
    
    String[] expected = { "the tires showed uneven wear" };
    setEqual(expected, w.getExamples("wear", "n"));

    String[] expected2 = { "the visit was especially wearing",
	"an exhausting march" };
    // printArr(w.getExamples("wearing", "a"));
    setEqual(expected2, w.getExamples("wearing", "a"));

    String[] expected6 = {};
    // printArr(w.getExamples("wearing", "r"));
    setEqual(expected6, w.getExamples("wearing", "r"));

    String[] expected3 = { "he had stupidly bought a one way ticket" };
    // printArr(w.getExamples("stupidly", "r"));
    setEqual(expected3, w.getExamples("stupidly", "r"));

    String[] expected4 = { "Don't run--you'll be out of breath",
	"The children ran to the store" };
    // printArr(w.getExamples("run", "v"));
    setEqual(expected4, w.getExamples("run", "v"));

    String[] expected5 = {};
    // printArr(w.getExamples("run", "v"));
    setEqual(expected5, w.getExamples("runununun", "v"));
    setEqual(expected5, w.getExamples("run", "j"));

    // DO THE SAME TESTS AGAIN WITH BOTH TRUE (SHOULD BE SAME RESULT FOR GLOSSES
    // AND EXAMPLES)
    w.ignoreUpperCaseWords(true);
    w.ignoreCompoundWords(true);

    expected = new String[] { "the tires showed uneven wear" };
    setEqual(expected, w.getExamples("wear", "n"));

    expected2 = new String[] { "the visit was especially wearing",
	"an exhausting march" };
    // printArr(w.getExamples("wearing", "a"));
    setEqual(expected2, w.getExamples("wearing", "a"));

    expected6 = new String[] {};
    // printArr(w.getExamples("wearing", "r"));
    setEqual(expected6, w.getExamples("wearing", "r"));

    expected3 = new String[] { "he had stupidly bought a one way ticket" };
    // printArr(w.getExamples("stupidly", "r"));
    setEqual(expected3, w.getExamples("stupidly", "r"));

    expected4 = new String[] { "Don't run--you'll be out of breath",
	"The children ran to the store" };
    // printArr(w.getExamples("run", "v"));
    setEqual(expected4, w.getExamples("run", "v"));

    expected5 = new String[] {};
    // printArr(w.getExamples("run", "v"));
    setEqual(expected5, w.getExamples("runununun", "v"));

    setEqual(expected5, w.getExamples("run", "j"));

  }

  @Test
  public void testGetExamplesInt() {

    String[] expected = { "the tires showed uneven wear" };
    String[] result = w.getExamples(914586275);
    // printArr(result);
    setEqual(expected, result);

    String[] result2 = w.getExamples(94292941);
    // printArr(result2);
    setEqual(EMPTY, result2);

    String[] expected3 = { "spent many happy days on the beach",
	"a happy smile", "a happy marriage" };
    String[] result3 = w.getExamples(71151786);
    // printArr(result3);
    setEqual(expected3, result3);

    setEqual(EMPTY, w.getExamples(61151786));

    w.ignoreCompoundWords(true);
    w.ignoreUpperCaseWords(true);

    expected = new String[] { "the tires showed uneven wear" };
    result = w.getExamples(914586275);
    // printArr(result);
    setEqual(expected, result);

    result2 = w.getExamples(94292941);
    // printArr(result2);
    setEqual(EMPTY, result2);

    expected3 = new String[] { "spent many happy days on the beach",
	"a happy smile", "a happy marriage" };
    result3 = w.getExamples(71151786);
    // printArr(result3);
    setEqual(expected3, result3);

    setEqual(EMPTY, w.getExamples(61151786));

  }

  @Test
  public void testGetAllExamples() {

    String[] expected = {
	"The police are searching for clues",
	"They are searching for the missing man in the entire county",
	"the students had to research the history of the Second World War for their history project",
	"He searched for information on his relatives on the web",
	"The police searched the suspect",
	"We searched the whole house for the missing keys" };
    String[] result = w.getAllExamples("search", "v");
    // println(result);
    setEqual(expected, result);

    String[] expected2 = { "the visit was especially wearing" };
    String[] result2 = w.getAllExamples("wearing", "a");
    // printArr(result2);
    setEqual(expected2, result2);

    String[] expected3 = {};
    String[] result3 = w.getAllExamples("wearing", "r");
    // printArr(result3);
    setEqual(expected3, result3);

    String[] expected4 = {};
    String[] result4 = w.getAllExamples("wearing", "n");
    // printArr(result4);
    setEqual(expected4, result4);

    String[] expected5 = {};
    String[] result5 = w.getAllExamples("wearing", "v");
    // printArr(result5);
    setEqual(expected5, result5);

    String[] expected6 = { "What should I wear today?",
	"He always wears a smile", "wear one's hair in a certain way",
	"She was wearing yellow that day" };
    String[] result6 = w.getAllExamples("wear", "v");
    // printArr(result6);
    setEqual(expected6, result6);

    String[] expected7 = { "they shouted happily", "happily he was not injured" };
    String[] result7 = w.getAllExamples("happily", "r");
    // printArr(result7);
    setEqual(expected7, result7);

    String[] expected8 = { "a fat land", "fatty food",
	"he hadn't remembered how fat she was", "fat tissue", "a nice fat job",
	"a fat rope" };
    String[] result8 = w.getAllExamples("fat", "a");
    // printArr(result8);
    setEqual(expected8, result8);

    String[] expected9 = {};
    String[] result9 = w.getAllExamples("fatttttt", "a");
    // printArr(result9);
    setEqual(EMPTY, result9);
    setEqual(EMPTY, w.getAllExamples("fatttttt", "u"));

    w.ignoreCompoundWords(true);
    w.ignoreUpperCaseWords(true);

    expected = new String[] {
	"The police are searching for clues",
	"They are searching for the missing man in the entire county",
	"the students had to research the history of the Second World War for their history project",
	"He searched for information on his relatives on the web",
	"The police searched the suspect",
	"We searched the whole house for the missing keys" };
    result = w.getAllExamples("search", "v");
    // println(result);
    setEqual(expected, result);

    expected2 = new String[] { "the visit was especially wearing" };
    result2 = w.getAllExamples("wearing", "a");
    // printArr(result2);
    setEqual(expected2, result2);

    expected3 = new String[] {};
    result3 = w.getAllExamples("wearing", "r");
    // printArr(result3);
    setEqual(expected3, result3);

    expected4 = new String[] {};
    result4 = w.getAllExamples("wearing", "n");
    // printArr(result4);
    setEqual(expected4, result4);

    expected5 = new String[] {};
    result5 = w.getAllExamples("wearing", "v");
    // printArr(result5);
    setEqual(expected5, result5);

    expected6 = new String[] { "What should I wear today?",
	"He always wears a smile", "wear one's hair in a certain way",
	"She was wearing yellow that day" };
    result6 = w.getAllExamples("wear", "v");
    // printArr(result6);
    setEqual(expected6, result6);

    expected7 = new String[] { "they shouted happily",
	"happily he was not injured" };
    result7 = w.getAllExamples("happily", "r");
    // printArr(result7);
    setEqual(expected7, result7);

    expected8 = new String[] { "a fat land", "fatty food",
	"he hadn't remembered how fat she was", "fat tissue", "a nice fat job",
	"a fat rope" };
    result8 = w.getAllExamples("fat", "a");
    // printArr(result8);
    setEqual(expected8, result8);

    expected9 = new String[] {};
    result9 = w.getAllExamples("fatttttt", "a");
    // printArr(result9);
    setEqual(expected9, result9);
    setEqual(EMPTY, w.getAllExamples("fatttttt", "u"));

  }

  @Test
  public void testGetCommonParents() {

    String[] expected = { "wear", "habiliment", "vesture", "wearable",
	"article of clothing", "clothing" };
    String[] result = w.getCommonParents("activewear", "beachwear", "n");
    // printArr(result);
    setEqual(expected, result);

    String[] expected2 = { "hymenopterous insect", "hymenopter",
	"hymenopteron", "hymenopteran" };
    String[] result2 = w.getCommonParents("bee", "ant", "n");
    // printArr(result2);
    setEqual(expected2, result2);

    String[] expected3 = { "physical entity" };
    String[] result3 = w.getCommonParents("bee", "wood", "n");
    // printArr(result3);
    setEqual(expected3, result3);

    String[] expected4 = { "entity" };
    String[] result4 = w.getCommonParents("bee", "run", "n");
    // printArr(result4);
    setEqual(expected4, result4);

    String[] expected5 = {};
    String[] result5 = w.getCommonParents("beeesdasd", "run", "n");
    // printArr(result5);
    setEqual(expected5, result5);

    String[] expected6 = {};
    String[] result6 = w.getCommonParents("beeesdasd", "runasdasdasd", "n");
    // printArr(result6);
    setEqual(expected6, result6);

    String[] expected7 = {};
    String[] result7 = w.getCommonParents("flower", "runasdasdasd", "n");
    // printArr(result7);
    setEqual(expected7, result7);

    String[] expected8 = {};
    String[] result8 = w.getCommonParents("flower", "happily", "v");
    // printArr(result8);
    setEqual(expected8, result8);

    String[] expected9 = {};
    String[] result9 = w.getCommonParents("flower", "happily", "r");
    // printArr(result9);
    setEqual(expected9, result9);

    String[] expected10 = {};
    String[] result10 = w.getCommonParents("flower", "happily", "a");
    // printArr(result10);
    setEqual(expected10, result10);

    String[] expected11 = {};
    String[] result11 = w.getCommonParents("sadly", "happily", "r");
    // printArr(result11);
    setEqual(expected11, result11);

    String[] expected12 = {};
    String[] result12 = w.getCommonParents("fat", "thin", "a");
    // printArr(result12);
    setEqual(expected12, result12);

    String[] expected13 = {};
    String[] result13 = w.getCommonParents("go", "run", "v");
    // printArr(result13);
    setEqual(expected13, result13);

    setEqual(EMPTY, w.getCommonParents("fatttttt", "sad", "j"));

    w.ignoreCompoundWords(false);
    w.ignoreUpperCaseWords(false);

    expected = new String[] { "wear", "habiliment", "vesture", "wearable",
	"article of clothing", "clothing" };
    result = w.getCommonParents("activewear", "beachwear", "n");
    // printArr(result);
    setEqual(expected, result);

    expected2 = new String[] { "hymenopterous insect", "hymenopter",
	"hymenopteron", "hymenopteran" };
    result2 = w.getCommonParents("bee", "ant", "n");
    // printArr(result2);
    setEqual(expected2, result2);

    expected3 = new String[] { "physical entity" };
    result3 = w.getCommonParents("bee", "wood", "n");
    // printArr(result3);
    setEqual(expected3, result3);

    expected4 = new String[] { "entity" };
    result4 = w.getCommonParents("bee", "run", "n");
    // printArr(result4);
    setEqual(expected4, result4);

    expected5 = new String[] {};
    result5 = w.getCommonParents("beeesdasd", "run", "n");
    // printArr(result5);
    setEqual(expected5, result5);

    expected6 = new String[] {};
    result6 = w.getCommonParents("beeesdasd", "runasdasdasd", "n");
    // printArr(result6);
    setEqual(expected6, result6);

    expected7 = new String[] {};
    result7 = w.getCommonParents("flower", "runasdasdasd", "n");
    // printArr(result7);
    setEqual(expected7, result7);

    expected8 = new String[] {};
    result8 = w.getCommonParents("flower", "happily", "v");
    // printArr(result8);
    setEqual(expected8, result8);

    expected9 = new String[] {};
    result9 = w.getCommonParents("flower", "happily", "r");
    // printArr(result9);
    setEqual(expected9, result9);

    expected10 = new String[] {};
    result10 = w.getCommonParents("flower", "happily", "a");
    // printArr(result10);
    setEqual(expected10, result10);

    expected11 = new String[] {};
    result11 = w.getCommonParents("sadly", "happily", "r");
    // printArr(result11);
    setEqual(expected11, result11);

    expected12 = new String[] {};
    result12 = w.getCommonParents("fat", "thin", "a");
    // printArr(result12);
    setEqual(expected12, result12);

    expected13 = new String[] {};
    result13 = w.getCommonParents("go", "run", "v");
    // printArr(result13);
    setEqual(expected13, result13);

    setEqual(EMPTY, w.getCommonParents("fatttttt", "sad", "j"));

  }

  @Test
  public void testGetCommonParent() {

    // println("getCommonParent: "+w.getCommonParent(94292941, 82817909));
    equal(w.getCommonParent(94292941, 82817909), -1);

    int expected = 93055525;
    int result = w.getCommonParent(94292941, 92817909);
    // println("getCommonParent: "+result);
    equal(expected, result); // DCH: changed from KENNY's version

    int expected2 = 93055525;
    int result2 = w.getCommonParent(94292941, 92817909);
    // println(result2);
    equal(expected2, result2);

    equal(w.getCommonParent(84292941, 92817909), -1);
    equal(w.getCommonParent(84292941, 82817909), -1);

    w.ignoreCompoundWords(true);
    w.ignoreUpperCaseWords(true);

    expected = 93055525;
    result = w.getCommonParent(94292941, 92817909);
    // println("getCommonParent: "+result);
    equal(expected, result); // DCH: changed from KENNY's version

    // println(w.getSenseIds("activewear", "n"));
    // println(w.getSenseIds("beachwear", "n"));
    expected2 = 93055525;
    result2 = w.getCommonParent(94292941, 92817909);
    // println(result2);
    equal(expected2, result2);

    equal(w.getCommonParent(94292941, 82817909), -1);
    equal(w.getCommonParent(84292941, 92817909), -1);
    equal(w.getCommonParent(84292941, 82817909), -1);
  }

  @Test
  public void testGetSynsetStringStringIntBool() {

    int[] is = w.getSenseIds("dog", "n");
    for (int i = 0; i < is.length; i++) {
      	setEqual(w.getSynset(is[i]), w.getSynset("dog", "n", i+1, true));
    }
  }


  @Test
  public void testGetSynsetStringStringInt() {
  
    //setEqual(w.getSynset(92086723), w.getSynset("dog", "n", 1));

    int[] is = w.getSenseIds("dog", "n");
    for (int i = 0; i < is.length; i++) {
      	String[] result = w.getSynset(is[i]);
      	List tmp = new ArrayList(Arrays.asList(result));
      	tmp.remove("dog"); 
      	setEqual(tmp.toArray(new String[0]), w.getSynset("dog", "n", i+1));
    }
  }
  
  @Test
  public void testGetSynsetStringString() {

    // printArr(w.getSynset("medicare","n"));
    String[] expected = { "Medicare" };
    setEqualMulti(expected, "getSynset", "medicare", "n");

    String[] expected4 = {};
    // printArr(w.getSynset("Medicare","n"));
    setEqualMulti(expected4, "getSynset", "Medicare", "n");

    String[] expected2 = {};
    // printArr(w.getSynset("health insurance","n"));
    setEqualMulti(expected2, "getSynset", "health insurance", "n");

    // assertTrue(Arrays.asList(expected).containsAll(Arrays.asList(result)));

    String[] expected3 = { "athletic wear", "sportswear" };
    // printArr(w.getSynset("activewear", "n"));
    setEqualMulti(expected3, "getSynset", "activewear", "n");

    String[] expected5 = {};
    // printArr(w.getSynset("activewear", "v"));
    setEqualMulti(expected5, "getSynset", "activewear", "v");

    String[] expected6 = {};
    // printArr(w.getSynset("activewear", "a"));
    setEqualMulti(expected6, "getSynset", "activewear", "a");

    String[] expected7 = {};
    // printArr(w.getSynset("activewear", "r"));
    setEqualMulti(expected7, "getSynset", "activewear", "r");

    String[] expected8 = {};
    // printArr(w.getSynset("nosuchword", "n"));
    setEqualMulti(expected8, "getSynset", "nosuchword", "n");

    String[] expected9 = { "lope", "jog" };
    // printArr(w.getSynset("trot", "n"));
    setEqualMulti(expected9, "getSynset", "trot", "n");

    String[] expected10 = { "ramble on", "ramble" };
    // printArr(w.getSynset("jog", "v"));
    setEqualMulti(expected10, "getSynset", "jog", "v");

    String[] expected11 = {};
    // printArr(w.getSynset("pretty", "a"));
    setEqualMulti(expected11, "getSynset", "pretty", "a");

    String[] expected12 = { "tardily", "easy", "slow" };
    // printArr(w.getSynset("slowly", "r"));
    setEqualMulti(expected12, "getSynset", "slowly", "r");

    setEqual(w.getSynset("nosuchword", "t"), EMPTY);
  }

  @Test
  public void testGetSynsetStringStringBoolean() {

    String[] expected2 = { "sportswear", "athletic wear" };
    String[] result2 = w.getSynset("activewear", "n", false);
    // printArr(result2);
    setEqual(expected2, result2);

    String[] expected = { "activewear", "sportswear", "athletic wear" };
    String[] result = w.getSynset("activewear", "n", true);
    // println(w.getSenseIds("search", "v"));
    setEqual(expected, result);
    // assertTrue(Arrays.asList(expected).containsAll(Arrays.asList(result)));

    String[] expected3 = { "have on" };
    String[] result3 = w.getSynset("wear", "v", false);
    // printArr(result3);
    setEqual(expected3, result3);

    String[] expected7 = { "wear", "have on" };
    String[] result7 = w.getSynset("wear", "v", true);
    // printArr(result7);
    setEqual(expected7, result7);

    String[] expected4 = {};
    String[] result4 = w.getSynset("fat", "a", false);
    // printArr(result4);
    setEqual(expected4, result4);

    String[] expected8 = { "fat" };
    String[] result8 = w.getSynset("fat", "a", true);
    // printArr(result8);
    setEqual(expected8, result8);

    String[] expected5 = { "unhappily" };
    String[] result5 = w.getSynset("sadly", "r", false);
    // printArr(result5);
    setEqual(expected5, result5);

    String[] expected9 = { "sadly", "unhappily" };
    String[] result9 = w.getSynset("sadly", "r", true);
    // printArr(result9);
    setEqual(expected9, result9);

    String[] expected6 = {};
    String[] result6 = w.getSynset("nusuchword", "v", false);
    // printArr(result6);
    setEqual(expected6, result6);

    String[] expected10 = {};
    String[] result10 = w.getSynset("nusuchword", "v", true);
    // printArr(result10);
    setEqual(expected10, result10);

  }

  @Test
  public void testGetSynsetInt() // answers should contain ALL words in Synset
  {
    /* medicare = 01090933 health insurance = 13367788 activewear = 04292941 */

    String[] expected = { "sportswear", "athletic wear", "activewear" };
    String[] result = w.getSynset(94292941);
    // println(w.getSenseIds("activewear", "n"));
    // printArr(result);
    setEqual(expected, result);
    // assertTrue(Arrays.asList(expected).containsAll(Arrays.asList(result)));

    String[] expected3 = { "Medicare" };
    int[] a = w.getSenseIds("medicare", "n");
    // println(a); // 91090933
    String[] result3 = w.getSynset(91090933);
    // println(w.getSenseIds("medicare", "n"));
    // printArr(result3);
    setEqual(expected, result);
    // assertTrue(Arrays.asList(expected).containsAll(Arrays.asList(result)));

    setEqual(w.getSynset(9429294), EMPTY);

    w.ignoreCompoundWords(true);
    w.ignoreUpperCaseWords(true);

    String[] expected4 = { "sportswear", "activewear" };
    String[] result4 = w.getSynset(94292941);
    // println(w.getSenseIds("activewear", "n"));
    // printArr(result);
    setEqual(expected4, result4);
    // assertTrue(Arrays.asList(expected).containsAll(Arrays.asList(result)));

    expected3 = new String[] { "Medicare" };
    a = w.getSenseIds("medicare", "n");
    // rintln(a); // 91090933
    result3 = w.getSynset(91090933);
    // println(w.getSenseIds("activewear", "n"));
    // printArr(result3);
    setEqual(expected, result);
    // assertTrue(Arrays.asList(expected).containsAll(Arrays.asList(result)));

    setEqual(w.getSynset(9429294), EMPTY);
  }

  @Test
  public void testGetAllSynsets() {

    String[] expected = { "Medicare" };
    // printArr(w.getAllSynsets("medicare","n"));
    setEqualMulti(expected, "getAllSynsets", "medicare", "n");

    String[] expected4 = {};
    // printArr(w.getAllSynsets("Medicare","n"));
    setEqualMulti(expected4, "getAllSynsets", "Medicare", "n");

    String[] expected2 = {};
    // printArr(w.getAllSynsets("health insurance","n"));
    setEqualMulti(expected2, "getAllSynsets", "health insurance", "n");

    // assertTrue(Arrays.asList(expected).containsAll(Arrays.asList(result)));

    String[] expected3 = { "athletic wear", "sportswear" };
    // printArr(w.getAllSynsets("activewear", "n"));
    setEqualMulti(expected3, "getAllSynsets", "activewear", "n");

    String[] expected5 = {};
    // printArr(w.getAllSynsets("activewear", "v"));
    setEqualMulti(expected5, "getAllSynsets", "activewear", "v");

    String[] expected6 = {};
    // printArr(w.getAllSynsets("activewear", "a"));
    setEqualMulti(expected6, "getAllSynsets", "activewear", "a");

    String[] expected7 = {};
    // printArr(w.getAllSynsets("activewear", "r"));
    setEqualMulti(expected7, "getAllSynsets", "activewear", "r");

    String[] expected8 = {};
    // printArr(w.getAllSynsets("nosuchword", "n"));
    setEqualMulti(expected8, "getAllSynsets", "nosuchword", "n");

    String[] expected9 = { "Trot", "Trotskyite", "crib", "pony", "Trotskyist",
	"lope", "jog" };
    // printArr(w.getAllSynsets("trot", "n"));
    setEqualMulti(expected9, "getAllSynsets", "trot", "n");

    String[] expected10 = { "even up", "ramble on", "trot", "ramble",
	"square up", "clip" };
    // printArr(w.getAllSynsets("jog", "v"));
    setEqualMulti(expected10, "getAllSynsets", "jog", "v");

    String[] expected11 = {};
    // printArr(w.getAllSynsets("pretty", "a"));
    setEqualMulti(expected11, "getAllSynsets", "pretty", "a");

    String[] expected12 = { "lento", "slow", "easy", "tardily" };
    // printArr(w.getAllSynsets("slowly", "r"));
    setEqualMulti(expected12, "getAllSynsets", "slowly", "r");

    setEqualMulti(expected11, "getAllSynsets", "nosuchword", "t");

  }

  @Test
  public void testGetSenseCount() {

    int expected = 6;
    int result = w.getSenseCount("table", "n");
    // println(w.getSenseIds("activewear", "n"));
    // println(result);
    equal(expected, result);

    int expected2 = 2;
    int result2 = w.getSenseCount("table", "v");
    // println(w.getSenseIds("activewear", "n"));
    // println(result);
    equal(expected2, result2);

    int expected3 = 0;
    int result3 = w.getSenseCount("nosuchword", "r");
    // println(w.getSenseIds("activewear", "n"));
    // println(result);
    equal(expected3, result3);

    equal(w.getSenseCount("table", "j"), 0);

    w.ignoreCompoundWords(true);
    w.ignoreUpperCaseWords(true);

    expected = 6;
    result = w.getSenseCount("table", "n");
    // println(w.getSenseIds("activewear", "n"));
    // println(result);
    equal(expected, result);

    expected2 = 2;
    result2 = w.getSenseCount("table", "v");
    // println(w.getSenseIds("activewear", "n"));
    // println(result);
    equal(expected2, result2);

    expected3 = 0;
    result3 = w.getSenseCount("nosuchword", "r");
    // println(w.getSenseIds("activewear", "n"));
    // println(result);
    equal(expected3, result3);

    equal(w.getSenseCount("table", "j"), 0);

  }

  @Test
  public void testGetAntonymsStringString() {

    setEqual(w.getAntonyms("day", "n"), new String[] { "night" });
    
    setEqual(w.getAntonyms("night", "n"), new String[] { "day" });

    setEqual(w.getAntonyms("left", "a"), new String[] { "right" });
    setEqual(w.getAntonyms("right", "a"), new String[] { "left" });

    setEqual(w.getAntonyms("full", "a"), new String[] { "empty" });
    setEqual(w.getAntonyms("empty", "a"), new String[] { "full" });

    setEqual(w.getAntonyms("quickly", "r"), new String[] { "slowly" });
    setEqual(w.getAntonyms("slowly", "r"), new String[] { "quickly" });

    setEqual(w.getAntonyms("smoothly", "r"), new String[] {});
    setEqual(w.getAntonyms("", "r"), new String[] {});

    setEqual(w.getAntonyms("smoothlyyyyyyyyyyyy", "r"), new String[] {});

    w.ignoreCompoundWords(true);
    w.ignoreUpperCaseWords(true);

    setEqual(w.getAntonyms("day", "n"), new String[] { "night" });
    
    setEqual(w.getAntonyms("night", "n"), new String[] { "day" });

    setEqual(w.getAntonyms("left", "a"), new String[] { "right" });
    setEqual(w.getAntonyms("right", "a"), new String[] { "left" });

    setEqual(w.getAntonyms("full", "a"), new String[] { "empty" });
    setEqual(w.getAntonyms("empty", "a"), new String[] { "full" });

    setEqual(w.getAntonyms("quickly", "r"), new String[] { "slowly" });
    setEqual(w.getAntonyms("slowly", "r"), new String[] { "quickly" });

    setEqual(w.getAntonyms("smoothly", "r"), new String[] {});
    setEqual(w.getAntonyms("", "r"), new String[] {});

    setEqual(w.getAntonyms("smoothlyyyyyyyyyyyy", "r"), new String[] {});

    w.getAntonyms("smoothlyyyyyyyyyyyy", "u");
  }

  @Test
  public void testGetAntonymsInt() {

    String[] expected = { "day" };
    String[] result = w.getAntonyms(915192074);
    // println(w.getSenseIds("night", "n"));
    // println(result);
    setEqual(expected, result);
    // assertTrue(Arrays.asList(expected).containsAll(Arrays.asList(result)));

    String[] expected2 = { "night" };
    String[] result2 = w.getAntonyms(915190004);
    // //println(w.getSenseIds("day", "n"));
    // //println(result2);
    setEqual(expected2, result2);

    String[] expected3 = { "center", "right" };
    String[] result3 = w.getAntonyms(72038342);
    // //println(w.getSenseIds("left", "a"));
    // //println(result3);
    setEqual(expected3, result3);

    w.getAntonyms(62038342);

    w.ignoreCompoundWords(true);
    w.ignoreUpperCaseWords(true);

    expected = new String[] { "day" };
    result = w.getAntonyms(915192074);
    // println(w.getSenseIds("night", "n"));
    // println(result);
    setEqual(expected, result);
    // assertTrue(Arrays.asList(expected).containsAll(Arrays.asList(result)));

    expected2 = new String[] { "night" };
    result2 = w.getAntonyms(915190004);
    // //println(w.getSenseIds("day", "n"));
    // //println(result2);
    setEqual(expected2, result2);

    expected3 = new String[] { "center", "right" };
    result3 = w.getAntonyms(72038342);
    // //println(w.getSenseIds("left", "a"));
    // //println(result3);
    setEqual(expected3, result3);

    w.getAntonyms(62038342);

  }

  @Test
  public void testGetAllAntonyms() {

    String[] expected = { "day" };
    // printArr(w.getAllAntonyms("night", "n"));
    setEqualMulti(expected, "getAllAntonyms", "night", "n");
    // assertTrue(Arrays.asList(expected).containsAll(Arrays.asList(result)));

    String[] expected2 = { "night" };
    // printArr(w.getAllAntonyms("day", "n"));
    setEqualMulti(expected2, "getAllAntonyms", "day", "n");

    String[] expected3 = { "center", "right" };
    // printArr(w.getAllAntonyms("left", "a"));
    setEqualMulti(expected3, "getAllAntonyms", "left", "a");

    String[] expected4 = { "incorrect", "wrong", "center", "left" };
    // printArr(w.getAllAntonyms("right", "a"));
    setEqualMulti(expected4, "getAllAntonyms", "right", "a");

    String[] expected5 = { "slowly" };
    // printArr(w.getAllAntonyms("quickly", "r"));
    setEqualMulti(expected5, "getAllAntonyms", "quickly", "r");

    String[] expected6 = { "quickly" };
    // printArr(w.getAllAntonyms("slowly", "r"));
    setEqualMulti(expected6, "getAllAntonyms", "slowly", "r");

    String[] expected7 = {};
    // printArr(w.getAllAntonyms("slowly", "r"));
    setEqualMulti(expected7, "getAllAntonyms", "nusuchword", "r");

    w.getAllAntonyms("slowly", "j");

    w.ignoreCompoundWords(true);
    w.ignoreUpperCaseWords(true);

    expected = new String[] { "day" };
    // printArr(w.getAllAntonyms("night", "n"));
    setEqualMulti(expected, "getAllAntonyms", "night", "n");
    // assertTrue(Arrays.asList(expected).containsAll(Arrays.asList(result)));

    expected2 = new String[] { "night" };
    // printArr(w.getAllAntonyms("day", "n"));
    setEqualMulti(expected2, "getAllAntonyms", "day", "n");

    expected3 = new String[] { "center", "right" };
    // printArr(w.getAllAntonyms("left", "a"));
    setEqualMulti(expected3, "getAllAntonyms", "left", "a");

    expected4 = new String[] { "incorrect", "wrong", "center", "left" };
    // printArr(w.getAllAntonyms("right", "a"));
    setEqualMulti(expected4, "getAllAntonyms", "right", "a");

    expected5 = new String[] { "slowly" };
    // printArr(w.getAllAntonyms("quickly", "r"));
    setEqualMulti(expected5, "getAllAntonyms", "quickly", "r");

    expected6 = new String[] { "quickly" };
    // printArr(w.getAllAntonyms("slowly", "r"));
    setEqualMulti(expected6, "getAllAntonyms", "slowly", "r");

    expected7 = new String[] {};
    // printArr(w.getAllAntonyms("slowly", "r"));
    setEqualMulti(expected7, "getAllAntonyms", "nusuchword", "r");

    w.getAllAntonyms("slowly", "j");

  }

  @Test
  public void testGetHypernymsInt() {

    String[] expected = { "time period", "period", "period of time" };
    String[] result = w.getHypernyms(915192074);
    // println(w.getSenseIds("night", "n"));
    // printArr(result);
    setEqual(expected, result);
    // assertTrue(Arrays.asList(expected).containsAll(Arrays.asList(result)));

    String[] expected2 = { "period", "period of time", "time period" };
    String[] result2 = w.getHypernyms(915190004);
    // //println(w.getSenseIds("day", "n"));
    // printArr(result2);
    setEqual(expected2, result2);

    String[] expected3 = {};
    String[] result3 = w.getHypernyms(72038342);
    // //println(w.getSenseIds("left", "a"));
    // printArr(result3);
    setEqual(expected3, result3);

    w.getHypernyms(62038342);

    w.ignoreCompoundWords(true);
    w.ignoreUpperCaseWords(true);

    expected = new String[] { "period" };
    result = w.getHypernyms(915192074);
    // println(w.getSenseIds("night", "n"));
    // printArr(result);
    setEqual(expected, result);
    // assertTrue(Arrays.asList(expected).containsAll(Arrays.asList(result)));

    expected2 = new String[] { "period" };
    result2 = w.getHypernyms(915190004);
    // //println(w.getSenseIds("day", "n"));
    // printArr(result2);
    setEqual(expected2, result2);

    expected3 = new String[] {};
    result3 = w.getHypernyms(72038342);
    // //println(w.getSenseIds("left", "a"));
    // printArr(result3);
    setEqual(expected3, result3);

    w.getHypernyms(62038342);

  }

  @Test
  public void testGetAllHypernyms() {
    String[] expected = { "dark", "darkness", "period", "crepuscle",
	"time period", "gloaming", "time unit", "period of time", "crepuscule",
	"evenfall", "nightfall", "twilight", "fall", "dusk", "gloam",
	"unit of time" };
    // printArr(w.getAllHypernyms("night", "n"));
    setEqualMulti(expected, "getAllHypernyms", "night", "n");
    // assertTrue(Arrays.asList(expected).containsAll(Arrays.asList(result)));

    w.getAllHypernyms("night", "j");

  }

  /*
   * 0) 'night:nighttime:dark' 1) 'measure:quantity:amount' 2) 'period' 3)
   * 'entity' 4) 'abstraction'
   */
  @Test
  public void testGetHypernymTree() {

    int[] ids = w.getSenseIds("cat", "n");
    String[] htree = w.getHypernymTree(ids[0]);
    // RiTa.out(htree);
    
    System.out.println();
    
    ids = w.getSenseIds("dog", "n");
    htree = w.getHypernymTree(ids[0]);
    // RiTa.out(htree);
    // System.out.println();
   
    
    ids = w.getSenseIds("cell", "n");
    htree = w.getHypernymTree(ids[0]);
    // RiTa.out(htree);
    
    if (1==1) return;
    
    String[] expected = { "time period:period of time:period",
	"fundamental quantity:fundamental measure",
	"abstraction:abstract entity", "measure:quantity:amount",
	"night:nighttime:dark", "entity" };
    String[] result = w.getHypernymTree(915192074);
    // printArr(w.getHypernymTree(915192074));
    setEqual(expected, result);

    w.getHypernymTree(815192074);

    w.ignoreCompoundWords(true);
    w.ignoreUpperCaseWords(true);

    String[] expected2 = { "night:nighttime:dark", "measure:quantity:amount",
	"entity", "period", "abstraction" };
    String[] result2 = w.getHypernymTree(915192074);
    // printArr(w.getHypernymTree(915192074));
    setEqual(expected2, result2);

    w.getHypernymTree(815192074);    
  }

  @Test
  public void testGetHyponymsStringString() {
    String[] expected = { "weeknight", "wedding night" };
    // printArr( w.getHyponyms("night", "n"));
    setEqualMulti(expected, "getHyponyms", "night", "n");

    String[] expected2 = {};
    // printArr( w.getHyponyms("night", "n"));
    setEqualMulti(expected2, "getHyponyms", "nosuchword", "n");

    w.getHyponyms("night", "j");

  }

  @Test
  public void testGetHyponymsInt() {

    String[] expected = { "weeknight", "wedding night" };
    String[] result = w.getHyponyms(915192074);
    // printArr(w.getHyponyms(915192074));
    setEqual(expected, result);

    w.getHyponyms(815192074);

    w.ignoreCompoundWords(true);
    w.ignoreUpperCaseWords(true);

    expected = new String[] { "weeknight" };
    result = w.getHyponyms(915192074);
    // printArr(w.getHyponyms(915192074));
    setEqual(expected, result);

    w.getHyponyms(815192074);

    w.ignoreCompoundWords(false);
    w.ignoreUpperCaseWords(false);

  }

  @Test
  public void testGetAllHyponyms() {

    String[] expected = { "wedding night", "weeknight" };
    // printArr(w.getAllHyponyms("night", "n"));
    setEqualMulti(expected, "getAllHyponyms", "night", "n");
    // assertTrue(Arrays.asList(expected).containsAll(Arrays.asList(result)));

    String[] expected2 = {};
    // printArr(w.getAllHyponyms("nosuchword", "n"));
    setEqualMulti(expected2, "getAllHyponyms", "nosuchword", "n");
    // assertTrue(Arrays.asList(expected).containsAll(Arrays.asList(result)));

    w.getAllHyponyms("night", "j");
  }

  @Test
  public void testGetHyponymTree() {

    String[] expected = { "weeknight", "wedding night" };
    String[] result = w.getHyponymTree(915192074);
    // printArr(w.getHyponymTree(915192074));
    setEqual(expected, result);

    w.getHyponyms(815192074);

    w.ignoreCompoundWords(true);
    w.ignoreUpperCaseWords(true);

    expected = new String[] { "weeknight" };
    result = w.getHyponymTree(915192074);
    // printArr(w.getHyponymTree(915192074));
    setEqual(expected, result);

    w.getHyponymTree(815192074);

  }

  @Test
  public void testIsAdjective() {

    RiWordNet.useMorphologicalProcessor = true;
    equal(true, w.isAdjective("biggest"));
    RiWordNet.useMorphologicalProcessor = false;
    equal(!true, w.isAdjective("biggest"));

    equal(true, w.isAdjective("big"));
    equal(true, w.isAdjective("bigger"));
    equal(true, w.isAdjective("old"));
    equal(true, w.isAdjective("elder"));
    equal(true, w.isAdjective("eldest"));

    equal(false, w.isAdjective("Eldest"));
    equal(false, w.isAdjective("eLdest"));
    equal(false, w.isAdjective("eldest "));
    equal(false, w.isAdjective(" eldest"));
    equal(false, w.isAdjective(" eldest "));
    equal(false, w.isAdjective("eldest	"));
    equal(false, w.isAdjective("	eldest "));
    equal(false, w.isAdjective("eldest eldest"));

    equal(false, w.isAdjective("slowly"));
    equal(false, w.isAdjective("quite"));

    equal(false, w.isAdjective(""));
    equal(false, w.isAdjective("asdasdasdsad"));
    equal(false, w.isAdjective("$%^&*()"));

    w.ignoreCompoundWords(true);
    w.ignoreUpperCaseWords(true);

    RiWordNet.useMorphologicalProcessor = true;
    equal(true, w.isAdjective("biggest"));
    RiWordNet.useMorphologicalProcessor = false;
    equal(!true, w.isAdjective("biggest"));

    equal(true, w.isAdjective("big"));
    equal(true, w.isAdjective("bigger"));
    equal(true, w.isAdjective("old"));
    equal(true, w.isAdjective("elder"));
    equal(true, w.isAdjective("eldest"));
    equal(false, w.isAdjective("Eldest"));
    equal(false, w.isAdjective("eLdest"));
    equal(false, w.isAdjective("eldest "));
    equal(false, w.isAdjective(" eldest"));
    equal(false, w.isAdjective(" eldest "));
    equal(false, w.isAdjective("eldest	"));
    equal(false, w.isAdjective("	eldest "));
    equal(false, w.isAdjective("eldest eldest"));
    equal(false, w.isAdjective("slowly"));
    equal(false, w.isAdjective("quite"));

    equal(false, w.isAdjective(""));
    equal(false, w.isAdjective("asdasdasdsad"));
    equal(false, w.isAdjective("$%^&*()"));
  }

  @Test
  public void testIsAdverb() {

    w.ignoreCompoundWords(false);
    w.ignoreUpperCaseWords(false);

    equal(false, w.isAdverb("mary"));
    equal(false, w.isAdverb("marry"));
    equal(true, w.isAdverb("slowly"));
    equal(true, w.isAdverb("quite"));
    equal(true, w.isAdverb("together"));
    equal(false, w.isAdverb("Together"));
    equal(false, w.isAdverb("toGether"));
    equal(false, w.isAdverb("together "));
    equal(false, w.isAdverb(" together"));
    equal(false, w.isAdverb(" together "));
    equal(false, w.isAdverb("together	"));
    equal(false, w.isAdverb("	together "));
    equal(false, w.isAdverb("together together"));

    equal(false, w.isAdverb(""));
    equal(false, w.isAdverb("asdasdasdsad"));
    equal(false, w.isAdverb("$%^&*()"));

    w.ignoreCompoundWords(true);
    w.ignoreUpperCaseWords(true);

    equal(false, w.isAdverb("mary"));
    equal(false, w.isAdverb("marry"));
    equal(true, w.isAdverb("slowly"));
    equal(true, w.isAdverb("quite"));
    equal(true, w.isAdverb("together"));
    equal(false, w.isAdverb("Together"));
    equal(false, w.isAdverb("toGether"));
    equal(false, w.isAdverb("together "));
    equal(false, w.isAdverb(" together"));
    equal(false, w.isAdverb(" together "));
    equal(false, w.isAdverb("together "));
    equal(false, w.isAdverb(" together "));
    equal(false, w.isAdverb("together together"));

    equal(false, w.isAdverb(""));
    equal(false, w.isAdverb("asdasdasdsad"));
    equal(false, w.isAdverb("$%^&*()"));
  }

  @Test
  public void testIsVerb() {

    equal(false, w.isVerb("mary"));
    equal(true, w.isVerb("marry"));
    equal(true, w.isVerb("run"));
    equal(true, w.isVerb("walk"));
    equal(true, w.isVerb("sing"));
    equal(false, w.isVerb("Sing"));
    equal(false, w.isVerb("sIng"));
    equal(false, w.isVerb("sing "));
    equal(false, w.isVerb(" sing"));
    equal(false, w.isVerb(" sing "));
    equal(false, w.isVerb("sing	"));
    equal(false, w.isVerb("	sing "));
    equal(false, w.isVerb("sing sing"));

    equal(false, w.isVerb(""));
    equal(false, w.isVerb("asdasdasdsad"));
    equal(false, w.isVerb("$%^&*()"));

    w.ignoreCompoundWords(true);
    w.ignoreUpperCaseWords(true);

    equal(false, w.isVerb("mary"));
    equal(true, w.isVerb("marry"));
    equal(true, w.isVerb("run"));
    equal(true, w.isVerb("walk"));
    equal(false, w.isVerb("Sing"));
    equal(false, w.isVerb("sIng"));
    equal(false, w.isVerb("sing "));
    equal(false, w.isVerb(" sing"));
    equal(false, w.isVerb(" sing "));
    equal(false, w.isVerb("sing "));
    equal(false, w.isVerb(" sing "));
    equal(false, w.isVerb("sing sing"));

    equal(false, w.isVerb(""));
    equal(false, w.isVerb("asdasdasdsad"));
    equal(false, w.isVerb("$%^&*()"));
  }

  @Test
  public void testIsNoun() {

    equal(true, w.isNoun("mary"));
    equal(true, w.isNoun("run"));
    equal(false, w.isNoun("slowly"));
    equal(false, w.isNoun("together"));

    equal(false, w.isNoun("Run"));
    equal(false, w.isNoun("rUn"));
    equal(false, w.isNoun("run "));
    equal(false, w.isNoun(" run"));
    equal(false, w.isNoun(" run "));
    equal(false, w.isNoun("run	"));
    equal(false, w.isNoun("	run "));
    equal(false, w.isNoun("run run"));

    equal(false, w.isNoun(""));
    equal(false, w.isNoun("asdasdasdsad"));
    equal(false, w.isNoun("$%^&*()"));

    w.ignoreCompoundWords(true);
    w.ignoreUpperCaseWords(true);

    equal(true, w.isNoun("mary"));
    equal(true, w.isNoun("run"));
    equal(false, w.isNoun("slowly"));
    equal(false, w.isNoun("together"));

    equal(false, w.isNoun("Run"));
    equal(false, w.isNoun("rUn"));
    equal(false, w.isNoun("run "));
    equal(false, w.isNoun(" run"));
    equal(false, w.isNoun(" run "));
    equal(false, w.isNoun("run  "));
    equal(false, w.isNoun(" run "));
    equal(false, w.isNoun("run run"));

    equal(false, w.isNoun(""));
    equal(false, w.isNoun("asdasdasdsad"));
    equal(false, w.isNoun("$%^&*()"));
  }

  @Test
  public void testGetStems() {
    String[] expected = { "produce" };
    String[] result = w.getStems("produced", "v");
    setEqual(expected, result);

    expected = new String[] { "run" };
    result = w.getStems("running", "v");
    setEqual(expected, result);

    expected = w.getStems("cakes", "n");
    setEqual(expected, new String[] { "cake" });

    String[] tests = { "run", "runs", "running" };
    for (int i = 0; i < tests.length; i++)
      setEqual(w.getStems(tests[i], "v"), new String[] { "run" });

    expected = w.getStems("gases", "n");
    setEqual(expected, new String[] { "gas" });

    setEqual(w.getStems("buses", "n"), new String[] { "bus" });
    setEqual(w.getStems("happiness", "n"), new String[] { "happiness" });
    setEqual(w.getStems("terrible", "a"), new String[] { "terrible" });

    expected = w.getStems("terribl", "a");
    setEqual(expected, EMPTY);
    setEqual(w.getStems("terrible", "z"), EMPTY);
  }

  @Test
  public void testIsStem() {
    for (int i = 0; i < 2; i++) {
      w.ignoreCompoundWords(i == 0 ? false : true);
      w.ignoreUpperCaseWords(i == 0 ? false : true);

      equal(false, w.isStem("waiting", "v"));
      equal(true, w.isStem("wait", "n"));
      equal(false, w.isStem("", "n"));
      equal(false, w.isStem("asdasdasdsad", "n"));
      equal(false, w.isStem("$%^&*()", "n"));
      equal(false, w.isStem("Wait", "n"));
      equal(false, w.isStem("wAit", "n"));
      equal(false, w.isStem(" wait", "n"));
      equal(false, w.isStem(" wait ", "n"));
      equal(false, w.isStem("wait ", "n"));
      equal(false, w.isStem(" wait ", "n"));
      equal(false, w.isStem("wait wait", "n"));
      equal(false, w.isStem("wait ", "n"));
      equal(false, w.isStem("hey", "j"));
    }
  }

  @Test
  public void testExists() {
    for (int i = 0; i < 2; i++) {
      boolean val = i == 0 ? false : true;
      w.ignoreCompoundWords(val);
      w.ignoreUpperCaseWords(val);

      equal(true, w.exists("abc"));
      equal(true, w.exists("wait"));
      equal(false, w.exists("tesxx"));
      equal(true, w.exists("health insurance"));

      equal(false, w.exists("123"));
      equal(false, w.exists("#$%^&*()"));
      equal(false, w.exists("tes$%^"));

      equal(false, w.exists("Run"));
      equal(false, w.exists("rUn"));
      equal(false, w.exists("run "));
      equal(false, w.exists(" run"));
      equal(false, w.exists(" run "));
      equal(false, w.exists("run	"));
      equal(false, w.exists("	run "));
      equal(false, w.exists("run run"));
    }
  }

  @Test
  public void testRemoveNonExistent() {
    String[] testArray = { "abc", "wait", "tesxx" };
    String[] expected = { "abc", "wait" };
    List<String> testlist = new ArrayList<String>(Arrays.asList(testArray));
    List<String> expectedlist = new ArrayList<String>(Arrays.asList(expected));
    w.removeNonExistent(testlist);
    deepEqual(testlist, expectedlist);

  }

  @Test
  public void testGetPosString() {
    for (int i = 0; i < 2; i++) {
      boolean val = i == 0 ? false : true;
      w.ignoreCompoundWords(val);
      w.ignoreUpperCaseWords(val);

      String[] expected = { "v", "n" };
      String[] result = w.getPos("wait");
      // println(result);
      deepEqual(expected, result);
      // assertTrue(Arrays.asList(expected).containsAll(Arrays.asList(result)));

      String[] expected2 = { "a", "n", "r" };
      String[] result2 = w.getPos("deep");
      // printArr(result2);
      deepEqual(expected2, result2);

      String[] expected3 = {};
      String[] result3 = w.getPos("noSuchWord");
      // printArr(result3);
      deepEqual(expected3, result3);
    }

  }

  @Test
  public void testGetPosInt() {
    for (int i = 0; i < 2; i++) {
      w.ignoreCompoundWords(i == 0 ? false : true);
      w.ignoreUpperCaseWords(i == 0 ? false : true);

      equal("n", w.getPos(915297015));
      equal("n", w.getPos(91065863));
      equal("v", w.getPos(82644022));
      equal("v", w.getPos(82647547));
      equal("v", w.getPos(8721987));
      equal("v", w.getPos(82418420));
      ok(w.getPos(72418420) == null);
    }
  }

  @Test
  public void testGetPosStr() {

    for (int i = 0; i < 2; i++) {
      boolean val = i == 0 ? false : true;
      w.ignoreCompoundWords(val);
      w.ignoreUpperCaseWords(val);
      w.ignoreCompoundWords(false);
      w.ignoreUpperCaseWords(false);

      String expected = "vn";
      String result = w.getPosStr("wait");
      // println(w.getSenseIds("night", "n"));
      // println(result);
      equal(expected, result);
      // assertTrue(Arrays.asList(expected).containsAll(Arrays.asList(result)));

      String expected2 = "anr";
      String result2 = w.getPosStr("deep");
      // println(result2);
      deepEqual(expected2, result2);

      String expected3 = "";
      String result3 = w.getPosStr("noSuchWord");
      // println(result3);
      deepEqual(expected3, result3);

    }

  }

  @Test
  public void testGetBestPos() {
    equal("v", w.getBestPos("wait"));
    equal("n", w.getBestPos("dog"));

    equal("r", w.getBestPos("happily"));
    equal("a", w.getBestPos("happy"));

    equal(null, w.getBestPos("haPpily"));
    equal(null, w.getBestPos("happY"));
    equal(null, w.getBestPos(" happily"));
    equal(null, w.getBestPos("happY "));
    equal(null, w.getBestPos("	happily"));
    equal(null, w.getBestPos("happY	"));
    equal(null, w.getBestPos("	happY	"));

    equal(null, w.getBestPos("noSuchWord"));
    equal(null, w.getBestPos("n#$%^chWord"));
    equal(null, w.getBestPos(""));
  }

  @Test
  public void testGetRandomExample() {
    for (int i = 0; i < 2; i++) {
      boolean val = i == 0 ? false : true;
      w.ignoreCompoundWords(val);
      w.ignoreUpperCaseWords(val);

      ok(w.getRandomExample("r"));
      // println(w.getRandomExample("n"));

      ok(w.getRandomExample("v"));
      // println(w.getRandomExample("v"));

      ok(w.getRandomExample("a"));
      // println(w.getRandomExample("a"));

      ok(w.getRandomExample("r"));
      // println(w.getRandomExample("r"));

      // println(w.getRandomExample("j"));

      equal(null, w.getRandomExample("j"));
    }
  }

  @Test
  public void testGetRandomExamples() {
    w.ignoreCompoundWords(false); // does not affect the result
    w.ignoreUpperCaseWords(false);

    String[] result = w.getRandomExamples("r", 5);
    // println(result);
    equal(result.length, 5);

    String[] result2 = w.getRandomExamples("n", 50);
    // println(result);
    equal(result2.length, 50);

    String[] result4 = w.getRandomExamples("a", 1);
    // println(result);
    equal(result4.length, 1);

    setEqual(w.getRandomExamples("j", 1), EMPTY);

    String[] result3 = w.getRandomExamples("r", 5000); // fail when too large?
    // println(result);
    equal(result3.length, 5000);
  }

  @Test
  public void testGetRandomWords() {
    String[] result3 = w.getRandomWords("r", 5000);
    // println(result);
    equal(result3.length, 5000);
    for (int i = 0; i < result3.length; i++) {
      ok(w.isAdverb(result3[i]));
    }

    for (int j = 0; j < 2; j++) {
      boolean val = j == 0 ? false : true;
      w.ignoreCompoundWords(val);
      w.ignoreUpperCaseWords(val);

      String[] result = w.getRandomWords("v", 5);
      // println(result);
      equal(result.length, 5);
      for (int i = 0; i < result.length; i++) {
	ok(w.isVerb(result[i]));
      }

      String[] result2 = w.getRandomWords("n", 50);
      // println(result);
      equal(result2.length, 50);
      for (int i = 0; i < result2.length; i++) {
	ok(w.isNoun(result2[i]));
      }

      String[] result4 = w.getRandomWords("a", 1);
      // println(result);
      equal(result4.length, 1);
      for (int i = 0; i < result4.length; i++) {
	ok(w.isAdjective(result4[i]));
      }

      setEqual(EMPTY, w.getRandomWords("j", 1));

    }
  }

  @Test
  public void testGetRandomWordString() {
    for (int i = 0; i < 2; i++) {
      boolean val = i == 0 ? false : true;
      w.ignoreCompoundWords(val);
      w.ignoreUpperCaseWords(val);

      // println(w.getRandomWord("n"));
      ok(w.isNoun(w.getRandomWord("n")));
      ok(w.isVerb(w.getRandomWord("v")));
      ok(w.isAdverb(w.getRandomWord("r")));
      ok(w.isAdjective(w.getRandomWord("a")));
      // println(w.getRandomWord("v"));

      ok(w.getRandomWord("j") == null);
    }
    w.ignoreCompoundWords(false);
    w.ignoreUpperCaseWords(false);
  }

  @Test
  public void testGetRandomWordStringBooleanInt() {
    String result = w.getRandomWord("v", true, 10);
    ok(w.isStem(result, "v"));
    equal(true, w.isStem(result, "v"));

    String result2 = w.getRandomWord("n", true, 10);
    ok(w.isStem(result2, "n"));
    equal(true, w.isStem(result2, "n"));

    String result3 = w.getRandomWord("a", true, 10);
    ok(w.isStem(result3, "a"));
    equal(true, w.isStem(result3, "a"));

    String result4 = w.getRandomWord("r", true, 10);
    ok(w.isStem(result4, "r"));
    equal(true, w.isStem(result4, "r"));

    ok(w.getRandomWord("j", true, 10) == null);

  }

  @Test
  public void testGetDistance() {
    float expected = (float) 0.2;
    float result = w.getDistance("table", "chair", "n");
    equal(expected, result);

    float expected2 = (float) 0.3;
    float result2 = w.getDistance("wood", "chair", "n");
    // println(w.getDistance("wood", "chair", "n"));
    equal(expected2, result2);

    float expected3 = (float) 0.4;
    float result3 = w.getDistance("bird", "chair", "n");
    // println(w.getDistance("bird", "chair", "n"));
    equal(expected3, result3);

    float expected4 = (float) 0.7777778;
    float result4 = w.getDistance("health insurance", "chair", "n");
    // println(w.getDistance("health insurance", "chair", "n"));
    equal(expected4, result4);

    float expected5 = (float) 0.75;
    float result5 = w.getDistance("health insurance", "bird", "n");
    // println(w.getDistance("health insurance", "bird", "n"));
    equal(expected5, result5);

    float expected6 = (float) 0.9285714;
    float result6 = w.getDistance("health insurance", "human", "n");
    // println(w.getDistance("health insurance", "human", "n"));
    equal(expected6, result6);

    float expected7 = (float) 0.75;
    float result7 = w.getDistance("health insurance", "health", "n");
    // println(w.getDistance("health insurance", "health", "n"));
    equal(expected7, result7);

    float expected8 = (float) 1.0;
    float result8 = w.getDistance("notevenaword", "human", "n");
    // println(w.getDistance("notevenaword", "human", "n"));
    equal(expected8, result8);
  }

  @Test
  public void testGetMeronymsStringString() {
    String[] expected = { "row", "column" };
    // println(w.getMeronyms("table", "n");
    setEqualMulti(expected, "getMeronyms", "table", "n");

    String[] expected2 = {};
    // printArr(w.getMeronyms("row", "n"));
    setEqualMulti(expected2, "getMeronyms", "row", "n");

    String[] expected3 = {};
    // printArr(w.getMeronyms("table", "v"));
    setEqualMulti(expected3, "getMeronyms", "table", "v");

    String[] expected4 = { "duramen", "trunk", "crown", "burl", "bole",
	"tree stump", "tree branch", "limb", "treetop", "stump", "tree trunk",
	"heartwood", "sapwood" };
    // printArr(w.getMeronyms("tree", "n"));
    setEqualMulti(expected4, "getMeronyms", "tree", "n");

    String[] expected5 = {};
    // printArr(w.getMeronyms("apple", "n"));
    setEqualMulti(expected5, "getMeronyms", "apple", "n");

    setEqual(EMPTY, w.getMeronyms("table", "j"));

  }

  @Test
  public void testGetMeronymsInt() {

    String[] expected = { "row", "column" };
    String[] result = w.getMeronyms(98283156);
    // println(result);
    setEqual(expected, result);

    String[] expected2 = {};
    int[] i2 = w.getSenseIds("table", "v");
    // printArr(w.getMeronyms(i2[0]));
    setEqual(expected2, w.getMeronyms(i2[0]));

    String[] expected3 = { "limb", "crown", "bole", "heartwood", "stump",
	"treetop", "burl", "sapwood", "tree stump", "duramen", "tree branch",
	"tree trunk", "trunk" };
    int[] i3 = w.getSenseIds("tree", "n");
    // printArr(w.getMeronyms(i3[0]));
    setEqual(expected3, w.getMeronyms(i3[0]));

    String[] expected4 = {};
    int[] i4 = w.getSenseIds("apple", "n");
    // printArr(w.getMeronyms(i4[0]));
    setEqual(expected4, w.getMeronyms(i4[0]));

    String[] expected5 = { "digestive system", "systema alimentarium",
	"cavity", "crotch", "systema digestorium", "systema respiratorium",
	"lymphatic system", "circulatory system", "cavum", "neck",
	"systema nervosum", "respiratory system", "cardiovascular system",
	"cervix", "fork", "nervous system", "body", "systema lymphaticum",
	"leg", "endocrine system", "gastrointestinal system",
	"articulatory system", "caput", "vascular system", "head",
	"body substance", "trunk", "arm", "pressure point", "torso",
	"sensory system", "bodily cavity", "musculoskeletal system" };
    int[] i5 = w.getSenseIds("body", "n");
    // printArr(w.getMeronyms(i5[0]));
    setEqual(expected5, w.getMeronyms(i5[0]));

    setEqual(EMPTY, w.getMeronyms(88283156));

  }

  @Test
  public void testGetAllMeronyms() {

    String[] expected = { "row", "column", "leg", "tabletop", "tableware" };
    // println(w.getAllMeronyms("table", "n"),true);
    setEqualMulti(expected, "getAllMeronyms", "table", "n");

    String[] expected2 = {};
    // printArr(w.getAllMeronyms("table", "v"));
    setEqualMulti(expected2, "getAllMeronyms", "table", "v");

    String[] expected3 = { "duramen", "sapwood", "burl", "heartwood",
	"tree stump", "stump", "treetop", "tree branch", "trunk", "tree trunk",
	"limb", "crown", "bole" };
    // printArr(w.getAllMeronyms("tree", "n"));
    setEqualMulti(expected3, "getAllMeronyms", "tree", "n");

    String[] expected4 = {};
    // printArr(w.getAllMeronyms("apple", "n"));
    setEqualMulti(expected4, "getAllMeronyms", "apple", "n");

    String[] expected5 = { "systema lymphaticum", "systema nervosum", "cervix",
	"rear end", "dorsum", "leg", "nates", "bodily cavity",
	"body substance", "caput", "cavum", "serratus muscles",
	"gastrointestinal system", "buttock", "arm", "tooshie", "buttocks",
	"trunk", "respiratory system", "paunch", "ass", "sensory system",
	"crotch", "can", "seat", "arse", "cheek", "prat", "shoulder joint",
	"diaphragm", "butt", "head", "abdomen", "pressure point", "neck",
	"torso", "keister", "backside", "tush", "fundament",
	"circulatory system", "waist", "haunch", "derriere", "tail end",
	"cavity", "buns", "systema respiratorium", "digestive system", "bum",
	"shoulder", "cardiovascular system", "fanny", "musculoskeletal system",
	"stomach", "belly", "hip", "midsection", "side", "systema digestorium",
	"spare tire", "endocrine system", "vascular system", "midriff",
	"middle", "chest", "waistline", "nervous system",
	"articulatory system", "back", "systema alimentarium", "hind end",
	"thorax", "hindquarters", "loins", "rump", "love handle", "bottom",
	"articulatio humeri", "venter", "stern", "lymphatic system", "behind",
	"tail", "pectus", "rear", "posterior", "fork", "serratus" };
    // printArr(w.getAllMeronyms("body", "n"));
    setEqualMulti(expected5, "getAllMeronyms", "body", "n");

    setEqual(EMPTY, w.getAllMeronyms("table", "j"));
  }

  @Test
  public void testGetHolonymsStringString() {

    String[] expected = { "human", "body", "organic structure", "homo",
	"human being", "man", };
    // println(w.getHolonyms("arm", "n"),true);
    setEqualMulti(expected, "getHolonyms", "arm", "n");

    String[] expected2 = { "Malus", "genus Malus" };
    // printArr(w.getHolonyms("apple tree", "n"));
    setEqualMulti(expected2, "getHolonyms", "apple tree", "n");

    String[] expected3 = { "woods", "forest", "wood" };
    // printArr(w.getHolonyms("tree", "n"));
    setEqualMulti(expected3, "getHolonyms", "tree", "n");

    String[] expected4 = {};
    // printArr(w.getHolonyms("tree", "v"));
    setEqualMulti(expected4, "getHolonyms", "tree", "v");

    setEqual(EMPTY, w.getHolonyms("table", "j"));

  }

  @Test
  public void testGetHolonymsInt() {
    String[] expected = {};
    String[] result = w.getHolonyms(98283156);
    // printArr(result);
    setEqual(expected, result);

    String[] expected2 = {};
    int[] i2 = w.getSenseIds("table", "v");
    // printArr(w.getHolonyms(i2[0]));
    setEqual(expected2, w.getHolonyms(i2[0]));

    String[] expected3 = { "forest", "woods", "wood" };
    int[] i3 = w.getSenseIds("tree", "n");
    // printArr(w.getHolonyms(i3[0]));
    setEqual(expected3, w.getHolonyms(i3[0]));

    String[] expected4 = { "apple", "orchard apple tree", "Malus pumila" };
    int[] i4 = w.getSenseIds("apple", "n");
    // printArr(w.getHolonyms(i4[0]));
    setEqual(expected4, w.getHolonyms(i4[0]));

    String[] expected5 = {};
    int[] i5 = w.getSenseIds("body", "n");
    // printArr(w.getHolonyms(i5[0]));
    setEqual(expected5, w.getHolonyms(i5[0]));

    setEqual(EMPTY, w.getHolonyms(88283156));

    setEqual(w.getHolonyms(85571403), EMPTY);

  }

  @Test
  public void testGetAllHolonyms() {
    w.ignoreUpperCaseWords(false);
    w.ignoreCompoundWords(false);
    String[] expected = { "homo", "human", "weaponry", "body",
	"organic structure", "garment", "armchair", "man", "arms",
	"implements of war", "human being", "weapons system", "munition" };
    // printArr(w.getAllHolonyms("arm", "n"));
    setEqualMulti(expected, "getAllHolonyms", "arm", "n");

    String[] expected2 = { "Malus", "genus Malus" };
    // printArr(w.getAllHolonyms("apple tree", "n"));
    setEqualMulti(expected2, "getAllHolonyms", "apple tree", "n");

    String[] expected3 = { "woods", "forest", "wood" };
    // printArr(w.getAllHolonyms("tree", "n"));
    setEqualMulti(expected3, "getAllHolonyms", "tree", "n");

    String[] expected4 = {};
    // printArr(w.getAllHolonyms("tree", "v"));
    setEqualMulti(expected4, "getAllHolonyms", "tree", "v");

    setEqual(w.getAllHolonyms("arm", "j"), EMPTY);

  }

  @Test
  public void testGetCoordinatesStringString() {

    w.ignoreUpperCaseWords(false);
    w.ignoreCompoundWords(false);

    String[] expected = { "hindlimb", "cubitus", "hind limb", "forelimb",
	"crus", "flipper", "leg", "forearm", "thigh" };
    String[] result = w.getCoordinates("arm", "n");
    // println(w.getSenseIds("arm", "n"));
    // printArr(result);
    setEqualMulti(expected, "getCoordinates", "arm", "n");

    String[] expected2 = { "coco plum tree", "jackfruit", "Brazilian guava",
	"pomegranate tree", "Achras zapota", "Averrhoa carambola", "icaco",
	"Litchi chinensis", "lungen", "cocoa plum", "cherry tree", "pulassan",
	"Japanese medlar", "pawpaw", "pomegranate", "ginep",
	"Artocarpus heterophyllus", "mammee tree", "avocado tree",
	"Artocarpus altilis", "rose-apple tree", "genipa", "Psidium guineense",
	"loquat", "Eriobotrya japonica", "apricot tree", "coco plum",
	"mulberry tree", "jaboticaba", "mamey", "lichee", "Japanese plum",
	"longan", "longanberry", "Prunus persica", "pitanga", "loquat tree",
	"hog plum", "avocado", "mangosteen", "plum tree", "jambosa",
	"nectarine tree", "durian tree", "purple strawberry guava",
	"carambola tree", "Eugenia jambos", "rambutan", "Nephelium litchi",
	"mamoncillo", "marang tree", "guava bush", "jaboticaba tree", "mango",
	"Grias cauliflora", "mombin tree", "sapodilla tree", "cherry",
	"quince bush", "Eugenia corynantha", "mammee apple", "jocote",
	"Punica granatum", "true guava", "durion", "custard apple", "canistel",
	"persimmon tree", "marang", "mangosteen tree", "olive tree",
	"Myrciaria cauliflora", "akee tree", "Nephelium mutabile", "rambotan",
	"yellow mombin tree", "plumcot tree", "anchovy pear",
	"Garcinia mangostana", "pulasan tree", "melon tree",
	"Prunus persica nectarina", "Pouteria campechiana nervosa",
	"Spanish lime", "mustard tree", "Artocarpus odoratissima",
	"Pyrus communis", "yellow mombin", "guava", "Psidium littorale",
	"rambutan tree", "Psidium littorale longipes", "Spondias purpurea",
	"apricot", "Irvingia gabonensis", "Euphorbia litchi",
	"Psidium guajava", "litchi", "caimito", "peach tree", "breadfruit",
	"mombin", "plumcot", "sapodilla", "akee", "Melicocca bijuga",
	"Manilkara zapota", "mulberry", "Salvadora persica", "nectarine",
	"quince", "Mammea americana", "medlar", "durian", "Mangifera indica",
	"toothbrush tree", "papaya", "jackfruit tree", "citrus tree",
	"papaya tree", "Blighia sapida", "cattley guava", "strawberry guava",
	"pear", "Psidium cattleianum", "Chrysophyllum cainito", "mammee",
	"Spanish lime tree", "Melicocca bijugatus", "Artocarpus communis",
	"Nephelium lappaceum", "wild mango", "yellow cattley guava",
	"wild mango tree", "Cydonia oblonga", "star apple", "bilimbi",
	"Nephelium longana", "plum", "litchi tree", "medlar tree",
	"Surinam cherry", "breadfruit tree", "carambola", "sour cherry",
	"mango tree", "canistel tree", "Eugenia uniflora",
	"Mespilus germanica", "Persea Americana", "almond tree",
	"Spondias mombin", "Durio zibethinus", "anchovy pear tree",
	"rose apple", "Averrhoa bilimbi", "Dimocarpus longan", "papaia",
	"Chrysobalanus icaco", "peach", "honey berry", "genip", "pulasan",
	"pear tree", "custard apple tree", "citrus", "Carica papaya",
	"persimmon", "dika" };
    // printArr(w.getCoordinates("apple tree", "n"));
    setEqualMulti(expected2, "getCoordinates", "apple tree", "n");

    String[] expected3 = { "aralia", "traveller's tree", "Pipturus argenteus",
	"sweet clover", "bignoniad", "Queensland grass-cloth plant",
	"false indigo", "arborescent plant", "bugbane", "bush", "lespedeza",
	"milk-vetch", "lignosae", "gesneriad", "milk vetch", "nightshade",
	"traveler's tree", "ravenala", "melilot", "Ravenala madagascariensis",
	"shrub", "bramble", "combretum", "figwort", "melilotus", "lupin",
	"bush clover", "wild indigo", "lupine" };
    // printArr(w.getCoordinates("tree", "n"));
    setEqualMulti(expected3, "getCoordinates", "tree", "n");

    String[] expected4 = { "pull over", "navigate", "dock", "corner", "helm",
	"starboard", "sheer", "stand out", "conn", "park", "pilot", "crab",
	"channel" };
    // printArr(w.getCoordinates("tree", "v"));
    setEqualMulti(expected4, "getCoordinates", "tree", "v");

    setEqual(EMPTY, w.getCoordinates("arm", "j"));

  }

  @Test
  public void testGetCoordinatesInt() {
    String[] expected = { "matrix", "panoply", "bank", "tabular array",
	"column", "table", "row", "spectrum" };
    String[] result = w.getCoordinates(98283156);
    setEqual(expected, result);

    String[] expected2 = { "drag one's heels", "defer", "hesitate", "shelve",
	"shillyshally", "drag one's feet", "table", "dilly-dally", "postpone",
	"pause", "stall", "prorogue", "set back", "procrastinate", "hold over",
	"remit", "dillydally", "put over", "put off" };
    int[] i2 = w.getSenseIds("table", "v");
    // printArr(w.getCoordinates(i2[0]));
    setEqual(expected2, w.getCoordinates(i2[0]));

    String[] expected3 = { "bush clover", "nightshade", "aralia",
	"false indigo", "wild indigo", "milk vetch", "bugbane", "lignosae",
	"lupine", "traveler's tree", "shrub", "milk-vetch",
	"arborescent plant", "lupin", "bush", "Pipturus argenteus",
	"bignoniad", "tree", "combretum", "melilot", "traveller's tree",
	"Queensland grass-cloth plant", "figwort", "ravenala", "melilotus",
	"Ravenala madagascariensis", "lespedeza", "sweet clover", "gesneriad",
	"bramble" };
    int[] i3 = w.getSenseIds("tree", "n");
    // printArr(w.getCoordinates(i3[0]));
    setEqual(expected3, w.getCoordinates(i3[0]));

    String[] expected4 = { "elderberry", "plumcot", "cherry", "medlar",
	"custard apple", "monstera", "river pear", "quince", "kitambilla",
	"papaya", "clingstone", "nectarine", "akee", "genipap fruit", "lichi",
	"sorb apple", "jak", "pear", "pulasan", "Spanish lime", "star fruit",
	"rambotan", "carambola", "ugli fruit", "sorb", "quandong",
	"citrous fruit", "cling", "sapodilla plum", "alligator pear",
	"avocado", "prickly pear", "pomegranate", "wild plum", "date",
	"pitahaya", "garambulla", "carissa plum", "marmalade plum", "litchi",
	"Chinese jujube", "loquat", "windfall", "ketembilla", "lychee",
	"blade apple", "sapodilla", "genipap", "eggfruit", "sapote",
	"tamarind", "dried fruit", "passion fruit", "ananas", "papaw",
	"canistel", "fig", "Japanese plum", "sour gourd", "plum", "lanseh",
	"kai apple", "durian", "dragon's eye", "ugli", "sapota", "melon",
	"citrus", "pulassan", "grape", "marang", "Barbados gooseberry",
	"apple", "pineapple", "mamey", "ackee", "peach", "lansa", "apricot",
	"lanset", "litchee", "pawpaw", "banana", "native peach",
	"Chinese date", "pineapple guava", "mangosteen", "mammee",
	"natal plum", "icaco", "anchovy pear", "mango", "jujube", "leechee",
	"avocado pear", "coco plum", "kiwi fruit", "mammee apple",
	"kitembilla", "hog plum", "longanberry", "monkey bread", "quandang",
	"jaboticaba", "rambutan", "freestone", "mombin", "breadfruit",
	"jackfruit", "tamarindo", "cocoa plum", "citrus fruit", "kiwi",
	"litchi nut", "aguacate", "yellow mombin", "Chinese gooseberry",
	"rose apple", "feijoa", "jack", "guava", "quantong", "lansat", "genip",
	"lichee", "tangelo", "ceriman", "berry" };
    int[] i4 = w.getSenseIds("apple", "n");
    // printArr(w.getCoordinates(i4[0]));
    setEqual(expected4, w.getCoordinates(i4[0]));

    String[] expected5 = { "mechanism", "constellation", "nest",
	"natural covering", "creation", "cover", "plant structure", "cocoon",
	"dead body", "celestial body", "universe", "consolidation",
	"organic structure", "blackbody", "carpet", "heavenly body",
	"full radiator", "stone", "body", "tangle", "rock",
	"extraterrestrial object", "asterism", "estraterrestrial body",
	"existence", "radiator", "black body", "world", "macrocosm",
	"plant part", "cosmos", "covering", "sample" };
    int[] i5 = w.getSenseIds("body", "n");
    // printArr(w.getCoordinates(i5[0]));
    setEqual(expected5, w.getCoordinates(i5[0]));

    setEqual(EMPTY, w.getCoordinates(88283156));

  }

  @Test
  public void testGetAllCoordinates() {
    w.ignoreUpperCaseWords(false);
    w.ignoreCompoundWords(false);

    String[] expected = { "remainder", "constant", "base", "biquadrate",
	"pagination", "linage", "divisor", "natural number",
	"oxidation number", "quotient", "square", "prime", "no.", "quartic",
	"subtrahend", "fourth power", "floating-point number", "cardinal",
	"score", "baryon number", "integer", "minuend", "biquadratic",
	"decimal", "ordinal", "radix", "dividend", "root", "co-ordinate",
	"ordinal number", "Fibonacci number", "third power", "multiplier",
	"multiplicand", "factor", "complex quantity", "imaginary number",
	"prime quantity", "compound number", "paging", "oxidation state",
	"quota", "complex number", "atomic number", "lineage", "whole number",
	"cube", "record", "page number", "second power", "cardinal number",
	"count", "folio", "cardinality", "arity", "augend", "composite number",
	"imaginary", "addend", "difference", "multiplier factor",
	"fixed-point number" };
    setEqualMulti(expected, "getAllCoordinates", "coordinate", "n");

    String[] expected2 = { "sit", "dwell", "stay away", "keep one's eyes off",
	"attend", "pass", "come", "meet", "reach", "suffer", "poke out",
	"rest", "stand back", "inhabit", "find", "cover", "straddle", "see",
	"belong", "run", "keep one's hands off", "touch", "stretch", "fill",
	"reach out", "continue", "occupy", "lie", "center on", "extend",
	"know", "go", "lead", "sit around", "go to", "keep one's distance",
	"feel", "extend to", "follow", "enjoy", "endure", "experience", "face",
	"populate", "witness", "stretch along", };
    // printArr(w.getAllCoordinates("live", "v"),true);
    setEqualMulti(expected2, "getAllCoordinates", "live", "v");

    String[] expected3 = { "cosy", "tracer", "lap covering", "back", "plotter",
	"drip mould", "spike", "cauterant", "lobe", "measuring device",
	"patch", "skirt", "medical dressing", "sleeve", "section", "prong",
	"forearm", "boss", "extractor", "measuring system", "drip", "whip",
	"forelimb", "crus", "head", "medical instrument", "bedding", "flipper",
	"bedclothes", "department", "tea cozy", "limb", "fluke", "slipcover",
	"thigh", "branch", "surveying instrument", "cozy", "hind limb",
	"sonograph", "havelock", "seat", "engine", "knob", "weapon", "knee",
	"burr", "reed organ", "tea cosy", "rim", "analyser", "bosom", "tenon",
	"brim", "organ", "drip mold", "overhang", "hindlimb", "lap",
	"burial garment", "eyepatch", "cornice", "flange",
	"drafting instrument", "bed clothing", "instrument of execution",
	"cleat", "flue", "weapon system", "lug", "shoulder",
	"surveyor's instrument", "leg", "cubitus", "cautery", "antimacassar",
	"tooth", "blindfold", "optical instrument", "navigational instrument",
	"subdivision", "measuring instrument", "scientific instrument",
	"analyzer", "harmonium", "instrument of punishment", "elbow",
	"dressing" };
    // printArr(w.getAllCoordinates("arm", "n"));
    setEqualMulti(expected3, "getAllCoordinates", "arm", "n");

    String[] expected4 = { "coco plum tree", "jackfruit", "Brazilian guava",
	"pomegranate tree", "Achras zapota", "Averrhoa carambola", "icaco",
	"Litchi chinensis", "lungen", "cocoa plum", "cherry tree", "pulassan",
	"Japanese medlar", "pawpaw", "pomegranate", "ginep",
	"Artocarpus heterophyllus", "mammee tree", "avocado tree",
	"Artocarpus altilis", "rose-apple tree", "genipa", "Psidium guineense",
	"loquat", "Eriobotrya japonica", "apricot tree", "coco plum",
	"mulberry tree", "jaboticaba", "mamey", "lichee", "Japanese plum",
	"longan", "longanberry", "Prunus persica", "pitanga", "loquat tree",
	"hog plum", "avocado", "mangosteen", "plum tree", "jambosa",
	"nectarine tree", "durian tree", "purple strawberry guava",
	"carambola tree", "Eugenia jambos", "rambutan", "Nephelium litchi",
	"mamoncillo", "marang tree", "guava bush", "jaboticaba tree", "mango",
	"Grias cauliflora", "mombin tree", "sapodilla tree", "cherry",
	"quince bush", "Eugenia corynantha", "mammee apple", "jocote",
	"Punica granatum", "true guava", "durion", "custard apple", "canistel",
	"persimmon tree", "marang", "mangosteen tree", "olive tree",
	"Myrciaria cauliflora", "akee tree", "Nephelium mutabile", "rambotan",
	"yellow mombin tree", "plumcot tree", "anchovy pear",
	"Garcinia mangostana", "pulasan tree", "melon tree",
	"Prunus persica nectarina", "Pouteria campechiana nervosa",
	"Spanish lime", "mustard tree", "Artocarpus odoratissima",
	"Pyrus communis", "yellow mombin", "guava", "Psidium littorale",
	"rambutan tree", "Psidium littorale longipes", "Spondias purpurea",
	"apricot", "Irvingia gabonensis", "Euphorbia litchi",
	"Psidium guajava", "litchi", "caimito", "peach tree", "breadfruit",
	"mombin", "plumcot", "sapodilla", "akee", "Melicocca bijuga",
	"Manilkara zapota", "mulberry", "Salvadora persica", "nectarine",
	"quince", "Mammea americana", "medlar", "durian", "Mangifera indica",
	"toothbrush tree", "papaya", "jackfruit tree", "citrus tree",
	"papaya tree", "Blighia sapida", "cattley guava", "strawberry guava",
	"pear", "Psidium cattleianum", "Chrysophyllum cainito", "mammee",
	"Spanish lime tree", "Melicocca bijugatus", "Artocarpus communis",
	"Nephelium lappaceum", "wild mango", "yellow cattley guava",
	"wild mango tree", "Cydonia oblonga", "star apple", "bilimbi",
	"Nephelium longana", "plum", "litchi tree", "medlar tree",
	"Surinam cherry", "breadfruit tree", "carambola", "sour cherry",
	"mango tree", "canistel tree", "Eugenia uniflora",
	"Mespilus germanica", "Persea Americana", "almond tree",
	"Spondias mombin", "Durio zibethinus", "anchovy pear tree",
	"rose apple", "Averrhoa bilimbi", "Dimocarpus longan", "papaia",
	"Chrysobalanus icaco", "peach", "honey berry", "genip", "pulasan",
	"pear tree", "custard apple tree", "citrus", "Carica papaya",
	"persimmon", "dika" };
    // printArr(w.getAllCoordinates("apple tree", "n"));
    setEqualMulti(expected4, "getAllCoordinates", "apple tree", "n");

    String[] expected5 = { "figure of eight", "nightshade", "traveler's tree",
	"oblong", "bush clover", "bugbane", "conic section", "bramble",
	"melilot", "figwort", "melilotus", "semicircle", "lupin", "combretum",
	"arborescent plant", "Queensland grass-cloth plant", "ellipsoid",
	"sector", "lupine", "ravenala", "milk vetch", "shrub",
	"Pipturus argenteus", "paraboloid", "polygonal shape", "heart",
	"figure eight", "bignoniad", "star", "lespedeza", "sweet clover",
	"figure 8", "wild indigo", "tree diagram", "false indigo",
	"traveller's tree", "hemicycle", "lignosae", "conic", "gesneriad",
	"Ravenala madagascariensis", "polygon", "milk-vetch", "aralia", "bush" };
    // printArr(w.getAllCoordinates("tree", "n"));
    setEqualMulti(expected5, "getAllCoordinates", "tree", "n");

    String[] expected6 = { "checkrow", "quest", "bed", "channel", "afforest",
	"puddle", "pilot", "starboard", "root", "pull over", "hound",
	"replant", "navigate", "sheer", "dock", "run down", "helm", "shoetree",
	"forest", "conn", "crab", "stand out", "park", "dibble", "hunt",
	"corner", "trace" };
    // printArr(w.getAllCoordinates("tree", "v"));
    setEqualMulti(expected6, "getAllCoordinates", "tree", "v");

    setEqual(EMPTY, w.getAllCoordinates("live", "j"));

  }

  @Test
  public void testGetVerbGroupStringString() {
    w.ignoreUpperCaseWords(false);
    w.ignoreCompoundWords(false);

    String[] expected = { "populate", "dwell", "inhabit" };
    String[] result = w.getVerbGroup("live", "v");
    setEqualMulti(expected, "getVerbGroup", "live", "v");

    String[] expected2 = { "give-up the ghost", "drop dead", "choke",
	"give out", "pop off", "break", "pass", "pass away", "go bad",
	"expire", "buy the farm", "exit", "break down", "go",
	"cash in one's chips", "conk out", "give way", "fail",
	"kick the bucket", "croak", "decease", "snuff it", "conk", "perish" };
    // printArr(w.getVerbGroup("die", "v"));
    setEqualMulti(expected2, "getVerbGroup", "die", "v");

    String[] expected3 = { "go", "last", "hold out", "be", "live", "subsist",
	"live on", "hold up", "exist", "endure" };
    // printArr(w.getVerbGroup("survive", "v"));
    setEqualMulti(expected3, "getVerbGroup", "survive", "v");

    setEqual(EMPTY, w.getVerbGroup("live", "j"));
  }

  @Test
  public void testGetVerbGroupInt() {
    String[] expected = { "populate", "dwell", "live", "inhabit" };
    String[] result = w.getVerbGroup(82655932);
    // println(w.getSenseIds("live", "v"));
    setEqual(expected, result);

    String[] expected2 = { "give way", "decease", "pop off", "pass", "die",
	"pass away", "give-up the ghost", "exit", "go bad", "buy the farm",
	"drop dead", "conk", "choke", "break down", "give out",
	"cash in one's chips", "break", "go", "perish", "conk out", "fail",
	"croak", "kick the bucket", "snuff it", "expire" };
    int[] i2 = w.getSenseIds("die", "v");
    // printArr(w.getVerbGroup(i2[0]));
    setEqual(expected2, w.getVerbGroup(i2[0]));

    String[] expected3 = { "dice", "die" };
    int[] i3 = w.getSenseIds("die", "n");
    // printArr(w.getVerbGroup(i3[0]));
    setEqual(expected3, w.getVerbGroup(i3[0]));

    String[] expected4 = { "live", "go", "subsist", "survive", "hold up",
	"exist", "hold out", "live on", "last", "endure", "be" };
    int[] i4 = w.getSenseIds("survive", "v");
    // printArr(w.getVerbGroup(i4[0]));
    setEqual(expected4, w.getVerbGroup(i4[0]));

    setEqual(EMPTY, w.getVerbGroup(72655932));

  }

  @Test
  public void testAllGetVerbGroupStringString() {
    String[] expected = { "be", "go", "hold out", "inhabit", "know", "exist",
	"subsist", "last", "survive", "hold up", "experience", "endure",
	"dwell", "live on", "populate", };
    // w.ignoreCompoundWords(false);println(w.getAllVerbGroups("live",
    // "v"),true);
    setEqualMulti(expected, "getAllVerbGroups", "live", "v");

    String[] expected2 = { "become flat", "break", "break down",
	"buy the farm", "cash in one's chips", "choke", "conk", "conk out",
	"croak", "decease", "die out", "drop dead", "exit", "expire", "fail",
	"give out", "give way", "give-up the ghost", "go", "go bad",
	"kick the bucket", "pall", "pass", "pass away", "perish", "pop off",
	"snuff it", };
    // printArr(w.getAllVerbGroups("die", "v"));
    setEqualMulti(expected2, "getAllVerbGroups", "die", "v");

    String[] expected3 = { "dice" };
    // printArr(w.getAllVerbGroups("die", "n"));
    setEqualMulti(expected3, "getAllVerbGroups", "die", "n");

    String[] expected4 = { "come through", "subsist", "live on", "make it",
	"outlive", "pull through", "hold out", "pull round", "live", "last",
	"be", "hold up", "exist", "go", "endure", "outlast" };
    // printArr(w.getAllVerbGroups("survive", "v"));
    setEqualMulti(expected4, "getAllVerbGroups", "survive", "v");

    String[] expected5 = {};
    // printArr(w.getAllVerbGroups("survive", "r"));
    setEqualMulti(expected5, "getAllVerbGroups", "survive", "r");

    setEqual(EMPTY, w.getAllVerbGroups("happily", "j"));

  }

  @Test
  public void testGetDerivedTermsStringString() {
    String[] expected = { "jubilant", "blithe", "gay", "mirthful", "merry",
	"happy" };
    String[] result = w.getDerivedTerms("happily", "r");
    setEqualMulti(expected, "getDerivedTerms", "happily", "r");

    String[] expected2 = {};
    // printArr(w.getDerivedTerms("die", "v"));
    setEqualMulti(expected2, "getDerivedTerms", "die", "v");

    String[] expected3 = {};
    // printArr(w.getDerivedTerms("die", "n"));
    setEqualMulti(expected3, "getDerivedTerms", "die", "n");

    String[] expected4 = { "dead", "lifeless" };
    // printArr(w.getDerivedTerms("deadly", "r"));
    setEqualMulti(expected4, "getDerivedTerms", "deadly", "r");

    String[] expected5 = {};
    // printArr(w.getDerivedTerms("survive", "r"));
    setEqualMulti(expected5, "getDerivedTerms", "survive", "r");

    setEqual(EMPTY, w.getDerivedTerms("happily", "j"));

  }

  @Test
  public void testGetDerivedTermsInt() {
    String[] expected = { "jubilant", "blithe", "gay", "mirthful", "merry",
	"happy" };
    String[] result = w.getDerivedTerms(650835);
    // println(w.getSenseIds("happily", "r"));
    // println(result);
    setEqual(expected, result);

    String[] expected2 = { "dead", "lifeless" };
    int[] i2 = w.getSenseIds("deadly", "r");
    // printArr(w.getDerivedTerms(i2[0]));
    setEqual(expected2, w.getDerivedTerms(i2[0]));

    String[] expected3 = { "sudden", "abrupt" };
    int[] i3 = w.getSenseIds("dead", "r");
    // printArr(w.getDerivedTerms(i3[0]));
    setEqual(expected3, w.getDerivedTerms(i3[0]));

    String[] expected4 = {};
    int[] i4 = w.getSenseIds("dead", "n");
    // printArr(w.getDerivedTerms(i4[0]));
    setEqual(expected4, w.getDerivedTerms(i4[0]));

    String[] expected5 = { "unexpected" };
    ;
    int[] i5 = w.getSenseIds("unexpectedly", "r");
    // printArr(w.getDerivedTerms(i5[0]));
    setEqual(expected5, w.getDerivedTerms(i5[0]));

    setEqual(EMPTY, w.getDerivedTerms(550835));

  }

  @Test
  public void testGetAllDerivedTerms() {
    String[] expected = { "jubilant", "blithe", "gay", "mirthful", "merry",
	"happy" };
    String[] result = w.getAllDerivedTerms("happily", "r");
    // println(w.getSenseIds("arm", "n"));
    // println(result);
    setEqualMulti(expected, "getAllDerivedTerms", "happily", "r");

    String[] expected2 = {};
    // printArr(w.getAllDerivedTerms("die", "v"));
    setEqualMulti(expected2, "getAllDerivedTerms", "die", "v");

    String[] expected3 = {};
    // printArr(w.getAllDerivedTerms("die", "n"));
    setEqualMulti(expected3, "getAllDerivedTerms", "die", "n");

    String[] expected4 = { "dead", "lifeless" };
    // printArr(w.getAllDerivedTerms("deadly", "r"));
    setEqualMulti(expected4, "getAllDerivedTerms", "deadly", "r");

    String[] expected5 = {};
    // printArr(w.getAllDerivedTerms("survive", "r"));
    setEqualMulti(expected5, "getAllDerivedTerms", "survive", "r");

    setEqual(EMPTY, w.getAllDerivedTerms("happily", "j"));

  }

  @Test
  public void testGetAlsoSeesStringString() {
    String[] expected = { "cheerful", "contented", "content", "glad", "elated",
	"euphoric", "felicitous", "joyful", "joyous" };
    String[] result = w.getAlsoSees("happy", "a");
    // println(w.getSenseIds("arm", "n"));
    // println(result);
    setEqualMulti(expected, "getAlsoSees", "happy", "a");

    String[] expected2 = { "die down", "die off", "die out" };
    // printArr(w.getAlsoSees("die", "v"));
    setEqualMulti(expected2, "getAlsoSees", "die", "v");

    String[] expected3 = {};
    // printArr(w.getAlsoSees("die", "n"));
    setEqualMulti(expected3, "getAlsoSees", "die", "n");

    String[] expected4 = {};
    // printArr(w.getAlsoSees("deadly", "r"));
    setEqualMulti(expected4, "getAlsoSees", "deadly", "r");

    String[] expected5 = { "cheerful" };
    // printArr(w.getAlsoSees("glad", "a"));
    setEqualMulti(expected5, "getAlsoSees", "glad", "a");

    setEqual(EMPTY, w.getAlsoSees("happily", "j"));
  }

  /*
   * @Test public void testGetSynsetBySenseNumber() {
   * 
   * String[] s = w.getAllSynsets("dog", "n");
   * System.out.println(Arrays.asList(s)); int[] is = w.getSenseIds("dog", "n");
   * for (int i = 0; i < is.length; i++) {
   * System.out.println(i+"] "+w.getSynset(is[i])); }
   * System.out.println(Arrays.asList(w.getSynsetBySenseNumber("dog", "n", 1)));
   * }
   */

  @Test
  public void testGetAlsoSeesInt() {
    String[] expected = { "cheerful", "contented", "content", "glad", "elated",
	"euphoric", "felicitous", "joyful", "joyous" };
    int[] i = w.getSenseIds("happy", "a");
    setEqual(expected, w.getAlsoSees(i[0]));

    String[] expected2 = { "cheerful" };
    int[] i2 = w.getSenseIds("glad", "a");
    // printArr(w.getAlsoSees(i2[0]));
    setEqual(expected2, w.getAlsoSees(i2[0]));

    String[] expected3 = { "happy", "glad" };
    int[] i3 = w.getSenseIds("cheerful", "a");
    // printArr(w.getAlsoSees(i3[0]));
    setEqual(expected3, w.getAlsoSees(i3[0]));

    setEqual(EMPTY, w.getAlsoSees(61151786));
  }

  @Test
  public void testGetAllAlsoSees() {
    String[] expected = { "cheerful", "contented", "content", "glad", "elated",
	"euphoric", "felicitous", "joyful", "joyous" };
    String[] result = w.getAllAlsoSees("happy", "a");
    // println(w.getSenseIds("happy", "a"));
    // println(result);
    setEqualMulti(expected, "getAllAlsoSees", "happy", "a");

    String[] expected2 = { "die down", "die off", "die out" };
    // printArr(w.getAllAlsoSees("die", "v"));
    setEqualMulti(expected2, "getAllAlsoSees", "die", "v");

    String[] expected3 = {};
    // printArr(w.getAllAlsoSees("die", "n"));
    setEqualMulti(expected3, "getAllAlsoSees", "die", "n");

    String[] expected4 = {};
    // printArr(w.getAllAlsoSees("deadly", "r"));
    setEqualMulti(expected4, "getAllAlsoSees", "deadly", "r");

    String[] expected5 = { "cheerful" };
    // printArr(w.getAllAlsoSees("glad", "a"));
    setEqualMulti(expected5, "getAllAlsoSees", "glad", "a");

    String[] expected6 = { "happy", "glad" };
    // printArr(w.getAllAlsoSees("cheerful", "a"));
    setEqualMulti(expected6, "getAllAlsoSees", "cheerful", "a");

    setEqual(EMPTY, w.getAllAlsoSees("happy", "j"));

  }

  @Test
  public void testGetNominalizationsStringString() {

    String[] expected = { "happiness" };
    String[] result = w.getNominalizations("happy", "a");
    // println(w.getSenseIds("happy", "a"));
    // println(result);
    setEqualMulti(expected, "getNominalizations", "happy", "a");

    String[] expected2 = {};
    // printArr(w.getNominalizations("happy", "n"));
    setEqualMulti(expected2, "getNominalizations", "happy", "n");

    String[] expected3 = {};
    // printArr(result = w.getNominalizations("happy", "v"));
    setEqualMulti(expected3, "getNominalizations", "happy", "v");

    String[] expected4 = {};
    // printArr(w.getNominalizations("happy", "r"));
    setEqualMulti(expected4, "getNominalizations", "happy", "r");

    String[] expected5 = {};
    // printArr(w.getNominalizations("door", "n"));
    setEqualMulti(expected5, "getNominalizations", "door", "n");

    String[] expected6 = { "necessitous", "necessitate", "essential",
	"require", "requisite" };
    // printArr(w.getNominalizations("necessary", "n"));
    setEqualMulti(expected6, "getNominalizations", "necessary", "n");

    setEqual(EMPTY, w.getNominalizations("happily", "j"));

  }

  @Test
  public void testGetNominalizationsInt() {

    String[] expected = { "happiness" };
    String[] result = w.getNominalizations(71151786);
    // println(w.getSenseIds("happy", "a"));
    // println(result);
    setEqual(expected, result);

    String[] expected3 = { "coolness" };
    String[] result3 = w.getNominalizations(72540350);
    // println(w.getSenseIds("cool", "a"));
    // printArr(w.getNominalizations(72540350));
    setEqual(expected3, result3);

    String[] expected4 = { "chill", "cool", "coolant", "cooler", "chilling",
	"cooling" };
    String[] result4 = w.getNominalizations(8371065);
    // println(w.getSenseIds("cool", "v"));
    // printArr(w.getNominalizations(8371065));
    setEqual(expected4, result4);

    String[] expected5 = { "cool" };
    String[] result5 = w.getNominalizations(95023185);
    // println(w.getSenseIds("cool", "n"));
    // printArr(w.getNominalizations(95023185));
    setEqual(expected5, result5);

    String[] expected6 = {};
    String[] result6 = w.getNominalizations(72540775);
    // //println(w.getSenseIds("air-cooled", "a"));
    // printArr(w.getNominalizations(72540775));
    setEqual(expected6, result6);

    setEqual(EMPTY, w.getNominalizations(51151786));
    setEqual(EMPTY, w.getNominalizations(21151786));
    setEqual(EMPTY, w.getNominalizations(61151786));

    w.ignoreCompoundWords(true);
    w.ignoreUpperCaseWords(true);

    expected = new String[] { "happiness" };
    result = w.getNominalizations(71151786);
    // println(w.getSenseIds("happy", "a"));
    // println(result);
    setEqual(expected, result);

    expected3 = new String[] { "coolness" };
    result3 = w.getNominalizations(72540350);
    // println(w.getSenseIds("cool", "a"));
    // printArr(w.getNominalizations(72540350));
    setEqual(expected3, result3);

    expected4 = new String[] { "chill", "cool", "coolant", "cooler",
	"chilling", "cooling" };
    result4 = w.getNominalizations(8371065);
    // println(w.getSenseIds("cool", "v"));
    // printArr(w.getNominalizations(8371065));
    setEqual(expected4, result4);

    expected5 = new String[] { "cool" };
    result5 = w.getNominalizations(95023185);
    // print(w.getSenseIds("cool", "n"));
    // printArr(w.getNominalizations(95023185));
    setEqual(expected5, result5);

    expected6 = new String[] {};
    result6 = w.getNominalizations(72540775);
    // println(w.getSenseIds("air-cooled", "a"));
    // printArr(w.getNominalizations(72540775));
    setEqual(expected6, result6);

    setEqual(EMPTY, w.getNominalizations(51151786));
    setEqual(EMPTY, w.getNominalizations(21151786));
    setEqual(EMPTY, w.getNominalizations(61151786));

  }

  @Test
  public void testGetAllNominalizations() {
    String[] expected = { "happiness", "felicitousness", "felicity" };
    String[] result = w.getAllNominalizations("happy", "a");
    // println(w.getSenseIds("happy", "a"));
    // println(result);
    setEqualMulti(expected, "getAllNominalizations", "happy", "a");

    String[] expected2 = {};
    // printArr(w.getAllNominalizations("happy", "n"));
    setEqualMulti(expected2, "getAllNominalizations", "happy", "n");

    String[] expected3 = {};
    // printArr(result = w.getAllNominalizations("happy", "v"));
    setEqualMulti(expected3, "getAllNominalizations", "happy", "v");

    String[] expected4 = {};
    // printArr(w.getAllNominalizations("happy", "r"));
    setEqualMulti(expected4, "getAllNominalizations", "happy", "r");

    String[] expected5 = {};
    // printArr(w.getAllNominalizations("door", "n"));
    setEqualMulti(expected5, "getAllNominalizations", "door", "n");

    String[] expected6 = { "necessitous", "necessitate", "essential",
	"require", "requisite" };
    // printArr(w.getAllNominalizations("necessary", "n"));
    setEqualMulti(expected6, "getAllNominalizations", "necessary", "n");

    setEqual(EMPTY, w.getAllNominalizations("happily", "j"));

  }

  @Test
  public void testGetSimilarStringString() {
    String[] expected = { "golden", "blissful", "blessed", "prosperous",
	"bright", "riant", "halcyon", "laughing" };
    // printArr(w.getSimilar("happy","a"));
    setEqualMulti(expected, "getSimilar", "happy", "a");

    String[] expected2 = {};
    // printArr(w.getSimilar("happy","n"));
    setEqualMulti(expected2, "getSimilar", "happy", "n");

    String[] expected3 = { "air-cooled", "precooled", "caller", "water-cooled",
	"air-conditioned" };
    // printArr(w.getSimilar("cool","a"));
    setEqualMulti(expected3, "getSimilar", "cool", "a");

    String[] expected4 = {};
    // printArr(w.getSimilar("cool","v"));
    setEqualMulti(expected4, "getSimilar", "cool", "v");

    String[] expected5 = {};
    // printArr(w.getSimilar("cool","n"));
    setEqualMulti(expected5, "getSimilar", "cool", "n");

    String[] expected6 = {};
    // printArr(w.getSimilar("cool","r"));
    setEqualMulti(expected6, "getSimilar", "cool", "r");

    String[] expected7 = { "cool" };
    // printArr(w.getSimilar("air-cooled","a"));
    setEqualMulti(expected7, "getSimilar", "air-cooled", "a");

    setEqual(new String[] {}, w.getSimilar("nosuchword", "a"));

    setEqual(EMPTY, w.getSimilar("happily", "j"));
    setEqual(EMPTY, w.getSimilar("nosuchword", "j"));

  }

  @Test
  public void testGetSimilarInt() {

    String[] expected = { "blessed", "blissful", "bright", "golden", "halcyon",
	"prosperous", "laughing", "riant" };
    String[] result = w.getSimilar(71151786);
    // println(w.getSenseIds("happy", "a"));
    // println(result);
    setEqual(expected, result);

    String[] expected3 = { "precooled", "air-cooled", "water-cooled",
	"air-conditioned", "caller" };
    String[] result3 = w.getSimilar(72540350);
    // println(w.getSenseIds("cool", "a"));
    // printArr(w.getSimilar(72540350));
    setEqual(expected3, result3);

    String[] expected4 = {};
    String[] result4 = w.getSimilar(8371065);
    // println(w.getSenseIds("cool", "v"));
    // printArr(w.getSimilar(8371065));
    setEqual(expected4, result4);

    String[] expected5 = {};
    String[] result5 = w.getSimilar(95023185);
    // println(w.getSenseIds("cool", "n"));
    // printArr(w.getSimilar(95023185));
    setEqual(expected5, result5);

    String[] expected6 = { "cool" };
    String[] result6 = w.getSimilar(72540775);
    // println(w.getSenseIds("air-cooled", "a"));
    // printArr(w.getSimilar(72540775));
    setEqual(expected6, result6);

    setEqual(EMPTY, w.getSimilar(51151786));
    setEqual(EMPTY, w.getSimilar(21151786));

    w.ignoreCompoundWords(true);
    w.ignoreUpperCaseWords(true);

    expected = new String[] { "blessed", "blissful", "bright", "golden",
	"halcyon", "prosperous", "laughing", "riant" };
    result = w.getSimilar(71151786);
    // println(w.getSenseIds("happy", "a"));
    // println(result);
    setEqual(expected, result);

    expected3 = new String[] { "precooled", "air-cooled", "water-cooled",
	"air-conditioned", "caller" };
    result3 = w.getSimilar(72540350);
    // println(w.getSenseIds("cool", "a"));
    // printArr(w.getSimilar(72540350));
    setEqual(expected3, result3);

    expected4 = new String[] {};
    result4 = w.getSimilar(8371065);
    // println(w.getSenseIds("cool", "v"));
    // printArr(w.getSimilar(8371065));
    setEqual(expected4, result4);

    expected5 = new String[] {};
    result5 = w.getSimilar(95023185);
    // println(w.getSenseIds("cool", "n"));
    // printArr(w.getSimilar(95023185));
    setEqual(expected5, result5);

    expected6 = new String[] { "cool" };
    result6 = w.getSimilar(72540775);
    // println(w.getSenseIds("air-cooled", "a"));
    // printArr(w.getSimilar(72540775));
    setEqual(expected6, result6);

    setEqual(EMPTY, w.getSimilar(51151786));
    setEqual(EMPTY, w.getSimilar(21151786));

  }

  @Test
  public void testGetAllSimilar() {
    String[] expected = { "blessed", "blissful", "bright", "golden", "halcyon",
	"prosperous", "laughing", "riant", "fortunate", "willing", "felicitous" };
    // println(result);
    setEqualMulti(expected, "getAllSimilar", "happy", "a");

    String[] expected2 = {};
    // printArr(w.getAllSimilar("happy","n"));
    setEqualMulti(expected2, "getAllSimilar", "happy", "n");

    String[] expected3 = { "composed", "fashionable", "caller", "precooled",
	"air-cooled", "stylish", "satisfactory", "cold", "air-conditioned",
	"water-cooled", "unresponsive", "unqualified" };
    // printArr(w.getAllSimilar("cool","a"));
    setEqualMulti(expected3, "getAllSimilar", "cool", "a");

    String[] expected4 = {};
    // printArr(w.getAllSimilar("cool","v"));
    setEqualMulti(expected4, "getAllSimilar", "cool", "v");

    String[] expected5 = {};
    // printArr(w.getAllSimilar("cool","n"));
    setEqualMulti(expected5, "getAllSimilar", "cool", "n");

    String[] expected6 = {};
    // printArr(w.getAllSimilar("cool","r"));
    setEqualMulti(expected6, "getAllSimilar", "cool", "r");

    String[] expected7 = { "cool" };
    // printArr(w.getAllSimilar("air-cooled","a"));
    setEqualMulti(expected7, "getAllSimilar", "air-cooled", "a");

    setEqual(new String[] {}, w.getAllSimilar("nosuchword", "a"));

    setEqual(EMPTY, w.getAllSimilar("happily", "j"));
    setEqual(EMPTY, w.getAllSimilar("nosuchword", "j"));

  }

  @Test
  public void testGetAllVerbGroupsStringString() {

    String[] expected = new String[] { "crippled", "gamey", "mettlesome",
	"gritty", "halt", "spunky", "spirited", "gimpy", "halting", "gamy",
	"lame" };
    String[] result = w.getAllVerbGroups("game", "a");
    setEqual(expected, result);

    expected = new String[] { "hot", "lively", "alive", "unrecorded",
	"springy", "bouncy", "resilient" };
    result = w.getAllVerbGroups("live", "a");
    setEqual(expected, result);

    expected = new String[] { "go", "last", "experience", "exist", "live on",
	"inhabit", "endure", "be", "populate", "subsist", "survive", "dwell",
	"know", "hold up", "hold out" };
    result = w.getAllVerbGroups("live", "v");
    setEqual(expected, result);

    expected = new String[] {};
    result = w.getAllVerbGroups("happy", "v");
    setEqual(expected, result);

    expected = new String[] { "felicitous", "glad", "well-chosen" };
    result = w.getAllVerbGroups("happy", "a");
    setEqual(expected, result);
  }

  @Test
  public void testPosToWordNetString() {
    // rp particle as adverb
    String[] expectNoun = new String[] { "nn", "nns", "nnp", "nnps" };
    String[] expectAdjective = new String[] { "jj", "jjr", "jjs" };
    String[] expectVerb = new String[] { "vb", "vbd", "vbg", "vbn", "vbp",
	"vbz" };
    String[] expectAdverb = new String[] { "rb", "rbr", "rbs", "wrb" };
    String[] expectOther = new String[] { "cc", "cd", "dt", "ex", "fw", "in",
	"ls", "md", "pdt", "pos", "prp", "prp$", "rp", "sym", "to", "uh",
	"wdt", "wp", "wp$" };

    for (int i = 0; i < expectNoun.length; i++)
      equal(w.posToWordNet(expectNoun[i]), "n");

    for (int i = 0; i < expectAdjective.length; i++)
      equal(w.posToWordNet(expectAdjective[i]), "a");

    for (int i = 0; i < expectVerb.length; i++)
      equal(w.posToWordNet(expectVerb[i]), "v");

    for (int i = 0; i < expectAdverb.length; i++)
      equal(w.posToWordNet(expectAdverb[i]), "r");

    for (int i = 0; i < expectOther.length; i++)
      equal(w.posToWordNet(expectOther[i]), "-");
  }

  @Test
  public void testIgnoreCompoundWords() {
    w.ignoreCompoundWords(true);
    ok(w.ignoreCompoundWords());

    w.ignoreCompoundWords(false);
    ok(!w.ignoreCompoundWords());

    w.ignoreUpperCaseWords(true);
    w.ignoreCompoundWords(true);
    ok(w.ignoreUpperCaseWords());

    w.ignoreCompoundWords(true);

    String[] expected = new String[] { "sportswear" };
    String[] result = w.getSynset("activewear", "n");
    // printArr(result);
    // setEqual(expected, result);

    w.ignoreCompoundWords(false);

    expected = new String[] { "sportswear", "athletic wear" };
    result = w.getSynset("activewear", "n");
    // printArr(result);
    setEqual(expected, result);
  }

  @Test
  public void testIgnoreUpperCaseWordsBoolean() {
    w.ignoreUpperCaseWords(false);
    w.ignoreCompoundWords(true);

    String[] expected = { "Dulles" };
    String[] result = w.getSynset("dulles", "n");
    // System.out.println("testIgnoreUpperCaseWordsBoolean: ");
    setEqual(expected, result);

    expected = new String[] { "Fungi" };
    result = w.getSynset("fungi", "n");
    setEqual(expected, result);

    expected = new String[] { "Fungi", "Prokayotae", "Monera", "Protoctista",
	"Animalia", "Plantae" };
    result = w.getAllSynonyms("fungi", "n");
    setEqual(expected, result);

    w.ignoreUpperCaseWords(true);
    w.ignoreCompoundWords(true);

    expected = new String[] {};
    result = w.getSynset("dulles", "n");
    // System.out.println("testIgnoreUpperCaseWordsBoolean: ");
    setEqual(expected, result);

    expected = new String[] {};
    result = w.getSynset("fungi", "n");
    setEqual(expected, result);

    expected = new String[] {};
    result = w.getAllSynonyms("fungi", "n");
    setEqual(expected, result);
  }

  @Test
  public void testIgnoreUpperCaseWords() {
    w.ignoreUpperCaseWords(true);
    w.ignoreCompoundWords(true);
    ok(w.ignoreUpperCaseWords());

    String[] expected = new String[] {};
    String[] result = w.getSynset("fungi", "n");
    setEqual(expected, result);

    w.ignoreUpperCaseWords(false);
    ok(!w.ignoreUpperCaseWords());

    expected = new String[] { "Fungi" };
    result = w.getSynset("fungi", "n");
    setEqual(expected, result);
  }

  @Test
  public void testIsCompound() // DCH: ok now...
  {

    String[] input3 = { "back", "space", "back space", "back_space", "",
	"#$%^&", "air-cooled" };
    boolean[] expected3 = { false, false, true, true, false, false, false };
    for (int i = 0; i < input3.length; i++) {
      // println(w.isCompound(input3[i]));
      deepEqual(w.isCompound(input3[i]), expected3[i]);
    }
  }

  // /////////////////////////// Dynamics ///////////////////////////////

  void setContainsMulti(String[] expected, String methodNm, int id, int count) {
    setContainsMulti(expected, methodNm, new Class[] { int.class, int.class },
	new Object[] { id, count });
  }

  void setContainsMulti(String[] expected, String methodNm, String word,
      String pos, int count) {
    setContainsMulti(expected, methodNm, new Class[] { String.class,
	String.class, int.class }, new Object[] { word, pos, count });
  }

  void setContainsMulti(String[] expected, String methodNm, Class[] argTypes,
      Object[] args) {
    boolean ignoreCompoundsOrig = w.ignoreCompoundWords();
    boolean ignoreUppersOrig = w.ignoreUpperCaseWords();
    String[] result;
    Method m = RiTa._findMethod(w, methodNm, argTypes);
    try {
      w.ignoreCompoundWords(false);
      w.ignoreUpperCaseWords(false);
      result = (String[]) m.invoke(w, args);
      if (args[0] instanceof String) // make sure we don't have the search term
	ok(!Arrays.asList(result).contains(args[0]));
      setContains(expected, result);

      w.ignoreCompoundWords(true);
      w.ignoreUpperCaseWords(false);
      result = (String[]) m.invoke(w, args);
      if (args[0] instanceof String) // make sure we don't have the search term
	ok(!Arrays.asList(result).contains(args[0]));
      setContains(removeCompoundWords(expected), result);

      w.ignoreCompoundWords(false);
      w.ignoreUpperCaseWords(true);
      result = (String[]) m.invoke(w, args);
      if (args[0] instanceof String) // make sure we don't have the search term
	ok(!Arrays.asList(result).contains(args[0]));
      setContains(removeUpperCaseWords(expected), result);

      w.ignoreCompoundWords(true);
      w.ignoreUpperCaseWords(true);
      result = (String[]) m.invoke(w, args);
      if (args[0] instanceof String) // make sure we don't have the search term
	ok(!Arrays.asList(result).contains(args[0]));
      setContains(removeCompoundWords(removeUpperCaseWords(expected)), result);

    } catch (Throwable e) {
      throw new RuntimeException(e);
    }
    w.ignoreCompoundWords(ignoreCompoundsOrig);
    w.ignoreUpperCaseWords(ignoreUppersOrig);
  }

  void setEqualMulti(String[] expected, String methodNm, int id) {
    setEqualMulti(expected, methodNm, new Class[] { int.class },
	new Object[] { id });
  }

  void setEqualMulti(String[] expected, String methodNm, String word, String pos) {
    setEqualMulti(expected, methodNm,
	new Class[] { String.class, String.class }, new Object[] { word, pos });
  }

  void setEqualMulti(String[] expected, String methodNm, Class[] argTypes,
      Object[] args) {
    boolean ignoreCompoundsOrig = w.ignoreCompoundWords();
    boolean ignoreUppersOrig = w.ignoreUpperCaseWords();
    String[] result;
    Method m = RiTa._findMethod(w, methodNm, argTypes);
    try {
      w.ignoreCompoundWords(false);
      w.ignoreUpperCaseWords(false);
      result = (String[]) m.invoke(w, args);
      if (args[0] instanceof String) // make sure we don't have the search term
	ok(!Arrays.asList(result).contains(args[0]));
      setEqual(expected, result);

      w.ignoreCompoundWords(true);
      w.ignoreUpperCaseWords(false);
      result = (String[]) m.invoke(w, args);
      if (args[0] instanceof String) // make sure we don't have the search term
	ok(!Arrays.asList(result).contains(args[0]));
      setEqual(removeCompoundWords(expected), result);

      w.ignoreCompoundWords(false);
      w.ignoreUpperCaseWords(true);
      result = (String[]) m.invoke(w, args);
      if (args[0] instanceof String) // make sure we don't have the search term
	ok(!Arrays.asList(result).contains(args[0]));
      setEqual(removeUpperCaseWords(expected), result);

      w.ignoreCompoundWords(true);
      w.ignoreUpperCaseWords(true);
      result = (String[]) m.invoke(w, args);
      if (args[0] instanceof String) // make sure we don't have the search term
	ok(!Arrays.asList(result).contains(args[0]));
      setEqual(removeCompoundWords(removeUpperCaseWords(expected)), result);
    } catch (Throwable e) {
      throw new RuntimeException(e);
    }
    w.ignoreCompoundWords(ignoreCompoundsOrig);
    w.ignoreUpperCaseWords(ignoreUppersOrig);
  }

  // ////////////////////////////// Helpers ///////////////////////////////////

  private static String[] removeUpperCaseWords(String[] s) {
    ArrayList<String> al = new ArrayList<String>();
    for (int i = 0; i < s.length; i++) {
      if (!WordnetUtil.startsWithUppercase(s[i]))
	al.add(s[i]);
    }
    return al.toArray(new String[0]);
  }

  private static String[] removeCompoundWords(String[] s) {
    ArrayList<String> al = new ArrayList<String>();
    for (int i = 0; i < s.length; i++) {
      if (!w.isCompound(s[i]))
	al.add(s[i]);
    }
    return al.toArray(new String[0]);
  }

  public static void main(String[] args) {
    println(new RiWordNet(pathWordNet).ignoreCompoundWords(false).getSynset(
	"medicare", "n"));
  }
}