package it.bookshop.controller;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class AdvSearchController {

	@Autowired
	String appName;
	
	@RequestMapping(value = "/advanced_search", method = RequestMethod.GET)
	public String advSearch(Locale locale, Model model) {
		System.out.println("Advanced search Page Requested,  locale = " + locale);
		
		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);

		String formattedDate = dateFormat.format(date);

		model.addAttribute("serverTime", formattedDate);
		
		model.addAttribute("appName", appName);

		return "advanced_search";
	}
	
}
