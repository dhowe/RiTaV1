package rita.docgen;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Iterator;
import java.util.zip.GZIPOutputStream;

import org.json.*;

import processing.core.PApplet;
import rita.RiTa;

public class DocGenerator extends PApplet
{
  static final String VERSION = "0.13";
  static final boolean OUTPUT_HTML =  false;
  
  static String DATA_DIR = "RiTaLibraryJS/docs/";
  static String OUTPUT_DIR = "/tmp/";
  
  static String[] CLASS_NAMES = { 
    "RiTa", "RiString","RiText","RiGrammar",
    "RiMarkov","RiLexicon","RiTaEvent","RiWordNet" 
  };

  static boolean DBUG = false;
  
  static {
    
    RiTa.SILENT = !DBUG;
    System.out.println("[INFO] DocGen.version ["+VERSION+"]");
  }
 
  // ////////////////////////////////////////////////////////////////

  public static void go(String[] a)
  {
    if (a.length < 1)
    {
      System.out.println("\nusage: java rita.docgen.DocGenerator output-dir [input-dir]\n");
      System.exit(1);
    }
    else
    {
      pln("\nCWD: " + System.getProperty("user.dir"));

      OUTPUT_DIR = a[0];
    
      if (a.length > 1) 
        DATA_DIR = a[1];

      pln("DATA: " + DATA_DIR);
      
      if (a.length > 2) { 
        CLASS_NAMES = new String[] { a[2] };
        pln("CLASSES: " + Arrays.asList(CLASS_NAMES));
      }
    }

    htmlTemplate = DATA_DIR + "html/template.html";
    
    for (int i = 0; i < CLASS_NAMES.length; i++)
    {
      pln("\n******     " + CLASS_NAMES[i] + "     ******\n");
      pln("  Template : " + htmlTemplate);
      parseJSON(CLASS_NAMES[i]);
    }

    pln("\nDONE: files written to " + OUTPUT_DIR);
  }

  static int numOfMethods, numOfparameters, numOfreturns;

  static String[] lines, tmp_methodName, tmp_example, tmp_description, tmp_syntax,
      tmp_parameterType, tmp_parameterDesc, tmp_parameters, tmp_return, tmp_returnType,
      tmp_returnDesc, tmp_returns, tmp_related, tmp_platform, tmp_note, tmp_parameter;
  
  static boolean[] tmp_hidden, tmp_isVariable;

  static String htmlTemplate;

  static void parseJSON(String shortName)
  {
    String jsonFile = DATA_DIR + "json/" + shortName + ".json";

    pln("  DocFile : " + jsonFile);

    String result = RiTa.loadString(jsonFile);//join(loadTheStrings(request), "");

    result = "{ \"success\": true, \"pagination\": "
        + "{ \"current\": 1, \"max\": 1 }, \"refobj\": " + result + "}";
    
    try
    {
      JSONObject wrapper = new JSONObject(result);
      JSONObject json = wrapper.getJSONObject("refobj");
      String className = json.getString("class");
      pln("  Class : " + className);
      JSONArray items = json.getJSONArray("fields");

      numOfMethods = items.length();
      pln("  Fields("+numOfMethods+") : ");

      initArrays();

      for (int j = 0; j < numOfMethods; j++)
      {
        lines = RiTa.loadStrings(htmlTemplate);

        JSONObject entry = items.getJSONObject(j);

        tmp_hidden[j] = false;
        if (entry.has("hidden"))
          tmp_hidden[j] = entry.getBoolean("hidden");
        
        tmp_isVariable[j] = false;
        if (entry.has("variable"))
          tmp_isVariable[j] = entry.getBoolean("variable");
        
        tmp_methodName[j] = entry.getString("methodName");
        pln("    " + tmp_methodName[j]);
        
        tmp_example[j] = "";
        if (entry.has("example"))
          tmp_example[j] = entry.getString("example");

        tmp_description[j] = entry.getString("description");
        tmp_syntax[j] = entry.getString("syntax");

        JSONArray parametersJSON = entry.getJSONArray("parameters");
        numOfparameters = parametersJSON.length();
        tmp_parameter = new String[numOfparameters];
        tmp_parameterType = new String[numOfparameters];
        tmp_parameterDesc = new String[numOfparameters];
        for (int k = 0; k < numOfparameters; k++)
        {
          JSONObject parametersJSONEntry = parametersJSON.getJSONObject(k);
          tmp_parameterType[k] = parametersJSONEntry.getString("type");
          tmp_parameterDesc[k] = parametersJSONEntry.getString("desc");
        }

        JSONArray returnsJSON = entry.getJSONArray("returns");
        numOfreturns = returnsJSON.length();
        tmp_return = new String[numOfreturns];
        tmp_returnType = new String[numOfreturns];
        tmp_returnDesc = new String[numOfreturns];
        for (int k = 0; k < numOfreturns; k++)
        {

          JSONObject returnsJSONEntry = returnsJSON.getJSONObject(k);
          tmp_returnType[k] = returnsJSONEntry.getString("type");
          tmp_returnDesc[k] = returnsJSONEntry.getString("desc");

        }
        tmp_related[j] = entry.getString("related");
        tmp_platform[j] = entry.getString("platform");
        tmp_note[j] = entry.getString("note");

        template(j, shortName);
        
        plnHTML(shortName, tmp_methodName[j], tmp_isVariable[j]);
      }
    } 
    catch (JSONException e)
    {
      System.err.println("\nError parsing the JSONObject!");
      throw new RuntimeException(e);
    }
  }

  // ----------------------------------------------------------------------
  static void initArrays()
  {
    tmp_methodName = new String[numOfMethods];
    tmp_example = new String[numOfMethods];
    tmp_description = new String[numOfMethods];
    tmp_syntax = new String[numOfMethods];
    tmp_parameters = new String[numOfMethods];
    tmp_returns = new String[numOfMethods];
    tmp_related = new String[numOfMethods];
    tmp_platform = new String[numOfMethods];
    tmp_note = new String[numOfMethods];
    tmp_hidden = new boolean[numOfMethods];
    tmp_isVariable = new boolean[numOfMethods];
  }

  static public PrintWriter createWriter(File file)
  {
    if (file == null)
      throw new RuntimeException("null File passed to createWriter()");
    
    try
    {
      createPath(file); // make sure in-between folders exist
      OutputStream output = new FileOutputStream(file);
      if (file.getName().toLowerCase().endsWith(".gz"))
      {
        output = new GZIPOutputStream(output);
      }
      return createWriter(output);

    }
    catch (Exception e)
    {
      e.printStackTrace();
      throw new RuntimeException
        ("Couldn't create writer for "+ file.getAbsolutePath());
    }
  }

  static void template(int idx, String shortName)
  {
    if (tmp_hidden[idx]) return;
    

    String folder_methodName = tmp_methodName[idx].replaceAll("\\(\\)", "_");
    
    String fname = OUTPUT_DIR + shortName+"/"+folder_methodName+"/index.html";
    
    lines = replaceArr(lines, "tmp_className", shortName);
    lines = replaceArr(lines, "tmp_methodName", tmp_methodName[idx]);
    lines = replaceArr(lines, "tmp_description", tmp_description[idx]);
    lines = replaceArr(lines, "tmp_platform", tmp_platform[idx]);
      
    handleOptionalTag("example", tmp_example[idx]);
    handleOptionalTag("syntax", tmp_syntax[idx]);
    handleOptionalTag("related", tmp_related[idx]);
    handleOptionalTag("note", tmp_note[idx]);
    
    handleParameters(tmp_parameters[idx]);
    handleReturns(tmp_returns[idx]);
    
    if (tmp_isVariable[idx])
      lines = replaceArr(lines,"<th scope=\"row\">Returns</th>","<th scope=\"row\">Type</th>");
    
    writeFile(fname, lines);
  }

  private static void writeFile(String fname, String[] theLines)
  {
    PrintWriter output = createWriter(new File(fname));
    for (int i = 0; i < theLines.length; i++) {
      output.println(theLines[i]);
    }
    output.flush(); 
    output.close(); 
  }
  
  private static void handleParameters(String data)
  {
    if (numOfparameters<1)
    {
      lines = replaceArr(lines, "<tr class='Parameters'>", "<tr class='Parameters' style='display:none'>");
      return;
    }
    
    for (int i = 0; i < lines.length; i++)
    {
      String[] m = match(lines[i], "tmp_parameters");
      if (m != null)
      {
        for (int k = 0; k < numOfparameters; k++)
        {
          tmp_parameter[k] = "<tr class=''><th width='25%' scope='row' class=nobold>" + tmp_parameterType[k]
              + "</th><td width='75%'>" + tmp_parameterDesc[k] + "</td></tr>";
          
          data = data == null ? "" : data; 
          data += tmp_parameter[k];
        }
        
        if (tmp_parameters != null)
          lines[i] = lines[i].replaceAll("tmp_parameters", data);
      }
    }
  }
  

  private static void handleReturns(String data)
  {
    if (tmp_returnType.length==0 || tmp_returnType[0].length() == 0)
    {
      lines = replaceArr(lines, "tmp_returns", "void");
      return;
    }

    for (int i = 0; i < lines.length; i++)
    {
      String[] m2 = match(lines[i], "tmp_returns");
      
      if (m2 != null)
      {
        for (int k = 0; k < numOfreturns; k++)
        {
          tmp_return[k] = "<tr class=''><th width='25%' scope='row' class=nobold>" + tmp_returnType[k]
              + "</th><td width='75%'>" + tmp_returnDesc[k] + "</td></tr>";
          
          data = data == null ? "" : data; 
          data += tmp_return[k];
        }
        
        if (tmp_returns != null)
          lines[i] = lines[i].replaceAll("tmp_returns", data);
      }
    }    
  }


  static void handleOptionalTag(String name, String data) {
    
    String uname = upperCaseFirst(name);
    lines = (data.length()>0) ? replaceArr(lines, "tmp_"+name, data) :
      replaceArr(lines, "<tr class='"+uname+"'>", "<tr class='"+uname+"' style='display:none'>");
  }
  
  static String upperCaseFirst(String value) {
    
    return Character.toString(value.charAt(0)).toUpperCase() + value.substring(1);
  }
  
  static String[] replaceArr(String[] in, String from, String to) {
    
    String delim = "_XXX_"; // hack
    String joined = RiTa.join(in, delim);
    joined = joined.replaceAll(from, to);
    return joined.split(delim);
  }

  private static void pln(String s)
  {
    if (!OUTPUT_HTML) System.out.println(s);
  }
  
  // <a href="RiText/fill_/index.html">fill()</a> 

  private static void plnHTML(String classShortName, String field, boolean isVar)
  {
    if (OUTPUT_HTML) {
      
      field = field.replaceAll("\\(\\)", RiTa.E);
      StringBuilder sb =  new StringBuilder();
      sb.append("<a href=\""+classShortName+"/");
      sb.append(field+"_/index.html\">"+field);
      if (!isVar) sb.append("()");
      sb.append("</a>");
      System.out.println(sb.toString());
    }
  }

  public static void main(String[] args)
  {
    // argument should be 'output/'
     
    //
    if (args.length==0)
      //go(new String[] {OUTPUT_DIR,DATA_DIR}); // ALL
      go(new String[] {OUTPUT_DIR,DATA_DIR,"RiWordNet"}); // ONE
    else
      go(args);
  }

}
