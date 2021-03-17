package it.bookshop.services;

import java.util.List;

import it.bookshop.model.Object_form.BookInfoResponse;
import it.bookshop.model.entity.Book;
import it.bookshop.model.entity.BookOrder;
import it.bookshop.model.entity.Coupon;
import it.bookshop.model.entity.Order;
import it.bookshop.model.entity.User;

public interface OrderService {
	
	public Order findById(Long id);
	
	//If user buys from shopping cart page
	public Order createFromShoppingCart(Long userId, String shipmentAdress, String payment, Coupon coupon);
	
	public Order update(Order order);
	public List<Order> findAll();
	public List<Order> findUserOrders(User user);
	public void delete(Order order);

	List<BookOrder> findbyId(long id);
 
	double TotalEarnforBook(long id);
	double TotalEarn(List<Book> lb);
	
	BookInfoResponse findbyDate(String data_da, String data_a);

}
