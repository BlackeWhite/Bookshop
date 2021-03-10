package it.bookshop.services;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.bookshop.model.Object_form.BookInfoResponse;
import it.bookshop.model.dao.BookOrderDao;
import it.bookshop.model.dao.OrderDao;
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
		Date data_buy = new Date(Calendar.getInstance().getTime().getTime());
		
		Set<BookOrder> books = new HashSet<BookOrder>();
		for(ShoppingCart c : cart) {
			BookOrder b = new BookOrder();
			b.setBook(c.getBook());
			b.setCopies(c.getCopies()); 
			b.setPrice(c.getBook().getDiscountedPrice());
			b.setPricenovat(c.getBook().getDiscountedPriceNoVat());
			b.setPurchasedate(data_buy);
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
	
	@Override
	public List<BookOrder> findbyId(long id) {
		return this.bookOrderRepository.findbyId(id);
	}
	
	@Override
	public double TotalEarn(List<Book> lb) {
		// calcolo l'incasso totale per un venditore dalla lista dei suoi libri
		Iterator<Book> itbo = lb.iterator();
		double sum = 0;
		while (itbo.hasNext()) {
			sum += this.TotalEarnforBook(itbo.next().getId());
		}
		return sum;
	}

	public double TotalEarnforBook(long id) {
		// calcolo l'incasso totale per un libro
		return this.bookOrderRepository.sumPrice(id);
	}
	
	public BookInfoResponse findbyDate(String data_da, String data_a) {
		// calcolo l'incasso totale e le copie vendute per un intervallo temporale
		
		BookInfoResponse bresp = new BookInfoResponse();
		List<BookOrder> lo = new ArrayList<BookOrder>();
		lo = this.bookOrderRepository.findbyDate(data_da, data_a);
		Iterator <BookOrder> itlo = lo.iterator();
		int copies = 0;
		double earn = 0;
		
		while(itlo.hasNext()) {
			BookOrder bo = itlo.next();
			earn += bo.getPricenovat();
			copies += bo.getCopies();
		}
		
		bresp.setSoldcopies(copies);
		double sumapprox = Math.round(earn * 100.0) / 100.0;
		bresp.setTotearn(sumapprox);
		
		return bresp;
		
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
