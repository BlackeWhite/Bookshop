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
	public Order create(User buyer, Date date, String shipmentAddress, String payment);
	public Order create(User buyer, Date date, List<BookOrder> books, String shipmentAddress, String payment);
	public Order update(Order order);
	public List<Order> findAll();
	public List<Order> findUserOrders(Long buyerId);
	public void delete(Order order);
}
