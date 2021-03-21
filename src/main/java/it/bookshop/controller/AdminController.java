package it.bookshop.controller;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import it.bookshop.model.entity.Book;
import it.bookshop.model.entity.Coupon;
import it.bookshop.model.entity.Genre;
import it.bookshop.model.entity.Role;
import it.bookshop.model.entity.ShoppingCart;
import it.bookshop.model.entity.User;
import it.bookshop.services.BookService;
import it.bookshop.services.CouponService;
import it.bookshop.services.UserService;

@Controller
public class AdminController {

	@Autowired
	String appName;

	@Autowired
	private UserService userService;

	@Autowired
	private BookService bookService;
	
	@Autowired
	private CouponService couponService;

	@Autowired
	@Qualifier("registrationValidator")
	private Validator userValidator; //Validatore per l'aggiunta di un venditore
	
	@Autowired
	@Qualifier("genreValidator")
	private Validator genreValidator; //Validatore per l'aggiunta di un genere
	
	@Autowired
	@Qualifier("couponValidator")
	private Validator couponValidator; //Validatore per l'aggiunta di un coupon

	@InitBinder
	private void initUserBinder(WebDataBinder binder) {
		//L'unico modo per utilizzare più validatori insieme
		if (binder.getTarget() != null && User.class.equals(binder.getTarget().getClass())) {
			binder.setValidator(userValidator);
		}
		else if (binder.getTarget() != null && Genre.class.equals(binder.getTarget().getClass())) {
			binder.setValidator(genreValidator);
		}
		else if (binder.getTarget() != null && Coupon.class.equals(binder.getTarget().getClass())) {
			binder.setValidator(couponValidator);
		}
	}

	//Mappa da passare al select del tag "<form:select>" nel JSP
	private Map<String, String> countries = new LinkedHashMap<String, String>();

	//Pagina per l'aggiunta di un account per i venditori
	@GetMapping(value = "/admin/add_seller")
	public String addSellerPage(Model model, Authentication authentication) {

		String principal_name = authentication.getName();

		model.addAttribute("newSeller", new User());

		generalOperations(model, principal_name);

		return "add_seller";
	}

	//Gestione della richiesta post per l'aggiunta di un venditore
	@PostMapping(value = "/admin/add_seller")
	public String addSeller(@ModelAttribute("newSeller") @Validated User user, BindingResult br, Model model,
			final RedirectAttributes redirectAttributes, Authentication authentication) {

		//Se ci sono errori nella validazione
		if (br.hasErrors()) {
			generalOperations(model, authentication.getName());
			return "add_seller";
		}

		Role seller_role = userService.findRoleByName("SELLER");
		user.addRole(seller_role);
		this.userService.create(user);

		redirectAttributes.addFlashAttribute("message", "Account venditore creato correttamente!");
		redirectAttributes.addFlashAttribute("msgColor", "#F7941D");
		return "redirect:/admin/add_seller";

	}
	
	//Pagina della lista dei venditori
	@GetMapping(value = "/admin/sellers_list")
	public String sellersList(@RequestParam(required = false) String username, Model model, Authentication authentication) {
		
		String principal_name = authentication.getName();
		
		List<User> sellers = null;
		if(username != null) sellers = userService.findAllForRoleByUsername("SELLER", username);
		else sellers = userService.findAllForRole("SELLER");
		model.addAttribute("sellers", sellers);
		
		generalOperations(model, principal_name);
		return "sellers_list";
	}
	
	//Pagina della lista dei clienti
	@GetMapping(value = "/admin/buyers_list")
	public String buyersList(@RequestParam(required = false) String username, Model model, Authentication authentication) {
		
		String principal_name = authentication.getName();
		generalOperations(model, principal_name);
		
		List<User> buyers = null;
		if(username != null) buyers = userService.findAllBuyersOnlyByUsername(username);
		else buyers = userService.findAllBuyersOnly();
		model.addAttribute("buyers", buyers);
		
		
		return "buyers_list";
	}
	
	//Gestione della richiesta AJAX per eliminare un cliente/venditore
	@PostMapping(value = "/admin/delete_user")
	@ResponseBody
	public String deleteUser(@RequestBody String username) {
		userService.deleteByUsername(username);
		return "ok";
	}
	
	//Pagina per visualizzare i coupon e aggiungerne altri
	@GetMapping(value = "/admin/manage_coupons")
	public String manageCouponsPage(Model model, Authentication authentication) {
		String principal_name = authentication.getName();
		generalOperations(model, principal_name);
		
		model.addAttribute("coupons", couponService.findAll());
		model.addAttribute("newCoupon", new Coupon());
		return "manage_coupons";
	}
	
	//Gestione della richiesta POST dell'aggiunta di un coupon
	@PostMapping(value = "/admin/manage_coupons")
	public String addCoupon(@ModelAttribute("newCoupon") @Validated Coupon coupon, BindingResult br, Model model,
			final RedirectAttributes redirectAttributes, Authentication authentication) {
		
		if (br.hasErrors()) {
			generalOperations(model, authentication.getName());
			model.addAttribute("coupons", couponService.findAll());
			return "manage_coupons";
		}
		//Codice case insensitive
		coupon.setCode(coupon.getCode().trim().toUpperCase());
		
		try {
			couponService.create(coupon);
		} catch(ConstraintViolationException e) {
			e.printStackTrace();
		}
		
		redirectAttributes.addFlashAttribute("message", "Coupon aggiunto!");
		redirectAttributes.addFlashAttribute("msgColor", "#F7941D");
		return "redirect:/admin/manage_coupons";
	}
	
	//Pagina per visualizzare ed aggiungere generi
	@GetMapping(value = "/admin/manage_genres")
	public String manageGenresPage(Model model, Authentication authentication) {
		
		String principal_name = authentication.getName();
		generalOperations(model, principal_name);
		
		model.addAttribute("newGenre", new Genre());
		
		return "manage_genres";
	}
	
	//Gestione della richiesta POST per aggiungere un genere
	@PostMapping(value = "/admin/add_genre")
	public String addGenre(@ModelAttribute("newGenre") @Validated Genre genre, BindingResult br, Model model,
			final RedirectAttributes redirectAttributes, Authentication authentication) {

		if (br.hasErrors()) {
			generalOperations(model, authentication.getName());
			return "manage_genres";
		}
		
		String formattedName = StringUtils.capitalize(genre.getName().toLowerCase().trim());
		bookService.createGenre(formattedName);
		
		redirectAttributes.addFlashAttribute("message", "Genere aggiunto!");
		redirectAttributes.addFlashAttribute("msgColor", "#F7941D");
		return "redirect:/admin/manage_genres";

	}
	
	//Gestione della richiesta POST per eliminare un genere
	@PostMapping(value = "/admin/delete_genre")
	@ResponseBody
	public String deleteGenre(@RequestBody String name) throws NotEmptyGenreException {
		Set<Book> books = bookService.getAllBookForGenre(name);
		
		//Il genere può essere eliminato solo se non ha libri associati
		if(books == null || books.size()==0) {
			Genre genre = bookService.findGenreByName(name);
			bookService.deleteGenre(genre);
		} else throw new NotEmptyGenreException();
		return "ok";
	}
	
	@SuppressWarnings("serial")
	private class NotEmptyGenreException extends Exception {
		
	}

	//Operazioni generali eseguite prima di ogni return delle richieste GET
	private void generalOperations(Model model, String username) {

		User currentUser = userService.findUserByUsername(username);
		List<Genre> allGenres = this.bookService.getAllGenres();

		model.addAttribute("allGenres", allGenres);
		model.addAttribute("appName", appName);

		// Mini carrello
		List<ShoppingCart> user_cart = new ArrayList<ShoppingCart>(currentUser.getShoppingCart());
		model.addAttribute("user_cart", user_cart);
		model.addAttribute("cartTotalPrice", currentUser.getFormattedCartSubtotalPrice());
		model.addAttribute("cartTotalItems", currentUser.getCartTotalItems());

		countries.put("Austria", "Austria");
		countries.put("Germania", "Germania");
		countries.put("Grecia", "Grecia");
		countries.put("Francia", "Francia");
		countries.put("Italia", "Italia");
		countries.put("Spagna", "Spagna");
		countries.put("Svezia", "Svezia");
		countries.put("Svizzera", "Svizzera");
		model.addAttribute("countries", countries);
	}

}
