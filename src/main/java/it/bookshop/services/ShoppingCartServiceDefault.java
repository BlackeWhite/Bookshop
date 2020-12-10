package it.bookshop.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.bookshop.model.dao.BookDao;
import it.bookshop.model.dao.ShoppingCartDao;
import it.bookshop.model.dao.UserDetailsDao;
import it.bookshop.model.entity.Book;
import it.bookshop.model.entity.ShoppingCart;
import it.bookshop.model.entity.User;

@Transactional
@Service("offerService")
public class ShoppingCartServiceDefault implements ShoppingCartService{

	private ShoppingCartDao shoppingCartRepository;
	private UserDetailsDao userRepository;
	private BookDao bookRepository;
	
	@Override
	public ShoppingCart findById(Long sellerId, String bookIsbn) {
		return shoppingCartRepository.findById(sellerId, bookIsbn);
	}

	@Override
	public List<ShoppingCart> findSellerOffers(Long sellerId) {
		return shoppingCartRepository.findSellerOffers(sellerId);
	}
	
	@Override
	public List<ShoppingCart> findBookOffers(String bookIsbn) {
		return shoppingCartRepository.findBookOffers(bookIsbn);
	}

	@Override
	public List<ShoppingCart> findBookOffersByName(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ShoppingCart> findAll() {
		return shoppingCartRepository.findAll();
	}

	@Override
	public ShoppingCart create(User seller, Book book, int copies, double price) {
		return shoppingCartRepository.create(seller, book, copies, price);
	}
	
	@Override
	public ShoppingCart create(Long sellerId, Long bookId, int copies) {
		User seller = userRepository.findUserById(sellerId);
		Book book = bookRepository.findById(bookId);
		return shoppingCartRepository.create(seller, book, copies);
	}

	@Override
	public ShoppingCart update(ShoppingCart cart) {
		return shoppingCartRepository.update(cart);
	}


	public ShoppingCartDao getSaleRepository() {
		return shoppingCartRepository;
	}
	
	@Autowired
	public void setSaleRepository(ShoppingCartDao saleRepository) {
		this.shoppingCartRepository = saleRepository;
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
