package bookshop;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import it.bookshop.app.DataServiceConfig;
import it.bookshop.model.dao.UserDetailsDao;

public class TestUser {
	void testBeginCommitTransaction() {
		try (AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(DataServiceConfig.class)) {
			SessionFactory sf = ctx.getBean("sessionFactory", SessionFactory.class);
			UserDetailsDao userdao = ctx.getBean(UserDetailsDao.class);
			Session s = sf.openSession();

			assertTrue(s.isOpen());

			s.beginTransaction();

			userdao.setSession(s);
			assertEquals(s, userdao.getSession()); // verifica se è la stessa sessione
			s.getTransaction().commit();

			assertFalse(s.getTransaction().isActive());
		}

	}

}
