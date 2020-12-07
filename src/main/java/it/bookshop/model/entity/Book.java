package it.bookshop.model.entity;

import java.io.Serializable;
import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Column; 
import javax.persistence.Table; 
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.ManyToMany;
import javax.persistence.CascadeType;

@Entity
@Table(name="BOOKS")
public class Book implements Serializable{
	
	private static final long serialVersionUID = 1L;

	private String isbn;
	private String title;
	private Date publish;
	private int pages;
	private String summary; //short synthesis for book preview
	private String cover; //file name of cover image
	private Set<Author> authors = new HashSet<Author>();
	private Set<Genre> genres = new HashSet<Genre>();
	private Set<Purchase> purchases = new HashSet<Purchase>();
	private Set<Offer> offers = new HashSet<Offer>();
	
			
	@Id
	@Column(name = "ISBN")
	public String getIsbn() {
		return this.isbn;
	}
	
	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}
	
	@Column(name = "TITLE", nullable = false)
	public String getTitle() {
		return this.title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	@Column(name = "PUBLISH")
	public Date getPubblish() {
		return this.publish;
	}
	
	public void setPubblish(Date publishs) {
		this.publish = publishs;
	}
	
	@Column(name = "PAGES")
	public int getPages() {
		return this.pages;
	}
	
	public void setPages(int num_of_pages) {
		this.pages = num_of_pages;
	}
	
	@Column(name = "SUMMARY")
	public String getSummary() {
		return this.summary;
	}
	
	public void setSummary(String summary) {
		this.summary = summary;
	}
	
	@Column(name = "COVER")
	public String getCover() {
		return this.cover;
	}
	
	public void setCover(String cover) {
		this.cover = cover;
	}
	
	/*
	 * relations
	 */
	//author
	@ManyToMany(cascade = { CascadeType.DETACH,
			CascadeType.MERGE,
			CascadeType.REFRESH,
			CascadeType.PERSIST })
	@JoinTable( name = "BOOK_AUTHORS", 
				joinColumns = @JoinColumn(name = "ISBN", referencedColumnName = "ISBN"),
				inverseJoinColumns = @JoinColumn(name = "AUTHOR", referencedColumnName = "ID") )
	public Set<Author> getAuthors() {
		return this.authors;
	}
	public void setAuthors(Set<Author> authors) {
		this.authors = authors;
	}
	
	//genre
	@ManyToMany(fetch = FetchType.EAGER,        
			cascade =
	        {
	                CascadeType.DETACH,
	                CascadeType.MERGE,
	                CascadeType.REFRESH,
	                CascadeType.PERSIST
	        })
	@JoinTable(name="book_genre",
		 joinColumns = @JoinColumn(name = "ISBN", nullable = false,
                 updatable = false), 
		 inverseJoinColumns = @JoinColumn(name = "GENRE", nullable = false,
         updatable = false)) 
	public Set<Genre> getGenres() {
		return this.genres;
	}
	public void setGenres(Set<Genre> genres) {
		this.genres = genres;
	}
	
	//buyers
	@OneToMany(cascade = { CascadeType.DETACH,
							CascadeType.MERGE,
							CascadeType.REFRESH,
							CascadeType.PERSIST },
			mappedBy="book")	
	public Set<Purchase> getPurchases() {
		return this.purchases;
	}
	public void setPurchases(Set<Purchase> purchases) {
		this.purchases = purchases;
	}
	
	//sellers
	@OneToMany(cascade = { CascadeType.DETACH,
							CascadeType.MERGE,
							CascadeType.REFRESH,
							CascadeType.PERSIST },
			mappedBy="book")
	public Set<Offer> getOffers() {
		return this.offers;
	}
	public void setOffers(Set<Offer> offers) {
		this.offers = offers;
	}
	
}