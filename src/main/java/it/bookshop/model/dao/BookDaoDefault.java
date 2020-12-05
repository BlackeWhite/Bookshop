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
		return this.getSession().createQuery("FROM book b", Book.class).getResultList();
	}
	
	@Override
	public Book findByIsbn(String isbn) {
		return this.getSession().createQuery("FROM book b WHERE b.isbn = :isbn", Book.class).setParameter("isbn", isbn).getSingleResult();
	}
	
	@Override
	public Book create(String title, Date publish_date, int num_of_pages, String summary, String cover) {
		Book b = new Book();
		b.setTitle(title);
		b.setDate(publish_date);
		b.setPages(num_of_pages);
		b.setSummary(summary);
		b.setCover(cover);
		
		this.getSession().save(b);
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
