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

	
	@RequestMapping(value = "/cart", method = RequestMethod.GET)
	public String cart(Locale locale, Model model, Authentication authentication) {
		System.out.println("Cart Controller Page Requested,  locale = " + locale);
		
		/*DateFormat date = new SimpleDateFormat("dd-MM-yyyy");
		java.util.Date date_x = null;
		try {
			date_x = date.parse("21-01-2005");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Date birth = new Date(date_x.getTime());
		long cap = 60135;
		userService.create(
				"red_mario65", 
				"mariorossi65@gmail.com", 
				"m5r10r0ss1", 
				"Mario", 
				"Rossi",
				birth, 
				"Via Giuseppe Verdi 14", 
				"Ancona", 
				cap, 
				"Italia");
		shopCartService.create(
				Long.valueOf(1), //id user
				Long.valueOf(2), //id book
				2); //copie */
		
		String principal_name = authentication.getName();
		User user = userService.findUserByUsername(principal_name);
		List<ShoppingCart> user_cart = new ArrayList<ShoppingCart>(user.getShoppingCart());
		model.addAttribute("user", user);
		model.addAttribute("user_cart", user_cart);
		model.addAttribute("appName", appName);

		return "cart";
	}
	
	
	public static class httpRequestBody{
	    
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
	
	public static class httpResponseBody{
		    
		private String response1;
		private String response2;
		
		public httpResponseBody(String response1, String response2) {
			super();
			this.response1 = response1;
			this.response2 = response2;
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
	}
	
	
	@PostMapping(value = "/add_to_cart")
	@ResponseBody
	public httpResponseBody add_to_cart(@RequestBody httpRequestBody reqBody, Authentication authentication) {
		
		String principal_name = authentication.getName();
		User user = userService.findUserByUsername(principal_name);
		ShoppingCart cartElement = shopCartService.findById(user.getUserID(), reqBody.getBookID());
		if(cartElement != null ) {
			cartElement.setCopies(Integer.parseInt(reqBody.getArg2()) + cartElement.getCopies());
			shopCartService.update(cartElement);
		}
		else {
			shopCartService.create(user.getUserID(), reqBody.getBookID(), Integer.parseInt(reqBody.getArg2()));
		}
		
		return new httpResponseBody("", "");
	}
	
	
	@PostMapping(value = "/cart") 
	@ResponseBody
	public httpResponseBody cart_update(@RequestBody httpRequestBody reqBody, Locale locale, Model model,  Authentication authentication) {
		
		String principal_name = authentication.getName();
		User user = userService.findUserByUsername(principal_name);
		ShoppingCart cartElement = shopCartService.findById(user.getUserID(), reqBody.getBookID());
		if(reqBody.arg2.equals("delete")) {
			shopCartService.removeBook(cartElement);
			return new httpResponseBody("deleted", user.getFormattedCartTotalPrice()); 
		}
		else {if (reqBody.arg2.equals("minus")) {
			cartElement.setCopies(cartElement.getCopies()-1);}
			else {
				cartElement.setCopies(cartElement.getCopies()+1);}
			shopCartService.update(cartElement);
			return new httpResponseBody(cartElement.getFormattedElementTotalPrice(), user.getFormattedCartTotalPrice());
		}
		
	}
	
}
	
