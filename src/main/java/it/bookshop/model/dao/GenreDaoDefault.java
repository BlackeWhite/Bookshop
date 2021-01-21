package it.bookshop.model.dao;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import it.bookshop.model.entity.Book;
import it.bookshop.model.entity.Genre;


@Transactional
@Repository("genreDao") // annotazione usata per operazioni CRUD
public class GenreDaoDefault extends DefaultDao implements GenreDao {
	@Override
	public List<Genre> findAll() {
		return this.getSession().createQuery("FROM Genre g", Genre.class).getResultList();
	}
	
	@Override
	public Genre findByName(String name) {
		return this.getSession().createQuery("FROM Genre g WHERE g.name = :name", Genre.class).setParameter("name", name).getSingleResult();

	}
	@Override
	public List<Book> getBooksForGenre(Genre genre) { 
		// restituisce una lista di tutti i libri di un particolare genere 
		return this.getSession().createQuery("select b from Book b join b.genres g WHERE g.id = :genre",Book.class).setParameter("genre", genre.getId()).getResultList();
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
