[![Build Status](https://travis-ci.org/dhowe/RiTa.svg?branch=master)](https://travis-ci.org/dhowe/RiTa)

### RiTa: the generative language toolkit 

<a href="https://rednoise.org/rita/"><img height=120 src="https://rednoise.org/rita/img/RiTa-logo3.png"/></a>

#### <a href="https://rednoise.org/rita">The RiTa website</a>

RiTa is designed to be an easy-to-use toolkit for experiments in natural language and generative literature. It is implemented in Java and JavaScript (with a unified API for both) and optionally integrates with Processing(JS), Android, NodeJS, and Bower. It is free/libre and open-source according to the GPL license (http://www.gnu.org/licenses/gpl.txt). 

Please see https://github.com/dhowe/RiTaJS for the JavaScript implementation of RiTa.  

#### About the project
--------
* Original Author:  Daniel C. Howe (https://rednoise.org/~dhowe)
* Related:			RiTaJS -> https://github.com/dhowe/RiTaJS
* License:			GPL (see included LICENSE file for full license)
* Maintainers:      See included AUTHORS file for contributor list
* Web Site:         https://rednoise.org/rita
* Documentation:    https://rednoise.org/rita/reference
* Examples:         https://rednoise.org/rita/examples/
* Github Repo:      https://github.com/dhowe/RiTa/
* Bug Tracker:      https://github.com/dhowe/RiTa/issues

#### Can I contribute?
--------
Please! We are looking for more coders to help out... Just press *Fork* at the top of this page and get started, or follow the instructions below... 

If you don't feel like coding but still want to contribute, please send a twitter message to @RiTaSoftware.


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
   
   c. Click to expand the 'RiTa' menu and reveal the various tasks, then double-click 'build'
   
   d. When the build is complete, project resources can be found in RiTa/dist 

8. Work on an existing [issue](https://github.com/dhowe/RiTa/issues?q=is%3Aopen+is%3Aissue), then [submit a pull request...](https://help.github.com/articles/creating-a-pull-request)

 
 
