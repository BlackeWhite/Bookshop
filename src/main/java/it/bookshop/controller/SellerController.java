package it.bookshop.controller;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import it.bookshop.model.entity.Author;
import it.bookshop.model.entity.User;
import it.bookshop.controller.UserController.CartRequestBody;
import it.bookshop.model.ObjectForm.BookInfoResponse;
import it.bookshop.model.ObjectForm.Bookform;
import it.bookshop.model.ObjectForm.Authorform;
import it.bookshop.model.entity.Book;
import it.bookshop.model.entity.BookOrder;
import it.bookshop.model.entity.Genre;
import it.bookshop.model.entity.PaymentCard;
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
	
	//Validatori per i vari form di inserimento dati dell'autore
	@Autowired
	@Qualifier("authorValidator")
	private Validator authorValidator;
	
	// Validatori per le form di inserimento e modifica dei libri
	@Autowired
	@Qualifier("bookValidator")
	private Validator bookValidator;
	
	//Imposta i validatori a seconda del modelAttribute richiesto
	@InitBinder
	private void initBinder(WebDataBinder binder) {
		if (binder.getTarget() != null && Authorform.class.equals(binder.getTarget().getClass())) {
			binder.setValidator(authorValidator);
		} else if (binder.getTarget() != null && Bookform.class.equals(binder.getTarget().getClass())) {
			binder.setValidator(bookValidator);
		}
	}
	
	
	/*----------------------Seller Home----------------------*/
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(@RequestParam(required = false) Integer page, Locale locale, Model model,
			Authentication authentication) {
		String principal_name = authentication.getName();
		User seller = userService.findUserByUsername(principal_name);

		// PAGINAZIONE
		List<Book> sellerBooks = this.bookService.findAllBookSoldOfSeller(seller); // restituisce la lista di tutti i libri di quel venditore
		PagedListHolder<Book> pagedListHolder = new PagedListHolder<>(sellerBooks);
		pagedListHolder.setPageSize(5);

		if (page == null || page < 1 || page > pagedListHolder.getPageCount())
			page = 1;

		pagedListHolder.setPage(page - 1);

		model.addAttribute("sellerBooks", pagedListHolder.getPageList());
		model.addAttribute("maxPages", pagedListHolder.getPageCount());
		model.addAttribute("page", page);

		generalOperations(model);
		return "home_seller";
	}
	/*----------------------End Seller Home----------------------*/
	

	/*----------------------Add New Book----------------------*/
	@GetMapping(value = "/addition_book")
	public String additionBooK(@RequestParam(value = "error", required = false) Locale locale, Model model) {
		/*
		 * Metodo GET per l'aggiunta di un libro
		 */
		String errorMessage = null;
		Bookform bf = new Bookform();

		List<String> authors_name = new ArrayList<String>();
		List<String> authors_surname = new ArrayList<String>();


		model.addAttribute("errorMessage", errorMessage);
		model.addAttribute("newBook", bf);
		model.addAttribute("authorsName", authors_name);
		model.addAttribute("authorsSurname", authors_surname);

		generalOperations(model);
		return "add_book";
	}
	
	@RequestMapping(value = "/add_book", method = RequestMethod.POST, consumes = { "multipart/form-data" })
	public String addBook(@ModelAttribute("newBook") @RequestBody @Valid Bookform book, BindingResult br, 
			Model model, HttpSession session, Authentication authentication, 
			final RedirectAttributes redirectAttributes) {
		/*
		 * Metodo POST per l'aggiunta di un libro
		 */
		String principal_name = authentication.getName();
		User seller = userService.findUserByUsername(principal_name);

		if (br.hasErrors()) { // se ci sono errori nella form, ritorna la pagina della form con i campi sbagliati
			generalOperations(model);
			book.placeholder();
			model.addAttribute("newBook", book);
			return "addition_book";
		} else { // se non ci sono errori nella form, procede al caricamento dei dati del libro nel db
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
			String message = "\"" + bookCreated.getTitle() + "\" aggiunto correttamente ";
			redirectAttributes.addFlashAttribute("message", message);
			redirectAttributes.addFlashAttribute("msgColor", "#F7941D");
			return "redirect:/seller/";

		}
	}
	/*----------------------End Add New Book----------------------*/
	
	
	/*----------------------Edit Book----------------------*/
	@GetMapping(value = "/edit_book/{book_id}")
	public String editBookPage(@PathVariable("book_id") Long book_id, Model model, 
			Locale locale, Authentication authentication) {
		/*
		 * Metodo GET per la modifica di un libro
		 */
		Book b_temp = this.bookService.findById(book_id);
		String principal_name = authentication.getName();
		User seller = userService.findUserByUsername(principal_name);
		Long id_seller = seller.getUserID();
		Long id_sellerB = b_temp.getSeller().getUserID();
		try {
			/*
			 * Verifica che il libro che si sta modificando sia di propriet� di quel venditore
			 */
			if (seller.getUserID() == b_temp.getSeller().getUserID()) {

				Bookform bf = new Bookform();
				bf.populate(b_temp);
		
				model.addAttribute("authorsName", bf.getAuthorsName());
				model.addAttribute("authorsSurname", bf.getAuthorsSurname());
				model.addAttribute("bookToUpdate", bf);
				
				generalOperations(model);
				return "edit_book";
			}
			else { 
				/*
				 * Se non � cos� non gli permette la modifica e lo reindirizzo alla sua home
				 */
			return "redirect:/seller/";
			}
		} catch(Exception e) {
			return "redirect:/seller/";
		}
	}

	@RequestMapping(value = "/save_changes/{book_id}", method = RequestMethod.POST, consumes = { "multipart/form-data" })
	public String saveChangesBook(@ModelAttribute("bookToUpdate") @RequestBody @Valid Bookform bookChanged,
			BindingResult br, @PathVariable("book_id") Long book_id, Model model, final RedirectAttributes redirectAttributes,
			Authentication authentication, HttpSession session) {
		/*
		 * Procedura POST per la modifica di un libro
		 */
		if (br.hasErrors()) { 
			/*
			 * Se ci sono errori nella form, ritorna la pagina della form con i campi errati
			 */
			bookChanged.placeholder();
			generalOperations(model);
			model.addAttribute("newBook", bookChanged);
			model.addAttribute("authorsName", bookChanged.getAuthorsName());
			model.addAttribute("authorsSurname", bookChanged.getAuthorsSurname());
			return "edit_book";
		} else {
			try {
				// Memorizza il file caricato dalla form 
				String path = session.getServletContext().getRealPath("/");
				byte barr[] = bookChanged.getCover().getBytes();
				BufferedOutputStream bout = new BufferedOutputStream(new FileOutputStream(
						path + "resources/img/cover_book/" + bookChanged.getCover().getOriginalFilename()));
				bout.write(barr);
				bout.flush();
				bout.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		
		// Procede con l'update del libro con i dati aggiornati
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
	}
	/*----------------------End Edit Book----------------------*/

	
	/*----------------------Remove Book----------------------*/
	@GetMapping(value = "/remove_book/{book_id}")
	public String removeBook(@PathVariable("book_id") Long book_id, Model model,Authentication authentication,
			final RedirectAttributes redirectAttributes) {
		/*
		 * Metodo per la rimozione di un libro.
		 */
		Book removedBook = this.bookService.findById(book_id);
		String principal_name = authentication.getName();
		User seller = userService.findUserByUsername(principal_name);
		try {
			if (seller.getUserID() == removedBook.getSeller().getUserID()) { 
				// Verifica che il libro che si sta eliminando sia di propriet� di quel venditore
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
			else { 
				/*
				 *  Se non cos� non fosse, non gli permetto la cancellazione e 
				 *  lo reindirizzo alla sua home
				 */
				return "redirect:/seller/";
				}
		} catch(Exception e) {
					return "redirect:/seller/";
				}
	}
	/*----------------------End Remove Book----------------------*/
	
	/*----------------------Restore Book----------------------*/
	@RequestMapping(value = "/removed_books_list", method = RequestMethod.GET)
	public String removedBooksList(@RequestParam(required = false) Integer page, Locale locale, 
			Model model, Authentication authentication) {
		/* 
		* Metodo per la lista dei libri rimossi di un determinato venditore
		*/
		String principal_name = authentication.getName();
		User seller = userService.findUserByUsername(principal_name);

		// PAGINAZIONE
		List<Book> bookRemovedPerSeller = bookService.findBookRemovedForSeller(seller);
		PagedListHolder<Book> pagedListHolder = new PagedListHolder<>(bookRemovedPerSeller);
		pagedListHolder.setPageSize(5);

		if (page == null || page < 1 || page > pagedListHolder.getPageCount())
			page = 1;

		pagedListHolder.setPage(page - 1);

		model.addAttribute("bookRemovedPerSeller", pagedListHolder.getPageList());
		model.addAttribute("maxPages", pagedListHolder.getPageCount());
		model.addAttribute("page", page);

		generalOperations(model);
		return "removed_books_list";
	}
	
	@GetMapping(value = "/restore_book/{book_id}")
	public String restoreBook(@PathVariable("book_id") Long book_id, Model model,Authentication authentication,
			final RedirectAttributes redirectAttributes) {
		/*
		 * Metodo per il ripristino di un libro.
		 */
		Book restoredBook = this.bookService.findByIdRemoved(book_id);
		String principal_name = authentication.getName();
		User seller = userService.findUserByUsername(principal_name);
		try {
			if (seller.getUserID() == restoredBook.getSeller().getUserID()) { 
				// Verifica che il libro che si sta ripristinando sia di propriet� di quel venditore
				try {
					this.bookService.saveRestored(book_id);
					String message = "\"" + restoredBook.getTitle() + "\" ripristinato correttamente";
					redirectAttributes.addFlashAttribute("message", message);
				} catch (Exception e) {
					String message = "Qualcosa � andato storto. Il libro \"" + restoredBook.getTitle()
							+ "\" non � stato ripristiano correttamente ";
					redirectAttributes.addFlashAttribute("message", message);
				}
		
				redirectAttributes.addFlashAttribute("msgColor", "#F7941D");
				return "redirect:/seller/";
			}
			else { 
				/*
				 *  Se non cos� non fosse, non gli permetto il ripristino e 
				 *  lo reindirizzo alla sua home
				 */
				return "redirect:/seller/";
				}
		} catch(Exception e) {
					return "redirect:/seller/";
				}
	}
	/*----------------------End Restore Book----------------------*/
	
	/*----------------------Analysis----------------------*/
	// Area analisi vendite dei libri del venditore 
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

		// calcolo incasso totale per quel libro
		double sum = this.orderService.TotalEarnforBook(reqBody.getBookID());

		double sumapprox = Math.round(sum * 100.0) / 100.0;
		bresp.setTotearn(sumapprox);

		return bresp;
	}
	
	@PostMapping(value = "/range_data")  // informazioni su incasso e copie vednute in un intervallo di tempo(ajax)
	@ResponseBody
	public BookInfoResponse range_data(@RequestBody CartRequestBody reqBody, Authentication authentication) {

		String principal_name = authentication.getName();
		User seller = userService.findUserByUsername(principal_name);
		BookInfoResponse bresp = new BookInfoResponse();
		String data_da = reqBody.getArg2();
		String data_a = reqBody.getArg3();

		bresp = this.orderService.findbyDate(data_da, data_a,seller); // Calcolo dell'incasso totale in quel intervallo di
															   // tempo e delle copie vendute
		return bresp;
	}
	/*----------------------End Analysis----------------------*/
	
	
	/*----------------------Seller Author List----------------------*/
	@RequestMapping(value = "/authors_seller", method = RequestMethod.GET)
	public String authorsPerSeller(@RequestParam(required = false) Integer page, Locale locale, 
			Model model, Authentication authentication) {
		/* 
		* Si occupa della pagina dedicata agli autori legati al venditore considerato
		*/
		String principal_name = authentication.getName();
		User seller = userService.findUserByUsername(principal_name);

		// PAGINAZIONE
		List<Author> authorsPerSeller = findAuthorPerSeller(seller);
		PagedListHolder<Author> pagedListHolder = new PagedListHolder<>(authorsPerSeller);
		pagedListHolder.setPageSize(5);

		if (page == null || page < 1 || page > pagedListHolder.getPageCount())
			page = 1;

		pagedListHolder.setPage(page - 1);

		model.addAttribute("authorsPerSeller", pagedListHolder.getPageList());
		model.addAttribute("maxPages", pagedListHolder.getPageCount());
		model.addAttribute("page", page);

		generalOperations(model);
		return "authors_seller";
	}
	/*----------------------End Seller Author List----------------------*/
	
	
	/*----------------------Edit Author----------------------*/
	@GetMapping(value = "/edit_author/{authorId}")
	public String editAuthor(@PathVariable("authorId") Long authorId, Model model, Locale locale, 
			final RedirectAttributes redirectAttributes, Authentication authentication) {
		/*
		 * Metodo GET per la modifica di un autore legato al venditore considerato
		 */
		String principal_name = authentication.getName();
		User seller = userService.findUserByUsername(principal_name);
		
		List<Author> sellerAuthors = findAuthorPerSeller(seller);
		Author authorToEdit = authorService.findById(authorId);
		
		Authorform authorForm = new Authorform();
		authorForm.populate(authorToEdit);
		
		boolean valid = false;
		for (Author author: sellerAuthors) {
			Long currId = author.getId();
			if(currId.equals(authorToEdit.getId())) {
				valid = true;
			}
		}
		if(valid) {
			/*
			 * Controlla se l'autore da modificare � effettivamente un autore 
			 * legato al venditore considerato. Se lo �, procede con il caricamento
			 * della form dedicata alla modifica.
			 */
			model.addAttribute("author", authorForm);
			generalOperations(model);
			return "edit_author";
			
		} else {
			/*
			 * Se l'azione non � consentita, rimanda alla home del venditore
			 * con un messaggio d'errore.
			 */
			String message = "Mi dispiace, qualcosa � andato storto.";
			redirectAttributes.addFlashAttribute("message", message);
			redirectAttributes.addFlashAttribute("msgColor", "#F7941D");
			return "redirect:/seller/authors_seller";
		}
		
	}

	@RequestMapping(value = "/author_edited/{authorId}", method = RequestMethod.POST, consumes = { "multipart/form-data" })
	public String saveAuthorChanges(@ModelAttribute("author") @RequestBody @Valid Authorform author, 
			BindingResult br, @PathVariable("authorId") Long authorId, Model model, Locale locale, Authentication authentication, 
			final RedirectAttributes redirectAttributes, HttpSession session) {
		/*
		 * Metodo POST per la modifica di un autore legato al venditore considerato
		 */
		if (br.hasErrors()) {
			model.addAttribute("author", author);
			generalOperations(model);
			return "edit_author";
			//return "redirect:/seller/edit_author/{authorId}";
		} else {
			try {
				// memorizza il file appena caricato dalla form (stackoverflow)
				String path = session.getServletContext().getRealPath("/");
				byte barr[] = author.getPhotoFile().getBytes();
				BufferedOutputStream bout = new BufferedOutputStream(new FileOutputStream(
						path + "resources/img/authors/" + author.getPhotoFile().getOriginalFilename()));
				bout.write(barr);
				bout.flush();
				bout.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		
		
		if(author.getSurname().isEmpty()) {
			/*
			 * Se il campo "surname" � vuoto, inserisce il placeholder
			 */
			author.setSurname("#SURNAME_PLACEHOLDER");
		}
		try{
			Author authorNotUpdated = authorService.findById(authorId);
			Author authorToUpdate = author.authorformToAuthor(author, authorId, authorNotUpdated);
			authorService.update(authorToUpdate);
			redirectAttributes.addFlashAttribute("message", "Dati modificati correttamente!");
		} catch(Exception e) {
			redirectAttributes.addFlashAttribute("message", "Mi dispiace, qualcosa � andato storto.");
		}

		redirectAttributes.addFlashAttribute("msgColor", "#F7941D");
		return "redirect:/seller/authors_seller";
		}
	}
	/*----------------------End Edit Author----------------------*/
	
	
	/*----------------------Utilities----------------------*/
	private void generalOperations(Model model) {
		/*
		 * Metodo per passare parametri utilizzati di frequente
		 */
		List<Genre> allGenres = this.bookService.getAllGenres();

		model.addAttribute("allGenres", allGenres);
		model.addAttribute("appName", appName);

	}
	
	private Set<Author> getListAuthors(Bookform book) {
		/* 
		* A partire dagli autori contenuti nell'istanza di Bookform (stringhe)
		* restituisce una lista di authori (Author, non stringhe)
		*/
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
		/* 
		* A partire dai generi contenuti nell'istanza di Bookform (stringhe)
		* restituisce una lista di generi (Genre, non stringhe)
		*/
		List<String> genreBookFormList = book.getGenre();
		List<Genre> genreList = bookService.findGenresFromNamesArray(genreBookFormList);
		Set<Genre> genreSet = new HashSet<>(genreList);
		return genreSet;
	}
	
	public List<Author> findAuthorPerSeller(User seller){
		/* 
		* Restituisce una lista di autori legati ai libri venduti dal venditore considerato
		*/
		List <Book> sellerBooks = bookService.findAllBookSoldOfSeller(seller);
		Set<Author> authorSet = new HashSet<>();
			
		Iterator<Book> iterBook = sellerBooks.iterator();
		while(iterBook.hasNext()) {
			Set<Author> currAuthors = iterBook.next().getAuthors();
			authorSet.addAll(currAuthors);
		}
		List<Author> authorList = new ArrayList<>(authorSet);
		// Ordina la lista di autori
		Comparator<Author> cm1=Comparator.comparing(Author::getName);
		Collections.sort(authorList,cm1); 
		return authorList;
	}
	
	/*----------------------End Utilities----------------------*/
}
