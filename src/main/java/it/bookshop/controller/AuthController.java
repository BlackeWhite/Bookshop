package it.bookshop.controller;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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
	private RoleDao roleDao;

	@Autowired
	private BookService bookService;

	@GetMapping(value = "/login")
	public String loginPage(@RequestParam(value = "error", required = false) String error, Model model) {
		String errorMessage = null;
		if (error != null) {
			errorMessage = "Username o Password errati !!";
		}

		List<Genre> allGenres = this.bookService.getAllGenres();

		model.addAttribute("errorMessage", errorMessage);
		model.addAttribute("allGenres", allGenres);
		model.addAttribute("appName", appName);
		return "login";
	}

	@GetMapping(value = "/register")
	public String registerPage(@RequestParam(value = "error", required = false) String error, Model model) {
		String errorMessage = null;
		Map<String, String> countries = new LinkedHashMap<String, String>();
		countries.put("Italia", "Italia");
		countries.put("Germania", "Germania");
		countries.put("Francia", "Francia");
		countries.put("Svizzera", "Svizzera");

		model.addAttribute("errorMessage", errorMessage);
		model.addAttribute("appName", appName);
		model.addAttribute("countries", countries);
		model.addAttribute("newUser", new User());

		List<Genre> allGenres = this.bookService.getAllGenres();
		model.addAttribute("allGenres", allGenres);

		return "register";
	}

	@PostMapping(value = "/register")
	public String register(@ModelAttribute("newUser") User user, BindingResult br) {

		// TODO implementare la validazione e il password confirmation
		Role user_role = roleDao.findByName("USER");
		user.addRole(user_role);
		this.userService.create(user);

		return "redirect:/login";

		// return "redirect:singers/list"; // NB questo non funzionerebbe!

	}

	@GetMapping(value = "/account")
	public String accountPage(@RequestParam(value = "error", required = false) String error, Model model,
			Authentication authentication) {

		String principal_name = authentication.getName();
		User currentUser = userService.findUserByUsername(principal_name);
		PaymentCard newCard = new PaymentCard();

		List<Genre> allGenres = this.bookService.getAllGenres();

		Map<String, String> countries = new LinkedHashMap<String, String>();
		countries.put("Italia", "Italia");
		countries.put("Germania", "Germania");
		countries.put("Francia", "Francia");
		countries.put("Svizzera", "Svizzera");

		Map<String, String> cardTypes = new LinkedHashMap<String, String>();
		cardTypes.put("Visa", "Visa");
		cardTypes.put("MasterCard", "MasterCard");
		cardTypes.put("Bancomat", "Bancomat");
		cardTypes.put("PostePay", "PostePay");

		model.addAttribute("allGenres", allGenres);
		model.addAttribute("countries", countries);
		model.addAttribute("cardTypes", cardTypes);
		model.addAttribute("currentUser", currentUser);
		model.addAttribute("userCards", currentUser.getPaymentCards());
		model.addAttribute("newCard", newCard);

		// Mini carrello
		List<ShoppingCart> user_cart = new ArrayList<ShoppingCart>(currentUser.getShoppingCart());
		model.addAttribute("user_cart", user_cart);
		model.addAttribute("cartTotalPrice", currentUser.getFormattedCartTotalPrice());
		model.addAttribute("cartTotalItems", currentUser.getCartTotalItems());

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

	@PostMapping(value = "/account_save")
	public String accountSave(@ModelAttribute("currentUser") User currentUser, BindingResult br) {
		userService.update(currentUser);
		return "redirect:/account";
	}
}
