package it.bookshop.model.dao;

import java.sql.Date;
import java.util.List;

import org.hibernate.Session;

import it.bookshop.model.entity.Book;
import it.bookshop.model.entity.Order;
import it.bookshop.model.entity.User;

public interface OrderDao {
	
	public Session getSession();
	public void setSession(Session session);
	
	public Order findById(Long buyerId, Long sellerId, String bookIsbn, Date date);
	public Order create(User buyer, User seller, Book book, int copies, double total_price,Date date);
	public Order update(Order order);
	public List<Order> findAll();
	public List<Order> findBuyerPurchases(Long buyerId);
	public List<Order> findSellerSales(Long sellerId);
	public void delete(Order order);
}
