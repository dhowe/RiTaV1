<?php
  $path = '.';
  $hslashcount = substr_count(__FILE__, '/');
  $pslashcount = substr_count(realpath($_SERVER["SCRIPT_FILENAME"]), '/');
  for ($i=0; $i < $pslashcount - $hslashcount; $i++) {
    $path .= '/..';
  }
?>

<nav class="site-navigation clearfix">
   <div class="logo"><a href="<?php echo $path.'/index.php'; ?>"><img src="<?php echo $path.'/img/RiTaLogo.png'; ?>" alt="RiTa"></a></div>
  <div class="gd-center">
    <ul id="nav" class="menu">

      <li><a href="<?php echo $path.'/index.php'; ?>">Home</a></li>
      <li> <a href="<?php echo $path.'/download.php'; ?>">Download</a> </li>
      <li><a >Learning</a>
      <ul>
        <li><a href="<?php echo $path.'/quickstart.php'; ?>">Quickstart</a></li>
        <li><a href="<?php echo $path.'/tutorial/index.php'; ?>">Tutorials</a></li>
        <li><a href="<?php echo $path.'/examples.php'; ?>">Examples</a></li>
         <li><a href="https://github.com/dhowe/RiTa/wiki/Frequently-Asked-Questions">FAQ</a></li>
      </ul></li>
      <li><a href="<?php echo $path.'/reference/index.php'; ?>">Reference</a></li>
      <li><a href="<?php echo $path.'/gallery.php'; ?>">Gallery</a></li>
      <li><a href="mail.html" onmouseover="this.href='mai' + 'lto:' + 'rita-questions' + '@' + 'rednoise.org?subject=Re: RiTa'">Contact</a></li>
    </ul>

    <a class="toggle-nav" href="#">&#9776;</a>
    <div id="downloadBanner">
    <a class="buttonlink" href="download.php"><span class="button">Download RiTa</span></a>
      <div class="buttons">
      <iframe src="http://ghbtns.com/github-btn.html?user=dhowe&amp;repo=RiTa&amp;type=watch&amp;count=true&amp" allowtransparency="true" frameborder="0" scrolling="0" width="100" height="21"></iframe>
<a href="https://twitter.com/share?ref_src=twsrc%5Etfw" class="twitter-share-button" data-text="I&#39;m coding language with RiTa" data-url="https://rednoise.org/rita/" data-hashtags="RiTa" data-show-count="false">Tweet</a><script async src="https://platform.twitter.com/widgets.js" charset="utf-8"></script>
    </div>
    </div>
    <div class="gitwrap">

       <img id="gitFork" style="" src="https://camo.githubusercontent.com/365986a132ccd6a44c23a9169022c0b5c890c387/68747470733a2f2f73332e616d617a6f6e6177732e636f6d2f6769746875622f726962626f6e732f666f726b6d655f72696768745f7265645f6161303030302e706e67" alt="Fork me on GitHub" data-canonical-src="https://s3.amazonaws.com/github/ribbons/forkme_right_red_aa0000.png">
 <a class="triangleHref" href="https://github.com/dhowe/RiTa/"></a>
     </div>

<script language="javascript">

  function extractPageName(hrefString) {
    var arr = hrefString.split('/');
    return  (arr.length<2) ? hrefString : arr[arr.length-2].toLowerCase() +
      arr[arr.length-1].toLowerCase();
  }

  function setActiveMenu(arr, crtPage) {
    for (var i=0; i<arr.length; i++) {
      if(extractPageName(arr[i].href) == crtPage) {
        if (arr[i].parentNode.tagName != "DIV") {
          arr[i].className = "current";
          arr[i].parentNode.className = "current";
        }
      }
    }
  }

  function showDownloadBanner(page) {
    if (page == extractPageName(document.getElementById("nav").getElementsByTagName("a")[0].href))
        document.getElementById("downloadBanner").className += "visible";
  }

  (function() {
    hrefString = document.location.href ?
      document.location.href : document.location;
    if (document.getElementById("nav")!=null)
      var page = extractPageName(hrefString)
      setActiveMenu(document.getElementById("nav").getElementsByTagName("a"),
        page);
      showDownloadBanner(page);
  })();

</script>
</div>
  </nav>
