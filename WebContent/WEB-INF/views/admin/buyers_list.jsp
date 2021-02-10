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
						<li class="active"><a href="<c:url value="/admin/buyers_list" />">Lista
								acquirenti</a></li>
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
		<div class="users-list-bar">
			<span>Lista acquirenti</span>
			<div class="users-list-search justify-content-center align-items-center">
				<c:url value="/buyers_list" var="search_action" />
				<form action="${search_action}" method="GET">
					<input name="username" placeholder="Filtra per username....."
						type="search">
					<button type="submit" class="btnn">
						<i class="ti-search"></i>
					</button>
				</form>
			</div>
		</div>
		<table class="table">
			<thead>
				<tr>
					<th scope="col">Id</th>
					<th scope="col">Nome</th>
					<th scope="col">Cognome</th>
					<th scope="col">Username</th>
					<th scope="col">Email</th>
					<th scope="col">Elimina</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${buyers}" var="buyer">
					<tr id="user_${buyer.username}">
						<th scope="row">${buyer.userID}</th>
						<td>${buyer.personalData.name}</td>
						<td>${buyer.personalData.surname}</td>
						<td>${buyer.username}</td>
						<td>${buyer.email}</td>
						<td><button data-user="${buyer.username}" class="remove-user">
								<i class="fa fa-remove"></i>
							</button> <br></td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>
</div>