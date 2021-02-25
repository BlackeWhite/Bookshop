package it.bookshop.model.dao;


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
		BookOrder b = new BookOrder();
		BookOrderId id = new BookOrderId(order.getId(), book.getId());
		b.setId(id);
		b.setOrder(order);
		b.setBook(book);
		book.setCopies(book.getCopies()-copies); //aggiornamento copie disponibili 
		b.setCopies(copies);
		b.setPrice(book.getDiscountedPrice());
		getSession().save(b);
		return b;
	}

	@Override
	public BookOrder create(Order order, BookOrder b) {
		BookOrderId id = new BookOrderId(order.getId(), b.getBook().getId());
		b.getBook().setCopies(b.getBook().getCopies() - b.getCopies()); //aggiornamento copie disponibili
		b.getBook().setSoldCopies(b.getBook().getCopies() + b.getCopies());
		b.setId(id);
		b.setOrder(order);
		getSession().save(b);
		return b;
	}
	

}
