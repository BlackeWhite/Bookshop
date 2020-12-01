package it.bookshop.model.dao;

import java.sql.Date;
import java.util.List;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import it.bookshop.model.entity.Author;

@Transactional
@Repository("authorDao") //@Repository  is a specialization of @Component
public class AuthorDaoDefault implements AuthorDao{


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
	public Author update(Author author) {
		return (Author) getSession().merge(author);
	}

	@Override
	public List<Author> findAll() {
		return getSession().createQuery("select a from AUTHOR", Author.class).getResultList();
	}

	@Override
	public void delete(Author author) {
		getSession().delete(author);		
	}

}
