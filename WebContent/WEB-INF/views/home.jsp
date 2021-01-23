<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

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
							<div class="col-lg-6 col-md-6 col-12">
								<div class="list-image overlay">
									<img src="<c:url value="/resources/img/${tBSBooks.cover}"/>"
										alt="#"> <a href="#" class="buy"><i
										class="fa fa-shopping-bag"></i></a>
								</div>
							</div>
							<div class="col-lg-6 col-md-6 col-12 no-padding">
								<div class="content">
									<h4 class="title">
										<a href="<c:url value="/show_book/${tBSBooks.id}"/>">${tBSBooks.title}</a>
									</h4>
									<p class="price with-discount">${tBSBooks.price}</p>
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
							<div class="col-lg-6 col-md-6 col-12">
								<div class="list-image overlay">
									<img src="<c:url value="/resources/img/${tFNBooks.cover}"/>"
										alt="#"> <a href="#" class="buy"><i
										class="fa fa-shopping-bag"></i></a>
								</div>
							</div>
							<div class="col-lg-6 col-md-6 col-12 no-padding">
								<div class="content">
									<h5 class="title">
										<a href="<c:url value="/show_book/${tFNBooks.id}"/>">${tFNBooks.title}</a>
									</h5>
									<p class="price with-discount">${tFNBooks.price}</p>
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
					<div class="single-list">
						<div class="row">
							<div class="col-lg-6 col-md-6 col-12">
								<div class="list-image overlay">
									<img src="https://via.placeholder.com/115x140" alt="#"> <a
										href="#" class="buy"><i class="fa fa-shopping-bag"></i></a>
								</div>
							</div>
							<div class="col-lg-6 col-md-6 col-12 no-padding">
								<div class="content">
									<h5 class="title">
										<a href="#">${tsAuthor.name} ${tsAuthor.surname}</a>
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
<div class="product-area section">
	<div class="container">
		<div class="row">
			<div class="col-12">
				<div class="section-title">
					<h2>PIù visualizzati</h2>
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
													<a href="product-details.html"> <img
														class="default-img"
														src="<c:url value="/resources/img/${tMCB.cover}"/>"
														alt="#"> <img class="hover-img"
														src="https://via.placeholder.com/550x750" alt="#">
													</a>
													<div class="button-head">
														<div class="product-action">
															<a title="Wishlist" href="#"><i class=" ti-heart "></i><span>Add
																	to Wishlist</span></a> <a title="Compare" href="#"><i
																class="ti-bar-chart-alt"></i><span>Add to Compare</span></a>
														</div>
														<div class="product-action-2">
															<a title="Add to cart" href="#">Add to cart</a>
														</div>
													</div>
												</div>

												<div class="product-content">
													<h3>
														<a href="product-details.html">${tMCB.title}</a>
													</h3>
													<div class="product-price">
														<span>${tMCB.price}</span>
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
<!-- Start Cowndown Area -->
<section class="cown-down">
	<div class="section-inner ">
		<div class="container-fluid">
			<div class="row">
				<div class="col-lg-6 col-12 padding-right">
					<div class="image">
						<img src="https://via.placeholder.com/750x590" alt="#">
					</div>
				</div>
				<div class="col-lg-6 col-12 padding-left">
					<div class="content">
						<div class="heading-block">
							<p class="small-title">Deal of day</p>
							<h3 class="title">Beatutyful dress for women</h3>
							<p class="text">Suspendisse massa leo, vestibulum cursus
								nulla sit amet, frungilla placerat lorem. Cars fermentum,
								sapien.</p>
							<h1 class="price">
								$1200 <s>$1890</s>
							</h1>
							<div class="coming-time">
								<div class="clearfix" data-countdown="2021/02/30"></div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</section>
<!-- /End Cowndown Area -->

