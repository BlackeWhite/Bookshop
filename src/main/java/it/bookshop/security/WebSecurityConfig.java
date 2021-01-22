package it.bookshop.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import it.bookshop.services.UserDetailsServiceDefault;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	

	@Bean
	public UserDetailsService userDetailsService() {
		return new UserDetailsServiceDefault();
	};

	/**
	 * Configurazione dell'autenticazione
	 */
	@Autowired
	public void configure(AuthenticationManagerBuilder auth, PasswordEncoder passwordEncoder) throws Exception {

		auth.inMemoryAuthentication().withUser("imuser")
			.password(passwordEncoder.encode("imuser"))
			.roles("USER");
		auth.inMemoryAuthentication().withUser("imadmin")
			.password(passwordEncoder.encode("imadmin"))
			.roles("USER", "ADMIN");

		auth.userDetailsService(userDetailsService()).passwordEncoder(passwordEncoder); // TODO refactor
	}

	/**
	 * Configurazione dell'autorizzazione
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.authorizeRequests().
		antMatchers("/login").permitAll().
		antMatchers("/").permitAll().
		antMatchers("/advanced_search").hasAnyRole("USER").
		antMatchers("/**").permitAll().
		and().formLogin().loginPage("/login").defaultSuccessUrl("/").failureUrl("/login?error=true").permitAll().
		and().logout().logoutSuccessUrl("/").invalidateHttpSession(true).permitAll().
		and().csrf().disable();

	}
}
