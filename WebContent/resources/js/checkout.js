$(document).ready(function() { 
	
	//Inserimento nuovo indirizzo
	$("#newAddress").click(function() {
		$(".shipmentForm").html(
			'<form class=" shipmentForm form" method="post">' +
				'<div class="row">' +
					'<div class="col-lg-6 col-md-6 col-12">' +
						'<div class="form-group">' +
							'<label>Città <span>*</span></label>' +
							'<input id="shipCity" type="text" placeholder="Città" >' +
						'</div>' +
					'</div>' +
					'<div class="col-lg-6 col-md-6 col-12">' +
						'<div class="form-group">' +
							'<label>Indirizzo di spedizione <span>*</span></label>' +
							'<input id="shipAddr" type="text" placeholder="Via/Piazza/Frazione ...">' +
						'</div>' +
					'</div>' +
					'<div class="col-lg-6 col-md-6 col-12">' +
						'<div class="form-group">' +
							'<label>Codice postale<span>*</span> </label>' +
							'<input id="shipCAP" type="text" placeholder="CAP" >' +
						'</div>' +
					'</div>' +
				'</div>' +
			'</form>');
	});

	//Gestione metodo di pagamento con comparsa e scomparsa 
	//delle opzioni di selezione della carta 
	$(".card_payment").click(function() {
		$(".card_selection").show();
		});	
	$(".no_card_payment").click(function() {
		$(".card_selection").hide();
		});
	
	//Gestione checkout
	$("#checkout").click(function() { //$(document).on('click', '#checkout', function()
		var fullAddress;
		var paymentDetails = "";
		var coupon_code = $("#coupon_code_hidden").val().toUpperCase().trim();
		
		var emptyForm;
		//Controllo form indirizzo di spedizione completo
		$(".shipmentForm input").each(function() {
			if($(this).val() == "") {
				popupMessage("Completare l'indirizzo di spedizione!");
				throw new Error("Incomplete Address Form"); 
			}
		});
		
		fullAddress = $("#shipAddr").val() +", "+ $("#shipCity").val() +", "+ $("#shipCAP").val();
			
		//payment method management
		$("#paymentMethods input").each(function() {
	    	if ($(this).is(":checked")) {
				if ($(this).attr("id") == "card_payment") {
					var selection = $("#credit_card_select :selected").text();
					if (selection=="Nessuna carta selezionata") {
						popupMessage("Nessuna carta selezionata!");
						throw new Error("No credit card selected");
					} else{
						paymentDetails = selection;
						return false;
					} //forza uscita dall'iterazione dell'each())
				} else if ($(this).attr("id") == "cash_payment") {
					paymentDetails = "Pagamento alla consegna";
					return false;
				} else {
					paymentDetails = "Paypal";
					return false;
				}
			};
		});
		
		if (paymentDetails =="") {
				popupMessage("Scegliere un metodo di pagamento!");
				throw new Error("No payment method selected");
		};

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
								'<p style="text-align:center; font-size:40px" > Ordine effettuato con successo </p>' +
								'<p style="text-align:center; font-size:20px" >' + data["response1"] +
								'<p style="text-align:center; font-size:20px" >' + 'Metodo di pagamento: ' + data["response2"] +
								'<p style="text-align:center; font-size:20px" >' + data["response3"] +
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
	
	//Validazione e applicazione coupon
	$(document).on('click', '#coupon', function(){ 
		coupon_code = $("#coupon_code").val().toUpperCase().trim();
		$.ajax({
            type : 'POST',
            url : coupon_validation_url,
			data : JSON.stringify({"arg1": "", "arg2": "", "arg3": coupon_code}),
			contentType : 'application/json',
           	dataType: "json", //The type of data that you're expecting back from the server	
			success: function (data) {
				if (data["response1"]=="used") {
					//aggiunta nuovo campo sconto in ul 
					popupMessage("Coupon già utilizzato!");
				} else if (data["response1"]=="unavailable") { 
					popupMessage("Coupon non esistente!");
				} else {
					//add discoount field in chechkout 
					$("#savings").after('<li id="coupon_save">'+  coupon_code + '<span>' + '-' +data["response2"] +'</span></li>');
					var oldValue = $("#checkout_total span").text().split(" ")[1];
					$("#checkout_total").replaceWith('<li id="checkout_total" class="last" oldValue=' + oldValue + '>Totale<span>'+ data["response3"] +'</span></li>');
					$("#coupon").replaceWith('<button id="delCoupon" class="btn btn-sm">RIMUOVI</button>');
					$("#coupon_code_hidden").val($("#coupon_code").val());
					$("#coupon_code").prop("disabled", true);
				}	
			},
            processData : false });
	});
	
	//Eliminazione coupon
	$(document).on('click', '#delCoupon', function(){
		//remove coupon
		$("#coupon_save").remove();
		$("#checkout_total").replaceWith('<li id="checkout_total" class="last">Totale<span>€ '+ $("#checkout_total").attr("oldValue") +'</span></li>');
		$("#coupon_code_hidden").val("");
		$("#coupon_code").val("");
		$("#coupon_code").prop("disabled", false);
		$("#delCoupon").replaceWith('<button id="coupon" class="btn btn-sm">APPLICA</button>');
	});
	
	
});