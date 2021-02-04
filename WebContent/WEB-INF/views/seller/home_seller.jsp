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
					<!-- Single Widget -->
					<div class="single-widget range">
						<h3 class="title">Menu</h3>
						<li><a href=""> Aggiungi un libro in vendita </a></li>
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
					
						
				
				</div>
				
			</div>
		</div>
	</div>
</section>
<!--/ End Product Style 1  -->
