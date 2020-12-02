package it.bookshop.model.entity;

import java.io.Serializable;
import java.sql.Date;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

//This class is used to define the composite key of the Purchase entity (relationship table)
@Embeddable
public class PurchaseId implements Serializable{
	
	private static final long serialVersionUID = 1L;

	private Long buyerId;
	
	private Long sellerId;
	
	private String bookIsbn;
	
	private Date date;
	
	private PurchaseId() {}
	
	public PurchaseId(Long buyerId, Long sellerId, String bookIsbn, Date date) {
		this.buyerId = buyerId;
		this.sellerId = sellerId;
		this.bookIsbn = bookIsbn;
		this.date = date;
	}
	

	@Column(name = "BUYER_ID")
	public Long getBuyerId() {
		return buyerId;
	}
	public void setBuyerId(Long buyerId) {
		this.buyerId = buyerId;
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

	@Column(name = "DATE")
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	
	//This type of embeddable must override equals and hashCode
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		
		PurchaseId t = (PurchaseId) o;
		return Objects.equals(this.buyerId, t.buyerId) &&
				Objects.equals(this.sellerId,t.sellerId) &&
				Objects.equals(this.bookIsbn, t.bookIsbn) &&
				Objects.equals(this.date, t.date);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(buyerId, sellerId, bookIsbn, date);
	}
	
}
