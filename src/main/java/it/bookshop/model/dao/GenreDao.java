package it.bookshop.model.dao;

import java.util.List;

import org.hibernate.Session;

import it.bookshop.model.entity.Genre;

public interface GenreDao {
	
	Session getSession();
	public void setSession(Session session);
	
	List<Genre> findAll();
	Genre findByName(String name);
	Genre create(String Name);
	Genre update(Genre genre);
	void delete(Genre genre);

}
