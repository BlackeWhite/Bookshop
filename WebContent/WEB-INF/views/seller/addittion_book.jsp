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
						<li class="active"><a
							href="<c:url value="/advanced_search"/>">Area Personale - Venditore</a></li>
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
						<h3 class="title">Menu</h3>
						<c:url value="/addittion_book" var="addition_book"/>
						<li><a href="${addition_book}"> Aggiungi un libro in vendita </a></li>
						<li><a href=""> Modifica un libro in vendita </a></li>
						<li><a href=""> Metti in sconto un libro in vendita </a></li>
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
				  <c:url value="/seller/add_book" var="add_book_action"/>
					<form:form id="register-form" action="${add_book_action}" modelAttribute="newBook" class="form" method="post">
						<fieldset>
							<legend>Autore del libro</legend>
							
								<c:forEach items="${authors}" var="author"> 
							     <p hidden>${i=i+1}</p> 
										<form:checkbox id="${author}" name="${author}" path="authors" value="${author}" label="${author}" />
										 &thinsp; &nbsp;
									    <form:errors path="authors" cssClass="validation-error"/>
									<c:if test="${i%6 == 0 }"> <br></c:if>	 
								</c:forEach>
							
						</fieldset>
						<fieldset>
						<legend> Informazioni Libro  </legend>
								<form:label path="title">Nome Libro</form:label>
								<form:input required="required" placeholder="nome del libro" type="text" path="title" id="title" />
								<form:errors path="title" cssClass="validation-error" />
							
								<form:label path="title">ISBN</form:label> 
								<form:input required="required" placeholder="ISBN del libro" type="text" path="isbn" id="isbn"  maxlength="13"/>
								<form:errors path="isbn" cssClass="validation-error" />
                            	<br>
								<form:label path="title">Numero Copie disponibili</form:label>
								<form:input required="required" type="number" path="copies" id="copies" min="0" />
								<form:errors path="copies" cssClass="validation-error" />
								<br>
						    	<form:label path="title">Prezzo del libro</form:label>
								<span>&euro;</span><form:input type="number" min="0.01" step="0.01" max="2500" value="10" required="required" path="price" id="price" />
								<form:errors path="price" cssClass="validation-error" />
								<form:label path="title">Numero Pagine del libro</form:label>
								<form:input required="required" type="number" path="pages" id="pages" min="0" />
								<form:errors path="pages" cssClass="validation-error" />
                            	<br>
								<form:label path="title">Data Pubblicazione del libro</form:label>
								<form:input required="required" type="date" path="publish" id="publish"/>
								<form:errors path="publish" cssClass="validation-error" />	
								<br> 
								
							    <form:label path="title">Descrizione del Libro</form:label> <br>
								<form:textarea required="required" placeholder="Breve descrizione del libro" path="summary" id="summary" maxlength="250" rows="4" cols="50" />
								<form:errors path="summary" cssClass="validation-error" />
								<br>
							
												
							</fieldset>
							<fieldset>
							<legend>Genere del libro</legend>
							
								<c:forEach items="${genre}" var="g"> 
							     <p hidden>${i=i+1}</p> 
										<form:checkbox id="${g}" name="${g}" path="genre" value="${g}" label="${g}" />
										 &thinsp; &nbsp;
									    <form:errors path="genre" cssClass="validation-error"/>
									<c:if test="${i%6 == 0 }"> <br></c:if>	 
								</c:forEach>
							
						</fieldset>
						
						<br>
						<fieldset>
						<legend>Applica un sconto al libro</legend>

							<form:label path="title">Sconto</form:label>
							
							<form:input type="number" min="0" step="1" max="100"
								value="0" path="discount" id="discount" /> <span>&#37;</span>
							<form:errors path="discount" cssClass="validation-error" />

						</fieldset>
							
							<br>
						<!-- 
							<fieldset>
							
						<legend>Autore  </legend>
								<form:label path="title">Nome autore</form:label> <br>
								<form:input required="required" type="text" path="title" id="title" />
								<form:errors path="title" cssClass="validation-error" />
							</fieldset>  -->
						<br>
                        <button type="submit" name="submit" class="btn with-pass-conf">Inserisci Libro </button>	
					</form:form>
				
				</div>
				
			</div>
		</div>
	</div>
</section>
<!--/ End Product Style 1  -->			<!-- Single Widget -->
		
