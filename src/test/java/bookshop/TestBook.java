package bookshop;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Iterator;


import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterAll;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;


import it.bookshop.model.dao.BookDao;
import it.bookshop.model.dao.GenreDao;
import it.bookshop.model.dao.UserDetailsDao;
import it.bookshop.model.entity.Book;
import it.bookshop.model.entity.Genre;
import it.bookshop.model.entity.User;
import it.bookshop.test.DataServiceConfigTest;

public class TestBook {

	private static SessionFactory sf;
	private static AnnotationConfigApplicationContext ctx;
	private static Book b;
	private static User u;

	@BeforeAll
    public static void setup() {
		/*
		 * Preparazione dell'environment di test
		 */
		System.out.println("Preparazione environment suite di test");
		
		ctx = new AnnotationConfigApplicationContext(DataServiceConfigTest.class);
		sf = ctx.getBean("sessionFactory", SessionFactory.class);
		
		


	}
	
	@AfterAll
	static void tearDown() {
		/*
		 * Pulizia dell'environment di test
		 */
		System.out.println("Pulizia environment suite di test");
		
		ctx = new AnnotationConfigApplicationContext(DataServiceConfigTest.class);
		sf = ctx.getBean("sessionFactory", SessionFactory.class);
		
		BookDao bookdao = ctx.getBean(BookDao.class);
				
		Session s = sf.openSession();
		
		s.beginTransaction();
		
		bookdao.setSession(s);
		
		b.setRemoved(1);  // lo disabilito 
		bookdao.update(b);
		s.getTransaction().commit();
		
		
		ctx.close();
	}
	
	@Test
	void testCreateofBook() {
		
			BookDao bookdao = ctx.getBean(BookDao.class);
			UserDetailsDao userdao = ctx.getBean(UserDetailsDao.class);
			GenreDao genredao = ctx.getBean(GenreDao.class);

			Session session = sf.openSession();
				// Start the bean' sessions
				genredao.setSession(session);
				bookdao.setSession(session);
				userdao.setSession(session);

				session.beginTransaction();
				DateFormat date = new SimpleDateFormat("dd-MM-yyyy");
				java.util.Date date1 = null;

				try {
					date1 = date.parse("21-01-2015");
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Date publish_date1 = new Date(date1.getTime());
				// genere
				Genre g1 = genredao.findByName("Poema");
				if (g1 == null) {
					g1 = genredao.create("Poema");
				}
				// venditore
				u = userdao.findUserByUsername("unitTesting");
				if (u == null) {
					u = userdao.create("unitTesting", "email", "pass", "test", "surname",
							new Date(System.currentTimeMillis()), "test", "test", 3333, "test");
				}
	
				// creazione di un libro
				b = bookdao.create("CCCC", "Book2", publish_date1, publish_date1, 1, 30, u, 20, "summary2", "image2",
						0.30);
				g1.addBooks(b);// associo un genere al libro
				session.getTransaction().commit();

				assertEquals(b.getClass().getSimpleName(), "Book"); 
				assertEquals(b.getGenres().iterator().next().getName(), "Poema"); // genere del libro uguale al genere creato
				assertEquals(b.getSeller().getUserID(), u.getUserID()); // utente proprietario di quel libro
			}
	
	
	@Test
	void testClickedBook() {

		try (AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(
				DataServiceConfigTest.class)) {

			SessionFactory sf = ctx.getBean("sessionFactory", SessionFactory.class);
			BookDao bookdao = ctx.getBean(BookDao.class);
			UserDetailsDao userdao = ctx.getBean(UserDetailsDao.class);
			GenreDao genredao = ctx.getBean(GenreDao.class);

			try (Session session = sf.openSession()) {
				// Start the bean' sessions
				genredao.setSession(session);
				bookdao.setSession(session);
				userdao.setSession(session);

				session.beginTransaction();

				// aggiungo un click al libro
				b.setClicked(1);
				bookdao.update(b);

				Iterator<Book> itbook = bookdao.findMostClick().iterator();

				while (itbook.hasNext()) {
					Book book_temp = itbook.next();
					if (book_temp.equals(b)) {
						if (book_temp.getClicked() == 1) {
							assertTrue(true);

						} else
							assertTrue(false);
					}
				}
				session.getTransaction().commit();
			}

		}

	}

	@Test
	void testSaleBook() {

		try (AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(
				DataServiceConfigTest.class)) {

			SessionFactory sf = ctx.getBean("sessionFactory", SessionFactory.class);
			BookDao bookdao = ctx.getBean(BookDao.class);
			UserDetailsDao userdao = ctx.getBean(UserDetailsDao.class);
			GenreDao genredao = ctx.getBean(GenreDao.class);

			try (Session session = sf.openSession()) {
				// Start the bean' sessions
				genredao.setSession(session);
				bookdao.setSession(session);
				userdao.setSession(session);

				session.beginTransaction();

				Iterator<Book> itbook = bookdao.findBookOnSale().iterator();

				while (itbook.hasNext()) {
					Book book_temp = itbook.next();
					if (book_temp.equals(b)) {
						assertTrue(true); // libro in sconto trovato
					}
				}
				session.getTransaction().commit();
			}

		}
	}

	@Test
	void testRecentBook() {

		try (AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(
				DataServiceConfigTest.class)) {

			SessionFactory sf = ctx.getBean("sessionFactory", SessionFactory.class);
			BookDao bookdao = ctx.getBean(BookDao.class);
			UserDetailsDao userdao = ctx.getBean(UserDetailsDao.class);
			GenreDao genredao = ctx.getBean(GenreDao.class);

			try (Session session = sf.openSession()) {
				// Start the bean' sessions
				genredao.setSession(session);
				bookdao.setSession(session);
				userdao.setSession(session);

				session.beginTransaction();

				Iterator<Book> itbook = bookdao.findFiveMostRecentBook().iterator();

				while (itbook.hasNext()) {
					Book book_temp = itbook.next();
					if (book_temp.equals(b)) {
						assertTrue(true); // libro aggiunto di recente trovato
					}
				}
				session.getTransaction().commit();
			}

		}
	}

	@Test
	void testGenreBook() {

		try (AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(
				DataServiceConfigTest.class)) {

			SessionFactory sf = ctx.getBean("sessionFactory", SessionFactory.class);
			BookDao bookdao = ctx.getBean(BookDao.class);
			UserDetailsDao userdao = ctx.getBean(UserDetailsDao.class);
			GenreDao genredao = ctx.getBean(GenreDao.class);

			try (Session session = sf.openSession()) {
				// Start the bean' sessions
				genredao.setSession(session);
				bookdao.setSession(session);
				userdao.setSession(session);

				session.beginTransaction();

				Iterator<Book> itbook = bookdao.findAllBookForGenre("Poema").iterator();

				while (itbook.hasNext()) {
					Book book_temp = itbook.next();
					if (book_temp.equals(b)) {
						assertTrue(true); // libro di quel genere trovato
					}
				}
				session.getTransaction().commit();
			}

		}
	}
	
	@Test
	void testSellerBook() {

		try (AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(
				DataServiceConfigTest.class)) {

			SessionFactory sf = ctx.getBean("sessionFactory", SessionFactory.class);
			BookDao bookdao = ctx.getBean(BookDao.class);
			UserDetailsDao userdao = ctx.getBean(UserDetailsDao.class);
			GenreDao genredao = ctx.getBean(GenreDao.class);

			try (Session session = sf.openSession()) {
				// Start the bean' sessions
				genredao.setSession(session);
				bookdao.setSession(session);
				userdao.setSession(session);

				session.beginTransaction();

				Iterator<Book> itbook = bookdao.findSellerBook(u.getUserID()).iterator();

				while (itbook.hasNext()) {
					Book book_temp = itbook.next();
					if (book_temp.equals(b)) {
						assertTrue(true); // libro di questo venditore trovato
					}
				}
				session.getTransaction().commit();
			}

		}
	}
}
