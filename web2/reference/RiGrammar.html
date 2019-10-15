<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.o../xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
	<head>
		<meta http-equiv="content-type" content="text/html; charset=utf-8" />
		<title>RiGrammar</title>
<link rel="stylesheet" href="../css/normalize.css">
  <link rel="stylesheet" href="../css/main.css">
    <link rel="stylesheet" href="../css/style.css" type="text/css" />
    <link rel="stylesheet" href="../css/bootstrap.css" type="text/css" />
  <script src="../js/vendor/modernizr-2.6.2.min.js"></script>
  <script language="javascript" src="../js/highlight.js"></script>
	</head>

	<body>

<?php include("../header.php"); ?>


<div class="gd-section pad-large">
<div class="gd-center pad-large">
<div class="row">

				<div class="refbar span3">
					<div id="index">
						<!-- begin publish.classesIndex -->
						<h3></h3>
						<ul>
							<br />
							<li style="top:60px;left:50px">
								<a href="index.php">Back to index</a>
							</li>
						</ul>
						<!-- end publish.classesIndex -->
					</div>
				</div>

		        	<div class="span11">

						<div class="reference row">

							<div id="content">

<!-- ============================== class title ============================ -->

								<h2 class="classTitle">RiGrammar</h2></br>
<!-- ============================== class summary ========================== -->

<table cellpadding="0" cellspacing="0" border="0" class="ref-item">
		<tr class="">
			      <th scope="row"<br>Description</th>
			      <!-- DESCRIPTION  -->
			      <td><p><span class="description">A probabilistic context-free grammar with literary extensions for text-generation</span></td>
		</tr>
		<tr class="">
          <th scope="row">Examples</th>
          <td></br>
          	<div class="example">

<pre class="margin">
rg = new RiGrammar(stringOrObject);
result = rg.expand();</pre>

RiTa grammars are valid <a href="http://json.org" target="new">JSON</a> or
<a href="http://keleshev.com/yaml-quick-introduction" target="new">YAML</a> formatted strings (or objects), as follows<p>
<pre>
{
  "&lt;start&gt;": [ "&lt;rule1&gt;", "&lt;rule2&gt;", "&lt;rule3&gt;" ],

  "&lt;rule2&gt;": [ "terminal1", "terminal2", "&lt;rule1&gt;" ],
  ...
}
</pre>
OR (in YAML, where quotes are generally not needed)<p>
<pre>
&lt;start&gt;: [ &lt;rule1&gt;, &lt;rule2&gt;, &lt;rule3&gt; ]

&lt;rule2&gt;: [ terminal1, terminal2, &lt;rule1&gt; ]
...
</pre>
OR (again, in YAML)<p>
<pre>
&lt;start&gt;:
  - &lt;rule1&gt;
  - &lt;multiline&gt;

&lt;rule1&gt;:
  - terminal string 1
  - terminal string 2

&lt;multiline&gt;: >
  This is
  a long string
  that wraps three lines
...
</pre>

<li><a href="http://keleshev.com/yaml-quick-introduction" target="new">YAML</a> is a human-friendly superset of JSON,
which is often more convenient to use if you're writing your grammar as a String.
If you're working in JavaScript, you can alternatively just pass a JavaScript/JSON object instead.
And if you're not sure whether your grammar is valid YAML or JSON, you can check it at
<a href='http://yamllint.com/' target="new"> yamllint.com</a> or <a target="new" href='http://jsonlint.com/'> jsonlint.com</a>.<br/>&nbsp;

<li>Commonly used methods:</li>
<br/>
<ul>

<li><a href="RiGrammar/expand/" target="new">expand()</a> begins at the &lt;start&gt; state and
generates a string of terminals from the grammar.<p>

<li><a href="RiGrammar/expandFrom/" target="new">expandFrom(String)</a> begins with the argument
in String (which can consist of both non-terminals and terminals), and expands from there.
<br>
<p>
	<br>
Notice that <a href="RiGrammar/expand/" target="new">expand()</a> is simply
a convenient version of <a href="RiGrammar/expandFrom/" target="new">expandFrom("&lt;start&gt;");</a>.</p>

<!--li><code>expandWith(String, String)</code> takes 2 String arguments, the 1st
(a terminal) is guaranteed to be substituted for the 2nd (a non-terminal). Once this
substitution is made, the algorithm then works backwards (up the tree from the leaf)
ensuring that the terminal (terminal1) appears in the output string.
For example, with the grammar fragment above, one might call:<p>
<pre>
grammar.expandWith(terminal1, "&lt;rule2&gt;");
</pre>
assuring not only that <code>&lt;rule2&gt;</code>will be used at least
once in the generation process, but that when it is, it will be replaced
by the terminal "hello". -->
</ul>

<li>A RiGrammar object will assign (by default) equal weights to all choices in a rule.
One can adjust the weights by adding 'multipliers' as follows: (in the rule below,
'terminal1' will be chosen twice as often as the 2 other choices.<p>

<pre>&lt;rule2&gt;: terminal1 [2] | terminal2 | &lt;rule1&gt;
</pre>

<li>The RiGrammar object supports callbacks, from your grammar, back into your code.
To generate a callback, add the method call in your grammar, surrounded by back-ticks, as follows:
<p>
<pre>&lt;rule2&gt;:
  - The cat ran after the `randomNoun()`
  - The cat ran after the `pluralize('cat')`
  - The &lt;noun&gt; ran after the `pluralize(&lt;noun&gt;)`
</pre>

<p>
Then call <a href="RiGrammar/expand/" target="new">expand(callee)</a>, where callee is the (optional) object on which to execute the method.
If no object is supplied (e.g., in JavaScript) then the function is called in the current scope.
</p><p>
Any number of arguments may be passed in a callback, but for each call,
there must be a corresponding method in the object that gets the callback , e.g.,
</p>
<pre>function pluralize(theString) {
   ...
}
</pre>
Note: this mechanism is not yet implemented in NodeJS (see <a href="https://github.com/dhowe/RiTa/issues/71">Issue #71</a>)...<p>

            </div></td>
        </tr>

		<tr class="">
		  <th scope="row">Syntax</th>
		  <!-- SYNTAX  -->
		  <td>
<pre>
// Initialize an empty RiGrammar object.

rg = new RiGrammar();


// Or specify a grammar as a YAML/JSON string.

rg = RiGrammar("&lt;start&gt;: [ &lt;rule&gt;, terminal ] ...");


// Or specify a grammar in a YAML/JSON object.

rg = RiGrammar(myJsObject);


// Or load a YAML/JSON grammar from a file

rg = new RiGrammar();
rg.loadFrom("grammar.yaml");


// Or load a YAML/JSON grammar from a URL

rg = new RiGrammar();
rg.loadFrom("http://www.theserver.org/path/grammar.json");

</pre>
</td>
        </tr>

	    <tr class="">
	      <th scope="row">Parameters</th>
	      <td><table cellpadding="0" cellspacing="0" border="0" class="sub-table">
		    <tr class="">
	          <!-- PARAMETERS  -->
	          <th width="25%" scope="row" class="nobold">&nbsp; String or Object</th>
	          <td width="75%">the YAML or JSON grammar</td>
            </tr>
	        </table></td>
        </tr>

	   	<tr class="">
	      <th scope="row">Platform</th>
	      <!-- SYNTAX  -->
	      <td>Java / Javascript</td>
        </tr>

		<tr class='Note' style='display:none'>
	      <th scope="row">Note</th>
	      <!-- SYNTAX  -->
	      <td></td>
        </tr>

		<tr class="">
			<th scope="row"></th>
			<td></td>
		</tr>

	      </table>


  </div>
</div>
</div>
</div>
    </div>
</div>



<?php include("../footer.php"); ?>
<!--
End Site Content
-->
</body>
</html>
