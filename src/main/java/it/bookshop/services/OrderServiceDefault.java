package it.bookshop.services;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.bookshop.model.ObjectForm.BookInfoResponse;
import it.bookshop.model.dao.BookDao;
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
	private BookDao bookRepository;
	private BookOrderDao bookOrderRepository;
	private ShoppingCartService shoppingCartService;
	private UserService userService;
	
	@Override
	public Order findById(Long id) {
		return orderRepository.findById(id);
	}


	@Override
	public Order createFromShoppingCart(Long userId, String shipmentAddress, String payment, Coupon coupon) {
		LocalDateTime date = LocalDateTime.now();
		User user = userService.findUserById(userId);
		List<ShoppingCart> cart = shoppingCartService.findUserShoppingCart(user);
		Date data_buy = new Date(Calendar.getInstance().getTime().getTime());
		
		Set<BookOrder> books = new HashSet<BookOrder>();
		//Crea gli elementi BookOrder dal carrello dell'utente
		for(ShoppingCart c : cart) {
			BookOrder b = new BookOrder();
			b.setBook(c.getBook());
			b.setCopies(c.getCopies());
			//Applica lo sconto ai singoli libri 
			double coupon_saving1 = 0, coupon_saving2 = 0;
			if(coupon !=null) {
				coupon_saving1 = c.getBook().getDiscountedPrice()*(double)(coupon.getDiscount()/100.00f);
				coupon_saving2 = c.getBook().getDiscountedPriceNoVat()*(double)(coupon.getDiscount()/100.00f);
			}
			b.setPrice(c.getBook().getDiscountedPrice()-coupon_saving1);
			b.setPricenovat(c.getBook().getDiscountedPriceNoVat()-coupon_saving2);
			b.setPurchasedate(data_buy);
			books.add(b);
			}
		
		//Il costo di spedizione non � calcolato al momento, quindi per semplicit� si passa sempre 5
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
	public List<Order> findUserOrders(User user) {
		return orderRepository.findUserOrders(user); 
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
	
	public BookInfoResponse findbyDate(String data_da, String data_a, User seller) {
		// calcolo l'incasso totale e le copie vendute per un intervallo di tempo
		
		BookInfoResponse bresp = new BookInfoResponse();
		List<BookOrder> lo = new ArrayList<BookOrder>();
		lo = this.bookOrderRepository.findbyDate(data_da, data_a); //lista libri acquistati in quell'intervallo di tempo
		Iterator <BookOrder> itlo = lo.iterator();
		int copies = 0;
		double earn = 0;
		
		while(itlo.hasNext()) {
			BookOrder bo = itlo.next();
				if (bo.getBook().getSeller().getUserID() == seller.getUserID()) { // verifico che il ibro sia del venditore 
				earn += bo.getCopies()*bo.getPricenovat();
				copies += bo.getCopies();
				}
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
	public void setBookRepository(BookDao bookRepository) {
		this.bookRepository = bookRepository;
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
