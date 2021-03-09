package it.bookshop.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import it.bookshop.model.entity.PaymentCard;
import it.bookshop.model.entity.User;
import it.bookshop.services.UserService;

@Component("cardValidator")
public class CardValidator implements Validator {

	@Autowired
	private UserService userService;

	@Override
	public boolean supports(Class<?> clazz) {
		return PaymentCard.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		PaymentCard card = (PaymentCard) target;

		// Tecnicamente il browser non permette il submit se questi campi sono vuoti
		// Tuttavia alcuni browser potrebbero non supportare questa funzione
		// Quindi si inseriscono per sicurezza questi controlli manuali
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "number", "number.required", "Inserisci un numero.");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "expirationDate", "expirationDate.required",
				"Inserisci una data di scadenza.");

		//Controlla che la carta non sia già stata inserita
		String currentUserUserName = SecurityContextHolder.getContext().getAuthentication().getName();
		User currentUser = userService.findUserByUsername(currentUserUserName);
		for (PaymentCard p : currentUser.getPaymentCards()) {
			if (p.getNumber().equals(card.getNumber())) {
				errors.rejectValue("number", "invalidNumber", new Object[] { "'number'" },
						"Hai già inserito questa carta.");
			}
		}
		
		//Controlla che la carta non sia già scaduta
		if (!CustomUtils.isFutureDate(card.getExpirationDate())) {
			errors.rejectValue("expirationDate", "invalidExpirationDate", new Object[] { "'expirationDate'" },
					"La carta è già scaduta.");
		}
	}

}
