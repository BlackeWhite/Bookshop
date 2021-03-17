package bookshop;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import it.bookshop.app.DataServiceConfig;
import it.bookshop.model.dao.AuthorDao;
import it.bookshop.model.dao.BookDao;
import it.bookshop.model.dao.BookOrderDao;
import it.bookshop.model.dao.GenreDao;
import it.bookshop.model.dao.OrderDao;
import it.bookshop.model.dao.RoleDao;
import it.bookshop.model.dao.UserDetailsDao;
import it.bookshop.model.entity.Book;
import it.bookshop.model.entity.Genre;
import it.bookshop.model.entity.Role;
import it.bookshop.model.entity.User;
import it.bookshop.services.BookService;
import it.bookshop.services.UserService;
import it.bookshop.test.DataServiceConfigTest;

public class TestBook {
	
	private static SessionFactory sf;
	private static AnnotationConfigApplicationContext ctx;

		
	@BeforeEach
	void openContext() {
		ctx = new AnnotationConfigApplicationContext(DataServiceConfigTest.class);
		sf = ctx.getBean("sessionFactory", SessionFactory.class);
	}

	@AfterEach
	void closeContext() {
		ctx.close();
	}

		
	@Test
	void testBeginCommitTransaction() {
	
			Session s = sf.openSession();
			BookDao bookdao = ctx.getBean(BookDao.class);

			assertTrue(s.isOpen()); // verifica se comunica con il db

			s.beginTransaction();

			bookdao.setSession(s);
			assertEquals(s, bookdao.getSession()); // verifica se è la stessa sessione
			s.getTransaction().commit();

			assertFalse(s.getTransaction().isActive());

	}

	@Test
	void testCreateofBook() {
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

				DateFormat date = new SimpleDateFormat("dd-MM-yyyy");
				java.util.Date date1 = null;

				try {
					date1 = date.parse("21-01-2015");
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Date publish_date1 = new Date(date1.getTime());
				// creo un genere
				Genre g1 = genredao.create("Poema");
				// venditore
				User u = userdao.findUserByUsername("unitTesting");
				if (u == null) {
					u = userdao.create("unitTesting", "email", "pass", "test", "surname",
							new Date(System.currentTimeMillis()), "test", "test", 3333, "test");
				}

				Book b = bookdao.create("CCCC", "Book2", publish_date1, publish_date1, 1, 30, u, 20, "summary2",
						"image2", 0.30);
				g1.addBooks(b);

				Book b1 = bookdao.findById(b.getId());
				session.getTransaction().commit();
				assertEquals(b.getClass().getSimpleName(), "Book");
				assertEquals(b.getGenres().iterator().next(), g1); // genere del libro uguale al genere creato
				assertEquals(b, b1); // verifico che il libro è stato creato nel db
			}
		}
	}
}
