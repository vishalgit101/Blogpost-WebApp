package configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import filter.JsonUsernamePasswordAuthenticationFilter;
import filter.RestAuthenticationEntryPoint;
import jakarta.servlet.http.HttpServletResponse;

@Configuration
public class SecurityConfigs {
	
	private final UserDetailsService userDetailsService;
	private RestAuthenticationEntryPoint restAuthenticationEntryPoint;
	public SecurityConfigs(UserDetailsService userDetailsService, RestAuthenticationEntryPoint restAuthenticationEntryPoint) {
		super();
		this.userDetailsService = userDetailsService;
		this.restAuthenticationEntryPoint = restAuthenticationEntryPoint;
	}

	/*@Bean
	public UserDetailsService userDetail() {
		UserDetails vishal = User.builder()
				.username("vishal")
				.password("{noop}pass123")
				.roles("User", "Admin")
				.build();
		
		return new InMemoryUserDetailsManager(vishal);
	}*/
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		
		 /*JsonUsernamePasswordAuthenticationFilter jsonFilter = new JsonUsernamePasswordAuthenticationFilter();
		    jsonFilter.setAuthenticationManager(authManager);
		    jsonFilter.setFilterProcessesUrl("/login"); // match frontend login POST URL*/
		
		return http
			.csrf(customizer -> customizer.disable()) // can get the csrf token and pass a key value from Controller HttpRequest Object
			.authorizeHttpRequests(request -> request
					.requestMatchers("/login", "/login.html", "/register.html", "/register", "home2.html", "/api", "/api/all-posts", "api/read", "/read-blog2", "read-blog2.html", "login.js","/login.js", "/scripts/**", "/styles/**").permitAll()
					.requestMatchers("/admin/**").hasRole("ADMIN") // future- add comment
					.requestMatchers("/manager/**").hasAnyRole("MANAGER", "ADMIN")
					.requestMatchers("/user/**").hasAnyRole("USER","MANAGER","ADMIN")
					.anyRequest().authenticated())
			
			//.httpBasic((Customizer.withDefaults()))
			
			.formLogin(form-> form 
					.loginPage("/login") // Custom User login page url
					.loginProcessingUrl("/login") // Post endpoint for credentials
					.defaultSuccessUrl("/home2.html",true) // Where to go after sucessful login 
					.permitAll()
					)
			
			.exceptionHandling(ex -> ex
					.defaultAuthenticationEntryPointFor(
						restAuthenticationEntryPoint, 
						new AntPathRequestMatcher("/api/**") // Apply only to API calls
					)
				)
			
			//.httpBasic(Customizer.withDefaults())
			.logout(logout -> logout
					.logoutUrl("/logout")
					.logoutSuccessUrl("/home2.html"))
			
			/*.exceptionHandling(configure -> configure
						.accessDeniedPage("/access-denied"))*/
			
			.exceptionHandling(exception -> exception
			          .accessDeniedHandler((request, response, accessDeniedException) -> {
			              response.setStatus(HttpServletResponse.SC_FORBIDDEN);
			              response.setContentType("application/json");
			              response.getWriter().write("{\"error\": \"Access Denied\"}");
			          }))
			
			.build();
		
		
		
		}
	
	@Bean
	public AuthenticationProvider authProvider() { // multiple provider exist but we want dB-AuthProvider
		
		
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider(); 
		//provider.setPasswordEncoder(NoOpPasswordEncoder.getInstance());
		provider.setPasswordEncoder(new BCryptPasswordEncoder(10));
		provider.setUserDetailsService(this.userDetailsService); // either we use the @Bean of UserDetailService we defined here, but if we
		// want it to come from the database then we create a custom class for it and its repo
		return provider;
		
	}
	
	/*@Bean
	public AuthenticationManager authManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager() ;
	}*/
}
