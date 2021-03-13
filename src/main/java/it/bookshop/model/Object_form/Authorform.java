package it.bookshop.model.Object_form;

import java.sql.Date;

import org.springframework.web.multipart.MultipartFile;

import it.bookshop.model.entity.Author;

public class Authorform {

	/*
	 * Classe utilizzata nella form per la modifica dell'autore. Utile per gestire le immagini.
	 */
	private Long id;
	private String name;
	private String surname;
	private Date birthdate;
	private MultipartFile photoFile;
	private String photoName;
	private String nationality;
	private String biography;
	
	/*----------------------Get and Setter----------------------*/
	public long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurame() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}
	
	public Date getBirthday() {
		return birthdate;
	}

	public void setBirthday(Date birthdate) {
		this.birthdate = birthdate;
	}
	
	public String getNationality() {
		return nationality;
	}
	
	public void setNationality(String nationality) {
		this.nationality = nationality;
	}

	public String getBiography() {
		return biography;
	}
	
	public void setBiography(String biography) {
		this.biography = biography;
	}

	public MultipartFile getPhotoFile() {
		return photoFile;
	}

	public void setPhotoFile(MultipartFile photoFile) {
		this.photoFile = photoFile;
	}

	public String getPhotoName() {
		return photoName;
	}

	public void setPhotoName(String photoName) {
		this.photoName = photoName;
	}
	/*----------------------End Get and Setter----------------------*/

	
	/*----------------------Populate Authorform's object----------------------*/
	public void populate(Author author) {
		/*
		 * Popola un oggetto Authorform con i dati dell'oggetto Author passato.
		 */
		this.id = author.getId();
		this.name = author.getName();
		this.surname = author.getSurname();
		this.birthdate = author.getBirthdate();
		this.biography = author.getBiography();
		this.nationality = author.getNationality();
		this.photoName = author.getPhoto();
	}
	
	/*----------------------Populate Author's object----------------------*/
	public Author authorformToAuthor(Authorform authorForm,
			Long author_id, Author authorNotUpdated) {
		/*
		 * 
		 */
		Author author = new Author();
		author.setId(author_id);
		author.setName(authorForm.getName());
		author.setSurname(authorForm.getSurame());
		author.setBirthdate(authorForm.getBirthday());
		author.setBiography(authorForm.getBiography());
		String authorPhoto = authorForm.getPhotoFile().getOriginalFilename();
		if(authorPhoto.isEmpty()) {
			author.setPhoto(authorNotUpdated.getPhoto());
			if (author.getPhoto().isEmpty()) {
				author.setPhoto("profile-placeholder.png");
			}
		} else {
			author.setPhoto(authorPhoto);
		}
		return author;
	}

}
