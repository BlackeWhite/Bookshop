package it.bookshop.services;

import java.sql.Date;
import java.util.List;

import it.bookshop.model.entity.Book;

public interface BookService {
	Book findByIsbn(String isbn);
	List<Book> findAll();
	Book create(String isbn, String title, Date publish_date, int num_of_pages, String summary, String cover);
	Book update(Book book);
	void delete(Book book);
	void delete(String isbn);
};