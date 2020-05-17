<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

<head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8" />
    <title>Tutorial-Concordance and Key-Word-In-Context</title>
    <meta name="generator" content="JsDoc" />
    <link rel="stylesheet" href="../css/mainStyle.css">
    <link rel="stylesheet" href="../css/tutorial.css">

</head>

<body>


<div class="gd-section tutorial pad-top-large">
<div class="gd-center pad-large">
<div class="row">
  <div class="col2"></div>
  <div class="col8">

     <h4><a href="index.php"><span>Tutorial ></span></a>Concordance and Key-Word-In-Context</h4>
 <p>In publishing, a concordance is an alphabetical list of the words used in a book or body of work, listing each instance of each word with its immediate surrounding context<a href="https://en.wikipedia.org/wiki/Concordance_%28publishing%29"><sup>1</sup></a>. In the precomputing era, when search was unavailable, a concordance offered the reader an easy way to search for words they might want to look up.</p>

<p>In programming, when we mention a concordance, we usually refer to an object that stores the words, and  corresponding counts, for a given text. For example, if my text is</p>

</p>
<div class="example">
   <p>
Summer is hot. It is hot today.</p></div>

<p>the corresponding concordance would be:</p>
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

<p>In RiTa, we can generate the above concordance with the <a href="../reference/RiTa/RiTa.concordance/index.php"><b>RiTa.concordance()</b></a> function.</p>
<pre><code class="language-javascript">  var args = {
    ignoreCase: true
  };
  var c = RiTa.concordance("Summer is hot. It is hot today.", args);
</code></pre>

<p>Here is <a href="https://shiffman.github.io/A2Z-F16/week5-analysis/02_pos_concordance/">an example</a> for you to test how a concordance would look like with your own text.
</p>
<br />
<h5>Key Word In Context</h5>
<p>A common format for a concordance is <b>Key-Word-In-Context</b> or <b>KWIC</b> model.  It sorts and aligns the words in a way that allows each to be searchable in the index. The search result for the word 'window' in an example KWIC model looks like this:
</p>
<img src="../img/kwic.png" alt="" />

<br />&nbsp;</br>

<p>In RiTa, we can achieve this with the <a href="../reference/RiTa/RiTa.kwic/index.php"><b>RiTa.kwic()</b></a> function, as follows:</p>
<pre><code class="language-javascript">  var args = {
    ignoreCase: false,
    ignorePunctuation: true,
    wordCount: 6
  };
  var lines = RiTa.kwic(text, keyword, args);
</code></pre>

<br />

<p>Here is an interactive <a href="../examples/p5js/KWICmodel/index.html">example</a> of a KWIC model in RiTa.</p>

<hr/>

<div id="Next" class="pad-small">
  <p><a href="index.php">TUTORIAL > </a></p>
</div>

</div>




</div>
<div class="col2"></div>
</div>
</div>
</div>


<!--
End Site Content
-->

</body>
</html>
