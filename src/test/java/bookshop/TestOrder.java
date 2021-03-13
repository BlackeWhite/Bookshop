package bookshop;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
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

	private SessionFactory sf;
	private OrderDao orderDao;
	private AnnotationConfigApplicationContext ctx;
	
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
	void testBookOrderCreation() {
		
		BookOrderDao bookOrderDao = ctx.getBean(BookOrderDao.class);
		BookDao bookDao = ctx.getBean(BookDao.class);
		UserDetailsDao userDao= ctx.getBean(UserDetailsDao.class);
		
		Session s = sf.openSession();
		s.beginTransaction();
		
		orderDao.setSession(s);
		bookOrderDao.setSession(s);
		bookDao.setSession(s);
		userDao.setSession(s);
		
		Book a = bookDao.create("AAAA", "Book1", null, null, 1, 20, null, 10, "summary1", "image1", 0.20);
		Book b = bookDao.create("BBBB", "Book2", null, null, 1, 30, null, 20, "summary2", "image2", 0.30);
		Set<BookOrder> books = new HashSet<>();
		BookOrder c = new BookOrder();
		c.setBook(a);
		BookOrder d = new BookOrder();
		d.setBook(b);
		books.add(c);
		books.add(d);
		User u = userDao.create("user", "email", "pass", "test", "surname", null, null, null, 3333, null);
		
		Order o = orderDao.create(u, null, books, "Address", "Payment", 5);
		Long id = o.getId();
		
		s.getTransaction().commit();
		
		Order o2 = orderDao.findById(id);
		assertNotNull(o2);
		
		assertEquals(o, o2);
		assertEquals(books, o2.getBooks());

			
	}
	}
	
	
	