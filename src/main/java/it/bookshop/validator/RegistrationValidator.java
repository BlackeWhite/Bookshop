package it.bookshop.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

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

		// The second argument is not necessary at the moment, it can be implemented in
		// the validation property file
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

		User u = userService.findUserByUsername(user.getUsername());
		if (u!=null && u.getUserID() != user.getUserID() ) {
			errors.rejectValue("username", "invalidUsername", new Object[] { "'username'" },
					"Username già utilizzato.");
		}

		if (CustomUtils.isFutureDate(user.getPersonalData().getBirthdate())) {
			errors.rejectValue("personalData.birthdate", "invalidBirthdate",
					new Object[] { "'personalData.birthdate'" }, "Inserisci una data passata.");
		}

		if (!CustomUtils.isValidEmail(user.getEmail())) {
			errors.rejectValue("email", "invalidEmail",
					new Object[] { "'email'" }, "Inserisci una email valida.");
		}

	}

}
