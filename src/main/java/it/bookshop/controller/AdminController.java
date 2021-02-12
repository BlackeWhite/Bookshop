package it.bookshop.controller;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

import it.bookshop.model.dao.RoleDao;
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
	private Validator userValidator;
	
	@Autowired
	@Qualifier("genreValidator")
	private Validator genreValidator;
	
	@Autowired
	@Qualifier("couponValidator")
	private Validator couponValidator;

	@InitBinder
	private void initUserBinder(WebDataBinder binder) {
		//Only known working way to use multiple validators
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

	private Map<String, String> countries = new LinkedHashMap<String, String>();

	@GetMapping(value = "/admin/add_seller")
	public String addSellerPage(Model model, Authentication authentication) {

		String principal_name = authentication.getName();

		model.addAttribute("newSeller", new User());

		generalOperations(model, principal_name);

		return "add_seller";
	}

	@PostMapping(value = "/admin/add_seller")
	public String addSeller(@ModelAttribute("newSeller") @Validated User user, BindingResult br, Model model,
			final RedirectAttributes redirectAttributes, Authentication authentication) {

		if (br.hasErrors()) {
			generalOperations(model, authentication.getName());
			return "add_seller";
		}

		Role user_role = userService.findRoleByName("USER");
		Role seller_role = userService.findRoleByName("SELLER");
		user.addRole(user_role);
		user.addRole(seller_role);
		this.userService.create(user);

		redirectAttributes.addFlashAttribute("message", "Account venditore creato correttamente!");
		redirectAttributes.addFlashAttribute("msgColor", "#F7941D");
		return "redirect:/admin/add_seller";

	}
	
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
	
	@PostMapping(value = "/admin/delete_user")
	@ResponseBody
	public String deleteUser(@RequestBody String username) {
		userService.deleteByUsername(username);
		return "ok";
	}
	
	@GetMapping(value = "/admin/manage_coupons")
	public String manageCouponsPage(Model model, Authentication authentication) {
		String principal_name = authentication.getName();
		generalOperations(model, principal_name);
		
		model.addAttribute("coupons", couponService.findAll());
		model.addAttribute("newCoupon", new Coupon());
		return "manage_coupons";
	}
	
	@PostMapping(value = "/admin/manage_coupons")
	public String addCoupon(@ModelAttribute("newCoupon") @Validated Coupon coupon, BindingResult br, Model model,
			final RedirectAttributes redirectAttributes, Authentication authentication) {
		
		if (br.hasErrors()) {
			generalOperations(model, authentication.getName());
			model.addAttribute("coupons", couponService.findAll());
			return "manage_coupons";
		}
		
		//TODO implement a create function that takes a coupon directly
		couponService.create(coupon.getCode(), coupon.getDiscount(), coupon.getExpireDate());
		
		redirectAttributes.addFlashAttribute("message", "Coupon aggiunto!");
		redirectAttributes.addFlashAttribute("msgColor", "#F7941D");
		return "redirect:/admin/manage_coupons";
	}
	
	@GetMapping(value = "/admin/manage_genres")
	public String manageGenresPage(Model model, Authentication authentication) {
		
		String principal_name = authentication.getName();
		generalOperations(model, principal_name);
		
		model.addAttribute("newGenre", new Genre());
		
		return "manage_genres";
	}
	
	
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
	
	@PostMapping(value = "/admin/delete_genre")
	@ResponseBody
	public String deleteGenre(@RequestBody String name) throws NotEmptyGenreException {
		Set<Book> books = bookService.getAllBookForGenre(name);
		if(books == null || books.size()==0) {
			Genre genre = bookService.findGenreByName(name);
			bookService.deleteGenre(genre);
		} else throw new NotEmptyGenreException();
		return "ok";
	}
	
	private class NotEmptyGenreException extends Exception {
		
	}

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

		countries.put("Italia", "Italia");
		countries.put("Germania", "Germania");
		countries.put("Francia", "Francia");
		countries.put("Svizzera", "Svizzera");
		model.addAttribute("countries", countries);
	}

}
