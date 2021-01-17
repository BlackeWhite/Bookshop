<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	
<!-- Breadcrumbs -->
<div class="breadcrumbs">
	<div class="container">
		<div class="row">
			<div class="col-12">
				<div class="bread-inner">
					<ul class="bread-list">
						<li><a href="index1.html">Home<i class="ti-arrow-right"></i></a></li>
						<li class="active"><a href="blog-single.html">Shop Grid</a></li>
					</ul>
				</div>
			</div>
		</div>
	</div>
</div>
<!-- End Breadcrumbs -->

<!-- Product Style -->
<section class="product-area shop-sidebar shop section">
	<div class="container">
		<div class="row">
			<div class="col-lg-3 col-md-4 col-12">
				<div class="shop-sidebar">
					<!-- Single Widget -->
					<div class="single-widget category">
						<h3 class="title">Generi</h3>
						<ul class="categor-list">
							<li><a href="#">Avventura</a></li>
							<li><a href="#">Giallo</a></li>
							<li><a href="#">Fantasy</a></li>
							<li><a href="#">Divulgativo</a></li>
							<li><a href="#">Biografia</a></li>
							<li><a href="#">Fantascienza</a></li>
							<li><a href="#">Fumetto</a></li>
						</ul>
					</div>
					<!--/ End Single Widget -->
					<!-- Shop By Price -->
					<div class="single-widget range">
						<h3 class="title">Shop by Price</h3>
						<div class="price-filter">
							<div class="price-filter-inner">
								<div id="slider-range"></div>
								<div class="price_slider_amount">
									<div class="label-input">
										<span>Range:</span><input type="text" id="amount" name="price"
											placeholder="Add Your Price" />
									</div>
								</div>
							</div>
						</div>
						<ul class="check-box-list">
							<li><label class="checkbox-inline" for="1"><input
									name="news" id="1" type="checkbox">$20 - $50<span
									class="count">(3)</span></label></li>
							<li><label class="checkbox-inline" for="2"><input
									name="news" id="2" type="checkbox">$50 - $100<span
									class="count">(5)</span></label></li>
							<li><label class="checkbox-inline" for="3"><input
									name="news" id="3" type="checkbox">$100 - $250<span
									class="count">(8)</span></label></li>
						</ul>
					</div>
					<!--/ End Shop By Price -->
					<!-- Single Widget -->
					<div class="single-widget recent-post">
						<h3 class="title">Recent post</h3>
						<!-- Single Post -->
						<div class="single-post first">
							<div class="image">
								<img src="https://via.placeholder.com/75x75" alt="#">
							</div>
							<div class="content">
								<h5>
									<a href="#">Girls Dress</a>
								</h5>
								<p class="price">$99.50</p>
								<ul class="reviews">
									<li class="yellow"><i class="ti-star"></i></li>
									<li class="yellow"><i class="ti-star"></i></li>
									<li class="yellow"><i class="ti-star"></i></li>
									<li><i class="ti-star"></i></li>
									<li><i class="ti-star"></i></li>
								</ul>
							</div>
						</div>
						<!-- End Single Post -->
						<!-- Single Post -->
						<div class="single-post first">
							<div class="image">
								<img src="https://via.placeholder.com/75x75" alt="#">
							</div>
							<div class="content">
								<h5>
									<a href="#">Women Clothings</a>
								</h5>
								<p class="price">$99.50</p>
								<ul class="reviews">
									<li class="yellow"><i class="ti-star"></i></li>
									<li class="yellow"><i class="ti-star"></i></li>
									<li class="yellow"><i class="ti-star"></i></li>
									<li class="yellow"><i class="ti-star"></i></li>
									<li><i class="ti-star"></i></li>
								</ul>
							</div>
						</div>
						<!-- End Single Post -->
						<!-- Single Post -->
						<div class="single-post first">
							<div class="image">
								<img src="https://via.placeholder.com/75x75" alt="#">
							</div>
							<div class="content">
								<h5>
									<a href="#">Man Tshirt</a>
								</h5>
								<p class="price">$99.50</p>
								<ul class="reviews">
									<li class="yellow"><i class="ti-star"></i></li>
									<li class="yellow"><i class="ti-star"></i></li>
									<li class="yellow"><i class="ti-star"></i></li>
									<li class="yellow"><i class="ti-star"></i></li>
									<li class="yellow"><i class="ti-star"></i></li>
								</ul>
							</div>
						</div>
						<!-- End Single Post -->
					</div>
					<!--/ End Single Widget -->
					<!-- Single Widget -->
					<div class="single-widget category">
						<h3 class="title">Autori popolari</h3>
						<ul class="categor-list">
							<li><a href="#">Alessandro Manzoni</a></li>
							<li><a href="#">Italo Svevo</a></li>
							<li><a href="#">J. R. R. Tolkien</a></li>
							<li><a href="#">Dante Alighieri</a></li>
							<li><a href="#">Tom Clancy</a></li>
						</ul>
					</div>
					<!--/ End Single Widget -->
				</div>
			</div>
			<div class="col-lg-9 col-md-8 col-12">
				<div class="row">
					<div class="col-12">
						<!-- Shop Top -->
						<div class="shop-top">
							<div class="shop-shorter">
								<div class="single-shorter">
									<label>Show :</label> <select>
										<option selected="selected">09</option>
										<option>15</option>
										<option>25</option>
										<option>30</option>
									</select>
								</div>
								<div class="single-shorter">
									<label>Sort By :</label> <select>
										<option selected="selected">Name</option>
										<option>Price</option>
										<option>Size</option>
									</select>
								</div>
							</div>
							<ul class="view-mode">
								<li class="active"><a href="shop-grid.html"><i
										class="fa fa-th-large"></i></a></li>
								<li><a href="shop-list.html"><i class="fa fa-th-list"></i></a></li>
							</ul>
						</div>
						<!--/ End Shop Top -->
					</div>
				</div>
				<div class="row">
					<div class="col-lg-4 col-md-6 col-12">
						<div class="single-product">
							<div class="product-img">
								<a href="product-details.html"> <img class="default-img"
									src="https://via.placeholder.com/550x750" alt="#"> <img
									class="hover-img" src="https://via.placeholder.com/550x750"
									alt="#">
								</a>
								<div class="button-head">
									<div class="product-action">
										<a data-toggle="modal" data-target="#exampleModal"
											title="Quick View" href="#"><i class=" ti-eye"></i><span>Quick
												Shop</span></a> <a title="Wishlist" href="#"><i class=" ti-heart "></i><span>Add
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
									<a href="product-details.html">Women Hot Collection</a>
								</h3>
								<div class="product-price">
									<span>$29.00</span>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</section>
<!--/ End Product Style 1  -->
