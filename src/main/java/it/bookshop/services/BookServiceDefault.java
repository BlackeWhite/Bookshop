package it.bookshop.services;

import java.sql.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.bookshop.model.dao.AuthorDao;
import it.bookshop.model.dao.BookDao;
import it.bookshop.model.entity.Author;
import it.bookshop.model.entity.Book;


@Transactional
@Service("bookService")
public class BookServiceDefault implements BookService {

	private BookDao bookRepository;
	private AuthorDao authorRepository;
	
	@Override
	public 	Book findByIsbn(String isbn) {
		return bookRepository.findByIsbn(isbn);
	}
	
	@Override
	public List<Book> findAll() {
		return bookRepository.findAll();
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
	
	@Autowired
	public void setAuthorRepository(AuthorDao authorRepository) {
		this.authorRepository = authorRepository;
	}

	@Override
	public Book create(String Name_author, String Surname_Author, String isbn, String title, Date publish_date,
			int num_of_pages, String summary, String cover) {
		Book b1 = bookRepository.create(isbn, title, publish_date, num_of_pages, summary, cover);
		Author a1 = authorRepository.findByNameAndSurname(Name_author, Surname_Author);
		if (a1 != null) {
		a1.addBooks(b1);
		}
		return b1;
		
	}
	

}
