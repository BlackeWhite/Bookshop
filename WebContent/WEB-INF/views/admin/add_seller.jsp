<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<!-- Breadcrumbs -->
<div class="breadcrumbs">
	<div class="container">
		<div class="row">
			<div class="col-12">
				<div class="bread-inner">
					<ul class="bread-list">
						<li><a href="<c:url value="/" />">Home<i class="ti-arrow-right"></i></a></li>
						<li class="active"><a href="<c:url value="/add_seller" />">Aggiungi venditori</a></li>
					</ul>
				</div>
			</div>
		</div>
	</div>
</div>
<!-- End Breadcrumbs -->

<div id="contact-us" class="contact-us section">
	<div class="container">
		<div class="form-main">
			<div id="add-seller-row"
				class="row justify-content-center align-items-center">
				<div id="add-seller-column" class="col-md-6">
					<div id="add-seller-box" class="col-md-12">
						<c:url value="/admin/add_seller" var="action"/>
						<form:form id="add-seller-form" action="${action}" modelAttribute="newSeller" class="form" method="post">
							<h4 class="title" style="color: ${msgColor}; text-size: 13px;">${message}</h4>
							<div class="title">
								<h3>Aggiungi un venditore</h3>
							</div>
							<div class="form-group">
								<form:label path="personalData.name">Nome:</form:label> <span>*</span> <br> 
								<form:input required="required" type="text" path="personalData.name" id="name"/>
								<form:errors path="personalData.name" cssClass="validation-error"/>
							</div>
							<div class="form-group">
								<form:label path="personalData.surname">Cognome:</form:label> <span>*</span> <br> 
								<form:input required="required" type="text" path="personalData.surname" id="surname"/>
								<form:errors path="personalData.surname" cssClass="validation-error"/>
							</div>
							<div class="form-group">
								<form:label path="personalData.birthdate">Data di nascita:</form:label> <span>*</span> <br> 
								<form:input required="required" type="date" path="personalData.birthdate" id="birthdate"/>
								<form:errors path="personalData.birthdate" cssClass="validation-error"/>
							</div>
							<div class="form-group">
								<form:label path="username">Username:</form:label> <span>*</span> <br>
								<form:input required="required" type="text" path="username" id="username"/>
								<form:errors path="username" cssClass="validation-error"/>
							</div>
							<div class="form-group">
								<form:label path="email">Email:</form:label> <span>*</span> <br> 
								<form:input required="required" type="email" path="email" id="email"/>
								<form:errors path="email" cssClass="validation-error"/>
							</div>
							<div class="form-group">
								<form:label path="password">Password:</form:label> <span>*</span> <br>
								<form:password required="required" path="password" id="password"/>
								<form:errors path="password" cssClass="validation-error"/>
							</div>
							<div class="form-group">
								<label for="password-confirm">Conferma Password:</label> <span>*</span>
								<br> 
								<input required type="password" name="password-confirm" id="password-confirm">
							</div>
							<div class="form-group">
								<label for="temp">Paese:</label>
								<input type="hidden" id="temp"></input>
								<br>
								<form:select required="required" path="personalData.state" items="${countries}"/>
								<br><br>
							</div>
							<div class="form-group">
								<form:label path="personalData.city">Città:</form:label> <br>
								<form:input required="required" type="text" path="personalData.city" id="city"/>
								<form:errors path="personalData.city" cssClass="validation-error"/>
							</div>
							<div class="form-group">
								<form:label path="personalData.street">Indirizzo:</form:label> <br>
								<form:input required="required" type="text" path="personalData.street" id="street"/>
								<form:errors path="personalData.street" cssClass="validation-error"/>
							</div>
							<div class="form-group">
								<form:label path="personalData.cap">CAP:</form:label> <br>
								<form:input required="required" type="text" path="personalData.cap" id="cap"/>
								<form:errors path="personalData.cap" cssClass="validation-error"/>
							</div>
							<div class="form-group button">
								<button type="submit" name="submit" class="btn with-pass-conf">AGGIUNGI</button>
							</div>
						</form:form>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>