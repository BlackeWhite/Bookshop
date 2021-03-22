package it.bookshop.model.dao;


import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;


import it.bookshop.model.ObjectForm.Bookform;
import it.bookshop.model.entity.Book;
import it.bookshop.model.entity.CustomUserDetails;
import it.bookshop.model.entity.User;

@Repository("bookDao")
public class BookDaoDefault extends DefaultDao implements BookDao {

	@Autowired
	BookOrderDao bookOrderRepository;
	
	private double getVatFactor() {
		if (!(SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken)) {
			CustomUserDetails details = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication()
					.getPrincipal();
			return (double) (1 + Book.vats.get(details.getState()));
		} else
			return 1.22; // Utente non autenticato -> IVA italiana
	}
	
	@Override
	public List<Book> findAll() {
		return this.getSession().createQuery("FROM Book b WHERE b.removed = 0", Book.class).getResultList();
	}

	@Override
	public List<Book> findAll(Double price_min, Double price_max, String order_by) {
		String[] params = order_by.split("_");
		// setParameter non funziona bene con ORDER BY quindi si crea la stringa
		// manualmente
		// if per evitare SQL injection
		if(!params[0].equals("title") && !params[0].equals("price") 
				&& !params[0].equals("discount")) return new ArrayList<Book>();
		if(!params[1].equals("DESC") && !params[1].equals("ASC")) return new ArrayList<Book>();
		String order_str = "ORDER BY b." + params[0] + " " + params[1];

		return this.getSession()
				.createQuery(
						"FROM Book b WHERE (b.price*:vat)>:priceMin AND (b.price*:vat)<:priceMax " + "AND b.removed = 0 " + order_str,
						Book.class)
				.setParameter("priceMin", price_min).setParameter("priceMax", price_max)
				.setParameter("vat", getVatFactor()).getResultList();
	}

	@Override
	public Book findById(Long bookId) {
		return this.getSession().createQuery("FROM Book b WHERE b.id = :id AND b.removed=0", Book.class)
				.setParameter("id", bookId).getSingleResult();
	}

	@Override
	public List<Book> searchBooksByParams(String search_by, String term, Double price_min, Double price_max,
			String order_by) {

		String[] params = order_by.split("_");
		// setParameter non funziona bene con il "." vicino quindi si crea la stringa
		// manualmente
		// if per evitare SQL injection
		if(!params[0].equals("title") && !params[0].equals("price") 
				&& !params[0].equals("discount")) return new ArrayList<Book>();
		if(!params[1].equals("DESC") && !params[1].equals("ASC")) return new ArrayList<Book>();
		String order_str = "ORDER BY b." + params[0] + " " + params[1];
		// if per evitare SQL injection
		if(!search_by.equals("title") && !search_by.equals("isbn")) return new ArrayList<Book>();
		String search_str = "b." + search_by;

		return this.getSession()
				.createQuery("FROM Book b WHERE LOWER(" + search_str + ") LIKE LOWER(CONCAT('%', :term, '%')) "
						+ "AND (b.price*:vat)>:min AND (b.price*:vat)<:max " + "AND b.removed = 0 " + order_str, Book.class)
				.setParameter("term", term).setParameter("min", price_min).setParameter("max", price_max)
				.setParameter("vat", getVatFactor()).getResultList();
	}

	@Override
	public List<Book> searchBooksByParamsAndAuthor(String term, Double price_min, Double price_max, String order_by) {

		String[] params = order_by.split("_");
		// setParameter non funziona bene con il "." vicino quindi si crea la stringa
		// manualmente
		// if per evitare SQL injection
		if(!params[0].equals("title") && !params[0].equals("price") 
				&& !params[0].equals("discount")) return new ArrayList<Book>();
		if(!params[1].equals("DESC") && !params[1].equals("ASC")) return new ArrayList<Book>();
		String order_str = "ORDER BY b." + params[0] + " " + params[1];

		return this.getSession().createQuery(
				"SELECT b FROM Book b JOIN b.authors a WHERE LOWER(a.name || ' ' || a.surname) LIKE LOWER(CONCAT('%', :term, '%')) "
						+ "AND (b.price*:vat)>:min AND (b.price*:vat)<:max " + "AND b.removed = 0 " + order_str,
				Book.class).setParameter("term", term).setParameter("min", price_min).setParameter("max", price_max)
				.setParameter("vat", getVatFactor()).getResultList();
	}

	@Override
	public Book create(String isbn, String title, Date publish_date, Date insert_date, int copies, double price,
			User seller, int pages, String summary, String cover, double discount) {
		Book b = new Book();
		b.setIsbn(isbn);
		b.setTitle(title);
		b.setPublish(publish_date);
		b.setCopies(copies);
		b.setPrice(price);
		b.setSeller(seller);
		b.setPages(pages);
		b.setSummary(summary);
		b.setCover(cover);
		if (b.getCover().isEmpty())
			b.setCover("bookcover-placeholder.png");
		b.setInsertdata(insert_date);
		b.setClicked(0);
		b.setSoldCopies(0);
		b.setDiscount(discount);
		b.setRemoved(0);
		getSession().save(b);
		return b;
	}

	@Override
	public Book create(Bookform book, User seller) {
		Book b = new Book();
		b.setIsbn(book.getIsbn());
		b.setTitle(book.getTitle());
		b.setPublish(book.getPublish());
		b.setCopies(book.getCopies());
		b.setPrice(book.getPrice());
		b.setSeller(seller);
		b.setPages(book.getPages());
		b.setSummary(book.getSummary());
		b.setCover(book.getCover().getOriginalFilename());
		if (b.getCover().isEmpty())
			b.setCover("bookcover-placeholder.png");
		Date date = new Date(Calendar.getInstance().getTime().getTime());
		b.setInsertdata(date);
		b.setClicked(0);
		b.setSoldCopies(0);
		b.setDiscount(((double) book.getDiscount()) / 100);
		b.setRemoved(0);
		getSession().save(b);
		return b;
	}

	@Override
	public Book update(Book book) {
		return (Book) this.getSession().merge(book);
	}

	@Override
	public void delete(Book book) {
		
		bookOrderRepository.deleteBookBookOrders(book);
		
		this.getSession().delete(book);
	}

	@Override
	public List<Book> findFiveMostRecentBook() {
		return this.getSession()
				.createNativeQuery("SELECT * FROM books WHERE removed=0 ORDER BY INSERTDATA DESC LIMIT 5", Book.class)
				.getResultList();
	}

	@Override
	public List<Book> findFiveBestSellingBook() {
		return this.getSession()
				.createNativeQuery("SELECT * FROM books WHERE removed=0 ORDER BY SOLD_COPIES DESC LIMIT 5", Book.class)
				.getResultList();
	}

	@Override
	public List<Book> findSellerBook(Long id) {
		return this.getSession().createQuery(
				"SELECT b FROM Book b JOIN b.seller s WHERE s.userID=:id AND b.removed=0 ORDER BY INSERTDATA DESC",
				Book.class).setParameter("id", id).getResultList();
	}

	@Override
	public List<Book> findMostClick() {
		return this.getSession()
				.createNativeQuery("SELECT * FROM books  WHERE removed=0 ORDER BY CLICK_BOOK DESC", Book.class)
				.getResultList();
	}

	@Override
	public List<Book> findAllBookForGenre(String name) {
		return this.getSession()
				.createQuery("SELECT b FROM Book b JOIN b.genres g WHERE g.name=:name AND b.removed=0", Book.class)
				.setParameter("name", name).getResultList();
	}

	@Override
	public List<Book> findBookOnSale() {
		return this.getSession().createQuery("FROM Book b WHERE b.discount > 0 AND b.removed=0", Book.class)
				.getResultList();
	}
	
	//Utilizzato solo nei Junit test
	@Override
	public void removeBook(Book book) {
		book.setCopies(0);
		book.setRemoved(1);
	}
	
	@Override
	public List<Book> findBookRemovedForSeller(User seller) {
		/*
		 * Metodo per la ricerca dei libri fuori catalogo di un determinato venditore.
		 */
		return this.getSession().createQuery(
				"SELECT b FROM Book b JOIN b.seller s WHERE s.userID=:id AND b.removed=1 ORDER BY INSERTDATA DESC",
				Book.class).setParameter("id", seller.getUserID()).getResultList();
	}
	
	@Override
	public void saveRemoved(Book book) {
		book.setCopies(0);
		book.setRemoved(1);
		this.update(book);
	}
	
	@Override
	public void saveRestored(Book book) {
		book.setRemoved(0);
		this.update(book);
	}
	
	@Override
	public Book findByIdRemoved(Long bookId) {
		return this.getSession().createQuery("FROM Book b WHERE b.id = :id AND b.removed=1", Book.class)
				.setParameter("id", bookId).getSingleResult();
	}
	
}
