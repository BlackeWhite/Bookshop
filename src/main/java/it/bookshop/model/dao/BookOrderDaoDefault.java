package it.bookshop.model.dao;


import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import it.bookshop.model.entity.Book;
import it.bookshop.model.entity.BookOrder;
import it.bookshop.model.entity.BookOrderId;
import it.bookshop.model.entity.Order;

@Transactional
@Repository("bookOrderDao")
public class BookOrderDaoDefault extends DefaultDao implements BookOrderDao {

	@Override
	public BookOrder create(Order order, Book book, int copies) {
		Date data_buy = new Date(Calendar.getInstance().getTime().getTime());
		BookOrder b = new BookOrder();
		BookOrderId id = new BookOrderId(order.getId(), book.getId());
		b.setId(id);
		b.setOrder(order);
		b.setBook(book);
		book.setCopies(book.getCopies()-copies); //aggiornamento copie disponibili 
		book.setCopies(book.getSoldCopies()+copies); //aggiornamento copie vendute 
		b.setCopies(copies);
		b.setPrice(book.getDiscountedPrice());
		b.setPurchasedate(data_buy);
		b.setPricenovat(book.getDiscountedPriceNoVat());
		getSession().save(b);
		return b;
	}

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
	

}
