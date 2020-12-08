package it.bookshop.model.dao;

import java.util.List;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import it.bookshop.model.entity.Book;
import it.bookshop.model.entity.Offer;
import it.bookshop.model.entity.OfferId;
import it.bookshop.model.entity.User;

@Transactional
@Repository("offerDao") //@Repository  is a specialization of @Component
public class OfferDaoDefault extends DefaultDao implements OfferDao {

	@Override
	public Offer findById(Long sellerId, String bookIsbn) {
		//Creating composite primary key
		OfferId id = new OfferId(sellerId, bookIsbn);
		return getSession().find(Offer.class, id);
	}

	@Override
	public Offer create(User seller, Book book, int copies, double price) {
		Offer o = new Offer();
		OfferId id = new OfferId(seller.getUserID(), book.getIsbn());
		o.setId(id);
		o.setSeller(seller);
		o.setBook(book);
		o.setCopies(copies);
		o.setPrice(price);
		getSession().save(o);
		return o;
	}

	@Override
	public Offer update(Offer offer) {
		return (Offer) getSession().merge(offer);
	}

	@Override
	public List<Offer> findAll() {
		return getSession().createQuery("FROM Offer o", Offer.class).getResultList();
	}

	@Override
	public void delete(Offer offer) {
		getSession().delete(offer);
	}

	@Override
	public List<Offer> findSellerOffers(Long sellerId) {
		return getSession().createQuery("FROM Offer o WHERE o.seller = :id", Offer.class)
				.setParameter("id", sellerId).getResultList();
	}

	@Override
	public List<Offer> findBookOffers(String bookIsbn) {
		return getSession().createQuery("FROM Offer o WHERE o.book = :isbn", Offer.class)
				.setParameter("isbn", bookIsbn).getResultList();
	}

}
