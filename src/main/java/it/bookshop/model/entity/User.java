package it.bookshop.model.entity;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.JoinColumn;
import javax.persistence.Embedded;
import java.io.Serializable;

@Entity
@Table(name = "USERS")
public class User implements Serializable{
	
	/**
	 *
	 */
	// perché viene sempre 1L?
	private static final long serialVersionUID = 1L;
	private long userID;
	private String username;
	private String password;
	private String email;
	private boolean enabled;
	private Set<Role> roles = new HashSet<Role>();
	private Set<Purchase> sales = new HashSet<Purchase>(); //Reserved for seller side
	private Set<Offer> offers = new HashSet<Offer>(); //Reserved for seller side
	private Set<Purchase> purchases = new HashSet<Purchase>();

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
	// (unique = true è una shortcut di UniqueConstrain)
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
					name = "username", referencedColumnName = "username"),
			inverseJoinColumns = @JoinColumn(
					name = "ROLE_ID", referencedColumnName = "id"))
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
				CascadeType.PERSIST,
				CascadeType.REMOVE},
				mappedBy="buyer")
	public Set<Purchase> getPurchases() {
		return purchases;
	}
	public void setPurchases(Set<Purchase> purchases) {
		this.purchases = purchases;
	}

	
	
	@OneToMany(cascade = { CascadeType.DETACH,
				CascadeType.MERGE,
				CascadeType.REFRESH,
				CascadeType.PERSIST,},
				mappedBy="seller")
	public Set<Purchase> getSales() {
		return sales;
	}
	public void setSales(Set<Purchase> sales) {
		this.sales = sales;
	}

	
	@OneToMany(cascade = { CascadeType.DETACH,
				CascadeType.MERGE,
				CascadeType.REFRESH,
				CascadeType.PERSIST,
				CascadeType.REMOVE},
				mappedBy="seller")
	public Set<Offer> getOffers() {
		return offers;
	}
	public void setOffers(Set<Offer> offers) {
		this.offers = offers;
	}

	
	public void setPersonalData(PersonalData personalData) {
		this.personalData = personalData;
	}
	public PersonalData getPersonalData() {
		return personalData;
	}


}
