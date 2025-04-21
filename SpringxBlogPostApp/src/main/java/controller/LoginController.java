package controller;

import java.io.IOException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import entity.User;
import jakarta.servlet.http.HttpServletResponse;

@RestController
public class LoginController {
	
	@GetMapping("/login")
	public void login(HttpServletResponse response) throws IOException {
		response.sendRedirect("/login.html"); // Redirects to static login page
	}
	
	/*@PostMapping("/login")
	public ResponseEntity<User> postLogin() { // DTO with that sends User Data, Blogs, comments needed
		return null;
	}*/
}