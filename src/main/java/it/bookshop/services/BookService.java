package it.bookshop.services;

import java.sql.Date;
import java.util.List;

import it.bookshop.model.entity.Book;
import it.bookshop.model.entity.Genre;
import it.bookshop.model.entity.User;

public interface BookService {
	Book findById(Long bookId);
	List<Book> searchBooksByTitle(String title, String order_by);
	List<Genre> getAllGenres();
	List<Genre> findGenresFromNamesArray(List<String> names);
	List<Book> findAll();
	Book create(String Name_author,String Surname_Author,String isbn, String title, 
				Date publish_date, Date insert_date, int copies, double price, User seller, int pages, String summary, String cover, String genre);
	Book update(Book book);
	void delete(Book book);
	void delete(Long bookId);
	void deleteAll();
	List<Book> findFiveMostRecentBook();
	List<Book> findFiveBestSellingBook();
	List<Book> getAllBookForGenre(String name);
	void add_click(Long id);
	List<Book> findMostClickBook();
	
};