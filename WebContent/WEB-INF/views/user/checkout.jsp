<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>


<!-- Breadcrumbs -->
<div class="breadcrumbs">
	<div class="container">
		<div class="row">
			<div class="col-12">
				<div class="bread-inner">
					<ul class="bread-list">
						<li><a href="<c:url value="/" />">Home<i class="ti-arrow-right"></i></a></li>
						<li class="active"><a href="<c:url value="/checkout" />">Checkout</a></li>
					</ul>
				</div>
			</div>
		</div>
	</div>
</div>
<!-- End Breadcrumbs -->
				
<!-- Start Checkout -->
<section class="checkout shop section">
	<div class="container">
		<div class="row"> 
			<div class="col-lg-8 col-12">
				<div class="checkout-form">
					<h2>Dati personali</h2>
					<br>
					<!-- Form -->
					<form class="form" method="post"action="#" >
						<div class="row">
							<div class="col-lg-6 col-md-6 col-12">
								<div class="form-group">
									<label>Nome<span>*</span></label>
									<input type="text" name="name" placeholder="${user.personalData.name}" disabled>
								</div>
							</div>
							<div class="col-lg-6 col-md-6 col-12">
								<div class="form-group">
									<label>Cognome<span>*</span></label>
									<input type="text" name="name" placeholder="${user.personalData.surname}" disabled>
								</div>
							</div>
							<div class="col-lg-6 col-md-6 col-12">
								<div class="form-group">
									<label>Indirizzo Email <span>*</span></label>
									<input type="email" name="email" placeholder="${user.email}" disabled>
								</div>
							</div>
							<div class="col-lg-6 col-md-6 col-12">
								<div class="form-group">
									<label>Stato<span>*</span></label>
									<select name="country_name" id="country" disabled>
										<option value="AL">Albania</option>
										<option value="AT">Austria</option>
										<option value="BY">Belarus</option>
										<option value="BE">Belgium</option>
										<option value="BA">Bosnia and Herzegovina</option>
										<option value="BG">Bulgaria</option>
										<option value="HR">Croatia</option>
										<option value="CZ">Czech Republic</option>
										<option value="DK">Denmark</option>
										<option value="EE">Estonia</option>
										<option value="FI">Finland</option>
										<option value="FR">France</option>
										<option value="DE">Germany</option>
										<option value="GR">Greece</option>
										<option value="GL">Greenland</option>
										<option value="GD">Grenada</option>
										<option value="HU">Hungary</option>
										<option value="IS">Iceland</option>
										<option value="IQ">Iraq</option>
										<option value="IE">Ireland</option>
										<option selected="selected" value="IT">Italy</option>
										<option value="LI">Liechtenstein</option>
										<option value="LT">Lithuania</option>
										<option value="LU">Luxembourg</option>
										<option value="MT">Malta</option>
										<option value="MC">Monaco</option>
										<option value="ME">Montenegro</option>
										<option value="NL">Netherlands</option>
										<option value="AN">Netherlands Antilles</option>
										<option value="NO">Norway</option>
										<option value="PE">Peru</option>
										<option value="PL">Poland</option>
										<option value="PT">Portugal</option>
										<option value="RO">Romania</option>
										<option value="SM">San Marino</option>
										<option value="RS">Serbia</option>
										<option value="SK">Slovakia</option>
										<option value="SI">Slovenia</option>
										<option value="ES">Spain</option>
										<option value="SE">Sweden</option>
										<option value="CH">Switzerland</option>
										<option value="UA">Ukraine</option>
										<option value="VA">Vatican City</option>
									</select>
								</div>
							</div>
							<div class="col-lg-6 col-md-6 col-12">
								<div class="form-group">
									<label>Regione<span>*</span></label>
									<select name="state-province" id="state-province"> 
										<option selected="selected" value="0">Abruzzo</option>
							            <option value="1">Basilicata</option>
							            <option value="2">Calabria</option>
				                        <option value="3">Campania</option>
				                        <option value="4">Emilia-Romagna</option>
				                        <option value="5">Friuli-Venezia Giulia</option>
				                        <option value="6">Lazio</option>
				                        <option value="7">Liguria</option>
				                        <option value="8">Lombardia</option>
				                        <option value="9">Marche</option>
				                        <option value="10">Molise</option>
				                        <option value="11">Piemonte</option>
				                        <option value="12">Puglia</option>
				                        <option value="13">Sardegna</option>
				                        <option value="14">Sicilia</option>
				                        <option value="15">Toscana</option>
				                        <option value="16">Trentino-Alto Adige</option>
				                        <option value="17">Umbria</option>
				                        <option value="18">Valle d'Aosta</option>
				                        <option value="19">Veneto</option>
									</select>
								</div>
							</div>
						</div>
					</form>
					<br>
					<h2>Spedizione</h2>
					<br>
					<form class="shipmentForm form" method="post">
						<div class="row">
								<div class="col-lg-6 col-md-6 col-12">
									<div class="form-group">
										<label>Città <span>*</span></label>
										<input id="shipCity" type="text" placeholder="${user.personalData.city}" value="${user.personalData.city}" disabled>
									</div>
								</div>
								<div class="col-lg-6 col-md-6 col-12">
									<div class="form-group">
										<label>Indirizzo di spedizione <span>*</span></label>
										<input id="shipAddr" type="text" placeholder="${user.personalData.street}" value="${user.personalData.street}" disabled>
									</div>
								</div>
								<div class="col-lg-6 col-md-6 col-12">
									<div class="form-group">
										<label>Codice postale<span>*</span></label>
										<input id="shipCAP" type="text" placeholder="${user.personalData.cap}" value="${user.personalData.cap}" disabled>
									</div>
								</div>
								<div class="col-12">
									<div class="form-group ">
										<button id="newAddress" class="btn btn-sm" >
											<span >
												Inserisci un nuovo indirizzo
											</span>
										</button>
										<%-- <label>Vuoi inserire un nuovo indirizzo?</label> --%>
									</div>
								</div>
						</div>
					</form>
					<!--/ End Form -->
				</div>
			</div>
			<div class="col-lg-4 col-12">
				<div class="order-details">
					<!-- Order Widget -->
					<div class="single-widget">
						<h2>Riepilogo totale carrello</h2>
						<div class="content">
							<ul id="checkout_costs_report">
								<li>Subtotale<span>${user.formattedCartSubtotalPrice}</span></li>
								<li id="vat_amount" vat=${vat}>Vat (${vat}%)<span>${vatAmount}</span></li>
								<li>Costi di spedizione<span>€ 5,00</span></li>
								<c:choose>
								<c:when test="${user.savedMoney>0}"> 
									<li id="savings">Risparmio<span>- ${user.formattedSavedMoney}</span></li>
								</c:when>
								<c:otherwise>
									<li id="savings"> </li>
								</c:otherwise>
								</c:choose>
								<li id="checkout_total" class="last">Totale<span>${user.formattedCheckoutTotalPrice}</span></li>
							</ul>
						</div>
					</div>
					<div class="single-widget">
						<h2>Coupon</h2>
						<div class="content">
						<ul>
							<li>
								<div class="coupon">
									<!--  <form class="couponForm form" target="_blank">  </form> -->
									<input id="coupon_code" type="text" placeholder="INSERISCI IL COUPON">
									<input id="coupon_code_hidden" type="hidden">
									<button id="coupon" class="btn btn-sm">APPLICA</button>
								</div>
							</li>
						</ul>
						</div>
					</div>	
					<!--/ End Order Widget -->
					<!-- Order Widget -->
					<div class="single-widget">
						<h2>Metodo di pagamento</h2>
						<div class="content">
						<ul>
							<li>
								<div class="card_selection">
									<p style="text-align:center;">Seleziona una carta</p>
									<br>
									<select id="credit_card_select" id="credit_card">
										<option selected>Nessuna carta selezionata</option>
									<c:forEach items="${user.paymentCards}" var="card">
										<option id="" value="${card.type} ${card.hiddenNumber} ${card.shortExpirationDate}" style="margin:auto; font-size:2px">
											${card.type} | ${card.hiddenNumber.toLowerCase()} | ${card.shortExpirationDate}
										</option>
									</c:forEach>
									</select>
								</div>
							</li>
							<div id="paymentMethods" class="radio">
								<li>
									<label ><input id="card_payment" class="card_payment" type="radio" name="payment" checked> Card
									<img src="images/payment-method.png">
									<img src="images/payment-method.png">
									<img src="images/payment-method.png"></label>
								</li> 
								<li>
									<label><input id="cash_payment" class="no_card_payment" type="radio" name="payment"> Cash On Delivery</label>
									<img src="images/payment-method.png" alt="#"></label>
								</li>
								<li>
									<label><input id="paypal_payment" class="no_card_payment" type="radio" name="payment"> PayPal</label>
									<img src="images/payment-method.png" alt="#"></label>
								</li>
							</div>
						</ul>
						</div>
					</div>
					<!--/ End Order Widget -->
					<!-- Payment Method Widget -->
					<div class="single-widget payement">
						<div class="content">
							<img src="images/payment-method.png" alt="#">
						</div>
					</div>
					<!--/ End Payment Method Widget -->
					<!-- Button Widget -->
					<div class="single-widget get-button">
						<div class="form-group ">
							<button id="checkout" class="btn" >
								<span >
									Checkout
								</span>
							</button>
						</div>
					</div>
					<!--/ End Button Widget -->
				</div>
			</div>
		</div>
	</div>
</section>
<!--/ End Checkout -->