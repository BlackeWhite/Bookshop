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
						<li class="active"><a href="<c:url value="/about_us" />">Chi
								siamo</a></li>
					</ul>
				</div>
			</div>
		</div>
	</div>
</div>
<!-- End Breadcrumbs -->
<!-- Start Contact -->
<section id="contact-us" class="contact-us section">
	<div class="container">
		<div class="contact-head">
			<div class="row">
				<div class="col-lg-8 col-12">
					<div class="form-main">
						<div class="title">
							<h4>Get in touch</h4>
							<h3>Write us a message</h3>
						</div>
					</div>
				</div>
				<div class="col-lg-4 col-12">
					<div class="single-head">
						<div class="single-info">
							<i class="fa fa-phone"></i>
							<h4 class="title">Call us Now:</h4>
							<ul>
								<li>+123 456-789-1120</li>
								<li>+522 672-452-1120</li>
							</ul>
						</div>
						<div class="single-info">
							<i class="fa fa-envelope-open"></i>
							<h4 class="title">Email:</h4>
							<ul>
								<li><a href="mailto:info@yourwebsite.com">info@yourwebsite.com</a></li>
								<li><a href="mailto:info@yourwebsite.com">support@yourwebsite.com</a></li>
							</ul>
						</div>
						<div class="single-info">
							<i class="fa fa-location-arrow"></i>
							<h4 class="title">Our Address:</h4>
							<ul>
								<li>KA-62/1, Travel Agency, 45 Grand Central Terminal, New
									York.</li>
							</ul>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</section>
<!--/ End Contact -->

<!-- Map Section -->
<div class="map-section">
	<div id="myMap"></div>
</div>
<!--/ End Map Section -->
