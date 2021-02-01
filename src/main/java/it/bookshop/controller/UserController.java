package it.bookshop.controller;

import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.text.DateFormat;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
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
import it.bookshop.model.entity.Order;
import it.bookshop.model.entity.Role;
import it.bookshop.model.entity.ShoppingCart;
import it.bookshop.model.entity.ShoppingCartId;

import it.bookshop.model.dao.ShoppingCartDao;
import it.bookshop.services.BookService;
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
		model.addAttribute("cartTotalPrice", user.getFormattedCartTotalPrice());
		model.addAttribute("cartTotalItems", user.getCartTotalItems());
		model.addAttribute("appName", appName);

		return "cart";
	}

	@PostMapping(value = "/add_to_cart")
	@ResponseBody
	public addCartResponse add_to_cart(@RequestBody httpRequestBody reqBody, Authentication authentication) 
			throws MaxCopiesException {

		String principal_name = authentication.getName();
		User user = userService.findUserByUsername(principal_name);
		ShoppingCart elem = shopCartService.findById(user.getUserID(), reqBody.getBookID());
		String operation = "";

		if (elem != null) {
			int copies = Integer.parseInt(reqBody.getArg2()) + elem.getCopies();
			if(copies > elem.getBook().getCopies()) throw new MaxCopiesException();
			else elem.setCopies(copies);
			elem = shopCartService.update(elem);
			operation = "update";
		} else {
			int copies = Integer.parseInt(reqBody.getArg2());
			if(copies > bookService.findById(reqBody.getBookID()).getCopies()) throw new MaxCopiesException();
			elem = shopCartService.create(user.getUserID(), reqBody.getBookID(), copies);
			operation = "add";
		}
		//To update the state
		user = userService.findUserByUsername(principal_name);
		
		return new addCartResponse(operation, reqBody.getBookID(), elem.getBook().getTitle(), elem.getBook().getCover(), 
				elem.getCopies(), elem.getFormattedElementTotalPrice(), 
				user.getFormattedCartTotalPrice(), user.getCartTotalItems());
	}

	@PostMapping(value = "/cart")
	@ResponseBody
	public httpResponseBody cart_update(@RequestBody httpRequestBody reqBody, Locale locale, Model model,
			Authentication authentication) throws MaxCopiesException, MinCopiesException {

		String principal_name = authentication.getName();
		User user = userService.findUserByUsername(principal_name);
		ShoppingCart cartElement = shopCartService.findById(user.getUserID(), reqBody.getBookID());
		if (reqBody.getArg2().equals("delete")) {
			shopCartService.removeBook(cartElement);
			//Update user state
			user = userService.findUserByUsername(principal_name);
			return new httpResponseBody("deleted", user.getFormattedCartTotalPrice(), 
					String.valueOf(user.getCartTotalItems()));
		} 
		else {
			int cartElementCopies = cartElement.getCopies();
			
			if (reqBody.getArg2().equals("minus") && cartElementCopies>1) {
				cartElement.setCopies(cartElement.getCopies() - 1);
			} 
			else if (reqBody.getArg2().equals("minus") && cartElementCopies==1) {
				throw new MinCopiesException();
			}
			else if (cartElementCopies<bookService.findById(reqBody.getBookID()).getCopies()){ 
				cartElement.setCopies(cartElementCopies + 1);
			}
			else {
				throw new MaxCopiesException();
				
			}
			shopCartService.update(cartElement);
			//Update user state
			user = userService.findUserByUsername(principal_name);
			return new httpResponseBody(cartElement.getFormattedElementTotalPrice(), user.getFormattedCartTotalPrice(),
					"");
		}
	}

	//to finish: add payment management section and input form management
	@RequestMapping(value = "/checkout", method={RequestMethod.GET, RequestMethod.POST})
	public String checkout(Locale locale, Model model, Authentication authentication) {
		String principal_name = authentication.getName();
		User user = userService.findUserByUsername(principal_name);
		model.addAttribute("user", user);
		model.addAttribute("total", user.getFormattedCartTotalPrice());
		return "checkout";
	}
	
	
	public class MinCopiesException extends Exception {
		
	}
	
	public class MaxCopiesException extends Exception {
		
	}

	public static class httpRequestBody {

		public httpRequestBody() {
		}

		private long bookID;
		private String arg2;

		public httpRequestBody(long bookID, String arg2) {
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
	}

	public static class httpResponseBody {

		private String response1;
		private String response2;
		private String response3;

		public httpResponseBody(String response1, String response2, String response3) {
			super();
			this.response1 = response1;
			this.response2 = response2;
			this.response3 = response3;
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

		public addCartResponse(String operation, long bookID, String title, String cover, int copies, String elemTotalPrice,
				String cartTotalPrice, int cartTotalItems) {
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
