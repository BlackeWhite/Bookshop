package it.bookshop.controller;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
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
import it.bookshop.services.CouponService;
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
	
	@Autowired
	private CouponService couponService;

	//Homepage
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
	
	//Pagina della ricerca avanzata
	@RequestMapping(value = "/advanced_search", method = RequestMethod.GET)
	public String advSearch(@RequestParam(defaultValue = "title") String search_by,
			@RequestParam(defaultValue = "") String term, @RequestParam(required = false) List<String> genres,
			@RequestParam(defaultValue = "title_ASC") String order_by, @RequestParam(required = false) Long authorId,
			@RequestParam(defaultValue = "0") Double price_min, @RequestParam(defaultValue = "10000") Double price_max,
			@RequestParam(required = false) Integer page, @RequestParam(defaultValue = "6") Integer books_per_page,
			Authentication authentication, Locale locale, Model model) {

		System.out.println("Advanced search Page Requested,  locale = " + locale);

		List<Genre> allGenres = this.bookService.getAllGenres();

		List<Book> books;
		books = bookService.searchBooksByParams(search_by, term, price_min, price_max, order_by);
		

		//Numero di libri per genere una volta fitrati dai parametri di ricerca
		Map<String, Integer> books_for_genre = bookService.booksAmountPerGenreFromList(books);

		// Filtra per generi
		if (genres != null && !genres.isEmpty()) {
			books = bookService.filterByGenres(books, genres);
		}

		//Paginazione
		PagedListHolder<Book> pagedListHolder = new PagedListHolder<>(books);
		pagedListHolder.setPageSize(books_per_page);

		if (page == null || page < 1 || page > pagedListHolder.getPageCount())
			page = 1;

		pagedListHolder.setPage(page - 1);

		model.addAttribute("books", pagedListHolder.getPageList());
		model.addAttribute("maxPages", pagedListHolder.getPageCount());
		model.addAttribute("page", page);

		model.addAttribute("appName", appName);
		model.addAttribute("allGenres", allGenres);
		model.addAttribute("books_for_genre", books_for_genre);
		model.addAttribute("term", term);
		model.addAttribute("priceMin", price_min);
		model.addAttribute("priceMax", (price_max==10000) ? 70: price_max);

		// Mini carrello a comparsa
		if (authentication != null) {
			String principal_name = authentication.getName();
			User user = userService.findUserByUsername(principal_name);
			List<ShoppingCart> user_cart = new ArrayList<ShoppingCart>(user.getShoppingCart());
			model.addAttribute("user_cart", user_cart);
			model.addAttribute("cartTotalPrice", user.getFormattedCartSubtotalPrice());
			model.addAttribute("cartTotalItems", user.getCartTotalItems());
		}
		System.out.println(books.size());
		return "advanced_search";
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
	
	//Pagina del singolo libro
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
		
		
		model.addAttribute("authorsList", authorsList); // lista degli autori del libro
		model.addAttribute("appName", appName);
		model.addAttribute("book", b);
		model.addAttribute("checklistaut", checklistaut); 
		model.addAttribute("checklistgenre", checklistgenre);  
		model.addAttribute("booksimilgenre", booksimilargenre); // libri consigliati per genere
		model.addAttribute("booksimilaut", booksimilarauthor); // libri consigliati per autore 
		
		model.addAttribute("allGenres", allGenres); // per la navbar
		
		//Mini carrello a comparsa
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
	
	//Pagina del singolo autore
	@GetMapping(value = "/show_author/{id}")
	public String ShowDetailsAuthor(@PathVariable("id") String id, Model model, Authentication authentication) {
		long long_id = Long.parseLong(id);
		Author author = this.authorService.findById(long_id);
		
		List<Genre> allGenres = this.bookService.getAllGenres();
		
		List<Book> authorBooks = this.bookService.findBooksAuthor(author);
		Boolean checkAuthorBooks = authorBooks.isEmpty();
		
		model.addAttribute("appName", appName);
		model.addAttribute("author", author);
		model.addAttribute("authorBooks", authorBooks);
		model.addAttribute("checkAuthorBooks",checkAuthorBooks);
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
		
		return "single_author";
	}
	
	//Pagina "chi siamo"
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
	
	//Pagina delle "FAQ"
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
	
	//Pagina "Termini e privacy"
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
	
	//Pagina "aiuto"
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
	
	//Popola il db con elementi di test iniziali
	@GetMapping(value = "/populatedb")
	public String populatedb(Locale locale, Model model) {

		userService.findOrCreateRole("USER");
		userService.findOrCreateRole("ADMIN");
		userService.findOrCreateRole("SELLER");
		User admin = userService.findUserByUsername("admin");
		if (admin == null) {
			userService.create("admin", "admin@email.com", "admin", "admin", "admin", null, "Via brecce bianche",
					"Ancona", 60000, "Italia", Arrays.asList("ADMIN"));
		}
		// venditori 
		User seller = userService.findUserByUsername("libreria");
		if (seller == null) {
			seller  = userService.create("libreria", "seller@email.com", "1234", "libreria", "ragni", null,
					"Via ugo bassi", "Ancona", 60000, "Italia", Arrays.asList("SELLER"));
 
		}
		
		User seller1 = userService.findUserByUsername("Mondadori");
		if (seller1 == null) {
			seller1  = userService.create("Mondadori", "mondadori@email.com", "1234", "Ernesto", "Mauri", null,
					"Via tiburtina", "Milano", 20010, "Italia", Arrays.asList("SELLER"));
 
		}
		
		DateFormat date = new SimpleDateFormat("dd-MM-yyyy");
		Date date1 = null;
		Date date2 = null;
		Date date3 = null;
		Date date4 = null;
		Date date5 = null;
		Date date6 = null;
		Date date7 = null;
		Date datec1 = null;
		Date datec2 = null;
		try {
			date1 = date.parse("21-01-2005");
			date2 = date.parse("28-03-2008");
			date3 = date.parse("21-11-2009");
			date4 = date.parse("01-01-2012");
			date5 = date.parse("15-08-2014");
			date6 = date.parse("21-01-2018");
			date7 = date.parse("21-01-1995");
			datec1 = date.parse("21-04-2021");
			datec2 = date.parse("31-12-2021");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		java.sql.Date publish_date1 = new java.sql.Date(date1.getTime());
		java.sql.Date publish_date2 = new java.sql.Date(date2.getTime());
		java.sql.Date publish_date3 = new java.sql.Date(date3.getTime());
		java.sql.Date publish_date4 = new java.sql.Date(date4.getTime());
		java.sql.Date publish_date5 = new java.sql.Date(date5.getTime());
		java.sql.Date publish_date6 = new java.sql.Date(date6.getTime());
		java.sql.Date date_birth = new java.sql.Date(date7.getTime());
		java.sql.Date dateC1 = new java.sql.Date(datec1.getTime());
		java.sql.Date dateC2 = new java.sql.Date(datec2.getTime());
		
		// acquirenti
		User buyer = userService.findUserByUsername("user1");
		if (buyer == null) {
			buyer = userService.create("user1", "user1@email.com", "5678", "Roberto", "Rossi", date_birth,
					"Via ugo bassi", "Ancona", 60121, "Italia", Arrays.asList("USER"));
 
		}
		
		User buyer1 = userService.findUserByUsername("user2");
		if (buyer1 == null) {
			buyer1 = userService.create("user2", "user2@email.com", "0000", "Franceso", "Pozzo", date_birth,
					"Via flaminia", "Ancona", 60121, "Italia", Arrays.asList("USER"));
 
		}
		
	
		
		bookService.deleteAll();

		bookService.create("Dante", "Alighieri", "838832989113223", "La Divina Commedia", publish_date1, publish_date1,
				3, 34, seller, 300, "Nel mezzo del cammin...", "6.jpg", Arrays.asList("Poema"), 0.15);
		bookService.create("J.J.R.", "Tolkien", "746382492401", "Il Signore degli Anelli - Le due torri", publish_date2,
				publish_date2, 4, 40.99, seller1, 450, "Sauron � tornato a Mordor...", "7.jpg", Arrays.asList("Fantasy"),
				0);
		bookService.create("Alessandro", "Manzoni", "8235234631481401", "I promessi sposi", publish_date3,
				publish_date3, 10, 25.99, seller, 370, "Renzo e Lucia ...", "8.jpg", Arrays.asList("Romanzo"), 0);
		bookService.create("H.P.", "Lovecraft", "83883267788997", "Il caso di Charles Dexter Ward", publish_date4,
				publish_date4, 3, 39, seller, 300, "Charles Dexter Ward era il discendente di...", "11.jpg",
				Arrays.asList("Horror", "Fantasy"), 0);
		bookService.create("J.J.R.", "Tolkien", "344567880909", "Il Signore degli Anelli - La compagnia dell'anello",
				publish_date5, publish_date5, 7, 42.50, seller, 450, "Frodo Baggins stava tornando a casa...", "9.jpg",
				Arrays.asList("Fantasy"), 0);
		bookService.create("J.J.R.", "Tolkien", "9283755352729", "Il Signore degli Anelli - Il ritorno del re",
				publish_date6, publish_date6, 10, 37.99, seller, 390, "Aragorn, dopo un discorso da Oscar ...", "10.jpg",
				Arrays.asList("Fantasy"), 0.10);
		bookService.create("Michael", "Crichton", "198934345798876", "Jurassic Park", publish_date3, publish_date3, 12,
				18.30, seller1, 267, "Alan Grant � un paleontologo che ...", "12.jpg",
				Arrays.asList("Fantascienza", "Horror"), 0);
		bookService.create("Stephen W.", "Hawking", "5667899121887",
				"Dal Big Bang ai buchi neri - Breve storia del tempo", publish_date4, publish_date4, 18, 23, seller1, 280,
				"Il Big Bang � stato un evento ...", "13.jpg", Arrays.asList("Divulgativo"), 0.20);
		bookService.create("Primo", "Levi", "23456789045678", "Se questo � un uomo", publish_date1, publish_date1, 15,
				28.99, seller1, 220, "Boh non me ricordo ...", "14.jpg", Arrays.asList("Biografia"), 0);
		bookService.create("Michael", "Ende", "345678899876756", "La storia infinita", publish_date2, publish_date2, 20,
				15.00, seller1, 440, "Questa scritta stava sulla porta ...", "15.jpg",
				Arrays.asList("Avventura", "Fantasy"), 0);
		bookService.create("Rick", "Riordan", "234559878653546",
				"Percy Jackson e gli dei dell'Olimpo - Il ladro di fulmini", publish_date4, publish_date4, 15, 17, seller1,
				500, "Percy era il figlio Poseidone...", "16.jpg", Arrays.asList("Avventura", "Fantasy"), 0);
		bookService.create("Conan", "Doyle", "111111111111", "Le avventure di Sherlock Holmes", publish_date6,
				publish_date6, 5, 20.99, seller, 256, "Elementare Watson! ...", "17.jpg",
				Arrays.asList("Giallo", "Romanzo"), 0.25);
		
		//coupons generation
		couponService.create("INVERNO2021", 10, dateC1);
		couponService.create("EXTRASCONTO15", 15, dateC2);
		
		return "redirect:/advanced_search";
	}
}
