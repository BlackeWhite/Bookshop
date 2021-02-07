package it.bookshop.model.dao;

import java.sql.Date;
import java.util.Calendar;
import java.util.List;


import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import it.bookshop.model.Object_form.Bookform;
import it.bookshop.model.entity.Author;
import it.bookshop.model.entity.Book;
import it.bookshop.model.entity.User;


@Transactional
@Repository("bookDao") 
public class BookDaoDefault extends DefaultDao implements BookDao{
	
	@Override
	public List<Book> findAll() {
		return this.getSession().createQuery("FROM Book b", Book.class).getResultList();
	}
	
	@Override
	public List<Book> findAll(Double price_min, Double price_max, String order_by) {
		String[] params = order_by.split("_");
		//setParameter non funziona bene con ORDER BY quindi si crea la stringa manualmente
		String order_str = "ORDER BY b." + params[0] + " " + params[1];
		
		return this.getSession().createQuery("FROM Book b WHERE b.price>:priceMin AND b.price<:priceMax " 
		+ order_str, Book.class)
				.setParameter("priceMin", price_min)
				.setParameter("priceMax", price_max).getResultList();
	}
	
	@Override
	public Book findById(Long bookId) {
		return this.getSession().createQuery("FROM Book b WHERE b.id = :id", Book.class)
				.setParameter("id", bookId).getSingleResult();
	}
	
	@Override
	public List<Book> searchBooksByParams(String search_by, String term, Double price_min, Double price_max, String order_by) {
		
		String[] params = order_by.split("_");
		//setParameter non funziona bene con il "." vicino quindi si creano la stringa manualmente
		String order_str = "ORDER BY b." + params[0] + " " + params[1];
		String search_str = "b." + search_by;
		
		return this.getSession()
				.createQuery("FROM Book b WHERE LOWER("+search_str+") LIKE LOWER(CONCAT('%', :term, '%')) "
						+ "AND b.price>:min AND b.price<:max " + order_str, Book.class)
				.setParameter("term", term)
				.setParameter("min", price_min)
				.setParameter("max", price_max).getResultList();
	}
	
	@Override
	public Book create(String isbn,String title, Date publish_date, Date insert_date, int copies, double price, 
			User seller, int pages, String summary, String cover,double discount) {
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
		b.setInsertData(insert_date);
		b.setClicked(0);
		b.setSoldCopies(0);
		b.setDiscount(discount);
		getSession().save(b);
		return b;
	}
	
	@Override
	public Book create(Bookform book, String cover, User seller) {
		Book b = new Book();
		b.setIsbn(book.getIsbn());
		b.setTitle(book.getTitle());
		b.setPublish(book.getPublish());
		b.setCopies(book.getCopies());
		b.setPrice(book.getPrice());
		b.setSeller(seller);
		b.setPages(book.getPages());
		b.setSummary(book.getSummary());
		b.setCover(cover);
		Date date = new Date(Calendar.getInstance().getTime().getTime()); // si prende la data odierna per l'inserimento del libro 
		b.setInsertData(date);
		b.setClicked(0);
		b.setSoldCopies(0);
		b.setDiscount(book.getDiscount());
		getSession().save(b);
		return b;
	}
	
	
	@Override
	public Book update(Book book) {
		return (Book)this.getSession().merge(book);
	}
	
	@Override
	public void delete(Book book) {
		this.getSession().delete(book);
	}
	
	@Override
	public List<Book> findFiveMostRecentBook(){
		return this.getSession().createNativeQuery("SELECT * FROM books ORDER BY INSERTDATA DESC LIMIT 5", Book.class).getResultList();
	}
	
	@Override
	public List<Book> findFiveBestSellingBook(){
		return this.getSession().createNativeQuery("SELECT * FROM books ORDER BY SOLD_COPIES DESC LIMIT 5", Book.class).getResultList();
	}
	
	@Override
	public List<Book> findMostClick(){
		return this.getSession().createNativeQuery("SELECT * FROM books ORDER BY CLICK_BOOK DESC", Book.class).getResultList();
	}
	 
   @Override
   public List<Book> findBookOnSale(){
		return this.getSession().createQuery("FROM Book b WHERE b.discount > 0 ", Book.class).getResultList();
   }
   
}
