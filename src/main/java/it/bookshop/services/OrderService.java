package it.bookshop.services;

import java.sql.Date;
import java.util.List;

import it.bookshop.model.entity.Book;
import it.bookshop.model.entity.Order;
import it.bookshop.model.entity.User;

public interface OrderService {
	
	public Order findById(Long id);
	public Order create(User buyer, double total_expense);
	//If user purchases directly from the book page/mini page
	//TODO delete some unused create functions
	public Order createFromDirectPurchase(User buyer, Book book, int copies);
	//If user buys from shopping cart page
	public Order createFromShoppingCart(Long userId, String payment);
	public Order update(Order order);
	public List<Order> findAll();
	public List<Order> findAllMadeAfter(Date date);
	public List<Order> findUserOrders(Long buyerId);
	public List<Order> findUserOrdersMadeAfter(Long buyerId, Date date);
	public void delete(Order order);
}
