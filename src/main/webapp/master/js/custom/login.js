//Custom jQuery
//----------------------------------- 


(function(window, document, $, undefined){

	$(function(){ //BEGIN: document ready


		let fileName = window.location.pathname.split("/").pop();

		//init the login page
		if(fileName === "login.html") {
			$('#loginForm').parsley()
			.on('field:validated', () => {
				var ok = $('.parsley-error').length === 0;
				$('.bs-callout-info').toggleClass('hidden', !ok);
				$('.bs-callout-warning').toggleClass('hidden', ok);
			})
//			.on("form:submit", () => {
//				console.log("Submitting login form...");
//			})
			;
		}

		//login submit handler
		$("#loginForm").submit((event) => {
			event.preventDefault();

			console.log("Trying to login...")
			//get the user input
			const userEmail = $("#loginInputEmail").val();
			const userPasswd = $("#loginInputPasswd").val();
			const authString = "Basic " + btoa(userEmail + ":" + userPasswd);
			const rememberMe = $("#loginCheckboxRememberMe").prop("checked");
			const requestUrl = ctap.apiURL + "/user/login/?rememberMe=" + rememberMe;

//			console.log(requestUrl);

			$("#incorrectFeedback").addClass("hidden");
			const ajaxRequest = {
					type: "GET",
					url: requestUrl,
					beforeSend: (jqXHR, settings) => {
						jqXHR.setRequestHeader("Authorization", authString);
					}
			};

			$.ajax(ajaxRequest)
			.done((data, textStatus, jqXHR) => {
				console.log("Login succeeded! Redirecting to dashboard...");

				window.location.href="./dashboard.html";


			})
			.fail((jqXHR, textStatus, errorThrown) => {
				console.log("Login failed!");

				if(jqXHR.status == 401) { //unauthorized
					$("#incorrectFeedback").removeClass("hidden").text("Incorrect email or password!");
				} else if(jqXHR.readyState == 0){
					$("#incorrectFeedback").removeClass("hidden").text("Unable to connect to server.");
				}

			})
			;
		});

	}); // END: document ready

})(window, document, window.jQuery);



