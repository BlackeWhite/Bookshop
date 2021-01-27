package it.bookshop.controller;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import it.bookshop.model.dao.GenreDao;
import it.bookshop.model.entity.Author;
import it.bookshop.model.entity.Book;
import it.bookshop.model.entity.Genre;
import it.bookshop.services.AuthorService;
import it.bookshop.services.BookService;

@Controller
public class AdvSearchController {

	@Autowired
	String appName;
	
	@Autowired
	private BookService bookService;
	
	@Autowired
	private GenreDao genreDao;
	
	@Autowired
	private AuthorService authorService;
	
	@RequestMapping(value = "/advanced_search", method = RequestMethod.GET)
	public String advSearch(@RequestParam(required = false) List<String> genres, @RequestParam(defaultValue="") String term,
			@RequestParam(defaultValue = "title_ASC") String order_by, @RequestParam(required = false) Long authorId,
			@RequestParam(defaultValue = "0") Double price_min, @RequestParam(defaultValue = "50") Double price_max, 
			Locale locale, Model model) {
		System.out.println("Advanced search Page Requested,  locale = " + locale);
		
		/*
		DateFormat date = new SimpleDateFormat("dd-MM-yyyy");
		java.util.Date date1 = null;
		java.util.Date date2 = null;
		java.util.Date date3 = null;
		java.util.Date date4 = null;
		java.util.Date date5 = null;
		java.util.Date date6 = null;
		try {
			date1 = date.parse("21-01-2005");
			date2 = date.parse("28-03-2008");
			date3 = date.parse("21-11-2009");
			date4 = date.parse("01-01-2012");
			date5 = date.parse("15-08-2014");
			date6 = date.parse("21-01-2018");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Date publish_date1 = new Date(date1.getTime());
		Date publish_date2 = new Date(date2.getTime());
		Date publish_date3 = new Date(date3.getTime());
		Date publish_date4 = new Date(date4.getTime());
		Date publish_date5 = new Date(date5.getTime());
		Date publish_date6 = new Date(date6.getTime());
		bookService.deleteAll();
		
		bookService.create("Dante", "Alighieri", "838832989113223", "La Divina Commedia", 
				publish_date1,publish_date1, 3, 34, null, 300, "Nel mezzo del cammin...", "6.jpg", "Poema");
		bookService.create("J.J.R.", "Tolkien", "746382492401", "Il Signore degli Anelli - Le due torri", 
				publish_date2, publish_date2, 4, 40.99, null, 450, "Sauron � tornato a Mordor...", "7.jpg", "Fantasy");
		bookService.create("Alessandro", "Manzoni", "8235234631481401", "I promessi sposi", 
				publish_date3, publish_date3, 10, 25.99, null, 370, "Renzo e Lucia ...", "8.jpg", "Romanzo");
		bookService.create("Dante", "Alighieri", "838832989113223", "La DivinaTRISTEa", 
				publish_date4, publish_date4, 3, 34, null, 300, "Nel mezzo del cammin...", "6.jpg", "Poema");
		bookService.create("J.J.R.", "Tolkien", "746382492401", "Il SignAAAAore degli Anelli - Le due torri", 
				publish_date5, publish_date5,4, 40.99, null, 450, "Sauron � tornato a Mordor...", "7.jpg", "Fantasy");
		bookService.create("Alessandro", "Manzoni", "8235234631481401", "I pCAZZromessi sposi", 
				publish_date6, publish_date6,10, 25.99, null, 370, "Renzo e Lucia ...", "8.jpg", "Romanzo");
		*/
	
		
		List<Genre> allGenres = this.bookService.getAllGenres();
		List <Author> topFiveAuthor = this.authorService.findBestSellingAuthor();
		List<Book> topFiveBestSellersBooks = bookService.findFiveBestSellingBook();
		
		
		List<Book> books;
		if(!term.equals("")) {
			books = bookService.searchBooksByParams(term, price_min, price_max, order_by);
		} else if(authorId != null) {
			Set<Book> a_books = authorService.findById(authorId).getBooks();
			books = new ArrayList<Book>(a_books);
		} else {
			books = bookService.findAll(order_by);
		}
		
		Map<String, Integer> books_for_genre = new HashMap<String, Integer>();
		//Calcola il numero di libri per generi in base alla ricerca
		for(Genre g: allGenres) {
			books_for_genre.put(g.getName(), 0);
		}
		for(Book b: books) {
			for(Genre g: b.getGenres()) {
				int val = books_for_genre.getOrDefault(g.getName(), 0);
				books_for_genre.put(g.getName(), ++val);
			}
		}
		
		//Filtra per generi
		if(genres != null && !genres.isEmpty()) {
			List<Genre> pickedGenres = bookService.findGenresFromNamesArray(genres);
			books = books.stream()
					.filter(p -> p.getGenres().stream().map(Genre::getName).
							anyMatch(pickedGenres.stream().map(Genre::getName).collect(Collectors.toSet())::contains))
					.collect(Collectors.toList());
		}
		
		
		model.addAttribute("appName", appName);
		model.addAttribute("books", books);
		model.addAttribute("best_sellers", topFiveBestSellersBooks);
		model.addAttribute("top_authors", topFiveAuthor);
		model.addAttribute("allGenres", allGenres);
		model.addAttribute("books_for_genre", books_for_genre);
		model.addAttribute("term", term);
		model.addAttribute("priceMin", price_min);
		model.addAttribute("priceMax", price_max);

		return "advanced_search";
	}
	
}
