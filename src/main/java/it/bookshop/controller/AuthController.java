package it.bookshop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {

	@Autowired
	String appName;
	
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

        model.addAttribute("errorMessage", errorMessage);
        model.addAttribute("appName", appName);
        return "register";
    }
}
