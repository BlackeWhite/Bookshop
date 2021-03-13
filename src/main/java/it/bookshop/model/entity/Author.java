package it.bookshop.model.entity;

import java.io.Serializable;
import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Version;


@Entity
@Table(name = "AUTHOR")
public class Author implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private String name;
	private String surname;
	private Date birthdate;
	//Filename of the author's photo
	private String photo;
	private String nationality;
	//Short description of the author
	private String biography;
	private Set<Book> books = new HashSet<Book>();
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	@Column(name = "NAME")
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@Column(name = "SURNAME")
	public String getSurname() {
		return surname;
	}
	public void setSurname(String surname) {
		this.surname = surname;
	}
	
	@Transient
	public String getFullName() {
		return (this.name +" "+ this.surname).trim();
	}
	
	@Column(name = "BIRTHDATE")
	public Date getBirthdate() {
		return birthdate;
	}
	public void setBirthdate(Date birthdate) {
		this.birthdate = birthdate;
	}
	
	@Column(name = "PHOTO")
	public String getPhoto() {
		return photo;
	}
	public void setPhoto(String photo) {
		this.photo = photo;
	}
	
	@Column(name = "NATIONALITY")
	public String getNationality() {
		return nationality;
	}
	public void setNationality(String nationality) {
		this.nationality = nationality;
	}
	
	@Column(name = "BIOGRAPHY")
	public String getBiography() {
		return biography;
	}
	public void setBiography(String biography) {
		this.biography = biography;
	}
	
	@ManyToMany(fetch = FetchType.EAGER,         cascade =
        {
                CascadeType.DETACH,
                CascadeType.MERGE,
                CascadeType.REFRESH,
                CascadeType.PERSIST
        }, mappedBy = "authors")
	public Set<Book> getBooks() {
		return this.books;
	}
	public void setBooks(Set<Book> books) {
		this.books = books;
	}
	
	public void addBooks(Book b) {
		this.books.add(b);
		b.getAuthors().add(this); 
	}
	
	public void removeBooks(Book b) {
		this.books.remove(b);
		b.getAuthors().remove(this);
	}
	
}
