package it.bookshop.model.dao;


import java.util.List;

import org.hibernate.Session;

import it.bookshop.model.entity.Book;
import it.bookshop.model.entity.BookOrder;
import it.bookshop.model.entity.Order;

public interface BookOrderDao {

	public Session getSession();
	public void setSession(Session session);
	
	public BookOrder create(Order order, BookOrder b);
	public List<BookOrder> findbyId(long id);
	double sumPrice(long id);
	public List<BookOrder> findbyDate(String data_da, String data_a);
	public void delete(BookOrder b);
	public void deleteOrderBookOrders(Order o);
	public void deleteBookBookOrders(Book b);
	
}
