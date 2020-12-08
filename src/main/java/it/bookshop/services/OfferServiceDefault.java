package it.bookshop.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.bookshop.model.dao.BookDao;
import it.bookshop.model.dao.OfferDao;
import it.bookshop.model.dao.UserDetailsDao;
import it.bookshop.model.entity.Book;
import it.bookshop.model.entity.Offer;
import it.bookshop.model.entity.User;

@Transactional
@Service("offerService")
public class OfferServiceDefault implements OfferService{

	private OfferDao offerRepository;
	private UserDetailsDao userRepository;
	private BookDao bookRepository;
	
	@Override
	public Offer findById(Long sellerId, String bookIsbn) {
		return offerRepository.findById(sellerId, bookIsbn);
	}

	@Override
	public List<Offer> findSellerOffers(Long sellerId) {
		return offerRepository.findSellerOffers(sellerId);
	}
	
	@Override
	public List<Offer> findBookOffers(String bookIsbn) {
		return offerRepository.findBookOffers(bookIsbn);
	}

	@Override
	public List<Offer> findBookOffersByName(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Offer> findAll() {
		return offerRepository.findAll();
	}

	@Override
	public Offer create(User seller, Book book, int copies, double price) {
		return offerRepository.create(seller, book, copies, price);
	}
	
	@Override
	public Offer create(Long sellerId, String bookIsbn, int copies, double price) {
		User seller = userRepository.findUserById(sellerId);
		Book book = bookRepository.findByIsbn(bookIsbn);
		return offerRepository.create(seller, book, copies, price);
	}

	@Override
	public Offer update(Offer offer) {
		return offerRepository.update(offer);
	}

	@Override
	public void delete(Offer offer) {
		offerRepository.delete(offer);
	}

	public OfferDao getSaleRepository() {
		return offerRepository;
	}
	
	@Autowired
	public void setSaleRepository(OfferDao saleRepository) {
		this.offerRepository = saleRepository;
	}

	@Autowired
	public void setUserRepository(UserDetailsDao userRepository) {
		this.userRepository = userRepository;
	}

	@Autowired
	public void setBookRepository(BookDao bookRepository) {
		this.bookRepository = bookRepository;
	}


}
