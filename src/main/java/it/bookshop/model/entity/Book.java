package it.bookshop.model.entity;

import java.io.Serializable;
import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Column; 
import javax.persistence.Table;

import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.CascadeType;

@Entity
@Table(name="BOOKS")
public class Book implements Serializable{
	
	private static final long serialVersionUID = 1L;

	private long id;
	private String isbn;
	private String title;
	private int copies; //Available copies
	private double price;
	private Date publish; 
	private Date insertdata;
	private int pages;
	private String summary; //short synthesis for book preview
	private String cover; //file name of cover image
	private User seller; //Seller of the book
	private Set<Author> authors = new HashSet<Author>();
	private Set<Genre> genres = new HashSet<Genre>();
	private Set<BookOrder> orders = new HashSet<BookOrder>(); //All orders with one ore multiple copies of this book
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	public Long getId() {
		return this.id;
	}
	public void setId(Long id) {
		this.id = id;
	}
			
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
	
	@Column(name = "PRICE")
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	
	@Column(name = "COPIES")
	public int getCopies() {
		return copies;
	}
	public void setCopies(int copies) {
		this.copies = copies;
	}
	
	@Column(name = "PUBLISHDATA")
	public Date getPublish() {
		return this.publish;
	}
	public void setPublish(Date publish) {
		this.publish = publish;
	}
	
	@Column(name = "INSERTDATA")
	public Date getInsertData() {
		return this.insertdata;
	}
	public void setInsertData(Date insertdata) {
		this.insertdata = insertdata;
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
	
	//Seller
	@ManyToOne(cascade = { CascadeType.DETACH,
			CascadeType.MERGE,
			CascadeType.REFRESH,
			CascadeType.PERSIST,},
			fetch = FetchType.LAZY)
	@JoinColumn(name="SELLER_ID", referencedColumnName = "USER_ID")
	public User getSeller() {
		return seller;
	}
	public void setSeller(User seller) {
		this.seller = seller;
	}
	
	//author
	@ManyToMany(cascade = { CascadeType.DETACH,
			CascadeType.MERGE,
			CascadeType.REFRESH,
			CascadeType.PERSIST })
	@JoinTable( name = "BOOK_AUTHORS", 
				joinColumns = @JoinColumn(name = "BOOK", referencedColumnName = "ID"),
				inverseJoinColumns = @JoinColumn(name = "AUTHOR", referencedColumnName = "ID") )
	public Set<Author> getAuthors() {
		return this.authors;
	}
	public void setAuthors(Set<Author> authors) {
		this.authors = authors;
	}
	public void addAuthors(Author a) {
		this.authors.add(a);
		a.getBooks().add(this); // NB nota che non usiamo l'utility method addInstrument
	}
	public void removeAuthors(Author a) {
		this.authors.remove(a);
		a.getBooks().remove(this);
	}
	
	//genre
	@ManyToMany(cascade = { CascadeType.DETACH,
			CascadeType.MERGE,
			CascadeType.REFRESH,
			CascadeType.PERSIST })
	@JoinTable( name = "BOOK_GENRES", 
				joinColumns = @JoinColumn(name = "ID_BOOK", referencedColumnName = "ID"),
				inverseJoinColumns = @JoinColumn(name = "ID_GENRE", referencedColumnName = "ID") )
	public Set<Genre> getGenres() {
		return this.genres;
	}
	public void setGenres(Set<Genre> genres) {
		this.genres = genres;
	}
	public void addGenres(Genre g) {
		this.genres.add(g);
		g.getBooks().add(this); // NB nota che non usiamo l'utility method addInstrument
	}
	public void removeGenres(Genre g) {
		this.genres.remove(g);
		g.getBooks().remove(this);
	}
	
	//orders
	@OneToMany(fetch = FetchType.EAGER,
			cascade = { CascadeType.DETACH,
							CascadeType.MERGE,
							CascadeType.REFRESH,
							CascadeType.PERSIST },
			mappedBy="book")	
	public Set<BookOrder> getOrders() {
		return this.orders;
	}
	public void setOrders(Set<BookOrder> orders) {
		this.orders = orders;
	}
	
	
}