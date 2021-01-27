/**
 * manage quantity increment and decrement in cart
 */

$(document).ready(function() {
	$(".decrement").click(function() {
		var book_id = $(this).attr("id");
		//ajax request
        $.ajax({
            type : 'POST',
            url : cart_url,
			data : JSON.stringify({ "b_id" : book_id, "operation": "minus"}),
			contentType : 'application/json',
            dataType: "json", //The type of data that you're expecting back from the server
			success: function (data) {
				$("#cart_el_total_price"+book_id).text(data["price"]);
                },
            processData : false });
		//var bPrice = $("#"+book_id, ".price").text();
	 	//setCartTotal("minus",bPrice);
	});
	
	
	$(".increment").click(function() {
		var book_id = $(this).attr("id");
		//ajax request
        $.ajax({
            type : 'POST',
            url : cart_url,
			data : JSON.stringify({ "b_id" : book_id, "operation": "plus"}),
			contentType : 'application/json',
           	dataType: "json", //The type of data that you're expecting back from the server	
			success: function (data) { 
				$("#cart_el_total_price"+book_id).text(data["response"]);
                },
            processData : false });
	// var bPrice = getBookPrice;
	//setCartTotal("plus",bPrice);
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
			data : JSON.stringify({ "b_id" : book_id, "operation": "delete"}),
			contentType : 'application/json',
           	dataType: "json", //The type of data that you're expecting back from the server	
            processData : false });
	// var elementTotalPrice = getCartElementTotalPrice;
	// setCartTotal("delete",total);;
	});
		
});	
	
/*function setCartTotal(operation, price) {
	var currentTotal = getCurrentTotal();
	var Total;
	if (operation.localeCompare("minus")==0){
		Total = currentTotal - price;
	}
	else if(operation.localeCompare("plus")==0) {
		Total = currentTotal + price;
	}
	else {
		Total = currentTotal - price;
	}
	return Total;
}

function getCurrentTotal() {
	
}
*/	