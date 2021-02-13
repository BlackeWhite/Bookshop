<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
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
							href="<c:url value="/admin/manage_coupons" />">Gestisci Coupon</a></li>
					</ul>
				</div>
			</div>
		</div>
	</div>
</div>
<!-- End Breadcrumbs -->
<div
	class="container justify-content-center align-items-center manage-items">
	<div class="col-12">
		<div class="row">
			<div class="col-md-8 items-list">
				<div class="items-list-bar">
					<span>Lista coupon</span>
				</div>
				<table class="table">
					<thead>
						<tr>
							<th scope="col">Codice</th>
							<th scope="col">Sconto (%)</th>
							<th scope="col">Scadenza</th>
							<th scope="col">Utilizzi</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${coupons}" var="coupon">
							<tr>
								<th scope="row">${coupon.code}</th>
								<td>${coupon.discount}</td>
								<td>${coupon.expireDate}</td>
								<td>${coupon.usageCounter}</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
			<div class="col contact-us section" style="padding-top: 0px">
				<c:url value="/admin/manage_coupons" var="action" />
				<form:form id="account-form" action="${action}" class="form"
					modelAttribute="newCoupon" method="post">
					<h4 class="title" style="color: ${msgColor}; text-size: 13px;">${message}</h4>
					<h3 class="title">Aggiungi un coupon</h3>
					<div class="form-group">
						<form:label path="code">Codice:</form:label> <span>*</span>
						<form:input required="required" path="code" type="text" />
						<form:errors path="code" cssClass="validation-error" />
					</div>
					<div class="form-group">
						<form:label path="discount">Sconto (%):</form:label> <span>*</span>
						<form:input required="required" path="discount" min="5" max="50" type="number" />
						<form:errors path="discount" cssClass="validation-error" />
					</div>
					<div class="form-group">
						<form:label path="expireDate">Data di scadenza:</form:label> <span>*</span> <br> 
						<form:input required="required" type="date" path="expireDate" id="expireDate"/>
						<form:errors path="expireDate" cssClass="validation-error"/>
					</div>
					<div class="form-group button">
						<button type="submit" name="submit" class="btn">Aggiungi</button>
					</div>
				</form:form>
			</div>
		</div>
	</div>
</div>