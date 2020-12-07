package it.bookshop.services;

import java.sql.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import it.bookshop.model.dao.BookDao;
import it.bookshop.model.entity.Book;

@Transactional
@Service("bookService")
public class BookServiceDefault implements BookService {

	private BookDao bookRepository;
	
	@Override
	public 	Book findByIsbn(String isbn) {
		return bookRepository.findByIsbn(isbn);
	}
	
	@Override
	public List<Book> findAll() {
		return bookRepository.findAll();
	}
	
	@Override
	public Book create(String isbn, String title, Date publish_date, int num_of_pages, String summary, String cover) {
		return bookRepository.create(isbn, title, publish_date, num_of_pages, summary, cover);
	}
	
	@Override
	public Book update(Book book) {
		return bookRepository.update(book);
	}
	
	@Override
	public void delete(Book book) {
		bookRepository.delete(book);
	}

	@Override
	public void delete(String isbn) {
		Book b = bookRepository.findByIsbn(isbn);
		bookRepository.delete(b);
	}

	@Autowired
	public void setBookRepository(BookDao bookRepository) {
		this.bookRepository = bookRepository;
	}
}
