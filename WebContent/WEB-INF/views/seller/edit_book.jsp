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
								<li class="active"><a
									href="<c:url value="/seller/edit_book/${bookToUpdate.id}"/>">Modifica libro</a></li>
						</a></li>
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
						<li><a href="${addition_book}"> Aggiungi un libro in
								vendita </a></li>
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
					<div class="col-12"></div>
				</div>
				<div class="row">
					<c:url value="/seller/save_changes/${bookToUpdate.id}" var="book_action" />
					<form:form id="register-form" action="${book_action}"
						modelAttribute="bookToUpdate" class="form" method="post"
						enctype="multipart/form-data">
						<script type="text/javascript">
							function removerows(tablebody) {
								var rows = tablebody.getElementsByTagName("tr");
								while (rows.length)
									rows[0].parentNode.removeChild(rows[0]);
							}
							function addrows(tablebody, n) {
								for (var i = 0; i < n; i++) {
									var j = i + 1;
									var row = document.createElement("tr");
									var titlecell = document
											.createElement("td");
									var bold = document.createElement("strong");
									var br = document.createElement("br");

									title = titlecell
											.appendChild(document
													.createTextNode("Autore "
															+ j + ""));
									bold.appendChild(title);
									row.appendChild(bold);

									var cell = document.createElement("td");
									var input = document.createElement("input");
									var input_surname = document
											.createElement("input");

									input.setAttribute("id", "authorsName");
									input.setAttribute("name", "authorsName");
									input.setAttribute("placeholder", "Nome");
									input.setAttribute("type", "text");
									input.setAttribute("required", "required");

									;
									//input.setAttribute("class", "inputview");

									input_surname.setAttribute("id",
											"authorsSurname");
									input_surname.setAttribute("name",
											"authorsSurname");
									input_surname.setAttribute("placeholder",
											"Cognome");
									input_surname.setAttribute("type", "text");
									;
									//input_surname.setAttribute("class", "inputview");

									cell.appendChild(input);
									cell.appendChild(input_surname);
									row.appendChild(cell);
									tablebody.appendChild(row);
								}
							}

							function change_select() {
								var numAuthorse = $("#numAuthor").data("numAuthor");
								var select = document.getElementById("numrows");
								var index = select.selectedIndex
								var n = parseInt(select.value);
								var tablebody = document
										.getElementById("maintablebody");
								removerows(tablebody);
								addrows(tablebody, n);
							}
						</script>
						<input id="numAuthor" type="hidden" data-numAuthor="${numAuthor}"/>


						<legend>Autore/i del libro</legend>
						<br>
						<label path="title"><b>Numero di autori</b></label>
						<fieldset>
							<select id="numrows" name="numrows" onchange="change_select()">
								<option value="1">1</option>
								<option value="2">2</option>
								<option value="3">3</option>
								<option value="4">4</option>
							</select>
						</fieldset>
						<table id="maintable">
							<tbody id="maintablebody">
								<tr>
								<c:forEach items = "${authorsName}" var = "name">
									<td><form:label path="title">
											<b>Autore </b>
										</form:label></td>
									<td><form:input required="required" placeholder="Nome"
											type="text" path="authorsName" id="authorsName"
											class="inputview" value = "${name}"/></td>
									<td> 
									<c:if test="${authorsSurname.get(numAuthor) != '#SURNAME_PLACEHOLDER'}">
										<form:input placeholder="Cognome" type="text"
											path="authorsSurname" id="authorsSurname" class="inputview" value = "${authorsSurname.get(numAuthor)}"/></td>
									</c:if>
									<c:if test="${authorsSurname.get(numAuthor) == '#SURNAME_PLACEHOLDER'}">
										<form:input placeholder="Cognome" type="text"
											path="authorsSurname" id="authorsSurname" class="inputview" value ="" /></td>
									</c:if>
								</tr>
								<p hidden>${numAuthor = numAuthor+1}</p>
								</c:forEach>
							</tbody>
						</table>
						<fieldset>
							<legend> Informazioni Libro </legend>
							<br>
							<form:label path="title">
								<b>Titolo Libro</b>
							</form:label>
							<form:input required="required" placeholder="Titolo del libro"
								type="text" path="title" id="title" class="inputview" />
							<form:errors path="title" cssClass="validation-error" />
							<br> <br>
							<form:label path="title">
								<b>ISBN</b>
							</form:label>
							<form:input required="required" placeholder="ISBN del libro"
								type="text" path="isbn" id="isbn" maxlength="13"
								class="inputview" />
							<form:errors path="isbn" cssClass="validation-error" />
							<br> <br>
							<form:label path="title">
								<b>Numero Copie disponibili</b>
							</form:label>
							<form:input required="required" type="number" path="copies"
								id="copies" min="0" class="inputview" />
							<form:errors path="copies" cssClass="validation-error" />
							<br> <br>
							<form:label path="title">
								<b>Prezzo del libro (senza IVA)</b>
							</form:label>
							<span>&euro;</span>
							<form:input type="number" min="0.01" step="0.01" max="2500"
								required="required" path="price" id="price" class="inputview" />
							<form:errors path="price" cssClass="validation-error" />
							<br> <br>
							<form:label path="title">
								<b>Numero Pagine del libro</b>
							</form:label>
							<form:input required="required" type="number" path="pages"
								id="pages" min="1" class="inputview" />
							<form:errors path="pages" cssClass="validation-error" />
							<br> <br>
							<form:label path="title">
								<b>Data Pubblicazione del libro</b>
							</form:label>
							<form:input required="required" type="date" path="publish"
								id="publish" class="inputview" />
							<form:errors path="publish" cssClass="validation-error" />
							<br> <br>
							<!-- 
							<style>
								img {
								  border: 1px solid #ddd; /* Gray border */
								  border-radius: 4px;  /* Rounded border */
								  padding: 5px; /* Some padding */
								  width: 150px; /* Set a small width */
								}
								
								/* Add a hover effect (blue shadow) */
								img:hover {
								  box-shadow: 0 0 2px 1px rgba(0, 140, 186, 0.5);
							}
							</style>
							<form:label path="cover">
								<b>Vecchia copertina</b>
							</form:label>
							<a>
							  <img src="<c:url value="/resources/img/cover_book/${bookToUpdate.cover}"/>"  alt="Cover-preview">
							</a>
							<br> <br>
							 -->
							<form:label path="cover">
								<b>Cambia la copertina del libro</b>
							</form:label>
							</td>
							<form:input type="file" name="cover" path="cover"
								class="inputview"/>
							<form:errors path="cover" cssClass="validation-error" />
							<!-- TO-DO manca upload quando si modifca un libro  -->
							<br> <br>
							<form:label path="title">
								<b>Descrizione del Libro</b>
							</form:label>
							<br>
							<form:textarea required="required"
								placeholder="Breve descrizione del libro" path="summary"
								id="summary" maxlength="250" rows="4" cols="50" />
							<form:errors path="summary" cssClass="validation-error" />
							<br>
						</fieldset>
						<br>
						<fieldset>
							<legend>Genere del libro</legend>
							<br>
							<c:forEach items="${genre}" var="g">
								<p hidden>${i=i+1}</p>
								<form:checkbox id="${g}" name="${g}" path="genre" value="${g}"
									label="${g}" />
										 &thinsp; &nbsp;
									    <form:errors path="genre" cssClass="validation-error" />
								<c:if test="${i%8 == 0 }">
									<br>
								</c:if>
							</c:forEach>
						</fieldset>
						<br>
						<fieldset>
							<legend>Sconto sul libro</legend>
							<br>
							<form:label path="title">
								<b>Sconto</b>
							</form:label>

							<form:input type="number" min="0" step="1" max="100"
								path="discount" id="discount" />
							<span>&#37;</span>
							<form:errors path="discount" cssClass="validation-error" />
						</fieldset>
						<br>
						<br>
						<button type="submit" name="submit" class="btn with-pass-conf">Modifca
							Libro</button>
							<button class="btn with-pass-conf"><a href="<c:url value="/seller/"/>" >Annulla</a></button>
					</form:form>

				</div>

			</div>
		</div>
	</div>
</section>
<!--/ End Product Style 1  -->
<!-- Single Widget -->

