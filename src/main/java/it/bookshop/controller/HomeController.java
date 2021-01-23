package it.bookshop.controller;

import java.text.DateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import it.bookshop.model.entity.Author;
import it.bookshop.model.entity.Book;
import it.bookshop.model.entity.Genre;
import it.bookshop.services.AuthorService;
import it.bookshop.services.BookService;

@Controller
public class HomeController {

	@Autowired
	String appName;
	
	@Autowired
	private BookService bookService;
	
	@Autowired
	private AuthorService authorService;

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
	
	// mostra tutti i libri filtrati Genere 
	@GetMapping(value = "/show_genre/{genre}")
	public String ShowBookforGenre(@PathVariable("genre") String genre,Model model) {
		List<Book> bookGenre =  this.bookService.getAllBookForGenre(genre); // estrae tutti i libri per il genere scelto 
		List<Genre> allGenres = this.bookService.getAllGenres();
		
		// preso dall'advSearch controller 
		List<Book> orderedbooks = bookService.findAll();
		Comparator<Book> compareByOrders = (Book b1, Book b2) -> b1.getOrders().size()-b2.getOrders().size();
		orderedbooks.sort(compareByOrders);
		List<Book> top10;
		if(orderedbooks.size()>10) top10 = orderedbooks.subList(0,9);
		else top10 = orderedbooks;
		
		List<Author> orderedauthors = authorService.findAll();
		Comparator<Author> compareByBooks = (Author a1, Author a2) -> a1.getBooks().size()-a2.getBooks().size();
		orderedauthors.sort(compareByBooks);
		List<Author> top10authors;
		if(orderedauthors.size()>10) top10authors = orderedauthors.subList(0,9);
		else top10authors = orderedauthors;
		
		model.addAttribute("appName", appName);
		model.addAttribute("books", bookGenre);
		model.addAttribute("best_sellers", top10);
		model.addAttribute("top_authors", top10authors);
		model.addAttribute("genres", allGenres);
		model.addAttribute("allGenres", allGenres); // per la navbar

		return "advanced_search"; // riutilizzo la vista, siccome  è simile 

	}

}
