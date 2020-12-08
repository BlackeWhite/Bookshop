package it.bookshop.model.dao;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import it.bookshop.model.entity.Role;

@Transactional
@Repository("roleDao")
public class RoleDaoDefault extends DefaultDao implements RoleDao {

	@Override
	public Role create(String name) {
		Role r = new Role();
		r.setName(name);
		this.getSession().save(r);
		return r;
	}

	@Override
	public Role update(Role role) {
		return (Role)this.getSession().merge(role);
	}

	@Override
	public void delete(Role role) {
		this.getSession().delete(role);
	}


}
