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
		<a href="RiText/x/index.html">x</a>,<a href="RiText/y/index.html">y</a>,<a href="RiText/z/index.html">z</a><br>

	 	<span class="empty" style="line-height:16px; visibility:hidden"><br></span>

		<a href="RiText/align_/index.html">align()</a><br>
		<a href="RiText/alpha_/index.html">alpha()</a><br>

		<a href="RiText/analyze_/index.html">analyze()</a><br>
		<a href="RiText/boundingBox_/index.html">boundingBox()</a><br>
		<a href="RiText/boundingBox_/index.html">boundingStroke()</a><br>
		<a href="RiText/boundingBox_/index.html">boundingStrokeWeight()</a><br>
		<a href="RiText/center_/index.html">center()</a><br>
		<a href="RiText/charAt_/index.html">charAt()</a><br>
		<a href="RiText/charOffset_/index.html">charOffset()</a><br>
		<a href="RiText/colorTo_/index.html">colorTo()</a><br>
		<a href="RiText/concat_/index.html">concat()</a><br>
		<a href="RiText/contains_/index.html">contains()</a><br>
		<a href="RiText/copy_/index.html">copy()</a><br>
		<a href="RiText/defaults.alignments/index.html">defaults.alignments</a><br>
		<a href="RiText/defaults.fill/index.html">defaults.fill</a><br>
		<a href="RiText/defaults.motionType/index.html">defaults.motionType</a><br>
		<a href="RiText/defaults.rotateX/index.html">defaults.rotateX</a><br>
		<a href="RiText/defaults.rotateY/index.html">defaults.rotateY</a><br>
		<a href="RiText/defaults.rotateZ/index.html">defaults.rotateZ</a><br>
		<a href="RiText/defaults.scaleX/index.html">defaults.scaleX</a><br>
		<a href="RiText/defaults.scaleY/index.html">defaults.scaleY</a><br>
		<a href="RiText/defaults.scaleZ/index.html">defaults.scaleZ</a><br>
		<a href="RiText/distanceTo_/index.html">distanceTo()</a><br>
		<a href="RiText/draw_/index.html">draw()</a><br>
		<a href="RiText/endsWith_/index.html">endsWith()</a><br>
		<a href="RiText/equals_/index.html">equals()</a><br>
		<a href="RiText/equalsIgnoreCase_/index.html">equalsIgnoreCase()</a><br>
		<a href="RiText/fadeIn_/index.html">fadeIn()</a><br>
		<a href="RiText/fadeOut_/index.html">fadeOut()</a><br>
		<a href="RiText/features_/index.html">features()</a><br>
		<a href="RiText/fill_/index.html">fill()</a><br>
		<a href="RiText/font_/index.html">font()</a><br>
		<a href="RiText/fontSize_/index.html">fontSize()</a><br>
		<a href="RiText/indexOf_/index.html">indexOf()</a><br>
		<a href="RiText/insertWord_/index.html">insertWord()</a><br>
		<a href="RiText/isVisible_/index.html">isVisible()</a><br>
		<a href="RiText/lastIndexOf_/index.html">lastIndexOf()</a><br>
		<a href="RiText/length_/index.html">length()</a><br>
		<a href="RiText/match_/index.html">match()</a><br>
		<a href="RiText/motionType_/index.html">motionType()</a><br>
		<a href="RiText/moveTo_/index.html">moveTo()</a><br>
		<a href="RiText/pos_/index.html">pos()</a><br>
		<a href="RiText/posAt_/index.html">posAt()</a><br>
		<a href="RiText/position_/index.html">position()</a><br>
		<a href="RiText/removeCharAt_/index.html">removeChar()</a><br>
		<a href="RiText/replaceAll_/index.html">replaceAll()</a><br>
		<a href="RiText/replaceCharAt_/index.html">replaceChar()</a><br>
		<a href="RiText/replaceFirst_/index.html">replaceFirst()</a><br>
		<a href="RiText/replaceLast_/index.html">replaceLast()</a><br>
		<a href="RiText/replaceWordAt_/index.html">replaceWord()</a><br>
		<a href="RiText/rotate_/index.html">rotate()</a><br>
		<a href="RiText/rotateTo_/index.html">rotateTo()</a><br>
		<a href="RiText/scale_/index.html">scale()</a><br>
		<a href="RiText/scaleTo_/index.html">scaleTo()</a><br>
		<a href="RiText/showBounds_/index.html">showBounds()</a><br>
		<a href="RiText/slice_/index.html">slice()</a><br>
		<a href="RiText/splitLetters_/index.html">splitLetters()</a><br>
		<a href="RiText/splitWords_/index.html">splitWords()</a><br>
		<a href="RiText/startsWith_/index.html">startsWith()</a><br>
		<a href="RiText/stopBehavior_/index.html">stopBehavior()</a><br>
		<a href="RiText/stopBehaviors_/index.html">stopBehaviors()</a><br>
		<a href="RiText/substr_/index.html">substr()</a><br>
		<a href="RiText/substring_/index.html">substring()</a><br>
		<a href="RiText/text_/index.html">text()</a><br>
		<a href="RiText/textAscent_/index.html">textAscent()</a><br>
		<a href="RiText/textDescent_/index.html">textDescent()</a><br>
		<a href="RiText/textHeight_/index.html">textHeight()</a><br>
		<a href="RiText/textTo_/index.html">textTo()</a><br>
		<a href="RiText/textWidth_/index.html">textWidth()</a><br>
		<a href="RiText/toLowerCase_/index.html">toLowerCase()</a><br>
		<a href="RiText/toUpperCase_/index.html">toUpperCase()</a><br>
		<a href="RiText/trim_/index.html">trim()</a><br>
		<a href="RiText/wordAt_/index.html">wordAt()</a><br>
		<a href="RiText/wordCount_/index.html">wordCount()</a><br>
		<a href="RiText/wordOffset_/index.html">wordOffset()</a><br>
		<a href="RiText/words_/index.html">words()</a>	<br>

		<br>

		<a href="RiText/RiText.createLetters_/index.html">RiText.createLetters()</a><br>
		<a href="RiText/RiText.createLines_/index.html">RiText.createLines()</a><br>
		<a href="RiText/RiText.createWords_/index.html">RiText.createWords()</a><br>
		<a href="RiText/RiText.defaultFill_/index.html">RiText.defaultFill()</a><br>
		<a href="RiText/RiText.defaultFont_/index.html">RiText.defaultFont()</a><br>
		<a href="RiText/RiText.defaultFontSize_/index.html">RiText.defaultFontSize()</a><br>
		<a href="RiText/RiText.dispose_/index.html">RiText.dispose()</a><br>
		<a href="RiText/RiText.disposeAll_/index.html">RiText.disposeAll()</a><br>
		<a href="RiText/RiText.drawAll_/index.html">RiText.drawAll()</a><br>
		<a href="RiText/RiText.pauseTimer_/index.html">RiText.pauseTimer()</a> <br>
		<a href="RiText/RiText.picked_/index.html">RiText.picked()</a> <br>
		<a href="RiText/RiText.random_/index.html">RiText.random()</a><br>
		<a href="RiText/RiText.randomColor_/index.html">RiText.randomColor()</a><br>
		<a href="RiText/RiText.stopTimer_/index.html">RiText.stopTimer()</a><br>
		<a href="RiText/RiText.timer_/index.html">RiText.timer()</a> <br>
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
