package it.bookshop.model.dao;

import java.sql.Date;
import java.util.List;

import org.hibernate.Session;

import it.bookshop.model.entity.Author;
import it.bookshop.model.entity.Book;

public interface AuthorDao {
	
	public Session getSession();
	public void setSession(Session session);
	
	Author create(String name, String surname);
	Author findById(Long id);
	Author findByNameAndSurname(String Name, String Surname);
	Author create(String name, String surname, Date date, String nationality, String biography, String image);
	Author update(Author author);
	List<Author> findAll();
	void delete(Author author);
	List<Book> findBookForAuthor(Author author);
	List<Book> findBookRemovedForAuthor(Author author);
	
}
