package it.bookshop.model.Object_form;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import it.bookshop.model.entity.Author;
import it.bookshop.model.entity.Book;
import it.bookshop.model.entity.Genre;
import it.bookshop.model.entity.User;
import it.bookshop.services.AuthorService;
import it.bookshop.model.dao.GenreDao;
import it.bookshop.model.dao.AuthorDao;

public class Bookform {

	/*
	 * classe utilizzata nella form per aggiungere un libro unisce sia le
	 * informazione sul libro che dei diversi generi ad esso associato
	 */

	private long id;
	private String isbn;
	private String title;
	private int copies; // Copie Disponibili
	private List<String> genre;
	private List<String> authors;
	private List<String> authorsName;
	private List<String> authorsSurname;
	private double price;
	private Date publish;
	private Date insertdata;
	private int pages;
	private int discount; // se al libro è applicato uno sconto
	private String summary; // short synthesis for book preview
	private MultipartFile cover; // file name of cover image
	private String cover_name;

	@Autowired
	private AuthorService authorService;

	@Autowired
	private GenreDao genreDao;

	@Autowired
	private AuthorDao authorDao;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getCopies() {
		return copies;
	}

	public void setCopies(int copies) {
		this.copies = copies;
	}

	public Date getPublish() {
		return publish;
	}

	public void setPublish(Date publish) {
		this.publish = publish;
	}

	public Date getInsertdata() {
		return insertdata;
	}

	public void setInsertdata(Date insertdata) {
		this.insertdata = insertdata;
	}

	public int getPages() {
		return pages;
	}

	public void setPages(int pages) {
		this.pages = pages;
	}

	public int getDiscount() {
		return discount;
	}

	public void setDiscount(int discount) {
		this.discount = discount;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public MultipartFile getCover() {
		return cover;
	}

	public void setCover(MultipartFile cover) {
		this.cover = cover;
	}

	public String getCover_name() {
		return cover_name;
	}

	public void setCover_name(String cover) {
		this.cover_name = cover;
	}

	public List<String> getGenre() {
		return genre;
	}

	public void setAuthorsName(List<String> authors_name) {
		this.authorsName = authors_name;
	}

	public List<String> getAuthorsName() {
		return authorsName;
	}

	public void setAuthorsSurname(List<String> authors_surname) {
		this.authorsSurname = authors_surname;
	}

	public List<String> getAuthorsSurname() {
		return authorsSurname;
	}

	public void setGenre(List<String> genre) {
		this.genre = genre;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	// metodo usato nella forma di modifica del libro
	public void populate(Book b) {
		this.id = b.getId();
		this.isbn = b.getIsbn();
		this.title = b.getTitle();
		this.publish = b.getPublish();
		this.copies = b.getCopies();
		this.price = b.getPrice();
		this.discount = (int) (b.getDiscount() * 100);
		this.pages = b.getPages();
		this.summary = b.getSummary();
		this.cover_name = b.getCover();

		// per la lista dei generi
		List<String> genrelist = new ArrayList<String>();
		Iterator<Genre> iteGen = b.getGenres().iterator();
		while (iteGen.hasNext()) {
			genrelist.add(iteGen.next().getName());
		}

		this.genre = genrelist;

		// per la lista degli autori
		List<String> autlist = new ArrayList<String>();
		Iterator<Author> iterAuthors = b.getAuthors().iterator();
		while (iterAuthors.hasNext()) {
			autlist.add(iterAuthors.next().getFullName());
		}

		this.authors = autlist;

	}

	public Book bookformToBook(Bookform book, User seller, Set<Author> authorsList, Set<Genre> genreList,
			Long book_id) {
		Book b = new Book();
		b.setId(book_id);
		b.setIsbn(book.getIsbn());
		b.setTitle(book.getTitle());
		b.setPublish(book.getPublish());
		b.setCopies(book.getCopies());
		b.setPrice(book.getPrice());
		b.setSeller(seller);
		b.setPages(book.getPages());
		b.setSummary(book.getSummary());
		// b.setCover(book.getCover().getOriginalFilename());
		// if (b.getCover().isEmpty())
		// b.setCover("bookcover-placeholder.png");
		// Date date = new Date(Calendar.getInstance().getTime().getTime());
		// b.setInsertdata(date);
		// b.setClicked();
		// b.setSoldCopies(0);
		b.setDiscount(((double) book.getDiscount()) / 100);
		b.setRemoved(0);

		b.setAuthors(authorsList);
		b.setGenres(genreList);

		return b;
	}

}
