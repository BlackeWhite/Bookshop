package it.bookshop.model.dao;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import org.hibernate.Session;

import it.bookshop.model.entity.BookOrder;
import it.bookshop.model.entity.Order;
import it.bookshop.model.entity.User;

public interface OrderDao {
	
	public Session getSession();
	public void setSession(Session session);
	
	public Order findById(Long id);
	public Order create(User buyer, LocalDateTime date, Set<BookOrder> books, String shipmentAddress, String payment, double shipmentCost);
	public Order create(User buyer, LocalDateTime date, Set<BookOrder> books, String shipmentAddress, String payment, double shipmentCost, int coupon_discount);
	public Order update(Order order);
	public List<Order> findAll();
	public List<Order> findUserOrders(User user);
	public void delete(Order order);
}
