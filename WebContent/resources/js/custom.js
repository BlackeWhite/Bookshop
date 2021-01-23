$(document).ready(function() {
	$("#order_by").change(function() {
		var url = window.location.hostname + "/advanced_search?" + window.location.pathname;
		var order = $("#order_by option:selected").val();
		var genres = "&";
		$("input:checkbox[name=genre_checkbox]:checked").each(function(){
			genres += "genres=" + $(this).attr('id') + "&";
		});
		
		url += genres;
		url += "order_by=" + order;
		window.location.href = url;
	});
});
