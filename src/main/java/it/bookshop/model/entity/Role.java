package it.bookshop.model.entity;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import it.bookshop.model.entity.User;
import java.io.Serializable;

@Entity
public class Role implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private String name;
	private Long id;
	private Set<User> users = new HashSet<User>();


	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	@Column(name = "NAME")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@ManyToMany(mappedBy = "roles")
	public Set<User> getUsers() {
		return this.users;
	}
	
	public void setUsers(Set<User> user) {
		this.users = user;
	}
	public void addUsers(User u) {
		this.users.add(u);
		u.getRoles().add(this); 
	}
	
	public void removeUsers(User u) {
		this.users.remove(u);
		u.getRoles().remove(this);
	}

	
}

