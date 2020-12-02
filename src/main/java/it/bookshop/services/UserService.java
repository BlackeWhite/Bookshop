package it.bookshop.services;

import it.bookshop.model.entity.PersonalData;
import it.bookshop.model.entity.User;

public interface UserService {
	
	User findUserByUsername(String username);
	
	User findUserById(long id);
	
	User findUserByEmail(String email);
	
	User create(String username, String email, String password, boolean isEnabled, PersonalData personalData);
	
	User update(User user);
	
	void deleteByUsername(String username);
}
