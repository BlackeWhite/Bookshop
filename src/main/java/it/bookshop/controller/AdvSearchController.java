package it.bookshop.controller;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.sql.Date;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import it.bookshop.model.entity.Book;
import it.bookshop.services.BookService;

@Controller
public class AdvSearchController {

	@Autowired
	String appName;
	
	@Autowired
	private BookService bookService;
	
	@RequestMapping(value = "/advanced_search", method = RequestMethod.GET)
	public String advSearch(Locale locale, Model model) {
		System.out.println("Advanced search Page Requested,  locale = " + locale);
		
		DateFormat date = new SimpleDateFormat("dd-MM-yyyy");
		java.util.Date date2 = null;
		try {
			date2 = date.parse("21-01-2005");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Date publish_date = new Date(date2.getTime());
		bookService.deleteAll();
		
		bookService.create("Dante", "Alighieri", "838832989113223", "La Divina Commedia", 
				publish_date, 3, 34, null, 300, "Nel mezzo del cammin...", null, "Poema");
		bookService.create("J.J.R.", "Tolkien", "746382492401", "Il Signore degli Anelli - Le due torri", 
				publish_date, 4, 40.99, null, 450, "Sauron è tornato a Mordor...", null, "Fantasy");
		bookService.create("Alessandro", "Manzoni", "8235234631481401", "I promessi sposi", 
				publish_date, 10, 25.99, null, 370, "Renzo e Lucia ...", null, "Romanzo");
		
		List<Book> books = bookService.findAll();
		
		model.addAttribute("appName", appName);
		model.addAttribute("books", books);

		return "advanced_search";
	}
	
}
