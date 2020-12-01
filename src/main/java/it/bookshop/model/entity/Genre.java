package it.bookshop.model.entity;


import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.OrderColumn;
import javax.persistence.Table;


@Entity
@Table(name="Genre")
public class Genre {
	
	private String Name;
	private Set<Book> books = new HashSet<Book>();
	
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
        }, mappedBy = "genres_name")
	
	public Set<Book> getBooks() {
	return this.books;
	}

	public void setBooks(Set<Book> books) {
		this.books = books;
	}
}
