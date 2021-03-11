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

@Transactional
@Repository("orderDao") //@Repository  is a specialization of @Component
public class OrderDaoDefault extends DefaultDao implements OrderDao {


	@Autowired
	BookOrderDao bookOrderRepository;
	
	@Override
	public Order findById(Long id) {
		return getSession().find(Order.class, id);
	}


	@Override
	public Order create(User buyer, LocalDateTime date, Set<BookOrder> books, String shipmentAddress, String payment, double shipmentCost) {
		Order o = new Order();
		o.setBuyer(buyer);
		o.setDate(date);
		o.setShipmentAddress(shipmentAddress);
		o.setPayment(payment);
		o.setShipmentCost(shipmentCost);
		o.setBooks(books); 
		o.setTotalExpense(buyer.getCartTotalPrice() + shipmentCost);
		Long id = (Long) getSession().save(o);
		//For some reasong bookOrder set is not getting persisted
		//So we must save them manually
		o = findById(id);
		for(BookOrder b: books) {
			bookOrderRepository.create(o, b);
		}
		return o;
	}
	
	@Override
	public Order create(User buyer, LocalDateTime date, Set<BookOrder> books, String shipmentAddress, String payment, double shipmentCost, int coupon_discount) {
		Order o = new Order();
		o.setBuyer(buyer);
		o.setDate(date);
		o.setShipmentAddress(shipmentAddress);
		o.setPayment(payment);
		o.setBooks(books);
		double coupon_saving = buyer.getCartTotalPrice()*(double)(coupon_discount/100.00f);
		o.setTotalExpense(buyer.getCartTotalPrice() + shipmentCost - coupon_saving);
		o.setShipmentCost(shipmentCost);
		Long id = (Long) getSession().save(o);
		//Per qualche ragione il set di BookOrder non viene reso persistente
		//Quindi bisogna farlo manualmente
		o = findById(id);
		for(BookOrder b: books) {
			bookOrderRepository.create(o, b);
		}
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

	@Override
	public void delete(Order order) {
		getSession().delete(order);
	}

	@Override
	public List<Order> findUserOrders(User user) {
		return getSession().createQuery("FROM Order o WHERE o.buyer = :user ORDER BY o.date DESC", Order.class)
				.setParameter("user", user).getResultList();
	}


}
