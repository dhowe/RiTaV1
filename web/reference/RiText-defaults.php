<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.o../xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

	<head>
		<meta http-equiv="content-type" content="text/html; charset=utf-8" />
		<title>RiTaEvent</title>
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
				RiText.defaults
			</h2>

<!-- ============================== class summary ========================== -->

			<div class="span11">
			  <table cellpadding="0" cellspacing="0" border="0" class="ref-item">
			    <tr class="">
			      <th scope="row">Description</th>
			      <!-- DESCRIPTION  -->
			      <td>This (static) object stores the default values for newly created RiTexts<p></td></tr>
				<tr class="">
		          <th scope="row">Fields/Types</th>
		          <td><div class="example">
			     	<!--pre class="margin"-->
			     	<table class="fields">
			     		<tr><td>RiText.defaults.fill</td><td>float[]</td></tr>
			     		<tr><td>RiText.defaults.alignment</td><td>int</td></tr>
			     		<tr><td>RiText.defaults.motionType</td><td>int</td></tr>
			     		<tr><td>RiText.defaults.scaleX</td><td>float</td></tr>
			     		<tr><td>RiText.defaults.scaleY</td><td>float</td></tr>
			     		<tr><td>RiText.defaults.scaleZ</td><td>float</td></tr>
						<tr><td>RiText.defaults.rotateX</td><td>float</td></tr>
			     		<tr><td>RiText.defaults.rotateY</td><td>float</td></tr>
			     		<tr><td>RiText.defaults.rotateZ</td><td>float</td></tr>
			     		<tr><td>RiText.defaults.fontSize</td><td>float</td></tr>
			     		<tr><td>RiText.defaults.leadingFactor</td><td>float</td></tr>
						<tr><td>RiText.defaults.paragraphIndent</td><td>float</td></tr>
			     		<tr><td>RiText.defaults.paragraphLeading</td><td>float</td></tr>
			     		<tr><td>RiText.defaults.boundingStrokeWeight</td><td>float</td></tr>
			     		<tr><td>RiText.defaults.boundingStroke</td><td>float[]</td></tr>
						<tr><td>RiText.defaults.showBounds</td><td>boolean</td></tr>
						<tr><td>RiText.defaults.fontFamily</td><td>string</td></tr>
						<tr><td>RiText.defaults.font</td><td>object</td></tr>
			     	</table>
			     	<!--/pre-->
		            </div>
				  </td>
		        </tr>

			    <tr class="">
			      <th scope="row">Syntax</th>
			      <!-- SYNTAX  -->
			      <td>
<pre>
// Access the variables directly

RiText.defaults.motionType = RiTa.EASE_IN;  // 15 options
RiText.defaults.alignment = RiTa.CENTER;  // default=RiTa.LEFT
RiText.defaults.leadingFactor = 1.5;  // default=1.2
RiText.defaults.fontFamily = "Arial"; // default="Times New Roman"
RiText.defaults.boundingStroke = 2; // default=1

// Use helper methods instead:

RiText.defaultFont("Arial", 24);
RiText.defaultFill(255, 0, 0);
</pre>
				  </td>
		        </tr>
			    <tr class="" style='display:none'>
			      <th scope="row">Parameters</th>
			      <td><table cellpadding="0" cellspacing="0" border="0" class="sub-table">
			        <tr class="">
			          <!-- PARAMETERS  -->
			          <th width="25%" scope="row" class="nobold">&nbsp; Object</th>
			          <td width="75%">the source</td>
		            </tr>
                    			        <tr class="">
			          <!-- PARAMETERS  -->
			          <th width="25%" scope="row" class="nobold">&nbsp; int</th>
			          <td width="75%">the type</td>
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




<?php include("../footer.php"); ?>
<!--
End Site Content
-->

</body>
</html>
