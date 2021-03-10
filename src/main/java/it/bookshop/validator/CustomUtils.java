package it.bookshop.validator;

import java.sql.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Classe custom con metodi statici utili per la validazione
 */
public class CustomUtils {

	
	
	// REGEX per validare l'email
	public static final Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$",
			Pattern.CASE_INSENSITIVE);

	public static boolean isValidEmail(String emailStr) {
		Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
		return matcher.find();
	}
	
	//Controlla che una data sia futura a quella odierna
	public static boolean isFutureDate(Date date) {
		Date today = new Date(System.currentTimeMillis());
		return date.after(today);
	}
	
	//Controlla che la data inserita sia precedente di 18 anni da quella odierna
	//Per il controllo dei 18 anni
	public static boolean is18YearsOld(Date date) {
		Date d18yearsAgo = new Date(System.currentTimeMillis()-Long.parseLong("568080000000"));
		return date.before(d18yearsAgo);
	}
	
	public static final Pattern VALID_PASSWORD_REGEX = Pattern.compile("^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{8,20}$");
	
	//Controlla la validità della password
	public static boolean isValidPassword(String password) {
		Matcher matcher = VALID_PASSWORD_REGEX.matcher(password);
		return matcher.matches();		
	}
	
}
