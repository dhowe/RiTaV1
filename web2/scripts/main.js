$(document).ready(function() {

  //Learning -- Tutorials -- Panels

  var $panelGroup = $('.panel-collapse');

  $panelGroup.on('show.bs.collapse', function() {

    $('.collapse.in').collapse('hide');

    $(this).siblings('.panel-heading').addClass('active');

  });

  $panelGroup.on('hide.bs.collapse', function() {
    $(this).siblings('.panel-heading').removeClass('active');
    console.log('.panel-heading deactive');
  });

  //Overall - animation
  new WOW().init();


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


  //Learning - slick-slider
  $('.slick-slider-learning').not('.slick-initialized').slick({
    dots: true,
  });



});
