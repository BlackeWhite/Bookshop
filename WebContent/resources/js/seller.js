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
				$('#title_book').text("Titolo del libro: " + data["title"]);
				$('#copie_sold_book').text("Copie vendute: " + data["soldcopies"]);
				$('#total_earn').text("Incasso totale libro: " + data["totearn"]);
				
			},
			error: function(e) {
				popupMessage(e);
			},
			processData: false
		});
	});

	
	function removerows (tablebody) {
	      var rows = tablebody.getElementsByTagName("tr");
	      while (rows.length)
	        rows[0].parentNode.removeChild(rows[0]);
	    }
	
	function addrows (tablebody, n) {
		for (var i=0; i<n; i++) {
		    var j = i+1;
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