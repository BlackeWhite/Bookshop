package it.bookshop.model.entity;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;


//Entity that implements the relationship between Order and Book
@Entity
@Table(name="BOOK_ORDERS")
public class BookOrder {
	
	private BookOrderId id;
	private Order order;
	private Book book;
	
	@EmbeddedId
	public BookOrderId getId() {
		return id;
	}
	public void setId(BookOrderId id) {
		this.id = id;
	}
	
	@ManyToOne
	@JoinColumn(name="ORDER_ID", referencedColumnName="ID")
	@MapsId("orderId")
	public Order getOrder() {
		return order;
	}
	public void setOrder(Order order) {
		this.order = order;
	}
	
	@ManyToOne
	@JoinColumn(name="BOOK_ID", referencedColumnName="ID")
	@MapsId("bookId")
	public Book getBook() {
		return book;
	}
	public void setBook(Book book) {
		this.book = book;
	}
}
