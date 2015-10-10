/**
 * Java WordNet Library (JWNL)
 * See the documentation for copyright information.
 */
package rita.wordnet.jwnl;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import rita.wordnet.jwnl.dictionary.Dictionary;
import rita.wordnet.jwnl.util.ResourceBundleSet;
import rita.wordnet.jwnl.util.factory.Element;
import rita.wordnet.jwnl.util.factory.NameValueParam;
import rita.wordnet.jwnl.util.factory.Param;
import rita.wordnet.jwnl.util.factory.ParamList;
import rita.wordnet.jwnl.util.factory.ValueParam;
import rita.wordnet.jwnl.wndata.Adjective;
import rita.wordnet.jwnl.wndata.PointerType;
import rita.wordnet.jwnl.wndata.VerbFrame;

/** Contains system info as well as JWNL properties. */
public final class JWNL {
  // OS types
  public static final OS WINDOWS = new OS("windows");
  public static final OS UNIX = new OS("unix");
  public static final OS MAC = new OS("mac");
  public static final OS UNDEFINED = new OS("undefined");

  public static final OS[] DEFINED_OS_ARRAY = { WINDOWS, UNIX, MAC };
  public static final String OS_PROPERTY_NAME = "os.name";

  private static final String JAVA_VERSION_PROPERTY = "java.version";
  private static final String CORE_RESOURCE = "JWNLResource";

  // initialization stages
  private static final int UNINITIALIZED = 0;
  private static final int START = 1;
  private static final int DICTIONARY_PATH_SET = 2;
  private static final int VERSION_SET = 3;
  private static final int INITIALIZED = 4;

  private static Version _version;
  private static ResourceBundleSet _bundle;
  private static OS _currentOS = UNDEFINED;
  private static int _initStage = UNINITIALIZED;

  static {
    createResourceBundle();
    // set the OS
    String os = System.getProperty(OS_PROPERTY_NAME);
    for (int i = 0; i < DEFINED_OS_ARRAY.length; i++)
      if (DEFINED_OS_ARRAY[i].matches(os))
	_currentOS = DEFINED_OS_ARRAY[i];
  }

  private JWNL() {
  }

  // tag names
  private static final String VERSION_TAG = "version";
  private static final String DICTIONARY_TAG = "dictionary";
  private static final String PARAM_TAG = "param";
  private static final String RESOURCE_TAG = "resource";

  // attribute names
  private static final String LANGUAGE_ATTRIBUTE = "language";
  private static final String COUNTRY_ATTRIBUTE = "country";
  private static final String CLASS_ATTRIBUTE = "class";
  private static final String NAME_ATTRIBUTE = "name";
  private static final String VALUE_ATTRIBUTE = "value";
  private static final String PUBLISHER_ATTRIBUTE = "publisher";
  private static final String NUMBER_ATTRIBUTE = "number";

  public static void initialize(InputStream propertiesStream)
      throws JWNLException {

    // /if (propertiesStream == null) throw new
    // JWNLException("Null stream in JWNL.initialize!");

    checkInitialized(UNINITIALIZED);

    _initStage = START;
    try {
      // find the properties file
      if (propertiesStream == null || propertiesStream.available() <= 0) {
	throw new JWNLException("JWNL_EXCEPTION_001");
      }
    } catch (IOException ex) {
      throw new JWNLException("JWNL_EXCEPTION_001", ex);
    }

    // parse the properties file
    Document doc = null;
    try {
      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      factory.setValidating(false);
      DocumentBuilder docBuilder = factory.newDocumentBuilder();
      doc = docBuilder.parse(propertiesStream);
    } catch (Exception ex) {

      throw new JWNLException("JWNL_EXCEPTION_002", ex);
    }

    // do this in a separate try/catch since parse can also throw an IOException
    try {
      propertiesStream.close();
    } catch (IOException ex) {
    }

    org.w3c.dom.Element root = doc.getDocumentElement();

    // set the locale
    _bundle.setLocale(getLocale(getAttribute(root, LANGUAGE_ATTRIBUTE),
	getAttribute(root, COUNTRY_ATTRIBUTE)));

    // add additional resources
    NodeList resourceNodes = root.getElementsByTagName(RESOURCE_TAG);
    for (int i = 0; i < resourceNodes.getLength(); i++) {
      String resource = getAttribute(resourceNodes.item(i), CLASS_ATTRIBUTE);
      // System.err.println("JWNL.initialize("+resourceNodes.item(i)+")");
      if (resource != null) {
	System.err.println("JWNL.addBundle(" + resource + ")");
	_bundle.addResource(resource);
      }
    }

    // System.err.println("JWNL.initialize("+_bundle+")");

    // initialize bundle-dependent resources
    PointerType.initialize();
    Adjective.initialize();
    VerbFrame.initialize();

    // parse version information
    NodeList versionNodes = root.getElementsByTagName(VERSION_TAG);
    if (versionNodes.getLength() == 0) {
      throw new JWNLException("JWNL_EXCEPTION_003");
    }
    Node version = versionNodes.item(0);

    _initStage = DICTIONARY_PATH_SET;

    String number = getAttribute(version, NUMBER_ATTRIBUTE);
    _version = new Version(getAttribute(version, PUBLISHER_ATTRIBUTE),
	(number == null) ? 0.0 : Double.parseDouble(number), getLocale(
	    getAttribute(version, LANGUAGE_ATTRIBUTE),
	    getAttribute(version, COUNTRY_ATTRIBUTE)));

    _initStage = VERSION_SET;

    // parse dictionary
    NodeList dictionaryNodeList = root.getElementsByTagName(DICTIONARY_TAG);
    if (dictionaryNodeList.getLength() == 0)
      throw new JWNLException("JWNL_EXCEPTION_005");

    Node n = dictionaryNodeList.item(0);
    Element ele = createElementFromNode(n);
    ele.install();

    _initStage = INITIALIZED;
  }

  private static void createResourceBundle() {

    _bundle = new ResourceBundleSet(CORE_RESOURCE);
    if (_bundle == null || !_bundle.getKeys().hasMoreElements())
      throw new RuntimeException("NULL BUNDLE: CORE_RESOURCE");
  }

  private static Element createElementFromNode(Node node) throws JWNLException {

    return new Element(getAttribute(node, CLASS_ATTRIBUTE),
	getParams(node.getChildNodes()));
  }

  static int count = 0;

  private static Param[] getParams(NodeList list) throws JWNLException {

    List params = new ArrayList();
    for (int i = 0; i < list.getLength(); i++) {

      Node n = list.item(i);

      if (n.getNodeType() == Node.ELEMENT_NODE
	  && n.getNodeName().equals(PARAM_TAG)) {

	String name = getAttribute(n, NAME_ATTRIBUTE);
	String value = getAttribute(n, VALUE_ATTRIBUTE);

	if (name == null && value == null)
	  throw new JWNLException("JWNL_EXCEPTION_008");

	Param param = null;
	if (value == null) {

	  param = new ParamList(name.toLowerCase(),
	      getParams(n.getChildNodes()));
	} else if (name == null) {

	  param = new ValueParam(value, getParams(n.getChildNodes()));
	} else {

	  /*
	   * if (++count < 100) { System.out.println(name+" -> "+value);
	   * 
	   * Param[] params2 = getParams(n.getChildNodes()); for (int j = 0; j <
	   * params2.length; j++) { System.out.println("  "+j+") "+params2[j]);
	   * } }
	   */

	  param = new NameValueParam(name.toLowerCase(), value,
	      getParams(n.getChildNodes()));
	}

	params.add(param);
      }
    }
    return (Param[]) params.toArray(new Param[params.size()]);
  }

  private static String getAttribute(Node node, String attributeName) {

    NamedNodeMap map = node.getAttributes();
    if (map != null) {
      Node n = map.getNamedItem(attributeName);
      if (n != null) {
	return n.getNodeValue();
      }
    }
    return null;
  }

  private static Locale getLocale(String language, String country) {

    if (language == null) {
      return Locale.getDefault();
    } else if (country == null) {
      return new Locale(language, "");
    } else {
      return new Locale(language, country);
    }
  }

  public static boolean isInitialized() {
    return _initStage == INITIALIZED;
  }

  /** Get the current OS. */
  public static OS getOS() {
    return _currentOS;
  }

  public static double getJavaVersion() {
    String versionStr = System.getProperty(JAVA_VERSION_PROPERTY);
    return Double.parseDouble(versionStr.substring(0, 3));
  }

  /** Get the current WordNet version */
  public static Version getVersion() {
    checkInitialized(VERSION_SET);
    return _version;
  }

  public static ResourceBundle getResourceBundle() {
    return _bundle;
  }

  /** Resolve <var>msg</var> in one of the resource bundles used by the system */
  public static String resolveMessage(String msg) {
    return resolveMessage(msg, new Object[0]);
  }

  /**
   * Resolve <var>msg</var> in one of the resource bundles used by the system.
   * 
   * @param obj
   *          parameter to insert into the resolved message
   */
  public static String resolveMessage(String msg, Object obj) {
    return resolveMessage(msg, new Object[] { obj });
  }

  /**
   * Resolve <var>msg</var> in one of the resource bundles used by the system
   * 
   * @param params
   *          parameters to insert into the resolved message
   */
  public static String resolveMessage(String msg, Object[] params) {
    // System.err.println("JWNL.resolveMessage("+msg+")");
    checkInitialized(UNINITIALIZED);
    return insertParams(_bundle.getString(msg), params);
  }

  private static String insertParams(String str, Object[] params) {
    StringBuffer buf = new StringBuffer();
    int startIndex = 0;
    for (int i = 0; i < params.length && startIndex <= str.length(); i++) {
      int endIndex = str.indexOf("{" + i, startIndex);
      if (endIndex != -1) {
	buf.append(str.substring(startIndex, endIndex));
	buf.append(params[i] == null ? null : params[i].toString());
	startIndex = endIndex + 3;
      }
    }
    buf.append(str.substring(startIndex, str.length()));
    return buf.toString();
  }

  private static void checkInitialized(int requiredStage) {
    if (requiredStage > _initStage) {
      throw new JWNLRuntimeException("JWNL_EXCEPTION_007");
    }
  }

  public static void shutdown() {
    _initStage = UNINITIALIZED;
    Dictionary.uninstall();
    _version = null;
    createResourceBundle();
  }

  /**
   * Used to create constants that represent the major categories of operating
   * systems.
   */
  public static final class OS {
    private String _name;

    protected OS(String name) {
      _name = name;
    }

    public String toString() {
      return resolveMessage("JWNL_TOSTRING_001", _name);
    }

    /**
     * Returns true if <var>testOS</var> is a version of this OS. For example,
     * calling WINDOWS.matches("Windows 95") returns true.
     */
    public boolean matches(String test) {
      return test.toLowerCase().indexOf(_name.toLowerCase()) >= 0;
    }
  }

  /** Represents a version of WordNet. */
  public static final class Version {
    private static final String UNSPECIFIED = "unspecified";

    private String _publisher;
    private double _number;
    private Locale _locale;

    public Version(String publisher, double number, Locale locale) {
      if (publisher == null) {
	publisher = UNSPECIFIED;
      }
      _publisher = publisher;
      _number = number;
      _locale = locale;
    }

    public String getPublisher() {
      return _publisher;
    }

    public double getNumber() {
      return _number;
    }

    public Locale getLocale() {
      return _locale;
    }

    public boolean equals(Object obj) {
      return (obj instanceof Version)
	  && _publisher.equals(((Version) obj)._publisher)
	  && _number == ((Version) obj)._number
	  && _locale.equals(((Version) obj)._locale);
    }

    public String toString() {
      return resolveMessage("JWNL_TOSTRING_002", new Object[] { _publisher,
	  new Double(_number), _locale });
    }

    public int hashCode() {
      return _publisher.hashCode() ^ (int) (_number * 100);
    }
  }
}