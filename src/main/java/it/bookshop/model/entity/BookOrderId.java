package it.bookshop.model.entity;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

//This class is used to define the composite key of the Purchase entity (relationship table)
@Embeddable
public class BookOrderId implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long orderId;
	private Long bookId;
	
	public BookOrderId() {}

	public BookOrderId(Long orderId, Long bookId) {
		this.orderId = orderId;
		this.bookId = bookId;
	}

	@Column(name = "ORDER_ID", insertable = false, updatable = false)
	public Long getOrderId() {
		return orderId;
	}
	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	@Column(name = "BOOK_ID", insertable = false, updatable = false)
	public Long getBookId() {
		return bookId;
	}
	public void setBookId(Long bookId) {
		this.bookId = bookId;
	}
	
	//This type of embeddable must override equals and hashCode
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
			
		BookOrderId t = (BookOrderId) o;
		return Objects.equals(this.bookId, t.bookId) &&
				Objects.equals(this.orderId,t.orderId);
	}
		
	@Override
	public int hashCode() {
		return Objects.hash(orderId, bookId);
	}
}
