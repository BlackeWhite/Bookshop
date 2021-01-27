package it.bookshop.controller;

import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.text.DateFormat;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;



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
		model.addAttribute("user_cart", user_cart);
		model.addAttribute("appName", appName);

		return "cart";
	}
	
	@PostMapping(value = "/add_to_cart")
	public String add_to_cart(@RequestParam("bookID") Long bookID, Authentication authentication) {
		
		String principal_name = authentication.getName();
		System.out.println(principal_name);
		User user = userService.findUserByUsername(principal_name);
		this.shopCartService.create(user.getUserID(), bookID, 1);
		
		return "redirect:/advanced_search";
		
	}
	
	
	public static class httpRequestBody{
	    
		public httpRequestBody() {	
		}
	    
	    private long b_id;
		private String operation;
		
	    public long getB_id() {
			return b_id;
		}
	    
		public void setB_id(long b_id) {
			this.b_id = b_id;
		}
		public String getOperation() {
			return operation;
		}
		public void setOperation(String operation) {
			this.operation = operation;
		}
	    
	}
	
	public static class httpResponseBody{
		    
		private String response;
		
		public httpResponseBody(String response) {
			super();
			this.response = response;
		}
	
		public String getResponse() {
			return response;
		}
	
		public void setResponse(String response) {
			this.response = response;
		}
		
	}
	
	
	@PostMapping(value = "/cart") 
	@ResponseBody
	public httpResponseBody cart_update(@RequestBody httpRequestBody reqBody, Locale locale, Model model,  Authentication authentication) {
		
		String principal_name = authentication.getName();
		User user = userService.findUserByUsername(principal_name);
		ShoppingCart cartElement = shopCartService.findById(user.getUserID(), reqBody.getB_id());
		if(reqBody.operation.equals("delete")) {
			shopCartService.removeBook(cartElement);
			return new httpResponseBody("deleted"); 
		}
		else {if (reqBody.operation.equals("minus")) {
			cartElement.setCopies(cartElement.getCopies()-1);}
			else {
				cartElement.setCopies(cartElement.getCopies()+1);}
			shopCartService.update(cartElement);
			return new httpResponseBody(cartElement.getTotalFormattedPrice());}
	}
	
}
	
