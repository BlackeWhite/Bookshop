package it.bookshop.model.entity;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

@Entity
@Table(name = "OFFERS")
public class Offer {
	
	private OfferId id;
	private User seller;
	private Book book;
	private int copies;
	private double price;
	
	@EmbeddedId
	public OfferId getId() {
		return id;
	}
	public void setId(OfferId id) {
		this.id = id;
	}
	
	@ManyToOne
	@MapsId("sellerId")
	public User getSeller() {
		return seller;
	}
	public void setSeller(User seller) {
		this.seller = seller;
	}
	
	@ManyToOne
	@MapsId("bookIsbn")
	public Book getBook() {
		return book;
	}
	public void setBook(Book book) {
		this.book = book;
	}
	
	@Column(name = "COPIES")
	public int getCopies() {
		return copies;
	}
	public void setCopies(int copies) {
		this.copies = copies;
	}
	
	@Column(name = "PRICE")
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
}
