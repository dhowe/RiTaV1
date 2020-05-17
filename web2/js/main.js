$(document).ready(function() {

  //Learning -- Tutorials -- Panels

  var $myGroup = $('.panel-collapse');

  $myGroup.on('show.bs.collapse', function() {

    $('.collapse.in').collapse('hide');

    $(this).siblings('.panel-heading').addClass('active');

  });

  $myGroup.on('hide.bs.collapse', function() {
    $(this).siblings('.panel-heading').removeClass('active');
    console.log('.panel-heading deactive');
  });

  //Overall - animation
  new WOW().init();

  //Gallery - animation
  $(".gallery_home_item").each(function(index) {
    $(this).attr("data-wow-delay", Math.random() * 0.5 + "s");
  })

  //menubar - smoothscroll
  $('.nav-wrapper a[href^="#"]').on('click', function(e) {
    e.preventDefault();
    $(document).off("scroll");

    $('a').each(function() {
      $(this).removeClass('active');
    })
    $(this).addClass('active');

    var target = this.hash,
      menu = target;
    $target = $(target);
    $('html, body').stop().animate({
      'scrollTop': $target.offset().top + 2
    }, 500, 'swing', function() {
      window.location.hash = target;
    });
  });


  menuBar();

  //Gallery and Learning - slick-slider
  $('.slick-slider-gallery').slick({
    dots: true,
  });

  $('.slick-slider-learning').slick({
    dots: true,
  });
  /*
  var firstTime = true;
  $('#dlbtn').click(function(e) {

      console.log( "yes" + $('#download-section.collapse.in').length);
      console.log( "No" + $('#download-section').length);
    //  if(!$('#download-section.collapse.in').length){
    e.preventDefault();
    if (firstTime) {
      $('html, body').stop().animate({
        'scrollTop': $('#description-wrapper').offset().top - 100
      }, 500, 'swing', function() {
        window.location.hash = $('#description-wrapper');
      });
    } else {
      $('html, body').stop().animate({
        'scrollTop': $('#download-section').offset().top - 100
      }, 100, 'swing', function() {
        window.location.hash = $('#download-section');
      });
    }
    //  }
  //  firstTime = false;
//    $('#dlbtn').click(false);
  });
*/
});

(function($) {
  $.scrollToElement = function($element, speed) {

    speed = speed || 750;

    $("html, body").animate({
      scrollTop: $element.offset().top,
      scrollLeft: $element.offset().left
    }, speed);
    return $element;
  };

  $.fn.scrollTo = function(speed) {
    speed = speed || "normal";
    return $.scrollToElement(this, speed);
  };
})(jQuery);


function menuBar() {

  $(".menu-icon-mobile").click(function(e){
    e.preventDefault();
    $(".nav-wrapper").toggleClass("mobile-menu-active");
    $(".navbar-sticky").toggleClass("mobile-menu-active");
  });


  window.addEventListener("load", function() {
    var t = document.querySelector(".page-header").clientHeight,
      r = document.querySelector(".navbar-sticky"),
      n = 300;

    function scroll() {
      if (window.scrollY > t - n) return r.classList.add("visible");
      r.classList.remove("visible")
    }
    scroll(),
      window.addEventListener("scroll", function() {
        scroll()
      }),

      document.querySelectorAll(".nav-wrapper a").forEach(function(e) {
        var t = e.getAttribute("href") || "",
          r = "#" === t[0];
        r && e.addEventListener("click", function(t) {
          $(".nav-wrapper").toggleClass("mobile-menu-active");
          $(".navbar-sticky").toggleClass("mobile-menu-active");
                      t.preventDefault();
          if (e.scrollIntoView) {
            var r = document.querySelector(e.getAttribute("href"));
            r && r.scrollIntoView({
              block: "start",
              behavior: "smooth"
            })
          }
        })
      })
  })
}
