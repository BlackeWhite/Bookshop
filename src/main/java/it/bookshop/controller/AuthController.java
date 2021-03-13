package it.bookshop.controller;

import java.sql.Date;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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

import it.bookshop.model.entity.CustomUserDetails;
import it.bookshop.model.entity.Genre;
import it.bookshop.model.entity.PaymentCard;
import it.bookshop.model.entity.Role;
import it.bookshop.model.entity.ShoppingCart;
import it.bookshop.model.entity.User;
import it.bookshop.services.BookService;
import it.bookshop.services.UserService;
import it.bookshop.validator.CustomUtils;

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

	//Validatori per i vari form di inserimento dati
	@Autowired
	@Qualifier("registrationValidator")
	private Validator userValidator;

	@Autowired
	@Qualifier("cardValidator")
	private Validator cardValidator;

	//Imposta i validatori a seconda del modelAttribute richiesto
	@InitBinder
	private void initUserBinder(WebDataBinder binder) {
		if (binder.getTarget() != null && User.class.equals(binder.getTarget().getClass())) {
			binder.setValidator(userValidator);
		} else if (binder.getTarget() != null && PaymentCard.class.equals(binder.getTarget().getClass())) {
			binder.setValidator(cardValidator);
		}
	}

	//Mappe da passare ai select del taglib "<form:..>" di spring
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

		// Se ci sono errori dovuti alla validazione
		if (br.hasErrors()) {
			generalOperations(model);
			return "register";
		}

		Role user_role = userService.findRoleByName("USER");
		user.addRole(user_role);
		this.userService.create(user);

		return "redirect:/login";

	}

	//Pagina di modifica informazioni dell'account, cambio password e aggiunta di carte di credito
	@GetMapping(value = "/account")
	public String accountPage(@RequestParam(value = "error", required = false) String error, Model model,
			Authentication authentication) {

		String principal_name = authentication.getName();
		User currentUser = userService.findUserByUsername(principal_name);

		model.addAttribute("currentUser", currentUser);
		model.addAttribute("newCard", new PaymentCard());

		accountPageSpecificOps(model, currentUser);
		generalOperations(model);

		return "account";
	}

	//Post request per aggiungere una carta
	@PostMapping(value = "/add_payment_card")
	public String addPaymentCard(@ModelAttribute("newCard") @Validated PaymentCard newCard, BindingResult br,
			Model model, Authentication authentication) {

		String principal_name = authentication.getName();
		User currentUser = userService.findUserByUsername(principal_name);

		// Se ci sono errori dovuti alla validazione
		if (br.hasErrors()) {
			accountPageSpecificOps(model, currentUser);
			generalOperations(model);
			model.addAttribute("currentUser", currentUser);
			return "account";
		}

		userService.createPaymentCard(newCard, currentUser);

		return "redirect:/account";
	}

	//Classe custom per la richiesta ajax per eliminare una carta di pagamento
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
			// Controlla la validità della password
			if (!CustomUtils.isValidPassword(password)) {
				redirectAttributes.addFlashAttribute("message3",
				"La password deve contenere dagli 8 ai 20 caratteri, almeno una maiuscola e una minuscola e almeno uno dei seguenti caratteri @#$%.");
				return "redirect:/account";
			}

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

	// Aggiorna informazioni account
	@PostMapping(value = "/account_save")
	public String accountSave(@ModelAttribute("currentUser") @Validated User user, BindingResult br, Model model,
			Authentication authentication, final RedirectAttributes redirectAttributes) {

		CustomUserDetails details = (CustomUserDetails) authentication.getPrincipal();
		String principal_name = authentication.getName();
		User currentUser = userService.findUserByUsername(principal_name);

		// Se ci sono errori dovuti alla validazione
		if (br.hasErrors()) {
			accountPageSpecificOps(model, currentUser);
			generalOperations(model);
			model.addAttribute("newCard", new PaymentCard());
			return "account";
		}

		if (!details.getState().equals(user.getPersonalData().getState())) {
			details.setUser(user);
		}

		// Per ragioni di sicurezza e semplicità molti campi non sono passati nel form
		// HTML
		// Perciò dobbiamo sovrascrivere i campi modificati dell'utente esistente
		User existingUser = userService.findUserByUsername(user.getUsername());
		existingUser.setPersonalData(user.getPersonalData());
		existingUser.setEmail(user.getEmail());
		userService.update(existingUser);
		redirectAttributes.addFlashAttribute("message2", "Dati modificati correttamente!");
		redirectAttributes.addFlashAttribute("msgColor", "#F7941D");
		return "redirect:/account";
	}

	// Aggiunge attributi usati in quasi tutte le route
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
	
	//Eliminazione dell'account
	@PostMapping(value = "/delete_account")
	public String deleteAccount(Authentication authentication) {
		
		String principal_name = authentication.getName();
		userService.deleteByUsername(principal_name);
		
		//Reindirizzazione al logout
		return "redirect:/logout";
	}

	// Agggiunge gli attributi per la pagina di account
	// E' una funzione perché è usata più volte
	private void accountPageSpecificOps(Model model, User currentUser) {

		model.addAttribute("userCards", currentUser.getPaymentCards());

		Date today = new Date(System.currentTimeMillis());
		model.addAttribute("today", today);

		// Mini carrello
		List<ShoppingCart> user_cart = new ArrayList<ShoppingCart>(currentUser.getShoppingCart());
		model.addAttribute("user_cart", user_cart);
		model.addAttribute("cartTotalPrice", currentUser.getFormattedCartSubtotalPrice());
		model.addAttribute("cartTotalItems", currentUser.getCartTotalItems());
	}
}
