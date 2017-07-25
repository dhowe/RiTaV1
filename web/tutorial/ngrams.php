<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

<head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8" />
    <title>Tutorial: n-grams with RiTa</title>
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

     <h4><a href="index.php"><span>Tutorial ></span></a>Generating with RiMarkov</h4>
 <h5 class="sub">Markov Chains</h5>
 <p>Markov chains (also called “n-gram” models) are systems of states and transitions. An analogy might be a set of cities connected by highways, where each city is a “state”, and each highway is a “transition”, a way of getting from one city to another.
 </p>
<img style="width:60%;height:auto;" src="../img/tutorial/citiesandhighways.png" alt="" />
 </p>
 <p>Since we are concerned here with text, we can think of each “state” as a bit of text; a letter, a word, or a phrase (usually drawn inside a circle). A transition is a way of moving from one state to another (usually drawn as an arrow). Here’s a simple example:
 </p>
<img src="../img/tutorial/markov1.png" alt="" />
 </p>
 <p>In the image above we see a simple Markov chain for the sentence: “The girl created a game after school.” We can start at the first word, “The”, and follow the arrows to get to the final word, “.” In this very simple chain, each word leads to exactly one subsequent word, so there are no choices we need to make. </p><p>But consider the following example:
 </p>
<img src="../img/tutorial/markov2.png" alt="" />
 </p>
 <p>Again, we start from the left, at “The”, but after we get to “girl”, we have two choices about where to go next. Depending on which transition we take, we will end up with one of two different sentences.
Here the probability of choosing either sentence is 50% or 0.5.
The same idea can be further extended to a sequence of syllables, letters, words or sentences.</p>
<p>
Now let’s change the sentences to make it more interesting:</p>
<div class="example">The girl went to a game after dinner. <br /> The teacher went to dinner with a girl.</div>

 <p><br>The word “went” can occur after either “girl” or “teacher”. The word (or token) that follows “girl” can be either “.” or “went”. This contiguous sequence of <em>n</em> elements is an <b>n-gram</b>. <br/><br/>The minimum value of <em>n</em> in a Markov chain is 2 (otherwise we wouldn't have created a chain). If we try to build a Markov model for the above two sentences with  <em>n</em>=2, the outcome would be something like this (with | representing OR):
</p>
<pre>
      The     —>  girl | teacher
      girl    —>  went | .
      went    —>  to
      to      —>  a | dinner
      a       —>  game | girl
      game    —>  after
      after   —>  dinner
      dinner  —>  . | with
      .       —>  The
      teacher —>  went
      with    —>  a</pre>

<p>Imagine this to be an action guide from which the computer will attempt to generate sentences. The computer begins with the word “The”. Then it checks the action guide for words that follow “The”, and finds two options: “girl” or “teacher”. It flips a coin and picks “girl”. Next it checks for words that follow “girl”, and finds two options: “went” or “.” &nbsp;And so on...
  </p><p>
With a longer text sample we could experiment with different <em>n</em>-values. The higher the <em>n</em>-value, the more similar the output will be to the input text. Often the easiest way to find the best value for <em>n</em> is simply to experiment...
</p>

 <p> Here is a very simple example of <a href="../examples/p5js/Kafgenstein/index.html">text-generation with RiTa</a>.
</p>

<br />
<h5><a href="../reference/RiMarkov.php"><b>RiMarkov</b></a></h5>
<p>Generating text with Markov Chain in RiTa requires three simple steps:
</p>
<p>First, construct a Markov-chain and set its n-factor (the # of elements to consider). Let’s start with n=4...
</p>
<pre><code class="language-javascript">  var rm = new RiMarkov(4);</code></pre>
<br>Second, provide some text for RiTa to analyse. There are three functions to achieve this: <em>loadFrom()</em>,  <em>loadText()</em> and  <em>loadTokens()</em>. Let's start with <em>loadText()</em>, which simply loads a string of text into the model.
</p>
<pre><code class="language-javascript">
  rm.loadText("The girl went to a game after dinner. The teacher went \
    to dinner with a girl.");
</code></pre>
<br>Third, generate an outcome according to the Markov model. You can either use <em>generateSentences()</em>, <em>generateTokens()</em> or <em>generateUntil()</em> to achieve different results. Here are all three lines together:
</p>
<pre><code class="language-javascript">  var rm = new RiMarkov(4);
  rm.loadText("The girl went to a game after dinner. The teacher went \
    to dinner with a girl.");
  var sentences = rm.generateSentences(2);
</code></pre>
<br>We can now run this code. One possible output would be: </p>
<div class="example">
  The teacher went to dinner. <br />The girl went to dinner with a game after dinner.
</div>
<p>
<br>
</p>
<p>To get a better sense of how it all works, check out <a href="../examples/p5js/Kafgenstein/index.html">this example sketch</a> using RiTa Markov chains...</p>


<hr/>

<div id="Next" class="pad-small">
  <p>NEXT > see <a href="concordance.php"><b>Concordance and Key-Word-In-Context</b></a></p>
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
