	/**
	 * Sets/gets the default motionType for all RiTexts
	 * @param {object} motionType
	 * @returns {object} the current default motionType
     */
	RiText.defaultMotionType = function(motionType) {

		if (arguments.length==1) 
			RiText.defaults.motionType = motionType;
		return RiText.defaults.motionType;
	}
	
	/**
	 * Sets/gets the default alignment for all RiTexts
	 * @param {number} align (optional, for sets only)
	 * @returns {number} the current default alignment
     */
	RiText.defaultAlignment = function(align) {

		if (arguments.length==1)
			RiText.defaults.alignment = align;
		return RiText.defaults.alignment;
	}
	
	/**
	 * Sets/gets the default font size for all RiTexts
	 * @param {number} size (optional, for sets only)
	 * @returns {number} the current default font size
     */
	RiText.defaultFontSize = function(size) {

		if (arguments.length==1) 
			RiText.defaults.fontSize = size;
		return RiText.defaults.fontSize;
	}	 

	/**
	 * Sets/gets the default bounding box visibility
	 * @param {boolean} size (optional, for sets only)
	 * @returns {boolean} the current default bounding box visibility
     */
	RiText.defaultBounds = function(value) {
		
		if (arguments.length==1) 
			RiText.defaults.showBounds = value;
		return RiText.defaults.showBounds;
	}	
