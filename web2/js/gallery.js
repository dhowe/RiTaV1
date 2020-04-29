$(document).ready(function() {

  getFile();

  function getFile() {
    $.getJSON("gallery.json").done(function(projs) {
      if ($('#gallery-slider').length > 0) {
        createGallery(projs);
        afterGalleryCreated();
      }
    });
  }

  function createGallery(projects) {
    var container;
    var row, col, rowid, colid;
    var a = 3; //row
    var b = 3; //column
    if($( window ).width() < 1000){
       a = 3; //row
       b = 1; //column
    }

    var l = 0;
    for (k = 0; k <= projects.length; k += a*b) {
      container = '<div id=\"container' + k /  a*b + '\" class=\"container\"></div>';
      $('.slick-slider-gallery').slick('slickAdd', container);
      //$("#gallery-slider").append(container);
      for (i = 0; i < a; i++) {
        row = '<div id=\"con' + k /  a*b + 'row' + i + '\" class=\"row\"></div>';
        $("#container" + k /  a*b).append(row);
        for (j = 0; j < b; j++) {
          col = '<div id=\"con' + k /  a*b + 'row' + i + 'col' + j + '\" class=\"column\"></div>';
          if (l < projects.length) {
            var html = "<div class='col-md-4 gallery_home_item wow fadeInDown'>";
            html += "<a href='" + projects[l].link + "' target='new'>";
            html += "<img src='" + projects[l].thumb + "'/></a><br>";
            html += "<a href='" + projects[l].link + "' target='new'>" + projects[l].title + "</a>";
            html += "<p><span>by " + projects[l].artist + "</span></p>";
            html += "</div>";
          } else {
            break;
          }
          l++;
          $("#" + "con" + k /  a*b + "row" + i).append(col);
          $("#" + "con" + k /  a*b + "row" + i + "col" + j).append(html);
          //  console.log("#"+"con"+k/9 + "row" + i + "col" + j)
        }
      }
    }
  }

  function afterGalleryCreated() {

    //Gallery and Learning - slick-slider
    $('.slick-slider-gallery').not('.slick-initialized').slick({
      dots: true,
    });

    //Gallery - animation
    $(".gallery_home_item").each(function(index) {
      $(this).attr("data-wow-delay", Math.random() * 0.5 + "s");
    })
  }

});
