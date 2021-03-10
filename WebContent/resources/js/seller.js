$(document).ready(function() {



	// per l'analisi dei dati del singolo libro
	$("#books").change(function() {
		var b = document.querySelector('#books');

		$.ajax({
			type: 'POST',
			url: change_book,
			data: JSON.stringify({ "bookID": b.value }),
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
			alert("Inserisci un intervallo di data corretto")
			$('#dateda').val('') 
			$('#datea').val('')
		}
	});
	

	function removerows(tablebody) {
		var rows = tablebody.getElementsByTagName("tr");
		while (rows.length)
			rows[0].parentNode.removeChild(rows[0]);
	}

	function addrows(tablebody, n) {
		for (var i = 0; i < n; i++) {
			var j = i + 1;
			var row = document.createElement("tr");
			var titlecell = document.createElement("td");
			var bold = document.createElement("strong");
			var br = document.createElement("br");

			title = titlecell.appendChild(document.createTextNode("Autore " + j + ""));
			bold.appendChild(title);
			row.appendChild(bold);

			var cell = document.createElement("td");
			var input = document.createElement("input");
			var input_surname = document.createElement("input");

			input.setAttribute("id", "authorsName");
			input.setAttribute("name", "authorsName");
			input.setAttribute("placeholder", "Nome");
			input.setAttribute("type", "text");
			input.setAttribute("required", "required");;

			input_surname.setAttribute("id", "authorsSurname");
			input_surname.setAttribute("name", "authorsSurname");
			input_surname.setAttribute("placeholder", "Cognome");
			input_surname.setAttribute("type", "text");;

			cell.appendChild(input);
			cell.appendChild(input_surname);
			row.appendChild(cell);
			tablebody.appendChild(row);
		}
	}

    function change_select () {
	      var select = document.getElementById("numrows");
	      var index = select.selectedIndex
	      var n = parseInt(select.value);
	      var tablebody = document.getElementById("maintablebody");
	      removerows(tablebody);
	      addrows(tablebody, n);
	    }
});