package it.bookshop.services;

import java.sql.Date;
import java.util.List;
import java.util.Map;

import it.bookshop.model.entity.Book;
import it.bookshop.model.entity.Genre;
import it.bookshop.model.entity.User;

public interface BookService {
	Book findById(Long bookId);
	List<Book> searchBooksByParams(String title, Double price_min, Double price_max, String order_by);
	List<Genre> getAllGenres();
	List<Genre> findGenresFromNamesArray(List<String> names);
	List<Book> findAll();
	List<Book> findAll(Double price_min, Double price_max, String order_by);
	Book create(String Name_author,String Surname_Author,String isbn, String title, 
				Date publish_date, Date insert_date, int copies, double price, User seller, 
				int pages, String summary, String cover, List<String> genres);
	Book update(Book book);
	void delete(Book book);
	void delete(Long bookId);
	void deleteAll();
	List<Book> findFiveMostRecentBook();
	List<Book> findFiveBestSellingBook();
	List<Book> getAllBookForGenre(String name);
	void add_click(Long id);
	List<Book> findMostClickBook();
	
	Map<String, Integer> booksAmountPerGenreFromList(List<Book> books);
	List<Book> filterByGenres(List<Book> books, List<String> genres);
};