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
				"Inserisci la tua data di nascita.");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "personalData.name", "personalData.name.required",
				"Inserisci il tuo nome.");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "personalData.surname", "personalData.surname.required",
				"Inserisci il tuo cognome.");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "email.required", "Inserisci la tua Email.");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "personalData.state", "personalData.state.required",
				"Inserisci il tuo paese.");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "personalData.city", "personalData.city.required",
				"Inserisci la tua città.");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "personalData.street", "personalData.street.required",
				"Inserisci la tua via.");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "personalData.cap", "personalData.cap.required",
				"Inserisci il codice postale.");

		User u = userService.findUserByUsername(user.getUsername());
		if (u!=null) {
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
