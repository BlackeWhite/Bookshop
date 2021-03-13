<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>

<!-- Breadcrumbs -->
<div class="breadcrumbs">
	<div class="container">
		<div class="row">
			<div class="col-12">
				<div class="bread-inner">
						<ul class="bread-list">
						<li>
							<a href="<c:url value="/" />">Home<i class="ti-arrow-right"></i></a>
						</li>
							<security:authorize access="hasRole('SELLER')">
								<li class="active"><a href="<c:url value="/seller/"/>">Area Personale - Venditore<i class="ti-arrow-right"></i>
							</security:authorize>
						<li class="active">
							<a href="<c:url value="/account"/>">Account</a></li>
						</a></li>
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
			<div id="account-row" class="row">
				<div id="account-column" class="col-md-7">
					<div id="account-box" class="col-md-12">
						<c:url value="/account_save" var="action" />
						<form:form id="account-form" action="${action}"
							modelAttribute="currentUser" class="form" method="post">
							<h4 class="title" style="color: ${msgColor}; text-size: 13px;">${message2}</h4>
							<div class="title">
								<h3>Modifica i tuoi dati</h3>
							</div>
							<form:hidden path="username"/>
							<form:hidden path="userID"/>
							<div class="form-group">
								<form:label path="personalData.name">Nome:</form:label>
								<br>
								<form:input required="required" type="text" path="personalData.name" id="name" />
								<form:errors path="personalData.name" cssClass="validation-error"/>
							</div>
							<div class="form-group">
								<form:label path="personalData.surname">Cognome:</form:label>
								<br>
								<form:input required="required" type="text" path="personalData.surname" id="surname" />
								<form:errors path="personalData.surname" cssClass="validation-error"/>
							</div>
							<div class="form-group">
								<form:label path="personalData.birthdate">Data di nascita:</form:label>
								<br>
								<form:input required="required" type="date" path="personalData.birthdate" class="date" id="birthdate" />
								<form:errors path="personalData.birthdate" cssClass="validation-error"/>
							</div>
							<div class="form-group">
								<form:label path="email">Email:</form:label>
								<br>
								<form:input required="required" type="email" path="email" id="email" />
								<form:errors path="email" cssClass="validation-error"/>
							</div>
							<div class="form-group">
								<label for="temp">Paese:</label> <input type="hidden" id="temp"></input>
								<br>
								<form:select required="required" path="personalData.state"
									items="${countries}" />
								<br>
								<br>
							</div>
							<div class="form-group">
								<form:label path="personalData.city">Città:</form:label>
								<br>
								<form:input required="required" type="text" path="personalData.city" id="city" />
								<form:errors path="personalData.city" cssClass="validation-error"/>
							</div>
							<div class="form-group">
								<form:label path="personalData.street">Indirizzo:</form:label>
								<br>
								<form:input required="required" type="text" path="personalData.street" id="street" />
								<form:errors path="personalData.street" cssClass="validation-error"/>
							</div>
							<div class="form-group">
								<form:label path="personalData.cap">CAP:</form:label>
								<br>
								<form:input required="required" type="text" path="personalData.cap" id="cap" />
								<form:errors path="personalData.cap" cssClass="validation-error"/>
							</div>
							<div class="form-group button">
								<button type="submit" name="submit" class="btn">Salva</button>
							</div>
						</form:form>
						<c:url value="/delete_account" var="delacc" />
						<form action="${delacc}" method="post" onsubmit="return confirm('Sei sicuro di voler eliminare il tuo account?');">
							<button type="submit" class="btn delacc">ELIMINA  ACCOUNT</button>
						</form>
					</div>
				</div>
					<div id="payment-column" class="col">
						<div id="payment-box" class="col-md-12">
							<c:url value="/change_password" var="action3" />
							<form id="password-form" action="${action3}" class="form" method="post">
							<h4 class="title" style="color: ${msgColor}; text-size: 13px;">${message}</h4>
								<div class="title">
									<h3>Modifica password</h3>
								</div>
								<div class="form-group">
									<label for="old_password">Vecchia Password:</label> <span>*</span> <br>
									<input type="password" name="old_password" id="old_password"/>
								</div>
								<div class="form-group">
									<label for="password">Nuova Password:</label> <span>*</span> <br>
									<input type="password" name="password" id="password"/>
									<span class="validation-error">${message3}</span>
								</div>
								<div class="form-group">
									<label for="password-confirm">Conferma Password:</label> <span>*</span>
									<br> 
									<input type="password" name="password-confirm" id="password-confirm">
								</div>
								<div class="form-group button">
									<button type="submit" name="submit" class="btn with-pass-conf">Cambia</button>
								</div>
							</form>
							<hr>
							<security:authorize access="hasRole('USER')">
							<c:url value="/add_payment_card" var="action2" />
							<form:form id="payment-form" action="${action2}"
								modelAttribute="newCard" class="form" method="post">
								<div class="title">
									<h3>Gestisci carte di pagamento</h3>
								</div>
								<div class="form-group">
									<label for="temp">Tipo:</label> <input type="hidden" id="temp"></input>
									<br>
									<form:select required="required" path="type"
										items="${cardTypes}" />
									<br>
									<br>
								</div>
								<div class="form-group">
									<form:label path="number">Numero:</form:label>
									<br>
									<form:input required="required" minlength="16" maxlength="16" type="text"
										path="number" id="number" />
									<form:errors path="number" cssClass="validation-error"/>
								</div>
								<div class="form-group">
									<form:label path="expirationDate">Scadenza:</form:label>
									<br>
									<form:input required="required" type="date" class="date"
										path="expirationDate" id="expdate" />
									<form:errors path="expirationDate" cssClass="validation-error"/>
								</div>
								<div class="form-group button">
									<button type="submit" name="submit" class="btn">Aggiungi</button>
								</div>
							</form:form>
							<hr>
							<h4 class="title" style="margin-bottom: 15px;">Le tue carte</h4>
							<c:forEach items="${userCards}" var="card">
							<div id="card_${card.id}">
								<div class="card-container">
									<span>Carta </span><p>${card.type}</p><br>
									<span>Che termina con: </span><p>${card.hiddenNumber}</p>
									<button data-card="${card.id}" class="remove-card"><i class="fa fa-remove"></i></button><br>
									<span>Scadenza: </span><p >${card.shortExpirationDate}</p>
									<c:if test="${today gt card.expirationDate}">
									<span class="expired">Carta scaduta</span>
									</c:if>
									<br>
								</div>
								<hr>
							</div>
							</c:forEach>
						</security:authorize>
						</div>
					</div>
			</div>
		</div>
	</div>
</div>