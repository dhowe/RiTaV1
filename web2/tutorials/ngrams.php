<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

<head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8" />
    <title>Tutorial: n-grams with RiTa</title>
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

     <h4><a href="index.php"><span>Tutorial ></span></a>Generating with RiMarkov</h4>
 <h5 class="sub">Markov Chains</h5>

<p>Markov chains (also called “n-gram” models) are systems of states and transitions. An analogy might be a set of cities connected by highways, where each city is a “state”, and each highway is a “transition”, a way of getting from one city to another.
</p>
<img style="width:60%;height:auto;" src="../img/tutorial/citiesandhighways.png" alt="" />
</p>
<p>Since we are concerned here with text, we can think of each “state” as a bit of text; a letter, a word, or a phrase (usually drawn inside a circle). A “transition“ is a way of moving from one state to another (usually drawn as an arrow). Here’s a simple example:
</p>
<img src="../img/tutorial/markov1.png" alt="" />
</p>
<p>In the image above we see a simple Markov chain for the sentence: “The girl created a game after school.” We can start at the first word, “The”, and follow the arrows to get to the final word, “.” In this very simple chain, each word leads to exactly one subsequent word, so there are no choices we need to make.</p><p>But consider the following example (with 2 sentences):
</p>
<img src="../img/tutorial/markov2.png" alt="" />
</p>
<p>Again, we start from the left, at “The”, but after we get to “girl”, we have two choices about where to go next. Depending on which transition we take, we will end up with one of two different sentences. Here the probability of choosing either sentence is 50% or 0.5.
The same idea can be further extended to a sequence of syllables, letters, words or sentences.</p>
<p>
Now let’s change the sentences to make things slightly more interesting:</p>
<div class="example">The girl went to a game after dinner. <br /> The teacher went to dinner with a girl.</div>

<p><br>The word “went” can occur after either “girl” or “teacher”. The word (or token) that follows “girl” can be either “.” or “went”.

<p>If we consider all the word pairs for the above two sentences (<em>n</em>=2), the outcome would look something like this (you can think of the &rarr; symbol as 'can be followed by' and the &vert; symbol as OR).
</p>

<pre style="font-size: 16px; font-family: consolas; background-color: #f5f5f5">
      The      &rarr;    girl | teacher
      girl     &rarr;    went | .
      went     &rarr;    to
      to       &rarr;    a | dinner
      a        &rarr;    game | girl
      game     &rarr;    after
      after    &rarr;    dinner
      dinner   &rarr;    . | with
      .        &rarr;    The
      teacher  &rarr;    went
      with     &rarr;    a</pre>

<p>This way of looking at contiguous sequences of text, with a specific size (in our case, two words at a time), is known as an <b>n-gram</b> or <b>Markov chain</b>. And this idea can be extended to create models from sequences of arbitrary length, whether syllables, letters, words or sentences.<br></p>

<h5><br><b>Generating with n-grams</b></h5>

<p>Generally n-grams are used as a means of <em>analysing</em> language, but here we are interested in <em>generating</em> new text. To do this, we can think of the table (or model) above as an action guide that the computer can follow to create new sentences. For example, lets say the computer begins with the word “The”. Then it checks the guide for words that can be followed by “The”, and finds two options: “girl” and “teacher”. It flips a coin and picks “girl”. Then it checks for words that follow “girl”, and finds two options: “went” or “.” &nbsp;And so on...
</p>

<p>
With a longer text sample we could experiment with different <em>n</em>-values, looking at, say... 3 or 4 words at a time. The higher the <em>n</em>-value, the more similar the output will be to the input text (and the more processing/memory required). Often the easiest way to find the best value for <em>n</em> is just to experiment...
</p>

<p><br>Here is a very simple example of <a href="../examples/p5js/Kafgenstein/index.html">Markov-based generation with the RiTa library</a>.
</p>

<p><br><em>Aside: for the computer-science-oriented, we can think of these models as Finite State Machines, or more generally as (labelled) Directed Graphs. A common way of implementing them is as a tree, where each node contains a word, its count, and a list of children, one for each word that follows it.</em></p>


<br />
<h5><a href="../reference/RiMarkov.php"><b>RiMarkov</b></a></h5>
<p>To generate text with a Markov chain in RiTa requires three steps:
</p>
<p>First, we construct a model and set its n-factor (the # of elements to consider). Let’s start with n=4...
</p>
<pre><code class="language-javascript">  var rm = new RiMarkov(4);</code></pre>

<br>Second, we provide some text for RiTa to analyse. There are three functions to achieve this: <em>loadFrom()</em>,  <em>loadText()</em> and  <em>loadTokens()</em>. Let's start with <em>loadText()</em>, which simply loads a string of text into the model.
</p>
<pre><code class="language-javascript">
  rm.loadText("The girl went to a game after dinner. The teacher went \
    to dinner with a girl.");
</code></pre>
<br>Third, generate an outcome according to the Markov model. You can either use <em>generateSentences()</em>, <em>generateTokens()</em> or <em>generateUntil()</em> to achieve different types of results. Here are all three steps together:
</p>
<pre><code class="language-javascript">  var rm = new RiMarkov(4);
  rm.loadText("The girl went to a game after dinner. The teacher went \
    to dinner with a girl.");
  var sentences = rm.generateSentences(2);
</code></pre>
<br>If we were to run this code, we would get an output like:</p>
<div class="example">
  The teacher went to dinner. <br />The girl went to dinner with a game after dinner.
</div>
<p>
<br>
</p>
<p>To get a better sense of how it all works, check out <a href="../examples/p5js/Kafgenstein/index.html">this example sketch</a> which uses RiTa and p5.js to load and blend two different texts</p>


<hr/>

<div id="Next" class="pad-small">
<p>NEXT > see <a href="concordance.php"><b>Concordance and Key-Word-In-Context</b></a></p>
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
