package bookshop;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Date;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import it.bookshop.model.dao.RoleDao;
import it.bookshop.model.dao.UserDetailsDao;
import it.bookshop.model.entity.Role;
import it.bookshop.model.entity.User;
import it.bookshop.test.DataServiceConfigTest;

public class TestUser {
	
	private static AnnotationConfigApplicationContext ctx;
	private static SessionFactory sf;
	private UserDetailsDao userDetailsDao;
	private static User testUser;
	
	@BeforeAll
	static void setup() {
	}
	
	@AfterAll
	static void teardown() {
	}
	
	@BeforeEach
	void openContext() {
		
		ctx = new AnnotationConfigApplicationContext(DataServiceConfigTest.class);
		sf = ctx.getBean("sessionFactory", SessionFactory.class);
		userDetailsDao = ctx.getBean(UserDetailsDao.class);
		
	}
	
	@AfterEach
	void closeContext() {
		
		ctx.close();
		
	}
	
	@Test
	void testBeginCommitTransaction() {
			
			Session s = sf.openSession();
			assertTrue(s.isOpen());
			
			// Verifica sul funzionamento del meccanismo di associazione delle sessioni
			s.beginTransaction(); 
			userDetailsDao.setSession(s);
			assertEquals(s, userDetailsDao.getSession()); // verifica se la sessione coincide con quella creata
			
			s.getTransaction().commit();
			assertFalse(s.getTransaction().isActive());

	}
	
	@Test
	void testUserCreation () {
		
		Session s = sf.openSession();
		s.beginTransaction();
		userDetailsDao.setSession(s);
		
		// Verifica sul funzionamento di creazione dell'utente
		User testUser = userDetailsDao.create(
				"testUsername", 
				"testEmail", 
				"testPassword", 
				"testName", 
				"testSurname", 
				new Date(System.currentTimeMillis()), 
				"testStreet", 
				"testCity", 
				0000, 
				"testState");
		Long userId = testUser.getUserID();
		User sameTestUser = userDetailsDao.findUserById(userId);
		assertNotNull(sameTestUser);
		assertEquals(testUser, sameTestUser);
		
		// Verifica sul funzionamento di creazione a partire da istanza utente esistente
		testUser.setUsername("newUsername");
		User newTestUser = userDetailsDao.update(testUser);
		Long newUserId = newTestUser.getUserID();
		
		User sameNewTestUser = userDetailsDao.create(newTestUser);
		assertNotNull(sameNewTestUser);
		assertEquals(newTestUser, sameNewTestUser);
		userDetailsDao.delete(testUser);
		userDetailsDao.delete(newTestUser);
		s.getTransaction().commit();
		
	}
	
	
	@Test
	void testUserDelete() {
		
		Session s = sf.openSession();
		s.beginTransaction();
		userDetailsDao.setSession(s);
		
		// Verifica sul funzionamento di eliminazione dell'utente
		User testUser = userDetailsDao.create(
				"testUsername", 
				"testEmail", 
				"testPassword", 
				"testName", 
				"testSurname", 
				new Date(System.currentTimeMillis()), 
				"testStreet", 
				"testCity", 
				0000, 
				"testState");
		
		Long userId = testUser.getUserID();
		userDetailsDao.delete(testUser);
		User sameTestUser = userDetailsDao.findUserById(userId);
		assertNull(sameTestUser);
		s.getTransaction().commit();
		
	}
	
	@Test
	void testUserSearchByRoleAndUsername() {
		
		Session s = sf.openSession();
		s.beginTransaction();
		userDetailsDao.setSession(s);
		
		// Verifica sul funzionamento di ricerca per ruolo
		RoleDao roleDao = ctx.getBean(RoleDao.class);
		roleDao.setSession(s);
		Role testRole = roleDao.create("testUserRole");
		Set <Role> testRoles = new HashSet<Role>();
		testRoles.add(testRole);
		
		User testUser = userDetailsDao.create(
				"testUsername", 
				"testEmail", 
				"testPassword", 
				"testName", 
				"testSurname", 
				new Date(System.currentTimeMillis()), 
				"testStreet", 
				"testCity", 
				0000, 
				"testState");
		testUser.setRoles(testRoles);
		User roledTestUser = userDetailsDao.update(testUser);
		
		List <User> foundUsers1 = userDetailsDao.findAllForRole("testUserRole");
		System.out.println(foundUsers1.size());
		assertEquals(foundUsers1.get(0), roledTestUser);
		List <User> foundUsers2 = userDetailsDao.findAllForRoleByUsername("testUserRole", "testUsername");
		assertTrue(foundUsers2.size()==1);
		assertEquals(foundUsers2.get(0), roledTestUser);
		userDetailsDao.delete(testUser);
		roleDao.delete(testRole);
		s.getTransaction().commit();
		
	}
	
}
