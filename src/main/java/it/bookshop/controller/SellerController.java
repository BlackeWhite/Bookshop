package it.bookshop.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import it.bookshop.model.entity.Author;
import it.bookshop.model.entity.Role;
import it.bookshop.model.entity.ShoppingCart;
import it.bookshop.model.entity.User;
import it.bookshop.controller.UserController.CartRequestBody;
import it.bookshop.model.Object_form.BookInfoResponse;
import it.bookshop.model.Object_form.Bookform;
import it.bookshop.model.entity.Book;
import it.bookshop.model.entity.BookOrder;
import it.bookshop.model.entity.Genre;
import it.bookshop.model.entity.Order;
import it.bookshop.services.BookService;
import it.bookshop.services.OrderService;
import it.bookshop.services.UserService;
import it.bookshop.services.AuthorService;

import org.springframework.security.core.Authentication;

@Controller
@RequestMapping("/seller")
public class SellerController {

	@Autowired
	String appName;

	@Autowired
	private UserService userService;

	@Autowired
	private BookService bookService;

	@Autowired
	private OrderService orderService;

	@Autowired
	private AuthorService authorService;

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(@RequestParam(required = false) Integer page, Locale locale, Model model,
			Authentication authentication) {
		String principal_name = authentication.getName();
		User seller = userService.findUserByUsername(principal_name);

		// PAGINAZIONE
		List<Book> sellerBooks = this.bookService.findAllBookSoldOfSeller(seller);
		PagedListHolder<Book> pagedListHolder = new PagedListHolder<>(sellerBooks);
		pagedListHolder.setPageSize(5);

		if (page == null || page < 1 || page > pagedListHolder.getPageCount())
			page = 1;

		pagedListHolder.setPage(page - 1);

		model.addAttribute("sellerBooks", pagedListHolder.getPageList());
		model.addAttribute("maxPages", pagedListHolder.getPageCount());
		model.addAttribute("page", page);

		List<Genre> allGenres = bookService.getAllGenres();
		model.addAttribute("allGenres", allGenres);
		return "home_seller";
	}

	// procedura (get) per l'aggiunta di un libro
	@GetMapping(value = "/addition_book")
	public String additionBooK(@RequestParam(value = "error", required = false) Locale locale, Model model) {

		String errorMessage = null;
		int i = 0;
		String mode = "add"; // paramtero utilizzato nella vista per adattare la form in base a cosa si sta
								// facendo
								// facendo
		Bookform bf = new Bookform();

		List<String> gen = new ArrayList<String>();
		List<String> authors_name = new ArrayList<String>();
		List<String> authors_surname = new ArrayList<String>();
		List<Genre> allGenres = this.bookService.getAllGenres();
		Iterator<Genre> iteGen = allGenres.iterator();

		while (iteGen.hasNext()) {
			gen.add(iteGen.next().getName());
		}

		model.addAttribute("errorMessage", errorMessage);
		model.addAttribute("newBook", bf);
		model.addAttribute("genre", gen);
		model.addAttribute("authorsName", authors_name);
		model.addAttribute("authorsSurname", authors_surname);
		model.addAttribute("mode", mode);
		model.addAttribute("i", i); // utilizzata come contatore nella vista
		model.addAttribute("allGenres", allGenres);

		generalOperations(model);
		return "add_book";
	}

	// analisi di un libro
	@GetMapping(value = "/analysis_book")
	public String analysisBook(Model model, Authentication authentication) {
		String principal_name = authentication.getName();
		User seller = userService.findUserByUsername(principal_name);

		List<Book> lbooksold = bookService.findAllBookSoldOfSeller(seller);
		Iterator<Book> boit = lbooksold.iterator();
		int copies = 0;
		while (boit.hasNext()) {
			copies += boit.next().getSoldCopies();
		}
		double totearn = Math.round(this.orderService.TotalEarn(lbooksold) * 100.0) / 100.0;
		;
		model.addAttribute("listbook", lbooksold);
		model.addAttribute("totearn", totearn);
		model.addAttribute("totcopies", copies);
		generalOperations(model);
		return "analysis_book";
	}

	@PostMapping(value = "/change_book")
	@ResponseBody
	public BookInfoResponse change_book(@RequestBody CartRequestBody reqBody, Authentication authentication) {

		BookInfoResponse bresp = new BookInfoResponse();
		bresp.setBookID(reqBody.getBookID());

		Book b = this.bookService.findById(reqBody.getBookID());
		bresp.setTitle(b.getTitle());
		bresp.setSoldcopies(b.getSoldCopies());

		List<BookOrder> listsoldbook = this.orderService.findbyId(reqBody.getBookID());
		Iterator<BookOrder> iterbook = listsoldbook.iterator();

		// calcolo incasso totale
		double sum = this.orderService.TotalEarnforBook(reqBody.getBookID());

		double sumapprox = Math.round(sum * 100.0) / 100.0;
		bresp.setTotearn(sumapprox);

		return bresp;

	}

	@PostMapping(value = "/range_data")
	@ResponseBody
	public BookInfoResponse range_data(@RequestBody CartRequestBody reqBody, Authentication authentication) {

		BookInfoResponse bresp = new BookInfoResponse();
		String data_da = reqBody.getArg2();
		String data_a = reqBody.getArg3();

		bresp = this.orderService.findbyDate(data_da, data_a);

		return bresp;

	}

	// procedura (post) per l'aggiunta di un libro
	@RequestMapping(value = "/add_book", method = RequestMethod.POST, consumes = { "multipart/form-data" })
	public String addBook(@ModelAttribute("newBook") @RequestBody @Valid Bookform book, BindingResult br, Model model,
			HttpSession session, Authentication authentication, final RedirectAttributes redirectAttributes) {

		String principal_name = authentication.getName();
		User seller = userService.findUserByUsername(principal_name);

		if (br.hasErrors()) {
			generalOperations(model);
			List<String> gen = new ArrayList<String>();
			List<Genre> allGenres = this.bookService.getAllGenres();
			Iterator<Genre> iteGen = allGenres.iterator();
			while (iteGen.hasNext()) {
				gen.add(iteGen.next().getName());
			}
			model.addAttribute("genre", gen);
			model.addAttribute("newBook", book);
			return "addittion_book";
		} else {
			try {
				// memorizza il file appena caricato dalla form (stackoverflow)
				String path = session.getServletContext().getRealPath("/");
				byte barr[] = book.getCover().getBytes();
				BufferedOutputStream bout = new BufferedOutputStream(new FileOutputStream(
						path + "resources/img/cover_book/" + book.getCover().getOriginalFilename()));
				bout.write(barr);
				bout.flush();
				bout.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			Book bookCreated = this.bookService.create(book, seller);
			// Dati per la vista del libro appena creato
			Set<Author> authorSet = bookCreated.getAuthors();
			//List<Author> authorsList = this.authorService.getAuthorsListFromSet(authorSet);
			String message = "\"" + bookCreated.getTitle() + "\" aggiunto correttamente ";
			//model.addAttribute("message", message);
			//model.addAttribute("book", bookCreated);
			//model.addAttribute("authorsList", authorsList);
			//return "single_book";
			
			redirectAttributes.addFlashAttribute("message", message);
			redirectAttributes.addFlashAttribute("msgColor", "#F7941D");
			return "redirect:/seller/";
			
		}
	}
/*
	// mostra la form per la modifica di un libro
	@GetMapping(value = "/editBook/{book_id}")
	public String editBook(@PathVariable("book_id") Long book_id, Model model, Authentication authentication) {
		// TODO -> PASSARE SEMPRE AL MODEL I GENERI
		// AGGIUNGERE "IVA ESCLUSA" IN FASE DI INSERIMENTO DEL PRODOTTO
		// TERMINARE LA PARTE DI MODIFICA
		Book b_temp = this.bookService.findById(book_id);
		String principal_name = authentication.getName();
		User seller = userService.findUserByUsername(principal_name);
		if (seller.getUserID() == b_temp.getSeller().getUserID()) { // controllo se è un suo libro, altrimenti non può modificarlo
				
		Bookform bf = new Bookform();

		bf.populate(b_temp);

		int i = 0;
		List<String> gen = new ArrayList<String>();
		List<String> authors = new ArrayList<String>();

		List<Genre> allGenres = this.bookService.getAllGenres();
		Iterator<Genre> iteGen = allGenres.iterator();

		while (iteGen.hasNext()) {
			gen.add(iteGen.next().getName());
		}

		List<Author> allAuthors = this.authorService.findAll();
		Iterator<Author> iterAuthors = allAuthors.iterator();
		while (iterAuthors.hasNext()) {
			authors.add(iterAuthors.next().getFullName());
		}

		model.addAttribute("allGenres", allGenres);
		model.addAttribute("genre", gen);
		model.addAttribute("authors", authors);
		model.addAttribute("i", i); // utilizzata come contatore nella vista
		model.addAttribute("newBook", bf);
		generalOperations(model);
		return "edit_book";
		}
		else {
			return "redirect:/seller/";
		}

	}
	*/
	

	@GetMapping(value = "/edit_book/{book_id}")
	public String editBookPage(@PathVariable("book_id") Long book_id, Model model) {
		Book b_temp = this.bookService.findById(book_id);
		Bookform bf = new Bookform();

		bf.populate(b_temp);

		int i = 0;
		List<String> gen = new ArrayList<String>();
		List<String> authors = new ArrayList<String>();

		List<Genre> allGenres = this.bookService.getAllGenres();
		Iterator<Genre> iteGen = allGenres.iterator();

		while (iteGen.hasNext()) {
			gen.add(iteGen.next().getName());
		}

		List<Author> allAuthors = this.authorService.findAll();
		Iterator<Author> iterAuthors = allAuthors.iterator();
		while (iterAuthors.hasNext()) {
			authors.add(iterAuthors.next().getFullName());
		}

		model.addAttribute("allGenres", allGenres);
		model.addAttribute("genre", gen);
		model.addAttribute("authors", authors);
		model.addAttribute("i", i); // utilizzata come contatore nella vista
		model.addAttribute("bookToUpdate", bf);
		generalOperations(model);
		
		return "edit_book/{book_id}";
	}
	
	@PostMapping(value="/save_changes/{book_id}")
	public String saveChangesBook(@ModelAttribute("bookToUpdate") @RequestBody @Valid Bookform bookChanged,@PathVariable("book_id") Long book_id, Model model, final RedirectAttributes redirectAttributes, Authentication authentication) {
		//model.addAttribute("bookToUpdate",book);
		//bookService.update(book);
		String principal_name = authentication.getName();
		User seller = userService.findUserByUsername(principal_name);
		//Book bookToUpdate = bookService.findById(bookChanged, seller);
		
		redirectAttributes.addFlashAttribute("message2", "Dati modificati correttamente!");
		redirectAttributes.addFlashAttribute("msgColor", "#F7941D");
		return "redirect:/seller/edit_book/{book_id}";
	}
	@GetMapping(value = "/remove_book/{book_id}")
	public String removeBook(@PathVariable("book_id") Long book_id, Model model, final RedirectAttributes redirectAttributes) {
		Book removedBook = this.bookService.findById(book_id);
		try {
			this.bookService.removeBook(book_id);
			String message = "\"" + removedBook.getTitle() + "\" rimosso correttamente";
			redirectAttributes.addFlashAttribute("message", message);
		} catch (Exception e) {
			String message = "Qualcosa è andato storto. Il libro \"" + removedBook.getTitle()
					+ "\" non è stato rimosso correttamente ";
			redirectAttributes.addFlashAttribute("message", message);
		}
		
		redirectAttributes.addFlashAttribute("msgColor", "#F7941D");
		return "redirect:/seller/";
	}

	// Adds attributes used in almost all requests
	private void generalOperations(Model model) {
		List<Genre> allGenres = this.bookService.getAllGenres();

		model.addAttribute("allGenres", allGenres);
		model.addAttribute("appName", appName);

	}

}
