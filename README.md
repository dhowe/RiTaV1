[![Build Status](https://travis-ci.org/dhowe/RiTa.svg?branch=master)](https://travis-ci.org/dhowe/RiTa) <a href="http://www.gnu.org/licenses/gpl-3.0.en.html"><img src="https://img.shields.io/badge/license-GPL-orange.svg" alt="npm version"></a> <a href="https://www.npmjs.com/package/rita"><img src="https://img.shields.io/npm/v/rita.svg" alt="npm version"></a>

### RiTa: the generative language toolkit

<a href="https://rednoise.org/rita/"><img height=80 src="https://rednoise.org/rita/img/RiTa-logo3.png"/></a>

#### <a href="https://rednoise.org/rita">The RiTa website</a>

RiTa is designed to be an easy-to-use toolkit for experiments in natural language and generative literature. It is implemented in Java and JavaScript (with a unified API for both) and optionally integrates with Processing, Android, Node, p5.js, Browserify, Bower, etc, It is free/libre and open-source via the GPL license (http://www.gnu.org/licenses/gpl.txt).

Please see https://github.com/dhowe/RiTaJS for the JavaScript implementation of RiTa.  

#### About the project
--------
* Author:         [Daniel C. Howe](https://rednoise.org/~dhowe)
* Related:			  [RiTaJS](https://github.com/dhowe/RiTaJS)
* License:			  GPL (see included [LICENSE](https://github.com/dhowe/RiTa/blob/master/LICENSE) file)
* Web Site:       https://rednoise.org/rita
* Reference:      https://rednoise.org/rita/reference
* Github Repo:    https://github.com/dhowe/RiTa/
* Issues:    https://github.com/dhowe/RiTa/issues

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

Create a simple test sketch as follows (and/or see the included examples):
```
import rita.*;

println(RiTa.tokenize("The elephant took a bite!"));
```

#### In Processing (Android-mode)
--------
1. Follow these [instructions](https://github.com/processing/processing-android/wiki#Instructions) to setup your environment
2. To add RiTa to your Processing project, select  'Menu' > 'Tools' > 'Import Library' > 'Manage Libraries'
3. Find RiTa inside the 'Library Manager', via 'Menu' > 'Get Libraries' or simply  download ['RiTa.zip'](http://rednoise.org/rita/rita.zip) then select 'Install Compressed Library' to manually install the zip file.

Create a simple test sketch as follows:
```
import rita.*;

println(RiTa.tokenize("The elephant took a bite!"));

```

<!--
#### With Maven
--------
##### Setting up Rita for Maven in Eclipse from GitHub
1. Install [Eclipse IDE for Java Developers](https://eclipse.org/downloads/) 4.3 or newer
2. In Eclipse, select File > Import... > Projects from Git > Clone URI > https://github.com/dhowe/RiTa.git (or the address of your fork)
3. Right-click: RiTa project > Configure > Convert to Maven Project
-->

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

#### Development Setup (in Eclipse)
--------

1. Download and install [Eclipse for Java.](https://www.eclipse.org/downloads/)

2. In the Eclipse menu, select 'File' > 'Import...'

3. In the 'Import Window' select 'Git' > 'Projects from Git', then press Next.

3. Select 'Clone URI' > then Next and copy and paste the 'HTTPS clone URL'     [https://github.com/dhowe/RiTa.git](https://github.com/dhowe/RiTa.git)  from RiTa's Github page into the URI field.

4. Press Next to proceed with the default master branch or (optionally) configure the project directory.

5. Press Next and select 'Import existing projects' to finish.

6. To run the tests:

   a. Navigate to the RiTa/resources directory and right-click on 'build.xml'

   b. Select 'Run as' > 'Ant Build' to compile and run the tests in JUnit.

7. To build the project:

   a. In the Eclipse menu, select 'Window' > 'Show View' -> 'Ant

   b. Click the '+' button to add a buildfile, and navigate to RiTa/resources/build.xml

   c. Click to expand the 'RiTa' menu and reveal the various tasks, then double-click the menu title to run ant build or select "_quick.build" (requires [RiTaJS](https://github.com/dhowe/RiTaJS) in rita/js)

   d. When the build is complete, project resources can be found in RiTa/dist

8. Work on an existing [issue](https://github.com/dhowe/RiTa/issues?q=is%3Aopen+is%3Aissue), then [submit a pull request...](https://help.github.com/articles/creating-a-pull-request)
