
$("#order_by").change(function() {
	var url = window.location.href;
	var order = $("#order_by option:selected").text();
	url += "?order_by=" + order;
	window.location.href = url;
})