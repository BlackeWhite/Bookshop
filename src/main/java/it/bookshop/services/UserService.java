package it.bookshop.services;

import java.sql.Date;
import java.util.List;

import it.bookshop.model.entity.PaymentCard;
import it.bookshop.model.entity.PersonalData;
import it.bookshop.model.entity.User;

public interface UserService {
	
	User findUserByUsername(String username);
	
	User findUserById(long id);
	
	User findUserByEmail(String email);
	
	User create(String username, String email, String password, String name, String surname,
			Date birthdate, String street, String city, long cap, String state);
	
	//From modelAttribute param
	User create(User user);
	
	User update(User user);
	
	void deleteByUsername(String username);
	
	//PaymentCard
	List<PaymentCard> findAllPaymentCards();
	PaymentCard findPaymentCardById(Long id);
	PaymentCard createPaymentCard(PaymentCard card, User user);
	PaymentCard createPaymentCard(String type, String number, Date expirationDate, Long userId);
	PaymentCard updatePaymentCard(PaymentCard card);
	void deletePaymentCard(PaymentCard card);
}
