(function () {
    var mraid = window.mraid = {};
    
    // constant definitions
    var STATES = mraid.STATES = {
        LOADING : "loading",
        DEFAULT : "default",
        EXPANDED : "expanded",
        RESIZED : "resized",
        HIDDEN : "hidden",
    };
    
    var EVENTS = mraid.EVENTS = {
        ERROR : "error",
        READY : "ready",
        STATECHANGE : "stateChange",
        VIEWABLECHANGE : "viewableChange",
        SIZECHANGE : "sizeChange",
    };
    
    var CLOSE_POSITION = mraid.CLOSE_POSITION = {
    	TOPLEFT : 'top-left',
    	TOPRIGHT : 'top-right',
    	TOPCENTER : 'top-center',
    	CENTER : 'center',
    	BOTTOMLEFT : 'bottom-left',
    	BOTTOMRIGHT : 'bottom-right',
    	BOTTOMCENTER : 'bottom-center',
    };
    
    var FORCE_ORIENTATION = mraid.FORCE_ORIENTATION = {
    	PORTRAIT : 'portrait',
    	LANDSCAPE : 'landscape',
    	NONE : 'none',
    };
     
	var FEATURES = mraid.FEATURES = {
        SMS : 'sms',
        TEL : 'tel',
        CALENDAR : 'calendar',
        STORE_PICTURE : 'storePicture',
        INLINE_VIDEO : 'inlineVideo',
    };
 	/** The set of listeners for Events */
 	var listeners = { };
 	
 	/** holds the ads current viewable state controlled by the SDK **/
 	var viewable = true;
 	
 	/** holds the current placement type controlled by the SDK **/
 	var placementType = 'inline';
 
 	/** holds the current state of the mraid ad. controlled by the SDK **/
    var state = STATES.LOADING;
    
    // local variables for the default position
    var default_pos = mraid.default_pos = {
        offsetX:0,
        offsetY:0,
        height: 300,
        width : 50,
    };

    // local variables for the current position
    var current_pos = mraid.current_pos = {
        offsetX:0,
        offsetY:0,
        height: 300,
        width : 50,
    };

    // local variables for the screen size (includes system reserved area)
    var screen_size = mraid.screen_size = {
        width:300,
        height:50,
    };

    // local variables for the max size (max displayable size, excluding system reserved areas)
    var max_size = mraid.max_size = {
        width:300,
        height:50,
    };

    // local variables for the current orientation properties
	var orientation_props = mraid.orientation_props = {
  		allowOrientationChange : true,
		forceOrientation : FORCE_ORIENTATION.NONE,
	};

    // local variables for the current expandable properties
    var expandable_props = mraid.expandable_props = {
        width:300,
        height:50,
        useCustomClose:false,
        isModal:true,
    };
    
    // local variables for the current resize properties
    var resize_props = mraid.resize_props = {
        width:-1,
        height:-1,
        customClosePosition: CLOSE_POSITION.TOPRIGHT,
        offsetX:0,
        offsetY:0,
        allowOffScreen:true,
    };
    
	var resize_props_set = false;
    
    var supported_features = {
        'sms':false,
        'tel':false,
        'calendar':false,
        'storePicture':false,
        'inlineVideo':false,
    };
    
    var expandPropertyValidators = {
        useCustomClose:function(value) { return (value === true || value === false); },
        width:function(value) { return (!isNaN(value) && value >= 0); },
        height:function(value) { return (!isNaN(value) && value >= 0); },
    };
    
    var resizePropertyValidators = {
        width:function(value) { return (!isNaN(value) && value >= 0); },
        height:function(value) { return (!isNaN(value) && value >= 0); },
        customClosePosition:function(value) { return existsInObj(value, CLOSE_POSITION); },
        offsetX:function(value) { return (!isNaN(value)); },
        offsetY:function(value) { return (!isNaN(value)); },
        allowOffScreen:function(value) { return (value === true || value === false); },
	};
	
	var orientationPropertyValidators = {
        allowOrientationChange:function(value) { return (value === true || value === false); },
		forceOrientation:function(value) { return existsInObj(value,FORCE_ORIENTATION); },
	};
	
	//
	//
    // MRAID 1.0 methods.
    //
    //
    
    // expand properties
    mraid.getExpandProperties = function () {
        return expandable_props;
    };
    
    mraid.setExpandProperties = function (properties){
    	if (properties['isModal'] != undefined) {
       		mraid.fireErrorEvent('tried to set isModal property. This is read only.','setExpandProperties');
    	}
        if (valid(properties, expandPropertyValidators, 'setExpandProperties')) {
            for (var i in properties) {
            	mraid.log('changing expand property '+i+' from '+expandable_props[i]+' to '+properties[i],'setExpandProperties');
                expandable_props[i] = properties[i];
            }
            // always push the current state of the custom close flag to the SDK code
        	psm_mraid_bridge.updateExpandProps(expandable_props.useCustomClose);                   
        }
    };
    
    mraid.useCustomClose = function (value) {
		if (value === true || value === false) {
            mraid.log('changing expand property useCustomClose from '+expandable_props.useCustomClose+' to '+value,'useCustomClose');
        	expandable_props.useCustomClose = value;
            // always push the current state of the custom close flag to the SDK code
        	psm_mraid_bridge.updateExpandProps(expandable_props.useCustomClose);  
        }
        else {
       		mraid.fireErrorEvent('useCustomClose called with invalid value: '+custom,'useCustomClose');
        }
    };

    // getters
    mraid.getState = function () {
        return state;
    };
    
    mraid.getVersion = function () {
        return "2.0";
    };
    
    mraid.isViewable = function () {
        return viewable;
    };
    
    mraid.getPlacementType = function () {
        return placementType;
    }
    
    // event listeners

 	mraid.addEventListener = function( event, listener ) {
   		var handlers = listeners[event];
		if ( handlers == null ) {
        	// no handlers defined yet, set it up
        	listeners[event] = [];
        	handlers = listeners[event];
    	}
 
    	// see if the listener is already present
    	for ( var handler in handlers ) {
       		if ( listener == handler ) {
            	// listener already present, nothing to do
            	return;
        	}
    	}
 
        mraid.log('added new '+event+' listener','addEventListener');
    	// not present yet, go ahead and add it
    	handlers.push( listener );
 	};
 
	mraid.removeEventListener = function( event, listener ) {
    	var handlers = listeners[event];
    	if ( handlers != null ) {
    		// if listener is null, all listeners are removed.
    		if (listener == null) {
    		    mraid.log('removing all listeners for event '+event,'removeEventListener');
    			listeners[event]=null;
    		}
    		else {
				var i = handlers.indexOf(listener);
				if(i != -1) 
				{
    		    	mraid.log('removing listener for '+event,'removeEventListener');
					handlers.splice(i, 1);    
				}
				else
				{
    		    	mraid.fireErrorEvent('attempted to remove listener for '+event+' but did not find it registered.','removeEventListener');
				}
			}
		}
	};
 
    // window management
    mraid.close = function () {
		if (state == STATES.EXPANDED ||
			state == STATES.RESIZED) {
    		mraid.log('executing bridge CLOSE','close');
			// expanded ads will return to normal size.
			psm_mraid_bridge.close();

		}
		else if (state == STATES.DEFAULT) {
    		mraid.log('executing bridge HIDE','close');
			// default ads change to hidden.
			psm_mraid_bridge.hide();
		}
		else if (state == STATES.LOADING || 
				state == STATES.HIDDEN) {
			mraid.fireErrorEvent('close failed because current state prevents closing.','close');
		}
    };
    
    mraid.expand = function (url) {
    	if (placementType != 'inline') {
			mraid.fireErrorEvent('expand not allowed because placement type is not inline.','expand');
    		return;
    	}
		if (state == STATES.DEFAULT ||
			state == STATES.RESIZED) {
    		mraid.log('executing bridge EXPAND','expand');
			// default ads change to hidden.
			psm_mraid_bridge.expand(url, mraid.expandable_props);
		}
		else if (state == STATES.LOADING || 
				state == STATES.HIDDEN ||
				state == STATES.EXPANDED)
		{
			mraid.fireErrorEvent('expand failed because current state prevents expanding.','expand');
		}	
    };
    
    mraid.open = function (url) {
        if (!url) {
            mraid.fireErrorEvent( 'URL is required.', 'open');
        }
        else {
    		mraid.log('opening '+url,'open');

            //For iOS, this technique is required for the "click" to make it out of the UIWebView into Safari
            var a = document.createElement('a');
            a.setAttribute("href", url);
            a.setAttribute("target", "_blank");

            var dispatch = document.createEvent("HTMLEvents");
            dispatch.initEvent("click", true, false);
            a.dispatchEvent(dispatch);

		}
    };
 
 
 	//
 	// MRAID 2.0 methods
 	//
    mraid.setOrientationProperties = function (properties) {
        if (valid(properties, orientationPropertyValidators, 'setOrientationProperties')) {
            for (var i in properties) {
            	mraid.log('changing orientation property '+i+' from '+orientation_props[i]+' to '+properties[i],'setOrientationProperties');
                orientation_props[i] = properties[i];
            }
            // push the latest orientation settings to the container
			psm_mraid_bridge.updateOrientationProperties(orientation_props.allowOrientationChange,orientation_props.forceOrientation);
        }
    };
    
    mraid.getOrientationProperties = function () {
    	return orientation_props;
    };
    
    mraid.setResizeProperties = function (properties) {
        if (valid(properties, resizePropertyValidators, 'setResizeProperties')) {
            for (var i in properties) {
            	mraid.log('changing resize property '+i+' from '+resize_props[i]+' to '+properties[i],'setResizeProperties');
                resize_props[i] = properties[i];
            }       
        }
        resize_props_set = true;
    };
    
    mraid.getResizeProperties = function () {
    	return resize_props;
    };
    
    mraid.resize = function () {
    	if (placementType != 'inline') {
			mraid.fireErrorEvent('resize not allowed because placement type is not inline.','resize');
    		return;
    	}
		if (state == STATES.DEFAULT ||
			state == STATES.RESIZED) {
						
			// making sure that resize props have been set.
			if (resize_props_set == false) {
				mraid.fireErrorEvent('resize not allowed because Resize Properties have not been set.','resize');
				return;
			}
			
			if (resize_props.allowOffScreen) {
				// if allowed to be offscreen, make sure the close region is still visible.
				if (resize_props.offsetY < 0) {
					if (resize_props.customClosePosition == CLOSE_POSITION.TOPLEFT ||
						resize_props.customClosePosition == CLOSE_POSITION.TOPRIGHT ||
						resize_props.customClosePosition == CLOSE_POSITION.TOPCENTER) {
						mraid.fireErrorEvent('resize not allowed because close region would be offscreen.','resize');
						return;
					}
					if (resize_props.offsetY < -((resize_props.height/2)-25) &&
						resize_props.customClosePosition == CLOSE_POSITION.CENTER) {
						mraid.fireErrorEvent('resize not allowed because close region would be offscreen.','resize');
						return;
					}				}
				if (resize_props.offsetX < 0) {
					if (resize_props.customClosePosition == CLOSE_POSITION.TOPLEFT ||
						resize_props.customClosePosition == CLOSE_POSITION.BOTTOMLEFT) {
						mraid.fireErrorEvent('resize not allowed because close region would be offscreen.','resize');
						return;
					}
					if (resize_props.offsetX < -((resize_props.width/2)-25) &&
						resize_props.customClosePosition == CLOSE_POSITION.CENTER) {
						mraid.fireErrorEvent('resize not allowed because close region would be offscreen.','resize');
						return;
					}
				}
				if (resize_props.height + resize_props.offsetY > max_size.height) {
					if (resize_props.customClosePosition == CLOSE_POSITION.BOTTOMLEFT ||
						resize_props.customClosePosition == CLOSE_POSITION.BOTTOMRIGHT ||
						resize_props.customClosePosition == CLOSE_POSITION.BOTTOMCENTER) {
						mraid.fireErrorEvent('resize not allowed because close region would be offscreen.','resize');
						return;
					}
					if (resize_props.offsetY + ((resize_props.height/2)+25) > max_size.height &&
						resize_props.customClosePosition == CLOSE_POSITION.CENTER) {
						mraid.fireErrorEvent('resize not allowed because close region would be offscreen.','resize');
						return;
					}
				}
				if (resize_props.width + resize_props.offsetX > max_size.width) {
					if (resize_props.customClosePosition == CLOSE_POSITION.TOPRIGHT||
						resize_props.customClosePosition == CLOSE_POSITION.BOTTOMRIGHT) {
						mraid.fireErrorEvent('resize not allowed because close region would be offscreen.','resize');
						return;
					}
					if (resize_props.offsetX + ((resize_props.width/2)+25) > max_size.width &&
						resize_props.customClosePosition == CLOSE_POSITION.CENTER) {
						mraid.fireErrorEvent('resize not allowed because close region would be offscreen.','resize');
						return;
					}				}
			}
			else {
				// if not allowed off screen, make sure its not offscreen.
				// if the offset is negative, error
				// if width or height are larger than screen, error.
				if (resize_props.width > max_size.width || 
					resize_props.height > max_size.height ||
					resize_props.offsetX < 0 || 
					resize_props.offsetY < 0) {
					mraid.fireErrorEvent('resize not allowed because width or height is larger than screeen size and offscreen is not allowed.','resize');
					return;
				} 
				
				// if offset+width would be larger than screen, reduce offset
				if (resize_props.width + resize_props.offsetX > max_size.width) {
					resize_props.offsetX = max_size.width-resize_props.width;
				}
				if (resize_props.height + resize_props.offsetY > max_size.height) {
					resize_props.offsetY = max_size.height-resize_props.height;
				}
			}
				
			mraid.log('executing bridge RESIZE','resize');
			
			psm_mraid_bridge.resize(resize_props.width, resize_props.height, resize_props.offsetX, 
									resize_props.offsetY,resize_props.customClosePosition,resize_props.allowOffScreen);
			// reset the resize props
			resize_props_set = false;
		}
		else if (state == STATES.LOADING || 
				state == STATES.HIDDEN ||
				state == STATES.EXPANDED)
		{
			mraid.fireErrorEvent('resize failed because current state prevents resizing.','resize');
		}	
	};
	
	mraid.getCurrentPosition = function () {
		return current_pos;
	};

	mraid.getDefaultPosition = function () {
		return default_pos;
	};
	
	mraid.getMaxSize = function() {
		return max_size;
	};

	mraid.getScreenSize = function() {
		return screen_size;
	};
	
	mraid.supports = function(feature) {
		return supported_features[feature];
	};
	
	mraid.storePicture = function(url) {
		if (!supported_features['storePicture'])
		{
        	mraid.fireErrorEvent('Not supported','storePicture');
        	return;
        }
	};

	mraid.createCalendarEvent = function(parameters) {
		if (!supported_features['calendar'])
		{
		    mraid.fireErrorEvent('Not supported','createCalendarEvent');
		    return;
		}
	};

	mraid.playVideo = function(url) {
		if (!supported_features['playVideo'])
		{
		    mraid.fireErrorEvent('Not supported','playVideo');
		    return;
		}
		mraid.log('executing bridge PLAYVIDEO','playVideo');
		psm_mraid_bridge.playVideo(url);
	};
	

    //
    // private helper methods.
	//
	
	var valid = function(obj, validators, action, full) {
        if (full) {
            if (obj === undefined) {
                mraid.fireErrorEvent('Required object missing.', action);
                return false;
            } else {
                for (var i in validators) {
                    if (obj[i] === undefined) {
                        mraid.fireErrorEvent('Object missing required property ' + i, action);
                        return false;
                    }
                }
            }
        }
        for (var i in obj) {
            if (!validators[i]) {
                mraid.fireErrorEvent('Invalid property specified - ' + i + '.', action);
                return false;
            } else if (!validators[i](obj[i])) {
                mraid.fireErrorEvent('Value of property ' + i + ' is not valid type.', action);
                return false;
            }
        }
        return true;
    };

	var existsInObj = function (value, obj)
	{
	    for (var i in obj) {
            if (value == obj[i]) {
                return true;
            } 
        }
		return false;
	}

    //
    // public helper methods.
	//

	mraid.setCurrentPosition = function (position) {
        for (var i in position) {
            mraid.log('changing current position '+i+' from '+current_pos[i]+' to '+position[i],'setCurrentPosition');
            current_pos[i] = position[i];
        }       
        mraid.fireSizeChangeEvent();
	};

	mraid.setDefaultPosition = function (position) {
        for (var i in position) {
            mraid.log('changing default position '+i+' from '+default_pos[i]+' to '+position[i],'setDefaultPosition');
            default_pos[i] = position[i];
        }       
	};
	
	mraid.setMaxSize = function(size) {
        for (var i in size) {
            mraid.log('changing max size '+i+' from '+max_size[i]+' to '+size[i],'setMaxSize');
            max_size[i] = size[i];
        }       
	};

	mraid.setScreenSize = function(size) {
        for (var i in size) {
            mraid.log('changing screen size '+i+' from '+screen_size[i]+' to '+size[i],'setScreenSize');
            screen_size[i] = size[i];
        }       
	};	
	
	mraid.setViewable = function (shown)
	{
		if (shown != viewable)
		{
			// there is a change in the viewable state.
			viewable = shown;
			mraid.fireViewableChangeEvent();
		} else {
	    	mraid.log('setViewable called with no change to view state','setViewable');
		}
	};

	mraid.setState = function(newState) {
		if (state == STATES.LOADING &&
			newState == STATES.DEFAULT)
		{
			mraid.fireReadyEvent();
		}
 		state = newState;
		mraid.fireStateChangeEvent();
	};

	mraid.setPlacementType = function(newPlacement) {
    	mraid.log('changing placement type to:'+newPlacement,'setPlacementType');
		placementType=newPlacement;
	};

	mraid.setSupports = function(feature, value) {
		supported_features[feature] = value;
	};

	mraid.fireReadyEvent = function() {
		var handlers = listeners["ready"];
		if ( handlers != null ) {
			for ( var i = 0; i < handlers.length; i++ ) {
	    		mraid.log('executing ready event handler','fireReadyEvent');
				handlers[i]();
			}
		}
	
		return "OK";
	};
	
	mraid.fireErrorEvent = function( message, action ) {
		var handlers = listeners["error"];
		if ( handlers != null ) {
			for ( var i = 0; i < handlers.length; i++ ) {
	    		mraid.log('executing error event handler','fireErrorEvent');
				handlers[i]( message, action );
			}
		}
	
		return "OK";
	};
	
	// triggers the viewableChange event with the current viewable value
	mraid.fireViewableChangeEvent = function() {
		var handlers = listeners["viewableChange"];
		if ( handlers != null ) {
			for ( var i = 0; i < handlers.length; i++ ) {
	    		mraid.log('executing viewChange event handler','fireViewableChangeEvent');
				handlers[i]( viewable );
			}
		}
	
		return "OK";
	};
	
	// triggers the sizeChange event with the current size value
	mraid.fireSizeChangeEvent = function() {
		var handlers = listeners["sizeChange"];
		if ( handlers != null ) {
			for ( var i = 0; i < handlers.length; i++ ) {
	    		mraid.log('executing sizeChange event handler','fireSizeChangeEvent');
				handlers[i]( current_pos.width,current_pos.height );
			}
		}
	
		return "OK";
	};
	// triggers the stateChange event with the current state value.
	mraid.fireStateChangeEvent = function() {
		var handlers = listeners["stateChange"];
		if ( handlers != null ) {
			for ( var i = 0; i < handlers.length; i++ ) {
	    		mraid.log('executing stateChange event handler','fireStateChangeEvent');
				handlers[i]( state );
			}
		}
	
		return "OK";
	};

	mraid.log = function(logtext,method) {
        psm_mraid_bridge.log('DEBUG','MRAID.log:'+logtext,method);	
	}

	// initialization		
	// add an event listener to the error event so we can log errors
	// to the device console.
	mraid.addEventListener('error', function(error, method) {
        psm_mraid_bridge.error('DEBUG',error,method);
    });

	mraid.addEventListener('stateChange', function(state) {
        psm_mraid_bridge.log('DEBUG','State changed to '+state,'State change');
    });
    
    mraid.addEventListener('viewableChange', function(viewable) {
        psm_mraid_bridge.log('DEBUG','Viewable changed to '+viewable,'Viewable change');
    });
   
    mraid.addEventListener('sizeChange', function(width, height) {
        psm_mraid_bridge.log('DEBUG','Size changed to '+width+":"+height,'Size change');
    });
 
    psm_mraid_bridge.log('DEBUG','initialized', 'init');
    
})();