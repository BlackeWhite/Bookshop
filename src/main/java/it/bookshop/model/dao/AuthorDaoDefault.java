package it.bookshop.model.dao;

import java.sql.Date;
import java.util.List;

import javax.persistence.NoResultException;

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
	 /*
	@Override
	public List<Author> findMostSelledAuthor(){
		return this.getSession().createQuery("SELECT b.author, SUM(b.soldCopies) FROM book b GROUP BY b.author", Author.class).setMaxResults(5).getResultList();
	}
	
	@Override
	public List<Author> findMostSelledAuthor() { 
		return this.getSession().createQuery("select b from Book b join b.author a WHERE a.id = b.id", Author.class).getResultList();
	}
	*/
	@Override
	public List <Book> findBookForAuthor(Author author){
		// restituisce una lista di tutti i libri di un particolare autore
		return this.getSession().createQuery("select b from Book b join b.authors a WHERE a.id = :author AND b.removed = 0",Book.class).setParameter("author", author.getId()).getResultList();
	}
	
	@Override
	public Author create(String name, String surname, Date date, String nationality, String biography, String photo) {
		Author a = new Author();
		a.setName(name);
		
		if(surname.isEmpty()) {
			a.setSurname("#SURNAME_PLACEHOLDER");
			}
		else {
			a.setSurname(surname);
		}
		a.setBirthdate(date);
		a.setNationality(nationality);
		a.setBiography(biography);
		a.setPhoto(photo);
		if(a.getPhoto().isEmpty()) a.setPhoto("profile-placeholder.png");
		getSession().save(a);
		return a;
	}
	@Override
	public Author create(String name, String surname) {
		Author a = new Author();
		a.setName(name);
		if(surname.isEmpty()) {
			a.setSurname("#SURNAME_PLACEHOLDER");
			}
		else {
			a.setSurname(surname);
		}
		try {
			if (this.getSession().createQuery("FROM Author a WHERE a.name = :name and a.surname = :surname", Author.class).setParameter("name", name).setParameter("surname", surname).getSingleResult() != null) {
			throw new RuntimeException("Already added author"); }
		} catch (NoResultException e){};
		a.setPhoto("profile-placeholder.png");
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

	public List <Book> findBookRemovedForAuthor(Author author){
		// restituisce una lista di tutti i libri fuori catalogo di un particolare autore
		return this.getSession().createQuery("select b from Book b join b.authors a WHERE a.id = :author AND b.removed = 1",Book.class).setParameter("author", author.getId()).getResultList();
	}
	
}
