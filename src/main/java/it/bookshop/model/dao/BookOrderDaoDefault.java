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
		b.setCopies(copies);
		b.setPrice(book.getPrice());
		getSession().save(b);
		return b;
	}
	

}
