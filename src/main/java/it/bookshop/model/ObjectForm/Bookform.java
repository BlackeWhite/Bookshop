package it.bookshop.model.ObjectForm;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;


import org.springframework.web.multipart.MultipartFile;

import it.bookshop.model.entity.Author;
import it.bookshop.model.entity.Book;
import it.bookshop.model.entity.Genre;
import it.bookshop.model.entity.User;


public class Bookform {
	/*
	 * Classe utilizzata nella form per aggiungere un libro, unisce sia le
	 * informazione sul libro che dei diversi generi ad esso associato
	 */
	private long id;
	private String isbn;
	private String title;
	private int copies; // Copie Disponibili
	private List<String> genre;
	private List<String> authorsName;
	private List<String> authorsSurname;
	private double price;
	private Date publish;
	private Date insertdata;
	private int pages;
	private int discount; // se al libro è applicato uno sconto
	private String summary; // descrizione del libro
	private MultipartFile cover; // immagine contenente la copertina del libro
	private String cover_name;

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

	public void populate(Book b) {
		/*
		 * Popola l'istanza di Bookform con i dati dell'oggetto book
		 */
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
		
		int count = 0;
		List<String> tempName = new ArrayList<>();
		List<String> tempSurname = new ArrayList<>();
		for(Author author:b.getAuthors()) {
			/*
			 * Popola la lista dei nomi e dei cognomi degli autori
			 * e inserisce il placeholder nel caso il cognome sia vuoto
			 */
			tempName.add(count, author.getName());
			//this.authorsName.set(count, author.getName());
			String surname = author.getSurname();
			if(surname.isEmpty() || surname == null || surname == "") {
				tempSurname.add(count, "#SURNAME_PLACEHOLDER");
			} else {
				tempSurname.add(count, surname);
			} 
			count++;
			
		}
		this.authorsName = tempName;
		this.authorsSurname = tempSurname;

	}

	public Book bookformToBook(Bookform book, User seller, Set<Author> authorsList, Set<Genre> genreList,
			Long book_id, Book bookNotUpdated) {
		/*
		 * Popola e restituisce un oggetto di tipo Book con i dati dell'istanza di Bookform
		 * che contiene i dati della form
		 */
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
		String bookCover = book.getCover().getOriginalFilename();
		if(bookCover.isEmpty()) {
			b.setCover(bookNotUpdated.getCover());
			if (b.getCover().isEmpty()) {
				b.setCover("bookcover-placeholder.png");
			}
		} else {
			b.setCover(bookCover);
		}
		b.setDiscount(((double) book.getDiscount()) / 100);
		b.setRemoved(0);

		b.setAuthors(authorsList);
		b.setGenres(genreList);

		b.setInsertdata(bookNotUpdated.getInsertdata());
		b.setClicked(bookNotUpdated.getClicked());
		b.setSoldCopies(bookNotUpdated.getSoldCopies());
		
		return b;
	}
	
	public void placeholder(){
		/*
		 * Metodo per inserire il placeholder, specifico per quando il bindingresult
		 * da errore (nel controller)
		 */
		int count = 0;
		for(String author: authorsName) {
			String surname = authorsSurname.get(count);
			if(surname.isEmpty() || surname == null || surname == "") {
				authorsSurname.set(count, "#SURNAME_PLACEHOLDER");
			}
			count++;
		}
	}

}
