/**
 * manage quantity increment and decrement in cart
 */
$(document).ready(function() {
	$(".decrement").click(function() {
		var book_id = $(this).attr("id");
		//ajax request
        $.ajax({
            type: 'POST',
            url: cart_url,
			data:{"b_id":book_id},
			//contentType: 'application/json',
            //dataType: "json", //The type of data that you're expecting back from the server
            processData: false });
	});
	/*$(".increment").click(function() {
		//ajax request
		$.ajax({
	        type: "POST",
	        url: "http://localhost:8080/bookshop/cart",
	        dataType: "json",
	        success: function (data) {
                $("#" + id).parent().find('#error_' + id).html(' ');
                $("#" + id).parent().find('#error_' + id).html(getErrorHtml(errMsgs[id]));
	            },
	        contentType: false,
	        processData: false });
	});*/

});	
	
	
	