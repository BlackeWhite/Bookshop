package it.bookshop.services;

import java.sql.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.bookshop.model.entity.PaymentCard;
import it.bookshop.model.entity.PersonalData;
import it.bookshop.model.entity.Role;
import it.bookshop.model.entity.User;
import it.bookshop.model.dao.AuthorDao;
import it.bookshop.model.dao.PaymentCardDao;
import it.bookshop.model.dao.RoleDao;
import it.bookshop.model.dao.UserDetailsDao;

@Transactional
@Service("userService")
public class UserDetailsServiceDefault implements UserService, UserDetailsService {
	
	
	private UserDetailsDao userrepository;
	private PaymentCardDao paymentCardRepository;
	private RoleDao roleRepository;

	@Transactional(readOnly = true)
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

	    User user = userrepository.findUserByUsername(username);
	    UserBuilder builder = null;
	    if (user != null) {
	      
	      // qui "mappiamo" uno User della nostra app in uno User di spring
	      builder = org.springframework.security.core.userdetails.User.withUsername(username);
	      builder.disabled(!user.isEnabled());
	      builder.password(user.getPassword());
	      
	      // il builder vuole un vettore di stringhe e non hashtable
	      String [] roles = new String[user.getRoles().size()];
	      
	      int j = 0;
	      for (Role r : user.getRoles()) {
	    	  roles[j++] = r.getName();
	      }
	            
	      builder.roles(roles);
	    } else {
	      throw new UsernameNotFoundException("User not found.");
	    }
	    
	    return builder.build();		
	}
	
	@Override
	public List<User> findAllForRole(String role) {
		return userrepository.findAllForRole(role);
	}

	@Override
	public User findUserByUsername(String username) {
		return this.userrepository.findUserByUsername(username);
	}

	@Override
	public User findUserById(long id) {
		return this.userrepository.findUserById(id);
	}

	@Override
	public User findUserByEmail(String email) {
		return this.userrepository.findUserByEmail(email);
	}

	@Override
	public User create(String username, String email, String password, String name, String surname,
			Date birthdate, String street, String city, long cap, String state, List<String> roles) {
		User user = new User();
		PersonalData personalData = new PersonalData();
		
		user.setUsername(username);
		user.setEmail(email);
		user.setPassword(password);
		user.setEnabled(true);
		
		personalData.setName(name);
		personalData.setSurname(surname);
		personalData.setBirthdate(birthdate);
		personalData.setStreet(street);
		personalData.setCap(cap);
		personalData.setCity(city);
		personalData.setState(state);

		user.setPersonalData(personalData);
		for(String role: roles) {
			Role r = roleRepository.findByName(role);
			user.addRole(r);
		}
		
		return userrepository.create(user);
	}
	
	@Override
	public User create(User user) {
		return userrepository.create(user);
	}

	@Override
	public User update(User user) {
		return this.userrepository.update(user);
	}

	@Override
	public void deleteByUsername(String username) {
		User user = this.userrepository.findUserByUsername(username);
		this.userrepository.delete(user);
		
	}

	@Override
	public List<PaymentCard> findAllPaymentCards() {
		return this.paymentCardRepository.findAll();
	}

	@Override
	public PaymentCard findPaymentCardById(Long id) {
		return this.paymentCardRepository.findById(id);
	}

	@Override
	public PaymentCard createPaymentCard(PaymentCard card, User user) {
		return this.paymentCardRepository.create(card.getType(), card.getNumber(), card.getExpirationDate(), user);
	}
	
	@Override
	public PaymentCard createPaymentCard(String type, String number, Date expirationDate, Long userId) {
		User user = this.userrepository.findUserById(userId);
		return this.paymentCardRepository.create(type, number, expirationDate, user);
	}

	@Override
	public PaymentCard updatePaymentCard(PaymentCard card) {
		return this.paymentCardRepository.update(card);
	}

	@Override
	public void deletePaymentCard(Long id) {
		PaymentCard card = paymentCardRepository.findById(id);
		this.paymentCardRepository.delete(card);
	}

	@Override
	public Role findRoleByName(String name) {
		return roleRepository.findByName(name);
	}

	@Override
	public Role findOrCreateRole(String name) {
		Role role = roleRepository.findByName(name);
		if(role == null) {
			return roleRepository.create(name);
		}
		return role;
	}

	@Autowired
	public void setRoleRepository(RoleDao roleRepository) {
		this.roleRepository = roleRepository;
	}
	
	@Autowired
	public void setUserRepository(UserDetailsDao userrepository) {
		this.userrepository = userrepository;
	}
	
	@Autowired
	public void setPaymentCardRepository(PaymentCardDao paymentCardRepository) {
		this.paymentCardRepository = paymentCardRepository;
	}

	
}
