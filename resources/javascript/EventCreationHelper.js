EventCreationHelper = {
		showPublicEventCreationWindow : function() {
			jQuery('a.invisibleLink').attr("href", "http://bedework.sidan.is/caladmin/event/initAddEvent.do?b=de");
			jQuery('a.invisibleLink').trigger('click');
		}

		,showPrivateEventCreationWindow : function() {
			jQuery('a.invisibleLink').attr("href", "http://bedework.sidan.is/ucal");
			jQuery('a.invisibleLink').trigger('click');
		}
		
		,showPublicEventsWindow : function() {
			jQuery('a.invisibleLink').attr("href", "http://bedework.sidan.is/cal/main/showMain.rdo");
			jQuery('a.invisibleLink').trigger('click');
		}
		
		,initializeHiddenLink : function(){
			if (jQuery('a.invisibleLink').length == 0)
				jQuery(document.body).append("<a class='invisibleLink iframe' style='display: none;'></a>");
			
			jQuery("a.invisibleLink").fancybox({
				hideOnContentClick: false
				,zoomSpeedIn: 300
				,zoomSpeedOut: 300
				,overlayShow: true
				,width: windowinfo.getWindowWidth() * 0.8
				,height: windowinfo.getWindowHeight() * 0.8
				,autoDimensions: false
				,onComplete: function() {}
				,onClosed:function() {}
			});
		}
}