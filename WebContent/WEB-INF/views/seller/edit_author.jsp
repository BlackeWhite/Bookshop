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
								Personale - Venditore<i class="ti-arrow-right"></i>
						</a></li>
						<li class="active"><a
							href="<c:url value="/seller/addition_book"/>">Aggiungi libro<i class="ti-arrow-right"></i></a></li>
						<li class="active"><a href="<c:url value="/seller/authors_seller"/>">Lista Autori
							<i class="ti-arrow-right"></i></a></li>
						<li class="active"><a href="<c:url value="/seller/edit_author"/>">Modifica Autore
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
						<li><a href="<c:url value="/seller/authors_seller"/>"> Lista Autori</a></li>
						<li><a href="<c:url value="/seller/analysis_book"/>">Analisi
								delle vendite</a></li>
						<li><a href="<c:url value="/account"/>"> Modifica dati personali </a></li>
					</div>
					<!--/ End Single Widget -->
					
				</div>
			</div>
			<div class="col-lg-9 col-md-8 col-12">
				<div class="row">
					<div class="col-12">
					</div>
				</div>
				<div class="row">
				 <c:url value="/seller/author_edited/${author.id}" var="author_edit_action"/>
				 	<form:form id="register-form" action="${author_edit_action}" modelAttribute="author" class="form" method="post" enctype="multipart/form-data">
						<fieldset>
						<legend> Modifica Autore  </legend>
								<br>
								<form:label path="name"><b>Nome dell'autore</b></form:label>
								<form:input required="required" placeholder="Nome" type="text" path="name" id="authorsName" class = "inputview"/>
								<form:errors path="name" cssClass="validation-error" />
								<br>
								<form:label path="surname"><b>Cognome dell'autore</b></form:label>
						        <form:input placeholder="Cognome" type="text" path="surname" id="surname" class = "inputview"/>
						        <form:errors path="surname" cssClass="validation-error" />
						        <br>
						        <br>
						        <form:label path="birthdate"><b>Data di nascita dell'autore</b></form:label>
								<form:input required="required" type="date" path="birthdate" id="birthdate" class = "inputview"/>	
								<form:errors path="birthdate" cssClass="validation-error" />
								<br>
								<br> 
								<form:label path="nationality"><b>Nazionalità dell'autore</b></form:label>
						        <form:input placeholder="Nazionalità" type="text" path="nationality" id="nationality" class = "inputview"/>
						        <form:errors path="nationality" cssClass="validation-error" />
						        <br>
						        <br>
							    <form:label path="biography"><b>Breve biografia dell'autore</b></form:label> <br>
								<form:textarea  placeholder="Biografia dell'autore" path="biography" id="biography" maxlength="250" rows="4" cols="50" />
								<form:errors path="biography" cssClass="validation-error" />
								<br>
								<br>
								<form:label path="photoFile"><b>Carica la foto dell'autore</b></form:label></td>
                                <form:input type="file" name="photoFile" path="photoFile" class = "inputview"/>
								<form:errors path="photoFile" cssClass="validation-error" />
								<br>
							</fieldset>
						<br>
						<br>
				 <button type="submit" name="submit" class="btn with-pass-conf">Modifica autore</button>
				 <a href="<c:url value="/seller/"/>" >Annulla </a>
					</form:form>
				</div>
			</div>
		</div>
	</div>
</section>

		
