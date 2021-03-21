$(document).ready(function() {

	// per l'analisi dei dati del singolo libro
	$("#books").change(function() {
		var b = document.querySelector('#books');

		$.ajax({
			type: 'POST',
			url: change_book,
			data: JSON.stringify({ "bookID": b.value }), // id del libro selezionato
			contentType: 'application/json',
			dataType: "json", //The type of data that you're expecting back from the server
			success: function(data) {
				$('#title_book').html("Titolo del libro: " + data["title"]);
				$('#copie_sold_book').html("Copie vendute: " + data["soldcopies"]);
				$('#total_earn').html("Incasso totale libro: &euro;" + data["totearn"]);
			},
			error: function(e) {
				popupMessage(e);
			},
			processData: false
		});
	});

    // per l'analisi dei dati in un intervallo di tempo 
	$("#submit_data").on('click', function() {
		var date_da = new Date($('#dateda').val());
		var day = date_da.getDate();
		var month = date_da.getMonth() + 1;
		var year = date_da.getFullYear();
		var dateda_format = year + "/" + month + "/" + day; // for sql format 

		var date_a = new Date($('#datea').val());
		var day = date_a.getDate();
		var month = date_a.getMonth() + 1;
		var year = date_a.getFullYear();
		var datea_format = year + "/" + month + "/" + day; // for sql format 
		if (date_a >= date_da) { // verifico che l'intervallo inserito sia corretto'
		$.ajax({
			type: 'POST',
			url: range_data,
			data: JSON.stringify({ "arg2": dateda_format, "arg3": datea_format }),
			contentType: 'application/json',
			dataType: "json", //The type of data that you're expecting back from the server
			success: function(data) {
				$('#copie_sold_book_data').html("Copie vendute: " + data["soldcopies"]);
				$('#total_earn_data').html("Incasso totale: &euro;" + data["totearn"]);
			},
			error: function(e) {
				popupMessage(e);
			},
			processData: false
		});}
		else {
			alert("Inserisci un intervallo di tempo corretto")
			$('#dateda').val('') 
			$('#datea').val('')
		}
	});
	
});