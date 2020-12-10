package it.bookshop.model.dao;

import java.sql.Date;
import java.util.List;

import org.hibernate.Session;

import it.bookshop.model.entity.Book;

public interface BookDao {
	Session getSession();
	public void setSession(Session session);
	
	List<Book> findAll();
	
	Book findById(Long bookId);
	Book findByTitle(String title);
	Book create(String isbn,String title, Date publish_date, int num_of_pages, String summary, String cover);
	Book update(Book book);
	void delete(Book book);
}
