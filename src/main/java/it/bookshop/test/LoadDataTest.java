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
import it.bookshop.model.dao.RoleDao;
import it.bookshop.model.dao.UserDetailsDao;
import it.bookshop.model.entity.Author;



public class LoadDataTest {

	public static void main(String ...args) {
		
		
//		logger.info("Entrato ...");
		try (AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(DataServiceConfigTest.class)) {

			SessionFactory sf = ctx.getBean("sessionFactory", SessionFactory.class);
			
			AuthorDao authorDao = ctx.getBean(AuthorDao.class);
			UserDetailsDao userDao = ctx.getBean(UserDetailsDao.class);
			RoleDao roleDao = ctx.getBean(RoleDao.class);
			
			try (Session session = sf.openSession()) {
				
				authorDao.setSession(session);
				userDao.setSession(session);
				roleDao.setSession(session);
				
				Date date = new Date(System.currentTimeMillis());
			
				// phase 1 : add data to database
				
				session.beginTransaction();

				Author a1 = authorDao.create("Italo", "Svevo",date,"Italia","prova1-12","img.png");
				Author a2 = authorDao.create("Cesare", "Pavese",date,"Italia","prova2-13","img1.png");
							
				session.getTransaction().commit();
				
			}

		} catch (Exception e) {
//			logger.error("Eccezione: " + e.getMessage());
			e.printStackTrace(System.err);
		}
	
	}
	
}


