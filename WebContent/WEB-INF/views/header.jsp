<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<c:url value="logo.jpg" var="logoimg" />

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
							<security:authorize access="isAnonymous()">
								<li><i class="ti-alarm-clock"></i><a
									href="<c:url value="/register"/>">Registrati</a></li>
							</security:authorize>
							<!--  da mostrare solo agli utenti registrati  
								<li><i class="ti-user"></i> <a href="#">Account</a></li>  -->
							<security:authorize access="isAnonymous()">
								<li><i class="ti-power-off"></i><a
									href="<c:url value="/login"/>">Login</a></li>
							</security:authorize>
							<security:authorize access="isAuthenticated()">
								<li><i class="ti-power-off"></i><a
									href="<c:url value="/logout"/>">Logout</a></li>
							</security:authorize>
							<security:authorize access="isRememberMe()">
								<li><a href="#">test remember me</a></li>
							</security:authorize>
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
						<a href="<c:url value="/"/>"><img
							src="<c:url value="/resources/img/${logoimg}"/>" width="250"
							height="10" alt="logo"></a>
					</div>
					<!--/ End Logo -->
					<!-- Search Form -->
					<div class="search-top">
						<div class="top-search">
							<a href="#0"><i class="ti-search"></i></a>
						</div>
						<!-- Search Form -->
						<div class="search-top">
							<form class="search-form">
								<!-- inserire l'url su cui reindirizzare la barra di ricerca -->
								<input type="text" placeholder="Ricerca il tuo libro..."
									name="search">
								<button value="search" type="submit">
									<i class="ti-search"></i>
								</button>
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
							<c:url value="/advanced_search" var="search_action" />
							<form action="${search_action}" method="GET">
								<select name="search_by" id="search_by">
									<option id="title" value="title">Titolo</option>
									<option id="isbn" value="isbn">ISBN</option>
								</select> <input name="term" placeholder="Cerca i libri qui....."
									type="search">
								<button type="submit" class="btnn">
									<i class="ti-search"></i>
								</button>
							</form>
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
						<security:authorize access="isAuthenticated()">
							<div class="sinlge-bar shopping">
								<a href="<c:url value="/cart"/>" class="single-icon"><i
									class="ti-bag"></i> <span class="total-count">${cartTotalItems}</span></a>
								<!-- Carrello -->
								<div class="shopping-item">
									<div class="dropdown-cart-header">
										<c:choose>
											<c:when test="${cartTotalItems == 1}">
												<c:set value="o" var="chr" />
											</c:when>
											<c:otherwise>
												<c:set value="i" var="chr" />
											</c:otherwise>
										</c:choose>
										<span class="total-count-text">${cartTotalItems} Element${chr}</span>
										<!-- numero oggetti da nel carrello da sistemare con ajax e jquery -->
										<a href="<c:url value="/cart"/>">Visualizza Carrello</a>
									</div>
									<ul class="shopping-list">
										<!-- lista elementi nel carrello -->
										<!-- ogni elemento del carrello è in un tag <li> -->
										<c:forEach items="${user_cart}" var="cartElem">
											<li id="cart_${cartElem.book.id}"><a
												data-book="${cartElem.book.id}" class="remove"
												title="Elimina questo elemento"><i class="fa fa-remove"></i></a>
												<a class="cart-img"
												href="<c:url value="/show_book/${cartElem.book.id}"/>"><img
													src="<c:url value="/resources/img/${cartElem.book.cover}"/>"
													alt="#"></a>
												<h4>
													<a href="<c:url value="/show_book/${cartElem.book.id}"/>">${cartElem.book.title}</a>
												</h4>
												<p class="quantity">Copie: ${cartElem.copies} - Totale:
													${cartElem.formattedElementTotalPrice}</p>
											</li>
										</c:forEach>
									</ul>
									<div class="bottom">
										<div class="total">
											<span>Spesa Totale</span> <span class="total-amount">${cartTotalPrice}</span>
										</div>
										<c:if test="${cartTotalItems > 0 }">
											<a href="checkout.html" class="btn animate">Procedi al
												pagamento</a>
										</c:if>
									</div>
								</div>
								<!--/ End Shopping Item -->
							</div>
						</security:authorize>
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
											<li class="active"><a href="<c:url value="/"/>">Home</a></li>
											<li><a href="#">Generi<i class="ti-angle-down"></i></a>
												<ul class="dropdown">

													<c:forEach items="${allGenres}" var="genres">
														<li><a
															href="<c:url value="/show_genre/${genres.name}"/>">${genres.name}</a></li>
													</c:forEach>
												</ul></li>
											<li><a href="#">Sconti<span class="new">Sale</span></a></li>
											<li><a href="#">Acquisti<i class="ti-angle-down"></i></a>
												<ul class="dropdown">
													<li><a href="<c:url value="/cart"/>">Carrello</a></li>
													<li><a href="checkout.html">Procedi all'acquisto</a></li>
												</ul></li>
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