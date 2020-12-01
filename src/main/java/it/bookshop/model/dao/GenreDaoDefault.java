package it.bookshop.model.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import it.bookshop.model.entity.Genre;


@Repository("genreDao") // annotazione usata per operazioni CRUD
public class GenreDaoDefault extends DefaultDao implements GenreDao {
	@Override
	public List<Genre> findAll() {
		return this.getSession().createQuery("FROM genre g", Genre.class).getResultList();
	}
	
	@Override
	public Genre findByName(String name) {
		return this.getSession().createQuery("FROM genre g WHERE g.name = :name", Genre.class).setParameter("name", name).getSingleResult();
	}

	@Override
	public Genre create(String Name) {
		Genre g = new Genre();
		g.setName(Name);
		this.getSession().save(g);
		return g;
	}

	@Override
	public Genre update(Genre genre) {
		return (Genre)this.getSession().merge(genre);
	}
	
	@Override
	public void delete(Genre genre) {
		this.getSession().delete(genre);

	}



}
