package rita.support;

import java.util.Map;
import java.util.Set;

public interface EnglishConstants
{
  RegexRule NULL_PLURALS = new RegexRule(
      "^(bantu|bengalese|bengali|beninese|boche|bonsai|"
      + "burmese|chinese|colossus|congolese|discus|gabonese|guyanese|japanese|javanese|"
      + "lebanese|maltese|olympics|portuguese|senegalese|siamese|singhalese|"
      + "sinhalese|sioux|sudanese|swiss|taiwanese|togolese|vietnamese|aircraft|"
      + "anopheles|apparatus|asparagus|barracks|bellows|bison|bluefish|bob|bourgeois|"
      + "bream|brill|butterfingers|cargo|carp|catfish|chassis|clothes|chub|cod|codfish|"
      + "coley|contretemps|corps|crawfish|crayfish|crossroads|cuttlefish|dace|deer|dice|digitalis|"
      + "dogfish|doings|dory|downstairs|eldest|earnings|economics|electronics|emphasis|finnan|"
      + "firstborn|fish|flatfish|flounder|fowl|fry|fries|works|globefish|goldfish|golf|"
      + "grand|grief|gudgeon|gulden|haddock|hake|halibut|headquarters|herring|hertz|horsepower|"
      + "goods|hovercraft|hundredweight|ironworks|jackanapes|kilohertz|kurus|kwacha|ling|lungfish|"
      + "mackerel|means|megahertz|moorfowl|moorgame|mullet|nepalese|offspring|[a-z]+osis$|[a-z]+ness$"
      + "|[a-z]+itis$|pampas|parr|(pants$)|"
      + "patois|pekinese|penn'orth|perch|pickerel|pike|pince-nez|plaice|precis|quid|rand|"
      + "rendezvous|revers|roach|roux|salmon|samurai|series|seychelles|seychellois|shad|"
      + "sheep|shellfish|smelt|spacecraft|species|starfish|stockfish|sunfish|superficies|"
      + "sweepstakes|swordfish|tench|tennis|tobacco|tope|triceps|trout|tuna|tunafish|tunny|turbot|trousers|"
      + "undersigned|veg|waterfowl|waterworks|waxworks|whiting|wildfowl|woodworm|"
      + "yen|aries|pisces|forceps|lieder|jeans|physics|mathematics|news|odds|politics|remains|"
      + "surroundings|thanks|statistics|goods|aids|wildlife)$", 0, "");

  
  String[] QUESTION_STARTS = {
    "Was", "What", "When", "Where", "How", "Which", "If",  
    "Who", "Is", "Could", "Might", "Will", "Does", "Why", "Are", 
  };  
  
  String[] W_QUESTION_STARTS = {
    "Was", "What", "When", "Where", "How", "Which",
    "Why", "Who", "Will", 
  };
   
  public static final String[] STOP_WORDS = { ".", ",", "THE", 
    "AND", "A", "OF", "\"", "IN", "I", ":", "YOU", "IS", "TO",
    "THAT", ")", "(", "IT", "FOR", "ON", "!", "HAVE", "WITH", "?",
    "THIS", "BE", "...", "NOT", "ARE", "AS", "WAS", "BUT", "OR", "FROM",
    "MY", "AT", "IF", "THEY", "YOUR", "ALL", "HE", "BY", "ONE",
    "ME", "WHAT", "SO", "CAN", "WILL", "DO", "AN", "ABOUT", "WE", "JUST",
    "WOULD", "THERE", "NO", "LIKE", "OUT", "HIS", "HAS", "UP", "MORE", "WHO",
    "WHEN", "DON'T", "SOME", "HAD", "THEM", "ANY", "THEIR", "IT'S", "ONLY",
    ";", "WHICH", "I'M", "BEEN", "OTHER", "WERE", "HOW", "THEN", "NOW",
    "HER", "THAN", "SHE", "WELL", "ALSO", "US", "VERY", "BECAUSE",
    "AM", "HERE", "COULD", "EVEN", "HIM", "INTO", "OUR", "MUCH",
    "TOO", "DID", "SHOULD", "OVER", "WANT", "THESE", "MAY", "WHERE", "MOST",
    "MANY", "THOSE", "DOES", "WHY", "PLEASE", "OFF", "GOING", "ITS", "I'VE",
    "DOWN", "THAT'S", "CAN'T", "YOU'RE", "DIDN'T", "ANOTHER", "AROUND",
    "MUST",  "FEW", "DOESN'T", "EVERY", "YES", "EACH", "MAYBE",
    "I'LL", "AWAY", "DOING", "OH", "ELSE", "ISN'T", "HE'S", "THERE'S", "HI",
    "WON'T", "OK", "THEY'RE", "YEAH", "MINE", "WE'RE", "WHAT'S", "SHALL",
    "SHE'S", "HELLO", "OKAY", "HERE'S", "-", "LESS"
  };
  
  /** 
   * Words that end in "-se" in their plural forms (like "nurse" etc.)
   * Borrowed from the PlingStemmer stemmer implementation 
   * included in the Java Tools pacakge 
   *   (see http://mpii.de/yago-naga/javatools).
   * @exclude
   */
  Set<String> categorySE_SES= new FinalSet<String>(
   "nurses",
   "cruises"
  );

  /** 
   * Words that do not have a distinct plural form (like "atlas" etc.)
   */
  Set<String> category00=new FinalSet<String>(
   "alias",
   "asbestos",
   "atlas",
   "barracks",
   "bathos",
   "bias",
   "breeches",
   "britches",
   "canvas",
   "chaos",
   "clippers",
   "contretemps",
   "corps",
   "cosmos",
   "crossroads",
   "diabetes",
   "ethos",
   "gallows",
   "gas",
   "graffiti",
   "headquarters",
   "herpes",
   "high-jinks",
   "innings",
   "jackanapes",
   "lens",
   "means",
   "measles",
   "mews",
   "mumps",
   "news",
   "pathos",
   "pincers",
   "pliers",
   "proceedings",
   "rabies",
   "rhinoceros",
   "sassafras",
   "scissors",
   "series",
   "shears",
   "species",
   "tuna"
  );

  /** 
   * Words that change from "-um" to "-a" (like "curriculum" etc.), listed in their plural forms
   * Borrowed from the PlingStemmer stemmer implementation 
   * included in the Java Tools pacakge 
   *   (see http://mpii.de/yago-naga/javatools).
   * @exclude
   */
  Set<String> categoryUM_A=new FinalSet<String>(
    "addenda",
    "agenda",
    "aquaria",
    "bacteria",
    "candelabra",
    "compendia",
    "consortia",
    "crania",
    "curricula",
    "data",
    "desiderata",
    "dicta",
    "emporia",
    "enconia",
    "errata",
    "extrema",
    "gymnasia",
    "honoraria",
    "interregna",
    "lustra",
    "maxima",
    "media",
    "memoranda",
    "millenia",
    "minima",
    "momenta",
    "optima",
    "ova",
    "phyla",
    "quanta",
    "rostra",
    "spectra",
    "specula",
    "stadia",
    "strata",
    "symposia",
    "trapezia",
    "ultimata",
    "vacua",
    "vela"
  );

  /** 
   * Words that change from "-on" to "-a" (like "phenomenon" etc.), listed in their plural forms
   * Borrowed from the PlingStemmer stemmer implementation 
   * included in the Java Tools pacakge 
   *   (see http://mpii.de/yago-naga/javatools).
   * @exclude
   */
  Set<String> categoryON_A=new FinalSet<String>(
    "aphelia",
    "asyndeta",
    "automata", 
    "criteria",
    "hyperbata",
    "noumena",
    "organa",
    "perihelia",
    "phenomena",
    "prolegomena"
  );

  /** 
   * Words that change from "-o" to "-i" (like "libretto" etc.), listed in their plural forms
   * Borrowed from the PlingStemmer stemmer implementation 
   * included in the Java Tools pacakge 
   *   (see http://mpii.de/yago-naga/javatools).
   * @exclude 
   */
  Set<String> categoryO_I=new FinalSet<String>(
   "alti",
   "bassi",
   "canti",
   "contralti",
   "crescendi",
   "libretti",
   "soli",
   "soprani",
   "tempi",
   "virtuosi"
  );

  /**
   *  Words that change from "-us" to "-i" (like "fungus" etc.), listed in their plural forms
   * Borrowed from the PlingStemmer stemmer implementation 
   * included in the Java Tools pacakge 
   *   (see http://mpii.de/yago-naga/javatools).
   * @exclude
   */
  Set<String> categoryUS_I=new FinalSet<String>(
   "alumni",
   "bacilli",
   "cacti",
   "foci",
   "fungi",
   "genii",
   "hippopotami",
   "incubi",
   "nimbi",
   "nuclei",
   "nucleoli",
   "octopi",
   "radii",
   "stimuli",
   "styli",
   "succubi",
   "syllabi",
   "termini",
   "tori",
   "umbilici",
   "uteri"
  );

  /** 
   * Words that change from "-ix" to "-ices" (like "appendix" etc.), listed in their plural forms
   * Borrowed from the PlingStemmer stemmer implementation 
   * included in the Java Tools pacakge 
   *   (see http://mpii.de/yago-naga/javatools).
   * @exclude
   */
  Set<String> categoryIX_ICES=new FinalSet<String>(
    "appendices",
    "cervices"
  );

  /**
   * Words that change from "-is" to "-es" (like "axis" etc.), listed in their plural forms
   * Borrowed from the PlingStemmer stemmer implementation 
   * included in the Java Tools pacakge 
   *   (see http://mpii.de/yago-naga/javatools).
   * @exclude
   */
  Set<String> categoryIS_ES=new FinalSet<String>(
    // plus everybody ending in theses
    "analyses",
    "axes",
    "bases",
    "crises",
    "diagnoses",
    "ellipses",
    "emphases",
    "neuroses",
    "oases",
    "paralyses",
    "synopses"
  );

  /** Words that change from "-oe" to "-oes" (like "toe" etc.), listed in their plural forms*/
  Set<String> categoryOE_OES=new FinalSet<String>(
    "aloes",
    "backhoes",
    "beroes",
    "canoes",
    "chigoes",
    "cohoes",
    "does",
    "felloes",
    "floes",
    "foes",
    "gumshoes",
    "hammertoes",
    "hoes",
    "hoopoes",
    "horseshoes",
    "leucothoes",
    "mahoes",
    "mistletoes",
    "oboes",
    "overshoes",
    "pahoehoes",
    "pekoes",
    "roes",
    "shoes",
    "sloes",
    "snowshoes",
    "throes",
    "tic-tac-toes",
    "tick-tack-toes",
    "ticktacktoes",
    "tiptoes",
    "tit-tat-toes",
    "toes",
    "toetoes",
    "tuckahoes",
    "woes"
  );

  /** Words that change from "-ex" to "-ices" (like "index" etc.), listed in their plural forms*/
  Set<String> categoryEX_ICES=new FinalSet<String>(
    "apices",
    "codices",
    "cortices",
    "indices",
    "latices",
    "murices",
    "pontifices",
    "silices",
    "simplices",
    "vertices",
    "vortices"
  );

  /** Words that change from "-u" to "-us" (like "emu" etc.), listed in their plural forms*/
  Set<String> categoryU_US=new FinalSet<String>(
   "apercus",
   "barbus",
   "cornus",
   "ecrus",
   "emus",
   "fondus",
   "gnus",
   "iglus",
   "mus",
   "nandus",
   "napus",
   "poilus",
   "quipus",
   "snafus",
   "tabus",
   "tamandus",
   "tatus",
   "timucus",
   "tiramisus",
   "tofus",
   "tutus"
  );

  /** Words that change from "-sse" to "-sses" (like "finesse" etc.), listed in their plural forms*/
  Set<String> categorySSE_SSES=new FinalSet<String>(
    //plus those ending in mousse
    "bouillabaisses",
    "coulisses",
    "crevasses",
    "crosses",
    "cuisses",
    "demitasses",
    "ecrevisses",
    "fesses",
    "finesses",
    "fosses",
    "impasses",
    "lacrosses",
    "largesses",
    "masses",
    "noblesses",
    "palliasses",
    "pelisses",
    "politesses",
    "posses",
    "tasses",
    "wrasses"
  );

  /** Words that change from "-che" to "-ches" (like "brioche" etc.), listed in their plural forms*/
  Set<String> categoryCHE_CHES=new FinalSet<String>(
    "adrenarches",
    "attaches",
    "avalanches",
    "barouches",
    "brioches",
    "caches",
    "caleches",
    "caroches",
    "cartouches",
    "cliches",
    "cloches",
    "creches",
    "demarches",
    "douches",
    "gouaches",
    "guilloches",
    "headaches",
    "heartaches",
    "huaraches",
    "menarches",
    "microfiches",
    "moustaches",
    "mustaches",
    "niches",
    "panaches",
    "panoches",
    "pastiches",
    "penuches",
    "pinches",
    "postiches",
    "psyches",
    "quiches",
    "schottisches",
    "seiches",
    "soutaches",
    "synecdoches",
    "thelarches",
    "troches"
  );

  /** Words that end with "-ics" and do not exist as nouns without the 's' (like "aerobics" etc.)*/
  Set<String> categoryICS=new FinalSet<String>(
    "aerobatics",
    "aerobics",
    "aerodynamics",
    "aeromechanics",
    "aeronautics",
    "alphanumerics",
    "animatronics",
    "apologetics",
    "architectonics",
    "astrodynamics",
    "astronautics",
    "astrophysics",
    "athletics",
    "atmospherics",
    "autogenics",
    "avionics",
    "ballistics",
    "bibliotics",
    "bioethics",
    "biometrics",
    "bionics",
    "bionomics",
    "biophysics",
    "biosystematics",
    "cacogenics",
    "calisthenics",
    "callisthenics",
    "catoptrics",
    "civics",
    "cladistics",
    "cryogenics",
    "cryonics",
    "cryptanalytics",
    "cybernetics",
    "cytoarchitectonics",
    "cytogenetics",
    "diagnostics",
    "dietetics",
    "dramatics",
    "dysgenics",
    "econometrics",
    "economics",
    "electromagnetics",
    "electronics",
    "electrostatics",
    "endodontics",
    "enterics",
    "ergonomics",
    "eugenics",
    "eurhythmics",
    "eurythmics",
    "exodontics",
    "fibreoptics",
    "futuristics",
    "genetics",
    "genomics",
    "geographics",
    "geophysics",
    "geopolitics",
    "geriatrics",
    "glyptics",
    "graphics",
    "gymnastics",
    "hermeneutics",
    "histrionics",
    "homiletics",
    "hydraulics",
    "hydrodynamics",
    "hydrokinetics",
    "hydroponics",
    "hydrostatics",
    "hygienics",
    "informatics",
    "kinematics",
    "kinesthetics",
    "kinetics",
    "lexicostatistics",
    "linguistics",
    "lithoglyptics",
    "liturgics",
    "logistics",
    "macrobiotics",
    "macroeconomics",
    "magnetics",
    "magnetohydrodynamics",
    "mathematics",
    "metamathematics",
    "metaphysics",
    "microeconomics",
    "microelectronics",
    "mnemonics",
    "morphophonemics",
    "neuroethics",
    "neurolinguistics",
    "nucleonics",
    "numismatics",
    "obstetrics",
    "onomastics",
    "orthodontics",
    "orthopaedics",
    "orthopedics",
    "orthoptics",
    "paediatrics",
    "patristics",
    "patristics",
    "pedagogics",
    "pediatrics",
    "periodontics",
    "pharmaceutics",
    "pharmacogenetics",
    "pharmacokinetics",
    "phonemics",
    "phonetics",
    "phonics",
    "photomechanics",
    "physiatrics",
    "pneumatics",
    "poetics",
    "politics",
    "pragmatics",
    "prosthetics",
    "prosthodontics",
    "proteomics",
    "proxemics",
    "psycholinguistics",
    "psychometrics",
    "psychonomics",
    "psychophysics",
    "psychotherapeutics",
    "robotics",
    "semantics",
    "semiotics",
    "semitropics",
    "sociolinguistics",
    "stemmatics",
    "strategics",
    "subtropics",
    "systematics",
    "tectonics",
    "telerobotics",
    "therapeutics",
    "thermionics",
    "thermodynamics",
    "thermostatics"
  );

  /** Words that change from "-ie" to "-ies" (like "auntie" etc.), listed in their plural forms*/
  Set<String> categoryIE_IES = new FinalSet<String>(
    "aeries",
    "anomies",
    "aunties",
    "baddies",
    "beanies",
    "birdies",
    "boccies",
    "bogies",
    "bolshies",
    "bombies",
    "bonhomies",
    "bonxies",
    "booboisies",
    "boogies",
    "boogie-woogies",
    "bookies",
    "booties",
    "bosies",
    "bourgeoisies",
    "brasseries",
    "brassies",
    "brownies",
    "budgies",
    "byrnies",
    "caddies",
    "calories",
    "camaraderies",
    "capercaillies",
    "capercailzies",
    "cassies",
    "catties",
    "causeries",
    "charcuteries",
    "chinoiseries",
    "collies",
    "commies",
    "cookies",
    "coolies",
    "coonties",
    "cooties",
    "corries",
    "coteries",
    "cowpies",
    "cowries",
    "cozies",
    "crappies",
    "crossties",
    "curies",
    "dachsies",
    "darkies",
    "dassies",
    "dearies",
    "dickies",
    "dies",
    "dixies",
    "doggies",
    "dogies",
    "dominies",
    "dovekies",
    "eyries",
    "faeries",
    "falsies",
    "floozies",
    "folies",
    "foodies",
    "freebies",
    "gaucheries",
    "gendarmeries",
    "genies",
    "ghillies",
    "gillies",
    "goalies",
    "goonies",
    "grannies",
    "grotesqueries",
    "groupies",
    "hankies",
    "hippies",
    "hoagies",
    "honkies",
    "hymies",
    "indies",
    "junkies",
    "kelpies",
    "kilocalories",
    "knobkerries",
    "koppies",
    "kylies",
    "laddies",
    "lassies",
    "lies",
    "lingeries",
    "magpies",
    "magpies",
    "marqueteries",
    "mashies",
    "mealies",
    "meanies",
    "menageries",
    "millicuries",
    "mollies",
    "facts1",
    "moxies",
    "neckties",
    "newbies",
    "nighties",
    "nookies",
    "oldies",
    "organdies",
    "panties",
    "parqueteries",
    "passementeries",
    "patisseries",
    "pies",
    "pinkies",
    "pixies",
    "porkpies",
    "potpies",
    "prairies",
    "preemies",
    "premies",
    "punkies",
    "pyxies",
    "quickies",
    "ramies",
    "reveries",
    "rookies",
    "rotisseries",
    "scrapies",
    "sharpies",
    "smoothies",
    "softies",
    "stoolies",
    "stymies",
    "swaggies",
    "sweeties",
    "talkies",
    "techies",
    "ties",
    "tooshies",
    "toughies",
    "townies",
    "veggies",
    "walkie-talkies",
    "wedgies",
    "weenies",
    "weirdies",
    "yardies",
    "yuppies",
    "zombies"
  );

  /** Maps irregular Germanic English plural nouns to their singular form */
  Map<String,String> irregular = new FinalMap<String,String>(
    "beefs","beef",
    "beeves","beef",
    "brethren","brother",
    "busses","bus",
    "cattle","cattlebeast",
    "children","child",
    "corpora","corpus",
    "ephemerides","ephemeris",
    "firemen","fireman",
    "genera","genus",
    "genies","genie",
    "genii","genie",
    "kine","cow",
    "lice","louse",
    "men","man",
    "mice","mouse",
    "mongooses","mongoose",
    "monies","money",
    "mythoi","mythos",
    "octopodes","octopus",
    "octopuses","octopus",
    "oxen","ox",
    "people","person",
    "soliloquies","soliloquy",
    "taxis","taxi",
    "throes","throes",
    "trilbys","trilby",
    "women","woman"
  ); 
  
  /** 
   * Contains word forms that can either be plural or singular
   * Borrowed from the PlingStemmer stemmer implementation 
   * @exclude 
   */
  Set<String> SINGULAR_AND_PLURAL = new FinalSet<String>(
        "acoustics",        
        "aesthetics",
        "aquatics",
        "basics",
        "ceramics",
        "classics",
        "cosmetics",
        "dermatoglyphics",
        "dialectics",
        "deer",
        "dynamics",
        "esthetics",
        "ethics",
        "harmonics",
        "heroics",
        "isometrics",
        "mechanics",
        "metrics",
        "statistics",
        "optic",
        "people",
        "physics",
        "polemics",
        "propaedeutics",
        "pyrotechnics",
        "quadratics",
        "quarters",
        "statistics",
        "tactics",
        "tropics"
  );
  
}
