package it.bookshop.model.dao;

import java.sql.Date;
import java.util.List;

import org.hibernate.Session;
import it.bookshop.model.entity.User;
import it.bookshop.model.entity.PersonalData;

public interface UserDetailsDao {
	Session getSession();
	public void setSession(Session session);
	
	List<User> findAllForRole(String role);
	
	List<User> findAllForRoleByUsername(String role, String username);
	
	User findUserByUsername(String username);
	
	User findUserById(long id);
	
	User findUserByEmail(String email);
	
	User create(String username, String email, String password, String name, String surname,
			Date birthdate, String street, String city, long cap, String state);
	User create(User user);
	
	User update(User user);
	
	void delete(User user);
	
	public String encryptPassword(String password);
	
}
