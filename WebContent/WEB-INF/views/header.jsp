<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<c:url value="logo.png" var="logoimg" />

<header class="header shop">
	<div class="middle-inner">
		<div class="container">
			<div class="row">
				<div class="col">
					<!-- Logo -->
					<div class="logo">
						<a href="<c:url value="/"/>"><img
							src="<c:url value="/resources/img/${logoimg}"/>"
							style="height: 35px;" alt="logo"></a>
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
				<div class="col">
					<div class="search-bar-top">
						<div class="search-bar">
							<c:url value="/advanced_search" var="search_action" />
							<form action="${search_action}" method="GET">
								<select name="search_by" id="search_by">
									<option id="title" value="title">Titolo</option>
									<option id="isbn" value="isbn">ISBN</option>
									<option id="author" value="author">Autore</option>
								</select> <input name="term" placeholder="Cerca i libri qui....."
									type="search">
								<button type="submit" class="btnn">
									<i class="ti-search"></i>
								</button>
							</form>
						</div>
					</div>
				</div>
				
				<div class="col">
					<div class="right-bar">
						<!-- loghi inutili 
							<div class="sinlge-bar">
								<a href="#" class="single-icon"><i class="fa fa-heart-o" aria-hidden="true"></i></a>
							</div>
							<div class="sinlge-bar">
								<a href="#" class="single-icon"><i class="fa fa-user-circle-o" aria-hidden="true"></i></a>
							</div>
							 -->
						<c:set
								value="${requestScope['javax.servlet.forward.request_uri']}"
								var="uri" />
						<security:authorize access="hasRole('USER')">
							<c:url value="/cart" var="carturl" />
							<c:url value="/checkout" var="checkouturl" />
							<c:if test="${uri != carturl && uri != checkouturl}">
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
											<span class="total-count-text">${cartTotalItems}
												Element${chr}</span>
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
													<a class="cart-img" style="border: none"
													href="<c:url value="/show_book/${cartElem.book.id}"/>"><img
														src="<c:url value="/resources/img/cover_book/${cartElem.book.cover}"/>"
														alt="#" style="object-fit: contain; border: none"></a>
													<h4>
														<a href="<c:url value="/show_book/${cartElem.book.id}"/>">${cartElem.book.title}</a>
													</h4>
													<p class="quantity">Copie: ${cartElem.copies} - Totale:
														${cartElem.formattedElementTotalPrice}</p></li>
											</c:forEach>
										</ul>
										<div class="bottom">
											<div class="total">
												<span>Spesa Totale</span> <span class="total-amount">${cartTotalPrice}</span>
											</div>
											<c:if test="${cartTotalItems > 0 }">
												<a href="<c:url value="/checkout"/>"
													class="btn animate mini-checkout">Procedi al pagamento</a>
											</c:if>
										</div>
									</div>
									<!--/ End Shopping Item -->
								</div>
							</c:if>
						</security:authorize>
					</div>
				
				<!-- Top Right -->
					<div class="right-content">
						<ul class="list-main">
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
							<security:authorize access="hasAnyRole('USER', 'ADMIN')">
								<li><i class="ti-user"></i> <a
									href="<c:url value="/account"/>">Il mio account</a></li>
							</security:authorize>
							<security:authorize access="hasRole('SELLER')">
								<li><i class="ti-user"></i> <a
									href="<c:url value="/seller/"/>">Area Personale</a></li>
							</security:authorize>
							<security:authorize access="isAuthenticated()">
								<li><i class="ti-power-off"></i><a
									href="<c:url value="/logout"/>">Logout</a></li>
							</security:authorize>
						</ul>
					</div>
				<!-- End Top Right -->
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
											<c:url value="/" var="homeUrl" />
											<c:if test="${uri == homeUrl}">
												<c:set value="active" var="active1" />
											</c:if>
											<li class="${active1}"><a href="${homeUrl}">Home</a></li>

											<c:if test="${fn:contains(uri,'show_genre')}">
												<c:set value="active" var="active2" />
											</c:if>
											<li class="${active2}"><a href="#">Generi<i
													class="ti-angle-down"></i></a>
												<ul class="dropdown">

													<c:forEach items="${allGenres}" var="genres">
														<li><a
															href="<c:url value="/show_genre/${genres.name}"/>">${genres.name}</a></li>
													</c:forEach>
												</ul></li>
												<c:url value="/sales" var="salesUrl" />
												<c:if test="${uri == salesUrl}">
												<c:set value="active" var="active5" />
											</c:if>
											<li class="${active5}"><a  href="<c:url value="/sales"/>">Sconti</a></li>
											
											<security:authorize access="hasRole('USER')">
											<c:url value="/purchase_history" var="purchhisturl" />
											<c:if test="${uri == carturl || uri == checkouturl || uri == purchhisturl}">
												<c:set value="active" var="active4" />
											</c:if>
											<li class="${active4}"><a href="#">Acquisti<i
													class="ti-angle-down"></i></a>
												<ul class="dropdown">
													<li><a href="${carturl}">Carrello</a></li>
													<c:if test="${cartTotalItems > 0 }">
														<li><a href="${checkouturl}">Procedi all'acquisto</a></li>
													</c:if>
													<li><a href="${purchhisturl}">Cronologia acquisti</a></li>
												</ul></li>
											</security:authorize>
											<li><a href="<c:url value='/about_us'/>">Chi siamo</a></li>
											<security:authorize access="hasRole('ADMIN')">
												<c:url value="/admin/add_seller" var="addSeller" />
												<c:url value="/admin/sellers_list" var="sellersList" />
												<c:url value="/admin/buyers_list" var="buyersList" />
												<c:url value="/admin/manage_genres" var="manageGenres" />
												<c:url value="/admin/manage_coupons" var="manageCoupons" />
												<c:if
													test="${fn:contains(uri,'admin')}">
													<c:set value="active" var="active3" />
												</c:if>
												<li class="${active3}"><a href="#">Funzioni Admin<i
														class="ti-angle-down"></i></a>
													<ul class="dropdown">
														<li><a href="${addSeller}">Aggiungi Venditore</a></li>
														<li><a href="${sellersList}">Lista Venditori</a></li>
														<li><a href="${buyersList}">Lista Acquirenti</a></li>
														<li><a href="${manageCoupons}">Gestisci coupon</a></li>
														<li><a href="${manageGenres}">Gestisci generi</a></li>
													</ul></li>
											</security:authorize>
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
	<div class="bookshop-popup">
		<span class="popup-message">Libro aggiunto correttamente</span>
	</div>
</header>
<!--/ End Header -->