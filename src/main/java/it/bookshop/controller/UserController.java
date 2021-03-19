package it.bookshop.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.text.NumberFormat;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.security.core.Authentication;

import it.bookshop.model.entity.User;
import it.bookshop.model.entity.Book;
import it.bookshop.model.entity.Coupon;
import it.bookshop.model.entity.Genre;
import it.bookshop.model.entity.Order;
import it.bookshop.model.entity.PaymentCard;
import it.bookshop.model.entity.ShoppingCart;

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

	//Pagina del carrello
	@GetMapping(value = "/cart")
	public String cart(Locale locale, Model model, Authentication authentication) {
		System.out.println("User Controller Page Requested,  locale = " + locale);

		String principal_name = authentication.getName();
		User user = userService.findUserByUsername(principal_name);
		List<ShoppingCart> user_cart = new ArrayList<ShoppingCart>(user.getShoppingCart());
		model.addAttribute("user", user);
		model.addAttribute("user_cart", user_cart);
		model.addAttribute("cartTotalPrice", user.getFormattedCartSubtotalPrice());
		model.addAttribute("cartTotalItems", user.getCartTotalItems());
		model.addAttribute("appName", appName);
		List<Genre> allGenres = this.bookService.getAllGenres();
		model.addAttribute("allGenres", allGenres);

		return "cart";
	}

	//Gestione della richiesta AJAX per l'aggiunta al carrello (mini carrello a comparsa)
	@PostMapping(value = "/add_to_cart")
	@ResponseBody
	public addCartResponse addtoCart(@RequestBody CartRequestBody reqBody, Authentication authentication)
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
		// Per aggiornare lo stato dell'utente
		user = userService.findUserByUsername(principal_name);

		return new addCartResponse(operation, reqBody.getBookID(), elem.getBook().getTitle(), elem.getBook().getCover(),
				elem.getCopies(), elem.getFormattedElementTotalPrice(), user.getFormattedCartSubtotalPrice(),
				user.getCartTotalItems());
	}

	// controllo disponibilit√† copie per libri nel carello
	// questo metodo Ë lanciato prima di raggiungere la pagina della conferma acquisto
	// e prima dell'acquisto effettivo
	// NB: non si specifica per quale/i libro/i si ha insufficienza di copie
	// Il libro specifico Ë indicato tramite JSP
	private boolean copiesAndRemovedCheck(User buyer) {
		// check disponibilit√† copie
		List<ShoppingCart> cart = shopCartService.findUserShoppingCart(buyer);
		for (ShoppingCart element : cart) {
			if (element.getCopies() > element.getBook().getCopies() || element.getBook().getRemoved() == 1) {
				//Copie non sufficienti o libro rimosso
				return false;
			}
		}
		return true;
	}

	//Gestione dell'incremento,decremento delle copie o completa rimozione di un libro dalla pagina del carrello
	@PostMapping(value = "/cart")
	@ResponseBody
	public httpResponseBody cartUpdate(@RequestBody CartRequestBody reqBody, Locale locale, Model model,
			Authentication authentication) throws MaxCopiesException, MinCopiesException, NoAvailableCopies {

		String principal_name = authentication.getName();
		User user = userService.findUserByUsername(principal_name);
		
		ShoppingCart cartElement = shopCartService.findById(user.getUserID(), reqBody.getBookID());
		int cartElementCopies = cartElement.getCopies();
		if (reqBody.getArg2().equals("delete")) { //Rimozione totale del libro dal carrello
			shopCartService.removeBook(cartElement);
			// Aggiorna lo stato dell'utente
			user = userService.findUserByUsername(principal_name);
			return new httpResponseBody(user.getFormattedCartSubtotalPrice(), user.getFormattedCheckoutTotalPrice(),
					String.valueOf(user.getCartTotalItems()), user.getFormattedSavedMoney());
		} else {
			if (reqBody.getArg2().equals("minus")) { //Diminuzione delle copie

				if (cartElementCopies > 1) {
					cartElement.setCopies(cartElement.getCopies() - 1);
					shopCartService.update(cartElement);
				} else {
					throw new MinCopiesException();
				}
			} else { //Aggiunta di pi˘ copie

				if (cartElementCopies < bookService.findById(reqBody.getBookID()).getCopies()) {
					cartElement.setCopies(cartElementCopies + 1);
					shopCartService.update(cartElement);
				} else {
					throw new MaxCopiesException();
				}
			}
			// Aggiorna lo stato dell'utente
			user = userService.findUserByUsername(principal_name);
			return new httpResponseBody(cartElement.getFormattedElementTotalPrice(),
					user.getFormattedCartSubtotalPrice(), user.getFormattedCheckoutTotalPrice(),
					user.getFormattedSavedMoney());
		}
	}

	//Pagina del checkout, viene effettuato un controllo sulla disponibilit‡ dei libri
	//Prima di restituire la vista, altrimenti si reindirizza al carrello
	//Stesso vale se l'utente non ha nulla nel carrello
	@GetMapping(value = "/checkout")
	public String checkout(Locale locale, Model model, Authentication authentication) {

		// @RequestBody checkoutRequestBody checkoutReq,
		String principal_name = authentication.getName();
		User buyer = userService.findUserByUsername(principal_name);

		if (buyer.getCartTotalItems() == 0 || !copiesAndRemovedCheck(buyer)) {
			return "redirect:/cart";
		}
		
		//IVA in base al paese dell'utente
		//Qui viene caricato solo per mostrarlo all'utente nell'HTML e calcolare nel JSP
		//quanto paga SOLO di IVA, perchÔøΩ ÔøΩ giÔøΩ stato considerato nel prezzo dei libri
		double vat = Book.vats.get(buyer.getPersonalData().getState());
		model.addAttribute("vat", vat*100);
		
		model.addAttribute("user", buyer);
		model.addAttribute("vatAmount", buyer.getFormattedVatAmount(vat)); 
		model.addAttribute("total", buyer.getFormattedCartSubtotalPrice());
		List<Genre> allGenres = this.bookService.getAllGenres();
		model.addAttribute("allGenres", allGenres);
		
		//Rimozione delle carte di pagamento scadute
		Set<PaymentCard> temp = buyer.getPaymentCards();
		List<PaymentCard> nonExpiredCards = new ArrayList<>();
		Date today = new Date(System.currentTimeMillis());
		for(PaymentCard c : temp) {
			if(c.getExpirationDate().after(today)) nonExpiredCards.add(c);
		}
		model.addAttribute("paymentCards", nonExpiredCards);
		
		return "checkout";

	}

	//Validazione dei coupon
	//Controlla se il coupon Ë gi‡ stato utilizzato dall'utente
	//O se Ë scaduto
	private String couponCheck(Coupon coupon, User buyer) {
		if (coupon != null) {
			// check validit√† coupon
			if (buyer.checkUsage(coupon)) {
				return "used";
			} else if (coupon.getExpireDate().before(new Date(System.currentTimeMillis()))) {
				return "expired";
			} else {
				return "available";
			}
		} else {
			return "unavailable";
		}
	}

	// price formatter
	private String currencyFormatter(double value) {
		NumberFormat formatter = NumberFormat.getCurrencyInstance();
		return formatter.format(value);
	}

	//Gestione della richiesta AJAX all'applicazione di un coupon
	@PostMapping(value = "/coupon_validation")
	@ResponseBody
	public httpResponseBody couponValidation(@RequestBody checkoutRequestBody checkoutReq, Locale locale, Model model,
			Authentication authentication) {

		String principal_name = authentication.getName();
		User buyer = userService.findUserByUsername(principal_name);
		double vat = Book.vats.get(buyer.getPersonalData().getState());
		Coupon coupon = couponService.findByCode(checkoutReq.getArg3());
		String result = couponCheck(coupon, buyer);
		if (!result.equals("available")) {
			return new httpResponseBody(result, "", "", "");
		} else {
			double couponSaving = buyer.getCartTotalPrice() * (double) (coupon.getDiscount() / 100.00f);
			double shipmentCost = 5;
			double newTotal = buyer.getCartTotalPrice() - couponSaving;
			return new httpResponseBody(result, currencyFormatter(couponSaving),
					currencyFormatter(newTotal + shipmentCost), currencyFormatter(newTotal * vat));
		}
	}

	//Gestione della richiesta AJAX per la conferma dell'acquisto
	@PostMapping(value = "/checkout")
	@ResponseBody
	public httpResponseBody checkoutFill(@RequestBody checkoutRequestBody checkoutReq, Locale locale, Model model,
			Authentication authentication) throws NoAvailableCopies {

		String principal_name = authentication.getName();
		User buyer = userService.findUserByUsername(principal_name);

		//Viene effettuato il terzo e ultimo controllo della disponibilit‡ dei libri
		if (!copiesAndRemovedCheck(buyer)) {
			throw new NoAvailableCopies();
		}

		Coupon coupon; // = couponService.findByCode(checkoutReq.getArg3());
		String shipmentAddress = checkoutReq.getArg1();
		String payment = checkoutReq.getArg2();

		//Validazione del coupon
		coupon = couponService.findByCode(checkoutReq.getArg3());
		if (couponCheck(coupon, buyer) == "available") {
			buyer.addUsedCoupon(coupon);
			long newUsageCount = coupon.getUsageCounter() + 1;
			coupon.setUsageCounter(newUsageCount);// update usage
			userService.update(buyer);
			couponService.update(coupon);
		}

		// get order date
		LocalDateTime now = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
		String date = formatter.format(now);
		Long buyerID = Long.valueOf(buyer.getUserID());
		orderService.createFromShoppingCart(buyerID, shipmentAddress, payment, coupon);
		shopCartService.emptyUserCart(buyer);

		return new httpResponseBody(shipmentAddress, payment, date, "");
	}

	//Pagina della cronologia degli acquisti
	@GetMapping(value = "/purchase_history")
	public String purchaseHistory(@RequestParam(required = false) Integer page, Locale locale, Model model,
			Authentication authentication) {

		String principal_name = authentication.getName();
		User user = userService.findUserByUsername(principal_name);

		// PAGINAZIONE ACQUISTI
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

	@SuppressWarnings("serial")
	public class MinCopiesException extends Exception {

	}

	@SuppressWarnings("serial")
	public class MaxCopiesException extends Exception {

	}

	@SuppressWarnings("serial")
	public class NoAvailableCopies extends Exception {

	}

	//Classe per la richiesta ajax per aumentare, diminuire o eliminare libri dalla pagina del carrello
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
		// usato nell'analisi dei libri (sezione venditore)
		public CartRequestBody(long bookID) {
			super();
			this.bookID = bookID;
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

	//Classe generica utilizzata per diversi response di richieste Ajax
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

	//Classe per la richiesta Ajax del Checkout
	public static class checkoutRequestBody {

		private String arg1;
		private String arg2;
		private String arg3;

		public checkoutRequestBody() {

		}

		public String getArg1() {
			return arg1;
		}

		public void setArg1(String arg1) {
			this.arg1 = arg1;
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

	//Response body della richiesta Ajax per aggiornare il mini carrello
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
