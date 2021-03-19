package bookshop;

import java.sql.Date;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.transaction.annotation.Transactional;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import it.bookshop.test.DataServiceConfigTest;
import it.bookshop.model.dao.BookDao;
import it.bookshop.model.dao.ShoppingCartDao;
import it.bookshop.model.dao.UserDetailsDao;
import it.bookshop.model.entity.Book;
import it.bookshop.model.entity.ShoppingCart;
import it.bookshop.model.entity.ShoppingCartId;
import it.bookshop.model.entity.User;


public class TestShoppingCart {
	
	private static AnnotationConfigApplicationContext ctx;
	private static SessionFactory sf;
	private ShoppingCartDao shoppingCartDao;
	private static User testUser;
	private static Book testBook1;
	private static Book testBook2;
	
	/* 
	 * In genere ad un test è associata una precondizione che rappresenta il contesto in cui il test 
	 * viene eseguito e deve essere creato prima del test; nel caso in cui parte o l'intero contesto 
	 * sia comune a tutti i test è possbile inserire le istruzioni che lo creano nel meotodo annoatato
	 * con @BeforeEach
	 */
	
	@BeforeAll
	static void setup() {
		
		ctx = new AnnotationConfigApplicationContext(DataServiceConfigTest.class);
		sf = ctx.getBean("sessionFactory", SessionFactory.class);
		
		Session s = sf.openSession();
		s.beginTransaction();
		
		// Creazione di un generico utente
		UserDetailsDao userDao = ctx.getBean(UserDetailsDao.class);
		userDao.setSession(s);
		testUser = userDao.create("testUser1", "email", "password", "testName", "testSurname", new Date(System.currentTimeMillis()), "testAddress", "testCity", 0000, "testState");
		
		// Creazione di libri di test
		BookDao bookDao = ctx.getBean(BookDao.class);
		bookDao.setSession(s);
		testBook1 = bookDao.create("testISBN1", "testTitle1", null, null, 10, 50, null, 100, "summary1", "cover1", 0);
		testBook1 = bookDao.create("testISBN2", "testTitle2", null, null, 10, 100, null, 1000, "summary2", "cover2", 0);
	
		s.getTransaction().commit();
		ctx.close();
		
		}
	
	@AfterAll
	static void teardown( ) {
		
		ctx = new AnnotationConfigApplicationContext(DataServiceConfigTest.class);
		sf = ctx.getBean("sessionFactory", SessionFactory.class);
		Session s = sf.openSession();
		s.beginTransaction();
		
		// Eliminazione del generico utente creato 
		UserDetailsDao userDao = ctx.getBean(UserDetailsDao.class);
		userDao.setSession(s);
		userDao.delete(testUser);
		
		// Eliminazione dei libri di test creati 
		BookDao bookDao = ctx.getBean(BookDao.class);
		bookDao.setSession(s);
		bookDao.delete(testBook1);
		bookDao.delete(testBook2);
		
		s.getTransaction().commit();
		ctx.close();
		
		}
	
	@BeforeEach
	void openContext( ) {
		
		ctx = new AnnotationConfigApplicationContext(DataServiceConfigTest.class);
		sf = ctx.getBean("sessionFactory", SessionFactory.class);
		shoppingCartDao = ctx.getBean(ShoppingCartDao.class);
		
		}
	
	@AfterEach
	void closeContext( ) {
		
		ctx.close();
		
	}
	
	@Test
	void testBeginCommitTransaction() {

		Session s = sf.openSession();
		assertTrue(s.isOpen());
		
		// Verifica sul funzionamento del meccanismo di associazione delle sessioni
		s.beginTransaction();
		shoppingCartDao.setSession(s);
		assertEquals(s, shoppingCartDao.getSession()); 
		
		s.getTransaction().commit();
		assertFalse(s.getTransaction().isActive());

	}
	
	@Test
	void testShoppingCartListSearch () {
		
		Session s = sf.openSession();
		s.beginTransaction();
		shoppingCartDao.setSession(s);	
		
		// Verifica sul funzionamento di ricerca totale 
		List<ShoppingCart> testList =  shoppingCartDao.findUserShoppingCart(testUser);
		s.getTransaction().commit();
		assertEquals(testList.size(), 0);
		
		// Verifica parziale sul funzionamento di ricerca per ID
		ShoppingCartId notExistentID = new ShoppingCartId((long)1,(long)1); 
		ShoppingCart notExistentShoppingCart = shoppingCartDao.findById(notExistentID);
		assertNull(notExistentShoppingCart);
		
	}
	
	@Test
	void testShoppingCartCreationAndSearch () {
		
		Session s = sf.openSession();
		s.beginTransaction();
		shoppingCartDao.setSession(s);
		
		int testCopies = 1;
		ShoppingCart testShoppingCart = shoppingCartDao.create(testUser, testBook1, testCopies);
		
		// Verifica della corretta creazione dell'ID
		ShoppingCartId testSCId = testShoppingCart.getId();
		assertEquals(testSCId.getUserId(), testUser.getUserID());
		assertEquals(testSCId.getBookID(), testBook1.getId());
		
		// Verifica sulla creazione
		List<ShoppingCart> testList =  shoppingCartDao.findUserShoppingCart(testUser);
		assertEquals(testList.size(), 1);
		
		// Verifica sul funzionamento di ricerca per ID
		assertEquals(shoppingCartDao.findById(testSCId), testShoppingCart);
		
	}

	@Test
	void testShoppingCartDeletion () {
		Session s = sf.openSession();
		s.beginTransaction();
		shoppingCartDao.setSession(s);
		
		int testCopies = 1;
		ShoppingCart testShoppingCart = shoppingCartDao.create(testUser, testBook1, testCopies);
		shoppingCartDao.removeBook(testShoppingCart);
		List<ShoppingCart> testList =  shoppingCartDao.findUserShoppingCart(testUser);
		
		// Verifica sul funzionamento di eliminazione di un libro nel carrello 
		assertEquals(testList.size(), 0);
		ShoppingCartId testSCId = testShoppingCart.getId();
		assertNull(shoppingCartDao.findById(testSCId));
		
	}
	
	@Test
	void testUserShoppingCartEmptyFunctionality () {
		Session s = sf.openSession();
		s.beginTransaction();
		shoppingCartDao.setSession(s);
		
		int testCopies = 1;
		ShoppingCart testShoppingCart1 = shoppingCartDao.create(testUser, testBook1, testCopies);
		ShoppingCart testShoppingCart2 = shoppingCartDao.create(testUser, testBook2, testCopies);
		
		List<ShoppingCart> testList =  shoppingCartDao.findUserShoppingCart(testUser);
		assertEquals(testList.size(), 2);
		
		// Verifica sul funzionamento di svuotamento del carrello
		shoppingCartDao.emptyUserCart(testUser);
		assertEquals(testList.size(), 0);
		
	}
	
	
	@Test
	void testShoppingCartUpdate () {
		
		Session s = sf.openSession();
		s.beginTransaction();
		shoppingCartDao.setSession(s);
		
		int testCopies = 1;
		int newCopies = 3;
		ShoppingCart testShoppingCart = shoppingCartDao.create(testUser, testBook1, testCopies);
		ShoppingCartId testSCId = testShoppingCart.getId();
		
		// Verifica sul funzionamento di aggioranamento del carrello
		testShoppingCart.setCopies(newCopies);
		testShoppingCart.setBook(testBook2);
		shoppingCartDao.update(testShoppingCart);
		assertEquals(shoppingCartDao.findById(testSCId), testShoppingCart);
	}
	
}
