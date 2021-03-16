<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div class="modal-body">
	<div class="row no-gutters">
		<div class="col-lg-6 col-md-12 col-sm-12 col-xs-12">
			<img src="<c:url value="/resources/img/cover_book/${author.photo}"/>" alt="#"
				class="center_image">
		</div>
		<div class="col-lg-6 col-md-12 col-sm-12 col-xs-12">
			<div class="quickview-content">
			<c:if test = "${author.surname != '#SURNAME_PLACEHOLDER'}">
				<h2 >${author.name} ${author.surname}</h2>
			</c:if>
			<c:if test = "${author.surname == '#SURNAME_PLACEHOLDER'}">
				<h2 >${author.name}</h2>
			</c:if>
				<div class="quickview-ratting-review">
					<div class="quickview-ratting-wrap">
						<div class="quickview-ratting">
							<i><b> Nato il ${author.birthdate}, nazionalità ${author.nationality} </b></i>
						</div>

					</div>
					<div class="quickview-stock"></div>
				</div>
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
					<h4 class="share-now">Ulteriori informazioni sull'autore:</h4> <br>
					<c:if test = "${author.surname != '#SURNAME_PLACEHOLDER'}">
						<em><a href="https://it.wikipedia.org/wiki/${author.name}_${author.surname}" target="_blank" ><i
								class="fa fa-wikipedia-w"></i>ikipedia - ${author.name} ${author.surname}</a></em>
					</c:if>
					<c:if test = "${author.surname == '#SURNAME_PLACEHOLDER'}">
						<em><a href="https://it.wikipedia.org/wiki/${author.name}" target="_blank" ><i
								class="fa fa-wikipedia-w"></i>ikipedia - ${author.name}</a></em>
					</c:if>
				</div>
			</div>
		</div>
	</div>
	 <div class="container">
            <div class="row">
				<div class="col-12">
					<c:if test="${checkAuthorBooks == false }">
					<div>
						<h2>Libri dell'autore</h2>
					</div>
					</c:if>
				</div>
            </div>
            <div class="row">
                <div class="col-12">
                    <div class="owl-carousel popular-slider">
						
						<!-- Start Single Product -->
						<c:forEach items="${authorBooks}" var="authorBook">
						<div class="single-product">
							<div class="product-img">
								<a href="<c:url value="/show_book/${authorBook.id}"/>"> <img
														class="default-img"
														src="<c:url value="/resources/img/cover_book/${authorBook.cover}"/>"
														alt="#"> <img class="hover-img" 
														src="<c:url value="/resources/img/cover_book/${authorBook.cover}"/>" alt="#">
														<c:if test="${authorBook.discount > 0}">
														<span class="price-dec">${authorBook.truncatedDiscount}%</span>
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
								<h4><a href="product-details.html"></a>${authorBook.title}</h4>
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
