package it.bookshop.model.dao;

import java.util.List;

import org.hibernate.Session;

import it.bookshop.model.entity.Book;
import it.bookshop.model.entity.ShoppingCart;
import it.bookshop.model.entity.ShoppingCartId;
import it.bookshop.model.entity.User;

public interface ShoppingCartDao {

	public Session getSession();
	public void setSession(Session session);
	
	public ShoppingCart findById(ShoppingCartId id);
	public ShoppingCart create(User user, Book book, int copies);
	public ShoppingCart update(ShoppingCart cart);
	public List<ShoppingCart> findUserShoppingCart(Long userId);
	public void removeBook(ShoppingCart cart);
	public void emptyUserCart(Long userId);
	
}
