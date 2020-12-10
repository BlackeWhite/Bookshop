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
import it.bookshop.model.entity.ShoppingCartId;
import it.bookshop.model.entity.User;

@Transactional
@Service("offerService")
public class ShoppingCartServiceDefault implements ShoppingCartService{

	private ShoppingCartDao shoppingCartRepository;
	private UserDetailsDao userRepository;
	private BookDao bookRepository;
	
	@Override
	public ShoppingCart findById(ShoppingCartId id) {
		return shoppingCartRepository.findById(id);
	}
	
	@Override
	public ShoppingCart findById(Long userId, Long bookId) {
		ShoppingCartId id = new ShoppingCartId(userId, bookId);
		return shoppingCartRepository.findById(id);
	}

	@Override
	public List<ShoppingCart> findUserShoppingCart(Long userId) {
		return shoppingCartRepository.findUserShoppingCart(userId);
	}
	

	@Override
	public ShoppingCart create(User seller, Book book, int copies) {
		return shoppingCartRepository.create(seller, book, copies);
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
	
	@Override
	public void removeBook(ShoppingCart cart) {
		shoppingCartRepository.removeBook(cart);
	}

	@Override
	public void removeBook(Long userId, Long bookId) {
		ShoppingCart c = findById(userId, bookId);
		shoppingCartRepository.removeBook(c);
	}

	@Override
	public void emptyUserCart(Long userId) {
		shoppingCartRepository.emptyUserCart(userId);
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
