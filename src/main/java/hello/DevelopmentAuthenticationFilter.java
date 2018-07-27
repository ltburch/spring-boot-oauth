package hello;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import java.security.Principal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.intercept.RunAsUserToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

public class DevelopmentAuthenticationFilter extends GenericFilterBean implements Serializable {

    Logger logger = LoggerFactory.getLogger(DevelopmentAuthenticationFilter.class);
	
	private class PrivatePrincipal implements Principal, Serializable {
		String name;
		
		PrivatePrincipal(String name) {
			this.name = name;
		}

		@Override
		public String getName() {
			// TODO Auto-generated method stub
			return name;
		}
	}
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		String impersonationUser = request.getParameter("impersonate");
		if (impersonationUser != null) {
			logger.info("Development Impersonation of " + impersonationUser);
			Principal principal = new PrivatePrincipal(impersonationUser);
			Set<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();
			
			Authentication raut = new RunAsUserToken(this.getClass().getName(), principal, "DEVELOPMENT CREDENTIALS", authorities, UsernamePasswordAuthenticationToken.class);		
			SecurityContextHolder.getContext().setAuthentication(raut);
		}
		chain.doFilter(request,  response);
		
	}

}
