package it.bookshop.model.dao;

import java.sql.Date;
import java.util.List;
import org.hibernate.Session;

import it.bookshop.model.ObjectForm.Bookform;
import it.bookshop.model.entity.Book;
import it.bookshop.model.entity.User;

public interface BookDao {
	
	Session getSession();
	public void setSession(Session session);
	
	List<Book> findAll();
	List<Book> findAll(Double price_min, Double price_max, String order_by);
	Book findById(Long bookId);
	List<Book> searchBooksByParams(String search_by, String term, Double price_min, Double price_max, String order_by);
	List<Book> searchBooksByParamsAndAuthor(String term, Double price_min, Double price_max, String order_by);
	Book create(String isbn,String title, Date publish_date, Date insert_date, int copies, double price, User seller, int pages, String summary, String cover, double discount);
	Book update(Book book);
	void delete(Book book);
	List<Book> findFiveMostRecentBook();
	List<Book> findFiveBestSellingBook();
	List<Book> findMostClick();
	List<Book> findBookOnSale();
	List<Book> findAllBookForGenre(String name);
	Book create(Bookform book, User seller);
	List<Book> findSellerBook(Long id);
	void removeBook(Book book);
	List<Book> findBookRemovedForSeller(User seller);
	Book findByIdRemoved(Long bookId);
	
}
