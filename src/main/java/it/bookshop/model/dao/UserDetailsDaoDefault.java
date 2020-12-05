package it.bookshop.model.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import it.bookshop.model.entity.PersonalData;
import it.bookshop.model.entity.User;
import it.bookshop.model.entity.PersonalData;

@Repository
public class UserDetailsDaoDefault extends DefaultDao implements UserDetailsDao {
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Override
	public User findUserByUsername(String username) {
		return this.getSession().get(User.class, username);
	}
	
	@Override
	public User findUserById(long id) {
		return this.getSession().get(User.class, id);
	}
	
	@Override
	public User findUserByEmail(String email) {
		return this.getSession().get(User.class, email);
	}
	
	@Override
	public User create(String username, String email, String password, boolean isEnabled, PersonalData personalData) {
		User user = new User();
		user.setUsername(username);
		user.setEmail(email);
		user.setPassword(password);
		user.setPersonalData(personalData);
		user.setEnabled(isEnabled);
		this.getSession().save(user);
		
		return user;
	}
	
	@Override
	public User update(User user) {
		return (User)this.getSession().merge(user);
	}
	
	@Override
	public void delete(User user) {
		this.getSession().delete(user);
	}
	
	@Override
	public String encryptPassword(String password) {
		return this.passwordEncoder.encode(password);
	}

}
