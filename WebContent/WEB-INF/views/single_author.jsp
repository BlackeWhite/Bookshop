<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div class="modal-body">
	<div class="row no-gutters">
		<div class="col-lg-6 col-md-12 col-sm-12 col-xs-12">
			<img src="<c:url value="/resources/img/${author.photo}"/>" alt="#"
				class="center_image">
		</div>
		<div class="col-lg-6 col-md-12 col-sm-12 col-xs-12">
			<div class="quickview-content">
				<h2>${author.name} ${author.surname}</h2>
				<div class="quickview-ratting-review">
					<div class="quickview-ratting-wrap">
						<div class="quickview-ratting">
							<i> Nato il ${author.birthdate}, nazionalità ${author.nationality} </i>
						</div>

					</div>
					<div class="quickview-stock"></div>
				</div>
				<div class="quickview-peragraph">
					<p>${author.biography}</p>
				</div>
				<div class="size">
					<div class="row">
						<div class="col-lg-6 col-12"></div>
						<div class="col-lg-6 col-12"></div>
					</div>
				</div>

				<div class="default-social">
					<h4 class="share-now">Share:</h4>
					<ul>
						<li><a class="facebook" href="#"><i
								class="fa fa-facebook"></i></a></li>
						<li><a class="twitter" href="#"><i class="fa fa-twitter"></i></a></li>
						<li><a class="youtube" href="#"><i
								class="fa fa-pinterest-p"></i></a></li>
						<li><a class="dribbble" href="#"><i
								class="fa fa-google-plus"></i></a></li>
					</ul>
				</div>
			</div>
		</div>
	</div>
</div>
