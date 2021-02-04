package it.bookshop.services;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import it.bookshop.model.dao.AuthorDao;
import it.bookshop.model.entity.Author;
import it.bookshop.model.entity.Book;

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
	public List<Author> findBestSellingAuthor(){
		List <Author> a = this.authorRepository.findAll();
		Iterator <Author> iterAuthor = a.iterator();
		Map<Long, Integer> listAuthorCopies = new HashMap<Long, Integer>();
		
		while(iterAuthor.hasNext()) {
			Author aut = iterAuthor.next();
			Set <Book> b = aut.getBooks();
			Iterator <Book> iterBook = b.iterator();
			int sum = 0;
			while (iterBook.hasNext()) {
				sum += iterBook.next().getSoldCopies();
			}
			listAuthorCopies.put(new Long(aut.getId()),new Integer(sum));
		}
		List<Author> mostaut = new ArrayList<Author>();
		int count = 0;
		// ordina la map in ordine decrescente 
		Map<Long, Integer> sortedlistAuthorCopies = listAuthorCopies.entrySet().stream().sorted(Collections.reverseOrder(Map.Entry.comparingByValue())).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2,LinkedHashMap::new));
		
		for(Entry<Long, Integer> entry: sortedlistAuthorCopies.entrySet()) {
			if (count > 4) break; // deve estrarre solo i primi 5
			
			Author currAuthor = this.authorRepository.findById(entry.getKey()) ;
			mostaut.add(currAuthor);
			count++;
		}
		return mostaut;
		
	}
	
	@Override
	public List<Author> getAuthorsListFromSet(Set<Author> authorSet){
		List <Author> authorsList = new ArrayList<Author>(authorSet);
		return authorsList;
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
