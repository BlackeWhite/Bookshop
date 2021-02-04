package it.bookshop.model.dao;


import java.sql.Date;
import java.util.List;

import javax.persistence.NoResultException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import it.bookshop.model.entity.PersonalData;
import it.bookshop.model.entity.User;

@Transactional
@Repository("userDao")
public class UserDetailsDaoDefault extends DefaultDao implements UserDetailsDao {
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Override
	public List<User> findAllForRole(String role) {
		return this.getSession().createQuery("SELECT u FROM User u JOIN u.roles rs WHERE rs.name=:role", User.class )
				.setParameter("role", role).getResultList();
	}
	
	@Override
	public List<User> findAllForRoleAndUsername(String role, String username) {
		return this.getSession().createQuery("SELECT u FROM User u JOIN u.roles rs WHERE rs.name=:role" + 
				" AND LOWER(u.username) LIKE LOWER(CONCAT('%', :term, '%')) ", User.class )
				.setParameter("role", role)
				.setParameter("term", username).getResultList();
	}
	
	@Override
	public User findUserByUsername(String username) {
		try {
			return this.getSession().createQuery("FROM User u WHERE u.username = :username", User.class).setParameter("username", username).getSingleResult();
		}
		catch(NoResultException e) {
			return null;
		}
	}
	
	@Override
	public User findUserById(long id) {
		return this.getSession().get(User.class, id);
	}
	
	@Override
	public User findUserByEmail(String email) {
		try {
			return this.getSession().createQuery("FROM User u WHERE u.email = :email", User.class).setParameter("email", email).getSingleResult();
		}
		catch(NoResultException e) {
			return null;
		}
	}
	
	@Override
	public User create(String username, String email, String password, String name, String surname,
			Date birthdate, String street, String city, long cap, String state) {
		User user = new User();
		PersonalData personalData = new PersonalData();
		
		user.setUsername(username);
		user.setEmail(email);
		user.setPassword(passwordEncoder.encode(password));
		user.setEnabled(true);
		
		personalData.setName(name);
		personalData.setSurname(surname);
		personalData.setBirthdate(birthdate);
		personalData.setStreet(street);
		personalData.setCap(cap);
		personalData.setCity(city);
		personalData.setState(state);
		
		this.getSession().save(user);
		return user;
	}
	
	@Override
	public User create(User user) {
		user.setPassword(encryptPassword(user.getPassword()));
		user.setEnabled(true);
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
