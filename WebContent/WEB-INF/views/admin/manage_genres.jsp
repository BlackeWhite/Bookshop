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
						<li class="active"><a href="<c:url value="/admin/genres_list" />">Lista
								generi</a></li>
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
		<div class="genres-list-bar">
			<span>Lista generi</span>
		</div>
		<table class="table">
			<thead>
				<tr>
					<th scope="col">Nome</th>
					<th scope="col">Elimina</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${allGenres}" var="genre">
					<tr id="genre_${genre.name}">
						<th scope="row">${genre.name}</th>
						<td><button data-user="${genre.name}" class="remove-genre">
								<i class="fa fa-remove"></i>
							</button> <br></td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>
</div>