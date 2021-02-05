package it.bookshop.services;

import java.util.List;

import it.bookshop.model.entity.Book;
import it.bookshop.model.entity.ShoppingCart;
import it.bookshop.model.entity.ShoppingCartId;
import it.bookshop.model.entity.User;

public interface ShoppingCartService {

	public ShoppingCart findById(ShoppingCartId id);
	public ShoppingCart findById(Long userId, Long bookId);
	public ShoppingCart create(User user, Book book, int copies);
	public ShoppingCart create(Long userId, Long bookId, int copies);
	public ShoppingCart update(ShoppingCart cart);
	public List<ShoppingCart> findUserShoppingCart(User user);
	public void removeBook(ShoppingCart cart);
	public void removeBook(Long userId, Long bookId);
	public void emptyUserCart(User user);
	
}
