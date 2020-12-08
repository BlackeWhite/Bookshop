package it.bookshop.services;

import java.sql.Date;

import it.bookshop.model.entity.PersonalData;
import it.bookshop.model.entity.User;

public interface UserService {
	
	User findUserByUsername(String username);
	
	User findUserById(long id);
	
	User findUserByEmail(String email);
	
	User create(String username, String email, String password, String name, String surname,
			Date birthdate, String street, String city, long cap, String state);
	
	User update(User user);
	
	void deleteByUsername(String username);
}
