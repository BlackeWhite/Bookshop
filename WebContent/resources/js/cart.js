$(document).ready(function() {
	
	$(".decrement").click(function() {
		var book_id = $(this).attr("id");
		//ajax request
        $.ajax({
            type : 'POST',
            url : cart_url,
			data : JSON.stringify({ "bookID" : book_id, "arg2": "minus"}),
			contentType : 'application/json',
            dataType: "json", //The type of data that you're expecting back from the server
			success: function (data) {
				$("#element_total_price"+book_id).text(data["response1"]);
				$("#cart_subtotal").find($("span")).text(data["response2"]);
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
			data : JSON.stringify({ "bookID" : book_id, "arg2": "plus"}),
			contentType : 'application/json',
           	dataType: "json", //The type of data that you're expecting back from the server	
			success: function (data) { 
				$("#element_total_price"+book_id).text(data["response1"]);
				$("#cart_subtotal").find($("span")).text(data["response2"]);
                },
			error: function (e) {
				alert("Non ci sono abbastanza copie disponibili");
			},
            processData : false });
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
			data : JSON.stringify({ "bookID" : book_id, "arg2": "delete"}),
			contentType : 'application/json',
           	dataType: "json", //The type of data that you're expecting back from the server	
			success: function (data) {
				$("#cart_subtotal").find($("span")).text(data["response2"]);
				if (data["response3"]==0) {
					$(".shopping-cart").html('<p style="text-align:center; font-size:40px">Carrello vuoto.</p>')
				}
			},
            processData : false });
	});
		
});	
	
	




	