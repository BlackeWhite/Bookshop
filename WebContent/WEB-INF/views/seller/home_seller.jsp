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
							href="<c:url value="/seller/"/>">Area Personale -
								Venditore</a></li>
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
						<c:url value="/seller/addition_book" var="addition_book"/>
						<li><a href="${addition_book}"> Aggiungi un libro in vendita </a></li>
						<li><a href=""> Modifica un libro in vendita </a></li>
						<li><a href=""> Metti in sconto un libro in vendita </a></li>
					</div>
					<!--/ End Single Widget -->

				</div>
			</div>
			<div class="col-lg-9 col-md-8 col-12">
				<div class="row">
					<div class="col-12"></div>
				</div>
				<div class="row">
					
				</div>

			</div>
		</div>
	</div>
</section>
<!--/ End Product Style 1  -->
<!-- Single Widget -->

