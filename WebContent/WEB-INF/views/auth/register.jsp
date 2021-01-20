<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div id="contact-us" class="contact-us section">
	<h3 class="text-center text-white pt-5">Register form</h3>
	<div class="container">
		<div class="form-main">
			<div id="login-row"
				class="row justify-content-center align-items-center">
				<div id="login-column" class="col-md-6">
					<div id="login-box" class="col-md-12">
						<form id="login-form" class="form" method="post">
							<div class="title">
								<h3>Registrati</h3>
							</div>
							<div class="form-group">
								<label for="name">Nome:</label> <span>*</span> <br> 
								<input type="text" name="name" id="name">
							</div>
							<div class="form-group">
								<label for="surname">Cognome:</label> <span>*</span> <br> 
								<input type="text" name="surname" id="surname">
							</div>
							<div class="form-group">
								<label for="username">Username:</label> <span>*</span> <br>
								<input type="text" name="username" id="username">
							</div>
							<div class="form-group">
								<label for="email">Email:</label> <span>*</span> <br> 
								<input type="email" name="email" id="email">
							</div>
							<div class="form-group">
								<label for="password">Password:</label> <span>*</span> <br>
								<input type="password" name="password" id="password">
							</div>
							<div class="form-group">
								<label for="password-confirm">Conferma Password:</label> <span>*</span>
								<br> 
								<input type="password" name="password-confirm" id="password-confirm">
							</div>
							<div class="form-group">
								<label for="state">Paese:</label>
								<select name="state" id="state">
									<option value="it">Italia</option>
									<option value="sv">Svizzera</option>
									<option value="de">Germania</option>
									<option value="fr">Francia</option>
								</select>
							</div>
							<div class="form-group">
								<label for="city">Città:</label> <br>
								<input type="text" name="city" id="city">
							</div>
							<div class="form-group">
								<label for="street">Via:</label> <br>
								<input type="text" name="street" id="street">
							</div>
							<div class="form-group">
								<label for="cap">CAP:</label> <br>
								<input type="text" name="cap" id="cap">
							</div>
							<div class="form-group button">
								<button type="submit" name="submit" class="btn">Registrati</button>
							</div>
							<div id="login-link" class="text-right">
								<a href="#" class="text-info">Accedi</a>
							</div>
						</form>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>