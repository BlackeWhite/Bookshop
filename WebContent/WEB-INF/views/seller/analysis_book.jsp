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
							href="<c:url value="/seller/analysis_book"/>">
								Analisi delle vendite</a></li>
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
						<li class="active"><a href="<c:url value="/seller/"/>">I miei libri></li>
						<li><a href="${addition_book}"> Aggiungi un libro in
								vendita </a></li>
						<li><a href="<c:url value="/seller/removed_books_list"/>"> Ripristina un libro </a></li>
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
				<div class="row">
					<div class="col-12"><h3>Informazioni singolo libro</h3> <br></div>
				</div>
				
				<div class="row col-12">
				
					<label for="books">Seleziona un libro:</label> 
					<select name="books" id="books">
						<c:forEach items="${listbook}" var="listbook">
							<option value="${listbook.id}">${listbook.title}</option>
						</c:forEach>
					</select>
					
				</div>
				<br>
				<b id="title_book"></b> <br>
				<b id="copie_sold_book"></b><br>
				<b id="total_earn"></b><br>
				 <hr>
             <h3>Informazioni Generali sulle vendite dei libri </h3>
             <br>
            
             <b>Incasso Totale: &euro;${totearn } </b> <br>
             <b>Totale copie vendute: ${totcopies } </b> <br>
             <hr>
             
              <h3>Analisi delle vendite per data: </h3>
             <br> 
             <label for="dateda">Da: </label> 
             <input type="date" id="dateda" required />
               <label for="datea">A: </label>
             <input type="date" id="datea" required />
             
				<button type="submit" id="submit_data" class="btn with-pass-conf">Calcola</button>
  <br> <br>
				<b id="total_earn_data"></b> <br>
             <b id="copie_sold_book_data"> </b> <br>
             <hr>
             
            
			</div>
		</div>
	</div>
</section>
<!--/ End Product Style 1  -->
<!-- Single Widget -->

