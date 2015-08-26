<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

<head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8" />
    <title>Tutorial-Transformation</title>
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
    <h4><a href="index.php"><span>Tutorial ></span></a>Transformation</h4>
    <ul class="sublist">
            <li><a href="#Conjugation">Conjugation</a></li>
            <li><a href="#Stemming">Stemming</a></li>
            <li><a href="#Plurals/Singulars">Plurals/Singulars</a></li>
            <li><a href="#Splitting-sentences">Splitting-sentences</a></li>
            <li><a href="#Tokenizing/Untokenizing">Tokenizing/Untokenizing</a></li>
    </ul>

    <div id="Conjugation " class="pad-small">
      <h5 class="sub">Conjugation</h5>
      <p>Conjugation refers to how a verb changes to show a different person, tense, number or mood.
      The term conjugation is applied only to the inflection of verbs, and not of other parts of speech (inflection of nouns and adjectives is known as declension).
      </p>
      <p>For instance, the verb ‘to be’ is a particularly notable verb for conjugation because it’s so irregular.</p>
      <div class="example">
      <p>
      I am <br />
      You are<br />
      He/she/it/one is<br />
      We are<br />
      You are<br />
      They are</p></div>

<p>We can also conjugate for different tenses (past, present, future).</p>
<div class="example">
<p>I was, I am, I will be<br />
You were, you are, you will be<br />
He was, he is, he will be<br />
We were, we are, we will be<br />
They were, they are, they will be</p></div>

<br />
<p>You can use <a href="../reference/RiTa/RiTa.conjugate/index.php"><b>RiTa.conjugate</b> </a>to conjugate a verb to the particular form you want by specifying the tense, number and person.
</p>

<div class="example">
<p><b>TENSE:</b> PAST_TENSE,PRESENT_TENSE,FUTRE_TENSE;<br />
<b>NUMBER:</b> SINGULAR,PLURAL;<br />
<b>PERSON:</b> FIRST_PERSON,SECOND_PERSON,THIRD_PERSON;</p>
</div>

<pre><code class="language-javascript">

 var args = {
        tense: RiTa.PAST_TENSE,
        number: RiTa.SINGULAR,
        person: RiTa.FIRST_PERSON
 }
 $('#content').text(RiTa.conjugate("make",args));

</code></pre>

<p>The outcome of this example will be "maked".</p>
<div id="content" width=200 height=200></div>

    </div><hr />

    <div id="Stemming" class="pad-small">
      <h5 class="sub">Stemming</h5>
      <p>Stemming means reducing a word to its base (or stem). For example, the words 'writing', 'wrote' and 'written' all have the stem 'write'. A stemmer takes a word, or a list of words, and produces the stem, or a list of the stems, of the input.
      </p>
      <p>Stemming is useful when you are doing any kind of text-analysis: when you are concerned about the contents of a text, the different times of verbs, and the different endings for singular and plural, make it difficult to discern the importance of specific words within the text.
      </p>
      <p>If we take the following text as an example </p>
       <div class="example">
       <p>I wrote a book about cats, after I had written a short article on cats. Currently I'm writing about dogs. Next year I'll write poems. But I made my best writings when i was younger.
      </p></div>
      <p>It's obvious (to a human) that this text is mainly concerned with writing. But when you run a program to analyse it, the program will come to the conclusion that it's a text about cats, because the word 'cats' is the only word (aside from words like 'I', 'a', etc.) that occurs more than once.
      </p>
      <p>But if you stem the text before analysing it, replacing all words with their stems, the program will correctly tell you that it's a text about writing, since after stemming, the word 'write' will appear five times ter(because 'writing', 'written', 'writings' and 'wrote' have been replaced by 'write').
      </p>

      <h5><a href="../reference/RiTa/RiTa.stem/index.php">RiTa.stem</a></h5>
      <p>In RiTa, there are three different stemming algorithm for you to choose. 
      </p>
      <p>
      <b>RiTa.LANCASTER</b> (the default), <b>RiTa.PORTER</b>, or <b>RiTa.PLING</b>
      </p>
      <p>
      You can change to other algorithm like this:</p>
      <pre><code class="language-javascript">

RiTa.stem("wrote","Porter")
      </code></pre>
      <div class="ref">
     <p> Note: see <a href="http://text-processing.com/demo/stem/">http://text-processing.com/demo/stem/</a> for comparison of Lancaster and Porter algorithms or <a href="http://mpii.de/yago-naga/javatools">http://mpii.de/yago-naga/javatools</a> for info on PlingStemmer
</p></div>
      
    </div><hr />
    <div id="Plurals/Singulars" class="pad-small">
      <h5 class="sub">Plurals/Singulars</h5>
      <p><a href="../reference/RiTa/RiTa.pluralize/index.phpreference/RiTa/RiTa.pluralize/index.php"><b>RiTa.pluralize</b></a> is a simple pluralizer for nouns according to pluralisation rules. It uses a combination of letter-based rules and a lookup table of irregular exceptions. 
       <div class="example"><p> e.g., 'appendix' -> ‘appendices’</p> </div>
      </p>

      <p><a href="../reference/RiTa/RiTa.singularize/index.php"><b>RiTa.Singularize</b></a> will take a plural form of noun and return the stem, which is the singular form of the noun.</p>
      <div class="example"><p> e.g., 'apples' -> ‘apple’</p> </div>

    </div>
    <hr />
    <div id="Splitting-sentences" class="pad-small">
      <h5 class="sub">Splitting-sentences</h5>
     <p>Splitting Sentences means dividing a span of text into sentences.</p>
     <p>A question mark or exclamation mark always ends a sentence.  A period followed by an upper-case letter generally ends a sentence, but there are a number of exceptions.  For example, if the period is part of an abbreviated title ("Mr.", "Gen.", ...), it does not end a sentence.  A period following a single capitalized letter is assumed to be a person's initial, and is not considered the end of a sentence.
     </p>
 <p><a href="../reference/RiTa/RiTa.splitSentences/index.php"><b>RiTa.splitSentences</b></a> splits 'text' into sentences according to PENN Treebank conventions.
<br />
    
    <span class="ref">See: <a href=" http://www.cis.upenn.edu/~treebank/tokenization.html">http://www.cis.upenn.edu/~treebank/tokenization.html</a>
   </span> </p></div>  <hr />

    <div id="Tokenizing/Untokenizing" class="pad-small">
      <h5 class="sub">Tokenizing/Untokenizing</h5>
      <h5>Tokenizing</h5>

<p>Tokenizing is the task of chopping a text up into smaller pieces, called tokens. In RiTa, such tokens are words. There are different tokenising conventions out there. The one RiTa has used is the Penn Treebank conventions. 
<br /><span class="ref">See: <a href="http://www.cis.upenn.edu/~treebank/tokenization.html">http://www.cis.upenn.edu/~treebank/tokenization.html
</a></span></p>

<p>An example of tokenising in RiTa is like this:</p>
<div class="example"><p>
Input: “I want to have a cup of coffee.”<br />
Output: {“I”,“want”,“to”,“have”,“a”,“cup”,“of”,“coffee”,”.”}</p> </div>

<p>The default <a href="../reference/RiTa/RiTa.tokenize/index.php"><b>RiTa.tokenize</b></a> will just split the line into words. You can also choose to use a RegexTokenizer:</p>
 <pre><code class="language-javascript">
  RiTa.tokenize(words, regex)</code></pre>
<p>
To go in the other direction, from an array of words and punctuation to a sentence, you can use <a href="../reference/RiTa/RiTa.untokenize/index.php"><b>RiTa.untokenize()</b></a>;</p>

<h5>Untokenizing</h5>

<p>The reverse process of tokenizing: putting the individual tokens (in our case, words) back into a sequence.</p>

<p><a href="../reference/RiTa/RiTa.untokenize/index.php"><b>RiTa.untokenize</b></a> takes an array of word and joins them together and attempts to preserve punctuation position. It will adjust the spacing accordingly if a token is punctuation.
</p>

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
