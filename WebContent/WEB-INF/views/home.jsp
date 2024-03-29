<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>

<!-- Start Shop Home List  -->
<section class="shop-home-list section">
	<div class="container">
		<div class="row">
			<div class="col-lg-4 col-md-6 col-12">
				<div class="row">
					<div class="col-12">
						<div class="shop-section-title">
							<h1>Top 5 libri più venduti</h1>
						</div>
					</div>
				</div>
				<!-- List 5 most selled books  -->
				<c:forEach items="${topFiveBestSellersBooks}" var="tBSBooks">
					<div class="single-list">
						<div class="row">
							<div class="col-lg-7 col-md-7 col-12">
								<div class="list-image overlay">
									<img src="<c:url value="/resources/img/cover_book/${tBSBooks.cover}"/>"
										alt="#" style="height: 300px; object-fit: contain"><a href="<c:url value="/show_book/${tBSBooks.id}"/>" class="buy"> 
										<i class="fa fa-eye"></i></a>
								</div>
							</div>
							<div class="col-lg-4 col-md-4 col-12 no-padding">
								<div class="content">
									<h4 class="title">
										<a href="<c:url value="/show_book/${tBSBooks.id}"/>">${tBSBooks.title}</a>
									</h4>
									<p class="price with-discount">${tBSBooks.formattedDiscountedPrice}</p>
								</div>
							</div>
						</div>
					</div>
				</c:forEach>
				<!-- End Single List  -->
			</div>
			<div class="col-lg-4 col-md-6 col-12">
				<div class="row">
					<div class="col-12">
						<div class="shop-section-title">
							<h1>Nuovi Arrivi</h1>
						</div>
					</div>
				</div>
				<!-- List 5 most recent books  -->
				<c:forEach items="${topFiveNewBooks}" var="tFNBooks">
					<div class="single-list">
						<div class="row">
							<div class="col-lg-7 col-md-7 col-12">
								<div class="list-image overlay">
									<img src="<c:url value="/resources/img/cover_book/${tFNBooks.cover}"/>"
										alt="#" style="height: 300px; object-fit: contain"><a href="<c:url value="/show_book/${tFNBooks.id}"/>" class="buy">
										<i class="fa fa-eye"></i></a>
								</div>
							</div>
							<div class="col-lg-4 col-md-4 col-12 no-padding">
								<div class="content">
									<h5 class="title">
										<a href="<c:url value="/show_book/${tFNBooks.id}"/>">${tFNBooks.title}</a>
									</h5>
									<p class="price with-discount">${tFNBooks.formattedDiscountedPrice}</p>
								</div>
							</div>
						</div>
					</div>
				</c:forEach>
				<!-- End Single List  -->
			</div>
			<div class="col-lg-4 col-md-6 col-12">
				<div class="row">
					<div class="col-12">
						<div class="shop-section-title">
							<h1>Top 5 autori più venduti</h1>
						</div>
					</div>
				</div>
				<!-- Start Single List  -->
				<c:forEach items="${topFiveAuthor}" var="tsAuthor">
					<div class="single-list author-list" >
						<div class="row">
							<div class="col-lg-7 col-md-7 col-12">
								<div class="list-image overlay" style=" height:300px">
									<img src="<c:url value="/resources/img/authors/${tsAuthor.photo}"/>"
										alt="author_photo" style="height:270px; border-radius: 10%; margin-top: 10px; margin-left:20px; object-fit: cover"> 
										<a href="<c:url value="/show_author/${tsAuthor.id}"/>" class="buy" style="margin-left:2px">
											<i class="fa fa-eye"></i>
										</a>
								</div>
							</div>
							<div class="col-lg-4 col-md-4 col-12 no-padding">
								<div class="content" style="margin-top: 70px; text-align: center;">
									<h5 class="title">
									<c:if test = "${tsAuthor.surname != '#SURNAME_PLACEHOLDER'}">
										<a href="<c:url value="/show_author/${tsAuthor.id}"/>" style="font-size: large">
											${tsAuthor.name}
											<br>
											${tsAuthor.surname}
										</a>
									</c:if>
									<c:if test = "${tsAuthor.surname == '#SURNAME_PLACEHOLDER'}">
										<a href="<c:url value="/show_author/${tsAuthor.id}"/>">${tsAuthor.name}</a>
									</c:if>
									</h5>
								</div>
							</div>
						</div>
					</div>
				</c:forEach>
				<!-- End Single List  -->
			</div>
		</div>
	</div>
</section>
<!-- End Shop Home List  -->
<!-- Start Product Area -->
<div class="product-area section" style="padding-top: 0px; margin-top: -40px;">
	<div class="container">
		<div class="row">
			<div class="col-12">
				<div class="section-title" style="margin-bottom: 10px;">
					<h2 id="moreview">Più visualizzati</h2>
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
									<c:forEach items="${topMostClickBook}" var="tMCB">
										<div class="col-xl-3 col-lg-4 col-md-4 col-12">
											<div class="single-product">
												<div class="product-img">
													<a href="<c:url value="/show_book/${tMCB.id}"/>"> <img
														class="default-img"
														src="<c:url value="/resources/img/cover_book/${tMCB.cover}"/>"
														alt="#" style="height: 400px; object-fit: contain"> <img class="hover-img"
														src="<c:url value="/resources/img/cover_book/${tMCB.cover}"/>"
														alt="#" style="height: 400px; object-fit: contain">
														<c:if test="${tMCB.discount > 0}">
														<span class="price-dec">${tMCB.truncatedDiscount}%</span>
														</c:if>
													</a>
													<security:authorize access="hasRole('USER')">
													<div class="button-head">
														<div class="product-action"></div>
														<div class="product-action-2">
															<c:choose>
															<c:when test="${tMCB.copies > 0}">
																<input type="hidden" value="1" id="amount_${tMCB.id}">
																<button class="add_to_cart" data-book="${tMCB.id}">Aggiungi al carrello</button>
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
														<a href="<c:url value="/show_book/${tMCB.id}"/>">${tMCB.title}</a>
													</h3>
													<div class="product-price">
														<c:if test="${tMCB.discount > 0}">
															<span class="old">${tMCB.formattedPrice}</span>
															<span>${tMCB.formattedDiscountedPrice}</span>
														</c:if>
														<c:if test="${tMCB.discount == 0}">
															<span>${tMCB.formattedPrice}</span>
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
											<li class="page-item ${disabledBegin}"><a class="page-link-home"
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
												<li class="page-item ${active}"><a class="page-link-home"
													data-page="${i.index}">${i.index}</a></li>
											</c:forEach>
											<c:if test="${page == maxPages}">
												<c:set value="disabled" var="disabledEnd" />
											</c:if>
											<li class="page-item ${disabledEnd}"><a class="page-link-home"
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


