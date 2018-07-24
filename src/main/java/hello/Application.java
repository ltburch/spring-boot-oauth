package hello;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableOAuth2Sso
@SpringBootApplication
@EnableWebSecurity
public class Application extends WebSecurityConfigurerAdapter {

    public static void main(String[] args) throws Throwable {
        SpringApplication.run(Application.class, args);
    }
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
    	
        http
        .antMatcher("/**")
        .authorizeRequests()
          .antMatchers("/", "/home**", "/login**", "/webjars/**", "/error**")
          .permitAll()
        .anyRequest()
          .authenticated().and()
          .csrf()
          .ignoringAntMatchers("/login", "/logout")
          .and().logout().deleteCookies("JSESSIONID").logoutSuccessUrl("/").permitAll();
        
    }

    
}


