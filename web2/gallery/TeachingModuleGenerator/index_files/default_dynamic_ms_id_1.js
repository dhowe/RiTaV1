		
		function ajaxSubmitCommentForm(){
		"use strict";
		
		var options = {
		success: function(){
		$j("#commentform textarea").val("");
		$j("#commentform .success p").text("Comment has been sent!");
		}
		};
		
		$j('#commentform').submit(function() {
		$j(this).find('input[type="submit"]').next('.success').remove();
		$j(this).find('input[type="submit"]').after('<div class="success"><p></p></div>');
		$j(this).ajaxSubmit(options);
		return false;
		});
		}
		var header_height = 100;
		var min_header_height_scroll = 57;
		var min_header_height_fixed_hidden = 50;
		var min_header_height_sticky = 60;
		var scroll_amount_for_sticky = 85;
		var content_line_height = 60;
		var header_bottom_border_weight = 1;
		var scroll_amount_for_fixed_hiding = 200;
		var paspartu_width_init = 0.02;
		var search_header_height = 50;
		
								min_header_height_scroll = 100;
													var logo_height = 130; // stockholm logo height
		var logo_width = 280; // stockholm logo width
						logo_height  = 200;
				logo_width = 486;
					header_top_height = 0;
		
				
				
		var loading_text;
		loading_text = 'Loading new posts...';
		var finished_text;
		finished_text = 'No more posts';
					
			var piechartcolor;
			piechartcolor	= "#e6ae48";
							piechartcolor = "#000000";
						
			var geocoder;
			var map;
			
			function initialize() {
			"use strict";
			// Create an array of styles.
						var mapStyles = [
			{
			stylers: [
			{hue: "#393939" },
			{saturation: "-100"},
			{lightness: "-60"},
			{gamma: 1}
			]
			}
			];
			var qodeMapType = new google.maps.StyledMapType(mapStyles,
			{name: "Qode Map"});
			
			geocoder = new google.maps.Geocoder();
			var latlng = new google.maps.LatLng(-34.397, 150.644);
			var myOptions = {
						zoom: 12,
							scrollwheel: false,
						center: latlng,
			zoomControl: true,
			zoomControlOptions: {
			style: google.maps.ZoomControlStyle.SMALL,
			position: google.maps.ControlPosition.RIGHT_CENTER
			},
			scaleControl: false,
			scaleControlOptions: {
			position: google.maps.ControlPosition.LEFT_CENTER
			},
			streetViewControl: false,
			streetViewControlOptions: {
			position: google.maps.ControlPosition.LEFT_CENTER
			},
			panControl: false,
			panControlOptions: {
			position: google.maps.ControlPosition.LEFT_CENTER
			},
			mapTypeControl: false,
			mapTypeControlOptions: {
			mapTypeIds: [google.maps.MapTypeId.ROADMAP, 'qode_style'],
			style: google.maps.MapTypeControlStyle.HORIZONTAL_BAR,
			position: google.maps.ControlPosition.LEFT_CENTER
			},
										mapTypeId: 'qode_style'
						};
			map = new google.maps.Map(document.getElementById("map_canvas"), myOptions);
							map.mapTypes.set('qode_style', qodeMapType);
						}
			
			function codeAddress(data) {
			"use strict";
			
			if (data === '')
			return;
			
			var contentString = '<div id="content"><div id="siteNotice"></div><div id="bodyContent"><p>'+data+'</p></div></div>';
			var infowindow = new google.maps.InfoWindow({
			content: contentString
			});
			geocoder.geocode( { 'address': data}, function(results, status) {
			if (status === google.maps.GeocoderStatus.OK) {
			map.setCenter(results[0].geometry.location);
			var marker = new google.maps.Marker({
			map: map,
			position: results[0].geometry.location,
							icon:  'http://stockholm8.select-themes.com/wp-content/themes/stockholm/img/pin.png',
						title: data['store_title']
			});
			google.maps.event.addListener(marker, 'click', function() {
			infowindow.open(map,marker);
			});
			}
			});
			}
			
			var $j = jQuery.noConflict();
			
			$j(document).ready(function() {
			"use strict";
			
			showContactMap();
			});
					
		function showContactMap() {
		"use strict";
		
		if($j("#map_canvas").length > 0 && typeof google === 'object'){
		initialize();
		codeAddress("");
		codeAddress("");
		codeAddress("");
		codeAddress("");
		codeAddress("");
		}
		}
		
		var no_ajax_pages = [];
		var qode_root = 'https://arnoldhoogerwerf.net/';
		var theme_root = 'https://arnoldhoogerwerf.net/wp-content/themes/stockholm';
					var header_style_admin = "";
				if(typeof no_ajax_obj !== 'undefined') {
		no_ajax_pages = no_ajax_obj.no_ajax_pages;
		}
		
		