package it.bookshop.services;

import java.sql.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.bookshop.model.dao.BookDao;
import it.bookshop.model.dao.OrderDao;
import it.bookshop.model.dao.UserDetailsDao;
import it.bookshop.model.entity.Book;
import it.bookshop.model.entity.Order;
import it.bookshop.model.entity.User;

@Transactional
@Service("orderService")
public class OrderServiceDefault implements OrderService {

	private OrderDao orderRepository;
	private BookDao bookRepository;
	private UserDetailsDao userRepository;
	
	@Override
	public Order findById(Long buyerId, Long sellerId, String bookIsbn, Date date) {
		return orderRepository.findById(buyerId, sellerId, bookIsbn, date);
	}

	@Override
	public Order create(User buyer, User seller, Book book, int copies, double total_price,Date date) {
		return orderRepository.create(buyer, seller, book, copies, total_price,date);
	}
	
	@Override
	public Order create(Long buyerId, Long sellerId, String bookIsbn, int copies, double total_price,Date date) { 
		User seller = userRepository.findUserById(sellerId);
		User buyer = userRepository.findUserById(buyerId);
		Book book = bookRepository.findByIsbn(bookIsbn);
		return orderRepository.create(buyer, seller, book, copies, total_price,date);
	}

	@Override
	public Order update(Order order) {
		return orderRepository.update(order);
	}

	@Override
	public List<Order> findAll() {
		return orderRepository.findAll();
	}
	
	@Override
	public List<Order> findAllMadeAfter(Date date) {
		List<Order> all = orderRepository.findAll();
		//Lambda and streams to filter a list
		List<Order> after = all.stream()
				.filter(p -> p.getDate().after(date)).collect(Collectors.toList());
		return after;
	}
	
	@Override
	public List<Order> findBuyerPurchases(Long buyerId) {
		return orderRepository.findBuyerPurchases(buyerId);
	}
	
	@Override
	public List<Order> findBuyerPurchasesMadeAfter(Long buyerId, Date date) {
		List<Order> all = orderRepository.findBuyerPurchases(buyerId);
		List<Order> after = all.stream()
				.filter(p -> p.getDate().after(date)).collect(Collectors.toList());
		return after;
	}

	@Override
	public List<Order> findSellerSales(Long sellerId) {
		return orderRepository.findSellerSales(sellerId);
	}
	
	@Override
	public List<Order> findSellerSalesMadeAfter(Long sellerId, Date date) {
		List<Order> all = orderRepository.findSellerSales(sellerId);
		List<Order> after = all.stream()
				.filter(p -> p.getDate().after(date)).collect(Collectors.toList());
		return after;
	}

	@Override
	public void delete(Order order) {
		orderRepository.delete(order);
	}

	@Autowired
	public void setPurchaseRepository(OrderDao orderRepository) {
		this.orderRepository = orderRepository;
	}
	
	@Autowired
	public void setBookService(BookDao bookRepository) {
		this.bookRepository = bookRepository;
	}

	@Autowired
	public void setUserService(UserDetailsDao userRepository) {
		this.userRepository = userRepository;
	}

}
