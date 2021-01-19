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
//                            @RequestParam(value = "logout", required = false) String logout,
                            Model model) {
        String errorMessage = null;
        if(error != null) {
        	errorMessage = "Username o Password errati !!";
        }
//        if(logout != null) {
//        	// entriamo in questo caso se non specifichiamo una .logoutSuccessUrl in WebSecurityConf.configure
//        	errorMessage = "Uscita dal sistema avvenuta !!";
//        }
        model.addAttribute("errorMessage", errorMessage);
        model.addAttribute("appName", appName);
        return "login";
    }
}
