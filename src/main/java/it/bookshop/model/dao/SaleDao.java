package it.bookshop.model.dao;

import java.util.List;

import org.hibernate.Session;

import it.bookshop.model.entity.Book;
import it.bookshop.model.entity.Sale;
import it.bookshop.model.entity.User;

public interface SaleDao {

	public Session getSession();
	public void setSession(Session session);
	
	public Sale findById(Long sellerId, String bookIsbn);
	public Sale create(User seller, Book book, int copies, double price);
	public Sale update(Sale sale);
	public List<Sale> findAll();
	public void delete(Sale sale);
}
