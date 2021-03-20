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

import org.hibernate.exception.ConstraintViolationException;
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

	// Homepage
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, Model model, Authentication authentication,
			@RequestParam(required = false) Integer page, @RequestParam(defaultValue = "8") Integer books_per_page) {
		System.out.println("Home Page Requested,  locale = " + locale);

		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
		String formattedDate = dateFormat.format(date);

		List<Author> topFiveAuthor = this.authorService.findBestSellingAuthor(); // restituisce la lista degli autori(i primi 5) che hanno venduto più copie di libri
		List<Genre> allGenres = bookService.getAllGenres(); // restituisce la lista dei generi dei libri 
		List<Book> topFiveNewBooks = bookService.findFiveMostRecentBook(); // restituisce la lista dei primi 5 libri aggiunti di recente 
		List<Book> topFiveBestSellersBooks = bookService.findFiveBestSellingBook(); // restituisce la lista dei primi 5 libri più venduti
		List<Book> topMostClickBook = this.bookService.findMostClickBook(); // restituisce una lista di libri ordinati dal piu' cliccato al meno cliccato 

		// Pagination per I piu' visualizzati
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
		model.addAttribute("allGenres", allGenres); // Da qui sono presi anche i generi per la navbar
		model.addAttribute("topFiveNewBooks", topFiveNewBooks);
		model.addAttribute("topFiveBestSellersBooks", topFiveBestSellersBooks);
		

		// Mini carrello mostrato solo all'utente loggato con ruolo USER 
		if (authentication != null) {
			String principal_name = authentication.getName();
			User user = userService.findUserByUsername(principal_name);
			List<ShoppingCart> user_cart = new ArrayList<ShoppingCart>(user.getShoppingCart());
			model.addAttribute("user_cart", user_cart);
			model.addAttribute("cartTotalPrice", user.getFormattedCartSubtotalPrice());
			model.addAttribute("cartTotalItems", user.getCartTotalItems());
		}

		return "home";
	}

	// Pagina della ricerca avanzata
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

		// Numero di libri per genere una volta fitrati dai parametri di ricerca
		Map<String, Integer> books_for_genre = bookService.booksAmountPerGenreFromList(books);

		// Filtra per generi
		if (genres != null && !genres.isEmpty()) {
			books = bookService.filterByGenres(books, genres);
		}

		// Paginazione
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
		model.addAttribute("priceMax", (price_max == 10000) ? 70 : price_max);

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

	// mostra tutti i libri di un singolo genere
	@GetMapping(value = "/show_genre/{genre}")
	public String ShowBookforGenre(@PathVariable("genre") String genre, @RequestParam(required = false) Integer page,
			Model model, Authentication authentication) {
		Set<Book> bookGenre = this.bookService.getAllBookForGenre(genre); // estrae tutti i libri per il genere scelto
		List<Genre> allGenres = this.bookService.getAllGenres(); // Da qui sono presi anche i generi per la navbar

		// Paginazione
		PagedListHolder<Book> pagedListHolder = new PagedListHolder<>(new ArrayList<Book>(bookGenre));
		pagedListHolder.setPageSize(8);

		if (page == null || page < 1 || page > pagedListHolder.getPageCount())
			page = 1;

		pagedListHolder.setPage(page - 1);

		model.addAttribute("books", pagedListHolder.getPageList());
		model.addAttribute("maxPages", pagedListHolder.getPageCount());
		model.addAttribute("page", page);

		model.addAttribute("appName", appName);
		model.addAttribute("single_genre", genre); // nome del genere selezionato 
		model.addAttribute("genres", allGenres);
		model.addAttribute("allGenres", allGenres); // per la navbar

		// Mini carrello
		if (authentication != null) {
			String principal_name = authentication.getName();
			User user = userService.findUserByUsername(principal_name);
			List<ShoppingCart> user_cart = new ArrayList<ShoppingCart>(user.getShoppingCart());
			model.addAttribute("user_cart", user_cart);
			model.addAttribute("cartTotalPrice", user.getFormattedCartSubtotalPrice());
			model.addAttribute("cartTotalItems", user.getCartTotalItems());
		}

		return "grid_book";

	}

	// Mostra tutti i libri in sconto
	@RequestMapping(value = "/sales", method = RequestMethod.GET)
	public String ShowBookonSale(@RequestParam(required = false) Integer page, Model model,
			Authentication authentication) {
		List<Book> bookSale = this.bookService.findBookOnSale(); // estrae tutti i libri in sconto
		List<Genre> allGenres = this.bookService.getAllGenres();

		// Paginazione
		PagedListHolder<Book> pagedListHolder = new PagedListHolder<>(bookSale);
		pagedListHolder.setPageSize(8);

		if (page == null || page < 1 || page > pagedListHolder.getPageCount())
			page = 1;

		pagedListHolder.setPage(page - 1);

		model.addAttribute("books", pagedListHolder.getPageList());
		model.addAttribute("maxPages", pagedListHolder.getPageCount());
		model.addAttribute("page", page);

		model.addAttribute("appName", appName);
		model.addAttribute("allGenres", allGenres); // per la navbar

		// Mini carrello
		if (authentication != null) {
			String principal_name = authentication.getName();
			User user = userService.findUserByUsername(principal_name);
			List<ShoppingCart> user_cart = new ArrayList<ShoppingCart>(user.getShoppingCart());
			model.addAttribute("user_cart", user_cart);
			model.addAttribute("cartTotalPrice", user.getFormattedCartSubtotalPrice());
			model.addAttribute("cartTotalItems", user.getCartTotalItems());
		}

		return "grid_book_discounts";

	}

	// Pagina per il singolo libro
	@GetMapping(value = "/show_book/{id}")
	public String ShowDetailsBook(@PathVariable("id") String id, Model model, Authentication authentication) {
		long l_id = Long.parseLong(id); // cast from string to long
		this.bookService.add_click(l_id);

		Book b = this.bookService.findById(l_id);
		Set<Author> authorSet = b.getAuthors();

		Set<Book> booksimilargenre = this.bookService.getBooksimilargenre(b); // restituisce la lista di tutti i libri dello stesso genere del ibro mostrato
		Set<Book> booksimilarauthor = this.bookService.getBooksimilarAuthor(b);  // restituisce la lista di tutti i libri dello stesso autore del ibro mostrato
		
		// ccontrollo se le due liste sono vuote (usato nella vista) 
		Boolean checklistaut = booksimilarauthor.isEmpty();
		Boolean checklistgenre = booksimilargenre.isEmpty();

		List<Author> authorsList = this.authorService.getAuthorsListFromSet(authorSet);
		List<Genre> allGenres = this.bookService.getAllGenres();

		model.addAttribute("authorsList", authorsList); // lista degli autori del libro
		model.addAttribute("appName", appName);
		model.addAttribute("book", b);
		model.addAttribute("checklistaut", checklistaut);
		model.addAttribute("checklistgenre", checklistgenre);
		model.addAttribute("booksimilgenre", booksimilargenre); // libri consigliati degli stessi generi del libro mostarto
		model.addAttribute("booksimilaut", booksimilarauthor); // libri consigliati degli stessi autori del libro mostarto

		model.addAttribute("allGenres", allGenres); // per la navbar

		// Mini carrello a comparsa
		if (authentication != null) {
			String principal_name = authentication.getName();
			User user = userService.findUserByUsername(principal_name);
			List<ShoppingCart> user_cart = new ArrayList<ShoppingCart>(user.getShoppingCart());
			model.addAttribute("user_cart", user_cart);
			model.addAttribute("cartTotalPrice", user.getFormattedCartSubtotalPrice());
			model.addAttribute("cartTotalItems", user.getCartTotalItems());
		}

		return "single_book";

	}

	// Pagina del singolo autore
	@GetMapping(value = "/show_author/{id}")
	public String ShowDetailsAuthor(@PathVariable("id") String id, Model model, Authentication authentication) {
		long long_id = Long.parseLong(id);
		Author author = this.authorService.findById(long_id);

		List<Genre> allGenres = this.bookService.getAllGenres();

		List<Book> authorBooks = this.bookService.findBooksAuthor(author);// restituisce la lista di tutti i libri di quell'autore
		Boolean checkAuthorBooks = authorBooks.isEmpty();

		model.addAttribute("appName", appName);
		model.addAttribute("author", author);
		model.addAttribute("authorBooks", authorBooks);
		model.addAttribute("checkAuthorBooks", checkAuthorBooks);
		model.addAttribute("allGenres", allGenres); // per la navbar

		// Mini carrello
		if (authentication != null) {
			String principal_name = authentication.getName();
			User user = userService.findUserByUsername(principal_name);
			List<ShoppingCart> user_cart = new ArrayList<ShoppingCart>(user.getShoppingCart());
			model.addAttribute("user_cart", user_cart);
			model.addAttribute("cartTotalPrice", user.getFormattedCartSubtotalPrice());
			model.addAttribute("cartTotalItems", user.getCartTotalItems());
		}

		return "single_author";
	}

	// Pagina "chi siamo"
	@GetMapping(value = "/about_us")
	public String aboutUs(Model model, Authentication authentication) {

		List<Genre> allGenres = bookService.getAllGenres();
		model.addAttribute("allGenres", allGenres); // Da qui sono presi anche i generi della navbar

		// Mini carrello
		if (authentication != null) {
			String principal_name = authentication.getName();
			User user = userService.findUserByUsername(principal_name);
			List<ShoppingCart> user_cart = new ArrayList<ShoppingCart>(user.getShoppingCart());
			model.addAttribute("user_cart", user_cart);
			model.addAttribute("cartTotalPrice", user.getFormattedCartSubtotalPrice());
			model.addAttribute("cartTotalItems", user.getCartTotalItems());
		}
		return "about_us";
	}

	// Pagina delle "FAQ"
	@GetMapping(value = "/faq")
	public String faq(Model model, Authentication authentication) {

		List<Genre> allGenres = bookService.getAllGenres();
		model.addAttribute("allGenres", allGenres); // Da qui sono presi anche i generi della navbar

		// Mini carrello
		if (authentication != null) {
			String principal_name = authentication.getName();
			User user = userService.findUserByUsername(principal_name);
			List<ShoppingCart> user_cart = new ArrayList<ShoppingCart>(user.getShoppingCart());
			model.addAttribute("user_cart", user_cart);
			model.addAttribute("cartTotalPrice", user.getFormattedCartSubtotalPrice());
			model.addAttribute("cartTotalItems", user.getCartTotalItems());
		}
		return "faq";
	}

	// Pagina "Termini e privacy"
	@GetMapping(value = "/privacy&terms")
	public String privacy_sell_terms(Model model, Authentication authentication) {

		List<Genre> allGenres = bookService.getAllGenres();
		model.addAttribute("allGenres", allGenres); // Da qui sono presi anche i generi della navbar

		// Mini carrello
		if (authentication != null) {
			String principal_name = authentication.getName();
			User user = userService.findUserByUsername(principal_name);
			List<ShoppingCart> user_cart = new ArrayList<ShoppingCart>(user.getShoppingCart());
			model.addAttribute("user_cart", user_cart);
			model.addAttribute("cartTotalPrice", user.getFormattedCartSubtotalPrice());
			model.addAttribute("cartTotalItems", user.getCartTotalItems());
		}
		return "privacy_terms";
	}

	// Pagina "aiuto"
	@GetMapping(value = "/help")
	public String help(Model model, Authentication authentication) {

		List<Genre> allGenres = bookService.getAllGenres();
		model.addAttribute("allGenres", allGenres); // Da qui sono presi anche i generi della navbar

		// Mini carrello
		if (authentication != null) {
			String principal_name = authentication.getName();
			User user = userService.findUserByUsername(principal_name);
			List<ShoppingCart> user_cart = new ArrayList<ShoppingCart>(user.getShoppingCart());
			model.addAttribute("user_cart", user_cart);
			model.addAttribute("cartTotalPrice", user.getFormattedCartSubtotalPrice());
			model.addAttribute("cartTotalItems", user.getCartTotalItems());
		}
		return "help";
	}

	// Popola il db con elementi di test iniziali
	@GetMapping(value = "/populatedb")
	public String populatedb(Locale locale, Model model) {

		// Crea utente admin
		userService.findOrCreateRole("ADMIN");
		User admin = userService.findUserByUsername("admin");
		if (admin == null) {
			userService.create("admin", "admin@email.com", "admin", "admin", "admin", null, "Via Brecce Bianche",
					"Ancona", 60135, "Italia", Arrays.asList("ADMIN"));
		}

		// Crea utenti venditori
		userService.findOrCreateRole("SELLER");
		User seller1 = userService.findUserByUsername("libreria");
		if (seller1 == null) {
			seller1 = userService.create("libreria", "seller@email.com", "1234", "Libreria", "Ragni", null,
					"Via Redipuglia", "Ancona", 60122, "Italia", Arrays.asList("SELLER"));
		}
		User seller2 = userService.findUserByUsername("Mondadori");
		if (seller2 == null) {
			seller2 = userService.create("Mondadori", "mondadori@email.com", "1234", "Ernesto", "Mauri", null,
					"Via tiburtina", "Milano", 20010, "Italia", Arrays.asList("SELLER"));

		}

		// Crea generico utente acquirente
		userService.findOrCreateRole("USER");
		User buyer1 = userService.findUserByUsername("user1");
		if (buyer1 == null) {
			buyer1 = userService.create("user1", "user1@email.com", "5678", "Roberto", "Rossi", null, "Via Ugo Bassi",
					"Ancona", 60121, "Italia", Arrays.asList("USER"));
		}
		User buyer2 = userService.findUserByUsername("user2");
		if (buyer2 == null) {
			buyer2 = userService.create("user2", "user2@email.com", "0000", "Franceso", "Pozzo", null, "Via flaminia",
					"Ancona", 60121, "Italia", Arrays.asList("USER"));

		}

		// Crea date di nascita per gli autori
		DateFormat date = new SimpleDateFormat("dd-MM-yyyy");
		Date date01 = null;
		Date date02 = null;
		Date date03 = null;
		Date date04 = null;
		Date date05 = null;
		Date date06 = null;
		Date date07 = null;
		Date date08 = null;
		Date date09 = null;
		Date date10 = null;
		try {
			date01 = date.parse("01-01-1256");
			date02 = date.parse("03-01-1892");
			date03 = date.parse("07-03-1785");
			date04 = date.parse("15-03-1937");
			date05 = date.parse("23-10-1942");
			date06 = date.parse("08-01-1942");
			date07 = date.parse("31-07-1919");
			date08 = date.parse("12-11-1929");
			date09 = date.parse("05-06-1964");
			date10 = date.parse("22-05-1859");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		java.sql.Date birth_date1 = new java.sql.Date(date01.getTime());
		java.sql.Date birth_date2 = new java.sql.Date(date02.getTime());
		java.sql.Date birth_date3 = new java.sql.Date(date03.getTime());
		java.sql.Date birth_date4 = new java.sql.Date(date04.getTime());
		java.sql.Date birth_date5 = new java.sql.Date(date05.getTime());
		java.sql.Date birth_date6 = new java.sql.Date(date06.getTime());
		java.sql.Date birth_date7 = new java.sql.Date(date07.getTime());
		java.sql.Date birth_date8 = new java.sql.Date(date08.getTime());
		java.sql.Date birth_date9 = new java.sql.Date(date09.getTime());
		java.sql.Date birth_date10 = new java.sql.Date(date10.getTime());

		// Crea autori
		authorService.create("Dante", "Alighieri", birth_date1, "Italia", "Dante Alighieri nasce a ...",
				"Alighieri.jpg");
		authorService.create("J.J.R.", "Tolkien", birth_date2, "Inghilterra", "J.J.R. Tolkie nasce a ...",
				"Tolkien.jpg");
		authorService.create("Alessandro", "Manzoni", birth_date3, "Italia", "Alessandro Manzoni nasce a ...",
				"Manzoni.jpg");
		authorService.create("H.P.", "Lovecraft", birth_date4, "Rhode Island,U.S.A", "H.P. Lovecraft nasce a ...",
				"Lovecraft.jpg");
		authorService.create("Michael", "Crichton", birth_date5, "Chicago,U.S.A", "Michael Crichton nasce a ...",
				"Crichton.jpg");
		authorService.create("Stephen W.", "Hawking", birth_date6, "Inghilterra", "Stephen W. Hawking nasce a ...",
				"Hawking.jpg");
		authorService.create("Primo", "Levi", birth_date7, "Italia", "Pirmo Levi nasce a ...", "Levi.jpg");
		authorService.create("Michael", "Ende", birth_date8, "Germania", "Micheal Ende nasce a ...", "Ende.jpg");
		authorService.create("Rick", "Riordan", birth_date9, "Texas", "Rick Riordan nasce a ...", "Riordan.jpg");
		authorService.create("Conan", "Doyle", birth_date10, "Scozia", "Conan Doyle nasce a ...", "Doyle.jpg");

		// Crea date di pubblicazione dei libri da inserire
		Date date1 = null;
		Date date2 = null;
		Date date3 = null;
		Date date4 = null;
		Date date5 = null;
		Date date6 = null;
		Date datec1 = null;
		Date datec2 = null;
		try {
			date1 = date.parse("21-01-2005");
			date2 = date.parse("28-03-2008");
			date3 = date.parse("21-11-2009");
			date4 = date.parse("01-01-2012");
			date5 = date.parse("15-08-2014");
			date6 = date.parse("21-01-2018");
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
		java.sql.Date dateC1 = new java.sql.Date(datec1.getTime());
		java.sql.Date dateC2 = new java.sql.Date(datec2.getTime());

		// Crea libri
		bookService.deleteAll();
		bookService.create("Dante", "Alighieri", "838832989113223", "La Divina Commedia", publish_date1, publish_date1,
				3, 34, seller1, 300, "Nel mezzo del cammin...", "6.jpg", Arrays.asList("Poema"), 0.15);
		bookService.create("J.J.R.", "Tolkien", "746382492401", "Il Signore degli Anelli - Le due torri", publish_date2,
				publish_date2, 4, 40.99, seller2, 450, "Sauron ï¿½ tornato a Mordor...", "7.jpg",
				Arrays.asList("Fantasy"), 0);
		bookService.create("Alessandro", "Manzoni", "8235234631481401", "I promessi sposi", publish_date3,
				publish_date3, 10, 25.99, seller1, 370, "Renzo e Lucia ...", "8.jpg", Arrays.asList("Romanzo"), 0);
		bookService.create("H.P.", "Lovecraft", "83883267788997", "Il caso di Charles Dexter Ward", publish_date4,
				publish_date4, 3, 39, seller1, 300, "Charles Dexter Ward era il discendente di...", "11.jpg",
				Arrays.asList("Horror", "Fantasy"), 0);
		bookService.create("J.J.R.", "Tolkien", "344567880909", "Il Signore degli Anelli - La compagnia dell'anello",
				publish_date5, publish_date5, 7, 42.50, seller1, 450, "Frodo Baggins stava tornando a casa...", "9.jpg",
				Arrays.asList("Fantasy"), 0);
		bookService.create("J.J.R.", "Tolkien", "9283755352729", "Il Signore degli Anelli - Il ritorno del re",
				publish_date6, publish_date6, 10, 37.99, seller1, 390, "Aragorn, dopo un discorso da Oscar ...",
				"10.jpg", Arrays.asList("Fantasy"), 0.10);
		bookService.create("Michael", "Crichton", "198934345798876", "Jurassic Park", publish_date3, publish_date3, 12,
				18.30, seller2, 267, "Alan Grant ï¿½ un paleontologo che ...", "12.jpg",
				Arrays.asList("Fantascienza", "Horror"), 0);
		bookService.create("Stephen W.", "Hawking", "5667899121887",
				"Dal Big Bang ai buchi neri - Breve storia del tempo", publish_date4, publish_date4, 18, 23, seller2,
				280, "Il Big Bang ï¿½ stato un evento ...", "13.jpg", Arrays.asList("Divulgativo"), 0.20);
		bookService.create("Primo", "Levi", "23456789045678", "Se questo ï¿½ un uomo", publish_date1, publish_date1, 15,
				28.99, seller2, 220, "Boh non me ricordo ...", "14.jpg", Arrays.asList("Biografia"), 0);
		bookService.create("Michael", "Ende", "345678899876756", "La storia infinita", publish_date2, publish_date2, 20,
				15.00, seller2, 440, "Questa scritta stava sulla porta ...", "15.jpg",
				Arrays.asList("Avventura", "Fantasy"), 0);
		bookService.create("Rick", "Riordan", "234559878653546",
				"Percy Jackson e gli dei dell'Olimpo - Il ladro di fulmini", publish_date4, publish_date4, 15, 17,
				seller2, 500, "Percy era il figlio Poseidone...", "16.jpg", Arrays.asList("Avventura", "Fantasy"), 0);
		bookService.create("Conan", "Doyle", "111111111111", "Le avventure di Sherlock Holmes", publish_date6,
				publish_date6, 5, 20.99, seller1, 256, "Elementare Watson! ...", "17.jpg",
				Arrays.asList("Giallo", "Romanzo"), 0.25);

		// Crea coupon
		try {
			couponService.create("INVERNO2021", 10, dateC1);
			couponService.create("EXTRASCONTO15", 15, dateC2);
		} catch (ConstraintViolationException e) {
			e.printStackTrace();
		}

		return "redirect:/advanced_search";
	}
}
