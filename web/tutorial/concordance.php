<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

<head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8" />
    <title>Tutorial-Concordance and Key-Word-In-Context</title>
    <meta name="generator" content="JsDoc" />
    <link rel="stylesheet" href="../css/normalize.css">
  <link rel="stylesheet" href="../css/main.css">
  <link rel="stylesheet" href="../css/style.css" type="text/css" />
     <link rel="stylesheet" href="../css/prism.css">
  <script src="../js/vendor/modernizr-2.6.2.min.js"></script>
  <script language="javascript" src="../js/highlight.js"></script>
      <script language="javascript" src="../js/prism.js"></script>
</head>

<body>
<?php include("../header.php"); ?>

<div class="gd-section tutorial pad-top-large">
<div class="gd-center pad-large">
<div class="row">
  <div class="col2"></div>
  <div class="col8">

     <h4><a href="index.php"><span>Tutorial ></span></a>Concordance and Key-Word-In-Context</h4>
 <p>In publishing a concordance is an alphabetical list of the principal words used in a book or body of work, listing every instance of each word with its immediate context.
  [<a href="https://en.wikipedia.org/wiki/Concordance_%28publishing%29">source</a>] In the precomputing era, when search was unavailable, a concordance offered readers a easy way to search results for every word they might want to look up .</p> 

<p>In programming, when we mention concordance, it means  an object that stores all the words and the corresponding counts 
from a given text. For Example, if my text is :</p>

</p>
<div class="example">
   <p>
Summer is hot. It is hot today.</p></div>
<p>In RiTa we can get a concordance object like above using <a href="../reference/RiTa/RiTa.concordance/index.php"><b>RiTa.concordance</b></a>.</p>
<pre><code class="language-javascript">var args = {
  ignoreCase: true
};
var c = RiTa.concordance("Summer is hot. It is hot today.", args);
</code></pre>

<p>The corresponding Concordance object will be:</p>
<div class="example">
   <p>
    <table>
       <tr>
      <td>.</td>
      <td>2</td>
    </tr>
    <tr>
      <td>hot</td>
      <td>2</td>
    </tr>
    <tr>
      <td>is</td>
      <td>2</td>
    </tr>
    <tr>
      <td>it</td>
      <td>1</td>
    </tr>
    <tr>
      <td>summer</td>
      <td>1</td>
    </tr>
    <tr>
      <td>today</td>
      <td>1</td>
    </tr></table>
    </p>
</div>
<p>Here is <a href="http://shiffman.net/teaching/a2z/analysis/01_concordance/">an example</a> for you to test how a concordance would look like with your own text.
</p>
<br />
<h5>Key Word In Context</h5>
<p>A common format for concordance is <b>KWIC(Key Word In Context)</b>.  It sorts and aligns the words in a way allowing each word to be searchable alphabetically in the index. A search result of a certain keyword in KWIC format looks like this:
</p> 
<img src="../img/kwic.png" alt="" />
 <p>In RiTa, we can achieve this by <a href="../reference/RiTa/RiTa.kwic/index.php"><b>RiTa.kwic</b></a> like the following code shows:</p>
<pre><code class="language-javascript">var args = {
  ignoreCase: false,
  ignoreStopWords: true
};

var lines = RiTa.kwic(text,keyword, args);
</code></pre>

<p>Here is <a href="https://github.com/dhowe/RiTaJS/blob/master/examples/p5js/KWICmodel/index.html">an example of using RiTa.kwic</a></p>
</div>
</div>
<div class="col2"></div>
</div>
</div>
</div>


<?php include("../footer.php"); ?>
<!--
End Site Content
-->

</body>
</html>
