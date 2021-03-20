<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>


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
														src="<c:url value="/resources/img/cover_book/${b.cover}"/>"
														alt="#"> <img class="hover-img" style="height: 400px; object-fit: contain"
														src="<c:url value="/resources/img/cover_book/${b.cover}"/>"
														alt="#">
														<c:if test="${b.discount > 0}">
														<span class="price-dec">${b.truncatedDiscount}%</span>
														</c:if>
													</a>
													<security:authorize access="hasRole('USER')">
													<div class="button-head">
														<div class="product-action"></div>
														<div class="product-action-2">
															<c:choose>
															<c:when test="${b.copies > 0}">
																<input type="hidden" value="1" id="amount_${b.id}">
																<button class="add_to_cart" data-book="${b.id}">Aggiungi al carrello</button>
															</c:when>
															<c:otherwise>
																<button >Non disponibile</button>
															</c:otherwise>
															</c:choose>
														</div>
													</div>
													</security:authorize>
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
								<div class="pagination" style="margin: auto">
									<div class="center" style="margin: auto">
										<ul class="pagination pagination-lg pagination-list">
											<c:if test="${page == 1}">
												<c:set value="disabled" var="disabledBegin" />
											</c:if>
											<li class="page-item ${disabledBegin}"><a class="page-link"
												data-page="${page-1}">&laquo;</a></li>
											<c:forEach begin="1" end="${maxPages}" step="1" varStatus="i">
												<c:choose>
													<c:when test="${i.index == page}">
														<c:set value="active" var="active" />
													</c:when>
													<c:otherwise>
														<c:set value="" var="active" />
													</c:otherwise>
												</c:choose>
												<li class="page-item ${active}"><a class="page-link"
													data-page="${i.index}">${i.index}</a></li>
											</c:forEach>
											<c:if test="${page == maxPages}">
												<c:set value="disabled" var="disabledEnd" />
											</c:if>
											<li class="page-item ${disabledEnd}"><a class="page-link"
												data-page="${page+1}">&raquo;</a></li>
										</ul>
									</div>
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

