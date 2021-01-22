package it.bookshop.app;

import javax.servlet.Filter;

//import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import it.bookshop.security.WebSecurityConfig;

public class WebInit extends AbstractAnnotationConfigDispatcherServletInitializer {

	@Override
	protected Class<?>[] getRootConfigClasses() {
		// NB 1st level config class
		return new Class<?>[] { 
			DataServiceConfig.class, 
			WebSecurityConfig.class 
		};
	}

	@Override
	protected Class<?>[] getServletConfigClasses() {
		// NB 2nd level config class
		return new Class<?>[] { 
			WebConfig.class,  
		};
	}

	@Override
	protected String[] getServletMappings() {
		// as the <servlet-mapping>...</servlet-mapping> element in web.xml
		return new String[] { "/bookshop" };
	}

	@Override
	protected Filter[] getServletFilters() {
		// as the <filter>...</filter> element in web.xml
		CharacterEncodingFilter cef = new CharacterEncodingFilter();
		cef.setEncoding("UTF-8");
		cef.setForceEncoding(true);
		return new Filter[] { new HiddenHttpMethodFilter(), cef };
	}

}