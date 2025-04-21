package services;

import org.springframework.stereotype.Service;

import entity.User;
import jakarta.transaction.Transactional;
import repositories.UserRepository;

@Service
public class UserService {
	// Di repo
	private UserRepository userRepo;

	public UserService(UserRepository userRepo) {
		super();
		this.userRepo = userRepo;
	}
	
	@Transactional
	public void register(User user) {
		this.userRepo.save(user);
	}
	
}
