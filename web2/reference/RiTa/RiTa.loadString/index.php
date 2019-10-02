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
							<td><h3>RiTa.loadString</h3></td>
						</tr>

						<tr class="">
							<th scope="row">Description</th>

							<!-- DESCRIPTION  -->
							<td>Loads the contents of a file or URL and then calls back to the supplied 'callback' function with the loaded string as an argument.</td>
						</tr>

						<tr class='Syntax'>
							<th scope="row">Syntax</th>

							<!-- SYNTAX  -->
							<td><pre>RiTa.loadString(url);<br/><br/>RiTa.loadString(url, callback);<br/>RiTa.loadString(url, pApplet);<br/><br/>RiTa.loadString(url, callback, str);<br/>RiTa.loadString(url, pApplet, str);</pre></td>
						</tr>


						<tr class='Parameters'>
							<th scope="row">Parameters</th>
							<td>

							<!-- PARAMETERS  -->

							<table cellpadding="0" cellspacing="0" border="0" class="sub-table">
								<tr class=''><th width='25%' scope='row' class=nobold>String</th><td width='75%'>the filename from which to load the file</td></tr><tr class=''><th width='25%' scope='row' class=nobold>Function or Object (optional)</th><td width='75%'>A callback (in JS) or a PApplet (in Java)<br>If a callback function, it will be called after the file is loaded, with the loaded string as its argument</td></tr><tr class=''><th width='25%' scope='row' class=nobold>String (optional)</th><td width='75%'>with which to join the lines of the string (default = '\n') -- in some cases, a space character may be more useful</td></tr>
							</table></td>
						</tr>


						<tr class='Returns'>
							<th scope="row">Returns</th>
							<td>

							<!-- RETURNS/TYPE (for variables)  -->

							<table cellpadding="0" cellspacing="0" border="0" class="sub-table">
								void
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
							<td>In Node, uses the node 'filesystem' or 'request' modules; in JavaScript, uses an XMLHttpRequest; in Java, if a Processing PApplet is passed as a 2nd argument, it will be used to locate and check the 'data' folder of the sketch for the specified file</td>
						</tr>

						<tr class='Example'>
							<th scope='row'>Example</th>
							<td>
							<div class="example">

								<!-- EXAMPLE  -->
								<!--img src="../../../img/RiTa-logo4.png" alt="example pic" /-->
								<pre class="margin">txt = RiTa.loadString("kafka.txt"); <br>txt = RiTa.loadString('kafka.txt', callbackFun); // JS <br>txt = RiTa.loadString("kafka.txt", this, " "); &nbsp; // Java</pre>
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
