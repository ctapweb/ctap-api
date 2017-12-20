(function(window, document, $, undefined){

	$(function(){ //BEGIN: document ready
		
		let fileName = window.location.pathname.split("/").pop();

		//init dashboard page
		if(fileName === "dashboard.html") {
			//check if user logged in
			if(ctap.authString && ctap.authString.length) {
				console.log(ctap.authString);
			} else {
				console.log("User not logged in!");
			}
		}
//		//init the login page
//		if($('#loginForm').length) {
//			(function loginInit() {
//				$('#loginForm').parsley()
//				.on('field:validated', () => {
//					var ok = $('.parsley-error').length === 0;
//					$('.bs-callout-info').toggleClass('hidden', !ok);
//					$('.bs-callout-warning').toggleClass('hidden', ok);
//				})
//				.on("form:submit", () => {
//					console.log("Submitting login form...");
//				})
//				;
//			})();
//		}
//		
//		//check if user logged in
//		if(!ctap.authString) {
//			window.location.href="./login.html";
//		}



	}); // END: document ready

})(window, document, window.jQuery);


