
$(document).ready(function(){
$('.panel-collapse').on('show.bs.collapse', function () {
   $(this).siblings('.panel-heading').addClass('active');
   console.log('.panel-heading');
 });

 $('.panel-collapse').on('hide.bs.collapse', function () {
   $(this).siblings('.panel-heading').removeClass('active');
   console.log('.panel-heading deactive');
 });
});
