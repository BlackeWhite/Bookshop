package it.bookshop.model.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.JoinColumn;
import javax.persistence.Embedded;
import java.io.Serializable;
import java.text.NumberFormat;

@Entity
@Table(name = "USERS")
public class User implements Serializable{
	
	/**
	 *
	 */
	// perch� viene sempre 1L?
	private static final long serialVersionUID = 1L;
	private long userID;
	private String username;
	private String password;
	private String email;
	private boolean enabled;
	private Set<Role> roles = new HashSet<Role>();
	private Set<Order> orders = new HashSet<Order>();
	private Set<Book> booksForSale = new HashSet<Book>();
	private Set<ShoppingCart> shoppingCart = new HashSet<ShoppingCart>();
	private Set<PaymentCard> paymentCards = new HashSet<PaymentCard>();
	private Set<Coupon> coupons = new HashSet<Coupon>();

    @Embedded
    private PersonalData personalData;

	// Per la generazione dell'id si adotta il tipo ".IDENTITY": 
	// crea un campo auto-incrementale 
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY) 
	@Column(name = "USER_ID")
	public long getUserID() {
		return userID;
	}
	public void setUserID(long userID) {
		this.userID = userID;
	}
	
	// Nella colonna USERNAME non possono esserci duplicati
	// (unique = true � una shortcut di UniqueConstrain)
	@Column(name="USERNAME", unique = true)
	public void setUsername(String username) {
		this.username = username;
	}
	public String getUsername() {
		return this.username;
	}	
	
	@Column(name="EMAIL", unique = true)
	public void setEmail(String email) {
		this.email = email;
	}
	public String getEmail() {
		return email;
	}
	
	@Column(name="PASSWORD", nullable = false)
	public void setPassword(String password) {
		this.password = password;
	}
	public String getPassword() {
		return this.password;
	}	
	
	@Column(name="ENABLED", nullable = false)
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	public boolean isEnabled() {
		return this.enabled;
	}	
	
	@ManyToMany
	@JoinTable(
			name = "USERS_ROLES",
			joinColumns = @JoinColumn(
					name = "USER_ID", referencedColumnName = "USER_ID"),
			inverseJoinColumns = @JoinColumn(
					name = "ROLE_ID", referencedColumnName = "ID"))
	public Set<Role> getRoles(){
		return this.roles;
	}	
	
	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}
	
	public void addRole(Role role) {
		if (this.roles == null) {
			this.roles = new HashSet<Role>();
		}
		
		this.roles.add(role);
	}	
	
	@OneToMany(cascade = { CascadeType.DETACH,
				CascadeType.MERGE,
				CascadeType.REFRESH,
				CascadeType.PERSIST},
				mappedBy="buyer",
				fetch = FetchType.EAGER)
	public Set<Order> getOrders() {
		return orders;
	}
	public void setOrders(Set<Order> orders) {
		this.orders = orders;
	}

	
	@OneToMany(cascade = { CascadeType.DETACH,
				CascadeType.MERGE,
				CascadeType.REFRESH,
				CascadeType.PERSIST,
				CascadeType.REMOVE},
				mappedBy="seller")
	public Set<Book> getBooksForSale() {
		return booksForSale;
	}
	public void setBooksForSale(Set<Book> booksForSale) {
		this.booksForSale = booksForSale;
	}
	
	@OneToMany(cascade = { CascadeType.DETACH,
			CascadeType.MERGE,
			CascadeType.REFRESH,
			CascadeType.PERSIST,
			CascadeType.REMOVE},
			mappedBy="user",
			fetch = FetchType.EAGER)
	public Set<ShoppingCart> getShoppingCart() {
		return shoppingCart;
	}
	public void setShoppingCart(Set<ShoppingCart> shoppingCart) {
		this.shoppingCart = shoppingCart;
	}

	
	public void setPersonalData(PersonalData personalData) {
		this.personalData = personalData;
	}
	public PersonalData getPersonalData() {
		return personalData;
	}
	
	
	@OneToMany(cascade = { CascadeType.DETACH,
			CascadeType.MERGE,
			CascadeType.REFRESH,
			CascadeType.PERSIST,
			CascadeType.REMOVE},
			mappedBy="user",
			fetch = FetchType.EAGER)
	public Set<PaymentCard> getPaymentCards() {
		return paymentCards;
	}
	public void setPaymentCards(Set<PaymentCard> paymentCards) {
		this.paymentCards = paymentCards;
	}
	public void addPaymentCard(PaymentCard card) {
		paymentCards.add(card);
		card.setUser(this);
	}
	
	@ManyToMany
	@JoinTable(
			name = "USERS_COUPONS",
			joinColumns = @JoinColumn(
					name = "USER_ID", referencedColumnName = "USER_ID"),
			inverseJoinColumns = @JoinColumn(
					name = "COUPON_ID", referencedColumnName = "ID"))
	public Set<Coupon> getCoupons(){
		return this.coupons;
	}
	public void setCoupons(Set<Coupon> coupons) {
		this.coupons = coupons;
	}

	
	@Transient
	public double getCartTotalPrice() {
		double total = 0;
		for (ShoppingCart c : shoppingCart) {
			total += c.getElementTotalPrice(); 
		}
		return total;
	}
	
	@Transient
	public int getCartTotalItems() {
		int total = 0;
		for (ShoppingCart c : shoppingCart) {
			total += c.getCopies(); 
		}
		return total;
	}
	
	@Transient
	public String getFormattedCartSubtotalPrice() {
		double total = 0;
		for (ShoppingCart c : shoppingCart) {
			total += c.getElementTotalPrice(); 
		}
		NumberFormat formatter = NumberFormat.getCurrencyInstance();
		return formatter.format(total);  
	}
	
	@Transient
	public String getFormattedCheckoutTotalPrice(/*int coupon*/) {
		double total = 0;
		for (ShoppingCart c : shoppingCart) {
			total += c.getElementTotalPrice(); 
		}
		if (total>0) {
		total += 5;} //costi di spedizione
		//total = total - (total/100*coupon);
		NumberFormat formatter = NumberFormat.getCurrencyInstance();
		return formatter.format(total);  
	}
	
	@Transient
	public String getFormattedSavedMoney() {
		double total = 0;
		for (ShoppingCart c : shoppingCart) {
			total += c.getElementSavedMoney(); 
		}
		NumberFormat formatter = NumberFormat.getCurrencyInstance();
		return formatter.format(total);  
	}

}
