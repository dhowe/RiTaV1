[![Build Status](https://travis-ci.org/dhowe/RiTa.svg?branch=master)](https://travis-ci.org/dhowe/RiTa) <a href="http://www.gnu.org/licenses/gpl-3.0.en.html"><img src="https://img.shields.io/badge/license-GPL-orange.svg" alt="gpl license"></a> <a href="https://www.npmjs.com/package/rita"><img src="https://img.shields.io/npm/v/rita.svg" alt="npm version"></a>[![Gitter](https://badges.gitter.im/dhowe/rita.svg)](https://gitter.im/dhowe/rita)&nbsp;


### RiTa: the generative language toolkit

<a href="https://rednoise.org/rita/"><img height=80 src="https://rednoise.org/rita/img/RiTa-logo3.png"/></a>

#### <a href="https://rednoise.org/rita">The RiTa website</a>

RiTa is designed to be an easy-to-use toolkit for experiments in natural language and generative literature. It is implemented in Java and JavaScript (with a unified API for both) and optionally integrates with Processing, Android, Node, p5.js, Browserify, Bower, etc, It is free/libre and open-source via the GPL license (http://www.gnu.org/licenses/gpl.txt).

Please see https://github.com/dhowe/RiTaJS for the JavaScript implementation of RiTa.  

#### About the project
--------
* Author:         [Daniel C. Howe](https://rednoise.org/~dhowe)
* License:			  GPLv3 (see included [LICENSE](https://github.com/dhowe/RiTa/blob/master/LICENSE) file)
* Web Site:       https://rednoise.org/rita
* Reference:      https://rednoise.org/rita/reference
* Github Repo:    https://github.com/dhowe/RiTa/
* Issues:    https://github.com/dhowe/RiTa/issues
* FAQ:    https://github.com/dhowe/RiTa/wiki
* Related:			  [RiTaJS](https://github.com/dhowe/RiTaJS)

&nbsp;


#### A Simple Example (Java)
--------

1. Create a new Java project in Eclipse (or your IDE of choice)
2. Download [rita.jar](http://rednoise.org/rita/download/rita.jar) and add it to the build path for the project. In eclipse: 'Project' > 'Properties' > 'Java Build Path' > 'Libraries' > 'Add External JARs...'
3. Create and run a new class, SimpleExample.java, with the following code:
```Java
import rita.*;

public class SimpleExample {

  public static void main(String[] args) {

    RiString rs = new RiString("The elephant took a bite!");
    System.out.println(rs.features());
  }
}
```

#### In Processing
--------
To install:

1. Open Processing and select 'Sketch' menu > 'Import Library...' > 'Add Library...'
2. Search for 'RiTa' and then install it

Create an example sketch as follows (and/or see the included examples):
```java
import rita.*;
import java.util.*;

void setup() {

  size(600, 200);
  background(50);
  textSize(20);
  noStroke();

  RiString rs = new RiString("The elephant took a bite!");
  Map data = rs.features();

  float y = 15;
  for (Iterator it = data.keySet().iterator(); it.hasNext();) {
    String key = (String) it.next();
    text(key + ": " + data.get(key), 25, y += 26);
  }
}
```

#### In Processing (Android-mode)
--------
1. If RiTa library is not installed, open Processing and select 'Sketch' menu > 'Import Library...' > 'Add Library...'
2. Search for 'RiTa' and then install it
3. Follow these [instructions](https://github.com/processing/processing-android/wiki#android-mode) to setup your environment, OR follow steps 4-6 below
4. Switch to Android mode in Processing 3.x on PC or Mac by clicking the 'Java' button on the upper-right of the UI, then select 'Add Mode...'
5. Select 'Android Mode' from 'Contribution Manager' window and then install it and the required Android SDK when prompted
6. Restart Processing and input the example sketch below
7. Switch to 'Android' mode by clicking the 'Java' button
8. Connect your Android device to your PC via a USB cable
9. On your Android device, go to ‘Settings’ - ‘About phone’ and tap on ‘Build number’ for five times to become a developer
10.Go back to ‘Settings’, go to ‘Developer options’ and switch on ‘USB debugging'
11. Run the example sketch

An example sketch:
```java
import rita.*;
import java.util.*;

void setup() {

  size(600, 200);
  background(50);
  textSize(20);
  noStroke();

  RiString rs = new RiString("The elephant took a bite!");
  Map data = rs.features();

  float y = 15;
  for (Iterator it = data.keySet().iterator(); it.hasNext();) {
    String key = (String) it.next();
    text(key + ": " + data.get(key), 25, y += 26);
  }
}
```

<!--
#### With Maven
--------
##### Setting up Rita for Maven in Eclipse from GitHub
1. Install [Eclipse IDE for Java Developers](https://eclipse.org/downloads/) 4.3 or newer
2. In Eclipse, select File > Import... > Projects from Git > Clone URI > https://github.com/dhowe/RiTa.git (or the address of your fork)
3. Right-click: RiTa project > Configure > Convert to Maven Project
-->

<br/>

#### Can I contribute?
--------
Please! We are looking for more coders to help out... Just press *Fork* at the top of this page and get started, or follow the instructions below...

If you don't feel like coding but still want to contribute, please send a twitter message to @RiTaSoftware.

<!--
#### Development Setup (in Eclipse Maven)
--------

1. in Eclipse > Package Explorer, right click on pom.xml from the project
2. select > 'Run As' > '5 Maven Install'
-->


<br/>

#### Development Setup (in Eclipse)
--------

1. Download and install [Eclipse for Java.](https://www.eclipse.org/downloads/)

2. In the Eclipse menu, select 'File' > 'Import...'

3. In the 'Import Window' select 'Git' > 'Projects from Git', then press Next.

4. Select 'Clone URI' > then Next and copy and paste the 'HTTPS clone URL'     [https://github.com/dhowe/RiTa.git](https://github.com/dhowe/RiTa.git)  from RiTa's Github page into the URI field.

5. Press Next to proceed with the default master branch or (optionally) configure the project directory.

6. Press Next and select 'Import existing projects' to finish.

7. Right click on 'pom.xml' from RiTa root directory in Package Explorer panel in Eclipse and select 'Run as' > 'Maven install'.

8. To run the tests:

    a. Navigate to the RiTa/resources directory and right-click on 'build.xml'

    b. Select 'Run as' > 'Ant Build' to compile and run the tests in JUnit.

9. To build the project:

    a. In the Eclipse menu, select 'Window' > 'Show View' -> 'Ant

    b. Click the 'Add buildfile' button to add a buildfile in the newly added Ant panel, and navigate to RiTa/resources/build.xml

    c. Click to expand the 'RiTa' menu and reveal the various tasks, then double-click 'build' (or run ```$ cd RiTa/resources && ant build``` from the terminal)

    d. (Optional) if you are on Windows, you can use [cygwin](http://cygwin.com/install.html), by installing [ant](http://dita-ot.sourceforge.net/doc/ot-userguide13/xhtml/installing/windows_installingant.html) and using it to run the command ```$ cd RiTa/resources && ant build```.

    e. When the build is complete, project resources can be found in RiTa/dist

10. Work on an existing [issue](https://github.com/dhowe/RiTa/issues?q=is%3Aopen+is%3Aissue), then [submit a pull request...](https://help.github.com/articles/creating-a-pull-request)

11. (Optional) If you encounter error ```java.lang.UnsupportedClassVersionError: org/apache/maven/cli/MavenCli : Unsupported major.minor version```, follow [these instructions](http://crunchify.com/how-to-install-maven-on-mac-os-x-manually-fix-unsupportedclassversionerror-orgapachemavenclimavencli/) to install Maven manually.
