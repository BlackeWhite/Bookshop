package it.bookshop.model.dao;

import java.util.List;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import it.bookshop.model.entity.Book;
import it.bookshop.model.entity.Sale;
import it.bookshop.model.entity.SaleId;
import it.bookshop.model.entity.User;

@Transactional
@Repository("saleDao") //@Repository  is a specialization of @Component
public class SaleDaoDefault extends DefaultDao implements SaleDao {

	@Override
	public Sale findById(Long sellerId, String bookIsbn) {
		//Creating composite primary key
		SaleId id = new SaleId(sellerId, bookIsbn);
		return getSession().find(Sale.class, id);
	}

	@Override
	public Sale create(User seller, Book book, int copies, double price) {
		Sale s = new Sale();
		/*
		SaleId id = new SaleId(seller.getId(), book.getIsbn());
		s.setId(id);
		
		*/
		
		s.setSeller(seller);
		s.setBook(book);
		s.setCopies(copies);
		s.setPrice(price);
		getSession().save(s);
		return s;
	}

	@Override
	public Sale update(Sale sale) {
		return (Sale) getSession().merge(sale);
	}

	@Override
	public List<Sale> findAll() {
		return getSession().createQuery("select s from SALES", Sale.class).getResultList();
	}

	@Override
	public void delete(Sale sale) {
		getSession().delete(sale);
	}

}
