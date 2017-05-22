package rita.support;

/**
 * Portions Copyright 2001 Sun Microsystems, Inc.
 * Portions Copyright 1999-2001 Language Technologies Institute, 
 * Carnegie Mellon University.
 * All Rights Reserved.  Use is subject to license terms.
 */

import java.io.*;
import java.util.*;

import rita.RiTa;
import rita.RiTaException;

/**
 * Provides the phone list for words using the CMU6 letter-to-sound (LTS) rules,
 * which are based on the Black, Lenzo, and Pagel paper, "Issues in Building
 * General Letter-to-Sound Rules." Proceedings of ECSA Workshop on Speech
 * Synthesis, pages 77-80, Australia, 1998.
 */
public class LetterToSound implements Constants {
  static LetterToSound instance;

  public static LetterToSound getInstance() {
    if (instance == null)
      instance = new LetterToSound();
    return instance;
  }

  static Map<String, String> cache = new HashMap<String, String>();
  static {
    cache.put("a", "ey");
    cache.put("the", "dh-ah");
  }

  /**
   * Entry in file represents the total number of states in the file. This
   * should be at the top of the file. The format should be "TOTAL n" where n is
   * an integer value.
   */
  final static String TOTAL = "TOTAL";

  /**
   * Entry in file represents the beginning of a new letter index. This should
   * appear before the list of a new set of states for a particular letter. The
   * format should be "INDEX n c" where n is the index into the state machine
   * array and c is the character.
   */
  final static String INDEX = "INDEX";

  /**
   * Entry in file represents a state. The format should be "STATE i c t f"
   * where 'i' represents an index to look at in the decision string, c is the
   * character that should match, t is the index of the state to go to if there
   * is a match, and f is the of the state to go to if there isn't a match.
   */
  final static String STATE = "STATE";

  /**
   * Entry in file represents a final state. The format should be "PHONE p"
   * where p represents a phone string that comes from the phone table.
   */
  final static String PHONE = "PHONE";

  /**
   * If true, the state string is tokenized when it is first read. The side
   * effects of this are quicker lookups, but more memory usage and a longer
   * startup time.
   */
  protected boolean tokenizeOnLoad = false;

  /**
   * If true, the state string is tokenized the first time it is referenced. The
   * side effects of this are quicker lookups, but more memory usage.
   */
  protected boolean tokenizeOnLookup = true;

  /**
   * The LTS state machine. Entries can be String or State. An ArrayList could
   * be used here -- I chose not to because I thought it might be quicker to
   * avoid dealing with the dynamic resizing.
   */
  private Object[] stateMachine = null;

  /**
   * The number of states in the state machine.
   */
  private int numStates = 0;

  /**
   * The 'window size' of the LTS rules.
   */
  private final static int WINDOW_SIZE = 4;

  /**
   * An array of characters to hold a string for checking against a rule. This
   * will be reused over and over again, so the goal was just to have a single
   * area instead of new'ing up a new one for every word. The name choice is to
   * match that in Flite's <code>cst_lts.c</code>.
   */
  private char[] fval_buff = new char[WINDOW_SIZE * 2];

  /**
   * The indices of the starting points for letters in the state machine.
   */
  protected HashMap letterIndex;

  /**
   * The list of phones that can be returned by the LTS rules.
   */
  static private List phonemeTable;

  private LetterToSound() {
    loadLTS();
  }

  protected void loadLTS() {

    InputStream is = RiTa.class.getResourceAsStream(Constants.DEFAULT_LTS);
    try {
      if (is == null)
	throw new Exception("No LTS rules found");
      loadText(is).close();
    } catch (Exception e) {
      throw new RiTaException(e);
    }
  }

  /**
   * Loads the LTS rules from the given text input stream. The stream is not
   * closed after the rules are read.
   * 
   * @param is
   *          the input stream
   * 
   * @throws IOException
   *           if an error occurs on input.
   */
  private InputStream loadText(InputStream is) throws IOException {
    // TODO: compare perf. against parsing this with a JSON parser

    BufferedReader reader;
    String line;

    letterIndex = new HashMap();

    reader = new BufferedReader(new InputStreamReader(is));
    line = reader.readLine();
    while (line != null) {
      if (!line.startsWith("***"))
	parseAndAdd(line);
      line = reader.readLine();
    }

    return is;
  }

  public static String[] impossOnsets = { "pw", "bw", "fw", "vw" };

  // two labials
  // non-strident coronal followed by a lateral
  // voiced fricative
  // palatal constant
  public static boolean prohibitedOnset(String onset) {

    return (Arrays.asList(impossOnsets).contains(onset));
  }

  @SuppressWarnings("boxing")
  public String syllabify(String[] phones) {

    ArrayList<Integer> tmpOnset = new ArrayList<Integer>();
    boolean legalOnset = false;

    String sylls = E;
    int sonorityDistance = 2;

    // store phonemes and tags
    String[][] ONCs = new String[phones.length][2];

    String[] type = new String[phones.length];
    for (int i = 0; i < phones.length; i++) {

      String phone = phones[i].replaceAll("[102]", E);
      type[i] = (!Phoneme.isVowel(phone)) ? "consonant" : "vowel";
    }

    int i = 0;
    int sonA, sonB;
    boolean vowels = false;

    if (phones.length > 0) {

      // until current phoneme is vowel, label current phoneme as onset
      while (!type[i].equals("vowel")) {
	
	ONCs[i][0] = phones[i];
	ONCs[i][1] = "O";// +index;
	
	if (++i == phones.length) { // ADDED: (DCH) 1/14/16
	  if (!RiTa.SILENT)
	    System.err.println("[WARN] No vowel in LTS phonemes: ["+RiTa.join(phones)+"]");
	  break;
	}
      }

      while (i < phones.length) {

	if (phones[i].indexOf("1") == -1) {
	  
	  ONCs[i][0] = phones[i] + "0";
	} 
	else {
	  
	  ONCs[i][0] = phones[i];
	}
	
	ONCs[i][1] = "N";// +index;
	i++;
	vowels = false;

	// if there are no more vowels in the word, label all remaining
	// consonants and codas
	for (int j = i; j < phones.length; j++) {
	  if (type[j] == "vowel") {
	    vowels = true;
	  }
	}

	if (vowels == false) {
	  while (i < phones.length) {
	    ONCs[i][0] = phones[i];
	    ONCs[i][1] = "C";// +index;
	    i++;
	  }
	} else {

	  // label all consonants before next vowel as onsets
	  tmpOnset.clear();
	  while (type[i] != "vowel") {
	    ONCs[i][0] = phones[i];
	    ONCs[i][1] = "O";// +index;
	    tmpOnset.add(i);
	    i++;
	  }// until onset is legal, coda = coda + first phoneme of onset

	  if (tmpOnset.size() > 1) {
	    legalOnset = false;
	    String currOnset = E;
	    while (!legalOnset) {
	      currOnset = E;
	      for (int m = 0; m < tmpOnset.size(); m++) {
		currOnset += phones[tmpOnset.get(m)].replaceAll("[102]", E)
		    .toLowerCase() + " ";
	      }
	      // this needs to be changed...to prohibited, not allowed...
	      if (prohibitedOnset(currOnset.trim())) {
		legalOnset = false;

	      } else {

		legalOnset = true;
		for (int k = 1; k < tmpOnset.size(); k++) {
		  sonA = Phoneme.getSonority(phones[tmpOnset.get(k)]
		      .replaceAll("[102]", E));
		  sonB = Phoneme.getSonority(phones[tmpOnset.get(k - 1)]
		      .replaceAll("[102]", E));
		  if (Math.abs(sonA - sonB) >= sonorityDistance && sonA >= 0
		      && sonB >= 0) {
		    continue;
		  } else if (sonA == -1 || sonB == -1) {
		    System.out.println("PHONEME NOT FOUND: "
			+ phones[tmpOnset.get(k)] + " or "
			+ phones[tmpOnset.get(k - 1)]);
		  } else {
		    legalOnset = false;
		    break;
		  } // need to also check if it's on the prohibited list

		}
	      }

	      if (!legalOnset) {
		ONCs[tmpOnset.get(0)][1] = "C";
		tmpOnset.remove(0);
	      }
	    }
	  }
	}
      }
    }

    String prev = E, curr = E;
    for (int j = 0; j < ONCs.length; j++) {
      if (j > 0) {
	prev = curr;
	curr = ONCs[j][1];
	if (prev.equals("C") && curr.equals("O") || prev.equals("N")
	    && curr.equals("O")) {
	  sylls += " " + ONCs[j][0];
	} else {
	  sylls += "-" + ONCs[j][0];
	}

      } else {
	curr = ONCs[j][1];
	sylls += ONCs[j][0];
      }
    }

    return sylls;
  }

  /**
   * Creates a word from the given input line and add it to the state machine.
   * It expects the TOTAL line to come before any of the states.
   * 
   * @param line
   *          the line of text from the input file
   */
  protected void parseAndAdd(String aline) {
    String line = aline.replaceAll("(^'|',?)", E);
    StringTokenizer tokenizer = new StringTokenizer(line, " ");
    String type = tokenizer.nextToken();

    if (type.equals(STATE) || type.equals(PHONE)) {
      stateMachine[numStates++] = tokenizeOnLoad ? getState(type, tokenizer)
	  : line;
    } else if (type.equals(INDEX)) {
      int index = Integer.parseInt(tokenizer.nextToken());
      letterIndex.put(tokenizer.nextToken(), index);
    } else if (type.equals(TOTAL)) {
      stateMachine = new Object[Integer.parseInt(tokenizer.nextToken())];
    }
  }

  private List findPhonemes() // for documentation
  {
    Set set = new HashSet();
    for (int i = 0; i < stateMachine.length; i++) {
      if (stateMachine[i] instanceof FinalState) {
	FinalState fstate = (FinalState) stateMachine[i];
	if (fstate.phoneList != null) {
	  for (int j = 0; j < fstate.phoneList.length; j++) {
	    set.add(fstate.phoneList[j]);
	  }
	}
      }
    }
    return new ArrayList(set);
  }

  /**
   * Gets the <code>State</code> at the given index. This may replace a
   * <code>String</code> at the current spot with an actual <code>State</code>
   * instance.
   * 
   * @param i
   *          the index into the state machine
   * 
   * @return the <code>State</code> at the given index.
   */
  protected State getState(int i) {
    State state = null;
    if (stateMachine[i] instanceof String) {
      state = getState((String) stateMachine[i]);
      if (tokenizeOnLookup) {
	stateMachine[i] = state;
      }
    } else {
      state = (State) stateMachine[i];
    }
    return state;
  }

  /**
   * Gets the <code>State</code> based upon the <code>String</code>.
   * 
   * @param s
   *          the string to parse
   * 
   * @return the parsed <code>State</code>
   */
  protected State getState(String s) {
    StringTokenizer tokenizer = new StringTokenizer(s, " ");
    return getState(tokenizer.nextToken(), tokenizer);
  }

  /**
   * Gets the <code>State</code> based upon the <code>type</code> and
   * <code>tokenizer<code>.
   * 
   * @param type
   *          one of <code>STATE</code> or <code>PHONE</code>
   * @param tokenizer
   *          a <code>StringTokenizer</code> containing the <code>State</code>
   * 
   * @return the parsed <code>State</code>
   */
  protected State getState(String type, StringTokenizer tokenizer) {
    if (type.equals(STATE)) {
      int index = Integer.parseInt(tokenizer.nextToken());
      String c = tokenizer.nextToken();
      int qtrue = Integer.parseInt(tokenizer.nextToken());
      int qfalse = Integer.parseInt(tokenizer.nextToken());
      return new DecisionState(index, c.charAt(0), qtrue, qfalse);
    } else if (type.equals(PHONE)) {
      return new FinalState(tokenizer.nextToken());
    }
    return null;
  }

  /**
   * Makes a character array that looks like "000#word#000".
   * 
   * @param word
   *          the original word
   * 
   * @return the padded word
   */
  protected char[] getFullBuff(String word) {
    char[] full_buff = new char[word.length() + (2 * WINDOW_SIZE)];

    // Make full_buff look like "000#word#000"
    //
    for (int i = 0; i < (WINDOW_SIZE - 1); i++) {
      full_buff[i] = '0';
    }
    full_buff[WINDOW_SIZE - 1] = '#';
    word.getChars(0, word.length(), full_buff, WINDOW_SIZE);
    for (int i = 0; i < (WINDOW_SIZE - 1); i++) {
      full_buff[full_buff.length - i - 1] = '0';
    }
    full_buff[full_buff.length - WINDOW_SIZE] = '#';

    return full_buff;
  }

  /**
   * Calculates the phone list for a given word. If a phone list cannot be
   * determined, <code>null</code> is returned. This particular implementation
   * ignores the part of speech.
   * 
   * @param word
   *          the word to find
   * 
   * @return the list of phones for word or <code>null</code>
   */
  public String getPhones(String word) {
    String result = cache.get(word); // check cache
    if (result == null) {

      if (RiTa.PRINT_LTS_INFO && !RiTa.SILENT)
	System.out.println("[INFO] Using LTS for '" + word + "'");

      List<String> phoneList = new ArrayList<String>();
      State currentState;
      Integer startIndex;
      int stateIndex;
      char c;

      // Create "000#word#000"
      char[] full_buff = getFullBuff(word);

      // For each character in the word, create a WINDOW_SIZE
      // context on each size of the character, and then ask the
      // state machine what's next. Its magic
      for (int pos = 0; pos < word.length(); pos++) {
	for (int i = 0; i < WINDOW_SIZE; i++) {
	  fval_buff[i] = full_buff[pos + i];
	  fval_buff[i + WINDOW_SIZE] = full_buff[i + pos + 1 + WINDOW_SIZE];
	}
	c = word.charAt(pos);
	startIndex = (Integer) letterIndex.get(Character.toString(c));
	if (startIndex == null) {
	  continue;
	}

	stateIndex = startIndex.intValue();
	currentState = getState(stateIndex);
	while (!(currentState instanceof FinalState)) {
	  stateIndex = ((DecisionState) currentState).getNextState(fval_buff);
	  currentState = getState(stateIndex);
	}

	((FinalState) currentState).append((ArrayList<String>) phoneList);
	// RiTa.out("phoneList: "+phoneList);
      }

      result = syllabify(phoneList.toArray(new String[0]));
      // result = result.replaceAll("ax","ah"); // added 12/22/15

      cache.put(word, result);
    }
    
    result = result.replaceAll("0",  "");
    if (!result.contains("1") && !result.contains("0") && result.length() > 0) {
      String[] phones = result.split("-");
      result = "";
      for (int i = 0; i < phones.length; i++) {
	if (Phoneme.isVowel(phones[i]))
	  phones[i] += "1";
	result += phones[i] + "-";
      }
      result = result.substring(0, result.length() - 1);
    }
      
    return result;
  }

  /**
   * A marker interface for the states in the LTS state machine.
   * 
   * @see DecisionState
   * @see FinalState
   */
  static interface State {
    public void writeBinary(DataOutputStream dos) throws IOException;

    public boolean compare(State other);
  }

  /**
   * A <code>State</code> that represents a decision to be made.
   * 
   * @see FinalState
   */
  static class DecisionState implements State {
    final static int TYPE = 1;

    int index;

    char c;

    int qtrue;

    int qfalse;

    /**
     * Class constructor.
     * 
     * @param index
     *          the index into a string for comparison to c
     * @param c
     *          the character to match in a string at index
     * @param qtrue
     *          the state to go to in the state machine on a match
     * @param qfalse
     *          the state to go to in the state machine on no match
     */
    public DecisionState(int index, char c, int qtrue, int qfalse) {
      this.index = index;
      this.c = c;
      this.qtrue = qtrue;
      this.qfalse = qfalse;
    }

    /**
     * Gets the next state to go to based upon the given character sequence.
     * 
     * @param chars
     *          the characters for comparison
     * 
     * @ret an index into the state machine.
     */
    public int getNextState(char[] chars) {
      return (chars[index] == c) ? qtrue : qfalse;
    }

    /**
     * Outputs this <code>State</code> as though it came from the text input
     * file.
     * 
     * @return a <code>String</code> describing this <code>State</code>.
     */
    public String toString() {
      return STATE + " " + Integer.toString(index) + " "
	  + Character.toString(c) + " " + Integer.toString(qtrue) + " "
	  + Integer.toString(qfalse);
    }

    /**
     * Writes this <code>State</code> to the given output stream.
     * 
     * @param dos
     *          the data output stream
     * 
     * @throws IOException
     *           if an error occurs
     */
    public void writeBinary(DataOutputStream dos) throws IOException {
      dos.writeInt(TYPE);
      dos.writeInt(index);
      dos.writeChar(c);
      dos.writeInt(qtrue);
      dos.writeInt(qfalse);
    }

    /**
     * Loads a <code>DecisionState</code> object from the given input stream.
     * 
     * @param dis
     *          the data input stream
     * @return a newly constructed decision state
     * 
     * @throws IOException
     *           if an error occurs
     */
    public static State loadBinary(DataInputStream dis) throws IOException {
      int index = dis.readInt();
      char c = dis.readChar();
      int qtrue = dis.readInt();
      int qfalse = dis.readInt();
      return new DecisionState(index, c, qtrue, qfalse);
    }

    /**
     * Compares this state to another state for debugging purposes.
     * 
     * @param other
     *          the other state to compare against
     * 
     * @return true if the states are equivalent
     */
    public boolean compare(State other) {
      if (other instanceof DecisionState) {
	DecisionState otherState = (DecisionState) other;
	return index == otherState.index && c == otherState.c
	    && qtrue == otherState.qtrue && qfalse == otherState.qfalse;
      }
      return false;
    }
  }

  /**
   * A <code>State</code> that represents a final state in the state machine. It
   * contains one or more phones from the phone table.
   * 
   * @see DecisionState
   */
  static class FinalState implements State {
    final static int TYPE = 2;

    String[] phoneList;

    /**
     * Class constructor. The string "epsilon" is used to indicate an empty
     * list.
     * 
     * @param phones
     *          the phones for this state
     */
    public FinalState(String phones) {
      if (phones.equals("epsilon")) {
	phoneList = null;
      } else {
	int i = phones.indexOf('-');
	if (i != -1) {
	  phoneList = new String[2];
	  phoneList[0] = phones.substring(0, i);
	  phoneList[1] = phones.substring(i + 1);
	} else {
	  phoneList = new String[1];
	  phoneList[0] = phones;
	}
      }
    }

    /**
     * Class constructor.
     * 
     * @param phones
     *          an array of phones for this state
     */
    public FinalState(String[] phones) {
      phoneList = phones;
    }

    /**
     * Appends the phone list for this state to the given <code>ArrayList</code>
     * .
     * 
     * @param al
     *          the list to append to
     */
    public void append(ArrayList al) {
      if (phoneList == null)
	return;

      for (int i = 0; i < phoneList.length; i++)
	al.add(phoneList[i]);
    }

    /**
     * Outputs this <code>State</code> as though it came from the text input
     * file. The string "epsilon" is used to indicate an empty list.
     * 
     * @return a <code>String</code> describing this <code>State</code>
     */
    public String toString() {
      if (phoneList == null) {
	return PHONE + " epsilon";
      } else if (phoneList.length == 1) {
	return PHONE + " " + phoneList[0];
      } else {
	return PHONE + " " + phoneList[0] + "-" + phoneList[1];
      }
    }

    /**
     * Compares this state to another state for debugging purposes.
     * 
     * @param other
     *          the other state to compare against
     * 
     * @return <code>true</code> if the states are equivalent
     */
    public boolean compare(State other) {
      if (other instanceof FinalState) {
	FinalState otherState = (FinalState) other;
	if (phoneList == null) {
	  return otherState.phoneList == null;
	}
	for (int i = 0; i < phoneList.length; i++) {
	  if (!phoneList[i].equals(otherState.phoneList[i])) {
	    return false;
	  }
	}
	return true;
      }
      return false;
    }

    /**
     * Writes this state to the given output stream.
     * 
     * @param dos
     *          the data output stream
     * 
     * @throws IOException
     *           if an error occurs
     */
    public void writeBinary(DataOutputStream dos) throws IOException {
      dos.writeInt(TYPE);
      if (phoneList == null) {
	dos.writeInt(0);
      } else {
	dos.writeInt(phoneList.length);
	for (int i = 0; i < phoneList.length; i++) {
	  dos.writeInt(phonemeTable.indexOf(phoneList[i]));
	}
      }
    }
  }

  public static void main(String[] args) {
    LetterToSound text = LetterToSound.getInstance();
    String phones = text.getPhones("washington");
    System.out.println(phones + "\t\t"
	+ (phones.equals("w-aa1 sh-ih-ng t-ah-n") ? "OK" : "FAIL"));

    // System.out.println(Arrays.asList(text.getPhones("laggin")));
    // System.out.println(Arrays.asList(text.getPhones("dragon")));
    // System.out.println(Arrays.asList(text.getPhones("hello")));
    // System.out.println(Arrays.asList(text.getPhones("antelope", "n")));
  }
}
