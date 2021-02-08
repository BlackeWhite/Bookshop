package it.bookshop.services;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.bookshop.model.Object_form.Bookform;
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
	public Book findById(Long id) {
		return bookRepository.findById(id);
	}

	@Override
	public List<Book> searchBooksByParams(String search_by, String title, Double price_min, Double price_max, String order_by) {
		return bookRepository.searchBooksByParams(search_by, title, price_min, price_max, order_by);
	}

	@Override
	public List<Genre> getAllGenres() {
		return genreRepository.findAll();
	}

	@Override
	public List<Genre> findGenresFromNamesArray(List<String> names) {
		List<Genre> genres = new ArrayList<Genre>();
		for (String name : names) {
			genres.add(genreRepository.findByName(name));
		}
		return genres;
	}

	@Override
	public List<Book> findAll() {
		return bookRepository.findAll();
	}

	@Override
	public List<Book> findAll(Double price_min, Double price_max, String order_by) {
		return bookRepository.findAll(price_min, price_max, order_by);
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
		for (Book b : books) {
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
	public List<Book> findBooksAuthor(Author author){
		return this.authorRepository.findBookForAuthor(author);
	}
	
	@Override
	public List<Book> findFiveMostRecentBook() {
		return bookRepository.findFiveMostRecentBook();
	}

	@Override
	public List<Book> findFiveBestSellingBook() {
		return bookRepository.findFiveBestSellingBook();
	}

	@Override
	public List<Book> findMostClickBook() {
		return bookRepository.findMostClick();
	}
	
	@Override
	public List<Book> findBookOnSale() {
		return bookRepository.findBookOnSale();
	}

	// aggiunta di un click quando il libro � stato selezionato
	@Override
	public void add_click(Long id) {
		Book b = this.bookRepository.findById(id);
		int index = b.getClicked();
		index++;
		b.setClicked(index);
		this.bookRepository.update(b);
	}

	@Override
	public Set<Book> getAllBookForGenre(String name) {
		// restituisce una lista di tutti i libri di un particolare genere, definito
		// attarverso il nome
		Genre g = genreRepository.findByName(name);
		Set<Book> bookforgenre = g.getBooks();
		return bookforgenre;
	}
	
	@Override
	public Set<Book> getBooksimilargenre(Book b){
		// estraggo tutti i libri dello stesso genere del libro passato(usata per il singolo libro)
		Set <Genre> genreSet = b.getGenres(); // tutti i generi di quel libro 
		Iterator <Genre> iterGenre = genreSet.iterator();
		Set <Book> listbook = new HashSet<Book>();
		
		while(iterGenre.hasNext()) {
			String gen = iterGenre.next().getName();
			Set <Book> tempbook = this.getAllBookForGenre(gen);
			Iterator <Book> iterBook = tempbook.iterator();
			while(iterBook.hasNext()) {
				Book btemp = iterBook.next(); 
				if (btemp.getId()!=b.getId()) { // non va aggiunto il libro da cui estraiamo i libri simili 
					listbook.add(btemp);
				}
			}
		}
		return listbook;
	}
	
	@Override
	public Set<Book> getBooksimilarAuthor(Book b){
		// estraggo tutti i libri dello stesso autore del libro passato(usata per il singolo libro)
		Set <Author> AuthorSet = b.getAuthors(); // tutti gli autore di quel libro 
		Iterator <Author> iterAut = AuthorSet.iterator();
		Set <Book> listbook = new HashSet<Book>();
		
		while(iterAut.hasNext()) {
			Author  aut = iterAut.next();
			List <Book> templistbook = this.authorRepository.findBookForAuthor(aut);
			Iterator <Book> iterBook = templistbook.iterator();
			while(iterBook.hasNext()) {
				Book btemp = iterBook.next(); 
				if (btemp.getId()!=b.getId()) { // non va aggiunto il libro da cui estraiamo i libri simili 
					listbook.add(btemp);
				}
			}
		}
		return listbook;
	}
	
	@Override
	public Book create(String Name_author, String Surname_Author, String isbn, String title, Date publish_date,
			Date insert_date, int copies, double price, User seller, int pages, String summary, String cover,
			List<String> genres, double discount) {
		Book b1 = bookRepository.create(isbn, title, publish_date, insert_date, copies, price, seller, pages, summary,
				cover, discount);
		Author a1 = authorRepository.findByNameAndSurname(Name_author, Surname_Author);
		if (a1 != null) {
			a1.addBooks(b1); // ha trovato il libro
		} else { // se non trova l'autore lo crea
			Author a2 = authorRepository.create(Name_author, Surname_Author);
			a2.addBooks(b1);
		}
		for (String genre : genres) {
			Genre g1 = genreRepository.findByName(genre);
			if (g1 != null) {
				g1.addBooks(b1);
			} else {
				Genre g2 = genreRepository.create(genre); // se non trova il genere lo crea
				g2.addBooks(b1);
			}
		}
		return b1;
	}
	
	@Override
	public Book create(Bookform book, User seller) {
		String cover = "img_01.jpg"; // usato come nome di test, va modificato 
		Book b1 = bookRepository.create(book,cover,seller); 
		Iterator <String> iterAuthors = book.getAuthors().iterator();
		while(iterAuthors.hasNext()) { // associa il libro ai diversi autori ad esso associato
			String[] parts = iterAuthors.next().split(" ");
			Author a1 = authorRepository.findByNameAndSurname(parts[0], parts[1]);
			a1.addBooks(b1);
		}
		Iterator <String> itergen = book.getGenre().iterator();
		while(itergen.hasNext()) { // associa il libro ai diversi generi ad esso associato 
			Genre g1 = genreRepository.findByName(itergen.next());
			g1.addBooks(b1);
		}
		
		
		return b1;
	}

	@Override
	public Map<String, Integer> booksAmountPerGenreFromList(List<Book> books) {
		Map<String, Integer> books_for_genre = new HashMap<String, Integer>();
		// Calcola il numero di libri per generi in base alla ricerca
		for (Genre g : getAllGenres()) {
			books_for_genre.put(g.getName(), 0);
		}
		for (Book b : books) {
			for (Genre g : b.getGenres()) {
				int val = books_for_genre.getOrDefault(g.getName(), 0);
				books_for_genre.put(g.getName(), ++val);
			}
		}
		return books_for_genre;
	}

	@Override
	public List<Book> filterByGenres(List<Book> books, List<String> genres) {
		List<Genre> pickedGenres = findGenresFromNamesArray(genres);
		books = books.stream()
				.filter(p -> p.getGenres().stream().map(Genre::getName)
						.anyMatch(pickedGenres.stream().map(Genre::getName).collect(Collectors.toSet())::contains))
				.collect(Collectors.toList());
		return books;
	}
}
