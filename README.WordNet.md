[![Build Status](https://travis-ci.org/dhowe/RiTa.svg?branch=master)](https://travis-ci.org/dhowe/RiTa)

### RiTa: the generative language toolkit

#### Using RiWordNet with Processing
--------
1. Download [WordNet 3.1](http://wordnetcode.princeton.edu/wn3.1.dict.tar.gz)
2. Unzip the downloaded file and place it in a convenient place, e.g., ```/Users/<username>/Desktop/dict``` on Mac/Linux, or ```C:\\WN\\dict``` on Windows
3. Create a new Processing sketch as below, specifying the location of WordNet on your system:
```processing
import rita.*;

RiWordNet w = new RiWordNet("/Users/<username>/Desktop/example/dict");
String[] s = w.getAllSynsets("dog", "n");
println(s);
```

#### Using RiWordNet with Eclipse
--------
1. Create a new Java project in Eclipse
2. Download [rita.jar](http://rednoise.org/rita/download/rita.jar) and add it to the build path for the project. In eclipse: 'Project' > 'Properties' > 'Java Build Path' > 'Libraries' > 'Add External JARs...'
1. Download and extract [WordNet 3.1](http://wordnetcode.princeton.edu/wn3.1.dict.tar.gz) to a convenient place on your system, e.g., ```/Users/<username>/Desktop/dict``` on Mac/Linux, or ```C:\\WN\\dict``` on Windows
4. Create and run a new class, WordNetExample.java, with the following code:
```Java
import rita.*;

public class WordNetExample {

  public static void main(String[] args) {

	RiWordNet w = new RiWordNet("/Users/<username>/Desktop/example/dict");
	String[] s = w.getAllSynsets("dog", "n");
	System.out.println(java.util.Arrays.asList(s));
  }
}
```

#### Using RiWordNet with Processing on Android (Android support coming soon...)
--------
```processing
import rita.*;

RiWordNet w = new RiWordNet("/Users/<username>/Desktop/example/dict");
String[] s = w.getAllSynsets("dog", "n");
println(s);
```
