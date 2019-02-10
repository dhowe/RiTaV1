jQuery(document).ready(function() {
		
			jQuery('.collapse').on('shown.bs.collapse', function(){jQuery(this).parent().find(".fa-plus").removeClass("fa-plus").addClass("fa-minus"); jQuery(this).parent().find(".wpsm_panel-heading").addClass("acc-a"); }).on('hidden.bs.collapse', function(){jQuery(this).parent().find(".fa-minus").removeClass("fa-minus").addClass("fa-plus"); jQuery(this).parent().find(".wpsm_panel-heading").removeClass("acc-a");});
		
		});