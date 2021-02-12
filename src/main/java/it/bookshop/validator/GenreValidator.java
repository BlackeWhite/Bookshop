package it.bookshop.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import it.bookshop.model.entity.Genre;
import it.bookshop.services.BookService;

@Component("genreValidator")
public class GenreValidator implements Validator {

	@Autowired
	BookService bookService;

	@Override
	public boolean supports(Class<?> clazz) {
		return Genre.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		Genre genre = (Genre) target;
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "name.required", "Inserisci un nome.");

		Genre existing = bookService.findByName(genre.getName());
		if (existing != null) {
			errors.rejectValue("name", "invalidName", new Object[] { "'name'" }, "Genere già esistente.");
		}
	}

}
