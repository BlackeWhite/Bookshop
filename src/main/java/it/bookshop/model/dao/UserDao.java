package it.bookshop.model.dao;

import org.hibernate.Session;
import it.bookshop.model.entity.User;
import it.bookshop.model.entity.PersonalData;

public interface UserDao {
	Session getSession();
	public void setSession(Session session);
	
	User findUserByUsername(String username);
	
	User findUserById(long id);
	
	User findUserByEmail(String email);
	
	User create(String username, String email, String password, boolean isEnabled, PersonalData personalData);
	
	User update(User user);
	
	void delete(User user);
	
	public String encryptPassword(String password);

}
