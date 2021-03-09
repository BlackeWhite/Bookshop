package it.bookshop.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.mysql.cj.util.StringUtils;

import it.bookshop.model.entity.User;
import it.bookshop.services.UserService;

@Component("registrationValidator")
public class RegistrationValidator implements Validator {

	@Autowired
	private UserService userService;
	
	@Override
	public boolean supports(Class<?> clazz) {
		return User.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		User user = (User) target;

		//Controllo iniziale se i campi sono vuoti o meno
		// Il secondo argomento non è necessario al momento
		ValidationUtils.rejectIfEmpty(errors, "personalData.birthdate", "personalData.birthdate.required",
				"Inserisci la data di nascita.");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "personalData.name", "personalData.name.required",
				"Inserisci il nome.");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "personalData.surname", "personalData.surname.required",
				"Inserisci il cognome.");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "email.required", "Inserisci l'Email.");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", "email.required", "Inserisci l'username.");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "personalData.state", "personalData.state.required",
				"Inserisci il paese.");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "personalData.city", "personalData.city.required",
				"Inserisci la città.");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "personalData.street", "personalData.street.required",
				"Inserisci la via.");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "personalData.cap", "personalData.cap.required",
				"Inserisci il codice postale.");

		//Controllo dell'esistenza di utenti con lo stesso nome
		User u = userService.findUserByUsername(user.getUsername());
		//Ovviamente si esclude nell'if l'utente stesso in caso di aggiornamento dell'account
		//dove non si può modificare il proprio username
		if (u!=null && u.getUserID() != user.getUserID() ) {
			errors.rejectValue("username", "invalidUsername", new Object[] { "'username'" },
					"Username già utilizzato.");
		}

		//Controlla che la data di nascita sia precedente a quella odierna
		//Vedere customUtils
		if (!CustomUtils.is18YearsOld(user.getPersonalData().getBirthdate())) {
			errors.rejectValue("personalData.birthdate", "invalidBirthdate",
					new Object[] { "'personalData.birthdate'" }, "Data invalida, devi avere 18 anni.");
			//Attenzione a cambiare il testo di errore, questo validatore è usato anche nell'aggiornamento
			//delle informazioni nella pagina account
		}

		//Controlla che l'email sia valida (solo controllo regex)
		if (!CustomUtils.isValidEmail(user.getEmail())) {
			errors.rejectValue("email", "invalidEmail",
					new Object[] { "'email'" }, "Inserisci una email valida.");
		}
		
		//Controlla la validità della password
		if(!StringUtils.isEmptyOrWhitespaceOnly(user.getPassword()) && !CustomUtils.isValidPassword(user.getPassword())) {
			errors.rejectValue("password", "invalidPassword",
					new Object[] { "'password'" }, "La password deve contenere dagli 8 ai 20 caratteri, almeno una maiuscola e una minuscola e almeno uno dei seguenti caratteri @#$%.");
		}

	}

}
