package it.bookshop.validator;

import java.sql.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;

import it.bookshop.model.entity.User;
import it.bookshop.services.UserService;

/**
 * Custom class with static methods useful for the validators 
 */
public class CustomUtils {

	
	
	// REGEX for validating the email
	public static final Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$",
			Pattern.CASE_INSENSITIVE);

	public static boolean isValidEmail(String emailStr) {
		Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
		return matcher.find();
	}
	
	public static boolean isFutureDate(Date date) {
		Date today = new Date(System.currentTimeMillis());
		return date.after(today);
	}
	
}
