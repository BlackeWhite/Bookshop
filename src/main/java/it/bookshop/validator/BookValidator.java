package it.bookshop.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.mysql.cj.util.StringUtils;

import it.bookshop.model.entity.User;
import it.bookshop.services.UserService;
import it.bookshop.services.BookService;
import it.bookshop.model.ObjectForm.Bookform;

@Component("bookValidator")
public class BookValidator implements Validator {
	
	@Override
	public boolean supports(Class<?> clazz) {
		return Bookform.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		Bookform book = (Bookform) target;

		//Controllo iniziale se i campi sono vuoti o meno
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "title", "title.required",
				"Inserisci il titolo del libro.");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "isbn", "isbn.required",
				"Inserisci il codice ISBN.");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "copies", "copies.required", "Inserisci il numero di copie del libro disponibili.");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "price", "price.required", "Inserisci il prezzo (IVA esclusa).");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "pages", "pages.required",
				"Inserisci il numero di pagine del libro.");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "publish", "publish.required",
				"Inserisci la data di pubblicazione del libro.");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "cover", "cover.required",
				"Inserisci l'immagine della copertina del libro.");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "summary", "summary.required",
				"Inserisci una breve descrizione del libro.");

		if(CustomUtils.isFutureDate(book.getPublish())) {
			/*
			 * Controlla che la data non sia posteriore a quella odierna
			 */
			errors.rejectValue("publish", "invalidPublish",
					new Object[] { "'publish'" }, "Data invalida, inserire una data antecedente a quella odierna.");
		}
		

		if(!CustomUtils.isValidIsbn(book.getIsbn())) {
			/*
			 * Controlla la validità dell'ISBN
			 */
					errors.rejectValue("isbn", "invalidIsbn",
							new Object[] { "'isbn'" }, "Il codice ISBN deve essere composto da 13 cifre.");
				}
	}

}
