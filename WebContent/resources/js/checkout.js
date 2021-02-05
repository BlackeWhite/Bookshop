$(document).ready(function() { 
	var newAddr = 0;
	
	//Inserimento nuovo indirizzo
	$("#newAddress").click(function() {
		$(".shipmentForm").html(
			'<form class=" shipment_form form" method="post">' +
				'<div class="row">' +
					'<div class="col-lg-6 col-md-6 col-12">' +
						'<div class="form-group">' +
							'<label>Città <span>*</span></label>' +
							'<input id="newShipCity" type="text" name="città" placeholder="" >' +
						'</div>' +
					'</div>' +
					'<div class="col-lg-6 col-md-6 col-12">' +
						'<div class="form-group">' +
							'<label>Indirizzo di spedizione <span>*</span></label>' +
							'<input id="newShipAddr" type="text" name="address" placeholder="Via/Piazza/Frazione ...">' +
						'</div>' +
					'</div>' +
					'<div class="col-lg-6 col-md-6 col-12">' +
						'<div class="form-group">' +
							'<label>Codice postale<span>*</span> </label>' +
							'<input id="newShipCAP" type="text" name="post" placeholder="" >' +
						'</div>' +
					'</div>' +
				'</div>' +
			'</form>');
		newAddr = 1;
	});

	//Gestione metodo di pagamento con comparsa e scomparsa 
	//delle opzioni di selezione della carta 
	$(".card_payment").click(function() {
		$(".card_selection").show();
		});	
	$(".no_card_payment").click(function() {
		$(".card_selection").hide();
		});
	
	//Richiesta AJAX per gestione metodo di pagamento
	$("#checkout").click(function() {
		var fullAddress;
		var paymentDetails = "Metodo di pagamento: ";
		if (Boolean(newAddr)) {
			fullAddress = $("#newShipAddr").val() +","+ $("#newShipCity").val() +","+ $("#newShipCAP").val();
		}
		else{
			fullAddress = "standard shipment address";
		};
		if ($(".card_payment").is(":checked")) {
			paymentDetails += $("#credit_card_select").val();
		} else if ($(".cash_payment").is(":checked")) {
			paymentDetails += "Pagamento alla consegna";
		} else {
			paymentDetails += "Paypal";
		};
		$.ajax({
		   	type : 'POST',
			url : checkout_url,
			data : JSON.stringify({ "shipmentAddress" : fullAddress, "payment": paymentDetails}),
			contentType : 'application/json',
		   	dataType: "json", //The type of data that you're expecting back from the server	
			success: function (data) { 
				//window.location.replace(checkout_confirmation_url);
				$( ".checkout" ).replaceWith( '<section class="shop checkout section">' +
				'<div class="section">' +
					'<div class="container">' +
						'<div class="row">' +
							'<div class="col-12">' +
								'<p style="text-align:center; font-size:40px" > Ordine effettuato con successo </p>' +
								'<p style="text-align:center; font-size:20px" >' + data["response1"] +
								'<p style="text-align:center; font-size:20px" >' + data["response2"] +
								'<p style="text-align:center; font-size:20px" >' + data["response3"] +
							'</div>' +
						'</div>' +
					'</div>' +
				'</div>' +
			'</section>');
		    },
			error: function (e) {
				alert("La conferma dell'ordine non è andato a buon fine.");
			},
		    processData : false });
		
	});
	
});