package it.bookshop.model.dao;


import org.hibernate.Session;

import it.bookshop.model.entity.Book;
import it.bookshop.model.entity.BookOrder;
import it.bookshop.model.entity.Order;

public interface BookOrderDao {

	public Session getSession();
	public void setSession(Session session);
	
	public BookOrder create(Order order, Book book, int copies);
}
