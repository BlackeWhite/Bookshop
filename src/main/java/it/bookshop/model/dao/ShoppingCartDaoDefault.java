package it.bookshop.model.dao;

//import java.time.LocalDateTime;
import java.util.List;

//import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Repository;


import it.bookshop.model.entity.Book;
import it.bookshop.model.entity.ShoppingCart;
import it.bookshop.model.entity.ShoppingCartId;
import it.bookshop.model.entity.User;


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
		//s.setCreationTime(LocalDateTime.now());
		getSession().save(s);
		return s;
	}

	@Override
	public ShoppingCart update(ShoppingCart cart) {
		//cart.setCreationTime(LocalDateTime.now());
		return (ShoppingCart) getSession().merge(cart);
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

	/*
	 * //Every 15 minutes
		@Scheduled(fixedDelay = 900000)
		public void scheduleCleaningTask() {
			LocalDateTime threshold = LocalDateTime.now().minusMinutes(30);
			int i = getSession().createQuery("DELETE FROM ShoppingCart o WHERE o.creationTime < :date")
					.setParameter("date", threshold).executeUpdate();
			System.out.println("Deleted " + String.valueOf(i) + "ShoppingCart rows");
		}
	 */
		

}
