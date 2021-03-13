package it.bookshop.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.mysql.cj.util.StringUtils;

import it.bookshop.model.Object_form.Authorform;



@Component("authorValidator")
public class AuthorValidator implements Validator {

	
	@Override
	public boolean supports(Class<?> clazz) {
		return Authorform.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		//Authorform author = (Authorform) target;

		//Controllo iniziale se i campi sono vuoti o meno
		// Il secondo argomento non è necessario al momento
		ValidationUtils.rejectIfEmpty(errors, "name", "name.required",
				"Inserisci il nome dell'autore.");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "birthdate", "birthdate.required",
				"Inserisci il Data di nascita dell'autore");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "nationality", "nationality.required", "Inserisci la Nazionalità dell'autore");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "biography", "biography.required", "Inserisci una descrizione dell'autore");


	}

}
