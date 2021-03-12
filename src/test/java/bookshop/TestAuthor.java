package bookshop;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Date;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import it.bookshop.model.dao.AuthorDao;
import it.bookshop.model.entity.Author;
import it.bookshop.test.DataServiceConfigTest;

public class TestAuthor { 
	
	private AnnotationConfigApplicationContext ctx;
	private AuthorDao authorDao;
	private SessionFactory sf;

	/*
	 * permette il monitoraggio le fasi di test
	 */
	@BeforeAll
	static void setup() {
		System.out.println("Preparazione environment suite di test");
		// inserire eventualmente strutture dati 

	}

	@AfterAll
	static void tearDown() {
		System.out.println("Pulizia environment suite di test");
		// inserire eventualmente strutture dati 

	}

	/*
	 * inizializza e pulisce l'ambiente di test a fine esecuzione
	 */
	@BeforeEach
	void openContext() {
		
		System.out.println("Preparazione nuovo environment");
		ctx = new AnnotationConfigApplicationContext(DataServiceConfigTest.class);
		authorDao = ctx.getBean("authorDao", AuthorDao.class);
		sf = ctx.getBean("sessionFactory", SessionFactory.class);
	}
	
	@AfterEach
	void closeContext() {
		System.out.println("Pulizia nuovo environment");
		ctx.close();
	}


	@Test
	void testBeginCommitTransaction() {
		
		/**
		 * Test that dao stores session
		 */

		Session s = sf.openSession();
		assertTrue(s.isOpen());
		s.beginTransaction();
		authorDao.setSession(s);
		assertEquals(s, authorDao.getSession());
		assertSame(s, authorDao.getSession());
		assertTrue(authorDao.getSession().getTransaction().isActive());
		s.getTransaction().commit();
		assertFalse(authorDao.getSession().getTransaction().isActive());
		
	}
	
	@Test
	void testCreateAuthorDuplicate() {
		/**
		 * Test that it's not possible to create two authors with same name and surname
		 */

		Session s = sf.openSession();
		authorDao.setSession(s);
		//add new author
		Author newAuthor1 = authorDao.create("testName", "testSurname");
		assertThrows(RuntimeException.class, ()->{authorDao.create(newAuthor1.getName(), newAuthor1.getSurname());});
	}
	
}
	
	
