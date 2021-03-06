package it.bookshop.validator;


import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;


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

		// controloo solo i campi della form required 
		ValidationUtils.rejectIfEmpty(errors, "name", "name.required",
				"Inserisci il nome dell'autore.");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "birthdate", "birthdate.required",
				"Inserisci la Data di nascita dell'autore");
		
	


	}

}
