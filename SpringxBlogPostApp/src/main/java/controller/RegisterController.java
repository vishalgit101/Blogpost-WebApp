package controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import entity.User;
import services.UserService;

@RestController
public class RegisterController {
	private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(10);
	
	private UserService userService;

	public RegisterController(UserService userService) {
		super();
		this.userService = userService;
	}

	@PostMapping("/register")
	public ResponseEntity<Void> register(@RequestBody User user) {
		user.setPassword(encoder.encode(user.getPassword()));
		this.userService.register(user);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
	
	
}
