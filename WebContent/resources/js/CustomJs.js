(function($){
	$(document).ready(function() {
		$("#order_by").change(function() {
			alert("PROVA");
			var url = window.location.href;
			var order = $("#order_by option:selected").text();
			url += "?order_by=" + order;
			window.location.href = url;
		});
	});
});(jQuery);