package it.bookshop.model.entity;

import java.sql.Date;
import java.text.NumberFormat;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;


//Entity that implements the relationship between Order and Book
@Entity
@Table(name="BOOK_ORDERS")
public class BookOrder {
	
	private BookOrderId id = new BookOrderId();
	private Order order;
	private Book book;
	private int copies;
	private Date purchasedate;
	private double price; //Price at checkout
	private double pricenovat; //Price at checkout without vat
	
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
	
	@Column(name="COPIES")
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
	@Column(name="PRICE_NO_VAT")
	public double getPricenovat() {
		return pricenovat;
	}
	public void setPricenovat(double pricenovat) {
		this.pricenovat = pricenovat;
	}
	
	@Transient
	public String getFormattedPrice() {
		NumberFormat formatter = NumberFormat.getCurrencyInstance();
		return formatter.format(price);
	}
	
	@Column(name="PURCHASE_DATE")
	public Date getPurchasedate() {
		return purchasedate;
	}
	public void setPurchasedate(Date purchasedate) {
		this.purchasedate = purchasedate;
	}
	
	
	
}
