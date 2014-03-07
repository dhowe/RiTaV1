package rita;

import java.util.Map;

public interface RiStringIF extends CharSequence
{
  // first arg for these is regex 
  RiString replaceFirst(String regex, String t);
  RiString replaceLast(String regex, String t);
  RiString replaceAll(String regex, String t);
  RiString[] split(String regex); 
  RiString[] split(); 
  
  boolean startsWith(String s);
  boolean endsWith(String s); 
  
  // these return 'this'
  RiString trim();
  RiString toLowerCase();
  RiString toUpperCase();
  RiString insertAt(int charIdx, String toInsert);
  
  RiString removeCharAt(int charIdx);
  RiString insertCharAt(int charIdx, char c);
  RiString replaceCharAt(int charIdx, String s);
  
  RiString removeWordAt(int wordIdx);
  RiString insertWordAt(int wordIdx, String s);
  RiString replaceWordAt(int wordIdx, String s);
  
  // java-style override
  RiString concat(String cs);
  RiString concat(RiString cs);
  
  // equivalent to js-style getter/setters
  String text();
  RiString text(String s);
  
  boolean equalsIgnoreCase(String cs);
  int indexOf(String s);  
  RiString analyze();
  char charAt(int charIdx);
  String get(String s);
  Map features();
  char[] toCharArray();
  int lastIndexOf(String s);
  int length();
  int wordCount();
  
  String posAt(int charIdx);
  String slice(int start, int end); // ?
  String substring(int start, int end);
  String substr(int start, int end); // ?
  String wordAt(int charIdx);
  
  String[] pos();
  String[] words();
  String[] match(String regex);
  
  RiString copy(); // DCH: ADDED
}
