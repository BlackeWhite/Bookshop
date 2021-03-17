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

import it.bookshop.model.dao.CouponDao;
import it.bookshop.model.entity.Coupon;
import it.bookshop.test.DataServiceConfigTest;

public class TestCoupon {

	private SessionFactory sf;
	private AnnotationConfigApplicationContext ctx;
	private CouponDao couponDao;
	
	@BeforeEach
	void openContext() {
		ctx = new AnnotationConfigApplicationContext(DataServiceConfigTest.class);
		couponDao = ctx.getBean(CouponDao.class);
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

		couponDao.setSession(s);
		assertEquals(s, couponDao.getSession()); // verifica se è la stessa sessione
		s.getTransaction().commit();

		assertFalse(s.getTransaction().isActive());

	}
	
	//Test creazione, lettura e eliminazione dei coupon
	@Test
	void testCouponCreationAndDeletion() {
		Date d = new Date(System.currentTimeMillis());
		Session s = sf.openSession();
		s.beginTransaction();
		couponDao.setSession(s);
		
		Coupon a = couponDao.create("TESTCODE", 50, d);
		s.getTransaction().commit();
		
		Long id = a.getCouponID();
		Coupon b = couponDao.findByCode("NOTEXIST");
		assertNull(b);
		Coupon c = couponDao.findByCode("TESTCODE");
		assertEquals(id, c.getCouponID());
		assertEquals(50, c.getDiscount());
		assertEquals(d.getTime(), c.getExpireDate().getTime());
		
		s.beginTransaction();
		couponDao.delete(c);
		s.getTransaction().commit();
		assertNull(couponDao.findByCode("TESTCODE"));
	}
	
	@Test
	void testCodeUniqueConstraint() {
		Date d = new Date(System.currentTimeMillis());
		Session s = sf.openSession();
		s.beginTransaction();
		couponDao.setSession(s);
		
		couponDao.create("CODE", 20, d);
		assertThrows(ConstraintViolationException.class, () -> {couponDao.create("CODE", 39, d);});
		
		//Quando viene lanciata un eccezzione non si può chiudere la transazione, altrimenti restituisce eccezione
		//Chiudiamo invece direttamente la sessione altrimenti potrebbe andare in deadlock con l'altro test
		s.close();
	}
}
