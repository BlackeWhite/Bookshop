<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<div id="contact-us" class="contact-us section">
	<div class="container">
		<div class="form-main">
			<div id="account-row"
				class="row">
				<div id="account-column" class="col-md-7">
					<div id="account-box" class="col-md-12">
						<c:url value="/account_data" var="action"/>
						<form:form id="account-form" action="${action}" modelAttribute="currentUser" class="form" method="post">
							<div class="title">
								<h3>Modifica i tuoi dati</h3>
							</div>
							<div class="form-group">
								<form:label path="personalData.name">Nome:</form:label><br> 
								<form:input required="required" type="text" path="personalData.name" id="name"/>
							</div>
							<div class="form-group">
								<form:label path="personalData.surname">Cognome:</form:label><br> 
								<form:input required="required" type="text" path="personalData.surname" id="surname"/>
							</div>
							<div class="form-group">
								<form:label path="personalData.birthdate">Data di nascita:</form:label><br> 
								<form:input required="required" type="date" path="personalData.birthdate" id="birthdate"/>
							</div>
							<div class="form-group">
								<form:label path="email">Email:</form:label><br> 
								<form:input required="required" type="email" path="email" id="email"/>
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
							</div>
							<div class="form-group">
								<form:label path="personalData.street">Via:</form:label> <br>
								<form:input required="required" type="text" path="personalData.street" id="street"/>
							</div>
							<div class="form-group">
								<form:label path="personalData.cap">CAP:</form:label> <br>
								<form:input required="required" type="text" path="personalData.cap" id="cap"/>
							</div>
							<div class="form-group button">
								<button type="submit" name="submit" class="btn">Salva</button>
							</div>
						</form:form>
					</div>
				</div>
				<div id="payment-column" class="col">
					<div id="payment-box" class="col-md-12">
						<c:url value="/add_payment_card" var="action2"/>
						<form:form id="payment-form" action="${action2}" modelAttribute="newCard" class="form" method="post">
							<div class="title">
								<h3>Gestisci carte di pagamento</h3>
							</div>
							<div class="form-group">
								<label for="temp">Tipo:</label>
								<input type="hidden" id="temp"></input>
								<br>
								<form:select required="required" path="type" items="${cardTypes}"/>
								<br><br>
							</div>
							<div class="form-group">
								<form:label path="number">Numero:</form:label><br> 
								<form:input required="required" maxlength="16" type="text" path="number" id="number"/>
							</div>
							<div class="form-group">
								<form:label path="expirationDate">Scadenza:</form:label><br> 
								<form:input required="required" type="date" path="expirationDate" id="birthdate"/>
							</div>
							<div class="form-group button">
								<button type="submit" name="submit" class="btn">Aggiungi</button>
							</div>
						</form:form>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>