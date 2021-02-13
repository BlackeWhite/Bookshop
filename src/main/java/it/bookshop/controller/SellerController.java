package it.bookshop.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import it.bookshop.model.entity.Author;
import it.bookshop.model.entity.Role;
import it.bookshop.model.entity.User;
import it.bookshop.model.Object_form.Bookform;
import it.bookshop.model.entity.Book;
import it.bookshop.model.entity.Genre;
import it.bookshop.services.BookService;
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
	private AuthorService authorService;

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, Model model) {

		return "home_seller";
	}

	// form per l'aggiunta di un libro
	@GetMapping(value = "/addition_book")
	public String additionBooK(@RequestParam(value = "error", required = false) Locale locale, Model model) {

		String errorMessage = null;
		int i = 0;
		String mode = "add"; // paramtero utilizzato nella vista per adattare la form in base a cosa si sta
								// facendo

		Bookform bf = new Bookform();

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
		// manca l'upload di un'immagine del libro

		model.addAttribute("errorMessage", errorMessage);
		model.addAttribute("newBook", bf);
		model.addAttribute("genre", gen);
		model.addAttribute("authors", authors);
		model.addAttribute("mode", mode);
		model.addAttribute("i", i); // utilizzata come contatore nella vista

		generalOperations(model);
		return "addittion_book";
	}

	// procedura per l'aggiunta di un libro
	@PostMapping(value = "/add_book")
	public String addBook(@ModelAttribute("newBook") @RequestBody @Valid Bookform book, BindingResult br, Model model) {

		User seller = userService.create("admin", "admin@email.com", "libreria", "seller", "seller", null,
				"Via brecce bianche", "Ancona", 60000, "Italia", Arrays.asList("SELLER", "ADMIN"));

		if (br.hasErrors()) {
			generalOperations(model);
			List<String> gen = new ArrayList<String>();
			List<Genre> allGenres = this.bookService.getAllGenres();
			Iterator<Genre> iteGen = allGenres.iterator();
			while (iteGen.hasNext()) {
				gen.add(iteGen.next().getName());
			}

			List<String> authors = new ArrayList<String>();
			List<Author> allAuthors = this.authorService.findAll();
			Iterator<Author> iterAuthor = allAuthors.iterator();
			while (iterAuthor.hasNext()) {
				authors.add(iterAuthor.next().getFullName());
			}

			model.addAttribute("genre", gen);
			model.addAttribute("newBook", book);
			model.addAttribute("authors", authors);
			return "addittion_book";
		} else {
			this.bookService.create(book, seller); // manca l'upload di un'immagine del libro
			String message = "Libro aggiunto correttamente ";
			model.addAttribute("message", message);
		return "redirect:/seller/";
		}
	}

	// mostra la form per la modifica di un libro
	@GetMapping(value = "/mod_book/{book_id}")
	public String modifyBook(@PathVariable("book_id") Long book_id, Model model) {

		String mode = "modify"; // paramtero utilizzato nella vista per adattare la form in base a cosa si sta
								// facendo
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

		model.addAttribute("genre", gen);
		model.addAttribute("authors", authors);
		model.addAttribute("i", i); // utilizzata come contatore nella vista
		model.addAttribute("newBook", bf);
		model.addAttribute("mode", mode);
		generalOperations(model);
		return "addittion_book";

	}

	// Adds attributes used in almost all requests
	private void generalOperations(Model model) {
		List<Genre> allGenres = this.bookService.getAllGenres();

		model.addAttribute("allGenres", allGenres);
		model.addAttribute("appName", appName);

	}

}
