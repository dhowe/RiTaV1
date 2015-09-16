[![Build Status](https://travis-ci.org/dhowe/RiTaJS.svg?branch=master)](https://travis-ci.org/dhowe/RiTaJS)

### RiTa.js: a generative language toolkit for JavaScript


<a href="https://rednoise.org/rita"><img height=80 src="https://rednoise.org/rita/img/RiTa-logo3.png"/></a>

#### [The RiTa website](http://rednoise.org/rita)

RiTa.js is designed to an easy-to-use toolkit for experiments in natural language and generative literature, based on the [original RiTa](http://rednoise.org/rita) library for Java. RiTa.js works alone or in conjunction with p5.js, processing.js, node.js, bower, or browserify.  All RiTa and RiTa.js tools are free/libre/open-source according to the [GPL](http://www.gnu.org/licenses/gpl.txt).


#### About the project
--------
* Author:           [Daniel C. Howe](https://rednoise.org/daniel)
* Web Site:         https://rednoise.org/rita
* Reference:        http://www.rednoise.org/rita/reference/
* License:          GPL (see included [LICENSE](https://github.com/dhowe/RiTaJS/blob/master/LICENSE) file)
* Github Repo:      https://github.com/dhowe/RiTaJS/
* Bug Tracker:      https://github.com/dhowe/RiTa/issues
* Related:          [RiTa](https://github.com/dhowe/RiTa) (Java)


&nbsp;


#### A simple sketch
--------
Create a new file on your desktop called 'first.html' and download the latest rita.js from [here](http://rednoise.org/rita/download/rita-latest.micro.js), add the following lines, save and drag it into a browser:

```html
<html>
  <script src="https://code.jquery.com/jquery-1.11.2.min.js"></script>
  <script src="./rita-latest.micro.js"></script>
  <script>
    window.onload = function() {
      $('#content').text(RiTa.tokenize("The elephant took a bite!"));
    };
  </script>
  <div id="content" width=200 height=200></div>
<html>
```

#### With [node.js](http://nodejs.org/) and [npm](https://www.npmjs.com/)
--------
To install: `$ npm install rita`

```javascript
var rita = require('rita');
var rs = rita.RiString("The elephant took a bite!");
console.log(rs.features());
```

#### With [p5.js](http://p5js.org/)
--------
Create a new file on your desktop called 'first.html' and download the latest rita.js from [here](http://rednoise.org/rita/download/rita-latest.micro.js), add the following lines, save and drag it into a browser:

```html
<html>
  <script src="https://cdnjs.cloudflare.com/ajax/libs/p5.js/0.4.3/p5.min.js"></script>
  <script src="./rita-latest.micro.js"></script>
  <script>
  function setup() {

    createCanvas(200,200);
    background(50);
    textSize(20);
    noStroke();

    var words = RiTa.tokenize("The elephant took a bite!")
    for (var i=0, j = words.length; i<j; i++) {
        text(words[i], 50, 50 + i*20);
    }
  }
  </script>
</html>
```


#### With [browserify](http://browserify.org/) and [npm](https://www.npmjs.com/)
--------
Install [browserify](https://www.npmjs.com/package/browserify) (if you haven't already)
```
$ sudo npm install -g browserify
```
Create a file called 'main.js' with the following code:
```java
require('rita');

var rs = RiString("The elephant took a bite!");
console.log(rs.features());
```
Now install RiTa
```
$ npm install rita
```
Now use browserify to pack all the required modules into bundle.js
```
$ browserify main.js -o bundle.js
```
Create an html file with a single script tag as below, then open it in a web browser and check the output in the 'Web Console'
```html
<script src="bundle.js"></script>
```

#### With [bower](http://bower.io/)
--------

To install, choose a directory and do:

```bash
$ bower install rita
```

Now, create a file called 'test.html', add the following lines, save, and open in a browser:

```html
<html>
  <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/2.1.3/jquery.min.js"></script>
  <script src="./bower_components/rita/dist/rita.min.js"></script>
  <script>
    window.onload = function() {
      $('#content').text(RiTa.tokenize("The elephant took a bite!"));
    };
  </script>
  <div id="content" width=200 height=200></div>
<html>
```


#### With [processing.js](http://processingjs.org)
--------
Create a new file on your desktop called 'first.html' and download the latest rita.js from [here](http://rednoise.org/rita/download/rita-latest.micro.js), add the following lines, save and drag it into a browser:

```html
<html>
  <script src="https://cdnjs.cloudflare.com/ajax/libs/processing.js/1.4.8/processing.min.js"></script>
  <script src="./rita-latest.micro.js"></script>
  <script type="text/processing" data-processing-target="processing-canvas">
    void setup() {

      size(200,200);
      background(50);
      textSize(20);
      noStroke();

      String words = RiTa.tokenize("The elephant took a bite!");
      for (int i=0, j = words.length; i<j; i++) {
          text(words[i], 50, 50 + i*20);
      }
    }
  </script>
  <canvas id="processing-canvas"> </canvas>
</html>
```


#### Can I contribute?
--------
Please! We are looking for more coders to help out... Just press *Fork* at the top of this github page and get started, or follow the instructions below...


#### Development Setup
--------
1. Download and install [git](https://www.git-scm.com/), [npm](https://www.npmjs.org/), and [gulp](). If you have them already, move on to step #2.

  a. You can find a version of __git__ for your OS [here](https://www.git-scm.com/)  
  b. The easiest way to install __npm__ is to install [node.js](http://nodejs.org/)  
  c. You can install __gulp__ via npm as follows:

  ```bash
    $ npm install -g gulp
    ```
2. [Fork and clone](https://help.github.com/articles/fork-a-repo) this library.

  a. First, login to github and fork the project  
  b. Then, from a terminal/shell:

    ```bash
    $ git clone https://github.com/dhowe/RiTaJS.git
    ```
3. Now navigate into the project folder and install dependencies via npm.

  ```bash
  $ cd RiTaJS && npm install
  ```
4. To create the library from src, use gulp.

  ```bash
  $ gulp build
  ```
5. Run all tests (in phantomJS) with gulp.

  ```bash
  $ gulp test
  ```
6. Work on an existing [issue](https://github.com/dhowe/RiTa/issues?q=is%3Aopen+is%3Aissue+label%3ARiTaJS), then [submit a pull request...](https://help.github.com/articles/creating-a-pull-request)
