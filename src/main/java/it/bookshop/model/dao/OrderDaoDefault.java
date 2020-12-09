package it.bookshop.model.dao;

import java.sql.Date;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import it.bookshop.model.entity.Book;
import it.bookshop.model.entity.Order;
import it.bookshop.model.entity.User;

@Transactional
@Repository("orderDao") //@Repository  is a specialization of @Component
public class OrderDaoDefault extends DefaultDao implements OrderDao {


	@Override
	public Order findById(Long id) {
		return getSession().find(Order.class, id);
	}

	@Override
	public Order create(User buyer, User seller, Book book, int copies, double total_price,Date date) {
		Order p = new Order();
		p.setId(id);
		p.setBuyer(buyer);
		p.setSeller(seller);
		p.setBook(book);
		p.setCopies(copies);
		p.setTotal_price(total_price);
		getSession().save(p);
		return p;
	}

	@Override
	public Order update(Order order) {
		return (Order) getSession().merge(order);
	}

	@Override
	public List<Order> findAll() {
		return getSession().createQuery("FROM Order o", Order.class).getResultList();
	}

	@Override
	public void delete(Order order) {
		getSession().delete(order);
	}

	@Override
	public List<Order> findBuyerPurchases(Long buyerId) {
		return getSession().createQuery("FROM Order o WHERE o.buyer = :id", Order.class)
				.setParameter("id", buyerId).getResultList();
	}

	@Override
	public List<Order> findSellerSales(Long sellerId) {
		return getSession().createQuery("FROM Order o WHERE o.seller = :id", Order.class)
				.setParameter("id", sellerId).getResultList();
	}

}
