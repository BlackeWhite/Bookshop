package it.bookshop.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.support.RequestContextUtils;

import it.bookshop.model.entity.CustomUserDetails;
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
		antMatchers("/advanced_search").permitAll().
		antMatchers("/cart").hasRole("USER").
		antMatchers("/account").hasRole("USER").
		antMatchers("/checkout").hasRole("USER").
		antMatchers("/purchase_history").hasRole("USER").
		antMatchers("/admin/**").hasRole("ADMIN").
		antMatchers("/seller/**").hasRole("SELLER").
		antMatchers("/**").permitAll().
		
		
		and().formLogin().loginPage("/login").defaultSuccessUrl("/").failureUrl("/login?error=true").permitAll().
		and().logout().deleteCookies("JSESSIONID").logoutSuccessUrl("/").invalidateHttpSession(true).permitAll().
		and().rememberMe().key("BookShopSecretKey"). //Per la funzione ricordami
		and().csrf().disable();

	}
}
