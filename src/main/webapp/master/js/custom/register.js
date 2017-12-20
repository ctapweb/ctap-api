//Custom jQuery
//----------------------------------- 


(function(window, document, $, undefined){

	$(function(){ //BEGIN: document ready

		//init the register page
		let registerForm = $('#registerForm');
		if(registerForm.length) {
			(function registerInit() {
				registerForm.parsley()
				.on('field:validated', () => {
					var ok = $('.parsley-error').length === 0;
					$('.bs-callout-info').toggleClass('hidden', !ok);
					$('.bs-callout-warning').toggleClass('hidden', ok);
				})
				.on("form:submit", () => {
					console.log("Submitting register form...");
				})
				;
			})();
		}

		//register submit handler
		registerForm.submit((event) => {
			event.preventDefault();

			//get the user input
			let user = {
					firstName: $("#signupInputFirstName").val(),
					lastName: $("#signupInputLastName").val(),
					institution: $("#institution").val(),
					email: $("#signupInputEmail").val(),
					passwd: $("#signupInputPassword1").val(),
			}

			const ajaxRequest = {
					type: "POST",
					url: ctap.apiURL + "/users/",
					processData: false,
					data:JSON.stringify(user),
					contentType: 'application/json; charset=utf-8',
			};

			$.ajax(ajaxRequest)
			.done(() => {
				console.log("done");

//				window.location.href="dashboard.html";
			})
			.fail((jqXHR, textStatus, errorThrown) => {
				console.log("failed");
				console.log(jqXHR);
			})
			;
		});

	}); // END: document ready

})(window, document, window.jQuery);



