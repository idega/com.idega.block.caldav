var CalDAVHelper = {};

CalDAVHelper.manageChannelSubscription = function(id, path, loadingMessage) {
	showLoadingMessage(loadingMessage);
	CalDavService.setChannelSubscription(path, jQuery('#' + id).attr('checked') == true || jQuery('#' + id).attr('checked') == 'true' || jQuery('#' + id).attr('checked') == 'checked', {
		callback: function(result) {
			closeAllLoadingMessages();
			humanMsg.displayMsg(result.value, {timeout: 1500});
		}
	});
}