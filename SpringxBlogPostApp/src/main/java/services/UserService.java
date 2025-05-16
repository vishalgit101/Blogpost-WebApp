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
	public void registerWithDefaultRole(User user) { // saves the user on signup with a default role of "USER"
		// used in signup or register controller
		Role defaultRole = this.roleRepo.findRole("USER");
		
		user.setRoles(Set.of(defaultRole));
		this.userRepo.save(user);
	}
	
	@Transactional
	public Boolean grantWritePermission(String userEmail) {
		// find user with that user email
		User user = this.userRepo.findByUsername(userEmail);
		
		if(user == null) {
			System.out.println("No User found...UserService, returing false");
			return false;
		}
		
		Role authorRole = this.roleRepo.findRole("MANAGER");
		
		user.addRole(authorRole);
		
		this.userRepo.updateUser(user);
		
		System.out.println("User Updated Successfully");
		return true;
	}
	
}
