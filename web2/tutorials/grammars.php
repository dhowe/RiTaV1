<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

<head>
  <meta http-equiv="content-type" content="text/html; charset=utf-8" />
  <title>Generating with grammars</title>
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
              </a>
              Generating with RiGrammar</h4><br />
            <p><a href="../reference/RiGrammar.php"><b>RiGrammar</b></a> is a probabilistic context-free grammar with literary extensions for text-generation. This is a rather formal definition, but for this tutorial, it just means that we can use it to generate texts according to simple rules that we define.
            </p>
            <p>For example, if we want to generate simple sentences following a Subject-Verb-Object pattern, the RiGrammar might look like this:
            </p>

            <pre><code class="language-yaml">

  &lt;start&gt;:    &lt;subject&gt; &lt;verb&gt; &lt;object&gt;
  &lt;subject&gt;:  I | You | They
  &lt;object&gt;:   coffee | bread | milk
  &lt;verb&gt;:     want | hate | like | love
</code></pre>

            <p>One generation from this grammar would be <em>I hate milk</em>. Another would be <em>They want bread</em>.</p>

            <p>The first line above defines the whole structure of your grammar.
              In this case, it will generate a <b>&lt;subject&gt;</b>, followed by a <b>&lt;verb&gt;</b> and then an<b> &lt;object&gt;</b>.
              For each part, RiTa looks into the grammar for the corresponding rule and then chooses one string from the options listed.
            </p>

            <p>The grammar above is written as JSON. RiTa accepts both <a href="https://en.wikipedia.org/wiki/JSON" target="new">JSON</a> and <a href="https://en.wikipedia.org/wiki/YAML" target="new">YAML</a> formatted strings (or objects) for grammars. Below is our example grammar using native YAML format:</p>
            <pre><code class="language-yaml">
---
  &lt;start&gt;:
      - &lt;subject&gt;
      - &lt;verb&gt;
      - &lt;object&gt;
  &lt;subject&gt;:
      - I
      - You
      - They
  &lt;object&gt;:
      - coffee
      - bread
      - milk
  &lt;verb&gt;:
      - want
      - hate
      - like
      - love
</code></pre>
            <p>To run the grammar, you can use either the <a href="../reference/RiGrammar/expand/index.php">expand()</a> or <a href="../reference/RiGrammar/expandFrom/index.php">expandFrom()</a> functions... </p>

              <p>The former, <a href="../reference/RiGrammar/expand/index.php">expand()</a> runs the grammar
                from the &lt;start&gt; symbol, while
                <a href="../reference/RiGrammar/expandFrom/index.php">expandFrom()</a>, starts from whatever rule you pass in -- actually <a href="../reference/RiGrammar/expand/index.php">expand()</a> is just a convenient version of expandFrom('&lt;start&gt;').
            </p>

            <pre><code class="language-javascript">
  riGrammar.expandFrom('&lt;start&gt;');</code></pre>

            <p>By default, a RiGrammar object will assign equal weights to all choices in a rule.
              However, one can adjust the weights by adding 'multipliers' as follows:
            </p>

<pre><code class="language-yaml">
  &lt;object&gt;: coffee[2] | bread | milk
</code></pre>

            <p>This says that the word “coffee” should be chosen, on average, twice as often as either “bread” or “milk”.</p>

            <p>We can also make the grammar more complex by adding rules within rules:</p>

<pre><code class="language-yaml">

  &lt;start&gt;:    &lt;subject&gt; &lt;verb&gt; &lt;object&gt;
  &lt;subject&gt;:  I | You | They
  &lt;object&gt;:   &lt;food&gt; | &lt;animals&gt;
  &lt;verb&gt;:     want | hate | like | love
  &lt;food&gt;:     coffee | bread | milk
  &lt;animals&gt;:  cats |dogs | fish
</code></pre>

            <p>Now you might get <em>You love coffee</em> or <em>They want fish</em> ...</p>

            <p>To get a better sense of how it all works, check out <a href="../examples/p5js/HaikuGrammar">this example sketch</a> using RiTa grammars...</p>

            <br />
            <p class="aside">Aside: its important to note that using rules related to English grammar (subject, verb, etc. as in our example above) is just one one of <em>many</em> ways to use grammars. One can construct rules based on any textual features (syllable-count, as in the <a href="../examples/p5js/HaikuGrammar">linked example</a>, or phonemes as we might in a rhyming grammar), or even use grammars to generate image or sound. </p>

            <hr/>

            <div id="Next" class="pad-small">
              <p>NEXT > see <a href="ngrams.php"><b>Generating with n-grams</b></a></p>
            </div>

          </div>

          <div class="col2"></div>
        </div>
      </div>
    </div>



</body>
</html>
