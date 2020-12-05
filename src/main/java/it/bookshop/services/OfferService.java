package it.bookshop.services;

import java.util.List;

import it.bookshop.model.entity.Book;
import it.bookshop.model.entity.Offer;
import it.bookshop.model.entity.User;

public interface OfferService {

	public Offer findById(Long sellerId, String bookIsbn);
	public List<Offer> findSellerOffers(Long sellerId);
	public List<Offer> findAll();
	public Offer create(User seller, Book book, int copies, double price);
	public Offer create(Long sellerId, String bookIsbn, int copies, double price);
	public Offer update(Offer offer);
	public void delete(Offer offer);
}
