<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div class="modal-body">
	<div class="row no-gutters">
		<div class="col-lg-6 col-md-12 col-sm-12 col-xs-12">
			<img src="<c:url value="/resources/img/${book.cover}"/>" alt="#"
				class="center_image">
		</div>
		<div class="col-lg-6 col-md-12 col-sm-12 col-xs-12">
			<div class="quickview-content">
				<h2>${book.title}</h2>
				<h4><i> Di 
						<c:forEach items="${authorsList}" var="authorsList">
							<a href=" <c:url value ="/show_author/${authorsList.id}"/>" >${authorsList.name} ${authorsList.surname}</a>
							</c:forEach>	
					</i>
				</h4>
				<div class="quickview-ratting-review">
					<div class="quickview-ratting-wrap">
						<div class="quickview-ratting">
							<c:if test="${book.copies > 0}">
								<b style = "color:#228B22">Disponibile</b>
							</c:if>
							<c:if test="${book.copies <= 0}">
								<b style = "color:#B22222" >Non Disponibile</b>
							</c:if>


						</div>

					</div>
					<div class="quickview-stock"></div>
				</div>
				<h3>€ ${book.price}</h3>
				<div class="quickview-peragraph">
					<p>${book.summary}</p>
				</div>
				<div class="size">
					<div class="row">
						<div class="col-lg-6 col-12"></div>
						<div class="col-lg-6 col-12"></div>
					</div>
				</div>
				<div class="quantity">
					<!-- Input Order -->
					<div class="input-group">
						<div class="button minus">
							<button type="button" class="btn btn-primary btn-number"
								disabled="disabled" data-type="minus" data-field="quant[1]">
								<i class="ti-minus"></i>
							</button>
						</div>
						<c:if test="${book.copies > 0}">
							
						<input type="text" name="quant[1]" class="input-number"
							data-min="1" data-max="${book.copies}" value="1">
						<div class="button plus">
							<button type="button" class="btn btn-primary btn-number"
								data-type="plus" data-field="quant[1]">
								<i class="ti-plus"></i>
							</button>
						</div>
					</div>
					<!--/ End Input Order -->
				</div>
				<div class="add-to-cart">
					<a href="#" class="btn">Add to cart</a>
				</div>
				</c:if>
				<div class="default-social">
					<h4 class="share-now">Share:</h4>
					<ul>
						<li><a class="facebook" href="#"><i
								class="fa fa-facebook"></i></a></li>
						<li><a class="twitter" href="#"><i class="fa fa-twitter"></i></a></li>
						<li><a class="youtube" href="#"><i
								class="fa fa-pinterest-p"></i></a></li>
						<li><a class="dribbble" href="#"><i
								class="fa fa-google-plus"></i></a></li>
					</ul>
				</div>
			</div>
		</div>
	</div>
</div>
