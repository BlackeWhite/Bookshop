<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<div id="contact-us" class="contact-us section">
	<div class="container">
		<div class="form-main">
			<div id="register-row"
				class="row justify-content-center align-items-center">
				<div id="register-column" class="col-md-6">
					<div id="register-box" class="col-md-12">
						<c:url value="/register" var="action"/>
						<form:form id="register-form" action="${action}" modelAttribute="newUser" class="form" method="post">
							<div class="title">
								<h3>Registrati</h3>
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
								<form:label path="personalData.street">Via:</form:label> <br>
								<form:input required="required" type="text" path="personalData.street" id="street"/>
								<form:errors path="personalData.street" cssClass="validation-error"/>
							</div>
							<div class="form-group">
								<form:label path="personalData.cap">CAP:</form:label> <br>
								<form:input required="required" type="text" path="personalData.cap" id="cap"/>
								<form:errors path="personalData.cap" cssClass="validation-error"/>
							</div>
							<div class="form-group button">
								<button type="submit" name="submit" class="btn">Registrati</button>
							</div>
							<div id="login-link" class="text-right">
								<a href="<c:url value="/login"/>" class="text-info">Accedi</a>
							</div>
						</form:form>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>