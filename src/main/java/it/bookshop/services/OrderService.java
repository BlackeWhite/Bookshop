package it.bookshop.services;

import java.sql.Date;
import java.util.List;

import it.bookshop.model.entity.Book;
import it.bookshop.model.entity.Order;
import it.bookshop.model.entity.User;

public interface OrderService {
	
	public Order findById(Long buyerId, Long sellerId, String bookIsbn, Date date);
	public Order create(User buyer, User seller, Book book, int copies, double total_price,Date date);
	public Order create(Long buyerId, Long sellerId, String bookIsbn, int copies, double total_price,Date date);
	public Order update(Order order);
	public List<Order> findAll();
	public List<Order> findAllMadeAfter(Date date);
	public List<Order> findBuyerPurchases(Long buyerId);
	public List<Order> findBuyerPurchasesMadeAfter(Long buyerId, Date date);
	public List<Order> findSellerSales(Long sellerId);
	public List<Order> findSellerSalesMadeAfter(Long sellerId, Date date);
	public void delete(Order order);
}
