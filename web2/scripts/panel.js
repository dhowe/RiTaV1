
$(document).ready(function(){

  var $myGroup = $('.panel-collapse');

$myGroup.on('show.bs.collapse', function () {

 $('.collapse.in').collapse('hide');

   $(this).siblings('.panel-heading').addClass('active');

 });

 $myGroup.on('hide.bs.collapse', function () {
   $(this).siblings('.panel-heading').removeClass('active');
   console.log('.panel-heading deactive');
 });



});
