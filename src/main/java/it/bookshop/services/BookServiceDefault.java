package it.bookshop.services;

import java.sql.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.bookshop.model.dao.AuthorDao;
import it.bookshop.model.dao.BookDao;
import it.bookshop.model.dao.GenreDao;
import it.bookshop.model.entity.Author;
import it.bookshop.model.entity.Book;
import it.bookshop.model.entity.Genre;
import it.bookshop.model.entity.User;


@Transactional
@Service("bookService")
public class BookServiceDefault implements BookService {

	private BookDao bookRepository;
	private AuthorDao authorRepository;
	private GenreDao genreRepository;
	
	@Override
	public 	Book findById(Long id) {
		return bookRepository.findById(id);
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
	public void delete(Long bookId) {
		Book b = bookRepository.findById(bookId);
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
	
	@Autowired
	public void setGenreRepository(GenreDao genreRepository) {
		this.genreRepository = genreRepository;
	}

	@Override
	public Book create(String Name_author, String Surname_Author, String isbn,String title, 
			Date publish_date, int copies, double price, User seller, int pages, String summary, String cover, String genre) {
		Book b1 = bookRepository.create(isbn, title, publish_date, copies, price, seller, pages, summary, cover);
		Author a1 = authorRepository.findByNameAndSurname(Name_author, Surname_Author);
		if (a1 != null) {
			a1.addBooks(b1); // ha trovato il libro 
		}
		else 
		{ // se non trova l'autore lo crea 
			Author a2 = authorRepository.create(Name_author, Surname_Author);
			a2.addBooks(b1);
		}
		Genre g1 = genreRepository.findByName(genre);
		if (g1 != null) {
			g1.addBooks(b1);
			}
		else {
			Genre g2 = genreRepository.create(genre); // se non trova il genere lo crea 
			g2.addBooks(b1);
		}
		return b1;
	}
	

}
