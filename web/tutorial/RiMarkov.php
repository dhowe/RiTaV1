<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

<head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8" />
    <title>Tutorial-Using RiMarkov</title>
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

     <h4><a href="index.php"><span>Tutorial ></span></a>Using RiMarkov</h4>
 <h5 class="sub">Markov Chain</h5>
 <p>A Markov chain (also called an “n-gram” chain) is a system of states and transitions. An analogy might be a set of cities connected by highways, where each city is a “state”, and each highway is a “transition”, a way of getting from one city to another.
 </p>
<img style="width:60%;height:auto;" src="../img/tutorial/citiesandhighways.png" alt="" />
 </p>
 <p>Since we are concerned here with text, we can think of each “state” as a bit of text; a letter, a word, or a phrase (usually drawn inside a circle). A transition is a way of moving from one node to another (usually drawn as an arrow). Here’s a simple example:
 </p>
<img src="../img/tutorial/markov1.png" alt="" />
 </p>
 <p>In the image above we see a simple Markov chain for the sentence: “The girl created a game after school .” We start at the beginning, “The”, and follow the arrows to get to the final node, “.” In this very simple Markov chain, each word leads to exactly one subsequent word, so there are no choices we need to make. But consider the following chain:
 </p>
<img src="../img/tutorial/markov2.png" alt="" />
 </p>
 <p>Again, we start from the left, at “The”, but after we get to “girl”, we have two choices about where to go next. Depending on which transition we take, we will end up with one of two different sentences.
Here the probability of each sentence is 0.5.The same idea can be further extended to a sequence of letters,words or sentence.</p>

<p><a href="../reference/RiMarkov.php"><b>RiMarkov</b></a></p>
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
