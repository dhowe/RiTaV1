[![Build Status](https://travis-ci.org/dhowe/RiTaJS.svg?branch=master)](https://travis-ci.org/dhowe/RiTaJS)


## RiTa: a generative language toolkit for JavaScript

<a href="http://rednoise.org/rita"><img height=80 src="http://rednoise.org/rita/img/RiTa-logo3.png"/></a>

### [The RiTa website](http://rednoise.org/rita)

RiTa is designed to be an easy-to-use toolkit for experiments in natural language and generative literature. It is implemented in Java and JavaScript (with a common API for both). It is free/libre and open-source via the GPL license.

About the project
--------
* Author:   [Daniel C. Howe](http://rednoise.org/daniel)
* License: 			 GPL (see included [LICENSE](https://github.com/dhowe/RiTaJS/blob/master/LICENSE) file for full license)
* Web Site:          [https://rednoise.org/rita](http://rednoise.org/rita)
* Github Repo:       [https://github.com/dhowe/RiTa](https://github.com/dhowe/RiTa)
* Bug Tracker:       [https://github.com/dhowe/RiTa/issues](https://github.com/dhowe/RiTa/issues)
* Reference:    [https://rednoise.org/rita/reference](http://rednoise.org/rita/reference)

In [node.js](http://nodejs.org/)
--------
To install, do the following from a terminal prompt

`$ npm install rita`

then use require() from your code

```javascript
var rita = require('rita');
var rs = rita.RiString("The elephant took a bite!");
console.log(rs.features());
```

To run tests, do the following from a terminal prompt:

```bash
$ cd node_modules/rita && npm install && npm test
```

Or, see the 'Development Setup' instructions below...

#### Can I contribute?
--------
Please! We are looking for more coders to help out... Just press *Fork* at the top of this github page and get started, or follow the instructions below...

#### Development Setup
--------
- Download and install [git](https://www.git-scm.com/), [npm](https://www.npmjs.org/), and [gulp](). If you have them already, move on to step #2.

  a. You can find a version of __git__ for your OS [here](https://www.git-scm.com/)  
  b. The easiest way to install __npm__ is to install [node.js](http://nodejs.org/)  
  c. You can install __gulp__ via npm as follows:

  ```bash
    $ npm install -g gulp
    ```
- [Fork and clone](https://help.github.com/articles/fork-a-repo) this library.

    a. First, login to github and fork the project  
    b. Then, from a terminal/shell:

  ```bash
  $ git clone https://github.com/dhowe/RiTa.git
  ```

- Now navigate into the project folder and install dependencies via npm.
  ```bash
  $ cd js && npm install
  ```

- To create the library from src, use gulp.
  ```bash
  $ gulp build
  ```

- Optionally run tests in node with gulp.
  ```bash
  $ gulp test
  ```

- Work on an existing [issue](https://github.com/dhowe/RiTa/issues?q=is%3Aopen+is%3Aissue+label%3ARiTaJS), then [submit a pull request...](https://help.github.com/articles/creating-a-pull-request)
