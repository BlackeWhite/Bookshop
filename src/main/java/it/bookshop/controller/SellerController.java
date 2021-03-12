package it.bookshop.controller;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import it.bookshop.model.entity.Author;
import it.bookshop.model.entity.User;
import it.bookshop.controller.UserController.CartRequestBody;
import it.bookshop.model.Object_form.BookInfoResponse;
import it.bookshop.model.Object_form.Bookform;
import it.bookshop.model.entity.Book;
import it.bookshop.model.entity.BookOrder;
import it.bookshop.model.entity.Genre;
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
		model.addAttribute("i", i); // utilizzata come contatore nella vista per i generi
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
		// analisi totale di tutti i libri venduti
		Iterator<Book> boit = lbooksold.iterator();
		int copies = 0;
		while (boit.hasNext()) {
			copies += boit.next().getSoldCopies();  // calcolo di tutte le copie vendute per ogni libro
		}
		double totearn = Math.round(this.orderService.TotalEarn(lbooksold) * 100.0) / 100.0; // incasso totale di tutti i libri venduti 
		
		model.addAttribute("listbook", lbooksold);
		model.addAttribute("totearn", totearn);
		model.addAttribute("totcopies", copies);
		generalOperations(model);
		return "analysis_book";
	}

	@PostMapping(value = "/change_book") // informazioni su incasso e copie vednute di un partciolare libro(ajax)
	@ResponseBody
	public BookInfoResponse change_book(@RequestBody CartRequestBody reqBody, Authentication authentication) {

		BookInfoResponse bresp = new BookInfoResponse();
		bresp.setBookID(reqBody.getBookID());

		Book b = this.bookService.findById(reqBody.getBookID());
		bresp.setTitle(b.getTitle());
		bresp.setSoldcopies(b.getSoldCopies()); // copie vendute per quel libro

		List<BookOrder> listsoldbook = this.orderService.findbyId(reqBody.getBookID());
		Iterator<BookOrder> iterbook = listsoldbook.iterator();

		// calcolo incasso totale
		double sum = this.orderService.TotalEarnforBook(reqBody.getBookID());

		double sumapprox = Math.round(sum * 100.0) / 100.0;
		bresp.setTotearn(sumapprox);

		return bresp;

	}

	@PostMapping(value = "/range_data")  // informazioni su incasso e copie vednute in un intervallo di tempo(ajax)
	@ResponseBody
	public BookInfoResponse range_data(@RequestBody CartRequestBody reqBody, Authentication authentication) {

		BookInfoResponse bresp = new BookInfoResponse();
		String data_da = reqBody.getArg2();
		String data_a = reqBody.getArg3();

		bresp = this.orderService.findbyDate(data_da, data_a); // calcolo dell'incasso totale in quel intervallo di
																// tempo e delle copie vendute
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
			String message = "\"" + bookCreated.getTitle() + "\" aggiunto correttamente ";
			redirectAttributes.addFlashAttribute("message", message);
			redirectAttributes.addFlashAttribute("msgColor", "#F7941D");
			return "redirect:/seller/";

		}
	}

	@GetMapping(value = "/edit_book/{book_id}")
	public String editBookPage(@PathVariable("book_id") Long book_id, Model model, Locale locale,Authentication authentication) {
		Book b_temp = this.bookService.findById(book_id);
		String principal_name = authentication.getName();
		User seller = userService.findUserByUsername(principal_name);
		
		try {
			if (seller.getUserID() == b_temp.getSeller().getUserID()) { // verifico che il ibro che si sta modificando sia di propriet� di quel venditore
			
		Bookform bf = new Bookform();

		bf.populate(b_temp);

		int i = 0;
		List<String> gen = new ArrayList<String>();

		List<Genre> allGenres = this.bookService.getAllGenres();
		Iterator<Genre> iteGen = allGenres.iterator();

		while (iteGen.hasNext()) {
			gen.add(iteGen.next().getName());
		}
		
		Book book_prova = bookService.findById(book_id);
		List<Author> authors = new ArrayList<Author>(book_prova.getAuthors());
		Iterator<Author> autIter = authors.iterator();
		List<String> authorsName = new ArrayList<>();
		List<String> authorsSurname = new ArrayList<>();
		String name;
		String surname;
		int n = authors.size(); 
		for(int j=0; j<n; j++) {
			name = authors.get(j).getName();
			try {
				surname = authors.get(j).getSurname();
			} catch (Exception e) {
				surname = "#SURNAME_PLACEHOLDER";
			}
			authorsName.add(name);
			authorsSurname.add(surname);
			
		}/*
		while(autIter.hasNext()) {
			name = autIter.next().getName();
			try {
				surname = autIter.next().getSurname();
			} catch (Exception e) {
				surname = "#SURNAME_PLACEHOLDER";
			}
			authorsName.add(name);
			authorsSurname.add(surname);
		}*/
		
		int numAuthor = 0;
		model.addAttribute("allGenres", allGenres);
		model.addAttribute("genre", gen);
		model.addAttribute("authorsName", authorsName);
		model.addAttribute("authors", authors);
		model.addAttribute("authorsSurname", authorsSurname);
		model.addAttribute("prova", authorsName.get(0));
		model.addAttribute("numAuthor", numAuthor);
		model.addAttribute("i", i); // utilizzata come contatore nella vista
		model.addAttribute("bookToUpdate", bf);
		generalOperations(model);
		return "edit_book";
			}
			else { // se non � cos� non gli permetto la modifica e lo reindirizzo alla sua home
			return "redirect:/seller/";
			}
		} catch(Exception e) {
			return "redirect:/seller/";
		}
	}

	@PostMapping(value = "/save_changes/{book_id}")
	public String saveChangesBook(@ModelAttribute("bookToUpdate") @RequestBody @Valid Bookform bookChanged,
			@PathVariable("book_id") Long book_id, Model model, final RedirectAttributes redirectAttributes,
			Authentication authentication) {

		Bookform bf = new Bookform();
		String principal_name = authentication.getName();
		User seller = userService.findUserByUsername(principal_name);
		Book bookNotUpdated = bookService.findById(book_id);
		Set<Author> authorsList = getListAuthors(bookChanged);
		Set<Genre> genreList = getListGenres(bookChanged);
		Book bookToUpdate = bf.bookformToBook(bookChanged, seller, authorsList, genreList, book_id, bookNotUpdated);
		try{
			bookService.update(bookToUpdate);
			redirectAttributes.addFlashAttribute("message", "Dati modificati correttamente!");
		} catch(Exception e) {
			redirectAttributes.addFlashAttribute("message", "Mi dispiace, qualcosa � andato storto!");
		}
		
		redirectAttributes.addFlashAttribute("msgColor", "#F7941D");
		return "redirect:/seller/";
	}
	
	/*TODO->Aggiustare la vista della modifica, reindirizzamento pagine errore, modifica autore
	 * 
	 */
	@GetMapping(value = "/remove_book/{book_id}")
	public String removeBook(@PathVariable("book_id") Long book_id, Model model,Authentication authentication,
			final RedirectAttributes redirectAttributes) {
		
		Book removedBook = this.bookService.findById(book_id);
		String principal_name = authentication.getName();
		User seller = userService.findUserByUsername(principal_name);
		try {
			if (seller.getUserID() == removedBook.getSeller().getUserID()) { // verifico che il ibro che si sta eliminando sia di propriet� di quel venditore
	
		try {
			this.bookService.removeBook(book_id);
			String message = "\"" + removedBook.getTitle() + "\" rimosso correttamente";
			redirectAttributes.addFlashAttribute("message", message);
		} catch (Exception e) {
			String message = "Qualcosa � andato storto. Il libro \"" + removedBook.getTitle()
					+ "\" non � stato rimosso correttamente ";
			redirectAttributes.addFlashAttribute("message", message);
		}

		redirectAttributes.addFlashAttribute("msgColor", "#F7941D");
		return "redirect:/seller/";
		}
			else { // se non � cos� non gli permetto la cancellazione e lo reindirizzo alla sua home
				return "redirect:/seller/";
				}
			} catch(Exception e) {
					return "redirect:/seller/";
				}
	}

	// Adds attributes used in almost all requests
	private void generalOperations(Model model) {
		List<Genre> allGenres = this.bookService.getAllGenres();

		model.addAttribute("allGenres", allGenres);
		model.addAttribute("appName", appName);

	}
	
	private Set<Author> getListAuthors(Bookform book) {
		
		Set<Author> authorsList = new HashSet<Author>();
		
		List<String> authorsNameList = book.getAuthorsName();
		List<String> authorsSurnameList = book.getAuthorsSurname();

		int count = 0;
		Iterator<String> iterName = authorsNameList.iterator();
		while (iterName.hasNext()) {
			String name = iterName.next();
			String surname;
			try {
				surname = authorsSurnameList.get(count);
				if(surname.isEmpty()) surname = "#SURNAME_PLACEHOLDER";
			} catch(Exception e) {
				surname = "#SURNAME_PLACEHOLDER";
			}
			try {
				Author author = authorService.findByNameAndSurname(name, surname);
				if(author == null) {
					authorService.create(name, surname);
					author = authorService.findByNameAndSurname(name, surname);
				}
				authorsList.add(author);
			} catch (Exception e) {
				authorService.create(name, surname);
				Author author = authorService.findByNameAndSurname(name, surname);
				authorsList.add(author);
			}
			count++;
		}
		
		return authorsList;
		
	}
	
	private Set<Genre> getListGenres(Bookform book){
		List<String> genreBookFormList = book.getGenre();
		List<Genre> genreList = bookService.findGenresFromNamesArray(genreBookFormList);
		Set<Genre> genreSet = new HashSet<>(genreList);
		return genreSet;
	}

}
