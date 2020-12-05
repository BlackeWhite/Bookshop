package it.bookshop.services;

import java.sql.Date;
import java.util.List;

import it.bookshop.model.entity.Book;
import it.bookshop.model.entity.Purchase;
import it.bookshop.model.entity.User;

public interface PurchaseService {
	
	public Purchase findById(Long buyerId, Long sellerId, String bookIsbn, Date date);
	public Purchase create(User buyer, User seller, Book book, int copies, double total_price);
	public Purchase create(Long buyerId, Long sellerId, String bookIsbn, int copies, double total_price);
	public Purchase update(Purchase purchase);
	public List<Purchase> findAll();
	public List<Purchase> findAllMadeAfter(Date date);
	public List<Purchase> findBuyerPurchases(Long buyerId);
	public List<Purchase> findBuyerPurchasesMadeAfter(Long buyerId, Date date);
	public List<Purchase> findSellerSales(Long sellerId);
	public List<Purchase> findSellerSalesMadeAfter(Long sellerId, Date date);
	public void delete(Purchase purchase);
}
