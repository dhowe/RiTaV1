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
<!-- Sharingbutton Twitter -->
<a class="resp-sharing-button__link" href="https://twitter.com/intent/tweet?hashtags=RiTa&original_referer=http%3A%2F%2Flocalhost%3A8000%2Findex.php&ref_src=twsrc%5Etfw&text=I%27m%20coding%20language%20with%20RiTa&tw_p=tweetbutton&url=https%3A%2F%2Frednoise.org%2Frita%2F" target="_blank" aria-label="Twitter">
<div class="resp-sharing-button resp-sharing-button--twitter resp-sharing-button--medium"><div aria-hidden="true" class="resp-sharing-button__icon resp-sharing-button__icon--solid">
<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24"><path d="M23.44 4.83c-.8.37-1.5.38-2.22.02.93-.56.98-.96 1.32-2.02-.88.52-1.86.9-2.9 1.1-.82-.88-2-1.43-3.3-1.43-2.5 0-4.55 2.04-4.55 4.54 0 .36.03.7.1 1.04-3.77-.2-7.12-2-9.36-4.75-.4.67-.6 1.45-.6 2.3 0 1.56.8 2.95 2 3.77-.74-.03-1.44-.23-2.05-.57v.06c0 2.2 1.56 4.03 3.64 4.44-.67.2-1.37.2-2.06.08.58 1.8 2.26 3.12 4.25 3.16C5.78 18.1 3.37 18.74 1 18.46c2 1.3 4.4 2.04 6.97 2.04 8.35 0 12.92-6.92 12.92-12.93 0-.2 0-.4-.02-.6.9-.63 1.96-1.22 2.56-2.14z"/></svg></div>Twitter</div>
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
