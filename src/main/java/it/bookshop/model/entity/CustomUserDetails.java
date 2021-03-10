package it.bookshop.model.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

//Classe custom che viene caricata al login da Spring per poter accedere facilmente ad alcuni dati
//(In questo caso al paese) senza leggere il database all'interno de WebConfig
public class CustomUserDetails implements UserDetails{

	
	private static final long serialVersionUID = 1L;
	
	private User user;
	private String[] roles;

	public CustomUserDetails(User user, String[] roles) {
		this.user = user;
		this.roles = roles;
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		List<SimpleGrantedAuthority> authorities = new ArrayList<SimpleGrantedAuthority>();
		for(String r : roles) {
			authorities.add(new SimpleGrantedAuthority(r));
		}
		return authorities;
	}

	@Override
	public String getPassword() {
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		return user.getUsername();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return user.isEnabled();
	}

	public String getState() {
		return user.getPersonalData().getState();
	}
	
	public void setUser(User user) {
		this.user = user;
	}

}
