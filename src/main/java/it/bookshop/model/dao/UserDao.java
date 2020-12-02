package it.bookshop.model.dao;

import java.time.LocalDate;
import org.hibernate.Session;
import it.bookshop.model.entity.User;

public interface UserDao {
	Session getSession();
	public void setSession(Session session);
	
	User findUserByUsername(String username);
	//User findUserById(long id);
	
	//per l'utente devo aggiungere l'anagrafica come embeddable
	User create(String username, String password, boolean isEnabled);
	
	User update(User user);
	
	void delete(User user);
	
	public String encryptPassword(String password);

}
