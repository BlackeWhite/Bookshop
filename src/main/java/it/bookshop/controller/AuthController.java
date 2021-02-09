package it.bookshop.controller;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
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
import it.bookshop.model.entity.PaymentCard;
import it.bookshop.model.entity.PersonalData;
import it.bookshop.model.entity.Role;
import it.bookshop.model.entity.ShoppingCart;
import it.bookshop.model.entity.User;
import it.bookshop.services.BookService;
import it.bookshop.services.UserDetailsServiceDefault;
import it.bookshop.services.UserService;

@Controller
public class AuthController {

	@Autowired
	String appName;

	@Autowired
	private UserService userService;

	@Autowired
	private BookService bookService;

	@Autowired
	private PasswordEncoder passwordEncoder;

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
	private Map<String, String> cardTypes = new LinkedHashMap<String, String>();

	@GetMapping(value = "/login")
	public String loginPage(@RequestParam(value = "error", required = false) String error, Model model) {
		String errorMessage = null;
		if (error != null) {
			errorMessage = "Username o Password errati !!";
		}

		model.addAttribute("message", errorMessage);

		// Adds attributes used in almost all requests
		generalOperations(model);

		return "login";
	}

	@GetMapping(value = "/register")
	public String registerPage(@RequestParam(value = "error", required = false) String error, Model model) {
		String errorMessage = null;

		model.addAttribute("errorMessage", errorMessage);
		model.addAttribute("newUser", new User());

		generalOperations(model);

		return "register";
	}

	@PostMapping(value = "/register")
	public String register(@ModelAttribute("newUser") @Validated User user, BindingResult br, Model model) {

		if (br.hasErrors()) {
			generalOperations(model);
			return "register";
		}

		Role user_role = userService.findRoleByName("USER");
		user.addRole(user_role);
		this.userService.create(user);

		return "redirect:/login";

	}

	@GetMapping(value = "/account")
	public String accountPage(@RequestParam(value = "error", required = false) String error, Model model,
			Authentication authentication) {

		String principal_name = authentication.getName();
		User currentUser = userService.findUserByUsername(principal_name);

		model.addAttribute("currentUser", currentUser);

		accountPageSpecificOps(model, principal_name);
		generalOperations(model);

		return "account";
	}

	@PostMapping(value = "/add_payment_card")
	public String addPaymentCard(@ModelAttribute("newCard") PaymentCard newCard, BindingResult br,
			Authentication authentication) {

		String principal_name = authentication.getName();
		User currentUser = userService.findUserByUsername(principal_name);
		userService.createPaymentCard(newCard, currentUser);

		return "redirect:/account";
	}

	public static class CardRequest {
		private long cardId;

		public CardRequest() {
		}

		public long getCardId() {
			return cardId;
		}

		public void setCardId(long cardId) {
			this.cardId = cardId;
		}

	}

	@PostMapping(value = "/delete_card")
	@ResponseBody
	public String deleteCard(@RequestBody CardRequest card) {
		userService.deletePaymentCard(card.getCardId());
		return "";
	}

	@PostMapping(value = "change_password")
	public String changePassword(@RequestParam String old_password, @RequestParam String password,
			Authentication authentication, final RedirectAttributes redirectAttributes) {

		String principal_name = authentication.getName();
		User currentUser = userService.findUserByUsername(principal_name);

		if (passwordEncoder.matches(old_password, currentUser.getPassword())) {
			currentUser.setPassword(passwordEncoder.encode(password));
			userService.update(currentUser);
			redirectAttributes.addFlashAttribute("message", "Password modificata correttamente!");
			redirectAttributes.addFlashAttribute("msgColor", "#F7941D");
			return "redirect:/account";
		} else {
			redirectAttributes.addFlashAttribute("message", "Password errata!");
			redirectAttributes.addFlashAttribute("msgColor", "red");
			return "redirect:/account";
		}

	}

	@PostMapping(value = "/account_save")
	public String accountSave(@ModelAttribute("currentUser") @Validated User user, BindingResult br, Model model,
			final RedirectAttributes redirectAttributes) {

		// String principal_name = authentication.getName();

		if (br.hasErrors()) {
			accountPageSpecificOps(model, user.getUsername());
			generalOperations(model);
			return "account";
		}

		// FOR SECURITY AND SIMPLICITY REASONS MANY FIELDS ARE NOT PASSED IN THE HTML
		// FORM
		// SO WE MUST OVERWRITE THE EDITED FIELDS IN THE EXISTING USER
		User existingUser = userService.findUserByUsername(user.getUsername());
		existingUser.setPersonalData(user.getPersonalData());
		existingUser.setEmail(user.getEmail());
		userService.update(existingUser);
		redirectAttributes.addFlashAttribute("message2", "Dati modificati correttamente!");
		redirectAttributes.addFlashAttribute("msgColor", "#F7941D");
		return "redirect:/account";
	}

	// Adds attributes used in almost all requests
	private void generalOperations(Model model) {
		List<Genre> allGenres = this.bookService.getAllGenres();
		countries.put("Italia", "Italia");
		countries.put("Germania", "Germania");
		countries.put("Francia", "Francia");
		countries.put("Svizzera", "Svizzera");

		cardTypes.put("Visa", "Visa");
		cardTypes.put("MasterCard", "MasterCard");
		cardTypes.put("Bancomat", "Bancomat");
		cardTypes.put("PostePay", "PostePay");

		model.addAttribute("allGenres", allGenres);
		model.addAttribute("appName", appName);
		model.addAttribute("countries", countries);
		model.addAttribute("cardTypes", cardTypes);
	}

	// Adds attributes for the account page
	// It'sa function because it's used in two different routes
	private void accountPageSpecificOps(Model model, String username) {
		User currentUser = userService.findUserByUsername(username);
		PaymentCard newCard = new PaymentCard();

		model.addAttribute("userCards", currentUser.getPaymentCards());
		model.addAttribute("newCard", newCard);

		// Mini carrello
		List<ShoppingCart> user_cart = new ArrayList<ShoppingCart>(currentUser.getShoppingCart());
		model.addAttribute("user_cart", user_cart);
		model.addAttribute("cartTotalPrice", currentUser.getFormattedCartSubtotalPrice());
		model.addAttribute("cartTotalItems", currentUser.getCartTotalItems());
	}
}
