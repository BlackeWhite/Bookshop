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
							href="<c:url value="/purchase_history" />">Cronologia
								acquisti</a></li>
					</ul>
				</div>
			</div>
		</div>
	</div>
</div>
<!-- End Breadcrumbs -->
<div
	class="container justify-content-center align-items-center users-list">
	<div class="col-12">
		<div class="purchase-history">
			<c:choose>
				<c:when test="${fn:length(orders) > 0 }">
					<div class="title">I miei acquisti</div>
					<c:forEach items="${orders}" var="order">
						<div class="order">
							<div class="info">
								<div class="row">
									<div class="col-3 field">ORDINE EFFETTUATO IL</div>
									<div class="col field">TOTALE</div>
									<div class="col field">SPEDIZIONE</div>
									<div class="col-3 field">SPEDITO A</div>
									<div class="col-3 field">PAGATO CON:</div>
									<div class="col field">ORDINE N°:</div>
								</div>
								<div class="row">
									<div class="col-3 value">${order.formattedDate}</div>
									<div class="col value">${order.formattedTotalExpense}</div>
									<div class="col value">${order.formattedShipmentCost}</div>
									<div class="col-3 value">${order.shipmentAddress}</div>
									<div class="col-3 value">${order.payment}</div>
									<div class="col value">${order.id}</div>
								</div>
							</div>
							<div class="books">
								<c:forEach items="${order.books}" var="bookOrder">
									<div class="single-book">
										<div class="row">
											<div class="col-10">
												<img
													src="<c:url value="/resources/img/cover_book/${bookOrder.book.cover}"/>" />
												<h4>
												<c:if test="${bookOrder.book.removed != 1}"> 
													<a href="<c:url value="/show_book/${bookOrder.book.id}"/>">${bookOrder.book.title}</a>
												</c:if>
												<c:if test="${bookOrder.book.removed == 1}"> 
													<a href="">${bookOrder.book.title} (Non più disponibile)</a>
												</c:if>
												</h4>
												<span class="field">Autori: </span> <span> 
												<c:forEach items="${bookOrder.book.authors}" var="author">  
												
												<c:if test="${author.surname != '#SURNAME_PLACEHOLDER'}">
														<a href="<c:url value="/show_author/${author.id}"/>">
																			- ${author.name} ${author.surname}</a>
												</c:if>
												<c:if test="${author.surname == '#SURNAME_PLACEHOLDER'}">
														<a href="<c:url value="/show_author/${author.id}"/>">
																			- ${author.name}</a>
												</c:if>
												</c:forEach>
												</span> <br> <span class="field">Data di pubblicazione:
												</span> <span>${bookOrder.book.publish}</span>
											</div>
											<div class="col price-data">
												<span class="field">Copie:</span> <span>
													${bookOrder.copies}</span> <br> <span class="field">Prezzo:</span>
												<span>${bookOrder.formattedPrice}</span>
											</div>
										</div>
									</div>
								</c:forEach>
							</div>
						</div>
					</c:forEach>
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
				</c:when>
				<c:otherwise>
					<div class="title">Non hai effettuato acquisti</div>
				</c:otherwise>
			</c:choose>
		</div>
	</div>
</div>