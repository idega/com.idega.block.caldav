// Copyright 2011 Idega. All Rights Reserved.

var RemoteLoginHelper = {};

/**
 * @fileoverview Description of file, its uses and information
 * about its dependencies.
 * @author martynas@idega.com (Martynas Stake)
 */


//Man reikia:
//1 - funkcijos, kuri prilogintu prie Bedework loginimo metu
//2 - funkcijos, kuri sukurtu Iframe su public event kurimu
//3 - funkcijos, kuri sukurtu iframe su private event kurimu


/**
 * Creates login form to login to Bedework.
 * @param {string} j_username Username passed by user.
 * @param {string} j_password Password passed by user.
 * @return {boolean} True if logged in, false otherwise.
 */
RemoteLoginHelper.loginToBedework = function(containerId) {
	var path = "http://bedework.sidan.is/caladmin/j_security_check";
	var j_username = jQuery('#username').attr("value");
	var j_password = jQuery('#password').attr("value");
		
    jQuery('#' + containerId).append(
    		"<iframe src='", path+"?j_username="+j_username+"&j_password="+
            j_password+"&j_security_check=login' ></iframe>");
    return true;
}

RemoteLoginHelper.initiateRemoteLogin = function(containerId) {
	jQuery.each(jQuery('input[type=\'submit\']'), function() {
		  var inputSubmit = jQuery(this);
		  inputSubmit.click(function() {
			  RemoteLoginHelper.loginToBedework(containerId);
		  });
		});
	
	jQuery.each(jQuery('a.loginButton'), function() {
		  var linkLoginButton = jQuery(this);
		  linkLoginButton.click(function() {
			  RemoteLoginHelper.loginToBedework(containerId);
		  });
		});
}