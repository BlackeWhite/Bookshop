package it.bookshop.model.dao;

import java.sql.Date;
import java.util.List;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import it.bookshop.model.entity.Book;
import it.bookshop.model.entity.Purchase;
import it.bookshop.model.entity.PurchaseId;
import it.bookshop.model.entity.User;

@Transactional
@Repository("PurchaseDao") //@Repository  is a specialization of @Component
public class PurchaseDaoDefault extends DefaultDao implements PurchaseDao {


	@Override
	public Purchase findById(Long buyerId, Long sellerId, String bookIsbn, Date date) {
		//Create composite primary key
		PurchaseId id = new PurchaseId(buyerId, sellerId, bookIsbn, date);
		return getSession().find(Purchase.class, id);
	}

	@Override
	public Purchase create(User buyer, User seller, Book book, int copies, double total_price) {
		Purchase p = new Purchase();
		/*
		PurchaseId id = new PurchaseId(buyer.getId(), sellerId.getId(), bookIsbn.getIsbn(), date);
		p.setId(id);
		*/
		p.setBuyer(buyer);
		p.setSeller(seller);
		p.setBook(book);
		p.setCopies(copies);
		p.setTotal_price(total_price);
		getSession().save(p);
		return p;
	}

	@Override
	public Purchase update(Purchase purchase) {
		return (Purchase) getSession().merge(purchase);
	}

	@Override
	public List<Purchase> findAll() {
		return getSession().createQuery("select p from PURCHASES", Purchase.class).getResultList();
	}

	@Override
	public void delete(Purchase purchase) {
		getSession().delete(purchase);
	}

}
