package it.bookshop.model.dao;

import java.sql.Date;
import java.util.List;

import javax.persistence.NoResultException;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import it.bookshop.model.entity.Author;
import it.bookshop.model.entity.Book;

@Transactional
@Repository("authorDao") //@Repository  is a specialization of @Component
public class AuthorDaoDefault extends DefaultDao implements AuthorDao{


	@Override
	public Author findById(Long id) {
		return getSession().find(Author.class, id);
	}

	@Override
	public Author create(String name, String surname, Date date, String nationality, String biography, String photo) {
		Author a = new Author();
		a.setName(name);
		a.setSurname(surname);
		a.setBirthdate(date);
		a.setNationality(nationality);
		a.setBiography(biography);
		a.setPhoto(photo);
		
		getSession().save(a);
		return a;
	}
	@Override
	public Author create(String name, String surname) {
		Author a = new Author();
		a.setName(name);
		a.setSurname(surname);	
		getSession().save(a);
		return a;
	}

	@Override
	public Author update(Author author) {
		return (Author) getSession().merge(author);
	}

	@Override
	public List<Author> findAll() {
		return getSession().createQuery("FROM Author a", Author.class).getResultList();
	}

	@Override
	public void delete(Author author) {
		getSession().delete(author);		
	}

	@Override
	public Author findByNameAndSurname(String Name, String Surname) {
		try {
			return this.getSession().createQuery("FROM Author a WHERE a.name = :name and a.surname = :surname", Author.class).setParameter("name", Name).setParameter("surname", Surname).getSingleResult();
		}
		catch(NoResultException e) {
			return null;
		}
	}


	
}
