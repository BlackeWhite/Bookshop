package it.bookshop.services;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.bookshop.model.dao.BookOrderDao;
import it.bookshop.model.dao.OrderDao;
import it.bookshop.model.dao.ShoppingCartDao;
import it.bookshop.model.entity.Book;
import it.bookshop.model.entity.BookOrder;
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
	public Order create(User buyer, double total_expense) {
		Date date = new Date(System.currentTimeMillis());
		return orderRepository.create(buyer, total_expense, date);
	}
	
	@Override
	public Order createFromDirectPurchase(User buyer, Book book, int copies) {
		Date date = new Date(System.currentTimeMillis());
		double total_expense = book.getPrice() * copies;
		Order o = orderRepository.create(buyer, total_expense, date);
		bookOrderRepository.create(o, book, copies);
		//Should add the created BookOrder entity to o and update it?
		return o;
	}
	
	@Override
	public Order createFromShoppingCart(Long userId, String payment) {
		Date date = new Date(System.currentTimeMillis());
		List<ShoppingCart> cart = shoppingCartService.findUserShoppingCart(userId);
		double total_expense = 0;
		List<BookOrder> books = new ArrayList<BookOrder>();
		for(ShoppingCart c : cart) {
			BookOrder b = new BookOrder();
			b.setBook(c.getBook());
			b.setCopies(c.getCopies());
			b.setPrice(c.getBook().getPrice());
			books.add(b);
			total_expense = c.getCopies() * c.getBook().getPrice();
			}
		User u = userService.findUserById(userId);
		return orderRepository.create(u, total_expense, date, books, payment);
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
	public List<Order> findUserOrders(Long buyerId) {
		return orderRepository.findUserOrders(buyerId);
	}
	
	@Override
	public List<Order> findUserOrdersMadeAfter(Long buyerId, Date date) {
		List<Order> all = orderRepository.findUserOrders(buyerId);
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
