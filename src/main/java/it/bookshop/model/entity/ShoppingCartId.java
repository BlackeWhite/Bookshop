package it.bookshop.model.entity;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

//This class is used to define the composite key of the ShoppingCart entity (relationship table)
@Embeddable
public class ShoppingCartId implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long userId;
	private Long bookId;
	
	@SuppressWarnings("unused")
	private ShoppingCartId() {}

	public ShoppingCartId(Long userId, Long bookId) {
		this.userId = userId;
		this.bookId = bookId;
	}

	@Column(name = "USER_ID")
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}

	@Column(name = "BOOK_ID")
	public Long getBookID() {
		return bookId;
	}
	public void setBookID(Long bookId) {
		this.bookId = bookId;
	}
	
	//This type of embeddable must override equals and hashCode
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
			
		ShoppingCartId t = (ShoppingCartId) o;
		return Objects.equals(this.bookId, t.bookId) &&
				Objects.equals(this.userId,t.userId);
	}
		
	@Override
	public int hashCode() {
		return Objects.hash(userId, bookId);
	}
}
