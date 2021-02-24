$(document).ready(function() {
	
	$(".decrement").click(function() {
		var book_id = $(this).attr("id");
		//ajax request
        $.ajax({
            type : 'POST',
            url : cart_url,
			data : JSON.stringify({"bookID" : book_id, "arg2": "minus", "arg3": ""}),
			contentType : 'application/json',
            dataType: "json", //The type of data that you're expecting back from the server
			success: function (data) {
				availabilityCheck(book_id);
				$("#element_total_price"+book_id).find($("span")).text(data["response1"]);
				$("#cart_subtotal").find($("span")).text(data["response2"]);
				$("#checkout_total").find($("span")).text(data["response3"]);
				$("#savings").find($("span")).text(data["response4"]);
                },
			error: function (e) {
			},
            processData : false });
		
	});
	
	$(".increment").click(function() {
		var book_id = $(this).attr("id");
		//ajax request
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
	
	function availabilityCheck (book_id){
		var book_cp = $("#cp_"+book_id).attr("data-max");
		var input = $("#cp_"+book_id).val();
		if (book_cp >= input ) {
			$("#cp_"+book_id+"_error").remove();
		}
	}
	
	$("#pre-checkout").click(function(e) {
		//controllo disponibilità basato su presenza elemento html p 
		//che segnala insufficienza copie
		if (document.querySelectorAll(".copies_error").length) {
			e.preventDefault();
			popupMessage("Attenzione! Disponibilità di copie insufficiente");
		} else { 
			//controllo in caso di disponibilità modificata in fase di acquisto 
			// mentre si era rimasti sulla pagina di carrello
			$.ajax({
            type : 'GET',
            url : copies_check_url,
			contentType : 'application/json',
           	dataType: "json", //The type of data that you're expecting back from the server	
			success: function (data) {
				if (data["response1"]=="available") {
					window.location.href = checkout_url;
				}
			},
			error: function () {
				window.location.href = cart_url;
			},
            processData : false });
		}
	});
	
	// delete implementation
	$(".delete_element").click(function() {
		var delete_id = $(this).attr("id");
		var book_id = delete_id.split("_")[0];
		//alert(cart_element_book_id);
		$("#"+book_id).remove();
		$.ajax({
            type : 'POST',
            url : cart_url,
			data : JSON.stringify({"bookID" : book_id, "arg2": "delete", "arg3": ""}),
			contentType : 'application/json',
           	dataType: "json", //The type of data that you're expecting back from the server	
			success: function (data) {
				$("#cart_subtotal").find($("span")).text(data["response1"]);
				$("#checkout_total").find($("span")).text(data["response2"]);
				$("#savings").find($("span")).text(data["response4"]);
				if (data["response3"]==0) {
					$(".shopping-cart").html('<p style="text-align:center; font-size:40px">Carrello vuoto.</p>')
				}
			},
            processData : false });
	});
		
});	
