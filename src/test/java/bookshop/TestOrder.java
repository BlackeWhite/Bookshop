package bookshop;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import it.bookshop.model.dao.BookDao;
import it.bookshop.model.dao.BookOrderDao;
import it.bookshop.model.dao.OrderDao;
import it.bookshop.model.dao.UserDetailsDao;
import it.bookshop.model.entity.Book;
import it.bookshop.model.entity.BookOrder;
import it.bookshop.model.entity.Order;
import it.bookshop.model.entity.User;
import it.bookshop.test.DataServiceConfigTest;

public class TestOrder {

	private static SessionFactory sf;
	private OrderDao orderDao;
	private static AnnotationConfigApplicationContext ctx;
	private static Book a;
	private static Book b;
	private static User u;
	
	@BeforeAll
	static void createTestBooksAndUser() {
		ctx = new AnnotationConfigApplicationContext(DataServiceConfigTest.class);
		sf = ctx.getBean("sessionFactory", SessionFactory.class);
		
		BookDao bookDao = ctx.getBean(BookDao.class);
		UserDetailsDao userDao = ctx.getBean(UserDetailsDao.class);
		
		Session s = sf.openSession();
		s.beginTransaction();
		
		bookDao.setSession(s);
		userDao.setSession(s);

		a = bookDao.create("AAAA", "Book1", null, null, 1, 20, null, 10, "summary1", "image1", 0.20);
		b = bookDao.create("BBBB", "Book2", null, null, 1, 30, null, 20, "summary2", "image2", 0.30);
		u = userDao.findUserByUsername("unitTesting");
		if(u == null) {
		u = userDao.create("unitTesting", "email", "pass", "test", "surname", new Date(System.currentTimeMillis()),
				"test", "test", 3333, "test");
		}
		
		s.getTransaction().commit();
		ctx.close();
	}
	
	@AfterAll
	static void deleteTestBooksAndUser() {
		ctx = new AnnotationConfigApplicationContext(DataServiceConfigTest.class);
		sf = ctx.getBean("sessionFactory", SessionFactory.class);
		
		BookDao bookDao = ctx.getBean(BookDao.class);
		UserDetailsDao userDao = ctx.getBean(UserDetailsDao.class);
		BookOrderDao bookOrderDao = ctx.getBean(BookOrderDao.class);
		OrderDao orderDao = ctx.getBean(OrderDao.class);
		
		Session s = sf.openSession();
		s.beginTransaction();
		
		bookDao.setSession(s);
		userDao.setSession(s);
		bookOrderDao.setSession(s);
		orderDao.setSession(s);
		
		bookDao.delete(a);
		bookDao.delete(b);
		
		s.getTransaction().commit();
		ctx.close();
	}

	@BeforeEach
	void openContext() {
		ctx = new AnnotationConfigApplicationContext(DataServiceConfigTest.class);
		orderDao = ctx.getBean(OrderDao.class);
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

		orderDao.setSession(s);
		assertEquals(s, orderDao.getSession()); // verifica se è la stessa sessione
		s.getTransaction().commit();

		assertFalse(s.getTransaction().isActive());

	}

	@Test
	void testOrderCreation() {

		Session s = sf.openSession();
		
		s.beginTransaction();
		
		orderDao.setSession(s);
		BookOrderDao bookOrderDao = ctx.getBean(BookOrderDao.class);
		bookOrderDao.setSession(s);
		
		Set<BookOrder> books = new HashSet<>();
		BookOrder c = new BookOrder();
		c.setBook(a);
		
		BookOrder d = new BookOrder();
		d.setBook(b);
		books.add(c);
		books.add(d);
		
		
		Order o = orderDao.create(u, null, books, "Address", "Payment", 5);
		u.getOrders().add(o);
		Long id = o.getId();

		Order o2 = orderDao.findById(id);
		assertNotNull(o2);

		assertEquals(o, o2);
		assertEquals(books, o2.getBooks());
		assertEquals(2, o2.getBooks().size());
		s.getTransaction().commit();
	}
	
	@Test
	void testBookOrderFilterByDates() {
		Session s = sf.openSession();

		orderDao.setSession(s);
		BookOrderDao bookOrderDao = ctx.getBean(BookOrderDao.class);
		bookOrderDao.setSession(s);
		s.beginTransaction();

		BookOrder c = new BookOrder();
		c.setBook(a);
		BookOrder d = new BookOrder();
		d.setBook(b);
		BookOrder e = new BookOrder();
		e.setBook(a);
		
		Order o1 = orderDao.createTest();
		Order o2 = orderDao.createTest();
		
		Date date1 = null;
		Date date2 = null;
		Date date3 = null;
		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
		try {
			date1 = new Date(format.parse("2020/05/01").getTime());
			date2 = new Date(format.parse("2020/06/01").getTime());
			date3 = new Date(format.parse("2020/07/01").getTime());
			c.setPurchasedate(date1);
			d.setPurchasedate(date2);
			e.setPurchasedate(date3);
			bookOrderDao.create(o1, c);
			bookOrderDao.create(o1, d);
			bookOrderDao.create(o2, e);
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		
		//Filtra per data
		List<BookOrder> filtered = bookOrderDao.findbyDate("2020/05/04", "2020/06/30");
		
		//Se eseguito correttamente la lista dovrebbe contenere solo il bookOrder d
		assertEquals(1, filtered.size());
		assertEquals(filtered.get(0).getPurchasedate(), date2);
		
		orderDao.delete(o1);
		orderDao.delete(o2);
		s.getTransaction().commit();
	}
}
