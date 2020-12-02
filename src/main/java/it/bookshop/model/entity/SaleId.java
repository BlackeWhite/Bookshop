package it.bookshop.model.entity;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

//This class is used to define the composite key of the Purchase entity (relationship table)
@Embeddable
public class SaleId implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long sellerId;
	private String bookIsbn;
	
	private SaleId() {}

	public SaleId(Long sellerId, String bookIsbn) {
		this.sellerId = sellerId;
		this.bookIsbn = bookIsbn;
	}

	@Column(name = "SELLER_ID")
	public Long getSellerId() {
		return sellerId;
	}
	public void setSellerId(Long sellerId) {
		this.sellerId = sellerId;
	}

	@Column(name = "BOOK_ISBN")
	public String getBookIsbn() {
		return bookIsbn;
	}
	public void setBookIsbn(String bookIsbn) {
		this.bookIsbn = bookIsbn;
	}
	
	//This type of embeddable must override equals and hashCode
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
			
		SaleId t = (SaleId) o;
		return Objects.equals(this.bookIsbn, t.bookIsbn) &&
				Objects.equals(this.sellerId,t.sellerId);
	}
		
	@Override
	public int hashCode() {
		return Objects.hash(sellerId, bookIsbn);
	}
}
