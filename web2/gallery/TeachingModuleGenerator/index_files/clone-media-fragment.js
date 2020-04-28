function cloneMediaFragment() {
  // Check that the fragment is a Media Fragment (starts with t=)
  if(window.location.hash && window.location.hash.match(/^#t=/)) {
    // Find any video and audio tags on the page
    document.querySelectorAll("video,audio").forEach(function(el){
      // Create a virtual element to use as a URI parser
      var parser = document.createElement('a');
      parser.href = el.currentSrc;
      // Replace the hash 
      parser.hash = window.location.hash;
      // Set the src of the video/audio tag to the full URL
      el.src = parser.href;
    });
  }
}

document.addEventListener("DOMContentLoaded", function() {
  cloneMediaFragment();
  // When the media is paused, update the fragment of the page
  document.querySelectorAll("video,audio").forEach(function(el) { 
    el.addEventListener("pause", function(event) {
      // Update the media fragment to the current time
      // Use replaceState to avoid triggering the "hashchange" listener above
      history.replaceState(null, null, "#t=" + Math.round(event.target.currentTime));
    });
  });
});

// If the user changes the hash manually, clone the fragment to the media URLs
window.addEventListener("hashchange", cloneMediaFragment);
