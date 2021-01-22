package it.bookshop.model.dao;

import javax.persistence.NoResultException;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import it.bookshop.model.entity.Genre;
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
		return (Role) this.getSession().merge(role);
	}

	@Override
	public void delete(Role role) {
		this.getSession().delete(role);
	}

	@Override
	public Role findByName(String name) {
		try {
			return this.getSession().createQuery("FROM Role r WHERE r.name = :name", Role.class)
					.setParameter("name", name).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}

	}

}
