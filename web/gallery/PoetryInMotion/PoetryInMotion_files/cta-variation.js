/* clear old local storage */
localStorage.removeItem('wp_cta_impressions');

/**
 *  Record Impressions For Each Variation in CTA Object
 *
 * @param JSON ctas : a json string of {'cta':'vid'}
 */
function wp_cta_record_impressions(ctas , page_id) {

	/* Add Impressions to loaded varations*/
	jQuery.ajax({
		type: 'POST',
		url: cta_variation.admin_url,
		data: {
			action: 'wp_cta_record_impressions',
			cta_impressions: ctas,
			page_id: page_id,
		},
		success: function(user_id){
			_inbound.deBugger( 'cta', 'CTA Impressions Recorded');
		},
		error: function(MLHttpRequest, textStatus, errorThrown){

		}

	});

}

/**
 *   Adds Tracking Classes to Links and Forms to CTAs
 * @param OBJECT ctas : object containing {'cta','vid'}
 */
function wp_cta_add_tracking_classes(ctas) {

	jQuery.each( ctas,  function(cta_id,vid) {
		var vid = ctas[cta_id];

		//console.log('CTA '+cta_id+' loads variation:' + vid);
		jQuery('.wp_cta_'+cta_id+'_variation_'+vid).show();

		/* add tracking classes to links and forms */
		var wp_cta_id = '<input type="hidden" name="wp_cta_id" value="' + cta_id + '">';
		var wp_cta_vid = '<input type="hidden" name="wp_cta_vid" value="'+ vid +'">';
		jQuery('#wp_cta_'+cta_id+'_variation_'+vid+' form').each(function(){
			jQuery(this).addClass('wpl-track-me');
			jQuery(this).append(wp_cta_id);
			jQuery(this).append(wp_cta_vid);
		});


		/* add click tracking - get lead cookies */
		var lead_cpt_id = _inbound.Utils.readCookie("wp_lead_id");
		var lead_email = _inbound.Utils.readCookie("wp_lead_email");
		var lead_unique_key = _inbound.Utils.readCookie("wp_lead_uid");


		/* add click tracking  - setup lead data for click event tracking */
		if (typeof (lead_cpt_id) != "undefined" && lead_cpt_id !== null) {
			string = "&wpl_id=" + lead_cpt_id + "&l_type=wplid";
		} else if (typeof (lead_email) != "undefined" && lead_email !== null && lead_email !== "") {
			string = "&wpl_id=" + lead_email + "&l_type=wplemail";;
		} else if (typeof (lead_unique_key) != "undefined" && lead_unique_key !== null && lead_unique_key !== "") {
			string = "&wpl_id=" + lead_unique_key + "&l_type=wpluid";
		} else {
			string = "";
		}

	});
}

function wp_cta_load_variation( cta_id, vid ) {

	/* get loaded ctas */
	var loaded_ctas = _inbound.totalStorage('wp_cta_loaded');
	if (loaded_ctas === null) {
		var loaded_ctas = {};
	}

	/* get cta impressions object */
	var cta_impressions = _inbound.totalStorage('wp_cta_impressions');
	if (cta_impressions === null) {
		var cta_impressions = {};
	}

	/* if variation is pre-defined then immediately load variation*/
	if ( typeof vid != 'undefined' && vid != null && vid != '' ) {

		/* reveal variation */
		_inbound.debug('CTA '+cta_id+' loads variation:' + vid);
		jQuery('.wp_cta_'+cta_id+'_variation_'+vid).show();

		/* set memory and record impression  */
		loaded_ctas[cta_id] = vid;
		cta_impressions[cta_id] = vid;
		_inbound.totalStorage('wp_cta_loaded', loaded_ctas); // store cta data
		_inbound.totalStorage('wp_cta_impressions', cta_impressions); // store cta data

		/* add tracking classes */
		wp_cta_add_tracking_classes( loaded_ctas );

	}
	/* if split testing is disabled then update wp_cta_loaded storage object with variation 0 */
	else if ( parseInt(cta_variation.split_testing) == 0 ) {

		/* update local storage variable */
		loaded_ctas[cta_id] = 0;
		cta_impressions[cta_id] = 0;

		/* update local storage object */
		_inbound.totalStorage('wp_cta_loaded', loaded_ctas); // store cta data
		_inbound.totalStorage('wp_cta_impressions', cta_impressions); // store cta data
		_inbound.deBugger('WP CTA Load Object Updated:' + JSON.stringify(loaded_ctas));

	}
	/* if variation not set yet or sticky cta setting is on  */
	else if ( (cta_id in loaded_ctas) && parseInt(cta_variation.sticky_cta) == 1 ) {
		cta_impressions[cta_id] = loaded_ctas[cta_id];
		_inbound.totalStorage('wp_cta_impressions', cta_impressions); // store cta data

	}
	/* get variation id via AJAX */
	else {
		jQuery.ajax({
			type: "POST",
			url: cta_variation.admin_url,
			dataType: "html",
			async: false, /* required atm */
			data : {
				'action' : 'cta_get_variation',
				'cta_id' : cta_id,
				'fast_ajax': true,
				'load_plugins': ["inbound-pro/inbound-pro.php","_inbound-now/inbound-pro.php","cta/calls-to-action.php"]
			},
			success: function(vid) {
				/* update local storage variable */
				loaded_ctas[cta_id] = vid.trim();

				/* update local storage history and impression objects */
				_inbound.totalStorage('wp_cta_loaded', loaded_ctas); // store cta data
				_inbound.totalStorage('wp_cta_impressions', loaded_ctas); // store cta data
				_inbound.deBugger( 'cta', 'WP CTA Load Object Updated:' + JSON.stringify(loaded_ctas) );
			}
		});
	}
}



jQuery(document).ready(function($) {

	var timeout = 500;

	if( /Android|webOS|iPhone|iPad|iPod|BlackBerry|IEMobile|Opera Mini/i.test(navigator.userAgent) ) {
		timeout = 500;
	}


	if (cta_variation.cta_id > 0) {
		wp_cta_load_variation( cta_variation.cta_id , null );
	}

	setTimeout( function() {

		var ctas = _inbound.totalStorage('wp_cta_loaded');

		if(ctas === null || ctas === "undefined") { return false; }

		/* Add Tracking Classes & Reveal CTAs */
		wp_cta_add_tracking_classes(ctas);

		/* Record Impressions manually when page_tracking is off */
		if (cta_variation.page_tracking == 'off' && inbound_settings.post_type != 'landing-page') {
			wp_cta_record_impressions(ctas , inbound_settings.post_id);
		}

	} , timeout );

});
