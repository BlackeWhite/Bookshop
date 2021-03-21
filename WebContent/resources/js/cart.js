$(document).ready(function() {
	
	// Codice per l'eliminazione del messaggio fisso di errore per indisponibilità di copie solo nel caso 
	// di decremento della quantità (data l'eventuale presenza dell'errore per insufficienza di copie non 
	// avrebbe senso utilizzare la funzione anche per l'incremento)
	function availabilityCheck (book_id){
		// l'attributo data-max è valorizzato al numero di copie disponibili del libro 
		var book_cp = $("#cp_"+book_id).attr("data-max"); 
		var input = $("#cp_"+book_id).val();
		if (book_cp >= input ) {
			$("#cp_"+book_id+"_error").remove();
		}
	}
	
	// Codice per la gestione della funzionalità di decremento della quantità di un prodotto nel carrello
	$(".decrement").click(function() {
		var book_id = $(this).attr("id");
		// Richiesta AJAX per il controllo valore quantità da aggiornare e l''eventuale aggiornamento dei 
		// dati relativi al carrello in maniera persistente
        $.ajax({
            type : 'POST',
            url : cart_url,
			data : JSON.stringify({"bookID" : book_id, "arg2": "minus", "arg3": ""}),
			contentType : 'application/json',
            dataType: "json", 
			success: function (data) {
				availabilityCheck(book_id);
				$("#element_total_price"+book_id).find($("span")).text(data["response1"]);
				$("#cart_subtotal").find($("span")).text(data["response2"]);
				$("#checkout_total").find($("span")).text(data["response3"]);
				$("#savings").find($("span")).text(data["response4"]);
                },
			error: function (e) {
				// Si verifica quando il numero di copie richieste è uguale a 1 e non è possbile decrementare 
				// ulteriormente la quantità
			},
            processData : false });
	});
	
	// Codice per la gestione della funzionalità di incremento della quantità di un prodotto nel carrello
	$(".increment").click(function() {
		var book_id = $(this).attr("id");
		// Richiesta AJAX per i controlli sulla disponibilità e l'eventuale aggiornamento dei dati relativi al carrello
		// in maniera persistente
        $.ajax({
            type : 'POST',
            url : cart_url,
			data : JSON.stringify({"bookID" : book_id, "arg2": "plus", "arg3": ""}),
			contentType : 'application/json',
           	dataType: "json", //The type of data that you're expecting back from the server	
			success: function (data) { 
				$("#element_total_price"+book_id).find($("span")).text(data["response1"]);
				$("#cart_subtotal").find($("span")).text(data["response2"]);
				$("#checkout_total").find($("span")).text(data["response3"]);
				$("#savings").find($("span")).text(data["response4"]);
                },
			error: function (e) {
				popupMessage("Non ci sono abbastanza copie disponibili");
			},
            processData : false });
	});
	
	// Codice per il controllo sulla correttezza della quantità richieste in funzione dell disponibilità basato 
	// sulla presenza dell''elemento html p che segnala insufficienza copie; ulteriori controlli sono eseguiti
	// all'accesso alla pagina di checkout tramite il controller considerando i dati presenti nel db 
	$("#pre-checkout").click(function(e) {
		//
		if (document.querySelectorAll(".copies_error").length) {
			e.preventDefault();
			popupMessage("Attenzione! Disponibilità di copie insufficiente");
		}
		if (document.querySelectorAll(".removed_error").length) {
			e.preventDefault();
			popupMessage("Attenzione! Uno o più libri non più in vendita");
		}
	});
	
	// Codice per la gestione dell'eliminazione di un elemento nel carrello
	$(".delete_element").click(function() {
		var delete_id = $(this).attr("id");
		var book_id = delete_id.split("_")[0];
		// Eliminazione dell'elemento nel documento html 
		$("#"+book_id).remove();
		$.ajax({
            type : 'POST',
            url : cart_url,
			data : JSON.stringify({"bookID" : book_id, "arg2": "delete", "arg3": ""}),
			contentType : 'application/json',
           	dataType: "json", 
			//The type of data that you're expecting back from the server	
			success: function (data) {
				$("#cart_subtotal").find($("span")).text(data["response1"]);
				$("#checkout_total").find($("span")).text(data["response2"]);
				$("#savings").find($("span")).text(data["response4"]);
				// Caso in cui si elimina l'unico elemento nel carrello
				if (data["response3"]==0) {
					$(".shopping-cart").html('<p style="text-align:center; font-size:40px">Carrello vuoto.</p>')
				}
			},
			error: function (e) {
				// In caso di errore ritornato dal controller in fase di eliminazione
				window.location.href = cart_url;
			},
            processData : false });
	});
		
});	
