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
						<li class="active"><a href="<c:url value="/cart" />">Cart</a></li>
					</ul>
				</div>
			</div>
		</div>
	</div>
</div>
<!-- End Breadcrumbs -->

<!-- Shopping Cart -->
<div class="shopping-cart section">
	<div class="container">
		<div class="row">
			<div class="col-12">
				<!-- Shopping Summary -->
				<table class="table shopping-summery">
					<thead>
						<tr class="main-hading">
							<th>LIBRO</th>
							<th>TITOLO</th>
							<th class="text-center">PREZZO</th>
							<th class="text-center">QUANTITÀ</th>
							<th class="text-center">TOTALE</th>
							<th class="text-center"><i class="ti-trash remove-icon"></i></th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${user_cart}" var="cart_el">
						<tr>
							<td class="image" data-title="No">
								<img src="<c:url value="/resources/img/${cart_el.book.cover}"/>" alt="#">
							</td>
							<td class="product-des" data-title="Description">
								<p class="product-name">
									<a href="#">${cart_el.book.title}</a>
								</p>
								<p class="product-des">Autori: 
									<c:forEach items="${cart_el.book.authors}" var="author">
										${author.fullName} 
									</c:forEach>- Data publicazione:${cart_el.book.publish}
								</p>
							</td>
							<td class="price" data-title="Price"><span>${cart_el.book.formattedPrice} &euro;</span></td>
							<td class="qty" data-title="Qty">
								<!-- Input Order -->
								<div class="input-group">
									<div class="button minus">
										<button type="button" class="decrement btn btn-primary btn-number" 
											id="${cart_el.id.bookID}" data-type="minus" data-field="${cart_el.id.bookID}">
											<i class="ti-minus"></i>
										</button>
									</div>
									<input type="text" name="${cart_el.id.bookID}" class="input-number"
										data-min="1" data-max="${cart_el.book.copies}" value="${cart_el.copies}"> 
									<div class="button plus">
										<button type="button" class="increment btn btn-primary btn-number"
											id="${cart_el.id.bookID}" data-type="plus" data-field="quant[1]">
											<i class="ti-plus"></i>
										</button>
									</div>
								</div> <!--/ End Input Order -->
							</td>
							<td class="total-amount" data-title="Total"><span>$220.88</span></td>
							<td class="action" data-title="Remove"><a href="#"><i
									class="ti-trash remove-icon"></i></a></td>
						</tr>
						</c:forEach>
					</tbody>
				</table>
				<!--/ End Shopping Summary -->
			</div>
		</div>
		<div class="row">
			<div class="col-12">
				<!-- Total Amount -->
				<div class="total-amount">
					<div class="row">
						<div class="col-lg-8 col-md-5 col-12">
							<div class="left">
								<div class="coupon">
									<form action="#" target="_blank">
										<input name="Coupon" placeholder="Enter Your Coupon">
										<button class="btn">Apply</button>
									</form>
								</div>
								<div class="checkbox">
									<label class="checkbox-inline" for="2"><input
										name="news" id="2" type="checkbox"> Shipping (+10$)</label>
								</div>
							</div>
						</div>
						<div class="col-lg-4 col-md-7 col-12">
							<div class="right">
								<ul>
									<li>Cart Subtotal<span>$330.00</span></li>
									<li>Shipping<span>Free</span></li>
									<li>You Save<span>$20.00</span></li>
									<li class="last">You Pay<span>$310.00</span></li>
								</ul>
								<div class="button5">
									<a href="#" class="btn">Checkout</a> <a href="#" class="btn">Continue
										shopping</a>
								</div>
							</div>
						</div>
					</div>
				</div>
				<!--/ End Total Amount -->
			</div>
		</div>
	</div>
</div>
<!--/ End Shopping Cart -->