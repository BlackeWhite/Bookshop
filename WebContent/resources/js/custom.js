$(document).ready(function() {
	
	//Codice che passa i filtri come parametri dell'url
	$(".apply_filter").click(function() {
		var url = adv_search_url;
		var order = $("#order_by option:selected").val();
		var max_elems = $("#books_per_page option:selected").val();
		var search_by = $("#search_by option:selected").val();
		var genres = "&";
		$("input:checkbox[name=genre_checkbox]:checked").each(function() {
			genres += "genres=" + $(this).attr('id') + "&";
		});

		if ($("#search_term").val() == "") url += "?";
		else {
			url += "?search_by=" + search_by;
			url += "&term=" + $("#search_term").val();
		}
		url += genres;
		url += "order_by=" + order;
		url +="&price_min=" + currMin;
		url +="&price_max=" + currMax;
		url +="&books_per_page=" + max_elems;
		window.location.href = url;
	});
	
	$(".reset_filter").click(function() {
		window.location.href = adv_search_url;
	});
	
	
	$(".add_to_cart").click(function(){
		var id = $(this).attr("data-book");
		var amount = $("#amount_"+id).val();
		$.ajax({
            type : 'POST',
            url : add_cart_url,
			data : JSON.stringify({ "bookID" : id, "arg2": amount}),
			contentType : 'application/json',
            dataType: "json", //The type of data that you're expecting back from the server
			success: function (data) {
				alert("added");
                },
            processData : false });
	});
	
	let searchParams = new URLSearchParams(window.location.search)
	
	// Codice paginazione
	$(".page-link").click(function() {
		searchParams.delete("page");
		var url = adv_search_url;
		if(searchParams.toString() != "") url += "?" + searchParams.toString();
		url += url.includes("?") ? "&" : "?";
		url += "page="+$(this).attr("data-page");
		window.location.href = url;
	});
	
	//Codice che imposta i filtri a quelli presenti nell'url (altrimenti vengono resettati graficamente)
	if(searchParams.has('order_by')) {
		let op_id = searchParams.get('order_by');
		let text = $("#"+op_id).text();
		$("#"+op_id).attr("selected", "selected");
		$("#order_by").next(".nice-select").children(".current").text(text);
	} else {
		$("#order_by").next(".nice-select").children(".current").text("Titolo");
	}
	
	if(searchParams.has('books_per_page')) {
		let op_id = searchParams.get('books_per_page');
		let text = $("#"+op_id+"_op").text();
		$("#"+op_id+"_op").attr("selected", "selected");
		$("#books_per_page").next(".nice-select").children(".current").text(text);
	} else {
		$("#books_per_page").next(".nice-select").children(".current").text("06");
	}
	
	if(searchParams.has('search_by')) {
		let op_id = searchParams.get('search_by');
		let text = $("#"+op_id).text();
		$("#"+op_id).attr("selected", "selected");
		$("#search_by").next(".nice-select").children(".current").text(text);
	} else {
		$("#search_by").next(".nice-select").children(".current").text("Titolo");
	}
	
	
	if(searchParams.has('genres')) {
		let genres = searchParams.getAll('genres');
		for(let i = 0; i<genres.length; i++) {
			$("#"+genres[i]).prop("checked", true);
		}
	}

});
