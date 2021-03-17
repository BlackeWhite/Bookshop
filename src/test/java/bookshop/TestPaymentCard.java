package bookshop;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.sql.Date;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import it.bookshop.model.dao.PaymentCardDao;
import it.bookshop.model.dao.UserDetailsDao;
import it.bookshop.model.entity.PaymentCard;
import it.bookshop.model.entity.User;
import it.bookshop.test.DataServiceConfigTest;

public class TestPaymentCard {

	private SessionFactory sf;
	private AnnotationConfigApplicationContext ctx;
	private PaymentCardDao paymentCardDao;
	private UserDetailsDao userDao;
	
	@BeforeEach
	void openContext() {
		ctx = new AnnotationConfigApplicationContext(DataServiceConfigTest.class);
		paymentCardDao = ctx.getBean(PaymentCardDao.class);
		userDao = ctx.getBean(UserDetailsDao.class);
		sf = ctx.getBean("sessionFactory", SessionFactory.class);
	}

	@AfterEach
	void closeContext() {
		ctx.close();
	}
	
	@Test
	void testBeginCommitTransaction() {

		Session s = sf.openSession();

		assertTrue(s.isOpen());

		s.beginTransaction();

		paymentCardDao.setSession(s);
		assertEquals(s, paymentCardDao.getSession()); // verifica se è la stessa sessione
		s.getTransaction().commit();

		assertFalse(s.getTransaction().isActive());
	}
	
	//Test creazione, lettura e eliminazione delle carte di pagamento
	@Test
	void testCardCreationAndDeletion() {
		Date d = new Date(System.currentTimeMillis());
		Session s = sf.openSession();
		s.beginTransaction();
		paymentCardDao.setSession(s);
		userDao.setSession(s);
		
		User u = userDao.create("cardOwner", "email", "pass", "test", "surname", new Date(System.currentTimeMillis()),
				"test", "test", 3333, "test");
		PaymentCard a = paymentCardDao.create("VISA", "25236372723798", d, u);
		s.getTransaction().commit();
		
		PaymentCard b = paymentCardDao.findById(a.getId());
		assertEquals("VISA", b.getType());
		assertEquals("25236372723798", b.getNumber());
		assertEquals(d.getTime(), b.getExpirationDate().getTime());
		assertEquals(u.getUserID(), b.getUser().getUserID());
		
		
		s.beginTransaction();
		userDao.delete(u);
		paymentCardDao.delete(b);
		s.getTransaction().commit();
		
		assertNull(paymentCardDao.findById(a.getId()));
		assertNull(userDao.findUserByUsername("cardOwner"));
	}
	
	//Un particolare utente non può aggiungere due carte con lo stesso numero
	@Test
	void testUserAndCardUniqueConstraint() {
		Date d = new Date(System.currentTimeMillis());
		Session s = sf.openSession();
		s.beginTransaction();
		paymentCardDao.setSession(s);
		userDao.setSession(s);
		
		User u1 = userDao.create("forgetfulOwner", "email", "pass", "test", "surname", new Date(System.currentTimeMillis()),
				"test", "test", 3333, "test");
		User u2 = userDao.create("otherOwner", "email", "pass", "test", "surname", new Date(System.currentTimeMillis()),
				"test", "test", 3333, "test");
		paymentCardDao.create("VISA", "25236372723798", d, u1);
		paymentCardDao.create("VISA", "25236372723798", d, u2);
		assertThrows(ConstraintViolationException.class, () -> {paymentCardDao.create("VISA", "25236372723798", d, u1);});
		
		//Quando viene lanciata un eccezzione non si può chiudere la transazione, altrimenti restituisce eccezione
		//Chiudiamo invece direttamente la sessione altrimenti potrebbe andare in deadlock con l'altro test
		s.close();
	}
	
}
