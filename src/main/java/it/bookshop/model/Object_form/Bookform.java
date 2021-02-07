package it.bookshop.model.Object_form;

import java.sql.Date;
import java.util.List;

public class Bookform {
	
/*
 * classe utilizzata nella form per aggiungere un libro 
 *  unisce sia le informazione sul libro che dei diversi generi ad esso associato
 */
	
	// se serve aggiungere un attributo per l' id del libro 
	private String isbn;
	private String title;
	private int copies; //  Copie Disponibili
	private List<String> genre; 
	private double price;
	private Date publish; 
	private Date insertdata;
	private int pages;
	private double discount; // se al libro è applicato uno sconto 
	private String summary; //short synthesis for book preview
	private String cover; //file name of cover image
  
 
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
	public double getDiscount() {
		return discount;
	}
	public void setDiscount(double discount) {
		this.discount = discount;
	}
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	public String getCover() {
		return cover;
	}
	public void setCover(String cover) {
		this.cover = cover;
	}
	public List<String> getGenre() {
		return genre;
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

		  
}
