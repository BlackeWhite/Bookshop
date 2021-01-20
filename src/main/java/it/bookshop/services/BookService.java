package it.bookshop.services;

import java.sql.Date;
import java.util.List;

import it.bookshop.model.entity.Book;
import it.bookshop.model.entity.Genre;
import it.bookshop.model.entity.User;

public interface BookService {
	Book findById(Long bookId);
	Book findByName(String name);
	List<Genre> getAllGenres();
	List<Book> findAll();
	Book create(String Name_author,String Surname_Author,String isbn, String title, 
				Date publish_date, Date insert_date, int copies, double price, User seller, int pages, String summary, String cover, String genre);
	Book update(Book book);
	void delete(Book book);
	void delete(Long bookId);
	void deleteAll();
	List<Book> findFiveMostRecentBook();
};