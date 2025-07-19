package oAuth2;

import java.io.IOException;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import entity.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import repositories.UserRepository;
import services.MyUserDetailService;

@Component
public class OAuthAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
	
	// DI 
		private final UserRepository userRepo;
		private ApplicationContext context;
		
		Logger logger = LoggerFactory.getLogger(OAuthAuthenticationSuccessHandler.class);
		
		@Autowired
		public OAuthAuthenticationSuccessHandler(UserRepository userRepo, ApplicationContext context) {
			super();
			this.userRepo = userRepo;
			this.context = context;
		}


	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		// TODO Auto-generated method stub
		
		 OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
		    Map<String, Object> attributes = oauthToken.getPrincipal().getAttributes();

		    String email = (String) attributes.get("email"); // works for Google and GitHub
		    System.out.println("Email: " + email);
		    
		    for(Map.Entry<String, Object> entry: attributes.entrySet()) {
		    	System.out.println("Key: " + entry.getKey() + ", Value: " + entry.getValue() );
		    }

		    // Check if user exists in DB, create or update user here
		    User user = userRepo.findByUsername(email);
		    
		    
		    
		    if (user == null) {
		    	System.out.println("Users Came null in Oauth load user");
		        user = new User();
		        user.setUsername(email);
		        // set other fields or roles
		        userRepo.save(user);
		    }
		    
		    System.out.println("OAuth email: either from google or GitHub " + email);

			// load user details
			UserDetails userDetails = this.context.getBean(MyUserDetailService.class).loadUserByUsername(email);
			
			UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
			SecurityContextHolder.getContext().setAuthentication(authToken);

		    // Continue with redirect or response
		    //super.onAuthenticationSuccess(request, response, authentication);
			new DefaultRedirectStrategy().sendRedirect(request, response, "/home2.html");
	}

}
