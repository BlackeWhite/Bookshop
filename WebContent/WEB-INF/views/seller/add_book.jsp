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
						</a></li>
						<li class="active"><a
							href="<c:url value="/seller/addition_book"/>">Aggiungi libro</a></li>
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
						<li class="active"><a href="<c:url value="/seller/"/>">I miei libri</li>
						<li><a href="${addition_book}"> Aggiungi un libro in
								vendita </a></li>
						<li><a href="<c:url value="/seller/removed_books_list"/>"> Ripristina un libro </a></li>
						<li><a href="<c:url value="/seller/authors_seller"/>">
								Lista di autori</a></li>
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
					<c:url value="/seller/add_book" var="book_action" />
					<form:form id="register-form" action="${book_action}"
						modelAttribute="newBook" class="form" method="post"
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
									input.setAttribute("pattern",
									"([^\\s][A-z0-9À-ž\\.\\s]+)");
									input.setAttribute("title", "Non sono ammessi caratteri speciali come virgole e apici. Non sono ammessi spazi come caratteri iniziali.");
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
									input_surname.setAttribute("pattern",
									"([^\\s][A-z0-9À-ž\\.\\s]+)");
									input.setAttribute("title", "Non sono ammessi caratteri speciali come virgole e apici. Non sono ammessi spazi come caratteri iniziali.");

									;
									//input_surname.setAttribute("class", "inputview");

									cell.appendChild(input);
									cell.appendChild(input_surname);
									row.appendChild(cell);
									tablebody.appendChild(row);
								}
							}

							function change_select() {
								var select = document.getElementById("numrows");
								var index = select.selectedIndex
								var n = parseInt(select.value);
								var tablebody = document
										.getElementById("maintablebody");
								removerows(tablebody);
								addrows(tablebody, n);
							}
						</script>

						<h1><legend>Autore/i del libro</legend></h1>
						<br>
						<label path="title"><b>Numero di autori</b></label>
						<fieldset>
							<!-- onchange="change_select()" -->
							<select id="numrows" name="numrows" onchange="change_select()">
								<option value="1">1</option>
								<option value="2">2</option>
								<option value="3">3</option>
								<option value="4">4</option>
							</select>
						</fieldset>
						<table id="maintable">
							<tbody id="maintablebody">
									<c:set var="numAuthor" value="0" />
									<c:choose>
										<c:when test="${empty newBook.authorsName}">
										<tr>
											<td><form:label path="title">
													<b>Autore 1</b>
												</form:label></td>
											<td><form:input required="required" placeholder="Nome"
													type="text" path="authorsName" id="authorsName"
													class="inputview" pattern="([^\s][A-z0-9À-ž\.\s]+)" title = "Non sono ammessi caratteri speciali come virgole e apici. Non sono ammessi spazi come caratteri iniziali." /></td>
											<td><form:input placeholder="Cognome" type="text"
													path="authorsSurname" id="authorsSurname" class="inputview"
													pattern="([^\s][A-z0-9À-ž\.\s]+)" title = "Non sono ammessi caratteri speciali come virgole e apici. Non sono ammessi spazi come caratteri iniziali." /></td>
										</tr>
										</c:when>
										<c:otherwise>
											<c:forEach items="${newBook.authorsName}" var="name">
											<tr>
												<td><form:label path="title">
														<b>Autore </b>
													</form:label></td>
												<td><form:input required="required" placeholder="Nome"
														type="text" path="authorsName" id="authorsName"
														class="inputview" value="${name}" pattern="([^\s][A-z0-9À-ž\.\s]+)" title = "Non sono ammessi caratteri speciali come virgole e apici. Non sono ammessi spazi come caratteri iniziali."/></td>
												<td><form:input placeholder="Cognome" type="text"
														path="authorsSurname" id="authorsSurname"
														class="inputview authorSurname"
														value="${newBook.authorsSurname.get(numAuthor)}" pattern="([^\s][A-z0-9À-ž\.\s]+)" title = "Non sono ammessi caratteri speciali come virgole e apici. Non sono ammessi spazi come caratteri iniziali."/></td>
												<p hidden>${numAuthor = numAuthor+1}</p>
												</tr>
											</c:forEach>
										</c:otherwise>
									</c:choose>
							</tbody>
						</table>
						<fieldset>
							<h1><legend> Informazioni Libro </legend></h1>
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
							<form:label path="cover">
								<b>Carica la copertina del libro</b>
							</form:label>
							</td>
							<form:input required="required" type="file" name="cover"
								path="cover" class="inputview" accept="image/*"/>
							<form:errors path="cover" cssClass="validation-error" />
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
							<h1><legend>Genere del libro</legend></h1>
							<br>
							<c:set var="i" value="0" />
							<c:forEach items="${allGenres}" var="g">
								<p hidden>${i=i+1}</p>
								<form:checkbox id="${g.name}" name="${g.name}" path="genre"
									value="${g.name}" label="${g.name}" />
										 &thinsp; &nbsp;
									    <form:errors path="genre" cssClass="validation-error" />
								<c:if test="${i%8 == 0 }">
									<br>
								</c:if>
							</c:forEach>
						</fieldset>
						<br>
						<fieldset>
							<h1><legend>Sconto sul libro</legend></h1>
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
						<button type="submit" name="submit" class="btn">Inserisci
							Libro</button>
						<button class="btn">
							<a href="<c:url value="/seller/"/>">Annulla</a>
						</button>
					</form:form>
				</div>
			</div>
		</div>
	</div>
</section>


