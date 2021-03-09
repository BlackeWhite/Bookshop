package it.bookshop.model.dao;


import java.text.ParseException;
import java.util.List;

import org.hibernate.Session;

import it.bookshop.model.entity.Book;
import it.bookshop.model.entity.BookOrder;
import it.bookshop.model.entity.Order;

public interface BookOrderDao {

	public Session getSession();
	public void setSession(Session session);
	
	public BookOrder create(Order order, Book book, int copies);
	public BookOrder create(Order order, BookOrder b);
	
	List<BookOrder> findbyId(long id);
	double sumPrice(long id);
	List<BookOrder> findbyDate(String data_da, String data_a) throws ParseException;
	
}
