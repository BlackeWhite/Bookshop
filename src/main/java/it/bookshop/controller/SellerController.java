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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import it.bookshop.model.entity.Role;
import it.bookshop.model.entity.User;
import it.bookshop.model.Object_form.Bookform;
import it.bookshop.model.entity.Book;
import it.bookshop.model.entity.Genre;
import it.bookshop.services.BookService;
import it.bookshop.services.UserService;

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

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, Model model) {

		return "home_seller";
	}
	
	// form per l'aggiunta di un libro 
	@GetMapping(value = "/addition_book")
	public String additionBooK(@RequestParam(value = "error", required = false) Locale locale, Model model) {
        
		String errorMessage = null;
		int i = 0; 
		 
		Bookform bf = new Bookform();
		
        List<String> gen = new ArrayList<String>();
        
        List<Genre> allGenres = this.bookService.getAllGenres();
        Iterator<Genre> iteGen = allGenres.iterator();
        
        while(iteGen.hasNext()) {
        	gen.add(iteGen.next().getName());
        }       
        // manca l'upload di un'immagine del libro e l'aggiunta di un'autore 
        
     
		model.addAttribute("errorMessage", errorMessage);
		model.addAttribute("newBook", bf);
		model.addAttribute("genre", gen);
		model.addAttribute("i", i); // utilizzata come contatore nella vista 
      
		generalOperations(model);
		return "addittion_book";
	}
	

	// procedura per l'aggiunta di un libro 
	@PostMapping(value = "/add_book")
	public String addBook(@ModelAttribute("newBook")  @RequestBody @Valid Bookform book, BindingResult br, Model model) {
		
		User seller = userService.create("admin", "admin@email.com", "libreria", "seller", "seller", null, "Via brecce bianche",
				"Ancona", 60000, "Italia", Arrays.asList("SELLER", "ADMIN"));
		
		if (br.hasErrors()) {
			generalOperations(model);
			List<String> gen = new ArrayList<String>();
	        List<Genre> allGenres = this.bookService.getAllGenres();
	        Iterator<Genre> iteGen = allGenres.iterator(); 
	        while(iteGen.hasNext()) {
	        	gen.add(iteGen.next().getName());
	        }       
			model.addAttribute("genre", gen);
			model.addAttribute("newBook", book);
			return "addittion_book";
		}
		else {
		   this.bookService.create(book, seller);  // manca l'upload di un'immagine del libro e l'aggiunta di un'autore 
		}
		/*
		redirectAttributes.addFlashAttribute("message", "Account venditore creato correttamente!");
		redirectAttributes.addFlashAttribute("msgColor", "#F7941D");
	    */
		
		return "redirect:/seller/";
		
	}
	
		
	// Adds attributes used in almost all requests
	private void generalOperations(Model model) {
		List<Genre> allGenres = this.bookService.getAllGenres();

		model.addAttribute("allGenres", allGenres);
		model.addAttribute("appName", appName);

	}
	
	
}
