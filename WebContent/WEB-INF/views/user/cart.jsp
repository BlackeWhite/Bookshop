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
				<c:choose>
				    <c:when test="${empty user_cart}">
				    	<p style="text-align:center; font-size:40px">Carrello vuoto.</p>
				    </c:when>    
				    <c:otherwise><table class="table shopping-summery">
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
								<tr id="${cart_el.id.bookID}">
									<td class="image" data-title="No">
										<img src="<c:url value="/resources/img/cover_book/${cart_el.book.cover}"/>" style="height: 100px; object-fit: contain" alt="#">
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
										<c:choose>
									    	<c:when test="${cart_el.book.copies > 5}">
												<p style="color:green" >Disponibile</p>
											</c:when> 
											<c:when test="${cart_el.book.copies <= 5 && cart_el.book.copies >= 1 }">
												<p style="color:orange" > Copie disponibili: ${cart_el.book.copies}</p>
											</c:when>
											<c:otherwise>
											<p style="color:red">Non disponibile</p>
											</c:otherwise>
										</c:choose>
												
									</td>
									<c:choose>
									    <c:when test="${cart_el.book.discount>0}">
									        <td id="${cart_el.id.bookID}" class="price" data-title="Price">
												<span style="color:red;"><del>${cart_el.book.formattedPrice}</del></span>
												<span style="white-space:nowrap;">${cart_el.book.formattedDiscountedPrice}</span>
											</td>
										</c:when> 
										<c:otherwise>
									       <td id="${cart_el.id.bookID}" class="price" data-title="Price">
												<span>${cart_el.book.formattedPrice}</span>
											</td>
									     </c:otherwise>
									</c:choose>  
									<td class="qty" data-title="Qty">
										<!-- Input Order -->
										<div class="input-group">
											<div class="button minus">
												<button type="button" class="decrement btn btn-primary btn-number" 
													id="${cart_el.id.bookID}" data-type="minus" data-field="${cart_el.id.bookID}">
													<i class="ti-minus"></i>
												</button>
											</div>
											<input id="cp_${cart_el.id.bookID}" type="text" name="${cart_el.id.bookID}" class="input-number"
												data-min="1" data-max="${cart_el.book.copies}" value="${cart_el.copies}"> 
											<div class="button plus">
												<button type="button" class="increment btn btn-primary btn-number"
													id="${cart_el.id.bookID}" data-type="plus" data-field="${cart_el.id.bookID}">
													<i class="ti-plus"></i>
												</button>
											</div>
											<c:if  test="${cart_el.copies > cart_el.book.copies}">
												<p id="cp_${cart_el.id.bookID}_error" class="copies_error" style="color:red"> Disponibilità non sufficiente </p>
											</c:if>
										</div> 
										<!--/ End Input Order -->
									</td>
									<td id="element_total_price${cart_el.id.bookID}" class="element_total_price" data-title="Total" value="${cart_el.elementTotalPrice}">
										<span style="white-space:nowrap;">${cart_el.formattedElementTotalPrice}</span>
									</td>
									<td class="action" data-title="Remove">
									<button id="${cart_el.id.bookID}_delete" type="button" class="delete_element btn">
										<i class="ti-trash remove-icon">
										</i>
									</button>
									</td>
								</tr>
								</c:forEach>
							</tbody>
						</table>
					</c:otherwise>
				</c:choose>
				<!--/ End Shopping Summary -->
			</div>
		</div>
		<div class="row">
		<div class="col-12">
		<!-- Total Amount -->
		<c:choose>
		    <c:when test="${empty user_cart}">
		        <div class="section-title">
					<p></p>
				</div> 
		        <br />
		    </c:when>    
		    <c:otherwise>
				<div class="total-amount">
					<div class="row">
						<div class="col-lg-8 col-md-5 col-12">
							<div class="left">

							</div>
						</div>
						<div class="col-lg-4 col-md-7 col-12">
							<div class="right">
								<ul>
									<li id="cart_subtotal">Subtotale
										<span>${user.formattedCartSubtotalPrice}</span>
									</li>
									<li>Costi di spedizione<span>€ 5,00</span></li>
									<li id="savings">Risparmio<span>${user.formattedSavedMoney}</span></li>
									<li id="checkout_total" class="last">Totale<span>${user.formattedCheckoutTotalPrice}</span></li>
								</ul>
								<div class="button5">
									<a id="pre-checkout" class="btn" href="<c:url value="/checkout" />">Checkout</a> 
									<a href="<c:url value="/" />" class="btn">Continua gli acquisti</a>
								</div>
							</div>
						</div>
					</div>
				</div>
			</c:otherwise>
		</c:choose>
				<!--/ End Total Amount -->
			</div>
		</div>
	</div>
</div>
<!--/ End Shopping Cart -->