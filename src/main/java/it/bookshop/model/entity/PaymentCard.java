package it.bookshop.model.entity;

import java.io.Serializable;
import java.sql.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;

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
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="PAYMENT_METHODS")
public class PaymentCard implements Serializable{
	
	private static final long serialVersionUID = 1L;

	private long id;
	private String type; //Visa, MasterCard, 
	private String number;
	private Date expirationDate;
	private User user;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	
	@Column(name = "TYPE")
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	@Column(name = "NUMBER")
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	
	@Column(name = "EXPIRATION_DATE")
	public Date getExpirationDate() {
		return expirationDate;
	}
	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}
	
	@ManyToOne(cascade = { CascadeType.MERGE,
			CascadeType.REFRESH,
			CascadeType.PERSIST},
			fetch = FetchType.EAGER)
	@JoinColumn(name="USER_ID", referencedColumnName="USER_ID")
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	
	@Transient
	public String getHiddenNumber() {
		return "XXXX-XXXX-XXXX-"+number.substring(12, 16);
	}
	
	@Transient
	public String getShortExpirationDate() {
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(expirationDate);
		String year = String.valueOf(calendar.get(Calendar.YEAR));
		int month_n = calendar.get(Calendar.MONTH) + 1;
		String month = "";
		if(month_n < 10) {
			month = "0"+String.valueOf(month_n);
		} else month = String.valueOf(month_n);
		
		return month+"/"+year.substring(2,4);
		
	}
	
	
}
