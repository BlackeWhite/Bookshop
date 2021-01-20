package it.bookshop.controller;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import it.bookshop.model.entity.Book;
import it.bookshop.model.entity.Genre;
import it.bookshop.services.BookService;

@Controller
public class HomeController {

	@Autowired
	String appName;
	
	@Autowired
	private BookService bookService;

	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, Model model) {
		System.out.println("Home Page Requested,  locale = " + locale);
		
		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);

		String formattedDate = dateFormat.format(date);

		model.addAttribute("serverTime", formattedDate);
		
		model.addAttribute("appName", appName);

		List<Genre> allGenres = bookService.getAllGenres();
		
		List<Book> topFiveNewBooks = bookService.findFiveMostRecentBook();
		
		model.addAttribute("allGenres", allGenres); //Da qui sono presi anche i generi della navbar
		model.addAttribute("topFiveNewBooks", topFiveNewBooks);
	
		return "home";
	}

}
