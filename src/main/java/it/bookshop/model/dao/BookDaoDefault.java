package it.bookshop.model.dao;

import java.sql.Date;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import it.bookshop.model.entity.Book;


@Transactional
@Repository("bookDao") 
public class BookDaoDefault extends DefaultDao implements BookDao{
	
	@Override
	public List<Book> findAll() {
		return this.getSession().createQuery("FROM Book b", Book.class).getResultList();
	}
	
	@Override
	public Book findByIsbn(String isbn) {
		return this.getSession().createQuery("FROM Book b WHERE b.isbn = :isbn", Book.class).setParameter("isbn", isbn).getSingleResult();
	}
	
	public Book findByTitle(String title) {
		return this.getSession().createQuery("FROM Book b WHERE b.title = :title", Book.class).setParameter("title", title).getSingleResult();
	}
	
	@Override
	public Book create(String isbn,String title, Date publish_date, int num_of_pages, String summary, String cover) {
		Book b = new Book();
		b.setIsbn(isbn);
		b.setTitle(title);
		b.setPubblish(publish_date);
		b.setPages(num_of_pages);
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
