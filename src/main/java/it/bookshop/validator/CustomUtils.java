package it.bookshop.validator;

import java.sql.Date;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.web.multipart.MultipartFile;

import com.mysql.cj.util.StringUtils;


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
	
	
	public static boolean isValidIsbn(String isbn) {
		/*
		 * Controlla se l'isbn è composto da 13 cifre
		 */
	     // Regex to check string 
	  // contains only digits 
		String regex = "\\d{13}"; 
	  
	        // Compile the ReGex 
		Pattern p = Pattern.compile(regex); 
	  
	        // If the string is empty 
	        // return false 
	    if (isbn == null) { 
	        return false; 
	    }
	    if (StringUtils.isEmptyOrWhitespaceOnly(isbn) ){
	        return false;
	    }
	    Matcher m = p.matcher(isbn); 
	    
	    return m.matches();     

	}
	
	// Lista dei formati di immagini ammesse
	private static final List<String> contentTypes = Arrays.asList("image/png", "image/jpeg", "image/jpg");
	
	
	public static boolean isValidExtension(MultipartFile file) {
		String fileContentType = file.getContentType();
		if(contentTypes.contains(fileContentType)) {
			return true;
		} else {
			return false;
		}
	}
}
