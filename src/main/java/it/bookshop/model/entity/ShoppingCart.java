package it.bookshop.model.entity;

import java.text.NumberFormat;
//import java.time.LocalDateTime;

import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "SHOPPING_CARTS")
public class ShoppingCart {
	
	private ShoppingCartId id;
	private User user;
	private Book book;
	private int copies;
	//private LocalDateTime creationTime;
	
	@EmbeddedId
	public ShoppingCartId getId() {
		return id;
	}
	public void setId(ShoppingCartId id) {
		this.id = id;
	}
	
	@ManyToOne(cascade = { CascadeType.MERGE,
				CascadeType.REFRESH,
				CascadeType.PERSIST},
				fetch = FetchType.EAGER)
	@JoinColumn(name="USER_ID", referencedColumnName="USER_ID")
	@MapsId("userId")
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	
	@ManyToOne(cascade = { CascadeType.MERGE,
				CascadeType.REFRESH,
				CascadeType.PERSIST},
				fetch = FetchType.EAGER)
	@JoinColumn(name="BOOK_ID", referencedColumnName="ID")
	@MapsId("bookId")
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
	
	/*
	@Column(name = "CREATION_TIME")
	public LocalDateTime getCreationTime() {
		return creationTime;
	}
	public void setCreationTime(LocalDateTime creationTime) {
		this.creationTime = creationTime;
	}
	*/
	
	@Transient
	public double getElementTotalPrice() {
		return book.getDiscountedPrice() * copies;
	}
	
	@Transient
	public double getElementSavedMoney() {
		return (book.getPriceWithVat()-book.getDiscountedPrice()) * copies;
	}
	
	@Transient
	public String getFormattedElementTotalPrice() {
		double totalPrice = book.getDiscountedPrice() * copies;
		NumberFormat formatter = NumberFormat.getCurrencyInstance();
		return formatter.format(totalPrice);  
	}
	
}
