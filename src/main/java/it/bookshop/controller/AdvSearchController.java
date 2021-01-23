package it.bookshop.controller;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

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
	public String advSearch(@RequestParam(required = false) Map<String,String> params,Locale locale, Model model) {
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
				publish_date2, publish_date2, 4, 40.99, null, 450, "Sauron è tornato a Mordor...", "7.jpg", "Fantasy");
		bookService.create("Alessandro", "Manzoni", "8235234631481401", "I promessi sposi", 
				publish_date3, publish_date3, 10, 25.99, null, 370, "Renzo e Lucia ...", "8.jpg", "Romanzo");
		bookService.create("Dante", "Alighieri", "838832989113223", "La DivinaTRISTEa", 
				publish_date4, publish_date4, 3, 34, null, 300, "Nel mezzo del cammin...", "6.jpg", "Poema");
		bookService.create("J.J.R.", "Tolkien", "746382492401", "Il SignAAAAore degli Anelli - Le due torri", 
				publish_date5, publish_date5,4, 40.99, null, 450, "Sauron è tornato a Mordor...", "7.jpg", "Fantasy");
		bookService.create("Alessandro", "Manzoni", "8235234631481401", "I pCAZZromessi sposi", 
				publish_date6, publish_date6,10, 25.99, null, 370, "Renzo e Lucia ...", "8.jpg", "Romanzo");
		*/
				
		
		List<Book> books;
		String genre = params.get("genre");
		String id = params.get("authorId");
		if(genre!=null) {
			Genre gen = genreDao.findByName(genre);
			books = new ArrayList<Book>(gen.getBooks());
		} else if(id!=null) {
			Author a = authorService.findById(Long.parseLong(id));
			books = new ArrayList<Book>(a.getBooks());
		}
		else books = bookService.findAll();
		
		List<Genre> genres = genreDao.findAll();
		
		List<Book> top5 = bookService.findFiveBestSellingBook();
		
		//List<Author> top10authors = authorService.findBestSellingAuthor();
		
		model.addAttribute("appName", appName);
		model.addAttribute("books", books);
		model.addAttribute("best_sellers", top5);
		//model.addAttribute("top_authors", top10authors);
		model.addAttribute("genres", genres);

		return "advanced_search";
	}
	
}
