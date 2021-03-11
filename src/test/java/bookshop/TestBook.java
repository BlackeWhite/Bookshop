package bookshop;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import it.bookshop.app.DataServiceConfig;
import it.bookshop.model.dao.AuthorDao;
import it.bookshop.model.dao.BookDao;
import it.bookshop.model.dao.GenreDao;
import it.bookshop.model.dao.RoleDao;
import it.bookshop.model.dao.UserDetailsDao;
import it.bookshop.model.entity.Book;
import it.bookshop.model.entity.Genre;
import it.bookshop.model.entity.User;
import it.bookshop.services.BookService;
import it.bookshop.services.UserService;
import it.bookshop.test.DataServiceConfigTest;

public class TestBook {
	@Test
	void testBeginCommitTransaction() {
		try (AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(DataServiceConfig.class)) {
			SessionFactory sf = ctx.getBean("sessionFactory", SessionFactory.class);
			BookDao bookdao = ctx.getBean(BookDao.class);
			Session s = sf.openSession();

			assertTrue(s.isOpen());

			s.beginTransaction();

			bookdao.setSession(s);
			assertEquals(s, bookdao.getSession()); // verifica se è la stessa sessione
			s.getTransaction().commit();

			assertFalse(s.getTransaction().isActive());
		}

	}

	void testSameGenreForBook() {
		try (AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(
				DataServiceConfigTest.class)) {

			SessionFactory sf = ctx.getBean("sessionFactory", SessionFactory.class);
			BookDao bookdao = ctx.getBean(BookDao.class);
			GenreDao genredao = ctx.getBean(GenreDao.class);
			
			try (Session session = sf.openSession()) {

				// Start the bean' sessions
				genredao.setSession(session);
				bookdao.setSession(session);
				
	
				session.beginTransaction();

				DateFormat date = new SimpleDateFormat("dd-MM-yyyy");
				java.util.Date date1 = null;
	
				try {
					date1 = date.parse("21-01-2005");
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Date publish_date1 = new Date(date1.getTime());
				
				Genre g1 = genredao.create("Poema");
				
				// da finire (non riesco ad usare i servizi, forse possono essere usati solo i dao )
			}
		}
	}

}
