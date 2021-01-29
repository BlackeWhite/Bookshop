package it.bookshop.controller;

import java.text.DateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;

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
		
		List <Author> topFiveAuthor = this.authorService.findBestSellingAuthor();
		List<Genre> allGenres = bookService.getAllGenres();
		List<Book> topFiveNewBooks = bookService.findFiveMostRecentBook();
		List<Book> topFiveBestSellersBooks = bookService.findFiveBestSellingBook();
		List<Book> topMostClickBook = this.bookService.findMostClickBook();
		
		model.addAttribute("serverTime", formattedDate);
		model.addAttribute("appName", appName);
		model.addAttribute("topFiveAuthor", topFiveAuthor);
		model.addAttribute("allGenres", allGenres); //Da qui sono presi anche i generi della navbar
		model.addAttribute("topFiveNewBooks", topFiveNewBooks);
		model.addAttribute("topFiveBestSellersBooks", topFiveBestSellersBooks);
		model.addAttribute("topMostClickBook", topMostClickBook);
		return "home";
	}
	
	// mostra tutti i libri filtrati Genere 
	@GetMapping(value = "/show_genre/{genre}")
	public String ShowBookforGenre(@PathVariable("genre") String genre,Model model) {
		Set<Book> bookGenre =  this.bookService.getAllBookForGenre(genre); // estrae tutti i libri per il genere scelto 
		List<Genre> allGenres = this.bookService.getAllGenres();
		List <Author> topFiveAuthor = this.authorService.findBestSellingAuthor();
		List<Book> topFiveBestSellersBooks = this.bookService.findFiveBestSellingBook();
		
		
		model.addAttribute("appName", appName);
		model.addAttribute("books", bookGenre);
		model.addAttribute("best_sellers", topFiveBestSellersBooks);
		model.addAttribute("top_authors", topFiveAuthor);
		model.addAttribute("genres", allGenres);
		model.addAttribute("allGenres", allGenres); // per la navbar

		return "advanced_search"; // riutilizzo la vista, siccome  � simile 

	}
	
	
	@GetMapping(value = "/show_book/{id}")
	public String ShowDetailsBook(@PathVariable("id") String id,Model model) {
		long l_id = Long.parseLong(id); // cast from string to long 
		this.bookService.add_click(l_id); 
		
		Book b = this.bookService.findById(l_id);
		Set<Author> authorSet = b.getAuthors();
				
		Set<Book> booksimilargenre = this.bookService.getBooksimilargenre(b);
		Set<Book> booksimilarauthor = this.bookService.getBooksimilarAuthor(b);
		
		
		List<Author> authorsList = this.authorService.getAuthorsListFromSet(authorSet);
		List<Genre> allGenres = this.bookService.getAllGenres();
		List <Author> topFiveAuthor = this.authorService.findBestSellingAuthor();
		List<Book> topFiveBestSellersBooks = bookService.findFiveBestSellingBook();
		
		
		model.addAttribute("authorsList", authorsList); // lista degli autori del libro
		model.addAttribute("appName", appName);
		model.addAttribute("book", b);
		model.addAttribute("booksimilgenre", booksimilargenre); // libri consigliati per genere
		model.addAttribute("booksimilaut", booksimilarauthor); // libri consigliati per autore 
		
		model.addAttribute("allGenres", allGenres); // per la navbar

		return "single_book"; 


	}
	
	@GetMapping(value = "/show_author/{id}")
	public String ShowDetailsAuthor(@PathVariable("id") String id, Model model) {
		long long_id = Long.parseLong(id);
		Author author = this.authorService.findById(long_id);
		
		model.addAttribute("appName", appName);
		model.addAttribute("author", author);
		
		return "single_author";
	}
}
