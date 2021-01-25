$(document).ready(function() {
	$(".apply_filter").click(function() {
		var url = adv_search_url;
		var order = $("#order_by option:selected").val();
		var genres = "&";
		$("input:checkbox[name=genre_checkbox]:checked").each(function(){
			genres += "genres=" + $(this).attr('id') + "&";
		});
		
		if($("#search_term").val() == "") url += "?";
		else url += "?term=" + $("#search_term").val();
		url += genres;
		url += "order_by=" + order;
		window.location.href = url;
	});
});
