<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
	<head>
		<meta http-equiv="content-type" content="text/html; charset=utf-8" />
		<title>RiTa Reference</title>
		<link rel="stylesheet" href="../../../css/bootstrap.css" type="text/css" />
		<link rel="stylesheet" href="../../../css/syntax.css" type="text/css" />
		<link rel="stylesheet" href="../../../css/style.css" type="text/css" />
		<link rel="shortcut icon" type="image/x-icon" href="http://rednoise.org/rita/rita.ico"/>

		  <meta name="viewport" content="width=device-width, initial-scale=1">
		<link rel="stylesheet" href="../../../css/normalize.css">
		<link rel="stylesheet" href="../../../css/main.css">
		<script src="../../../js/vendor/modernizr-2.6.2.min.js"></script>
		<script language="javascript" src="../../../js/highlight.js"></script>
	</head>

	<body>

 <?php include("../../../header.php"); ?>


<div class="gd-section pad-large">
<div class="gd-center pad-large">
<div class="row">
  <div class="col1"></div>
  <div class="col10">
     <h3>Reference</h3>
     <div class="page row">

				<div class="refbar span3">
					<div id="index">
						<!-- begin publish.classesIndex -->
						<h3></h3>
						<ul class="classList" >
							<br />
							<li style="top:60px;left:50px">
								<a href="../../index.php">Back to index</a>
							</li>
						</ul>
						<hr />
						<!-- end publish.classesIndex -->
					</div>
				</div>


				<div class="span11">
					<table cellpadding="0" cellspacing="0" border="0" class="ref-item">
						<tr class="name-row">
							<th scope="row">Class</th>
							<!-- ------------ METHODS PROPERTIES HERE ------------ -->

							<!-- ClASS -->
							<td><h3><a href="../../RiText.php">RiText</a></h3></td>
						</tr>

						<tr class="name-row">
							<th scope="row">Name</th>

							<!-- METHOD NAME -->
							<td><h3>RiText.createWords</h3></td>
						</tr>

						<tr class="">
							<th scope="row">Description</th>

							<!-- DESCRIPTION  -->
							<td>Creates an array of RiTexts ,laid out in lines within the bounding rectange(x,y,w,h), one RiText per word, with spacing determined by the specified font and leading</td>
						</tr>

						<tr class='Syntax'>
							<th scope="row">Syntax</th>

							<!-- SYNTAX  -->
							<td><pre>RiText.createWords(pApplet, text, x, y, w, h, font, leading);</pre></td>
						</tr>


						<tr class='Parameters'>
							<th scope="row">Parameters</th>
							<td>

							<!-- PARAMETERS  -->

							<table cellpadding="0" cellspacing="0" border="0" class="sub-table">
								<tr class=''><th width='25%' scope='row' class=nobold>PApplet</th><td width='75%'>the sketch itself or 'this' (optional in JavaScript)</td></tr><tr class=''><th width='25%' scope='row' class=nobold>String or String[]</th><td width='75%'>the text to layout (if an array, each array element will be a line in the layout)</td></tr><tr class=''><th width='25%' scope='row' class=nobold>float</th><td width='75%'>x-position of layout rect</td></tr><tr class=''><th width='25%' scope='row' class=nobold>float</th><td width='75%'>y-position of layout rect</td></tr><tr class=''><th width='25%' scope='row' class=nobold>float</th><td width='75%'>width of layout rect (optional, defaults to right-most edge of sketch)</td></tr><tr class=''><th width='25%' scope='row' class=nobold>float</th><td width='75%'>height of layout rect (optional, defaults to Number.MAX_VALUE)</td></tr><tr class=''><th width='25%' scope='row' class=nobold>Object</th><td width='75%'>the font to use (generally a PFont) for the layout (optional, defaults to RiText.defaults.font)</td></tr><tr class=''><th width='25%' scope='row' class=nobold>float</th><td width='75%'>the leading to use for the layout (optional, defaults to RiText.defaults.leading)</td></tr>
							</table></td>
						</tr>


						<tr class='Returns'>
							<th scope="row">Returns</th>
							<td>

							<!-- RETURNS/TYPE (for variables)  -->

							<table cellpadding="0" cellspacing="0" border="0" class="sub-table">
								<tr class=''><th width='25%' scope='row' class=nobold>RiText[]</th><td width='75%'>The created RiTexts</td></tr>
							</table></td>
						</tr>

						<tr class='Related'>
							<th scope="row">Related</th>

							<!-- RELATED  -->
							<td>RiText.createLines(), RiText.createLetters</td>
						</tr>

						<tr class='Note' style='display:none'>
							<th scope="row">Note</th>
							<!-- NOTE  -->
							<td>tmp_note</td>
						</tr>

						<tr class='Example' style='display:none'>
							<th scope='row'>Example</th>
							<td>
							<div class="example">

								<!-- EXAMPLE  -->
								<!--img src="../../../img/RiTa-logo4.png" alt="example pic" /-->
								<pre class="margin">tmp_example</pre>
							</div></td>
						</tr>

						<tr class="">
							<th scope="row">Platform</th>
							<!-- PLATFORM  -->
							<td>Java / Javascript</td>
						</tr>

						<tr class="">
							<th scope="row"></th>
							<td></td>
						</tr>

					</table>
				</div>
			</div>
  </div>
    <div class="col1"></div>

</div>
</div>
    </div>




<?php include("../../../footer.php"); ?>


	</body>
</html>
