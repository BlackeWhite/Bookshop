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
		url += "&price_min=" + currMin;
		url += "&price_max=" + currMax;
		url += "&books_per_page=" + max_elems;
		window.location.href = url;
	});

	$(".reset_filter").click(function() {
		window.location.href = adv_search_url;
	});

	//Password confirmation
	$("#password-confirm").focusout(function() {
		if ($(this).val() != $("#password").val()) {
			$("#error-pass").remove();
			$(this).after('<span id="error-pass" class="validation-error">Le password non coincidono.</span>');
		} else $("#error-pass").remove();
	});
	$(".with-pass-conf").click(function(e) {
		if ($("#password-confirm").val() != $("#password").val()) {
			$("#error-pass").remove();
			$("#password-confirm").after('<span id="error-pass" class="validation-error">Le password non coincidono.</span>');
			e.preventDefault();
		}
	});

	//Funzione per gestire il mini carrello a comparsa
	$(".add_to_cart").click(function() {
		var id = $(this).attr("data-book");
		var amount = $("#amount_" + id).val();
		$.ajax({
			type: 'POST',
			url: add_cart_url,
			data: JSON.stringify({ "bookID": id, "arg2": amount }),
			contentType: 'application/json',
			dataType: "json", //The type of data that you're expecting back from the server
			success: function(data) {
				if (data["operation"] == "update") {
					$("#cart_" + data["bookID"]).children("p")
						.text("Copie: " + data["copies"] + " - Totale: " + data["elemTotalPrice"]);
				} else {
					$(".shopping-list").append('<li id="cart_' + data["bookID"] + '"><a' +
						' data-book="' + data["bookID"] + '" class="remove"' +
						' title="Elimina questo elemento"><i class="fa fa-remove"></i></a>' +
						'<a style="border: none" class="cart-img" href="' + show_book_url + '/' + data["bookID"] +
						'"><img style="object-fit:contain; border: none" src="' +
						resources_url + '/img/' + data["cover"] + '" alt="#"></a>' +
						'<h4><a href="' + show_book_url + '/' + data["bookID"] + '">' +
						data["title"] + '</a></h4><p class="quantity">' +
						"Copie: " + data["copies"] + " - Totale: " + data["elemTotalPrice"] +
						'</p></li>');
				}
				$(".total-amount").text(data["cartTotalPrice"]);
				$(".total-count").text(data["cartTotalItems"]);
				$(".total-count-text").text(data["cartTotalItems"] + " ELEMENTI");
				if ($(".mini-checkout").length == 0) {
					$(".total").after('<a href="' + checkout_url + '" ' +
						'class="btn animate mini-checkout">Procedi al pagamento</a>')
				}
			},
			error: function(e) {
				alert("Non ci sono abbastanza copie disponibili");
			},
			processData: false
		});
	});

	//Funzione per rimuovere un libro dal mini carrello
	$(".shopping-list").on('click', '.remove', function() {
		var id = $(this).attr("data-book");
		$.ajax({
			type: 'POST',
			url: cart_url,
			data: JSON.stringify({ "bookID": id, "arg2": "delete" }),
			contentType: 'application/json',
			dataType: "json", //The type of data that you're expecting back from the server
			success: function(data) {
				$("#cart_" + id).remove();
				$(".total-amount").text(data["response2"]);
				$(".total-count").text(data["response3"]);
				$(".total-count-text").text(data["response3"] + " ELEMENTI");
				if (data["response3"] == "0") {
					$(".mini-checkout").remove();
				}
			},
			processData: false
		});
	});

	//Funzione per eliminare una carta di pagamento
	$(".remove-card").click(function() {
		var id = $(this).attr("data-card");
		if (confirm("Sei sicuro di voler eliminare la carta?")) {
			$.ajax({
				type: 'POST',
				url: delete_card_url,
				data: JSON.stringify({ "cardId": id }),
				contentType: 'application/json',
				dataType: "text", //The type of data that you're expecting back from the server
				success: function(data) {
					$("#card_" + id).remove();
				},
				processData: false
			});
		}
	});

	//Funzione per rimuovere un utente dalla lista di venditori o utenti standard
	$(".remove-user").click(function() {
		var username = $(this).attr("data-user");
		if (confirm("Sei sicuro di voler eliminare questo utente?")) {
			$.ajax({
				type: 'POST',
				url: delete_user_url,
				data: username,
				contentType: 'text/plain',
				dataType: "text", //The type of data that you're expecting back from the server
				success: function(data) {
					$("#user_" + username).remove();
				},
				processData: false
			});
		}
	});

	let searchParams = new URLSearchParams(window.location.search)

	// Codice paginazione generalizzata
	$(".page-link").click(function() {
		searchParams.delete("page");
		var url = window.location.href.split('?')[0];
		if (searchParams.toString() != "") url += "?" + searchParams.toString();
		url += url.includes("?") ? "&" : "?";
		url += "page=" + $(this).attr("data-page");
		window.location.href = url;
	});


	// Codice paginazione per la home
	$(".page-link-home").click(function() {
		searchParams.delete("page");
		var url = home_url;
		if (searchParams.toString() != "") url += "?" + searchParams.toString();
		url += url.includes("?") ? "&" : "?";
		url += "page=" + $(this).attr("data-page");
		url += "#moreview";
		window.location.href = url;
	});

	//Codice che imposta i filtri a quelli presenti nell'url (altrimenti vengono resettati graficamente)
	if (searchParams.has('order_by')) {
		let op_id = searchParams.get('order_by');
		let text = $("#" + op_id).text();
		$("#" + op_id).attr("selected", "selected");
		$("#order_by").next(".nice-select").children(".current").text(text);
	} else {
		$("#order_by").next(".nice-select").children(".current").text("Titolo");
	}

	if (searchParams.has('books_per_page')) {
		let op_id = searchParams.get('books_per_page');
		let text = $("#" + op_id + "_op").text();
		$("#" + op_id + "_op").attr("selected", "selected");
		$("#books_per_page").next(".nice-select").children(".current").text(text);
	} else {
		$("#books_per_page").next(".nice-select").children(".current").text("06");
	}

	if (searchParams.has('search_by')) {
		let op_id = searchParams.get('search_by');
		let text = $("#" + op_id).text();
		$("#" + op_id).attr("selected", "selected");
		$("#search_by").next(".nice-select").children(".current").text(text);
	} else {
		$("#search_by").next(".nice-select").children(".current").text("Titolo");
	}


	if (searchParams.has('genres')) {
		let genres = searchParams.getAll('genres');
		for (let i = 0; i < genres.length; i++) {
			$("#" + genres[i]).prop("checked", true);
		}
	}

});
