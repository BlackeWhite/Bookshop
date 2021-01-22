<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div id="contact-us" class="contact-us section">
	<h3 class="text-center text-white pt-5">Login form</h3>
	<div class="container">
		<div class="form-main">
			<div id="login-row"
				class="row justify-content-center align-items-center">
				<div id="login-column" class="col-md-6">
					<div id="login-box" class="col-md-12">
						<form id="login-form" action="login" class="form" method="post">
							<div class="title">
								<h3>Login</h3>
							</div>
							<div class="form-group">
								<label for="username">Username:</label> <br> 
								<input type="text" name="username" id="username">
							</div>
							<div class="form-group">
								<label for="password">Password:</label> <br> 
								<input type="password" name="password" id="password">
							</div>
							<div class="form-group">
								<label for="remember-me">Ricordami<input
										id="remember-me" name="remember-me" class="form-check-input" style="height:16px" type="checkbox">
								</label>
							</div>
							<div class="form-group button">
								<button type="submit" name="submit" class="btn">Accedi</button>
							</div>
							<div id="register-link" class="text-right">
								<a href="<c:url value="/register"/>" class="text-info">Registrati</a>
							</div>
						</form>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>