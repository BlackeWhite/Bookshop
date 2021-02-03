<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!-- Breadcrumbs -->
<div class="breadcrumbs">
	<div class="container">
		<div class="row">
			<div class="col-12">
				<div class="bread-inner">
					<ul class="bread-list">
						<li><a href="<c:url value="/" />">Home<i
								class="ti-arrow-right"></i></a></li>
						<li class="active"><a href="<c:url value="/sellers_list" />">Lista
								venditori</a></li>
					</ul>
				</div>
			</div>
		</div>
	</div>
</div>
<!-- End Breadcrumbs -->

<table class="table">
	<thead>
		<tr>
			<th scope="col">Id</th>
			<th scope="col">Nome</th>
			<th scope="col">Cognome</th>
			<th scope="col">Username</th>
			<th scope="col">Email</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${sellers}" var="seller">
			<tr>
				<th scope="row">${seller.userID}</th>
				<td>${seller.personalData.name}</td>
				<td>${seller.personalData.surname}</td>
				<td>${seller.username}</td>
				<td>${seller.email}</td>
			</tr>
		</c:forEach>
	</tbody>
</table>
