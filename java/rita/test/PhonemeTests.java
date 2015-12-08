package rita.test;

import static rita.support.QUnitStubs.equal;

import org.junit.Test;

import rita.support.Constants;
import rita.support.Phoneme;

public class PhonemeTests implements Constants
{
  @Test
  public void testArpaToIPA()
  {
    Boolean includeStresses = false;
    
    String[] tests = {
	
	// from http://web.stanford.edu/class/linguist238/fig04.01.pdf
	"parsley",	"P AA1 R S L IY0 .",
	"catnip",	"K AE1 T N IH0 P .", 
	"bay",		"B EY1 .", 
	"dill",		"D IH1 L .",
	"garlic",	"G AA1 R L IH0 K .", 
	"mint",		"M IH1 N T .", 
	"nutmeg",	"N AH1 T M EH2 G .", 
	"ginseng",	"JH IH1 N S EH2 NG .", 
	"fennel",	"F EH1 N AH0 L .", 
	"sage",		"S EY1 JH .", 
	"hazelnut",	"HH EY1 Z AH0 L N AH2 T .", 
	"squash",	"S K W AA1 SH .", 
	"ambrosia",	"AE0 M B R OW1 ZH AH0 .", 
	"licorice",	"L IH1 K ER0 IH0 SH .",
	"kiwi",		"K IY1 W IY0 .", 
	"yew",		"Y UW1 .", 
	"horseradish",	"HH AO1 R S R AE2 D IH0 SH .", 
	"uh-oh",	"AH1 . OW1 .", 
	"butter",	"B AH1 T ER0 .",
	"thistle",	"TH IH1 S AH0 L .",
	
	// from https://en.wikipedia.org/wiki/Arpabet
	"off",		"AO1 F .",
	"fall",		"F AO1 L .",
	"frost",	"F R AO1 S T .",
	
	"father",	"F AA1 DH ER .",
	"cot",		"K AA1 T .",
	
	"bee",		"B IY1 .",
	"she",		"SH IY1 .",
	
	"you",		"Y UW1 .",
	"new",		"N UW1 .",
	"food",		"F UW1 D .",
	
	"red",		"R EH1 D .",
	"men",		"M EH1 N .",
	
	"big",		"B IH1 G .",
	"win",		"W IH1 N .",
	
	"should",	"SH UH1 D .",
	"could",	"K UH1 D .",
	
	"but",		"B AH1 T .",
	"sun",		"S AH1 N .",
	
	"sofa",		"S OW1 F AH0 .",
	"alone",	"AH0 L OW1 N .",
	
	"discus",	"D IH1 S K AX0 S .",
	"discuss",	"D IH0 S K AH1 S .",
	
	"at",		"AE1 T .",
	"fast",		"F AE1 S T .",
	
	"say",		"S EY1 .",
	"eight",	"EY1 T .",
	
	"my",		"M AY1 .",
	"why",		"W AY1 .",
	"ride",		"R AY1 D .",
	
	"show",		"SH OW1 .",
	"coat",		"K OW1 T .",
	
	"how",		"HH AW1 .",
	"now",		"N AW1 .",
	
	"boy",		"B OY1 .",
	"toy",		"T OY1 .",
	
	"her",		"HH ER0 .",
	"bird",		"B ER1 D .",
	"hurt",		"HH ER1 T .",
	"nurse",	"N ER1 S .",
	
	"father",	"F AA1 DH ER .",
	"coward",	"K AW1 ER D .",
	
	"air",		"EH1 R .",
	"where",	"W EH1 R .",
	"hair",		"HH EH1 R .",
	
	"cure",		"K Y UH1 R .",
	"bureau",	"B Y UH1 R OW0 .",
	"deter",	"D IH0 T UH1 R .",
	
	"more",		"M AO1 R .",
	"bored",	"B AO1 R D .",
	"chord",	"K AO1 R D .",
	
	"large",	"L AA1 R JH .",
	"hard",		"HH AA1 R D .",
	
	"ear",		"IY1 R .",
	"near",		"N IH1 R .",
	
	// AW R - This seems to be a rarely used r-controlled vowel. 
	// In some dialects flower (F L AW1 R; 
	// in other dialects F L AW1 ER0)
	"flower",	"F L AW1 R .",
	"flower",	"F L AW1 ER0 .",
	
	"pay",		"P EY1 .",
	"buy",		"B AY1 .",
	"take",		"T EY1 K .",
	"day",		"D EY1 .",
	"key",		"K IY1 .",
	"go",		"G OW1 .",
	
	"chair",	"CH EH1 R .",
	
	"just",		"JH AH1 S T .",
	"gym",		"JH IH1 M .",
	
	"for",		"F AO1 R .",
	
	"very",		"V EH1 R IY0 .",
	
	"thanks",	"TH AE1 NG K S .",
	"Thursday",	"TH ER1 Z D EY2 .",
	
	"that",		"DH AE1 T .",
	"the",		"DH AH0 .",
	"them",		"DH EH1 M .",
	
	"say",		"S EY1 .",
	
	"zoo",		"Z UW1 .",
	
	"show",		"SH OW1 .",
	
	"measure",	"M EH1 ZH ER0 .",
	"pleasure",	"P L EH1 ZH ER .",
	
	"house",	"HH AW1 S .",
	
	"man",		"M AE1 N .",
	"keep 'em",	"K IY1 P EM .",
	"no",		"N OW1 .",
	"button",	"B AH1 T EN .",
	"sing",		"S IH1 NG .",
	"Washington",	"W AO1 SH ENG T EN .",
	
	"late",		"L EY1 T .",
	"bottle",	"B AO1 DX EL .",
	"run",		"R AH1 N .",
	"wetter",	"W EH1 DX AXR .",
	"wintergreen",	"W IY2 NX AXR G R IY1 N .",
	
	"yes",		"Y EH1 S .",
	"way",		"W EY1 .",
    };
    
    // removing numeric stress value 
    if (!includeStresses) {
      for (int i = 1; i < tests.length; i+=2) {
	tests[i] = tests[i].replaceAll("[\\d]", "");
	// System.out.println(tests[i]);
      }
    }
    
    for (int i = 0; i < tests.length; i += 2) {
      equal(Phoneme.arpaToIPA(tests[i]), tests[i+1]);
    }
  }

}
