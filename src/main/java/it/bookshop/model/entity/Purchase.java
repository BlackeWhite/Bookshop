package it.bookshop.model.entity;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "PURCHASES")
public class Purchase {
	
	
	private PurchaseId id;
	private User buyer;
	private User seller;
	private Book book;
	private double total_price;
	private int copies;

	@EmbeddedId
	public PurchaseId getId() {
		return id;
	}
	public void setId(PurchaseId id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="BUYER_ID", referencedColumnName="ID_USER")
	@MapsId("buyerId")
	public User getBuyer() {
		return buyer;
	}
	public void setBuyer(User buyer) {
		this.buyer = buyer;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="SELLER_ID", referencedColumnName="ID_USER")
	@MapsId("sellerId")
	public User getSeller() {
		return seller;
	}
	public void setSeller(User seller) {
		this.seller = seller;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="BOOK_ISBN", referencedColumnName="ISBN")
	@MapsId("bookIsbn")
	public Book getBook() {
		return book;
	}
	public void setBook(Book book) {
		this.book = book;
	}

	@Column(name = "TOTAL_PRICE")
	public double getTotal_price() {
		return total_price;
	}
	public void setTotal_price(double total_price) {
		this.total_price = total_price;
	}
	
	@Column(name = "COPIES")
	public int getCopies() {
		return copies;
	}
	public void setCopies(int copies) {
		this.copies = copies;
	}
	
	@Transient
	public Date getDate() {
		return id.getDate();
	}
	
}
