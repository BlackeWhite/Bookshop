package it.bookshop.services;

import java.sql.Date;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.bookshop.model.dao.AuthorDao;
import it.bookshop.model.entity.Author;

@Transactional
@Service("authorService")
public class AuthorServiceDefault implements AuthorService {

	private AuthorDao authorRepository;
	
	@Override
	public Author findById(Long id) {
		return authorRepository.findById(id);
	}
	
	@Override
	public Author findByNameAndSurname(String name, String surname) {
		return authorRepository.findByNameAndSurname(name,surname);
	}


	@Override
	public Author create(String name, String surname, Date date, String nationality, String biography, String image) {
		return authorRepository.create(name, surname, date, nationality, biography, image);
	}
    // vedere se serve 
	@Override
	public Author create(String name, String surname) {
		return authorRepository.create(name, surname, null, null, null, null);
	}

	@Override
	public Author update(Author author) {
		return authorRepository.update(author);
	}

	@Override
	public List<Author> findAll() {
		return authorRepository.findAll();
	}

	@Override
	public void delete(Author author) {
		authorRepository.delete(author);
	}

	@Override
	public void delete(Long id) {
		Author a = authorRepository.findById(id);
		authorRepository.delete(a);
	}
	
	@Autowired
	public void setAuthorRepository(AuthorDao authorRepository) {
		this.authorRepository = authorRepository;
	}

	@Override
	public List<Author> findMostPopularAuthors() {
		List<Author> orderedauthors = authorRepository.findAll();
		Comparator<Author> compareByBooks = (Author a1, Author a2) -> a1.getBooks().size()-a2.getBooks().size();
		orderedauthors.sort(compareByBooks);
		List<Author> top10authors;
		if(orderedauthors.size()>10) top10authors = orderedauthors.subList(0,9);
		else top10authors = orderedauthors;
		return top10authors;
	}


}
