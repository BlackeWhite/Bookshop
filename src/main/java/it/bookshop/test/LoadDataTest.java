package it.bookshop.test;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import it.bookshop.model.dao.AuthorDao;
import it.bookshop.model.dao.BookDao;
import it.bookshop.model.dao.GenreDao;
import it.bookshop.model.dao.ShoppingCartDao;
import it.bookshop.model.dao.RoleDao;
import it.bookshop.model.dao.OrderDao;
import it.bookshop.model.dao.UserDetailsDao;
import it.bookshop.model.entity.Author;
import it.bookshop.model.entity.Book;
import it.bookshop.model.entity.Genre;
import it.bookshop.model.entity.Order;
import it.bookshop.model.entity.ShoppingCart;
import it.bookshop.model.entity.Role;
import it.bookshop.model.entity.User;
import it.bookshop.services.BookService;



public class LoadDataTest {

	public static void main(String ...args) {
		
		
//		logger.info("Entrato ...");
		try (AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(DataServiceConfigTest.class)) {

			SessionFactory sf = ctx.getBean("sessionFactory", SessionFactory.class);
			
			// Create every bean of Dao classes
			AuthorDao authorDao = ctx.getBean(AuthorDao.class);
			UserDetailsDao userDao = ctx.getBean(UserDetailsDao.class);
			RoleDao roleDao = ctx.getBean(RoleDao.class);
			BookDao bookdao = ctx.getBean(BookDao.class);
			GenreDao genredao = ctx.getBean(GenreDao.class);
			RoleDao roledao = ctx.getBean(RoleDao.class);
			UserDetailsDao userdao = ctx.getBean(UserDetailsDao.class);
			OrderDao orderdao = ctx.getBean(OrderDao.class);
			ShoppingCartDao offerdao = ctx.getBean(ShoppingCartDao.class);
			
			
			try (Session session = sf.openSession()) {
				
				// ------------------------ DAO TEST ------------------------\\
				
				// Start the bean' sessions
				authorDao.setSession(session);
				userDao.setSession(session);
				roleDao.setSession(session);
				bookdao.setSession(session);
				
				Date date = new Date(System.currentTimeMillis());
				
				//************* TEST 1.1 - Authors creation *************\\
				session.beginTransaction();

				Author a1 = authorDao.create("Italo", "Svevo",date,"Italia","prova1-12","img.png");
				Author a2 = authorDao.create("Cesare", "Pavese",date,"Italia","prova2-13","img1.png");
							
				session.getTransaction().commit();
				
				//************* TEST 1.2 - Book creation + link to the author + link to the genre *************\\
				session.beginTransaction();
				
                // Create a book, find his author, link the book to the author
				//Book b1 = bookdao.create("SE9788804492X948","La coscienza di Zeno", date, 800, "Sinossi", "cover.jpg");
				Author a3 = authorDao.findByNameAndSurname("Italo", "Svevo");
				//a3.addBooks(b1);
				// Create a genre, link the book b1 to the genre
				Genre g1 = genredao.create("Thriller");
			//	g1.addBooks(b1);
				
				session.getTransaction().commit();
				
				//************* TEST 1.3 - Users & Roles creation *************\\
				session.beginTransaction();
				
				// Users creation
				User c = userDao.create("ppFrank", "PippoFranco@mail.com", "1234","Pippo","Franco", date, "Via della Speranza", "Ancona", 60121, "Italia");
				User v = userDao.create("redMario", "MarioRossi@mail.com", "1233", "Mario", "Rossi", date, "Via del Disappunto", "Macerata", 60110, "Italia");		
				// Roles creation
				Role r1 = roleDao.create("Compratore");
				Role r2 = roleDao.create("Venditore");
				
				r1.addUsers(c);
				r2.addUsers(v);
				
				session.getTransaction().commit();
				
				//************* TEST 1.4 - Purchase and Offer *************\\
				session.beginTransaction();
				
				// Buyer make a purchase 
				//Order p1 = orderdao.create(c,v, b1, 1,12,date);
				// Seller offers -sells- a book 
				//ShoppingCart o1 = offerdao.create(v, b1, 10, 12);
				
				session.getTransaction().commit();
			}

		} catch (Exception e) {
//			logger.error("Eccezione: " + e.getMessage());
			e.printStackTrace(System.err);
		}
	
	}
	
}


