$(document).ready(function() { 
	//Inserimento nuovo indirizzo
	$("#newAddress").click(function() {
		$(".shipmentForm").html('<form class=" shipment_form form" method="post"><div class="row"><div class="col-lg-6 col-md-6 col-12"><div class="form-group"><label>Indirizzo di spedizione <span>*</span></label><input type="text" name="address" placeholder=""></div></div><div class="col-lg-6 col-md-6 col-12"><div class="form-group"><label>Codice postale<span>*</span></label><input type="text" name="post" placeholder="" ></div></div></div></form>')
	});
});