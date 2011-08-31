var CalDAVHelper = {};

CalDAVHelper.manageChannelSubscription = function(path, loadingMessage) {
	showLoadingMessage(loadingMessage);
	CalDAVService.setChannelSubscibtion(path, {
		callback: function(result) {
			closeAllLoadingMessages();
			humanMsg.displayMsg(result.value, {timeout: 1500});
		}
	});
}