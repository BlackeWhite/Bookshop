package it.bookshop.controller;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import it.bookshop.model.dao.RoleDao;
import it.bookshop.model.entity.Genre;
import it.bookshop.model.entity.Role;
import it.bookshop.model.entity.ShoppingCart;
import it.bookshop.model.entity.User;
import it.bookshop.services.BookService;
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
	@Qualifier("registrationValidator")
	private Validator userValidator;

	@InitBinder
	private void initUserBinder(WebDataBinder binder) {
		if (binder.getTarget() != null && User.class.equals(binder.getTarget().getClass())) {
			binder.setValidator(userValidator);
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
		return "redirect:/add_seller";

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

	private void generalOperations(Model model, String username) {

		User currentUser = userService.findUserByUsername(username);
		List<Genre> allGenres = this.bookService.getAllGenres();

		model.addAttribute("allGenres", allGenres);
		model.addAttribute("appName", appName);

		// Mini carrello
		List<ShoppingCart> user_cart = new ArrayList<ShoppingCart>(currentUser.getShoppingCart());
		model.addAttribute("user_cart", user_cart);
		model.addAttribute("cartTotalPrice", currentUser.getFormattedCartTotalPrice());
		model.addAttribute("cartTotalItems", currentUser.getCartTotalItems());

		countries.put("Italia", "Italia");
		countries.put("Germania", "Germania");
		countries.put("Francia", "Francia");
		countries.put("Svizzera", "Svizzera");
		model.addAttribute("countries", countries);
	}

}
