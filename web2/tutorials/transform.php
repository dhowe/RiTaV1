<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

<head>
  <meta http-equiv="content-type" content="text/html; charset=utf-8" />
  <title>Tutorial-Transformation</title>
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
            <h4>
              <a href="index.php">
                <span>Tutorial ></span>
              </a>Transformation</h4>
            <ul class="sublist">
              <li><a href="#Conjugation">Conjugation</a></li>
              <li><a href="#Stemming">Stemming</a></li>
              <li><a href="#Plurals/Singulars">Plurals/Singulars</a></li>
              <li><a href="#Splitting-sentences">Splitting-sentences</a></li>
              <li><a href="#Tokenizing/Untokenizing">Tokenizing/Untokenizing</a></li>
            </ul>

            <div id="Conjugation" class="pad-small">
              <h5 class="sub">Conjugation</h5>
              <p>Conjugation refers to how a verb changes to show a different person, tense, number or mood.
                The term conjugation is applied only to the inflection of verbs, and not of other parts of speech
                 (inflection of nouns and adjectives is known as
                declension).
              </p>
              <p>For instance, the verb ‘to be’ is a notable verb for conjugation because
                 it’s so irregular.</p>
              <div class="example">
                <p>
                  I <em>am</em>
                  <br/> You <em>are</em>
                  <br/> He/she/it/one <em>is</em>
                  <br/> We <em>are</em>
                  <br/> You <em>are</em>
                  <br/> They <em>are</em></p>
              </div>

              <p>We can also conjugate for different tenses (past, present, future).</p>
              <div class="example">
                <p>I was, I am, I will be
                  <br/> You were, you are, you will be
                  <br/> He was, he is, he will be
                  <br/> We were, we are, we will be
                  <br/> They were, they are, they will be</p>
              </div>

              <br/>
              <p>You can use <a href="../reference/RiTa/RiTa.conjugate/index.php"><b>RiTa.conjugate</b> </a>to conjugate a verb for the particular form you want by specifying the <em>tense</em>, <em>number</em> and <em>person</em>.
              </p>

              <div class="example">
                <p><b>TENSE: &nbsp;</b> PAST_TENSE, PRESENT_TENSE, FUTURE_TENSE
                  <br/><br/>
                  <b>NUMBER: &nbsp;</b> SINGULAR, PLURAL
                  <br/><br/>
                  <b>PERSON: &nbsp;</b> FIRST_PERSON, SECOND_PERSON, THIRD_PERSON</p>
              </div>
            </br>
An example, in JavaScript:
              <pre><code class="language-javascript">
  var args = {
    tense: RiTa.PAST_TENSE,
    number: RiTa.SINGULAR,
    person: RiTa.THIRD_PERSON
  };
  var result = RiTa.conjugate("swim", args);</code></pre>

              <p>The outcome of this example will be "swam".</p>
              <div id="content" width=200 height=200></div>

            </div>
            <hr />

            <div id="Stemming" class="pad-small">
              <h5 class="sub">Stemming</h5>
              <p>Stemming means reducing a word to its base, or stem. For example, the words 'writing', 'wrote' and 'written' all have the stem 'write'. A stemmer takes a word or list of words as input and return the stem(s).
              </p>
              <p>Stemming is useful when you are doing any kind of text-analysis. This is because different verb conjugations, different endings for singular and plural, different adjective forms, etc. can all make it difficult to discern the importance of specific words in a text.
              </p>
              <p>Let's take the following paragraph as an example: </p>
              <div class="example">
                <p><em>I wrote a book about cats, after I had written a short article on cats. Currently I'm writing about dogs. Next year I'll write poems. But I produced my best writings when I was younger.</em>
                </p>
              </div>
              <p>It's obvious (to a human) that this text is mainly concerned with <em>writing</em>. But when you run a program to analyze it, the program will come to the conclusion that it's about <em>cats</em>, because the word 'cats' is the only word (aside from
                words like 'I', 'a', etc.) that occurs more than once.
              </p>
              <p>But if you stem the text before analyzing it, replacing all words with their stems, the program will correctly tell you that it's a text about writing. This because, after stemming, the word 'write' will appear five times (because 'writing',
                'written', 'writings' and 'wrote' have been replaced by 'write').
              </p>

              <h5><a href="../reference/RiTa/RiTa.stem/index.php">RiTa.stem</a></h5>


              <pre><code class="language-javascript">RiTa.stem("write written writing");</code></pre>
              <p>The result would be: writ writ writ.</p>

                            <p>In RiTa, there are three different stemming algorithm for you to choose from.
              </p>
              <p>
                <b>RiTa.LANCASTER</b> (the default), <b>RiTa.PORTER</b>, or <b>RiTa.PLING</b>
              </p>
              <div class="ref">
                <p> Note: see <a href="http://text-processing.com/demo/stem/">http://text-processing.com/demo/stem/</a> for comparison of Lancaster and Porter algorithms or <a href="http://mpii.de/yago-naga/javatools">http://mpii.de/yago-naga/javatools</a> for info on the PlingStemmer
                </p>
              </div>
            </div>
            <hr />
            <div id="Plurals/Singulars" class="pad-small">
              <h5 class="sub">Plurals/Singulars</h5>
              <p><a href="../reference/RiTa/RiTa.pluralize/index.php"><b>RiTa.pluralize</b></a> is a simple pluralizer for nouns according to pluralisation rules. It uses a combination of letter-based rules and a lookup
                table of irregular exceptions.
                <pre><code class="language-javascript">RiTa.pluralize("apple")</code></pre>
                <div class="example">
                  <p> 'apple' -> 'apples'</br>
                  'child' -> 'children'</br>
                  'appendix' -> 'appendices'</p>
                </div>
              </p>
              <br/>
              <p><a href="../reference/RiTa/RiTa.singularize/index.php"><b>RiTa.singularize</b></a> does the reverse, taking the plural form of noun and returning the singular.</p>
              <pre><code class="language-javascript">RiTa.singularize("apples");</code></pre>
              <div class="example">

                <p> 'apples' -> 'apple'</br>
                'children' -> 'child'</br>
                'appendices' -> 'appendix'</p>
              </div>

            </div>
            <hr />
            <div id="Splitting-sentences" class="pad-small">
              <h5 class="sub">Splitting-sentences</h5>
              <p>Sentence-splitting means dividing a span of text into sentences.</p>
              <p>A question mark or exclamation mark always ends a sentence. A period followed by an upper-case letter generally ends a sentence, but there are a number of exceptions. For example, if the period is part of an abbreviated title ("Mr.", "Gen.",
                ...), it does not end a sentence. A period following a single capitalized letter is assumed to be a person's initial, and is not considered the end of a sentence.
              </p>
              <p><a href="../reference/RiTa/RiTa.splitSentences/index.php"><b>RiTa.splitSentences</b></a> splits 'text' into sentences according to the PENN Treebank conventions.
                <pre><code class="language-javascript">RiTa.splitSentences("'What's happened to me?' he thought. It wasn't a dream. \
  His room, a proper human room although a little too small, lay peacefully \
  between its four familiar walls.");</code></pre>
              <p>This will return an array of three sentences:</p>
              <div class="example">
               [0] 'What's happened to me?' he thought. <br />
               [1] It wasn't a dream. <br />
               [2] His room, a proper human room although a little too small, lay peacefully between its four familiar walls.


              </div>
              <br/>

                <span class="ref">More about <a href="ftp://ftp.cis.upenn.edu/pub/treebank/public_html/tokenization.html">PENN Treebank tokenization</a>
                </span>
              </p>
            </div>
            <hr />

            <div id="Tokenizing/Untokenizing" class="pad-small">
              <h5 class="sub">Tokenizing/Untokenizing</h5>
              <h5>Tokenizing</h5>

              <p>Tokenizing is the task of chopping a text up into smaller pieces called tokens. In RiTa such tokens are usually words (and punctuation characters). There are different tokenizing conventions, but the one RiTa uses is called the Penn Treebank convention.
              </p>
              <p>
                <span class="ref">More about <a href="ftp://ftp.cis.upenn.edu/pub/treebank/public_html/tokenization.html">PENN Treebank tokenization</a>
                </span>
              </p>

              <p>An example of tokenizing in RiTa looks like this:</p>
              <pre><code class="language-javascript">  RiTa.tokenize("I want to have a cup of coffee.");</code></pre>
                <p>The output will be: [ 'I', 'want', 'to', 'have', 'a', 'cup', 'of', 'coffee', '.' ]</p>


              <p>The default <a href="../reference/RiTa/RiTa.tokenize/index.php"><b>RiTa.tokenize</b></a> function will
                 split a line of text into words and punctuation. You can also choose to use a RegexTokenizer (with a regular expression pattern of your choice):</p>
              <pre><code class="language-javascript">  RiTa.tokenize(words, regex);</code></pre>
              <p>
                To go in the other direction, from an array of words and punctuation to a sentence, you can use <a href="../reference/RiTa/RiTa.untokenize/index.php"><b>RiTa.untokenize</b></a>.</p>
</br>
              <h5>Untokenizing</h5>

              <p>Untokenizing is simply the reverse process of tokenizing; putting the individual tokens (in our case, words) back into a sequence.</p>

              <p><a href="../reference/RiTa/RiTa.untokenize/index.php"><b>RiTa.untokenize</b></a> takes an array of word and punctuation and joins them together into a sentence, preserving punctuation position and adding spaces as necessary.
              </p>

              <p>An example of untokenizing in RiTa:</p>
              <pre><code class="language-javascript">  var words = ['I', 'want', 'to', 'have', 'a', 'cup', 'of', 'coffee', '.'];
  RiTa.untokenize(words);</code></pre>

                <p>
                  The output will be: "I want to have a cup of coffee.”

                  </p>

            </div>
            <hr/>

            <div id="Next" class="pad-small">
              <p>NEXT > see <a href="grammars.php"><b>Generating with grammars</b></a></p>
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
