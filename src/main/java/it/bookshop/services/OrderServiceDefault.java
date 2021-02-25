package it.bookshop.services;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.bookshop.model.dao.BookOrderDao;
import it.bookshop.model.dao.OrderDao;
import it.bookshop.model.dao.ShoppingCartDao;
import it.bookshop.model.entity.Book;
import it.bookshop.model.entity.BookOrder;
import it.bookshop.model.entity.Coupon;
import it.bookshop.model.entity.Order;
import it.bookshop.model.entity.ShoppingCart;
import it.bookshop.model.entity.User;

@Transactional
@Service("orderService")
public class OrderServiceDefault implements OrderService {

	private OrderDao orderRepository;
	private BookOrderDao bookOrderRepository;
	private ShoppingCartService shoppingCartService;
	private UserService userService;
	
	@Override
	public Order findById(Long id) {
		return orderRepository.findById(id);
	}

	@Override
	public Order create(User buyer, String shipmentAddress, String payment) {
		double shipmentCost = 5;
		LocalDateTime date = LocalDateTime.now();
		return orderRepository.create(buyer, date, shipmentAddress, payment, shipmentCost);
	}
	
	//check usage 
	/*
	@Override
	public Order createFromDirectPurchase(User buyer, Book book, int copies) {
		Date date = new Date(System.currentTimeMillis());
		double total_expense = book.getPrice() * copies;
		Order o = orderRepository.create(buyer, total_expense, date);
		bookOrderRepository.create(o, book, copies);
		//Should add the created BookOrder entity to o and update it?
		return o;
	}
	*/

	@Override
	public Order createFromShoppingCart(Long userId, String shipmentAddress, String payment, Coupon coupon) {
		LocalDateTime date = LocalDateTime.now();
		User user = userService.findUserById(userId);
		List<ShoppingCart> cart = shoppingCartService.findUserShoppingCart(user);
		
		Set<BookOrder> books = new HashSet<BookOrder>();
		for(ShoppingCart c : cart) {
			BookOrder b = new BookOrder();
			b.setBook(c.getBook());
			b.setCopies(c.getCopies()); 
			b.setPrice(c.getBook().getDiscountedPrice());
			books.add(b);
			}
		
		double shipmentCost = 5;
		
		if (coupon != null) {
			return orderRepository.create(user, date, books, shipmentAddress, payment, shipmentCost, coupon.getDiscount());
		}
		else {
			return orderRepository.create(user, date, books, shipmentAddress, payment, shipmentCost);
		}
		
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
	public List<Order> findAllMadeAfter(LocalDateTime date) {
		List<Order> all = orderRepository.findAll();
		//Lambda and streams to filter a list
		List<Order> after = all.stream()
				.filter(p -> p.getDate().isAfter(date)).collect(Collectors.toList());
		return after;
	}
	
	@Override
	public List<Order> findUserOrders(User user) {
		return orderRepository.findUserOrders(user); 
	}
	
	@Override
	public List<Order> findUserOrdersMadeAfter(User user, LocalDateTime date) {
		List<Order> all = orderRepository.findUserOrders(user);
		List<Order> after = all.stream()
				.filter(p -> p.getDate().isAfter(date)).collect(Collectors.toList());
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
	public void setBookOrderRepository(BookOrderDao bookOrderRepository) {
		this.bookOrderRepository = bookOrderRepository;
	}

	@Autowired
	public void setShoppingCartService(ShoppingCartService shoppingCartService) {
		this.shoppingCartService = shoppingCartService;
	}

	@Autowired
	public void setUserService(UserService userService) {
		this.userService = userService;
	}

}
