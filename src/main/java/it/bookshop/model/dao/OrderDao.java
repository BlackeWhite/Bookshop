package it.bookshop.model.dao;

import java.sql.Date;
import java.util.List;

import org.hibernate.Session;

import it.bookshop.model.entity.BookOrder;
import it.bookshop.model.entity.Order;
import it.bookshop.model.entity.User;

public interface OrderDao {
	
	public Session getSession();
	public void setSession(Session session);
	
	public Order findById(Long id);
	public Order create(User buyer, double total_expense, Date date);
	public Order create(User buyer, double total_expense, Date date, List<BookOrder> books);
	public Order update(Order order);
	public List<Order> findAll();
	public List<Order> findUserOrders(Long buyerId);
	public void delete(Order order);
}
