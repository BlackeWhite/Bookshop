$(document).ready(function() { 
	
	// Codice per la pulizia del form autocompilato per l'inserimento di un nuovo indirizzo 
	$("#newAddress").click(function(e) {
		e.preventDefault();
		$("#shipCity").attr("value","");
		$("#shipCity").prop("disabled", false);
		$("#shipAddr").attr("placeholder", "Via/Piazza/Frazione");
		$("#shipAddr").removeAttr("value","");
		$("#shipAddr").prop("disabled", false);
		$("#shipCAP").removeAttr("value","");
		$("#shipCAP").prop("disabled", false);
		$(this).remove();
	});

	// Gestione metodo di pagamento con comparsa e scomparsa 
	// delle opzioni di selezione della carta 
	$(".card_payment").click(function() {
		$(".card_selection").show();
		});	
	$(".no_card_payment").click(function() {
		$(".card_selection").hide();
		});
	
	// Codice per la gestione del checkout in fase di conferma del checkout 
	$("#checkout").click(function() { //$(document).on('click', '#checkout', function()
		var fullAddress;
		var paymentDetails = "";
		var coupon_code = $("#coupon_code_hidden").val().toUpperCase().trim();
		
		// Controllo sulla completezza del form relativo all'indirizzo di spedizione
		$(".shipmentForm input").each(function() {
			if($(this).val() == "") {
				popupMessage("Completare l'indirizzo di spedizione!");
				throw new Error("Incomplete Address Form"); 
			}
		});
		
		fullAddress = $("#shipAddr").val() +", "+ $("#shipCity").val() +", "+ $("#shipState").val()+ ", "+ $("#shipCAP").val();
			
		// Controllo sulla selezione del metodo di pagamento
		$("#paymentMethods input").each(function() {
	    	if ($(this).is(":checked")) {
				if ($(this).attr("id") == "card_payment") {
					var selection = $("#credit_card_select :selected").text();
					if (selection=="Nessuna carta selezionata") {
						popupMessage("Nessuna carta selezionata!");
						throw new Error("No credit card selected");
						}
					else {
						paymentDetails = selection;
						return false; // forza uscita dall'iterazione dell'each())
						}
					}   
				else if ($(this).attr("id") == "cash_payment") {
						paymentDetails = "Pagamento alla consegna";
						return false;
						}
				else {
					paymentDetails = "Paypal";
					return false;
					}
				};
		});
		
		if (paymentDetails =="") {
				popupMessage("Scegliere un metodo di pagamento!");
				throw new Error("No payment method selected");
		};
		// Richiesta AJAX per la creazione dell'ordine  
		$.ajax({
		   	type : 'POST',
			url : checkout_url,
			data : JSON.stringify({ "arg1" : fullAddress, "arg2": paymentDetails, "arg3": coupon_code}),
			contentType : 'application/json',
		   	dataType: "json", //The type of data that you're expecting back from the server	
			success: function (data) { 
				//window.location.replace(checkout_confirmation_url);
				$( ".checkout" ).replaceWith( '<section class="shop checkout section">' +
				'<div class="section">' +
					'<div class="container">' +
						'<div class="row">' +
							'<div class="col-12">' +
								'<p style="text-align:center; font-size:40px" > Ordine effettuato con successo </p><br>' +
								'<p style="text-align:center; font-size:20px" >' + 'Indirizzo di spedizione: ' + data["response1"] +
								'<p style="text-align:center; font-size:20px" >' + 'Metodo di pagamento: ' + data["response2"] +
								'<p style="text-align:center; font-size:20px" >' + 'Effettuato in data: ' + data["response3"] +
								'<p style="text-align:center; font-size:20px" >' + 'Totale: ' + $("#checkout_total").find($("span")).text() +
							'</div>' +
						'</div>' +
					'</div>' +
				'</div>' +
			'</section>');
			
		    },
			error: function (e) {
				window.location.href = cart_url;
			},
		    processData : false });
		
	});
	
	// Codice per la gestione e la validazione dei coupon inseriti
	$(document).on('click', '#coupon', function(){ 
		coupon_code = $("#coupon_code").val().toUpperCase().trim();
		// Richiesta AJAX per l'esecuzione dei controlli sulla validità del coupon inserito e l'eventuale applicazione 
		$.ajax({
            type : 'POST',
            url : coupon_validation_url,
			data : JSON.stringify({"arg1": "", "arg2": "", "arg3": coupon_code}),
			contentType : 'application/json',
           	dataType: "json", 
			success: function (data) {
				if (data["response1"]=="used") { 
					popupMessage("Coupon già utilizzato!");
					} 
				else if (data["response1"]=="unavailable") { 
					popupMessage("Coupon non esistente!");
					} 
				else if (data["response1"]=="expired") { 
					popupMessage("Coupon scaduto!");
					} 
				else {
					//aggiunta nuovo campo sconto nella sezione di resoconto dei costi 
					$("#savings").after('<li id="coupon_save">'+  coupon_code + '<span>' + '-' +data["response2"] +'</span></li>');
					//oldTotal e oldVat sono utilizzate per tornare allo stato precedente all'applicazione del coupon 
					//in caso di eliminazione
					var oldTotal = $("#checkout_total span").text().split(" ")[1];
					var oldVat = $("#vat_amount span").text().split(" ")[1];
					$("#vat_amount").replaceWith('<li id="vat_amount" vat='+ $("#vat_amount").attr("vat") +' oldVat=' + oldVat + '>Vat('+ $("#vat_amount").attr("vat") +'%)<span>'+ data["response4"] +'</span></li>');
					$("#checkout_total").replaceWith('<li id="checkout_total" class="last" oldTotal=' + oldTotal + '>Totale<span>'+ data["response3"] +'</span></li>');
					$("#coupon").replaceWith('<button id="delCoupon" class="btn btn-sm">RIMUOVI</button>');
					$("#coupon_code_hidden").val($("#coupon_code").val());
					$("#coupon_code").prop("disabled", true);
				}	
			},
            processData : false });
	});
	
	//Eliminazione coupon
	$(document).on('click', '#delCoupon', function(){
		// si elimina il nuovo campo sul resoconto dei costi
		$("#coupon_save").remove();
		// si riportano VAT e totale allo stato precedente all'inserimento  
		$("#vat_amount").html('Vat('+ $("#vat_amount").attr("vat") +'%)<span>€ '+ $("#vat_amount").attr("oldVat") +'</span>');
		$("#checkout_total").html('Totale<span>€ '+ $("#checkout_total").attr("oldTotal") +'</span>');
		$("#coupon_code_hidden").val("");
		$("#coupon_code").val("");
		$("#coupon_code").prop("disabled", false);
		$("#delCoupon").replaceWith('<button id="coupon" class="btn btn-sm">APPLICA</button>');
	});
	
	
});