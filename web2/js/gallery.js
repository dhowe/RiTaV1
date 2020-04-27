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
    var l = 0;
    for (k = 0; k <= projects.length; k += 9) {
      container = '<div id=\"container' + k / 9 + '\" class=\"container\"></div>';
      $('.slick-slider-gallery').slick('slickAdd', container);
      //$("#gallery-slider").append(container);
      for (i = 0; i < 3; i++) {
        row = '<div id=\"con' + k / 9 + 'row' + i + '\" class=\"row\"></div>';
        $("#container" + k / 9).append(row);
        for (j = 0; j < 3; j++) {
          col = '<div id=\"con' + k / 9 + 'row' + i + 'col' + j + '\" class=\"column\"></div>';
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
          $("#" + "con" + k / 9 + "row" + i).append(col);
          $("#" + "con" + k / 9 + "row" + i + "col" + j).append(html);
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
