<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>



<!-- Breadcrumbs -->
<div class="breadcrumbs">
	<div class="container">
		<div class="row">
			<div class="col-12">
				<div class="bread-inner">
					<ul class="bread-list">
						<li><a href="<c:url value="/" />">Home<i
								class="ti-arrow-right"></i></a></li>
						<li class="active"><a
							href="<c:url value="/show_genre/${single_genre}"/>">${single_genre}</a></li>
					</ul>
				</div>
			</div>
		</div>
	</div>
</div>
<!-- End Breadcrumbs -->


<!-- Start Product Area -->
<div class="product-area section">
	<div class="container">
		<div class="row">
			<div class="col-12">
				<div class="section-title">
					<h2>${single_genre}</h2>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-12">
				<div class="product-info">
					<div class="tab-content" id="myTabContent">
						<!-- Start Single Tab -->
						<div class="tab-pane fade show active" id="man" role="tabpanel">
							<div class="tab-single">
								<div class="row">
									<c:forEach items="${books}" var="b">
										<div class="col-xl-3 col-lg-4 col-md-4 col-12">
											<div class="single-product">
												<div class="product-img">
													<a href="<c:url value="/show_book/${b.id}"/>"> <img
														class="default-img" style="height: 400px; object-fit: contain"
														src="<c:url value="/resources/img/${b.cover}"/>"
														alt="#"> <img class="hover-img" style="height: 400px; object-fit: contain"
														src="<c:url value="/resources/img/${b.cover}"/>"
														alt="#">
														<c:if test="${b.discount > 0}">
														<span class="price-dec">${b.truncatedDiscount}%</span>
														</c:if>
													</a>
													<div class="button-head">
														<div class="product-action"></div>
														<div class="product-action-2">
															<a title="Add to cart" href="#">Aggiungi al carrello</a>
														</div>
													</div>
												</div>

												<div class="product-content">
													<h3>
														<a href="<c:url value="/show_book/${b.id}"/>">${b.title}</a>
													</h3>
													<div class="product-price">
														<c:if test="${b.discount > 0}">
															<span class="old">${b.formattedPrice}</span>
															<span>${b.formattedDiscountedPrice}</span>
														</c:if>
														<c:if test="${b.discount == 0}">
															<span>${b.formattedPrice}</span>
														</c:if>
													</div>
												</div>
											</div>
										</div>
									</c:forEach>
								</div>
							</div>
						</div>
						<!--/ End Single Tab -->
					</div>

				</div>
			</div>
		</div>
	</div>
</div>
<!-- End Product Area -->

