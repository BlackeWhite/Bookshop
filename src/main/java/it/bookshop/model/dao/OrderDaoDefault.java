package it.bookshop.model.dao;

import java.sql.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import it.bookshop.model.entity.BookOrder;
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
	public Order create(User buyer, double totalExpense, Date date) {
		Order p = new Order();
		p.setBuyer(buyer);
		p.setTotalExpense(totalExpense);
		p.setDate(date);
		getSession().save(p);
		return p;
	}
	
	@Override
	public Order create(User buyer, double totalExpense, Date date, List<BookOrder> books, String payment) {
		Order p = new Order();
		p.setBuyer(buyer);
		p.setTotalExpense(totalExpense);
		p.setDate(date);
		Set<BookOrder> s = new HashSet<BookOrder>(books);
		p.setBooks(s);
		p.setPayment(payment);
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
	public List<Order> findUserOrders(Long buyerId) {
		return getSession().createQuery("FROM Order o WHERE o.buyer = :id", Order.class)
				.setParameter("id", buyerId).getResultList();
	}


}
