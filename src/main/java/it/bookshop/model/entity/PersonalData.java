//***Classe @Embeddable sui dati anagrafici dell'utente.
//   Attualmente ho posizionato la classe nel package model
//   delle entità, ma non saprei se è corretto (Agostino).

package it.bookshop.model.entity;

import javax.persistence.Embeddable;
import java.sql.Date;
import javax.persistence.Column;
import javax.persistence.Transient;


@Embeddable
public class PersonalData {
	
	@Column(name = "NAME")
	private String name;
	@Column(name = "SURNAME")
	private String surname;
	@Column(name = "BIRTHDATE")
	private Date birthdate;
	
	@Column(name="STREET")
	private String street;
	@Column(name="CITY")
    private String city;
	@Column(name="CAP")
    private long cap;
	@Column(name="STATE")
    private String state;

    public void setName(String name) {
    	this.name = name;
    }
    public String getName() {
    	return name;
    }
    
    public void setSurname(String surname) {
    	this.surname = surname;
    }
    public String getSurname() {
    	return surname;
    }
    
    @Transient
    public String getFullName() {
    	return (this.name + this.surname).trim();
    }
    
    public void setBirthdate(Date birthdate) {
    	this.birthdate = birthdate;
    }
    public Date getBirthdate() {
    	return birthdate;
    }
    
    
    public void setStreet(String street) {
    	this.street = street;
    }
    public String getStreet() {
    	return street;
    }
    
    public void setCity(String city) {
    	this.city = city;
    }
    public String getCity(String city) {
    	return city;
    }
    
    public void setCap(long cap) {
    	this.cap = cap;
    }
    public long getCap() {
    	return cap;
    }
    
    public void setState(String state) {
    	this.state = state;
    }
    public String getState(String state) {
    	return state;
    }
}
