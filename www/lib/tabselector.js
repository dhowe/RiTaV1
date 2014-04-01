document.addEventListener("DOMContentLoaded", function(e) {
  // Add a class to the currently active tab
  var path = location.pathname.substring(0, location.pathname.indexOf('/', 1) + 1);
  if (path) {
    var list = document.querySelectorAll("div.navbar ul li a");
    for (var item in list) {
      item = list[item];
      if (item.href.indexOf(path) != -1) {
        item.className += " selected ";
        break;
      }
    }
  } else {
    var item = document.querySelector("div.navbar ul li a");
    item.className = item.className + " selected ";
  }
  
  // Run the Twitter widget
  jQuery(function($){
    $(".twitter").tweet({
      username: "processingjs",
      join_text: "auto",
      avatar_size: 32,
      count: 5,
      auto_join_text_default: "we said,",
      auto_join_text_ed: "we",
      auto_join_text_ing: "we were",
      auto_join_text_reply: "we replied to",
      auto_join_text_url: "we were checking out",
      loading_text: "loading tweets..."
    });
  });
}, false);
