package services;

import java.util.Set;

import org.springframework.stereotype.Service;

import entity.Role;
import entity.User;
import jakarta.transaction.Transactional;
import repositories.RoleRepo;
import repositories.UserRepository;

@Service
public class UserService {
	// Di repo
	private UserRepository userRepo;
	private RoleRepo roleRepo;
	public UserService(UserRepository userRepo, RoleRepo roleRpo) {
		super();
		this.userRepo = userRepo;
		this.roleRepo = roleRpo;
	}
	
	/*@Transactional
	public void register(User user) {
		this.userRepo.save(user);
	}*/

	@Transactional
	public void registerWithDefaultRole(User user) {
		Role defaultRole = this.roleRepo.findRole("USER");
		
		user.setRoles(Set.of(defaultRole));
		this.userRepo.save(user);
	}
	
}
