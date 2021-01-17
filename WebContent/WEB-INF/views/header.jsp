<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
	
	<!-- Preloader -->
	<div class="preloader">
		<div class="preloader-inner">
			<div class="preloader-icon">
				<span></span>
				<span></span>
			</div>
		</div>
	</div>
	<!-- End Preloader -->
	<header class="header shop">
		<!-- Topbar -->
		<div class="topbar">
			<div class="container">
				<div class="row">
					<div class="col-lg-4 col-md-12 col-12">
						<!-- Top Left -->
						<div class="top-left">
							<ul class="list-main">
								<li><i class="ti-headphone-alt"></i> +39 06 5826478</li>
								<li><i class="ti-email"></i> support@bookshop.com</li>
							</ul>
						</div>
						<!--/ End Top Left -->
					</div>
					<div class="col-lg-8 col-md-12 col-12">
						<!-- Top Right -->
						<div class="right-content">
							<ul class="list-main">
								<li><i class="ti-location-pin"></i>Chi siamo</li>
								<li><i class="ti-alarm-clock"></i> <a href="#">Registrati</a></li>
								<!--  da mostrare solo agli utenti registrati  
								<li><i class="ti-user"></i> <a href="#">Account</a></li>  -->
								<li><i class="ti-power-off"></i><a href="login.html#">Login</a></li>
							</ul>
						</div>
						<!-- End Top Right -->
					</div>
				</div>
			</div>
		</div>
		<!-- End Topbar -->
		<div class="middle-inner">
			<div class="container">
				<div class="row">
					<div class="col-lg-2 col-md-2 col-12">
						<!-- Logo -->
						<div class="logo">
							<a href="index.html"><img src="images/logo.png" alt="logo"></a>
						</div>
						<!--/ End Logo -->
						<!-- Search Form -->
						<div class="search-top">
							<div class="top-search"><a href="#0"><i class="ti-search"></i></a></div>
							<!-- Search Form -->
							<div class="search-top">
								<form class="search-form"> <!-- inserire l'url su cui reindirizzare la barra di ricerca -->
									<input type="text" placeholder="Ricerca il tuo libro..." name="search">
									<button value="search" type="submit"><i class="ti-search"></i></button>
								</form>
							</div>
							<!--/ End Search Form -->
						</div>
						<!--/ End Search Form -->
						<div class="mobile-nav"></div>
					</div>
					<div class="col-lg-8 col-md-7 col-12">
						<div class="search-bar-top">
							<div class="search-bar">
								<!--/ non utilizzati al momento 
								<select>
									<option selected="selected">All Category</option>
									<option>watch</option>
									<option>mobile</option>
									<option>kid’s item</option>
								</select>
								
								<form>
									<input name="search" placeholder="Search Products Here....." type="search">
									<button class="btnn"><i class="ti-search"></i></button>
								</form> -->
							</div>
						</div>
					</div>
					<div class="col-lg-2 col-md-3 col-12">
						<div class="right-bar">
							<!-- loghi inutili 
							<div class="sinlge-bar">
								<a href="#" class="single-icon"><i class="fa fa-heart-o" aria-hidden="true"></i></a>
							</div>
							<div class="sinlge-bar">
								<a href="#" class="single-icon"><i class="fa fa-user-circle-o" aria-hidden="true"></i></a>
							</div>
							 -->
							<div class="sinlge-bar shopping">
								<a href="#" class="single-icon"><i class="ti-bag"></i> <span class="total-count">2</span></a>
								<!-- Carrello -->
								<div class="shopping-item">
									<div class="dropdown-cart-header">
										<span>2 Items</span> <!-- numero oggetti da nel carrello da sistemare con ajax e jquery -->
										<a href="#">Visualizza Carrello</a>
									</div>
									<ul class="shopping-list"> <!-- lista elementi nel carrello -->
										<!-- ogni elemento del carrello è in un tag <li> -->
										<li>
											<a href="#" class="remove" title="Elimina questo elemento"><i class="fa fa-remove"></i></a>
											<a class="cart-img" href="#"><img src="https://via.placeholder.com/70x70" alt="#"></a>
											<h4><a href="#">Nome oggetto nel carrello</a></h4>
											<p class="quantity">quantita oggetto <span class="amount">costo</span></p>
										</li>
									</ul>
									<div class="bottom">
										<div class="total">
											<span>Totale spesa nel carrello</span>
											<span class="total-amount">Prezzo totale</span>
										</div>
										<a href="checkout.html" class="btn animate">Procedi al pagamento</a>
									</div>
								</div>
								<!--/ End Shopping Item -->
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
<!-- Header Inner -->
		<div class="header-inner">
			<div class="container">
				<div class="cat-nav-head">
					<div class="row">
						
						<div class="col-lg-9 col-12">
							<div class="menu-area">
								<!-- Main Menu / NAVBAR -->
								<nav class="navbar navbar-expand-lg">
									<div class="navbar-collapse">	
										<div class="nav-inner">	
											<ul class="nav main-menu menu navbar-nav">
													<li class="active"><a href="#">Home</a></li>
													<li><a href="#">Generi<i class="ti-angle-down"></i></a>
														<ul class="dropdown">
														     <!-- da iterare con tutti i generi presenti nel db -->
															<li><a href="blog-single-sidebar.html">Genere 1</a></li>
														</ul>
													</li>												
													<li><a href="#">Sconti<span class="new">Sale</span></a></li>
													<li><a href="#">Acquisti<i class="ti-angle-down"></i></a>
														<ul class="dropdown">
															<li><a href="cart.html">Carrello</a></li>
															<li><a href="checkout.html">Procedi all'acquisto</a></li>
														</ul>
													</li>							
													<li><a href="contact.html">Contattaci</a></li>
												</ul>
										</div>
									</div>
								</nav>
								<!--/ End Main Menu -->	
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
		<!--/ End Header Inner -->
	</header>
	<!--/ End Header -->