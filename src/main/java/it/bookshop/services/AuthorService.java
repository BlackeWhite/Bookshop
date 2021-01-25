package it.bookshop.services;

import java.sql.Date;
import java.util.List;
import java.util.Set;

import it.bookshop.model.entity.Author;

public interface AuthorService {
	
	Author findById(Long id);
	Author findByNameAndSurname(String name, String surname);
	Author create(String name, String surname, Date date, String nationality, String biography, String image);
	Author create(String name, String surname);
	Author update(Author author);
	List<Author> findAll();
	List<Author> findMostPopularAuthors();
	void delete(Author author);
	void delete(Long id);
	List<Author> findBestSellingAuthor();
	List<Author> getAuthorsListFromSet(Set<Author> authorSet);
}
