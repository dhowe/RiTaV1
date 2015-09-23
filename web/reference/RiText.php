<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.o../xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
	<head>
		<meta http-equiv="content-type" content="text/html; charset=utf-8" />
		<title>RiText</title>
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
				RiText
			</h2>
<!-- ============================== class summary ========================== -->

	<table cellpadding="0" cellspacing="0" border="0" class="ref-item">
		<tr class="">
	      <th scope="row">Description</th>
	      <!-- DESCRIPTION  -->
	      <td>
			<p>
			<span style="color:#c00000">Note: the RiText object has been deprecated and will no longer be developed</span>.
			For backwards compatibility, the RiText object (from v1.0) will continue to be included in the Java distribution of RiTa.
			For JavaScript, you may manually included ritext.js, available on the download <a href="https://rednoise.org/rita/download">page</a>
			See the RiText documentation<a href="#ritext-docs">here</a>.
			</p>
			<p>
			Wraps an instance of RiString to provide utility methods for typography, text effects, animation, etc.
			</p>
		   </td>
		</tr>
		<tr class="">
          <th scope="row">Examples</th>
          <td><div class="example">
			    <!-- EXAMPLES  -->
			    <!-- <img src="../alpha_.gif" alt="example pic" /> -->
	     <pre class="margin">
import rita.*;

RiText rt;

void setup() {

   // start in the upper left corner
   rt = new RiText(this, "hello", 0, 10);

   // move to the lower right over 2 seconds
   rt.moveTo(width-40, height, 2);
}

void draw() {

   // draw bg and RiText each frame
   background(255);
   rt.draw();
}</pre>
            </div></td>
        </tr>

			    <tr class="">
			      <th scope="row">Syntax</th>
			      <!-- SYNTAX  -->
			      <td><pre>

// Creates a new RiText object base-aligned at ('x', 'y')
// using the font object (optionally) specified by 'theFont'.

RiText(PApplet pApplet, String text)
RiText(PApplet pApplet, String text, float x, float y)
RiText(PApplet pApplet, String text, float x, float y, PFont theFont)

Note: First argument (pApplet) is optional in JavaScript
</pre></td>

		        </tr>
			    <tr class="">
			      <th scope="row">Parameters</th>
			      <td><table cellpadding="0" cellspacing="0" border="0" class="sub-table">
			        <tr class="">
			          <!-- PARAMETERS  -->
			          <th width="25%" scope="row" class="nobold">&nbsp; PApplet</th>
			          <td width="75%">the pApplet</td>
		            </tr>
                    <tr class="">
			          <!-- PARAMETERS  -->
			          <th width="25%" scope="row" class="nobold">&nbsp; String</th>
			          <td width="75%">the text</td>
		            </tr>
                    <tr class="">
			          <!-- PARAMETERS  -->
			          <th width="25%" scope="row" class="nobold">&nbsp; float</th>
			          <td width="75%">the X position (optional)</td>
		            </tr>
                    <tr class="">
			          <!-- PARAMETERS  -->
			          <th width="25%" scope="row" class="nobold">&nbsp; float</th>
			          <td width="75%">the Y position (optional)</td>
		            </tr>
                    <tr class="">
			          <!-- PARAMETERS  -->
			          <th width="25%" scope="row" class="nobold">&nbsp; PFont</th>
			          <td width="75%">the font (optional)</td>
		            </tr>

			        </table></td>
		        </tr>
			   <tr class="">
			      <th scope="row">Platform</th>
			      <!-- SYNTAX  -->
			      <td>Java / Javascript</td>
		        </tr>
				<tr class='Note'>
			      <th scope="row">Note</th>
			      <!-- NOTE  -->
			      <td>First argument (pApplet) is optional when using JavaScript mode</td>
		        </tr>
		        <tr class='Note'>
			      <th id="ritext-docs" scope="row">Documentation:</th>

			      <!-- NOTE  -->
			      <td>
			      	<div class="category" style="line-height:16px; padding-top:20px;">
 	<p>
		<a href="RiText/x/">x</a>,<a href="RiText/y/">y</a>,<a href="RiText/z/">z</a><br>

	 	<span class="empty" style="line-height:16px; visibility:hidden"><br></span>

		<a href="RiText/align/">align()</a><br>
		<a href="RiText/alpha/">alpha()</a><br>

		<a href="RiText/analyze/">analyze()</a><br>
		<a href="RiText/boundingBox/">boundingBox()</a><br>
		<a href="RiText/boundingBox/">boundingStroke()</a><br>
		<a href="RiText/boundingBox/">boundingStrokeWeight()</a><br>
		<a href="RiText/center/">center()</a><br>
		<a href="RiText/charAt/">charAt()</a><br>
		<a href="RiText/charOffset/">charOffset()</a><br>
		<a href="RiText/colorTo/">colorTo()</a><br>
		<a href="RiText/concat/">concat()</a><br>
		<a href="RiText/contains/">contains()</a><br>
		<a href="RiText/copy/">copy()</a><br>
		<a href="RiText/defaults.alignments/">defaults.alignments</a><br>
		<a href="RiText/defaults.fill/">defaults.fill</a><br>
		<a href="RiText/defaults.motionType/">defaults.motionType</a><br>
		<a href="RiText/defaults.rotateX/">defaults.rotateX</a><br>
		<a href="RiText/defaults.rotateY/">defaults.rotateY</a><br>
		<a href="RiText/defaults.rotateZ/">defaults.rotateZ</a><br>
		<a href="RiText/defaults.scaleX/">defaults.scaleX</a><br>
		<a href="RiText/defaults.scaleY/">defaults.scaleY</a><br>
		<a href="RiText/defaults.scaleZ/">defaults.scaleZ</a><br>
		<a href="RiText/distanceTo/">distanceTo()</a><br>
		<a href="RiText/draw/">draw()</a><br>
		<a href="RiText/endsWith/">endsWith()</a><br>
		<a href="RiText/equals/">equals()</a><br>
		<a href="RiText/equalsIgnoreCase/">equalsIgnoreCase()</a><br>
		<a href="RiText/fadeIn/">fadeIn()</a><br>
		<a href="RiText/fadeOut/">fadeOut()</a><br>
		<a href="RiText/features/">features()</a><br>
		<a href="RiText/fill/">fill()</a><br>
		<a href="RiText/font/">font()</a><br>
		<a href="RiText/fontSize/">fontSize()</a><br>
		<a href="RiText/indexOf/">indexOf()</a><br>
		<a href="RiText/insertWord/">insertWord()</a><br>
		<a href="RiText/isVisible/">isVisible()</a><br>
		<a href="RiText/lastIndexOf/">lastIndexOf()</a><br>
		<a href="RiText/length/">length()</a><br>
		<a href="RiText/match/">match()</a><br>
		<a href="RiText/motionType/">motionType()</a><br>
		<a href="RiText/moveTo/">moveTo()</a><br>
		<a href="RiText/pos/">pos()</a><br>
		<a href="RiText/posAt/">posAt()</a><br>
		<a href="RiText/position/">position()</a><br>
		<a href="RiText/removeCharAt/">removeChar()</a><br>
		<a href="RiText/replaceAll/">replaceAll()</a><br>
		<a href="RiText/replaceCharAt/">replaceChar()</a><br>
		<a href="RiText/replaceFirst/">replaceFirst()</a><br>
		<a href="RiText/replaceLast/">replaceLast()</a><br>
		<a href="RiText/replaceWordAt/">replaceWord()</a><br>
		<a href="RiText/rotate/">rotate()</a><br>
		<a href="RiText/rotateTo/">rotateTo()</a><br>
		<a href="RiText/scale/">scale()</a><br>
		<a href="RiText/scaleTo/">scaleTo()</a><br>
		<a href="RiText/showBounds/">showBounds()</a><br>
		<a href="RiText/slice/">slice()</a><br>
		<a href="RiText/splitLetters/">splitLetters()</a><br>
		<a href="RiText/splitWords/">splitWords()</a><br>
		<a href="RiText/startsWith/">startsWith()</a><br>
		<a href="RiText/stopBehavior/">stopBehavior()</a><br>
		<a href="RiText/stopBehaviors/">stopBehaviors()</a><br>
		<a href="RiText/substr/">substr()</a><br>
		<a href="RiText/substring/">substring()</a><br>
		<a href="RiText/text/">text()</a><br>
		<a href="RiText/textAscent/">textAscent()</a><br>
		<a href="RiText/textDescent/">textDescent()</a><br>
		<a href="RiText/textHeight/">textHeight()</a><br>
		<a href="RiText/textTo/">textTo()</a><br>
		<a href="RiText/textWidth/">textWidth()</a><br>
		<a href="RiText/toLowerCase/">toLowerCase()</a><br>
		<a href="RiText/toUpperCase/">toUpperCase()</a><br>
		<a href="RiText/trim/">trim()</a><br>
		<a href="RiText/wordAt/">wordAt()</a><br>
		<a href="RiText/wordCount/">wordCount()</a><br>
		<a href="RiText/wordOffset/">wordOffset()</a><br>
		<a href="RiText/words/">words()</a>	<br>

		<br>

		<a href="RiText/RiText.createLetters/">RiText.createLetters()</a><br>
		<a href="RiText/RiText.createLines/">RiText.createLines()</a><br>
		<a href="RiText/RiText.createWords/">RiText.createWords()</a><br>
		<a href="RiText/RiText.defaultFill/">RiText.defaultFill()</a><br>
		<a href="RiText/RiText.defaultFont/">RiText.defaultFont()</a><br>
		<a href="RiText/RiText.defaultFontSize/">RiText.defaultFontSize()</a><br>
		<a href="RiText/RiText.dispose/">RiText.dispose()</a><br>
		<a href="RiText/RiText.disposeAll/">RiText.disposeAll()</a><br>
		<a href="RiText/RiText.drawAll/">RiText.drawAll()</a><br>
		<a href="RiText/RiText.pauseTimer/">RiText.pauseTimer()</a> <br>
		<a href="RiText/RiText.picked/">RiText.picked()</a> <br>
		<a href="RiText/RiText.random/">RiText.random()</a><br>
		<a href="RiText/RiText.randomColor/">RiText.randomColor()</a><br>
		<a href="RiText/RiText.stopTimer/">RiText.stopTimer()</a><br>
		<a href="RiText/RiText.timer/">RiText.timer()</a> <br>
</p>
	</div>
			      </td>
		        </tr>

				<tr class="">
					<th scope="row"></th>
					<td></td>
				</tr>

		      </table>

			</div>
	        </div>

	      </div>

<!-- ============================== documentation ================================= -->

<!-- ============================== footer ================================= -->

</div>
    </div>
</div>



<?php include("../footer.php"); ?>
<!--
End Site Content
-->

</body>
</html>
