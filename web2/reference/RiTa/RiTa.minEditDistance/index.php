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
							<td><h3><a href="../../RiTa.php">RiTa</a></h3></td>
						</tr>

						<tr class="name-row">
							<th scope="row">Name</th>

							<!-- METHOD NAME -->
							<td><h3>RiTa.minEditDistance</h3></td>
						</tr>

						<tr class="">
							<th scope="row">Description</th>

							<!-- DESCRIPTION  -->
							<td>Takes a pair of strings or string-arrays and returns the minimum edit distance between them (the number of additions, subtractions, or modifications it takes to go from one to the other)</td>
						</tr>

						<tr class='Syntax'>
							<th scope="row">Syntax</th>

							<!-- SYNTAX  -->
							<td><pre>RiTa.minEditDistance(str1, str2);</pre></td>
						</tr>


						<tr class='Parameters'>
							<th scope="row">Parameters</th>
							<td>

							<!-- PARAMETERS  -->

							<table cellpadding="0" cellspacing="0" border="0" class="sub-table">
								<tr class=''><th width='25%' scope='row' class=nobold>String</th><td width='75%'>str1</td></tr><tr class=''><th width='25%' scope='row' class=nobold>String</th><td width='75%'>str2</td></tr><tr class=''><th width='25%' scope='row' class=nobold>boolean</th><td width='75%'>normalize (optional) if true, normalizes the distance based on the length of the strings</td></tr>
							</table></td>
						</tr>


						<tr class='Returns'>
							<th scope="row">Returns</th>
							<td>

							<!-- RETURNS/TYPE (for variables)  -->

							<table cellpadding="0" cellspacing="0" border="0" class="sub-table">
								<tr class=''><th width='25%' scope='row' class=nobold>int</th><td width='75%'>the minimum-edit distance</td></tr>
							</table></td>
						</tr>

						<tr class='Related' style='display:none'>
							<th scope="row">Related</th>

							<!-- RELATED  -->
							<td>tmp_related</td>
						</tr>

						<tr class='Note'>
							<th scope="row">Note</th>
							<!-- NOTE  -->
							<td>See <a href="https://en.wikipedia.org/wiki/Edit_distance">https://en.wikipedia.org/wiki/Edit_distance</a></td>
						</tr>

						<tr class='Example'>
							<th scope='row'>Example</th>
							<td>
							<div class="example">

								<!-- EXAMPLE  -->
								<!--img src="../../../img/RiTa-logo4.png" alt="example pic" /-->
								<pre class="margin"><br>RiTa.minEditDistance("Hello", "Hell"); // -> 1<br><br>RiTa.minEditDistance("Hello", "Hell", true); // -> 0.2<br><br></pre>
							</div></td>
						</tr>

						<tr class="">
							<th scope="row">Platform</th>
							<!-- PLATFORM  -->
							<td>Java / JavaScript</td>
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
