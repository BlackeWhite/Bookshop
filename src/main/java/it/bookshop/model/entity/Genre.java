package it.bookshop.model.entity;


import java.io.Serializable;
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
import javax.persistence.OrderColumn;
import javax.persistence.Table;


@Entity
@Table(name="GENRE")
public class Genre implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private String Name;
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
	
	@Column(name = "Name")
	@OrderColumn
	public String getName() {
		return this.Name;
	}

	public void setName(String Name) {
		this.Name = Name;
	}
	
	
	@ManyToMany(fetch = FetchType.EAGER,         cascade =
        {
                CascadeType.DETACH,
                CascadeType.MERGE,
                CascadeType.REFRESH,
                CascadeType.PERSIST
        }, mappedBy = "genres")
	public Set<Book> getBooks() {
	return this.books;
	}

	public void setBooks(Set<Book> books) {
		this.books = books;
	}
	public void addBooks(Book b) {
		this.books.add(b);
		b.getGenres().add(this); // NB nota che non usiamo l'utility method addInstrument
	}
	
	public void removeBooks(Book b) {
		this.books.remove(b);
		b.getGenres().remove(this);
	}

}
