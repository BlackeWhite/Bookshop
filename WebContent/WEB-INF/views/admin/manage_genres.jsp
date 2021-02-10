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
							href="<c:url value="/admin/manage_genres" />">Lista generi</a></li>
					</ul>
				</div>
			</div>
		</div>
	</div>
</div>
<!-- End Breadcrumbs -->
<div
	class="container justify-content-center align-items-center manage-genres">
	<div class="col-12">
		<div class="row">
			<div class="col-md-7 genres-list">
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
			<div class="col contact-us section" style="padding-top: 0px">
				<c:url value="/save_genre" var="action" />
				<form:form id="account-form" action="${action}" class="form"
					modelAttribute="newGenre" method="post">
					<h4 class="title" style="color: ${msgColor}; text-size: 13px;">${message}</h4>
					<h3 class="title">Aggiungi un genere</h3>
					<div class="form-group">
						<form:label path="name">Nome:</form:label>
						<form:input path="name" type="text" />
						<form:errors path="name" cssClass="validation-error" />
					</div>
					<div class="form-group button">
						<button type="submit" name="submit" class="btn">Aggiungi</button>
					</div>
				</form:form>
			</div>
		</div>
	</div>
</div>