package it.bookshop.controller;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import it.bookshop.model.entity.PersonalData;
import it.bookshop.model.entity.User;
import it.bookshop.services.UserDetailsServiceDefault;
import it.bookshop.services.UserService;

@Controller
public class AuthController {

	@Autowired
	String appName;
	
	@Autowired
	private UserService userService;
	
    @GetMapping(value = "/login")
    public String loginPage(@RequestParam(value = "error", required = false) String error,
                            Model model) {
        String errorMessage = null;
        if(error != null) {
        	errorMessage = "Username o Password errati !!";
        }

        model.addAttribute("errorMessage", errorMessage);
        model.addAttribute("appName", appName);
        return "login";
    }
    
    @GetMapping(value = "/register")
    public String registerPage(@RequestParam(value = "error", required = false) String error,
                            Model model) {
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
        return "register";
    }
    
    @PostMapping(value = "/register")
	public String register(@ModelAttribute("newUser") User user, BindingResult br) {
    	
    	System.out.println(user.getPersonalData().getName());
    	System.out.println(user.getPersonalData().getCity());
    	//TODO implementare la validazione e il password confirmation
		this.userService.create(user);
		
		return "redirect:/login";
		
		// return "redirect:singers/list"; // NB questo non funzionerebbe!
		
	}
}
