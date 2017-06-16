<!DOCTYPE html>
<!--[if lt IE 7]>      <html class="no-js lt-ie9 lt-ie8 lt-ie7"> <![endif]-->
<!--[if IE 7]>         <html class="no-js lt-ie9 lt-ie8"> <![endif]-->
<!--[if IE 8]>         <html class="no-js lt-ie9"> <![endif]-->
<!--[if gt IE 8]><!--> <html class="no-js"> <!--<![endif]-->
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <title>Downloads</title>
  <meta name="description" content="">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <!-- Place favicon.ico and apple-touch-icon.png in the root directory -->
  <link rel="stylesheet" href="css/normalize.css">
  <link rel="stylesheet" href="css/main.css">
  <link rel="stylesheet" href="css/style.css">
  <script src="js/vendor/modernizr-2.6.2.min.js"></script>
  <script type="text/javascript" src="js/vendor/jquery-2.1.4.min.js"></script>
  <script language="javascript" src="js/highlight.js"></script>
  <script>
  jQuery(document).ready(function ($) {
     $('.downloadbox.interactive').hover( function() {
       sizeOnMouseover($(this).find('span.sizes a.mini'));
     },function(){
       sizeOnMouseout($(this).find('span.sizes a.mini'));
     });
    $('.sizes a').mouseover(function () {
      sizeOnMouseover(this);
    });
    $('.sizes a').mouseout(function () {
      sizeOnMouseout(this);
     
    });
    var sizeOnMouseover = function(ele) {
       $(ele).siblings('.sizes a').removeClass("default");
       $(ele).addClass("default");
    }
    var sizeOnMouseout = function(ele) {
        $(ele).parent().children('a.source').removeClass("default");
        $(ele).parent().children('a.mini').addClass("default");
    }
  });
  
  </script>
</head>
<body>
<!--[if lt IE 7]>
<p class="browsehappy">You are using an <b>outdated</b> browser. Please <a href="http://browsehappy.com/">upgrade your browser</a> to improve your experience.</p>
<![endif]-->

<?php include("header.php"); ?>

<div class="gd-section pad-large">
<div class="gd-center pad-large">
<div class="row">
  <div class="col1"></div>
  <div class="col10">
<div id="version-div" height="500">
<a href="https://www.npmjs.com/package/rita"><img src="https://img.shields.io/npm/v/rita.svg" alt="npm version"></a><br>

<h3>Downloads</h3>

    <h4>RiTa library</h4>


<div class="downloadbox">
  <a href="rita.zip"><span></span></a>
 <b> RiTa-complete </b>
  <span>java, javascript, examples, source, etc.</span>
</div>

<div class="row pad-small">
<h4>RiTa.js<span>(js-only)</span></h4>

<!--
<form action="" method="get" class="ritajsform">
 <h4>Build your own</h4>
<div class="formcontent">
<label><input name="Fruit" type="checkbox" value="" />RiMarkov <span>(size)</span></label>
<label><input name="Fruit" type="checkbox" value="" />RiGrammar <span>(size)</span></label>
<label><input name="Fruit" type="checkbox" value="" />RiLexicon <span>(size)</span></label>
<label><input name="Fruit" type="checkbox" value="" />RiLetterToSound<span> (size)</span></label>
<label><input name="Fruit" type="checkbox" value="" /> RiText (deprecated) <span>(size)</span></label>
<div class="button bc">Generate</div>
</div>
</form>
-->

<div class="downloadbox mini interactive">
  <a href="download/rita.min.js" download="rita.min.js"> <span></span></a>
  rita.js
  <span>core (no lexicon, no LTS)</span>
  <span class="sizes">
  <a class="mini default" href="download/rita.min.js" download="rita.min.js">minified 86k</a>  |   
  <a class="source" href="download/rita.js" download="rita.js">source 152k</a> </span>
</div>

<div class="downloadbox mini interactive">
  <a href="download/rita-small.min.js" download="rita-small.min.js"> <span></span></a>
  rita-small.js
  <span>core (1k-word lexicon & lts-rules)</span>
  <span class="sizes">
  <a class="mini default" href="download/rita-small.min.js" download="rita-small.min.js">minified 415k</a>  |  
  <a class="source" href="download/rita-small.js" download="rita-small.js">source 509k</a> </span>
</div>

<div class="downloadbox mini interactive">
  <a href="download/rita-full.min.js" download="rita-full.min.js"> <span></span></a>
  rita-full.js
  <span>complete (w' lexicon & lts-rules)</span>
  <span class="sizes">
  <a class="mini default" href="download/rita-full.min.js" download="rita-full.min.js">minified 1.4m</a>  |  
  <a class="source" href="download/rita-full.js" download="rita-full.js">source 1.6m</a> </span>
</div>

</div>
<!--div class="row pad-small">
  <h4>JS-components</h4>
    <div class="downloadbox mini">
       <a href="download/rita_dict.js" download="rita_dict.js"> <span></span></a>
      rita_dict.js
      <span>the lexicon, 1.1m</span>
    </div>

    <div class="downloadbox mini">
       <a href="download/rita_dict_1000.js" download="rita_dict_1000.js"> <span></span></a>
      rita_dict_1000.js
      <span>the 1k-word lexicon, 32k</span>
    </div>

    <div class="downloadbox mini">
        <a href="http://www.rednoise.org/rita-archive/rita_lts.js" download="rita_lts.js"> <span></span> </a>
      rita_lts.js
    <span>letter-to-sound rules, 301k</span>
    </div>

    <div class="downloadbox mini">
        <a href="http://www.rednoise.org/rita-archive/lancaster.min.js" download="lancaster.min.js"><span></span> </a>
      lancaster stemmer
    <span>alternate stemmer, 8k</span>
  </div>

  <div class="downloadbox mini">
     <a href="http://www.rednoise.org/rita-archive/ritext.min.js" download="ritext.min.js"> <span></span> </a>
    ritext.min.js
  <span>renderer, deprecated, 33k</span>
  </div>
</div-->

<div class="row pad-small">
  <h4>RiTa.java<span>(java-jar only)</span></h4>
  <div class="downloadbox mini smallest"><a href="download/rita.jar"><span></span></a>rita.jar</div>
</div>

<!--div class="row pad-small">
<hr/>
<br>&nbsp;<br>
  <h4>Archived Versions</h4>
  <a href="http://rednoise.org/rita-archive/RiTa-1022.zip">RiTa-alpha.1022.zip</a><br>
  <a href="http://rednoise.org/rita-archive/RiTa.Wordnet-034.zip">RiTa.Wordnet-034.zip</a><br>&nbsp;<br>

  <h4>WordNet-3.1 (osx/unix)</h4>
  <a href="http://rednoise.org/rita-archive/WordNet-3.1.zip">WordNet-3.1.zip</a>

  <h4>Deprecated</h4>
  <a href="http://www.rednoise.org/rita-archive/ritext.min.js">ritext.min.js</a>
</div-->

</div>
</div>
</div>
</div>
</div>

<?php include("footer.php"); ?>

<!--
End Site Content
-->

</body>
</html>
