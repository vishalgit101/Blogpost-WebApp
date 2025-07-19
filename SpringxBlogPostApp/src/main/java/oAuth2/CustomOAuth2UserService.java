package oAuth2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import entity.User;
import repositories.UserRepository;
import services.MyUserDetailService;
@Component
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private MyUserDetailService myUserService; // Your UserDetailsService

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // Fetch basic user info from OAuth2 provider
        OAuth2User oAuth2User = super.loadUser(userRequest);

        // Extract email (you might need to adjust the attribute name based on provider)
        String email = oAuth2User.getAttribute("email");
        if (email == null) {
            throw new OAuth2AuthenticationException("Email not found in OAuth2 response");
        }

        // Use your existing UserDetailsService to load user
        UserDetails userDetails = myUserService.loadUserByUsername(email);

        // Create Authentication token manually and set in context
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(auth);

        return (OAuth2User) userDetails; // Your UserPrincipal should implement OAuth2User if needed
    }
}


