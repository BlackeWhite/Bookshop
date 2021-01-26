package it.bookshop.services;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

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
	public 	List<Book> searchBooksByTitle(String title, String order_by) {
		return bookRepository.searchBooksByTitle(title, order_by);
	}
	
	@Override
	public List<Genre> getAllGenres() {
		return genreRepository.findAll();
	}
	
	@Override
	public List<Genre> findGenresFromNamesArray(List<String> names) {
		List<Genre> genres = new ArrayList<Genre>();
		for(String name : names) {
			genres.add(genreRepository.findByName(name));
		}
		return genres;
	}
	
	@Override
	public List<Book> findAll() {
		return bookRepository.findAll();
	}
	
	@Override
	public List<Book> findAll(String order_by) {
		return bookRepository.findAll(order_by);
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
	public void deleteAll() {
		List<Book> books = findAll();
		for(Book b : books) {
			delete(b);
		}
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
	public List<Book> findFiveMostRecentBook(){
		return bookRepository.findFiveMostRecentBook();
	}
	
	@Override
	public List<Book> findFiveBestSellingBook(){
		return bookRepository.findFiveBestSellingBook();
	}
	
	@Override
	public List<Book> findMostClickBook(){
		return bookRepository.findMostClick();
	}
	
	// aggiunta di un click quando il libro è stato selezionato
	@Override
	public void add_click(Long id) {
		Book b = this.bookRepository.findById(id);
		int index = b.getClicked();
		index++; 
		b.setClicked(index);
		this.bookRepository.update(b);
	}
	
	@Override
	public List<Book> getAllBookForGenre(String name){
		// restituisce una lista di tutti i libri di un particolare genere, definito attarverso il nome 
		Genre g = genreRepository.findByName(name); 
		List<Book> bookforgenre =  g.getBooks();
		return bookforgenre; 
	}
	
	@Override  
	public Book create(String Name_author, String Surname_Author, String isbn,String title, 
			Date publish_date, Date insert_date, int copies, double price, User seller, int pages, String summary, String cover, String genre) {
		Book b1 = bookRepository.create(isbn, title, publish_date, insert_date, copies, price, seller, pages, summary, cover);
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
