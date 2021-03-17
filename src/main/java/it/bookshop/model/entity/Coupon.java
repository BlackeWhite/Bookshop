package it.bookshop.model.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import java.io.Serializable;
import java.sql.Date;
import javax.persistence.Column;


@Entity
@Table(name = "COUPON", uniqueConstraints= {@UniqueConstraint(columnNames= {"CODE"})})
public class Coupon implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private long couponID;
	private String code;
	private int discount;
	private Date expireDate; //Data di scadenza
	private long usageCounter; //Numero di utilizzi, visualizzato solo dall'admin
	
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY) 
	@Column(name = "ID")
	public long getCouponID() {
		return couponID;
	}
	public void setCouponID(long couponID) {
		this.couponID = couponID;
	}
	
	
	@Column(name="CODE", unique = true)
	public void setCode(String code) {
		this.code = code;
	}
	public String getCode() {
		return code;
	}
	
	
	@Column(name="DISCOUNT", nullable = false)
	public void setDiscount(int discount) {
		this.discount = discount;
	}
	public int getDiscount() {
		return discount;
	}
	
	
	@Column(name="EXPIRE_DATE")
	public void setExpireDate(Date expireDate) {
		this.expireDate = expireDate;
	}
	public Date getExpireDate() {
		return expireDate;
	}
	
	@Column(name="USAGE_COUNTER")
	public long getUsageCounter() {
		return usageCounter;
	}
	public void setUsageCounter(long usageCounter) {
		this.usageCounter = usageCounter;
	}
	
	/*for now this relationship is unidirectional 
	 * 
	@ManyToMany(mappedBy = "coupons")
	public Set<User> getBeneficiaries() {
		return beneficiaries;
	}
	public void setBeneficiaries(Set<User> beneficiaries) {
		this.beneficiaries = beneficiaries;
	}
	*/
	
}
