package it.bookshop.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.bookshop.model.entity.PersonalData;
import it.bookshop.model.entity.Role;
import it.bookshop.model.entity.User;
import it.bookshop.model.dao.AuthorDao;
import it.bookshop.model.dao.UserDetailsDao;

@Transactional
@Service("userService")
public class UserDetailsServiceDefault implements UserService, UserDetailsService {
	
	
	private UserDetailsDao userrepository;

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
	public User create(String username, String email, String password, boolean isEnabled, PersonalData personalData) {
		User newUser = this.userrepository.create(username, email, password, isEnabled, personalData);
		return newUser;
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
	@Autowired
	public void setUserRepository(UserDetailsDao userrepository) {
		this.userrepository = userrepository;
	}
	
	
}
