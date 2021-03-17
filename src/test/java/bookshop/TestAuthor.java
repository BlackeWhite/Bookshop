package bookshop;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Date;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import it.bookshop.model.dao.AuthorDao;
import it.bookshop.model.dao.BookDao;
import it.bookshop.model.dao.UserDetailsDao;
import it.bookshop.model.entity.Author;
import it.bookshop.model.entity.Book;
import it.bookshop.model.entity.User;
import it.bookshop.test.DataServiceConfigTest;


public class TestAuthor { 
	
	private static AnnotationConfigApplicationContext ctx;
	private AuthorDao authorDao;
	private static SessionFactory sf;
	private static Book a;
	private static Book b;
	private static User u;
	
	
	@BeforeAll
	static void setup() {
		/*
		 * Preparazione dell'environment di test
		 */
		System.out.println("Preparazione environment suite di test");
		
		ctx = new AnnotationConfigApplicationContext(DataServiceConfigTest.class);
		sf = ctx.getBean("sessionFactory", SessionFactory.class);
		
		BookDao bookDao = ctx.getBean(BookDao.class);
		UserDetailsDao userDao = ctx.getBean(UserDetailsDao.class);
		
		Session s = sf.openSession();
		s.beginTransaction();
		
		bookDao.setSession(s);
		userDao.setSession(s);
		
		u = userDao.findUserByUsername("sellerTest");
		if(u == null) {
		u = userDao.create("sellerTest", "email", "pass", "test", "surname", new Date(System.currentTimeMillis()),
				"test", "test", 3333, "test");
		}
		
		a = bookDao.create("1111111111111", "Il nome del vento", null, null, 1, 20, u, 10, "summary1", "image1", 0.20);
		b = bookDao.create("2222222222222", "La paura del saggio", null, null, 1, 30, u, 20, "summary2", "image2", 0.30);
		
		s.getTransaction().commit();
	}
	
	@AfterAll
	static void tearDown() {
		/*
		 * Pulizia dell'environment di test
		 */
		System.out.println("Pulizia environment suite di test");
		
		BookDao bookDao = ctx.getBean(BookDao.class);
		UserDetailsDao userDao = ctx.getBean(UserDetailsDao.class);
		
		Session s = sf.openSession();
		
		s.beginTransaction();
		
		bookDao.setSession(s);
		userDao.setSession(s);

		bookDao.delete(a);
		bookDao.delete(b);
		
		ctx.close();
	}

	@Test
	void testBeginCommitTransaction() {
		/*
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
	void testCreateAndFind() {
		/*
		 * Test per la creazione e la ricerca dell'autore
		 */
		Session s = sf.openSession();
		authorDao.setSession(s);
		
		Author newAuthor = authorDao.create(" Patrick", "Rothfuss  ");
		
		try {
			/*
			 * Testa il metodo findById
			 */
			authorDao.findById(newAuthor.getId());
		} catch (Exception e) {
			fail("Exception not expected: " + e.getMessage());
		}
		
		try {
			/*
			 * Testa il metodo findByNameAndSurname
			 */
			authorDao.findByNameAndSurname(newAuthor.getName(), newAuthor.getSurname());
		} catch (Exception e) {
			fail("Exception not expected: " + e.getMessage());
		}
		
		try {
			/*
			 * Testa il metodo findByNameAndSurname con parametri errati (ci si aspetta un'eccezione)
			 */
			Author notFound = authorDao.findByNameAndSurname("foo", "poo");
			assertEquals(notFound, null); 
		} catch (Exception e) {
			assertTrue(true);
		}
		
		s.getTransaction().commit();	
		
		/*
		 * Testa se il metodo findAll trova l'autore creato misurando la lunghezza della lista di autori
		 */
		List<Author> AllAuthors = authorDao.findAll();
		assertEquals(AllAuthors.size(), 1);
	}
	
	@Test
	void testCreateAndDelete() {
		
	}
	
	@Test
	void testCreateAndUpdate() {
		/*
		 * Test per la creazione e l'aggiornamento dell'autore
		 */
		Session s = sf.openSession();
		authorDao.setSession(s);
		
		Author newAuthor1 = authorDao.create("Brandon", "Sanderson");
		try {
			authorDao.findById(newAuthor1.getId());
		} catch (Exception e) {
			fail("Exception not expected: " + e.getMessage());
		}
		
		Author newAuthor2 = new Author();
		newAuthor2.setId(newAuthor1.getId());
		newAuthor2.setName("Ursula Kroeber");
		newAuthor2.setSurname("Le Guin");
		
		assertNotEquals(newAuthor1.getFullName(), newAuthor2.getFullName()); // Ci si aspetta che il nome completo dei due autori sia diverso
		
		Author newAuthor3 = authorDao.update(newAuthor2); // Aggiorna il nome e il cognome dell'autore 1 con quelli dell'autore 2
		s.getTransaction().commit();
		
		s.beginTransaction();
		
		assertEquals(newAuthor1, newAuthor3); // Hanno lo stesso id e sono collegati
		assertNotEquals(newAuthor3, newAuthor2);  
		
		try {
			/*
			 * Ci si aspetta di fallire la ricerca con il vecchio nome dell'autore
			 */
			Author notFound = authorDao.findByNameAndSurname("Brandon", "Sanderson");
			assertEquals(notFound, null); 
		} catch (Exception e) {
			assertTrue(true);
		}
		try {
			/*
			 * Ci si aspetta di completare la ricerca con il nuovo nome dell'autore
			 */
			authorDao.findByNameAndSurname("Ursula Kroeber", "Le Guin");
		} catch (Exception e) {
			fail("Exception not expected: " + e.getMessage());
		}
		s.getTransaction().commit();
	}
	
	@Test
	void testCreateAuthorDuplicate() {
		/*
		 * 
		 * Test that it's not possible to create two authors with same name and surname
		 */
		Session s = sf.openSession();
		authorDao.setSession(s);
		//add new author
		Author newAuthor1 = authorDao.create("testName", "testSurname");
		assertThrows(RuntimeException.class, ()->{authorDao.create(newAuthor1.getName(), newAuthor1.getSurname());});
	}
	
}
	
	
