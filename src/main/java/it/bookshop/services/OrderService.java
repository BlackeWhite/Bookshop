package it.bookshop.services;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.List;

import it.bookshop.model.entity.Book;
import it.bookshop.model.entity.BookOrder;
import it.bookshop.model.entity.Coupon;
import it.bookshop.model.entity.Order;
import it.bookshop.model.entity.User;

public interface OrderService {
	
	public Order findById(Long id);
	
	public Order create(User buyer, String shipmentAddress, String payment);
	
	//If user purchases directly from the book page/mini page
	//TODO delete some unused create functions
	//public Order createFromDirectPurchase(User buyer, Book book, int copies);
	//If user buys from shopping cart page
	public Order createFromShoppingCart(Long userId, String shipmentAdress, String payment, Coupon coupon);
	
	public Order update(Order order);
	public List<Order> findAll();
	public List<Order> findAllMadeAfter(LocalDateTime date);
	public List<Order> findUserOrders(User user);
	public List<Order> findUserOrdersMadeAfter(User user, LocalDateTime date);
	public void delete(Order order);

	List<BookOrder> findbyId(long id);

}
