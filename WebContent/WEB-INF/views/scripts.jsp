<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:url value="/resources/js/jquery.min.js" var="jquerymin" />
<script type="text/javascript" src="${jquerymin}"></script>
<c:url value="/resources/js/jquery-migrate-3.0.0.js" var="jquerymigrt" />
<script type="text/javascript" src="${jquerymigrt}"></script>
<c:url value="/resources/js/jquery-ui.min.js" var="jqueryui" />
<script type="text/javascript" src="${jqueryui}"></script>
<c:url value="/resources/js/popper.min.js" var="popper" />
<script type="text/javascript" src="${popper}"></script>
<c:url value="/resources/js/bootstrap.min.js" var="bootstrap" />
<script type="text/javascript" src="${bootstrap}"></script>
<c:url value="/resources/js/colors.js" var="colorsjs" />
<script type="text/javascript" src="${colorsjs}"></script>
<c:url value="/resources/js/slicknav.min.js" var="slicknavminjs" />
<script type="text/javascript" src="${slicknavminjs}"></script>
<c:url value="/resources/js/owl-carousel.js" var="owlcarouseljs" />
<script type="text/javascript" src="${owlcarouseljs}"></script>
<c:url value="/resources/js/magnific-popup.js" var="magnificpopupjs" />
<script type="text/javascript" src="${magnificpopupjs}"></script>
<c:url value="/resources/js/facnybox.min.js" var="facnybox" />
<script type="text/javascript" src="${facnybox}"></script>
<c:url value="/resources/js/waypoints.min.js" var="waypointsminjs" />
<script type="text/javascript" src="${waypointsminjs}"></script>
<c:url value="/resources/js/finalcountdown.min.js" var="countdown" />
<script type="text/javascript" src="${countdown}"></script>
<c:url value="/resources/js/nicesellect.js" var="nicesellect" />
<script type="text/javascript" src="${nicesellect}"></script>
<c:url value="/resources/js/ytplayer.min.js" var="ytplayer" />
<script type="text/javascript" src="${ytplayer}"></script>
<c:url value="/resources/js/flex-slider.js" var="flexslider" />
<script type="text/javascript" src="${flexslider}"></script>
<c:url value="/resources/js/scrollup.js" var="scrollup" />
<script type="text/javascript" src="${scrollup}"></script>
<c:url value="/resources/js/onepage-nav.min.js" var="onepage" />
<script type="text/javascript" src="${onepage}"></script>
<c:url value="/resources/js/easing.js" var="easing" />
<script type="text/javascript" src="${easing}"></script>
<c:url value="/resources/js/active.js" var="active" />
<script type="text/javascript" src="${active}"></script>

<!-- Url global variables -->
<c:url value="/advanced_search" var="adv_search" />
<c:url value="/" var = "home" />
<c:url value="/cart" var="cart" />
<c:url value="/add_to_cart" var="add_cart" />
<c:url value="/resources" var="resources" />
<c:url value="/show_book"  var="show_book"/>
<c:url value="/delete_card"  var="delete_card"/>
<c:url value="/copies_check"  var="copies_check"/>
<c:url value="/coupon_validation"  var="coupon_validation"/>
<c:url value="/checkout"  var="checkout"/>
<c:url value="/admin/delete_user"  var="delete_user"/>
<c:url value="/admin/delete_genre"  var="delete_genre"/>
<script type="text/javascript">
var adv_search_url = "${adv_search}";
var home_url = "${home}";
var cart_url = "${cart}";
var add_cart_url = "${add_cart}";
var resources_url = "${resources}";
var show_book_url = "${show_book}";
var delete_card_url = "${delete_card}";
var copies_check_url = "${copies_check}";
var coupon_validation_url = "${coupon_validation}";
var checkout_url = "${checkout}";
var delete_user_url = "${delete_user}";
var delete_genre_url = "${delete_genre}";
</script>

<c:url value="/resources/js/custom.js" var="custom" />
<script type="text/javascript" src="${custom}"></script>

<c:url value="/resources/js/cart.js" var="cart" />
<script type="text/javascript" src="${cart}"></script>

<c:url value="/resources/js/checkout.js" var="checkout" />
<script type="text/javascript" src="${checkout}"></script>
