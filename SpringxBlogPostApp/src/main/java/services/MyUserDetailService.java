package services;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import entity.User;
import model.UserPrincipal;
import repositories.UserRepository;

@Service
public class MyUserDetailService implements UserDetailsService{
	
	private UserRepository userRepo;
	
	public MyUserDetailService(UserRepository userRepo) {
		super();
		this.userRepo = userRepo;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		// load user from the DB
		User user = this.userRepo.findByUsername(username);
		
		if(user == null) {
			throw new UsernameNotFoundException("User Not found!");
		}
		
		return new UserPrincipal(user);
	}
	
}
