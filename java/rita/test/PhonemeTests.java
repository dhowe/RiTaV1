package rita.test;

import static rita.support.QUnitStubs.equal;

import org.junit.Test;

import rita.RiTa;
import rita.support.Constants;
import rita.support.Phoneme;

public class PhonemeTests implements Constants
{
  @Test
  public void testArpaToIPA()
  {
    String[] tests = {
	"b ih k ah1 m", "bɪˈkʌm",
	
	// from http://web.stanford.edu/class/linguist238/fig04.01.pdf
	"p aa1 r s l iy0", "ˈpɑrsli",
	"k ae1 t n ih0 p", "ˈkætnɪp",
	"b ey1", "beɪ",
	"d ih1 l", "dɪl",
	"g aa1 r l ih0 k", "ˈgɑrlɪk",
	"m ih1 n t", "mɪnt",
	"n ah1 t m eh2 g", "ˈnʌtˌmɛg",
	"jh ih1 n s eh2 ng", "ˈʤɪnˌsɛŋ",
	"f eh1 n ah0 l", "ˈfɛnəl",
	"s ey1 jh", "seɪʤ",
	"hh ey1 z ah0 l n ah2 t", "ˈheɪzəlˌnʌt",
	"s k w aa1 sh", "skwɑʃ",
	"ae0 m b r ow1 zh ah0", "æmˈbroʊʒə",
	"l ih1 k er0 ih0 sh", "ˈlɪkərɪʃ",
	"k iy1 w iy0", "ˈkiwi",
	"y uw1", "ju",
	"hh ao1 r s r ae2 d ih0 sh", "ˈhɔrsˌrædɪʃ",
	"ah1 ow1", "ʌ oʊ",
	"b ah1 t er0", "ˈbʌtər",
	"th ih1 s ah0 l", "ˈθɪsəl",
	
	// from https://en.wikipedia.org/wiki/arpabet
	"ao1 f", "ɔf",
	"f ao1 l", "fɔl",
	"f r ao1 s t", "frɔst",
	
	"f aa1 dh er", "ˈfɑðər",
	"k aa1 t", "kɑt",
	
	"b iy1", "bi",
	"sh iy1", "ʃi",
	
	"y uw1", "ju",
	"n uw1", "nu",
	"f uw1 d", "fud",
	
	"r eh1 d", "rɛd",
	"m eh1 n", "mɛn",
	
	"b ih1 g", "bɪg",
	"w ih1 n", "wɪn",
	
	"sh uh1 d", "ʃʊd",
	"k uh1 d", "kʊd",
	
	"b ah1 t", "bʌt",
	"s ah1 n", "sʌn",
	
	"s ow1 f ah0", "ˈsoʊfə",
	"ah0 l ow1 n", "əˈloʊn",
	
	"d ih1 s k ax0 s", "ˈdɪskəs",
	"d ih0 s k ah1 s", "dɪˈskʌs",
	
	"ae1 t", "æt",
	"f ae1 s t", "fæst",
	
	"s ey1", "seɪ",
	"ey1 t", "eɪt",
	
	"m ay1", "maɪ",
	"w ay1", "waɪ",
	"r ay1 d", "raɪd",
	
	"sh ow1", "ʃoʊ",
	"k ow1 t", "koʊt",
	
	"hh aw1", "haʊ",
	"n aw1", "naʊ",
	
	"b oy1", "bɔɪ",
	"t oy1", "tɔɪ",
	
	"hh er0", "hɜr",
	"b er1 d", "bɜrd",
	"hh er1 t", "hɜrt",
	"n er1 s", "nɜrs",
	
	"k aw1 er d", "ˈkaʊərd",
	
	"eh1 r", "ɛr",
	"w eh1 r", "wɛr",
	"hh eh1 r", "hɛr",
	
	"k y uh1 r", "kjʊr",
	"b y uh1 r ow0", "ˈbjʊroʊ",
	"d ih0 t uh1 r", "dɪˈtɜr",
	
	"m ao1 r", "mɔr",
	"b ao1 r d", "bɔrd",
	"k ao1 r d", "kɔrd",
	
	"l aa1 r jh", "lɑrʤ",
	"hh aa1 r d", "hɑrd",
	
	"iy1 r", "ir",
	"n ih1 r", "nɪr",
	
	"f l aw1 r", "ˈflaʊər",
	
	"p ey1", "peɪ",
	"b ay1", "baɪ",
	"t ey1 k", "teɪk",
	"d ey1", "deɪ",
	"k iy1", "ki",
	"g ow1", "goʊ",
	
	"ch eh1 r", "ʧɛr",
	
	"jh ah1 s t", "ʤʌst",
	"jh ih1 m", "ʤɪm",
	
	"f ao1 r", "fɔr",
	
	"v eh1 r iy0", "ˈvɛri",
	
	"th ae1 ng k s", "θæŋks",
	"th er1 z d ey2", "ˈθɜrzˌdeɪ", // or "ˈθɜrzdi"
	
	"dh ae1 t", "ðæt",
	"dh ah0", "ði",
	"dh eh1 m", "ðɛm",
	
	"s ey1", "seɪ",
	
	"z uw1", "zu",
	
	"sh ow1", "ʃoʊ",
	
	"m eh1 zh er0", "ˈmɛʒər",
	"p l eh1 zh er", "ˈplɛʒər",
	
	"hh aw1 s", "haʊs", // or "haʊz"
	
	"m ae1 n", "mən",
	"k iy1 p em", "kip ɛm",
	"n ow1", "noʊ",
	"b ah1 t en", "ˈbʌtən",
	"s ih1 ng", "sɪŋ",
	"w ao1 sh eng t en", "ˈwɑʃɪŋtən", // or "ˈwɔʃɪŋtən"
	
	"l ey1 t", "leɪt",
	"b ao1 dx el", "ˈbɑtəl",
	"r ah1 n", "rʌn",
	"w eh1 dx axr", "ˈwɛtər",
	"w iy2 nx axr g r iy1 n", "ˈwɪntər grin",
	
	"y eh1 s", "jɛs",
	"w ey1", "weɪ",	
    };
	
    for (int i = 0; i < tests.length; i += 2) {
      
      // test with stresses
      String ipa = Phoneme.arpaToIPA(tests[i]);
      System.out.println("expected "+tests[i+1]+", got "+ipa);
      equal(tests[i+1], ipa);
      
      // test without stresses
      tests[i] = tests[i].replaceAll("[\\d]", "");
      tests[i+1] = tests[i+1].replaceAll(Phoneme.IPA_STRESS, "");
      ipa = Phoneme.arpaToIPA(tests[i]);
      System.out.println("expected "+tests[i+1]+", got "+ipa);
      equal(tests[i+1], ipa);
    }
  }

}
