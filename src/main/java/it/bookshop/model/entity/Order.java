package it.bookshop.model.entity;


import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

@Entity
@Table(name = "ORDERS")
public class Order {
	
	private Long id;
	private User buyer;
	private LocalDateTime date;
	private String shipmentAddress;
	private String payment;
	private Set<BookOrder> books = new HashSet<BookOrder>();
	private double totalExpense;
	private double shipmentCost;

    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="BUYER_ID", referencedColumnName="USER_ID")
	public User getBuyer() {
		return buyer;
	}
	public void setBuyer(User buyer) {
		this.buyer = buyer;
	}
	
	@Column(name="DATE")
	public LocalDateTime getDate() {
		return date;
	}
	public void setDate(LocalDateTime date) {
		this.date = date;
	}
	
	//Perché il normale dateTime inserisce una T tra la data e l'ora
	@Transient
	public String getFormattedDate() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		return date.format(formatter);
	}
	
	@Column(name="SHIP_ADDRESS")
	public String getShipmentAddress() {
		return shipmentAddress;
	}
	public void setShipmentAddress(String shipmentAddress) {
		this.shipmentAddress = shipmentAddress;
	}
	
	@Column(name="PAYMENT")
	public String getPayment() {
		return payment;
	}
	public void setPayment(String payment) {
		this.payment = payment;
	}
	
	@OneToMany(fetch = FetchType.EAGER,
			mappedBy="order")
	@Cascade(value = {CascadeType.DETACH,
			CascadeType.MERGE,
			CascadeType.REFRESH,
			CascadeType.REMOVE,
			CascadeType.SAVE_UPDATE})
	public Set<BookOrder> getBooks() {
		return books;
	}
	public void setBooks(Set<BookOrder> books) {
		this.books = books;
	}
	public void addBook(BookOrder book) {
		books.add(book);
		book.setOrder(this);
	}
	
	@Column(name = "TOTAL_EXPENSE")
	public double getTotalExpense() {
		return totalExpense;
	}
	public void setTotalExpense(double totalExpense) {
		this.totalExpense = totalExpense;
	}
	
	@Transient
	public String getFormattedTotalExpense() {
		NumberFormat formatter = NumberFormat.getCurrencyInstance();
		return formatter.format(totalExpense);
	}
	
	@Column(name = "SHIPMENT_COST")
	public double getShipmentCost() {
		return shipmentCost;
	}
	public void setShipmentCost(double shipmentCost) {
		this.shipmentCost = shipmentCost;
	}
	
	@Transient
	public String getFormattedShipmentCost() {
		NumberFormat formatter = NumberFormat.getCurrencyInstance();
		return formatter.format(shipmentCost);
	}
	
}
