package it.bookshop.controller;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.text.ParseException;
import java.text.DateFormat;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.security.core.Authentication;

import it.bookshop.model.entity.User;
import it.bookshop.model.entity.Author;
import it.bookshop.model.entity.Book;
import it.bookshop.model.entity.BookOrder;
import it.bookshop.model.entity.BookOrderId;
import it.bookshop.model.entity.Coupon;
import it.bookshop.model.entity.Genre;
import it.bookshop.model.entity.Order;
import it.bookshop.model.entity.Role;
import it.bookshop.model.entity.ShoppingCart;
import it.bookshop.model.entity.ShoppingCartId;

import it.bookshop.model.dao.ShoppingCartDao;
import it.bookshop.services.BookService;
import it.bookshop.services.CouponService;
import it.bookshop.services.UserService;
import it.bookshop.services.OrderService;
import it.bookshop.services.ShoppingCartService;

@Controller
public class UserController {

	@Autowired
	String appName;

	@Autowired
	private UserService userService;
	@Autowired
	private ShoppingCartService shopCartService;
	@Autowired
	private BookService bookService;
	@Autowired
	private OrderService orderService;
	@Autowired
	private CouponService couponService;

	@GetMapping(value = "/cart")
	public String cart(Locale locale, Model model, Authentication authentication) {
		System.out.println("User Controller Page Requested,  locale = " + locale);

		/*
		 * DateFormat date = new SimpleDateFormat("dd-MM-yyyy"); java.util.Date date_x =
		 * null; try { date_x = date.parse("21-01-2005"); } catch (ParseException e) {
		 * // TODO Auto-generated catch block e.printStackTrace(); } Date birth = new
		 * Date(date_x.getTime()); long cap = 60135; userService.create( "red_mario65",
		 * "mariorossi65@gmail.com", "m5r10r0ss1", "Mario", "Rossi", birth,
		 * "Via Giuseppe Verdi 14", "Ancona", cap, "Italia"); shopCartService.create(
		 * Long.valueOf(1), //id user Long.valueOf(2), //id book 2); //copie
		 */

		String principal_name = authentication.getName();
		User user = userService.findUserByUsername(principal_name);
		List<ShoppingCart> user_cart = new ArrayList<ShoppingCart>(user.getShoppingCart());
		model.addAttribute("user", user);
		model.addAttribute("user_cart", user_cart);
		model.addAttribute("cartTotalPrice", user.getFormattedCartSubtotalPrice());
		model.addAttribute("cartTotalItems", user.getCartTotalItems());
		model.addAttribute("appName", appName);

		return "cart";
	}

	@PostMapping(value = "/add_to_cart")
	@ResponseBody
	public addCartResponse add_to_cart(@RequestBody CartRequestBody reqBody, Authentication authentication)
			throws MaxCopiesException {

		String principal_name = authentication.getName();
		User user = userService.findUserByUsername(principal_name);
		ShoppingCart elem = shopCartService.findById(user.getUserID(), reqBody.getBookID());
		String operation = "";

		if (elem != null) {
			int copies = Integer.parseInt(reqBody.getArg2()) + elem.getCopies();
			if (copies > elem.getBook().getCopies())
				throw new MaxCopiesException();
			else
				elem.setCopies(copies);
			elem = shopCartService.update(elem);
			operation = "update";
		} else {
			int copies = Integer.parseInt(reqBody.getArg2());
			if (copies > bookService.findById(reqBody.getBookID()).getCopies())
				throw new MaxCopiesException();
			elem = shopCartService.create(user.getUserID(), reqBody.getBookID(), copies);
			operation = "add";
		}
		// To update the state
		user = userService.findUserByUsername(principal_name);

		return new addCartResponse(operation, reqBody.getBookID(), elem.getBook().getTitle(), elem.getBook().getCover(),
				elem.getCopies(), elem.getFormattedElementTotalPrice(), user.getFormattedCartSubtotalPrice(),
				user.getCartTotalItems());
	}

	@PostMapping(value = "/cart")
	@ResponseBody
	public httpResponseBody cart_update(@RequestBody CartRequestBody reqBody, Locale locale, Model model,
			Authentication authentication) throws MaxCopiesException, MinCopiesException {

		String principal_name = authentication.getName();
		User user = userService.findUserByUsername(principal_name);
		ShoppingCart cartElement = shopCartService.findById(user.getUserID(), reqBody.getBookID());
		int cartElementCopies = cartElement.getCopies();
		if (reqBody.getArg2().equals("delete")) {
			shopCartService.removeBook(cartElement);
			// Update user state
			user = userService.findUserByUsername(principal_name);
			return new httpResponseBody(user.getFormattedCartSubtotalPrice(), user.getFormattedCheckoutTotalPrice(),
					String.valueOf(user.getCartTotalItems()), user.getFormattedSavedMoney());
		} else {
			if (reqBody.getArg2().equals("minus")) {
				
				if (cartElementCopies > 1) {
					cartElement.setCopies(cartElement.getCopies() - 1);
					shopCartService.update(cartElement);
				} else {
					throw new MinCopiesException();
				} 
			} else {

				if (cartElementCopies < bookService.findById(reqBody.getBookID()).getCopies()) {
					cartElement.setCopies(cartElementCopies + 1);
					shopCartService.update(cartElement);
				} else {
					throw new MaxCopiesException();
				}
			}
			// update user state
			user = userService.findUserByUsername(principal_name);
			return new httpResponseBody(cartElement.getFormattedElementTotalPrice(),
					user.getFormattedCartSubtotalPrice(), user.getFormattedCheckoutTotalPrice(),
					user.getFormattedSavedMoney());
		}
	}

	@GetMapping(value = "/checkout")
	public String checkout(Locale locale, Model model, Authentication authentication) {

		String principal_name = authentication.getName();
		User buyer = userService.findUserByUsername(principal_name);
		List<Genre> allGenres = this.bookService.getAllGenres();
		model.addAttribute("allGenres", allGenres);

		/*
		 * else if (reqBody.getArg2().equals("coupon")) { Coupon coupon =
		 * couponService.findByCode(reqBody.getArg3()); if (coupon!= null) { if (true)
		 * //user.checkUsage(coupon) { return new httpResponseBody("already used", "",
		 * "", ""); } else { //user.addUsedCoupon(coupon); return new
		 * httpResponseBody("", "", "", ""); } } else { return new httpResponseBody("",
		 * "", "", ""); } }
		 */

		if (buyer.getCartTotalItems() == 0) {
			return "cart";
		} else {
			model.addAttribute("user", buyer);
			model.addAttribute("total", buyer.getFormattedCartSubtotalPrice());
			return "checkout";
		}
	}

	@PostMapping(value = "/checkout")
	@ResponseBody
	public httpResponseBody checkout_fill(@RequestBody checkoutRequest checkoutReq, Locale locale, Model model,
			Authentication authentication) {

		String principal_name = authentication.getName();
		User buyer = userService.findUserByUsername(principal_name);
		String payment = checkoutReq.getPayment(); // card number
		String shipmentAddress;

		// check per valorizzazione indirizzo di spedizione
		if (checkoutReq.getShipmentAddress().equals("standard shipment address")) {
			shipmentAddress = buyer.getPersonalData().getFullAddress();
		} else {
			shipmentAddress = checkoutReq.getShipmentAddress();
		}

		// get order date
		LocalDateTime now = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
		String date = formatter.format(now);
		Long buyerID = Long.valueOf(buyer.getUserID());
		orderService.createFromShoppingCart(buyerID, shipmentAddress, payment);
		shopCartService.emptyUserCart(buyer);

		model.addAttribute("user", buyer);
		model.addAttribute("total", buyer.getFormattedCartSubtotalPrice());
		return new httpResponseBody(shipmentAddress, payment, date, "");
	}

	@GetMapping(value = "/purchase_history")
	public String purchaseHistory(@RequestParam(required = false) Integer page, Locale locale, Model model,
			Authentication authentication) {

		String principal_name = authentication.getName();
		User user = userService.findUserByUsername(principal_name);

		// PAGINAZIONE ORDINI
		List<Order> orders = orderService.findUserOrders(user);
		PagedListHolder<Order> pagedListHolder = new PagedListHolder<>(orders);
		pagedListHolder.setPageSize(10);

		if (page == null || page < 1 || page > pagedListHolder.getPageCount())
			page = 1;

		pagedListHolder.setPage(page - 1);

		model.addAttribute("orders", pagedListHolder.getPageList());
		model.addAttribute("maxPages", pagedListHolder.getPageCount());
		model.addAttribute("page", page);

		// MINI CARRELO A COMPARSA E MENU A TENDINA GENERI
		List<ShoppingCart> user_cart = new ArrayList<ShoppingCart>(user.getShoppingCart());
		List<Genre> allGenres = this.bookService.getAllGenres();
		model.addAttribute("allGenres", allGenres);
		model.addAttribute("user", user);
		model.addAttribute("user_cart", user_cart);
		model.addAttribute("cartTotalPrice", user.getFormattedCartSubtotalPrice());
		model.addAttribute("cartTotalItems", user.getCartTotalItems());
		model.addAttribute("appName", appName);

		return "purchase_history";
	}

	public class MinCopiesException extends Exception {

	}

	public class MaxCopiesException extends Exception {

	}

	public static class CartRequestBody {

		private long bookID;
		private String arg2;
		private String arg3;

		public CartRequestBody() {
		}

		public CartRequestBody(long bookID, String arg2) {
			super();
			this.bookID = bookID;
			this.arg2 = arg2;
		}

		public long getBookID() {
			return bookID;
		}

		public void setBookID(long bookID) {
			this.bookID = bookID;
		}

		public String getArg2() {
			return arg2;
		}

		public void setArg2(String arg2) {
			this.arg2 = arg2;
		}

		public String getArg3() {
			return arg3;
		}

		public void setArg3(String arg3) {
			this.arg3 = arg3;
		}

	}

	public static class httpResponseBody {

		private String response1;
		private String response2;
		private String response3;
		private String response4;

		public httpResponseBody(String response1, String response2, String response3, String response4) {
			super();
			this.response1 = response1;
			this.response2 = response2;
			this.response3 = response3;
			this.response4 = response4;
		}

		public String getResponse1() {
			return response1;
		}

		public void setResponse1(String response1) {
			this.response1 = response1;
		}

		public String getResponse2() {
			return response2;
		}

		public void setResponse2(String response2) {
			this.response2 = response2;
		}

		public String getResponse3() {
			return response3;
		}

		public void setResponse3(String response3) {
			this.response3 = response3;
		}

		public String getResponse4() {
			return response4;
		}

		public void setResponse4(String response4) {
			this.response4 = response4;
		}
	}

	public static class checkoutRequest {

		private String shipmentAddress;
		private String payment;

		public checkoutRequest() {

		}

		public checkoutRequest(String shipmentAddress, String payment) {
			super();
			this.shipmentAddress = shipmentAddress;
			this.payment = payment;
		}

		public String getShipmentAddress() {
			return shipmentAddress;
		}

		public void setShipmentAddress(String shipmentAddress) {
			this.shipmentAddress = shipmentAddress;
		}

		public String getPayment() {
			return payment;
		}

		public void setPayment(String payment) {
			this.payment = payment;
		}
	}

	public static class addCartResponse {
		private String operation;
		private long bookID;
		private String title;
		private String cover;
		private int copies;
		private String elemTotalPrice;
		private String cartTotalPrice;
		private int cartTotalItems;

		public addCartResponse(String operation, long bookID, String title, String cover, int copies,
				String elemTotalPrice, String cartTotalPrice, int cartTotalItems) {
			this.operation = operation;
			this.bookID = bookID;
			this.title = title;
			this.cover = cover;
			this.copies = copies;
			this.elemTotalPrice = elemTotalPrice;
			this.cartTotalPrice = cartTotalPrice;
			this.cartTotalItems = cartTotalItems;
		}

		public String getOperation() {
			return operation;
		}

		public void setOperation(String operation) {
			this.operation = operation;
		}

		public long getBookID() {
			return bookID;
		}

		public void setBookID(long bookID) {
			this.bookID = bookID;
		}

		public int getCopies() {
			return copies;
		}

		public void setCopies(int copies) {
			this.copies = copies;
		}

		public String getElemTotalPrice() {
			return elemTotalPrice;
		}

		public void setElemTotalPrice(String elemTotalPrice) {
			this.elemTotalPrice = elemTotalPrice;
		}

		public String getCartTotalPrice() {
			return cartTotalPrice;
		}

		public void setCartTotalPrice(String cartTotalPrice) {
			this.cartTotalPrice = cartTotalPrice;
		}

		public int getCartTotalItems() {
			return cartTotalItems;
		}

		public void setCartTotalItems(int cartTotalItems) {
			this.cartTotalItems = cartTotalItems;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public String getCover() {
			return cover;
		}

		public void setCover(String cover) {
			this.cover = cover;
		}
	}
}
