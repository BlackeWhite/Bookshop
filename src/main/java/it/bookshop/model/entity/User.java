package it.bookshop.model.entity;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
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

	// Per la generazione dell'id si adotta il tipo ".IDENTITY": 
	// crea un campo auto-incrementale 
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY) 
	@Column(name = "ID_USER")
	private long id_user;
	
	// Nella colonna USERNAME non possono esserci duplicati
	// (unique = true è una shortcut di UniqueConstrain)
	@Column(name="USERNAME", unique = true)
	private String username;
	
	@Column(name="EMAIL", unique = true)
	private String email;
	
	@Column(name="PASSWORD", nullable = false)
	private String password;
	
	@Column(name="ENABLED", nullable = false)
	private boolean enabled;
	
    @Embedded
    private PersonalData personalData;
	
	@ManyToMany
	@JoinTable(
			name = "USERS_ROLES",
			joinColumns = @JoinColumn(
					name = "username", referencedColumnName = "USERNAME"),
			inverseJoinColumns = @JoinColumn(
					name = "ROLE_ID", referencedColumnName = "id"))
	private Set<Role> roles = new HashSet<Role>();
	
	
	public void setUsername(String username) {
		this.username = username;
	}
	public String getUsername() {
		return this.username;
	}	
	
	public void setEmail(String email) {
		this.email = email;
	}
	public String getEmail() {
		return email;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	public String getPassword() {
		return this.password;
	}	
	
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	public boolean isEnabled() {
		return this.enabled;
	}	

	public void setPersonalData(PersonalData personalData) {
		this.personalData = personalData;
	}
	public PersonalData getPersonalData() {
		return personalData;
	}
	
	public Set<Role> roles() {
		return this.roles;
	}	
	public void addRole(Role role) {
		if (this.roles == null) {
			this.roles = new HashSet<Role>();
		}
		
		this.roles.add(role);
	}
	
	public Set<Role> getRoles(){
		return this.roles;
	}	
	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}
	
	
}
