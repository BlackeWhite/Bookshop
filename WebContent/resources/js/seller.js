$(document).ready(function() {
	


	// per l'analisi dei dati
	$("#books").change(function() {
		var b = document.querySelector('#books');
		
		$.ajax({
			type: 'POST',
			url: change_book,
			data: JSON.stringify({"bookID": b.value}),
			contentType: 'application/json',
			dataType: "json", //The type of data that you're expecting back from the server
			success: function(data) {
				var title = data["title"] // da finire 
				alert(title);
			},
			error: function(e) {
				popupMessage(e);
			},
			processData: false
		});
	});


	

});