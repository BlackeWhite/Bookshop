package it.bookshop.model.dao;

import java.sql.Date;
import java.util.List;

import javax.persistence.NoResultException;

import org.springframework.stereotype.Repository;

import it.bookshop.model.entity.Author;
import it.bookshop.model.entity.Book;

@Repository("authorDao") // @Repository is a specialization of @Component
public class AuthorDaoDefault extends DefaultDao implements AuthorDao {

	@Override
	public Author findById(Long id) {
		/*
		 * Metodo per la ricerca tramite di un autore.
		 */
		return getSession().find(Author.class, id);
	}

	@Override
	public List<Book> findBookForAuthor(Author author) {
		/*
		 * Metodo per la ricerca di tutti i libri associati ad un determinato autore
		 */
		return this.getSession()
				.createQuery("select b from Book b join b.authors a WHERE a.id = :author AND b.removed = 0", Book.class)
				.setParameter("author", author.getId()).getResultList();
	}

	@Override
	public Author create(String name, String surname, Date date, String nationality, String biography, String photo) {
		Author a = new Author();
		try {
			/*
			 * Se non c'è il nome genera un'eccezione
			 */
			if (name.isEmpty() || name == null || name == "" || name == " ") {
				throw new RuntimeException("INVALID NAME: Empty name");
			}
		} catch (NoResultException e) {
		}
		;
		a.setName(name.trim());
		if (surname.isEmpty() || surname == null || surname == "" || surname == " ") {
			/*
			 * Se non c'è il cognome, inserisce un placeholder
			 */
			a.setSurname("#SURNAME_PLACEHOLDER");
		} else {
			a.setSurname(surname.trim());
		}
		a.setBirthdate(date);
		a.setNationality(nationality);
		a.setBiography(biography);
		a.setPhoto(photo);
		if (a.getPhoto().isEmpty())
			a.setPhoto("profile-placeholder.png");
		try {
			/*
			 * Restituisce un'eccezione se l'autore è stato già creato
			 */
			if (this.getSession()
					.createQuery("FROM Author a WHERE a.name = :name and a.surname = :surname", Author.class)
					.setParameter("name", name).setParameter("surname", surname).getSingleResult() != null) {
				throw new RuntimeException("L'autore è stato già aggiunto.");
			}
		} catch (NoResultException e) {
		}
		getSession().save(a);
		return a;
	}

	@Override
	public Author create(String name, String surname) {
		/*
		 * Metodo per la creazione di un autore usando solo il Nome e il Cognome
		 */
		Author a = new Author();
		try {
			/*
			 * Se non c'è il nome genera un'eccezione
			 */
			if (name.isEmpty() || name == null || name == "" || name == " ") {
				throw new RuntimeException("INVALID NAME: Empty name");
			}
		} catch (NoResultException e) {
		}
		;
		a.setName(name.trim());
		if (surname.isEmpty() || surname == null || surname == "" || surname == " ") {
			/*
			 * Se non c'è il cognome, inserisce un placeholder
			 */
			a.setSurname("#SURNAME_PLACEHOLDER");
		} else {
			a.setSurname(surname.trim());
		}
		try {
			/*
			 * Restituisce un'eccezione se l'autore è stato già creato
			 */
			if (this.getSession()
					.createQuery("FROM Author a WHERE a.name = :name and a.surname = :surname", Author.class)
					.setParameter("name", name).setParameter("surname", surname).getSingleResult() != null) {
				throw new RuntimeException("L'autore è stato già aggiunto.");
			}
		} catch (NoResultException e) {
		}
		;
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
		/*
		 * Metodo per la ricerca di tutti gli autori presenti nel DB.
		 */
		return getSession().createQuery("FROM Author a", Author.class).getResultList();
	}

	@Override
	public void delete(Author author) {
		/*
		 * Metodo per la cancellazione di un autore.
		 */
		getSession().delete(author);
	}

	@Override
	public Author findByNameAndSurname(String Name, String Surname) {
		/*
		 * Metodo per la ricerca di un autore a partire dal Nome e dal Cognome
		 */
		try {
			return this.getSession()
					.createQuery("FROM Author a WHERE a.name = :name and a.surname = :surname", Author.class)
					.setParameter("name", Name).setParameter("surname", Surname).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	public List<Book> findBookRemovedForAuthor(Author author) {
		/*
		 * Metodo per la ricerca dei libri fuori catalogo di un determinato autore.
		 */
		return this.getSession()
				.createQuery("select b from Book b join b.authors a WHERE a.id = :author AND b.removed = 1", Book.class)
				.setParameter("author", author.getId()).getResultList();
	}

}
