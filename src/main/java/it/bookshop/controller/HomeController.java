package it.bookshop.controller;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import it.bookshop.model.entity.Author;
import it.bookshop.model.entity.Book;
import it.bookshop.model.entity.Genre;
import it.bookshop.model.entity.ShoppingCart;
import it.bookshop.model.entity.User;
import it.bookshop.services.AuthorService;

import it.bookshop.services.BookService;
import it.bookshop.services.UserService;

@Controller
public class HomeController {

	@Autowired
	String appName;
	
	@Autowired
	private BookService bookService;
	
	@Autowired
	private AuthorService authorService;
	
	@Autowired
	private UserService userService;

	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, Model model, Authentication authentication, 
			@RequestParam(required = false) Integer page, 
			@RequestParam(defaultValue = "8") Integer books_per_page) {
		System.out.println("Home Page Requested,  locale = " + locale);
		
		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
		String formattedDate = dateFormat.format(date);
		
		List <Author> topFiveAuthor = this.authorService.findBestSellingAuthor();
		List<Genre> allGenres = bookService.getAllGenres();
		List<Book> topFiveNewBooks = bookService.findFiveMostRecentBook();
		List<Book> topFiveBestSellersBooks = bookService.findFiveBestSellingBook();
		List<Book> topMostClickBook = this.bookService.findMostClickBook();
		
		// Pagination per I pi� visualizzati
		PagedListHolder<Book> pagedListHolder = new PagedListHolder<>(topMostClickBook);
		pagedListHolder.setPageSize(books_per_page);

		if (page == null || page < 1 || page > pagedListHolder.getPageCount())
			page = 1;

		pagedListHolder.setPage(page - 1);

		model.addAttribute("topMostClickBook", pagedListHolder.getPageList());
		model.addAttribute("maxPages", pagedListHolder.getPageCount());
		model.addAttribute("page", page);
		// Fine Pagination
		
		model.addAttribute("serverTime", formattedDate);
		model.addAttribute("appName", appName);
		model.addAttribute("topFiveAuthor", topFiveAuthor);
		model.addAttribute("allGenres", allGenres); //Da qui sono presi anche i generi della navbar
		model.addAttribute("topFiveNewBooks", topFiveNewBooks);
		model.addAttribute("topFiveBestSellersBooks", topFiveBestSellersBooks);
		//model.addAttribute("topMostClickBook", topMostClickBook);
		
		//Mini carrello
		if(authentication != null) {
			String principal_name = authentication.getName();
			User user = userService.findUserByUsername(principal_name);
			List<ShoppingCart> user_cart = new ArrayList<ShoppingCart>(user.getShoppingCart());
			model.addAttribute("user_cart", user_cart);
			model.addAttribute("cartTotalPrice", user.getFormattedCartSubtotalPrice());
			model.addAttribute("cartTotalItems", user.getCartTotalItems());
		}
		
		return "home";
	}
	
	// mostra tutti i libri filtrati Genere 
	@GetMapping(value = "/show_genre/{genre}")
	public String ShowBookforGenre(@PathVariable("genre") String genre,Model model, Authentication authentication) {
		Set<Book> bookGenre =  this.bookService.getAllBookForGenre(genre); // estrae tutti i libri per il genere scelto 
		List<Genre> allGenres = this.bookService.getAllGenres();

		
		
		model.addAttribute("appName", appName);
		model.addAttribute("books", bookGenre);
		model.addAttribute("single_genre", genre);
		model.addAttribute("genres", allGenres);
		model.addAttribute("allGenres", allGenres); // per la navbar
		
		//Mini carrello
		if(authentication != null) {
			String principal_name = authentication.getName();
			User user = userService.findUserByUsername(principal_name);
			List<ShoppingCart> user_cart = new ArrayList<ShoppingCart>(user.getShoppingCart());
			model.addAttribute("user_cart", user_cart);
			model.addAttribute("cartTotalPrice", user.getFormattedCartSubtotalPrice());
			model.addAttribute("cartTotalItems", user.getCartTotalItems());
		}

		return "grid_book"; 

	}
	
	// mostra tutti i libri in sconto 
	@RequestMapping(value = "/sales", method = RequestMethod.GET)
	public String ShowBookonSale(Model model, Authentication authentication) {
		List<Book> bookSale =  this.bookService.findBookOnSale(); // estrae tutti i libri in sconto 
		List<Genre> allGenres = this.bookService.getAllGenres();
        String sale = "In offerta";
		
		
		model.addAttribute("appName", appName);
		model.addAttribute("books", bookSale);
		model.addAttribute("single_genre", sale);
		model.addAttribute("genres", allGenres);
		model.addAttribute("allGenres", allGenres); // per la navbar
		
		//Mini carrello
		if(authentication != null) {
			String principal_name = authentication.getName();
			User user = userService.findUserByUsername(principal_name);
			List<ShoppingCart> user_cart = new ArrayList<ShoppingCart>(user.getShoppingCart());
			model.addAttribute("user_cart", user_cart);
			model.addAttribute("cartTotalPrice", user.getFormattedCartSubtotalPrice());
			model.addAttribute("cartTotalItems", user.getCartTotalItems());
		}

		return "grid_book";  

	}
	
	
	
	
	@GetMapping(value = "/show_book/{id}")
	public String ShowDetailsBook(@PathVariable("id") String id, Model model, Authentication authentication) {
		long l_id = Long.parseLong(id); // cast from string to long 
		this.bookService.add_click(l_id); 
		
		Book b = this.bookService.findById(l_id);
		Set<Author> authorSet = b.getAuthors();
				
		Set<Book> booksimilargenre = this.bookService.getBooksimilargenre(b);
		Set<Book> booksimilarauthor = this.bookService.getBooksimilarAuthor(b);
		// ccontrollo se le due liste sono vuote 
		Boolean checklistaut =  booksimilarauthor.isEmpty(); 
		Boolean checklistgenre =  booksimilargenre.isEmpty();
		
		List<Author> authorsList = this.authorService.getAuthorsListFromSet(authorSet);
		List<Genre> allGenres = this.bookService.getAllGenres();
		List <Author> topFiveAuthor = this.authorService.findBestSellingAuthor();
		List<Book> topFiveBestSellersBooks = bookService.findFiveBestSellingBook();
		
		
		model.addAttribute("authorsList", authorsList); // lista degli autori del libro
		model.addAttribute("appName", appName);
		model.addAttribute("book", b);
		model.addAttribute("checklistaut", checklistaut); 
		model.addAttribute("checklistgenre", checklistgenre);  
		model.addAttribute("booksimilgenre", booksimilargenre); // libri consigliati per genere
		model.addAttribute("booksimilaut", booksimilarauthor); // libri consigliati per autore 
		
		model.addAttribute("allGenres", allGenres); // per la navbar
		
		//Mini carrello
		if(authentication != null) {
			String principal_name = authentication.getName();
			User user = userService.findUserByUsername(principal_name);
			List<ShoppingCart> user_cart = new ArrayList<ShoppingCart>(user.getShoppingCart());
			model.addAttribute("user_cart", user_cart);
			model.addAttribute("cartTotalPrice", user.getFormattedCartSubtotalPrice());
			model.addAttribute("cartTotalItems", user.getCartTotalItems());
		}

		return "single_book"; 


	}
	
	@GetMapping(value = "/show_author/{id}")
	public String ShowDetailsAuthor(@PathVariable("id") String id, Model model, Authentication authentication) {
		long long_id = Long.parseLong(id);
		Author author = this.authorService.findById(long_id);
		
		List<Book> authorBooks = this.bookService.findBooksAuthor(author);
		Boolean checkAuthorBooks = authorBooks.isEmpty();
		
		model.addAttribute("appName", appName);
		model.addAttribute("author", author);
		model.addAttribute("authorBooks", authorBooks);
		model.addAttribute("checkAuthorBooks",checkAuthorBooks);
		
		//Mini carrello
		if(authentication != null) {
			String principal_name = authentication.getName();
			User user = userService.findUserByUsername(principal_name);
			List<ShoppingCart> user_cart = new ArrayList<ShoppingCart>(user.getShoppingCart());
			model.addAttribute("user_cart", user_cart);
			model.addAttribute("cartTotalPrice", user.getFormattedCartSubtotalPrice());
			model.addAttribute("cartTotalItems", user.getCartTotalItems());
		}
		
		return "single_author";
	}
	
	@GetMapping(value = "/about_us")
	public String aboutUs(Model model, Authentication authentication) {
		
		List<Genre> allGenres = bookService.getAllGenres();
		model.addAttribute("allGenres", allGenres); //Da qui sono presi anche i generi della navbar
		
		//Mini carrello
		if(authentication != null) {
			String principal_name = authentication.getName();
			User user = userService.findUserByUsername(principal_name);
			List<ShoppingCart> user_cart = new ArrayList<ShoppingCart>(user.getShoppingCart());
			model.addAttribute("user_cart", user_cart);
			model.addAttribute("cartTotalPrice", user.getFormattedCartSubtotalPrice());
			model.addAttribute("cartTotalItems", user.getCartTotalItems());
		}
		return "about_us";
	}
	
	@GetMapping(value = "/faq")
	public String faq(Model model, Authentication authentication) {
		
		List<Genre> allGenres = bookService.getAllGenres();
		model.addAttribute("allGenres", allGenres); //Da qui sono presi anche i generi della navbar
		
		//Mini carrello
		if(authentication != null) {
			String principal_name = authentication.getName();
			User user = userService.findUserByUsername(principal_name);
			List<ShoppingCart> user_cart = new ArrayList<ShoppingCart>(user.getShoppingCart());
			model.addAttribute("user_cart", user_cart);
			model.addAttribute("cartTotalPrice", user.getFormattedCartSubtotalPrice());
			model.addAttribute("cartTotalItems", user.getCartTotalItems());
		}
		return "faq";
	}
	
	@GetMapping(value = "/privacy&terms")
	public String privacy_sell_terms(Model model, Authentication authentication) {
		
		List<Genre> allGenres = bookService.getAllGenres();
		model.addAttribute("allGenres", allGenres); //Da qui sono presi anche i generi della navbar
		
		//Mini carrello
		if(authentication != null) {
			String principal_name = authentication.getName();
			User user = userService.findUserByUsername(principal_name);
			List<ShoppingCart> user_cart = new ArrayList<ShoppingCart>(user.getShoppingCart());
			model.addAttribute("user_cart", user_cart);
			model.addAttribute("cartTotalPrice", user.getFormattedCartSubtotalPrice());
			model.addAttribute("cartTotalItems", user.getCartTotalItems());
		}
		return "privacy_terms";
	}
	
	@GetMapping(value = "/help")
	public String help(Model model, Authentication authentication) {
		
		List<Genre> allGenres = bookService.getAllGenres();
		model.addAttribute("allGenres", allGenres); //Da qui sono presi anche i generi della navbar
		
		//Mini carrello
		if(authentication != null) {
			String principal_name = authentication.getName();
			User user = userService.findUserByUsername(principal_name);
			List<ShoppingCart> user_cart = new ArrayList<ShoppingCart>(user.getShoppingCart());
			model.addAttribute("user_cart", user_cart);
			model.addAttribute("cartTotalPrice", user.getFormattedCartSubtotalPrice());
			model.addAttribute("cartTotalItems", user.getCartTotalItems());
		}
		return "help";
	}
}
