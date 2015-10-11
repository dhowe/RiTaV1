<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

<head>
  <meta http-equiv="content-type" content="text/html; charset=utf-8" />
  <title>Tutorial-Using RiGrammar</title>
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
            <h4>
              <a href="index.php">
                <span>Tutorial ></span>
              </a>Generating with RiGrammar</h4>
            <p><a href="../reference/RiGrammar.php"><b>RiGrammar</b></a> is a probabilistic context-free grammar with literary extensions for text-generation. It enables us to generate texts according to rules that we define.
            </p>
            <p>RiTa grammars are valid <a href="https://en.wikipedia.org/wiki/JSON" target="new">JSON</a> or <a href="https://en.wikipedia.org/wiki/YAML" target="new">YAML</a> formatted strings (or objects).</p>
            <p>For example, if we want to generate simple sentences following a Subject-Verb-Object pattern, the RiGrammar might look like this:
            </p>

            <pre><code class="language-yaml">

  &lt;start&gt;: &lt;subject&gt; &lt;verb&gt; &lt;object&gt;
  &lt;subject&gt;: I | You | They
  &lt;verb&gt;: want | hate | like | love
  &lt;object&gt;: coffee | bread | milk
</code></pre>


            <p>One generation from this grammar would be “I hate milk”.<br>Another would be “They want bread”.</p>
            <p>The first line defines the whole structure of your grammar.
              In this case, it will generate a <b>&lt;subject&gt;</b>, followed by a <b>&lt;verb&gt;</b> and then an<b> &lt;object&gt;</b>.
              For each part, RiTa looks into the grammar for the corresponding rule
              and randomly chooses one string from the options listed.
            </p>
            <p>You can also use native YAML format as follows:</p>
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
  &lt;verb&gt;:
  - want
  - hate
  - like
  - love
  &lt;object&gt;:
  - coffee
  - bread
  - milk
</code></pre>
            <p>To run the grammar, you will need <a href="../reference/RiGrammar/expandFrom/index.php"><em>expandFrom()</em></a> or <a href="../reference/RiGrammar/expand/index.php"><em>expand()</em></a>... <br/></br>
              The former, <a href="../reference/RiGrammar/expandFrom/index.php">expandFrom()</a>, expands the grammar from the given symbol, while  <a href="../reference/RiGrammar/expand/index.php">expand()</a> starts from the &lt;start&gt; symbol (and is simply a convenient version of expandFrom('&lt;start&gt;');).

            </p>

            <pre><code class="language-javascript">
  RiGrammar.expandFrom('&lt;start&gt;');</code></pre>


            <p><br>A RiGrammar object will assign (by default) equal weights to all choices in a rule.
              One can adjust the weights by adding 'multipliers' as follows:
            </p>

<pre><code class="language-yaml">

  &lt;object&gt;: coffee[2] | bread | milk
</code></pre>

            <p>Then the chance of getting the word “coffee” will be twice as often as either “bread” or “milk”.</p>
            <p>We can also make the grammar more flexible by adding rules within rules:</p>

<pre><code class="language-yaml">

  &lt;start&gt;: &lt;subject&gt;, &lt;verb&gt;, &lt;object&gt;
  &lt;subject&gt;: I | You | They
  &lt;verb&gt;: want | hate | like | love
  &lt;object&gt;: &lt;food&gt; | &lt;animals&gt;
  &lt;food&gt;: coffee | bread | milk
  &lt;animals&gt;: cats |dogs | fish
</code></pre>


            <p>Now you might get “You love coffee” or “They want fish”.</p><br>

            <p>To get a better sense of how it all works, check out <a href="../examples/p5js/HaikuGrammar/index.html">this example sketch</a> using RiTa grammars...</p>

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
