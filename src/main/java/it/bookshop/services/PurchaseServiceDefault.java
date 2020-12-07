package it.bookshop.services;

import java.sql.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.bookshop.model.dao.PurchaseDao;
import it.bookshop.model.entity.Book;
import it.bookshop.model.entity.Purchase;
import it.bookshop.model.entity.User;

@Transactional
@Service("purchaseService")
public class PurchaseServiceDefault implements PurchaseService {

	private PurchaseDao purchaseRepository;
	private BookService bookService;
	private UserService userService;
	
	@Override
	public Purchase findById(Long buyerId, Long sellerId, String bookIsbn, Date date) {
		return purchaseRepository.findById(buyerId, sellerId, bookIsbn, date);
	}

	@Override
	public Purchase create(User buyer, User seller, Book book, int copies, double total_price) {
		return purchaseRepository.create(buyer, seller, book, copies, total_price);
	}
	
	@Override
	public Purchase create(Long buyerId, Long sellerId, String bookIsbn, int copies, double total_price) { 
		User seller = userService.findUserById(sellerId);
		User buyer = userService.findUserById(buyerId);
		Book book = bookService.findByIsbn(bookIsbn);
		return purchaseRepository.create(buyer, seller, book, copies, total_price);
	}

	@Override
	public Purchase update(Purchase purchase) {
		return purchaseRepository.update(purchase);
	}

	@Override
	public List<Purchase> findAll() {
		return purchaseRepository.findAll();
	}
	
	@Override
	public List<Purchase> findAllMadeAfter(Date date) {
		List<Purchase> all = purchaseRepository.findAll();
		//Lambda and streams to filter a list
		List<Purchase> after = all.stream()
				.filter(p -> p.getDate().after(date)).collect(Collectors.toList());
		return after;
	}
	
	@Override
	public List<Purchase> findBuyerPurchases(Long buyerId) {
		return purchaseRepository.findBuyerPurchases(buyerId);
	}
	
	@Override
	public List<Purchase> findBuyerPurchasesMadeAfter(Long buyerId, Date date) {
		List<Purchase> all = purchaseRepository.findBuyerPurchases(buyerId);
		List<Purchase> after = all.stream()
				.filter(p -> p.getDate().after(date)).collect(Collectors.toList());
		return after;
	}

	@Override
	public List<Purchase> findSellerSales(Long sellerId) {
		return purchaseRepository.findSellerSales(sellerId);
	}
	
	@Override
	public List<Purchase> findSellerSalesMadeAfter(Long sellerId, Date date) {
		List<Purchase> all = purchaseRepository.findSellerSales(sellerId);
		List<Purchase> after = all.stream()
				.filter(p -> p.getDate().after(date)).collect(Collectors.toList());
		return after;
	}

	@Override
	public void delete(Purchase purchase) {
		purchaseRepository.delete(purchase);
	}

	@Autowired
	public void setPurchaseRepository(PurchaseDao purchaseRepository) {
		this.purchaseRepository = purchaseRepository;
	}
	
	@Autowired
	public void setBookService(BookService bookService) {
		this.bookService = bookService;
	}

	@Autowired
	public void setUserService(UserService userService) {
		this.userService = userService;
	}

}
