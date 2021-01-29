package it.bookshop.model.dao;

import java.sql.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import it.bookshop.model.entity.PaymentCard;
import it.bookshop.model.entity.User;

@Repository("paymentCardDao")
public class PaymentCardDaoDefault extends DefaultDao implements PaymentCardDao {

	@Override
	public List<PaymentCard> findAll() {
		return this.getSession().createQuery("FROM PaymentCard c", PaymentCard.class).getResultList();
	}

	@Override
	public PaymentCard findById(Long id) {
		return this.getSession().createQuery("FROM PaymentCard c WHERE c.id = :id", PaymentCard.class).
				setParameter("id", id).getSingleResult();
	}

	@Override
	public PaymentCard create(String type, String number, Date expirationDate, User user) {
		PaymentCard card = new PaymentCard();
		card.setType(type);
		card.setNumber(number);
		card.setExpirationDate(expirationDate);
		card.setUser(user);
		this.getSession().save(card);
		return card;
	}

	@Override
	public PaymentCard update(PaymentCard card) {
		return (PaymentCard) getSession().merge(card);
	}

	@Override
	public void delete(PaymentCard card) {
		getSession().delete(card);
	}

}
