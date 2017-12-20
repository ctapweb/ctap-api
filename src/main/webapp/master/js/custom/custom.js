//Custom jQuery
//----------------------------------- 
//a ctap object for storing ctap related information
const ctap = new (function CTAP() {
	this.apiHost = "localhost";
	this.apiPort = "8080";
	this.apiContext = "/v1";
	this.apiURL = "http://" + this.apiHost + ":" + this.apiPort + this.apiContext;
})();

(function(window, document, $, undefined){

	$(function(){ //BEGIN: document ready


	}); // END: document ready

})(window, document, window.jQuery);



