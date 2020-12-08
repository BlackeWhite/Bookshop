package it.bookshop.model.dao;

import java.sql.Date;
import java.util.List;

import org.hibernate.Session;

import it.bookshop.model.entity.Book;
import it.bookshop.model.entity.Purchase;
import it.bookshop.model.entity.User;

public interface PurchaseDao {
	
	public Session getSession();
	public void setSession(Session session);
	
	public Purchase findById(Long buyerId, Long sellerId, String bookIsbn, Date date);
	public Purchase create(User buyer, User seller, Book book, int copies, double total_price,Date date);
	public Purchase update(Purchase purchase);
	public List<Purchase> findAll();
	public List<Purchase> findBuyerPurchases(Long buyerId);
	public List<Purchase> findSellerSales(Long sellerId);
	public void delete(Purchase purchase);
}
