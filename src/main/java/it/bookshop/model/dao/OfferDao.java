package it.bookshop.model.dao;

import java.util.List;

import org.hibernate.Session;

import it.bookshop.model.entity.Book;
import it.bookshop.model.entity.Offer;
import it.bookshop.model.entity.User;

public interface OfferDao {

	public Session getSession();
	public void setSession(Session session);
	
	public Offer findById(Long sellerId, String bookIsbn);
	public Offer create(User seller, Book book, int copies, double price);
	public Offer update(Offer offer);
	public List<Offer> findAll();
	public List<Offer> findSellerOffers(Long sellerId);
	public void delete(Offer offer);
}
