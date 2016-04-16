jQuery(document).ready(function ($) {

  $('.toggle-nav').click(function () {
    $(".menu").slideToggle(300);
  });

  //hide Github when scroll down
  $(window).scroll(function () {
    if ($(this).scrollTop() > 350) { //use `this`, not `document`
      $('#gitFork').hide(400);
    }
    if ($(this).scrollTop() < 350) { //use `this`, not `document`
      $('#gitFork').show(400);
    }
  });
});
