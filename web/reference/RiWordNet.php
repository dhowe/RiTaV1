<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.o../xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

	<head>
		<meta http-equiv="content-type" content="text/html; charset=utf-8" />
		<title>RiWordNet</title>
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
						<ul class="classList" >
							<br />
							<li style="top:60px;left:50px">
								<a href="index.php">Back to index</a>
							</li>
						</ul>
						<!-- end publish.classesIndex -->
					</div>

				</div>
		        <div class="span12">

		<div class="reference row">


<!-- ============================== classes index ============================ -->


		<div id="content">
<!-- ============================== class title ============================ -->
			<h2 class="classTitle">
				RiWordNet (Java-only)
			</h2>


<!-- ============================== class summary ========================== -->

			<div class="span11">
			  <table cellpadding="0" cellspacing="0" border="0" class="ref-item">
			    <tr class="">
			      <th scope="row">Description</th>
			      <!-- DESCRIPTION  -->
			      <td>
			      <p>
Provides support for access to the WordNet ontological database. <b>Note that WordNet data files are no longer supplied
as part of the installation.</b> Please download the version (3.x is recommended) for your platform
<a href="https://wordnet.princeton.edu/download/current-version" target=_blank>here</a>.
The examples below assume the following installation structure (linux/unix/osx):</p><p>
     &nbsp; &nbsp;/path/to/WordNet3.1 <br>
     &nbsp; &nbsp;/path/to/WordNet3.1/dict/<br>
     &nbsp; &nbsp;/path/to/WordNet3.1/dict/data.noun<br>
     &nbsp; &nbsp;...
 </p>
  <p>
 For more info on the meaning of various WordNet concepts (synset, sense,
 hypernym, etc), see <a href="https://wordnet.princeton.edu/documentation/wngloss7wn" target=_blank>this page</a>.
  </p>
 <br/>
 Construct a RiWordNet object like so:

 <pre>
   RiWordNet wordnet = new RiWordNet(&quot;/path/to/WordNet3.1&quot;);
 </pre>

 or, on Windows:

 <pre>
   RiWordNet wordnet = new RiWordNet(&quot;c:\\path\\to\\WordNet3.0&quot;);
 </pre>

 Generally three methods are provided for each relation type. Using hypernyms as an example:
 <pre>
 getHyponyms(int id);

 getHyponyms(String word, String pos)

 getAllHyponyms(String word, String pos).</pre>

 The 1st version returns hyponyms for a specific
 sense (as specified by its unique id), the 2nd returns hyponyms for the most common sense,
 and the 3rd returns hyponyms for ALL senses for the word/pos pair.

 <p/>

 <br/>
 You can also retrieve the entire tree of hyponyms (down to the leaves) for a
 specific sense of a word.

 <!-- ADD EXAMPLE: VariousHypernyms -->

 <br/>&nbsp;<br/>
 <p>
 Note: Valid WordNet parts-of-speech include (noun="n",verb="v",adj="a", and
 adverb="r"). <br>These can be specified either as a literal (e.g., "n") or using the constants:

 <pre>
    RiWordNet.NOUN
    RiWordNet.VERB
    RiWordNet.ADJ
    RiWordNet.ADV
 </pre>

 <p>
 <em>NOTE:</em>&nbsp; Methods returning a String[] will return a zero-length array when the query term
  is not found or there are no entries for the relation type being sought,
  rather than null (as in the original version of this library).

 <p>
 <em>NOTE:</em>&nbsp; Methods returning a String[] will return results in <em>randomized</em> order.
 To disable this behavior, call wordnet.randomizeResults(false);


 <p>
 For more info on the meaning of various WordNet concepts (synset, sense,
 hypernym, etc), see <a href="http://wordnet.princeton.edu/wordnet/man/wngloss.7WN.html" target=_blank>this page</a>.

 <p>
 See the included documentation for license information (in the rita.wordnet pkg).

			      </td>
		        </tr>
            
            <tr class='Syntax'>
							<th scope="row">Syntax</th>

							<!-- SYNTAX  -->
							<td><pre>RiWordNet(String wordnetInstallDir);<br/>RiWordNet(String wordnetInstallDir, boolean ignoreCompoundWords);<br/>RiWordNet("/path/to/WordNet3.1", boolean ignoreCompoundWords, boolean ignoreUpperCaseWords);</pre></td>
						</tr>


						<tr class='Parameters'>
							<th scope="row">Parameters</th>
							<td>

							<!-- PARAMETERS  -->

							<table cellpadding="0" cellspacing="0" border="0" class="sub-table">
								<tr class=''><th width='25%' scope='row' class=nobold>String</th><td width='75%'>path to WordNet dictionary</td></tr><tr class=''><th width='25%' scope='row' class=nobold>boolean</th><td width='75%'>false by default, ignore compound words in the dictionary (optional)</td></tr><tr class=''><th width='25%' scope='row' class=nobold>boolean</th><td width='75%'>false by default, ignore upper case words in the dictionary (optional)</td></tr>
							</table></td>
						</tr>

			   	<tr class="">
			      <th scope="row">Platform</th>
			      <td>Java only&nbsp;(Android coming soon...)
			      &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;  &nbsp; &nbsp; &nbsp; &nbsp;  &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;  &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
			      &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;  &nbsp; &nbsp; &nbsp; &nbsp;  &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;  &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
			      </td>


		        </tr>
               <tr class='Note'>
			      <th id="ritext-docs" scope="row">Documentation:</th>

			      <!-- NOTE  -->
			      <td>
			      	 	<div class="category" style="line-height:16px; padding-top:20px;">

 	<p>

                    <a href="RiWordNet/RiWordNet.ADJ/index.php">RiWordNet.ADJ</a>
                    <br>
                    <a href="RiWordNet/RiWordNet.ADV/index.php">RiWordNet.ADV</a>
                    <br>
                    <a href="RiWordNet/RiWordNet.NOUN/index.php">RiWordNet.NOUN</a>
                    <br>
                    <a href="RiWordNet/RiWordNet.VERB/index.php">RiWordNet.VERB</a>
                    <br>

                    <span class="empty" style="line-height:8px; visibility:hidden">
                        <br>
                    </span>

                    <a href="RiWordNet/exists/index.php">exists()</a>
                    <br>
                    <a href="RiWordNet/getAllAlsoSees/index.php">getAllAlsoSees()</a>
                    <br>
                    <a href="RiWordNet/getAllAntonyms/index.php">getAllAntonyms()</a>
                    <br>
                    <a href="RiWordNet/getAllCoordinates/index.php">getAllCoordinates()</a>
                    <br>
                    <a href="RiWordNet/getAllDerivedTerms/index.php">getAllDerivedTerms()</a>
                    <br>
                    <a href="RiWordNet/getAllExamples/index.php">getAllExamples()</a>
                    <br>
                    <a href="RiWordNet/getAllGlosses/index.php">getAllGlosses()</a>
                    <br>
                    <a href="RiWordNet/getAllHolonyms/index.php">getAllHolonyms()</a>
                    <br>
                    <a href="RiWordNet/getAllHypernyms/index.php">getAllHypernyms()</a>
                    <br>
                    <a href="RiWordNet/getAllHyponyms/index.php">getAllHyponyms()</a>
                    <br>
                    <a href="RiWordNet/getAllMeronyms/index.php">getAllMeronyms()</a>
                    <br>
                    <a href="RiWordNet/getAllNominalizations/index.php">getAllNominalizations()</a>
                    <br>
                    <a href="RiWordNet/getAllSimilar/index.php">getAllSimilar()</a>
                    <br>
                    <a href="RiWordNet/getAllSynonyms/index.php">getAllSynonyms()</a>
                    <br>
                    <a href="RiWordNet/getAllSynsets/index.php">getAllSynsets()</a>
                    <br>
                    <a href="RiWordNet/getAllVerbGroups/index.php">getAllVerbGroups()</a>
                    <br>
                    <a href="RiWordNet/getAlsoSees/index.php">getAlsoSees()</a>
                    <br>
                    <a href="RiWordNet/getAnagrams/index.php">getAnagrams()</a>
                    <br>
                    <a href="RiWordNet/getAntonyms/index.php">getAntonyms()</a>
                    <br>
                    <!--a href="RiWordNet/getBaseForm/index.php">getBaseForm()</a><br-->
                    <a href="RiWordNet/getBestPos/index.php">getBestPos()</a>
                    <br>
                    <a href="RiWordNet/getCommonParent/index.php">getCommonParent()</a>
                    <br>
                    <a href="RiWordNet/getCommonParents/index.php">getCommonParents()</a>
                    <br>
                    <a href="RiWordNet/getContains/index.php">getContains()</a>
                    <br>
                    <a href="RiWordNet/getCoordinates/index.php">getCoordinates()</a>
                    <br>
                    <a href="RiWordNet/getDerivedTerms/index.php">getDerivedTerms()</a>
                    <!--br>
                    <a href="RiWordNet/getDescription/index.php">getDescription()</a-->
                    <br>
                    <a href="RiWordNet/getDistance/index.php">getDistance()</a>
                    <br>
                    <a href="RiWordNet/getEndsWith/index.php">getEndsWith()</a>
                    <br>
                    <a href="RiWordNet/getExamples/index.php">getExamples()</a>
                    <br>
                    <a href="RiWordNet/getGloss/index.php">getGloss()</a>
                    <br>
                    <a href="RiWordNet/getHolonyms/index.php">getHolonyms()</a>
                    <br>
                    <a href="RiWordNet/getHypernyms/index.php">getHypernyms()</a>
                    <br>
                    <a href="RiWordNet/getHypernymTree/index.php">getHypernymTree()</a>
                    <br>
                    <a href="RiWordNet/getHyponymTree/index.php">getHyponymTree()</a>
                    <br>
                    <a href="RiWordNet/getMeronyms/index.php">getMeronyms()</a>
                    <br>
                    <a href="RiWordNet/getNominalizations/index.php">getNominalizations()</a>
                    <br>
                    <a href="RiWordNet/getPos/index.php">getPos()</a>
                    <br>
                    <a href="RiWordNet/getRandomExample/index.php">getRandomExample()</a>
                    <br>
                    <a href="RiWordNet/getRandomExamples/index.php">getRandomExamples()</a>
                    <br>
                    <a href="RiWordNet/getRandomWord/index.php">getRandomWord()</a>
                    <br>
                    <a href="RiWordNet/getRandomWords/index.php">getRandomWords()</a>
                    <br>
                    <a href="RiWordNet/getRegexMatch/index.php">getRegexMatch()</a>
                    <br>
                    <a href="RiWordNet/getSenseCount/index.php">getSenseCount()</a>
                    <br>
                    <a href="RiWordNet/getSenseIds/index.php">getSenseIds()</a>
                    <br>
                    <a href="RiWordNet/getSimilar/index.php">getSimilar()</a>
                    <br>
                    <a href="RiWordNet/getSoundsLike/index.php">getSoundsLike()</a>
                    <br>
                    <a href="RiWordNet/getStartsWith/index.php">getStartsWith()</a>
                    <br>
                    <a href="RiWordNet/getStems/index.php">getStems()</a>
                    <br>
                    <a href="RiWordNet/getSynonyms/index.php">getSynonyms()</a>
                    <br>
                    <a href="RiWordNet/getSynset/index.php">getSynset()</a>
                    <br>
                    <a href="RiWordNet/getVerbGroup/index.php">getVerbGroup()</a>
                    <br>
                    <a href="RiWordNet/getWildcardMatch/index.php">getWildcardMatch()</a>
                    <br>
                    <a href="RiWordNet/ignoreCompoundWords/index.php">ignoreCompoundWords()</a>
                    <br>
                    <a href="RiWordNet/ignoreUpperCaseWords/index.php">ignoreUpperCaseWords()</a>
                    <br>
                    <a href="RiWordNet/isAdjective/index.php">isAdjective()</a>
                    <br>
                    <a href="RiWordNet/isAdverb/index.php">isAdverb()</a>
                    <br>
                    <a href="RiWordNet/isIgnoringCompoundWords/index.php">isIgnoringCompoundWords()</a>
                    <br>
                    <a href="RiWordNet/isIgnoringUpperCaseWords/index.php">isIgnoringUpperCaseWords()</a>
                    <br>
                    <a href="RiWordNet/isNoun/index.php">isNoun()</a>
                    <br>
                    <a href="RiWordNet/isStem/index.php">isStem()</a>
                    <br>
                    <a href="RiWordNet/isVerb/index.php">isVerb()</a>
                    <br>
                    <a href="RiWordNet/iterator/index.php">iterator()</a>
                    <br>
                    <a href="RiWordNet/printHypernymTree/index.php">printHypernymTree()</a>
 	</p>

			      	 	</div>
			      </td>
		      </table>


</div>
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
