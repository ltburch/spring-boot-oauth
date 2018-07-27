package hello;


import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@EnableOAuth2Sso
@SpringBootApplication
@EnableWebSecurity
public class Application extends WebSecurityConfigurerAdapter {

	@Autowired
	Environment env;
	
	public static void main(String[] args) throws Throwable {
        SpringApplication.run(Application.class, args);
    }
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
    	
    	if (Arrays.stream(env.getActiveProfiles()).anyMatch(x -> x.equals("fakeLogin"))) {
        	http.addFilterAfter(
      	          new DevelopmentAuthenticationFilter(), BasicAuthenticationFilter.class);    	
    	}
    	
        http
        .antMatcher("/**")
        .authorizeRequests()
          .antMatchers("/", "/home**", "/login**", "/webjars/**", "/error**")
          .permitAll()
        .anyRequest()
          .authenticated().and()
          .csrf()
          .ignoringAntMatchers("/login", "/logout")
          .and().logout().logoutSuccessUrl("/").permitAll();
        // configure default behavior
        
    }

    
}


