package it.bookshop.controller;

import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.text.DateFormat;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import it.bookshop.services.BookService;

import it.bookshop.model.entity.User;
import it.bookshop.model.entity.Author;
import it.bookshop.model.entity.Book;
import it.bookshop.model.entity.BookOrder;
import it.bookshop.model.entity.BookOrderId;
import it.bookshop.model.entity.Order;
import it.bookshop.model.entity.ShoppingCart;
import it.bookshop.model.entity.ShoppingCartId;

import it.bookshop.model.dao.ShoppingCartDao;

import it.bookshop.services.BookService;
import it.bookshop.services.UserService;
import it.bookshop.services.OrderService;
import it.bookshop.services.ShoppingCartService;


@Controller
public class CartController {

	@Autowired
	String appName;
	
	
	@Autowired
	private UserService userService;
	@Autowired
	private ShoppingCartService shopCartService;

	
	@RequestMapping(value = "/cart", method = RequestMethod.GET)
	public String cart(Locale locale, Model model) {
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
		
		
		Long test_user_id = Long.valueOf(1);
		User u = userService.findUserById(test_user_id);
		
		
		List<ShoppingCart> user_cart = new ArrayList<ShoppingCart>(u.getShoppingCart());
		
		
		model.addAttribute("user_cart", user_cart);
		model.addAttribute("appName", appName);

		return "cart";
	}
	
}
