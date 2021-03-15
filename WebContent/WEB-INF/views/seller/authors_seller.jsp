<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>


<!-- Breadcrumbs -->
<div class="breadcrumbs">
	<div class="container">
		<div class="row">
			<div class="col-12">
				<div class="bread-inner">
					<ul class="bread-list">
						<li><a href="<c:url value="/" />">Home<i
								class="ti-arrow-right"></i></a></li>
						<li class="active"><a href="<c:url value="/seller/"/>">Area
								Personale - Venditore<i
								class="ti-arrow-right"></i></a></li>
							<li class="active"><a href="<c:url value="/seller/authors_seller"/>">Lista di autori
							</a></li>
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
					<div class="single-widget range">
						<a href="<c:url value="/seller/"/>"><h3 class="title">Menu</h3></a>
						<c:url value="/seller/addition_book" var="addition_book" />
						<li><a href="${addition_book}"> Aggiungi un libro in
								vendita </a></li>
						<li><a href="<c:url value="/seller/authors_seller"/>"> Lista di autori</a></li>
						<li><a href="<c:url value="/seller/analysis_book"/>">Analisi
								delle vendite</a></li>
						<li><a href="<c:url value="/account"/>"> Modifica dati
								personali </a></li>
					</div>
					<!--/ End Single Widget -->
				</div>
			</div>
			<div class="col-lg-9 col-md-8 col-12">
				<div
					class="container justify-content-center align-items-center users-list">
					<div class="col-12">
						<div class="home-seller">
							<h4 class="title" style="color: ${msgColor}; text-size: 13px;">${message}</h4>
							<c:choose>
								<c:when test="${fn:length(authorsPerSeller) > 0 }">
									<div class="title">Autori legati ai miei libri</div>
									<c:forEach items="${authorsPerSeller}" var="authorsPerSeller">
										<div class="order" id="book_${authorsPerSeller.id}">
											<div class="info">
												<div class="row">
													<div class="col-3 field">
													<h4>
													<c:if test="${authorsPerSeller.surname != '#SURNAME_PLACEHOLDER'}">
														<a href="<c:url value="/show_author/${authorsPerSeller.id}"/>">${authorsPerSeller.name} ${authorsPerSeller.surname}</a>
													</c:if>
													<c:if test="${authorsPerSeller.surname == '#SURNAME_PLACEHOLDER'}">
														<a href="<c:url value="/show_author/${authorsPerSeller.id}"/>">${authorsPerSeller.name}</a>
													</c:if></h4></div>
													<div class="col field">
														<!-- Div utile per avere modifica ed elimina nell'ultima colonna -->
													</div>
													<div class="col-3 field">
														<button bookId="${authorsPerSeller.id}" class="generic-button">
															<a href="<c:url value="/show_author/${authorsPerSeller.id}"/>"><i
																class="fa fa-remove">VISUALIZZA </i></a>
														</button>
														<button bookId="${authorsPerSeller.id}" class="generic-button">
															<a href="<c:url value="/seller/edit_author/${authorsPerSeller.id}"/>"><i
																class="fa fa-remove">MODIFICA</i></a>
														</button>
													</div>
												</div>
											</div>
											<div class="books">
												<div class="single-book">
													<div class="row">
														<div class="col-10">
															<img
																src="<c:url value="/resources/img/cover_book/${authorsPerSeller.photo}"/>" />
															<h4>
																<c:if test = "${authorsPerSeller.surname != '#SURNAME_PLACEHOLDER'} ">
																	<a href="<c:url value="/show_author/${authorsPerSeller.id}"/>">${authorsPerSeller.name} ${authorsPerSeller.surname}</a>
																</c:if>
																<c:if test = "${authorsPerSeller.surname == '#SURNAME_PLACEHOLDER'} ">
																	<a href="<c:url value="/show_author/${authorsPerSeller.id}"/>">${authorsPerSeller.name}</a>
																</c:if>
															</h4>
															
															</span> <span class="field">Data di
																nascita: </span> <span>${authorsPerSeller.birthdate} </span> <br>
															<span class="field">Nazionalità: </span> <span>${authorsPerSeller.nationality}
															</span><br>
															<span class="field">Breve biografia: </span> <span>${authorsPerSeller.biography}
															</span>
														</div>

													</div>
												</div>
											</div>
										</div>

									</c:forEach>
									<div class="pagination" style="margin: auto">
										<div class="center" style="margin: auto">
											<ul class="pagination pagination-lg pagination-list">
												<c:if test="${page == 1}">
													<c:set value="disabled" var="disabledBegin" />
												</c:if>
												<li class="page-item ${disabledBegin}"><a
													class="page-link" data-page="${page-1}">&laquo;</a></li>
												<c:forEach begin="1" end="${maxPages}" step="1"
													varStatus="i">
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
												<li class="page-item ${disabledEnd}"><a
													class="page-link" data-page="${page+1}">&raquo;</a></li>
											</ul>
										</div>
									</div>
								</c:when>
								<c:otherwise>
									<div class="title">
										<h1>Nessun autore legato al tuo account</h1>
										<c:url value="/seller/addition_book" var="addition_book" />
										<a href="${addition_book}">
											<h2>Aggiungi un libro in vendita</h2>
										</a>
									</div>
								</c:otherwise>
							</c:choose>
						</div>
					</div>
				</div>
			</div>

		</div>
	</div>
</section>
<!--/ End Product Style 1  -->
<!-- Single Widget -->

