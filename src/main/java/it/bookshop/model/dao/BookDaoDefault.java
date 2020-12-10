package it.bookshop.model.dao;

import java.sql.Date;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

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
	public Book findById(Long bookId) {
		return this.getSession().createQuery("FROM Book b WHERE b.id = :id", Book.class)
				.setParameter("id", bookId).getSingleResult();
	}
	
	public Book findByTitle(String title) {
		//TODO Wrong implementation
		return this.getSession().createQuery("FROM Book b WHERE b.title = :title", Book.class).setParameter("title", title).getSingleResult();
	}
	
	@Override
	public Book create(String isbn,String title, Date publish_date, int copies, double price, User seller, int pages, String summary, String cover) {
		Book b = new Book();
		b.setIsbn(isbn);
		b.setTitle(title);
		b.setPubblish(publish_date);
		b.setCopies(copies);
		b.setPrice(price);
		b.setSeller(seller);
		b.setPages(pages);
		b.setSummary(summary);
		b.setCover(cover);
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

}
