<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<!-- Hidden input to get the current search term in the javascript -->
<input id="search_term" type="hidden" value="${term}">
<!-- Variabili per memorizzare i valori dello slider -->
<!-- Valorizzati al caricamento della pagina e modificati al movimento dello slider-->
<script type="text/javascript">
	var currMin = ${priceMin};
	var currMax = ${priceMax};
</script>

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
							href="<c:url value="/advanced_search"/>">Ricerca Avanzata</a></li>
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
					<div class="single-widget range">
						<h3 class="title">Generi</h3>
						<ul class="check-box-list">
							<c:forEach items="${allGenres}" var="g">
								<li><label class="checkbox-inline" for="${g.name}"><input
										id="${g.name}" name="genre_checkbox" type="checkbox">${g.name}<span
										class="count"> (${books_for_genre[g.name]}) </span></label></li>
							</c:forEach>
						</ul>
					</div>
					<!--/ End Single Widget -->
					<!-- Shop By Price -->
					<div class="single-widget range">
						<h3 class="title">Ricerca per Prezzo</h3>
						<div class="price-filter">
							<div class="price-filter-inner">
								<div id="slider-range"
									class="ui-slider ui-slider-horizontal ui-widget ui-widget-content ui-corner-all"></div>
								<div class="price_slider_amount">
									<div class="label-input">
										<span>Intervallo:</span><input type="text" id="amount" name="price"
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
						<h3 class="title">Libri Popolari</h3>
						<!-- Single Post -->
						<c:forEach items="${best_sellers}" var="bs">
							<div class="single-post first">
								<div class="image">
									<img src="<c:url value="/resources/img/${bs.cover}"/>" alt="#">
								</div>
								<div class="content">
									<h5>
										<a href="<c:url value="/show_book/${bs.id}"/>">${bs.title}</a>
									</h5>
									<p class="price">${bs.formattedPrice}&euro;</p>
									<p class="price">${bs.publish}</p>
								</div>
							</div>
						</c:forEach>
						<!-- End Single Post -->
					</div>
					<!--/ End Single Widget -->
					<!-- Single Widget -->
					<div class="single-widget category">
						<h3 class="title">Autori popolari</h3>
						<ul class="categor-list">
							<c:forEach items="${top_authors}" var="a">
								<li><a
									href="<c:url value="/advanced_search?authorId=${a.id}"/>">${a.name}
										${a.surname}</a></li>
							</c:forEach>
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
									<label>Mostra :</label> <select id="books_per_page">
										<option id="6_op" data-max="6">06</option>
										<option id="9_op" data-max="9">09</option>
										<option id="12_op" data-max="12">12</option>
									</select>
								</div>
								<div class="single-shorter">
									<label>Ordina per:</label> <select id="order_by">
										<option id="title_ASC" value="title_ASC">Titolo</option>
										<option id="price_DESC" value="price_DESC">Prezzo
											decrescente</option>
										<option id="price_ASC" value="price_ASC">Prezzo
											crescente</option>
									</select>
								</div>
							</div>
							<ul class="view-mode">
								<li>
									<button class="apply_filter">Applica filtri</button>
								</li>
								<li>
									<button class="reset_filter">Elimina filtri</button>
								</li>
							</ul>
						</div>
						<!--/ End Shop Top -->
					</div>
				</div>
				<div class="row">
					<c:forEach items="${books}" var="b">
						<div class="col-lg-4 col-md-6 col-12">
							<div class="single-product">
								<div class="product-img">
									<a href="<c:url value="/show_book/${b.id}"/>"> <img
										class="default-img" style="height: 400px; object-fit: contain"
										src="<c:url value="/resources/img/${b.cover}"/>" alt="#">
										<img class="hover-img"
										style="height: 400px; object-fit: contain"
										src="<c:url value="/resources/img/${b.cover}"/>">
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
											<c:url value="/add_to_cart" var="add_action" />
											<form action="${add_action}" method="POST">
												<input type="hidden" value="${b.id}" name="bookID">
												<button type="submit">Aggiungi al carrello</button>
											</form>
										</div>
									</div>
								</div>
								<div class="product-content">
									<h3>
										<a href="product-details.html">${b.title}</a>
									</h3>
									<div class="product-price">
										<span>${b.formattedPrice} &euro;</span>
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
								 <c:set value="disabled" var="disabledBegin"/>
							</c:if>
							<li class="page-item ${disabledBegin}"><a class="page-link" data-page="${page-1}">&laquo;</a></li>
							<c:forEach begin="1" end="${maxPages}" step="1" varStatus="i">
								<c:choose>
									<c:when test="${i.index == page}"> 
								 		<c:set value="active" var="active"/>
									</c:when>
									<c:otherwise>
										<c:set value="" var="active"/>
									</c:otherwise>
								</c:choose>
								<li class="page-item ${active}"><a class="page-link" data-page="${i.index}">${i.index}</a>
								</li>
							</c:forEach>
							<c:if test="${page == maxPages}"> 
							 	<c:set value="disabled" var="disabledEnd"/>
							</c:if>
							<li class="page-item ${disabledEnd}"><a class="page-link" data-page="${page+1}">&raquo;</a>
							</li>
						</ul>
					</div>
				</div>
			</div>
		</div>
	</div>
</section>
<!--/ End Product Style 1  -->
