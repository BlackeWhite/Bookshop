package it.bookshop.model.dao;


import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;

import org.springframework.stereotype.Repository;

import it.bookshop.model.entity.Book;
import it.bookshop.model.entity.BookOrder;
import it.bookshop.model.entity.BookOrderId;
import it.bookshop.model.entity.Order;

@Repository("bookOrderDao")
public class BookOrderDaoDefault extends DefaultDao implements BookOrderDao {

	@Override
	public BookOrder create(Order order, BookOrder b) {
		BookOrderId id = new BookOrderId(order.getId(), b.getBook().getId());
		b.getBook().setCopies(b.getBook().getCopies() - b.getCopies()); //aggiornamento copie disponibili
		b.getBook().setSoldCopies(b.getBook().getSoldCopies() + b.getCopies()); //aggiornamento copie vendute 
		b.setId(id);
		b.setOrder(order);
		getSession().save(b);
		return b;
	}
	
	
	@Override
	public List<BookOrder> findbyId(long id) {
		// cerca per bookid nella lista dei libri acquistati 
		List<BookOrder> bo  = this.getSession().createQuery("select bo FROM BookOrder bo where bo.id.bookId=:id",BookOrder.class).setParameter("id", id).getResultList();
		return bo;
	}
	
	@Override
	public List<BookOrder> findbyDate(String data_da, String data_a){
		// cerca per bookid nella lista dei libri acquistati 
		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
		java.util.Date parseda = null;
		java.util.Date parsea = null;
		try {
			parseda = format.parse(data_da);
		} catch (ParseException e) {
			e.printStackTrace();
		}
        Date sql_data_da = new Date(parseda.getTime());
		try {
			parsea = format.parse(data_a);
		} catch (ParseException e) {
			e.printStackTrace();
		}
        Date sql_data_a = new Date(parsea.getTime());
		List<BookOrder> bo  = this.getSession().createQuery("select bo FROM BookOrder bo where bo.purchasedate BETWEEN :startDate AND :endDate",BookOrder.class).setParameter("startDate", sql_data_da).setParameter("endDate", sql_data_a).getResultList();
		return bo;
	}
	
	@Override
	public double sumPrice(long id) {
		// calcola l'incasso per quel libro 
		
		List<BookOrder> bolist  = this.getSession().createQuery("select bo FROM BookOrder bo where bo.id.bookId=:id",BookOrder.class).setParameter("id", id).getResultList();
		Iterator <BookOrder> itbo = bolist.iterator();
		double sum = 0;
		while(itbo.hasNext()) {
			BookOrder bo = itbo.next();
			sum += bo.getCopies()*(bo.getPricenovat()); 
		}
		return sum;
	}

	@Override
	public void delete(BookOrder b) {
		getSession().delete(b);
	}

	@Override
	public void deleteOrderBookOrders(Order o) {
		getSession().createQuery("DELETE FROM BookOrder o WHERE o.order = :order").setParameter("order", o)
		.executeUpdate();
	}

	@Override
	public void deleteBookBookOrders(Book b) {
		getSession().createQuery("DELETE FROM BookOrder o WHERE o.book = :book").setParameter("book", b)
		.executeUpdate();
	}
	

}
