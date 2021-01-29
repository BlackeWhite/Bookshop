package it.bookshop.model.dao;

import java.sql.Date;
import java.util.List;

import org.hibernate.Session;

import it.bookshop.model.entity.PaymentCard;
import it.bookshop.model.entity.User;

public interface PaymentCardDao {

	Session getSession();
	public void setSession(Session session);
	
	List<PaymentCard> findAll();
	PaymentCard findById(Long id);
	PaymentCard create(String type, String number, Date expirationDate, User user);
	PaymentCard update(PaymentCard card);
	void delete(PaymentCard card);
}
