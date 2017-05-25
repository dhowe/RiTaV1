<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.o../xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
	<head>
		<meta http-equiv="content-type" content="text/html; charset=utf-8" />
		<title>RiLexicon</title>
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

		<div id="content">
<!-- ============================== class title ============================ -->
			<h2 class="classTitle">
				RiLexicon
			</h2>
<!-- ============================== class summary ========================== -->
		  <table cellpadding="0" cellspacing="0" border="0" class="ref-item">
		  	<tr>
		  		<th></th><td><p>Note: All functions in ReLexicon can now be accessed via RiTa.*</p></td>
		  	</tr>
				    <tr class="">
			      <th scope="row">Description</th>
			      <!-- DESCRIPTION  -->
			      <td><p>The core 'dictionary' (or lexicon) for the RiTa tools.</p>
						<p>
						Contains ~40,000 words augmented with phonemic and syllabic data, as well as a list of valid parts-of-speech for each.
						The lexicon can be extended and/or customized for additional words, usages, or pronunciations.
						<p> Also equipped with implementations of a variety of matching algorithms
						(min-edit-distance, soundex, anagrams, alliteration, rhymes, looks-like, etc.)
						based on combinations of letters, syllables, phonemes, and stresses.
						<p>
						Note: If you wish to modify or customize the lexicon (e.g., add words, or change pronunciations)
						you can do so directly, by editing the included 'rita_dict.js' file,
						or programatically, via addWord() and removeWord().
						<p>
						Note: For performance, the data for RiLexicon is shared in a single location for ALL created instances
					     
					<br/>
</p></td>
		        </tr>
		<tr class="">
          <th scope="row">Examples</th>
          <td><div class="example">
			    <!-- EXAMPLES  -->
			    <!-- EXAMPLE IMAGE  -->
			    <!-- <img src="../alpha_.gif" alt="example pic" /> -->
	     <pre class="margin">
lexicon = new RiLexicon();

similars = lexicon.similarBySound("cat");

rhymes = lexicon.rhymes("cat");
</pre>
            </div></td>
        </tr>

			    <tr class="">
			      <th scope="row">Syntax</th>
			      <!-- SYNTAX  -->
			      <td><pre>RiLexicon()</pre>
		        </tr>
			    <!--tr class="">
			      <th scope="row">Parameters</th>
			      <td><table cellpadding="0" cellspacing="0" border="0" class="sub-table">
			        <tr class="">
			          <th width="25%" scope="row">PApplet</th>
			          <td width="75%">the pApplet</td>
		            </tr>
			        </table></td>
		        </tr-->
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
