package it.bookshop.services;

import java.sql.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import it.bookshop.model.ObjectForm.Bookform;
import it.bookshop.model.entity.Author;
import it.bookshop.model.entity.Book;
import it.bookshop.model.entity.Genre;
import it.bookshop.model.entity.User;

public interface BookService {
	Book findById(Long bookId);
	List<Book> searchBooksByParams(String search_by, String term, Double price_min, Double price_max, String order_by);
	List<Book> findAll();
	Book create(String Name_author,String Surname_Author,String isbn, String title, 
				Date publish_date, Date insert_date, int copies, double price, User seller, 
				int pages, String summary, String cover, List<String> genres, double discount);
	Book create(Bookform book, User seller); // quando aggiungo il ibro della form del venditore
	Book update(Book book);
	void delete(Book book);
	void delete(Long bookId);
	void deleteAll();
	List<Book> findFiveMostRecentBook();
	List<Book> findFiveBestSellingBook();
	Set<Book> getAllBookForGenre(String name);
	void add_click(Long id);
	List<Book> findMostClickBook();
	
	Map<String, Integer> booksAmountPerGenreFromList(List<Book> books);
	List<Book> filterByGenres(List<Book> books, List<String> genres);
	Set<Book> getBooksimilargenre(Book b);
	Set<Book> getBooksimilarAuthor(Book b);
	List<Book> findBookOnSale();
	List<Book> findBooksAuthor(Author author);
	
	List<Genre> getAllGenres();
	List<Genre> findGenresFromNamesArray(List<String> names);
	Genre findGenreByName(String name);
	Genre createGenre(String name);
	Genre findByName(String name);
	void deleteGenre(Genre genre);
	List<Book> findAllBookSoldOfSeller(User seller);
	void removeAllBook();
	void removeBook(Long id);
	List<Book> findBookRemovedForSeller(User seller);
	Book findByIdRemoved(Long bookId);
	void saveRestored(Long id);

	
};