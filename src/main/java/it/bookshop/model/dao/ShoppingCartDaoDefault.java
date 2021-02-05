package it.bookshop.model.dao;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import it.bookshop.model.entity.Book;
import it.bookshop.model.entity.ShoppingCart;
import it.bookshop.model.entity.ShoppingCartId;
import it.bookshop.model.entity.User;

@Transactional
@Repository("shoppingCartDao") // @Repository is a specialization of @Component
public class ShoppingCartDaoDefault extends DefaultDao implements ShoppingCartDao {

	@Override
	public ShoppingCart findById(ShoppingCartId id) {
		return getSession().find(ShoppingCart.class, id);
	}

	@Override
	public ShoppingCart create(User user, Book book, int copies) {
		ShoppingCart s = new ShoppingCart();
		ShoppingCartId id = new ShoppingCartId(user.getUserID(), book.getId());
		s.setId(id);
		s.setUser(user);
		s.setBook(book);
		s.setCopies(copies);
		getSession().save(s);
		return s;
	}

	@Override
	public ShoppingCart update(ShoppingCart offer) {
		return (ShoppingCart) getSession().merge(offer);
	}

	@Override
	public void removeBook(ShoppingCart cart) {
		getSession().delete(cart);
	}

	@Override
	public List<ShoppingCart> findUserShoppingCart(User user) {
		return getSession().createQuery("FROM ShoppingCart o WHERE o.user = :user", ShoppingCart.class)
				.setParameter("user", user).getResultList();
	}

	@Override
	public void emptyUserCart(User user) {
		int i = getSession().createQuery("DELETE FROM ShoppingCart o WHERE o.user = :user").setParameter("user", user)
				.executeUpdate();
		System.out.println("Deleted " + String.valueOf(i) + "ShoppingCart rows");
	}

}
