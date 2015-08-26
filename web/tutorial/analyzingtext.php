<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

<head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8" />
    <title>Tutorial-Analyzing Text</title>
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
    <h4><a href="index.php"><span>Tutorial ></span></a>Analyzing Text</h4>
    <ul class="sublist">
        <li><a href="#Phonemes">Phonemes</a></li>
        <li><a href="#Stresses">Stresses</a></li>
        <li><a href="#Tokens">Tokens</a></li>
        <li><a href="#POS">POS(Part of speech)</a></li>
    </ul>

    <div id="Phonemes" class="pad-small">
      <h5 class="sub">Phonemes</h5>
      <p>A phoneme is a single "unit" of sound that has meaning in any language, each one representing a different sound a person can make. Since there are only 26 letters in the alphabet, sometimes letter combinations need to be used to make a phoneme. A letter can also represent different phonemes. Here is a good example:
      </p>
       <div class="example">
       <p>
        chef = /ʃef/ <br />
        choir = /kwaɪə/<br />
        cheese = /tʃi:z/
        </p></div>
 <p>The "ch" letter combination has three different pronunciations, which are represented by three different phonemes: /ʃ/, /k/ and /tʃ/.
 </p>
 <p>In RiTa, we use Arpabet to represent the phonemes. Arpabet is a phonetic transcription code developed by Advanced Research Projects Agency (ARPA) as a part of the Speech Understanding Project. It represents each phoneme of General American English with a distinct sequence of ASCII characters. 
<br />
The above example would be translated like this if we use Arpabet:</p>
<div class="example">
   <p>
chef = ch eh f<br />
choir = k w ay r<br />
cheese = ch iy z
</p></div>



<p>You can use <a href="../reference/RiTa/RiTa.getPhonemes/index.php"><b>RiTa.getPhonemes</b></a> to analyse the phonemes.</p>


 <pre><code class="language-javascript">RiTa.getPhonemes("An elephant is a mammal");</code></pre>

<p>It will returen: 'ae-n eh-l-ax-f-ax-n-t ih-z ey m-ae-m-ax-l'.</p>
<div class="ref"><p>The phoneme tags used in RiTa: <br />
<a href="http://www.rednoise.org/rita/reference/PhonemeTags.html">http://www.rednoise.org/rita/reference/PhonemeTags.html</a></p>
<p>Reference list from Arpabet to IPA: <br />
<a href="https://en.wikipedia.org/wiki/Arpabet/">https://en.wikipedia.org/wiki/Arpabet/</a><p>
</div>
      
    </div><hr />

    <div id="Stresses" class="pad-small">
      <h5 class="sub">Stresses</h5>
     <p>In phonetics, the degree of emphasis given a sound or syllable in speech, resulting in relative loudness. Also called lexical stress or word stress.
</p> 

<p> <a href="../reference/RiTa/RiTa.getStresses/index.php"><b>RiTa.getStresses</b></a> analyzes the input and returns a new string containing the stress for each syllable of the input text.
</p> 

 <pre><code class="language-javascript"> RiTa.getStresses("computer") </code></pre>


<p> It should return 0/1/0. Here <b>1 </b>means 'stressed', and <b>0 </b>means ‘unstressed’.</p> 
    </div><hr />

    <div id="Tokens" class="pad-small">
      <h5 class="sub">Tokens</h5>
      <p>Tokens are the units for text tokenization. Tokens can be words or numbers or punctuation marks. <br />
See <a href="transformation.php#Tokenizing/Untokenizing"><b>Tokenizing.</b></a></p>
    </div>  <hr />

    <div id="POS" class="pad-small">
      <h5 class="sub">POS (Part of speech)</h5>
    <p>A part of speech is a category of words which have similar grammatical properties. 
Words that are assigned to the same part of speech play similar roles within the grammatical structure of sentences. Commonly used English parts of speech are noun, verb, adjective, adverb, pronoun, preposition, conjunction, interjection, and sometimes numeral, article or determiner.
</p>

   <p>You can use <a href="https://rednoise.org/rita/reference/RiTa/RiTa.getPosTags/index.html"><b>RiTa.getPosTags</b></a> to let RiTa analyse the Pos for you. The default Pos tagger uses tags from the PENN tag set. <br />
<a href="http://www.rednoise.org/rita/reference/PennTags.html">http://www.rednoise.org/rita/reference/PennTags.html </a>  </p>

<p>The PENN tag set is very detailed. If you only need the very basic Pos categories, you can switch to WordNet style tags, which covers the following categories:
</p>
<div style="line-height:1.618em!important;">
   <p>
n NOUN <br />

v VERB<br />

a ADJECTIVE<br />

s ADJECTIVE SATELLITE<br />

r ADVERB<br />
</p></div>
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
