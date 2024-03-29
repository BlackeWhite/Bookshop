package it.bookshop.model.dao;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import it.bookshop.model.entity.BookOrder;
import it.bookshop.model.entity.Order;
import it.bookshop.model.entity.User;

@Repository("orderDao")
public class OrderDaoDefault extends DefaultDao implements OrderDao {


	@Autowired
	BookOrderDao bookOrderRepository;
	
	@Override
	public Order findById(Long id) {
		return getSession().find(Order.class, id);
	}

	//Solo per i JUnit test
	@Override
	public Order createTest() {
		Order o = new Order();
		getSession().save(o);
		return o;
	}
	
	@Override
	public Order create(User buyer, LocalDateTime date, Set<BookOrder> books, String shipmentAddress, String payment, double shipmentCost) {
		Order o = new Order();
		o.setBuyer(buyer);
		o.setDate(date);
		o.setShipmentAddress(shipmentAddress);
		o.setPayment(payment);
		o.setShipmentCost(shipmentCost); 
		o.setTotalExpense(buyer.getCartTotalPrice() + shipmentCost);
		o.setBooks(books);
		for(BookOrder b : books) {
			b.setOrder(o);
			b.getBook().setCopies(b.getBook().getCopies() - b.getCopies()); //aggiornamento copie disponibili
			b.getBook().setSoldCopies(b.getBook().getSoldCopies() + b.getCopies()); //aggiornamento copie vendute 
			getSession().update(b.getBook());
		}
		getSession().save(o);

		return o;
	}
	
	@Override
	public Order create(User buyer, LocalDateTime date, Set<BookOrder> books, String shipmentAddress, String payment, double shipmentCost, int coupon_discount) {
		Order o = new Order();
		o.setBuyer(buyer);
		o.setDate(date);
		o.setShipmentAddress(shipmentAddress);
		o.setPayment(payment);
		double coupon_saving = buyer.getCartTotalPrice()*(double)(coupon_discount/100.00f);
		o.setTotalExpense(buyer.getCartTotalPrice() + shipmentCost - coupon_saving);
		o.setShipmentCost(shipmentCost);
		o.setBooks(books);
		for(BookOrder b : books) {
			b.setOrder(o);
			b.getBook().setCopies(b.getBook().getCopies() - b.getCopies()); //aggiornamento copie disponibili
			b.getBook().setSoldCopies(b.getBook().getSoldCopies() + b.getCopies()); //aggiornamento copie vendute 
			getSession().update(b.getBook());
		}
		getSession().save(o);
		
		return o;
	}
	
	@Override
	public Order update(Order order) {
		return (Order) getSession().merge(order);
	}

	@Override
	public List<Order> findAll() {
		return getSession().createQuery("FROM Order o", Order.class).getResultList();
	}

	//Utilizzato solo nel JUnit test perch� non � prevista l'eliminazione degli ordini
	@Transactional
	@Override
	public void delete(Order order) {
		
		bookOrderRepository.deleteOrderBookOrders(order);
	
		getSession().delete(order);
	}

	//Cerca e ordina per data decrescente gli ordini di un particolare utente
	@Override
	public List<Order> findUserOrders(User user) {
		return getSession().createQuery("FROM Order o WHERE o.buyer = :user ORDER BY o.date DESC", Order.class)
				.setParameter("user", user).getResultList();
	}


}
