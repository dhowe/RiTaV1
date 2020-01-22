$(document).ready(function() {
	$(".galleryThumbnail").live("click", function() {
		var i = $(this).index();
		$(".galleryItem").addClass("hidden").eq(i).removeClass("hidden");
	});
});