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
						</div>
					</form>
					<br>
					<h2>Spedizione</h2>
					<br>
					<form class="shipmentForm form">
						<div class="row">
							<div class="col-lg-6 col-md-6 col-12">
								<div class="form-group">
									<label>Stato<span>*</span></label>
									<input id="shipState" type="text" value="${user.personalData.state}" disabled>
									<small class="form-text text-muted">Modificare il seguente lo Stato nella sezione Il mio account .</small>
								</div>
							</div>
							<div class="col-lg-6 col-md-6 col-12">
								<div class="form-group">
									<label>Città <span>*</span></label>
									<input id="shipCity" type="text" value="${user.personalData.city}" disabled>
								</div>
							</div>
							<div class="col-lg-6 col-md-6 col-12">
								<div class="form-group">
									<label>Indirizzo di spedizione <span>*</span></label>
									<input id="shipAddr" type="text" value="${user.personalData.street}" disabled>
								</div>
							</div>
							<div class="col-lg-6 col-md-6 col-12">
								<div class="form-group">
									<label>Codice postale<span>*</span></label>
									<input id="shipCAP" type="text" value="${user.personalData.cap}" disabled>
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
								<li id="vat_amount" vat=${vat}>di cui Vat: (${vat}%)<span>${vatAmount}</span></li>
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
										<c:forEach items="${paymentCards}" var="card">
											<option id="" value="${card.type} ${card.hiddenNumber} ${card.shortExpirationDate}" style="margin:auto; font-size:2px">
												${card.type} | ${card.hiddenNumber.toLowerCase()} | ${card.shortExpirationDate}
											</option>
										</c:forEach>
										</select>
									</div>
								</li>
							</ul>
							<div id="paymentMethods" class="radio">
								<ul>
									<li>
										<label ><input id="card_payment" class="card_payment" type="radio" name="payment" checked> Card
										<i class="fa fa-credit-card" aria-hidden="true"></i>
										<i class="fa fa-cc-mastercard" aria-hidden="true"></i>
										<i class="fa fa-cc-visa" aria-hidden="true"></i>
										</label>
									</li> 
									<li>
										<label><input id="cash_payment" class="no_card_payment" type="radio" name="payment"> Cash On Delivery</label>
									</li>
									<li>
										<label><input id="paypal_payment" class="no_card_payment" type="radio" name="payment"> PayPal</label>
										<i class="fa fa-paypal" aria-hidden="true"></i>
									</li>
								</ul>
							</div>
						</div>
					</div>
					<!--/ End Order Widget -->
					<!-- Payment Method Widget -->
					<div class="single-widget payement">
						<div class="content">
							
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