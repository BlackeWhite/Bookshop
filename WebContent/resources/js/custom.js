$(document).ready(function() {
	
	//Codice che passa i filtri come parametri dell'url
	$(".apply_filter").click(function() {
		var url = adv_search_url;
		var order = $("#order_by option:selected").val();
		var genres = "&";
		$("input:checkbox[name=genre_checkbox]:checked").each(function() {
			genres += "genres=" + $(this).attr('id') + "&";
		});

		if ($("#search_term").val() == "") url += "?";
		else url += "?term=" + $("#search_term").val();
		url += genres;
		url += "order_by=" + order;
		window.location.href = url;
	});
	
	
	//Codice che imposta i filtri a quelli presenti nell'url (altrimenti vengono resettati graficamente)
	let searchParams = new URLSearchParams(window.location.search)
	if(searchParams.has('order_by')) {
		var op_id = searchParams.get('order_by');
		var text = $("#"+op_id).text();
		$("#"+op_id).attr("selected", "selected");
		$("#order_by").next(".nice-select").children(".current").text(text);
	} else {
		$("#order_by").next(".nice-select").children(".current").text("Titolo");
	}
	if(searchParams.has('genres')) {
		var genres = searchParams.getAll('genres');
		for(var i = 0; i<genres.length; i++) {
			$("#"+genres[i]).prop("checked", true);
		}
	}

});
