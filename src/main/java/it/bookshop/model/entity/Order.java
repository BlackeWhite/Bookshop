package it.bookshop.model.entity;

import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "ORDERS")
public class Order {
	
	
	private Long id;
	private User buyer;
	private Date date;
	private Set<BookOrder> books = new HashSet<BookOrder>();
	private double total_expense;


	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="BUYER_ID", referencedColumnName="USER_ID")
	public User getBuyer() {
		return buyer;
	}
	public void setBuyer(User buyer) {
		this.buyer = buyer;
	}
	

	@Column(name = "TOTAL_EXPENSE")
	public double getTotal_expense() {
		return total_expense;
	}
	public void setTotal_expense(double total_expense) {
		this.total_expense = total_expense;
	}
	
	
	@Column(name="DATE")
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
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
	
}
