<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<div id="contact-us" class="contact-us section">
	<h3 class="text-center text-white pt-5">Register form</h3>
	<div class="container">
		<div class="form-main">
			<div id="login-row"
				class="row justify-content-center align-items-center">
				<div id="login-column" class="col-md-6">
					<div id="login-box" class="col-md-12">
						<c:url value="/register" var="action"/>
						<form:form id="login-form" action="${action}" modelAttribute="newUser" class="form" method="post">
							<div class="title">
								<h3>Registrati</h3>
							</div>
							<div class="form-group">
								<form:label path="personalData.name">Nome:</form:label> <span>*</span> <br> 
								<form:input type="text" path="personalData.name" id="name"/>
							</div>
							<div class="form-group">
								<form:label path="personalData.surname">Cognome:</form:label> <span>*</span> <br> 
								<form:input type="text" path="personalData.surname" id="surname"/>
							</div>
							<div class="form-group">
								<form:label path="personalData.birthdate">Data di nascita:</form:label> <span>*</span> <br> 
								<form:input type="date" path="personalData.birthdate" id="birthdate"/>
							</div>
							<div class="form-group">
								<form:label path="username">Username:</form:label> <span>*</span> <br>
								<form:input type="text" path="username" id="username"/>
							</div>
							<div class="form-group">
								<form:label path="email">Email:</form:label> <span>*</span> <br> 
								<form:input type="email" path="email" id="email"/>
							</div>
							<div class="form-group">
								<form:label path="password">Password:</form:label> <span>*</span> <br>
								<form:password path="password" id="password"/>
							</div>
							<div class="form-group">
								<label for="password-confirm">Conferma Password:</label> <span>*</span>
								<br> 
								<input type="password" name="password-confirm" id="password-confirm">
							</div>
							<div class="form-group">
								<form:label path="personalData.state">Paese:</form:label>
								<form:select path="personalData.state" items="${countries}"/>
							</div>
							<div class="form-group">
								<form:label path="personalData.city">Città:</form:label> <br>
								<form:input type="text" path="personalData.city" id="city"/>
							</div>
							<div class="form-group">
								<form:label path="personalData.street">Via:</form:label> <br>
								<form:input type="text" path="personalData.street" id="street"/>
							</div>
							<div class="form-group">
								<form:label path="personalData.cap">CAP:</form:label> <br>
								<form:input type="text" path="personalData.cap" id="cap"/>
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