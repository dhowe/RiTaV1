### RiTa: the generative language toolkit

#### Using RiWordNet with Processing
--------
1. Download and extract [WordNet 3.1 Database File](http://wordnetcode.princeton.edu/wn3.1.dict.tar.gz)
2. Create and save a sketch named 'WordNetExample' on desktop
3. Create a folder named ```data``` under the sketch 'WordNetExample' then put the extracted database folder ```/dict``` to the data folder of the sketch on Windows or ```/Users/<username>/Desktop/WordNetExample/data``` on Mac
4. Run the sketch with the following code
```processing
import rita.*;

RiWordNet w = new RiWordNet("/dict"); // On Mac: RiWordNet w = new RiWordNet("/Users/<username>/Desktop/example/data");
String[] s = w.getAllSynsets("dog", "n");
println(s);
```

#### Using RiWordNet with Eclipse
--------
1. Create a new Java project in Eclipse (or your IDE of choice)
2. Download [rita-latest.jar](http://rednoise.org/rita/download/rita-latest.jar) and add it to the build path for the project. In eclipse: 'Project' > 'Properties' > 'Java Build Path' > 'Libraries' > 'Add External JARs...'
3. Download and extract [WordNet 3.1 Database File](http://wordnetcode.princeton.edu/wn3.1.dict.tar.gz) to your disk e.g. put it on ```c:/dict``` on Windows or ```/Users/<username>/Desktop/Dict``` on Mac
4. Create and run a new class, WordNetExample.java, with the following code:
```Java
import java.util.Arrays;
import rita.*;

public class WordNetExample {

  public static void main(String[] args) {

		RiWordNet w = new RiWordNet("c:/dict"); // on Mac: RiWordNet w = new RiWordNet("/Users/<username>/Desktop/Dict");
		String[] s = w.getAllSynsets("dog", "n");
		System.out.println(Arrays.asList(s));
  }
}
```

#### Using RiWordNet with Processing on Android (Android support coming soon...)
--------
1. Download and extract [WordNet 3.1 Database File](http://wordnetcode.princeton.edu/wn3.1.dict.tar.gz) on Android
2. Create and save the sketch named 'WordNetExample'
3. Put the extracted database folder ```/dict``` to storage/sdcard0/Sketchbook/RiWordNet/data
4. Run the sketch with the following code
```processing
import rita.*;

RiWordNet w = new RiWordNet("/dict");
String[] s = w.getAllSynsets("dog", "n");
println(s);
```
