<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div class="modal-body">
	<div class="row no-gutters">
		<div class="col-lg-6 col-md-12 col-sm-12 col-xs-12">
			<img src="<c:url value="/resources/img/${book.cover}"/>" alt="#"
				class="center_image">
			
		</div>
		<div class="col-lg-6 col-md-12 col-sm-12 col-xs-12">
			<div class="quickview-content">
				<h2>${book.title}</h2>
				<h4><i> Di 
						<c:forEach items="${authorsList}" var="authorsList">
							<a href=" <c:url value ="/show_author/${authorsList.id}"/>" >${authorsList.name} ${authorsList.surname}</a>
							</c:forEach>	
					</i>
				</h4>
				<div class="quickview-ratting-review">
					<div class="quickview-ratting-wrap">
						<div class="quickview-ratting">
							<c:if test="${book.copies > 0}">
								<b style = "color:#228B22">Disponibile</b>
							</c:if>
							<c:if test="${book.copies <= 0}">
								<b style = "color:#B22222" >Non Disponibile</b>
							</c:if>


						</div>

					</div>
					<div class="quickview-stock"></div>
				</div>
				<c:if test="${book.discount > 0 }">
				<h3><s style = "color:#B22222">${book.formattedPrice}</s>&nbsp ${book.formattedDiscountedPrice}<p style = "color:#B22222">(${ book.truncatedDiscount}% di sconto)</p> </h3>
				</c:if>
					<c:if test="${book.discount == 0 }">
				<h3>${book.formattedPrice}</h3>
				</c:if>
				<div class="quickview-peragraph">
					<p>${book.summary}</p>
				</div>
				<div class="size">
					<div class="row">
						<div class="col-lg-6 col-12"></div>
						<div class="col-lg-6 col-12"></div>
					</div>
				</div>
				<div class="quantity">
					<!-- Input Order -->
					<div class="input-group">
						<div class="button minus">
							<button type="button" class="btn btn-primary btn-number"
								disabled="disabled" data-type="minus" data-field="quant[1]">
								<i class="ti-minus"></i>
							</button>
						</div>
						<c:if test="${book.copies > 0}">
							
						<input type="text" name="quant[1]" class="input-number"
							data-min="1" data-max="${book.copies}" value="1" id="amount_${book.id}">
						<div class="button plus">
							<button type="button" class="btn btn-primary btn-number"
								data-type="plus" data-field="quant[1]">
								<i class="ti-plus"></i>
							</button>
						</div>
					</div>
					<!--/ End Input Order -->
				</div>
				<div class="add-to-cart">
					<a data-book="${book.id}" class="btn add_to_cart">Aggiungi al carrello</a>
				</div>
				</c:if>
				<div class="default-social">
					<h4 class="share-now">Share:</h4>
					<ul>
						<li><a class="facebook" href="#"><i
								class="fa fa-facebook"></i></a></li>
						<li><a class="twitter" href="#"><i class="fa fa-twitter"></i></a></li>
						<li><a class="youtube" href="#"><i
								class="fa fa-pinterest-p"></i></a></li>
						<li><a class="dribbble" href="#"><i
								class="fa fa-google-plus"></i></a></li>
					</ul>
				</div>
			</div>
		</div>

		<!-- Start Most Popular -->
	<div class="product-area most-popular section">
        <div class="container">
            <div class="row">
				<div class="col-12">
					<c:if test="${checklistgenre == false }">
					<div>
						<h2>Libri dello stesso genere che potrebbero interessarti</h2>
					</div>
					</c:if>
				</div>
            </div>
            <div class="row">
                <div class="col-12">
                    <div class="owl-carousel popular-slider">
						
						<!-- Start Single Product -->
						<c:forEach items="${booksimilgenre}" var="bookinterestgenre">
						<div class="single-product">
							<div class="product-img">
								<a href="<c:url value="/show_book/${bookinterestgenre.id}"/>"> <img
														class="default-img"
														src="<c:url value="/resources/img/${bookinterestgenre.cover}"/>"
														alt="#"> <img class="hover-img"
														src="<c:url value="/resources/img/${bookinterestgenre.cover}"/>" alt="#">
														<c:if test="${bookinterestgenre.discount > 0}">
														<span class="price-dec">${bookinterestgenre.truncatedDiscount}%</span>
														</c:if>
													</a>
								<div class="button-head">
									<div class="product-action">
									</div>
									<div class="product-action-2">
										<a title="Add to cart" href="#">Aggiungi al carrello </a>
									</div>
								</div>
							</div>
							<div class="product-content">
								<h4><a href="product-details.html"></a>${bookinterestgenre.title}</h4>
								<div class="product-price">
									<c:if test="${bookinterestgenre.discount > 0}">
									<span class="old">${bookinterestgenre.formattedPrice}</span>
									<span>${bookinterestgenre.formattedDiscountedPrice}</span>
									</c:if>
									<c:if test="${bookinterestgenre.discount == 0}">
									<span>${bookinterestgenre.formattedPrice}</span>
									</c:if>
								</div>
							</div>
						</div>
						</c:forEach>
						<!-- End Single Product -->
						
                    </div>
                </div>
            </div>
   </div>
   <br>
        <div class="container">
            <div class="row">
				<div class="col-12">
					<c:if test="${checklistaut == false }">
					<div>
						<h2>Libri dello stesso autore che potrebbero interessarti</h2>
					</div>
					</c:if>
				</div>
            </div>
            <div class="row">
                <div class="col-12">
                    <div class="owl-carousel popular-slider">
						
						<!-- Start Single Product -->
						<c:forEach items="${booksimilaut}" var="bookinterestaut">
						<div class="single-product">
							<div class="product-img">
								<a href="<c:url value="/show_book/${bookinterestaut.id}"/>"> <img
														class="default-img"
														src="<c:url value="/resources/img/${bookinterestaut.cover}"/>"
														alt="#"> <img class="hover-img"
														src="<c:url value="/resources/img/${bookinterestaut.cover}"/>" alt="#">
														<c:if test="${bookinterestaut.discount > 0}">
														<span class="price-dec">${bookinterestaut.truncatedDiscount}%</span>
														</c:if>
													</a>
								<div class="button-head">
									<div class="product-action">
									</div>
									<div class="product-action-2">
										<a title="Add to cart" href="#">Aggiungi al carrello </a>
									</div>
								</div>
							</div>
							<div class="product-content">
								<h4><a href="product-details.html"></a>${bookinterestaut.title}</h4>
								<div class="product-price">
									<c:if test="${bookinterestaut.discount > 0}">
									<span class="old">${bookinterestaut.formattedPrice}</span>
									<span>${bookinterestaut.formattedDiscountedPrice}</span>
									</c:if>
									<c:if test="${bookinterestaut.discount == 0}">
									<span>${bookinterestgenre.formattedPrice}</span>
									</c:if>
								</div>
							</div>
						</div>
						</c:forEach>
						<!-- End Single Product -->
						
                    </div>
                </div>
            </div>
        </div>
    </div>
  
	<!-- End Most Popular Area -->

	</div>
</div>
