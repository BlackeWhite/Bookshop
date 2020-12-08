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
import it.bookshop.model.dao.OfferDao;
import it.bookshop.model.dao.PurchaseDao;
import it.bookshop.model.dao.RoleDao;
import it.bookshop.model.dao.UserDetailsDao;
import it.bookshop.model.entity.Author;
import it.bookshop.model.entity.Book;
import it.bookshop.model.entity.Genre;
import it.bookshop.model.entity.Offer;
import it.bookshop.model.entity.Purchase;
import it.bookshop.model.entity.Role;
import it.bookshop.model.entity.User;
import it.bookshop.services.BookService;



public class LoadDataTest {

	public static void main(String ...args) {
		
		
//		logger.info("Entrato ...");
		try (AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(DataServiceConfigTest.class)) {

			SessionFactory sf = ctx.getBean("sessionFactory", SessionFactory.class);
			
			AuthorDao authorDao = ctx.getBean(AuthorDao.class);
			UserDetailsDao userDao = ctx.getBean(UserDetailsDao.class);
			RoleDao roleDao = ctx.getBean(RoleDao.class);
			BookDao bookdao = ctx.getBean(BookDao.class);
			GenreDao genredao = ctx.getBean(GenreDao.class);
			RoleDao roledao = ctx.getBean(RoleDao.class);
			UserDetailsDao userdao = ctx.getBean(UserDetailsDao.class);
			PurchaseDao purchasedao = ctx.getBean(PurchaseDao.class);
			OfferDao offerdao = ctx.getBean(OfferDao.class);
			//BookService bookservice = ctx.getBean(BookService.class);
			
			
			try (Session session = sf.openSession()) {
				
				/*
				authorDao.setSession(session);
				userDao.setSession(session);
				roleDao.setSession(session);
				bookdao.setSession(session);
				*/
				
				Date date = new Date(System.currentTimeMillis());
			
				// test 1.1
				
				session.beginTransaction();

				Author a1 = authorDao.create("Italo", "Svevo",date,"Italia","prova1-12","img.png");
				Author a2 = authorDao.create("Cesare", "Pavese",date,"Italia","prova2-13","img1.png");
							
				session.getTransaction().commit();
				
				//test 1.21
				
				session.beginTransaction();
				
               //collego un libro al suo autore e al genere 
				Book b1 = bookdao.create("SE9788804492X948","La coscienza di Zeno",date,800,"piccola intoduzione", "cover.jpg");
				Author a3 = authorDao.findByNameAndSurname("Italo", "Svevo");
				a3.addBooks(b1);
				
				Genre g1 = genredao.create("Thriller");
				g1.addBooks(b1);
				
				session.getTransaction().commit();
				
				
				// test 1.3
				// creazione di un ruolo 
				session.beginTransaction();
				User c = userDao.create("compratore","Pippo@mail.com","1234","Agostino","Dati",date, "strada","Ischitella",71010, "Italia");
				User v = userDao.create("venditore","Pippo2@mail.com","1233","Gostino","Dai",date, "stra","Ischitella",71110, "Italia");		
				Role r1 = roleDao.create("Compratore");
				Role r2 = roleDao.create("Venditore");
				
				r1.addUsers(c);
				r2.addUsers(v);
				
				//servizi rivedere meglio i servizi mancanti 
				//Book b4 = bookservice.create("Italo", "Svevo","SE9788804492X948","La coscienza di Zeno",date,800,"piccola intoduzione", "cover.jpg");
				session.getTransaction().commit();
				
				// test 1.4
				// acquisto di un libro 
				session.beginTransaction();
				Purchase p1 = purchasedao.create(c,v, b1, 1,12,date);
				//venditore mette in vendità un libro 
				Offer o1 = offerdao.create(v, b1, 10, 12);
				session.getTransaction().commit();
			}

		} catch (Exception e) {
//			logger.error("Eccezione: " + e.getMessage());
			e.printStackTrace(System.err);
		}
	
	}
	
}


