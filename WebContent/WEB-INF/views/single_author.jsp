<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<div class="modal-body">
	<div class="container">
		<div class="row" style="margin-top: 30px;">
			<div class="col-12">
				<div class="row">
					<div class="col-5">
						<img 
							src="<c:url value="/resources/img/authors/${author.photo}"/>"
							alt="#" class="center_image author_image">
					</div>
					<div class="col">
						<div style="margin-left: 15%; margin-top: 5%;" class="quickview-content" >
							<c:if test="${author.surname != '#SURNAME_PLACEHOLDER'}">
								<h2 style="font-size: 40px;"> ${author.name} ${author.surname}</h2>
							</c:if>
							<c:if test="${author.surname == '#SURNAME_PLACEHOLDER'}">
								<h2 style="font-size: 40px;">${author.name}</h2>
							</c:if>
							<div class="quickview-ratting-review">
								<div class="quickview-ratting-wrap">
									<i><b> Data di nascita: ${author.formattedBirthdate}</b>
									<br>
									<b>Nazionalità: ${author.nationality}</b></i>
								</div>
								<div class="quickview-stock"></div>
							</div>
							<br>
							<div class="quickview-peragraph">
								<p>${author.biography}</p>
							</div>
							<div class="size">
								<div class="row">
									<div class="col-lg-6 col-12"></div>
									<div class="col-lg-6 col-12"></div>
								</div>
							</div>

							<div class="default-social">
								<h4 class="share-now">Ulteriori informazioni sull'autore:</h4>
								<br>
								<c:if test="${author.surname != '#SURNAME_PLACEHOLDER'}">
									<em><a
										href="https://it.wikipedia.org/wiki/${author.name}_${author.surname}"
										target="_blank"><i class="fa fa-wikipedia-w"></i>ikipedia
											- ${author.name} ${author.surname}</a></em>
								</c:if>
								<c:if test="${author.surname == '#SURNAME_PLACEHOLDER'}">
									<em><a href="https://it.wikipedia.org/wiki/${author.name}"
										target="_blank"><i class="fa fa-wikipedia-w"></i>ikipedia
											- ${author.name}</a></em>
								</c:if>
							</div>
						</div>
					</div>
				</div>
				<br>
				<div class="row" style="margin-top: 30px;">
					<div class="col">
						<c:if test="${checkAuthorBooks == false }">
							<div>
								<h2>Libri dell'autore</h2>
							</div>
						</c:if>
					</div>
				</div>
				<div class="row">
					<div class="owl-carousel popular-slider">

						<!-- Start Single Product -->
						<c:forEach items="${authorBooks}" var="authorBook">
							<div class="single-product">
								<div class="product-img">
									<a href="<c:url value="/show_book/${authorBook.id}"/>"> <img
										class="default-img" style="height: 300px; object-fit: contain"
										src="<c:url value="/resources/img/cover_book/${authorBook.cover}"/>"
										alt="#"> <img class="hover-img"
										style="height: 300px; object-fit: contain"
										src="<c:url value="/resources/img/cover_book/${authorBook.cover}"/>"
										alt="#"> <c:if test="${authorBook.discount > 0}">
											<span class="price-dec">${authorBook.truncatedDiscount}%</span>
										</c:if>
									</a>
									<security:authorize access="hasRole('USER')">
										<div class="button-head">
											<div class="product-action"></div>
											<div class="product-action-2">
												<c:if test="${authorBook.copies > 0 }">
													<input type="hidden" value="1"
														id="amount_${authorBook.id}">
													<a data-book="${authorBook.id}"
														class="btn add_to_cart">Aggiungi al carrello</a>
												</c:if>
												<c:if test="${authorBook.copies <= 0 }">
													<a>Non disponibile</a>
												</c:if>
											</div>

										</div>
									</security:authorize>
								</div>
								<div class="product-content">
									<h4 style="font-size: 15px !important;">
										<a href="<c:url value="/show_book/${authorBook.id}"/>">${authorBook.title}</a>
									</h4>
									<div class="product-price">
										<c:if test="${authorBook.discount > 0}">
											<span class="old">${authorBook.formattedPrice}</span>
											<span>${authorBook.formattedDiscountedPrice}</span>
										</c:if>
										<c:if test="${authorBook.discount == 0}">
											<span>${authorBook.formattedPrice}</span>
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
</div>
