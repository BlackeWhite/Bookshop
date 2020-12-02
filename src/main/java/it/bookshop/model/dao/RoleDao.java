package it.bookshop.model.dao;

import org.hibernate.Session;
import it.bookshop.model.entity.Role;

public interface RoleDao {
	Session getSession();
	public void setSession(Session session);
	
	Role create(String name);
	
	Role update(Role role);
	
	void delete(Role role);

}
