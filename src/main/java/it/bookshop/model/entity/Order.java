package it.bookshop.model.entity;

import java.sql.Date;
import java.text.NumberFormat;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "ORDERS")
public class Order {
	
	private Long id;
	private User buyer;
	private Date date;
	private String shipmentAddress;
	private String payment;
	private Set<BookOrder> books = new HashSet<BookOrder>();
	private double totalExpense;

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
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
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
	
	@OneToMany(cascade = { CascadeType.DETACH,
			CascadeType.MERGE,
			CascadeType.REFRESH,
			CascadeType.PERSIST,
			CascadeType.REMOVE},
			mappedBy="order")
	public Set<BookOrder> getBooks() {
		return books;
	}
	public void setBooks(Set<BookOrder> books) {
		this.books = books;
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
	
}
