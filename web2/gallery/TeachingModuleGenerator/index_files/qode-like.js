var $j = jQuery.noConflict();

$j(document).ready(function ($) {
	initQodeLike();
});

function initQodeLike() {
	"use strict";
	
	$j(document).on('click', '.qode-like', function () {
		var $likeLink = $j(this),
			$id = $likeLink.attr('id');
		
		if ($likeLink.hasClass('liked')) {
			return false;
		}
		
		var $type = '';
		if (typeof $likeLink.data('type') !== 'undefined') {
			$type = $likeLink.data('type');
		}
		
		var $dataToPass = {
			action: 'stockholm_qode_like',
			likes_id: $id,
			type: $type
		};
		
		var like = $j.post(qodeLike.ajaxurl, $dataToPass, function (data) {
			$likeLink.html(data).addClass('liked').attr('title', 'You already like this!');
			
			if ($type !== 'portfolio_list') {
				$likeLink.find('span').css('opacity', 1);
			}
		});
		
		return false;
	});
}