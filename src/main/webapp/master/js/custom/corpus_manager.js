(function(window, document, $, undefined){

	$(function(){ //BEGIN: document ready
		let fileName = window.location.pathname.split("/").pop();

		//init the corpus manager page
		if(fileName === "corpus_manager.html") {
			console.log("you are in corpus manager");

			let corpusListTable = $("#corpusList").DataTable({
				scrollY: 200,
				ajax: {url: 'http://localhost:8080/v1/corpora/',
					dataSrc: '',
				},

				//select
				aoColumnDefs: [ {
		            orderable: false,
		            className: 'select-checkbox',
		            targets:   0
		        } ],
		        select: {
		            style:    'multi',
		            selector: 'td'
		        },

				columns: [
					{ data: 'id'},
					{ data: 'name' },
					{ data: 'description'},
					{ data: 'createDate'}
					],
				order: [[ 1, "desc" ]],
				dom: '<"html5buttons"B>lTfgitp',
		        buttons: [
		            {extend: 'copy',  className: 'btn-sm' },
		            {extend: 'csv',   className: 'btn-sm' },
		            {extend: 'excel', className: 'btn-sm', title: 'XLS-File'},
		            {extend: 'pdf',   className: 'btn-sm', title: $('title').text() },
		            {extend: 'print', className: 'btn-sm' },
		            //custom button
		            {
		                text: 'My button',
		                action: function ( e, dt, node, config ) {
		                    alert( 'Button activated' );
		                }
		            }
		        ]

			});
			
			//on row click, update the 
//			$('#corpusList')
//			.on('click', 'tr', function () {
//
//		        var data = corpusListTable.row(this).data();
//		        console.log(data);
//		        alert('You clicked on '+data.name+'\'s row' );
//		    })
//		    ;

		}


	}); // END: document ready

})(window, document, window.jQuery);



