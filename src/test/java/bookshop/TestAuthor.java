package bookshop;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

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
		
		a = bookDao.create("testIsbn1", "testBook1", null, null, 1, 20, u, 10, "summary1", "image1", 0.20);
		
		s.getTransaction().commit();
		
		ctx.close();
	}
	
	
	@AfterAll
	static void tearDown() {
		/*
		 * Pulizia dell'environment di test
		 */
		System.out.println("Pulizia environment suite di test");
		ctx = new AnnotationConfigApplicationContext(DataServiceConfigTest.class);
		//authorDao = ctx.getBean(AuthorDao.class);
		sf = ctx.getBean("sessionFactory", SessionFactory.class);
		
		BookDao bookDao = ctx.getBean(BookDao.class);
		UserDetailsDao userDao = ctx.getBean(UserDetailsDao.class);
		
		Session s = sf.openSession();
		
		s.beginTransaction();
		
		bookDao.setSession(s);
		userDao.setSession(s);

		bookDao.delete(a);
		
		s.getTransaction().commit();
		
		ctx.close();
	}

	
	@BeforeEach
	void openContext() {
		ctx = new AnnotationConfigApplicationContext(DataServiceConfigTest.class);
		authorDao = ctx.getBean(AuthorDao.class);
		sf = ctx.getBean("sessionFactory", SessionFactory.class);
	}

	
	@AfterEach
	void closeContext() {
		ctx.close();
	}
	
	
	@Test
	void testBeginCommitTransaction() {
		/*
		 * Test that dao stores session
		 */
		Session s = sf.openSession();
		
		assertTrue(s.isOpen());
		
		authorDao.setSession(s);
		s.beginTransaction();
		
		assertEquals(s, authorDao.getSession());
		assertSame(s, authorDao.getSession());
		assertTrue(authorDao.getSession().getTransaction().isActive());
		
		s.getTransaction().commit();
		
		assertFalse(authorDao.getSession().getTransaction().isActive());
		
	}
	
	
	@Test
	void testCreateAndDelete() {
		/*
		 * Test per la creazione e la cancellazione di un autore (nessun libro collegato).
		 */
		Session s = sf.openSession();
		
		assertTrue(s.isOpen());
		
		authorDao.setSession(s);
		s.beginTransaction();
		
		Author newAuthor = authorDao.create("authorTestName1", "authorTestSurname1");
		String name = newAuthor.getName();
		String surname = newAuthor.getSurname();
		
		s.getTransaction().commit();
		
		s.beginTransaction();
		
		try {
			/*
			 * Verifica se l'autore è stato creato
			 */
			authorDao.findById(newAuthor.getId());
		} catch (Exception e) {
			fail("Exception not expected: " + e.getMessage());
		}
		
		try {
			/*
			 * Tenta l'eliminazione dell'autore, ci si aspetta che vada a buon fine
			 */
			authorDao.delete(newAuthor);
		} catch (Exception e) {
			fail("Exception not expected: " + e.getMessage());
		}
		
		try {
			/*
			 * Verifica se l'autore esiste ancora. Ci si aspetta che fallisca il test.
			 */
			Author notFound = authorDao.findByNameAndSurname(name, surname);
			assertEquals(notFound, null);
		} catch (Exception e) {
			assertTrue(true);
		}
		
		s.getTransaction().commit();
		
		
		/*
		 * Tenta la creazione di un autore senza nome e senza cognome. Ci si aspetta che fallisca.
		 */
		try {
			Author newAuthor2 = authorDao.create(null, null);
			assertEquals(newAuthor2, null);
		} catch(Exception e) {
			assertTrue(true);
		}
		
	}
	
	
	@Test
	void testCreateAndDeleteAuthorWithBook() {
		/*
		 * Test per la creazione e la cancellazione di un autore (nessun libro collegato).
		 */
		Session s = sf.openSession();
		authorDao.setSession(s);
		s.beginTransaction();

		Author newAuthor = authorDao.create("authorTestName2", "authorTestSurname2");
		String name = newAuthor.getName();
		String surname = newAuthor.getSurname();
		Long id = newAuthor.getId();
		
		a.addAuthors(newAuthor);
		
		s.getTransaction().commit();
		
		s.beginTransaction();
		
		try {
			/*
			 * Verifica se l'autore è stato creato
			 */
			authorDao.findById(newAuthor.getId());
		} catch (Exception e) {
			fail("Exception not expected: " + e.getMessage());
		}
		try {
			/*
			 * Verifica se l'autore è stato collegato correttamente al libro
			 */
			Set<Author> bookAuthors = a.getAuthors();
			List<Author> bookAuthorsList = new ArrayList<Author>(bookAuthors);
			Iterator<Author> iterAuthors = bookAuthorsList.iterator();
			Author found = null;
			while(iterAuthors.hasNext()) {
				Author curr = iterAuthors.next();
				if(curr.getId() ==  newAuthor.getId()) {
					found = curr;
				}
			}
			assertEquals(found.getId(), newAuthor.getId());
		} catch (Exception e) {
			fail("Exception not expected: " + e.getMessage());
		}
		
		try {
			/*
			 * Tenta l'eliminazione dell'autore, ci si aspetta che vada a buon fine
			 */
			authorDao.delete(newAuthor);
		} catch (Exception e) {
			fail("Exception not expected: " + e.getMessage());
		}
		
		try {
			/*
			 * Verifica se l'autore esiste ancora. Ci si aspetta che fallisca il test.
			 */
			Author notFound = authorDao.findByNameAndSurname(name, surname);
			assertEquals(notFound, null);
		} catch (Exception e) {
			assertTrue(true);
		}
		try {
			/*
			 * Verifica se l'autore esiste ancora, tramite id. Ci si aspetta che fallisca il test.
			 */
			Author notFound = authorDao.findById(id);
			assertEquals(notFound, null);
		} catch (Exception e) {
			assertTrue(true);
		}
		
		s.getTransaction().commit();

	}
	
	
	@Test
	void testCreateAndFind() {
		/*
		 * Test per la creazione e la ricerca dell'autore
		 */
		Session s = sf.openSession();
		authorDao.setSession(s);
		s.beginTransaction();
		
		Author newAuthor = authorDao.create(" authorTestName3", "authorTestSurname3  ");
		
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
		 * Testa se il metodo findAll trova l'autore creato
		 */
		/*
		s.beginTransaction();
		
		List<Author> AllAuthors = authorDao.findAll();
		assertEquals(AllAuthors.size(), 1); // con il database popolato fallisce
		
		s.getTransaction().commit();
		*/
		s.beginTransaction();
		authorDao.delete(newAuthor);
		s.getTransaction().commit();
	}
	
	
	@Test
	void testCreateAndUpdate() {
		/*
		 * Test per la creazione e l'aggiornamento dell'autore
		 */
		Session s = sf.openSession();
		authorDao.setSession(s);
		s.beginTransaction();
		
		Author newAuthor1 = authorDao.create("authorTestName4", "authorTestSurname4");
		try {
			authorDao.findById(newAuthor1.getId());
		} catch (Exception e) {
			fail("Exception not expected: " + e.getMessage());
		}
		s.getTransaction().commit();
		
		
		s.beginTransaction();
		Author newAuthor2 = new Author();
		newAuthor2.setId(newAuthor1.getId());
		newAuthor2.setName("authorTestName5");
		newAuthor2.setSurname("authorTestSuname5");
		
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
			Author notFound = authorDao.findByNameAndSurname("authorTestName4", "authorTestSurname4");
			assertEquals(notFound, null); 
		} catch (Exception e) {
			assertTrue(true);
		}
		try {
			/*
			 * Ci si aspetta di completare la ricerca con il nuovo nome dell'autore
			 */
			authorDao.findByNameAndSurname("authorTestName5", "authorTestSuname5");
		} catch (Exception e) {
			fail("Exception not expected: " + e.getMessage());
		}
		s.getTransaction().commit();
		
		s.beginTransaction();
		authorDao.delete(newAuthor1);
		authorDao.delete(newAuthor3);
		s.getTransaction().commit();
	}
	
	
	@Test
	void testCreateAndSearchbookForAuthor() {
		/*
		 * Test per la creazione e la ricerca dei libri legati ad un autore
		 */
		Session s = sf.openSession();
		authorDao.setSession(s);
		s.beginTransaction();
		
		Author newAuthor = authorDao.create("authorTestName6", "authorTestSurname6");
		try {
			authorDao.findById(newAuthor.getId());
		} catch (Exception e) {
			fail("Exception not expected: " + e.getMessage());
		}
		s.getTransaction().commit();
		
		s.beginTransaction();
		
		
		BookDao bookDao = ctx.getBean(BookDao.class);
		bookDao.setSession(s);
		Book testBook = bookDao.create("testIsbn3", "testBook3", null, null, 1, 20, u, 10, "summary3", "image3", 0.20); // Crea un libro
		try{
			/*
			 * Ci si aspetta che all'autore non ci sia nessun libro associato
			 */
			List<Book> emptyBookList = authorDao.findBookForAuthor(newAuthor);
			assertEquals(emptyBookList.size(), 0);
		} catch(Exception e) {
			assertTrue(true);
		}
		s.getTransaction().commit();
		
		s.beginTransaction();
		testBook.addAuthors(newAuthor); // Associa l'autore al libro
		int size_a = testBook.getAuthors().size();
		int size_b = newAuthor.getBooks().size();
		assertEquals(size_a, size_b);
		s.getTransaction().commit();
		
		s.beginTransaction();
		try{
			/*
			 * Ci si aspetta che all'autore sia associato un libro
			 */
			Author authorWithBook = authorDao.findById(newAuthor.getId());
			try {
				authorDao.findBookForAuthor(authorWithBook);
			} catch (Exception e) {
				fail("Exception not expected: " + e.getMessage());
			}
			List<Book> bookList = authorDao.findBookForAuthor(authorWithBook);
			assertEquals(bookList.size(), 1);
		} catch(Exception e) {
			fail("Exception not expected: " + e.getMessage());
		}
		s.getTransaction().commit();
		
		s.beginTransaction();
		bookDao.delete(testBook);
		authorDao.delete(newAuthor);
		s.getTransaction().commit();
	}
	
	
	@Test
	void testCreateAndFindBookRemovedForAuthor() {
		/*
		 * Testa il metodo per recuperare i libri di un autore che sono stati rimossi
		 */
		Session s = sf.openSession();
		authorDao.setSession(s);
		s.beginTransaction();
		
		Author newAuthor = authorDao.create("authorTestName7", "authorTestSurname7");
		try {
			authorDao.findById(newAuthor.getId());
		} catch (Exception e) {
			fail("Exception not expected: " + e.getMessage());
		}
		s.getTransaction().commit();
		
		s.beginTransaction();
		
		BookDao bookDao = ctx.getBean(BookDao.class);
		bookDao.setSession(s);
		Book testBook1 = bookDao.create("testIsbn2", "testBook2", null, null, 1, 20, u, 10, "summary2", "image2", 0.20); // Crea un libro
		Book testBook2 = bookDao.create("testIsbn3", "testBook3", null, null, 1, 20, u, 10, "summary3", "image3", 0.20); // Crea un libro
		
		s.getTransaction().commit();

		s.beginTransaction();
		
		testBook1.addAuthors(newAuthor); // Associa l'autore al libro
		testBook2.addAuthors(newAuthor); // Associa l'autore al libro
		
		int size_book1 = testBook1.getAuthors().size();
		int size_book2 = testBook2.getAuthors().size();
		int size_author = newAuthor.getBooks().size();
		assertEquals(size_author, size_book1+size_book2); // Si assicura che il numero di libri dell'autore
														  // coincida con la somma del numero di autori dei
														  // singoli libri
		s.getTransaction().commit();
		
		s.beginTransaction();
		try{
			/*
			 * Testa la rimozione del libro
			 */
			bookDao.removeBook(testBook1); // Rimuove il libro
			
		} catch (Exception e) {
			fail("Exception not expected: " + e.getMessage());
		} 
		s.getTransaction().commit();
		
		s.beginTransaction();
		
		try {
			/*
			 * Testa il metodo findBookRemovedForAuthor
			 */
			List<Book> removedBookForAuthor = authorDao.findBookRemovedForAuthor(newAuthor);
			assertEquals(removedBookForAuthor.size(), 1);
			assertEquals(removedBookForAuthor.get(0).getTitle(), testBook1.getTitle());
		} catch (Exception e) {
			fail("Exception not expected: " + e.getMessage());
		} 
		
		
		bookDao.delete(testBook1);
		bookDao.delete(testBook2);
		authorDao.delete(newAuthor);
		s.getTransaction().commit();
		
	}

	
	@Test
	void testCreateAuthorDuplicate() {
		/*
		 * Test that it's not possible to create two authors with same name and surname
		 */
		Session s = sf.openSession();
		authorDao.setSession(s);
		s.beginTransaction();
		//add new author
		Author newAuthor1 = authorDao.create("testName7", "testSurname7");
		
		assertThrows(RuntimeException.class, ()->{authorDao.create(newAuthor1.getName(), newAuthor1.getSurname());});
	}
	
}
	
	
