package controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import DTOs.PostHomeDTO;
import entity.Post;
import entity.User;
import jakarta.servlet.http.HttpServletResponse;
import model.UserPrincipal;
import repositories.UserRepository;
import services.BlogPostService;

@RestController
public class BlogController {
	// Need Service layer...
	private BlogPostService blogPostService;
	private UserRepository userRepository;
	
	public BlogController(BlogPostService blogPostService, UserRepository userRepository) {
		super();
		this.blogPostService = blogPostService;
		this.userRepository = userRepository;
	}



	/*@PostMapping("/api/save")
	public ResponseEntity<Void> save(@RequestBody Post post){
		System.out.println("Post data:" + post);
		this.blogPostService.save(post);
		return ResponseEntity.status(HttpStatus.OK).body(null);
	}*/
	
	@PostMapping("/api/save")
	public ResponseEntity<Void> save(@RequestBody Map<String, String> data, @AuthenticationPrincipal UserPrincipal userPrincipal){
		// UserPrincipal only has some specific info about user... e.g: getUsername, getPassword
		// Need to find user from email....
	
		String email = userPrincipal.getUsername();
		User user = this.userRepository.findByUsername(email);
		System.out.println("Post data:" + data);
		Post tempPost = new Post();
		tempPost.setTitle(data.get("title"));  // Title...
		tempPost.setContent(data.get("content")); // Content...
		tempPost.setSummary(data.get("summary")); // Summary...
		tempPost.setUrl(data.get("img"));		// image_url...
		tempPost.setCreated(LocalDateTime.now().toString()); // created...
		tempPost.setUpdated(LocalDateTime.now().toString()); // updated...
		tempPost.setAuthor(user); // user...
		this.blogPostService.save(tempPost);
		return ResponseEntity.status(HttpStatus.OK).build();
	}
	
	@GetMapping("/api/all-posts")
	public ResponseEntity<List<PostHomeDTO>> getAllPosts() throws JsonProcessingException{
		System.out.println("Checking if its working or not");
		List<PostHomeDTO> posts = this.blogPostService.loadAll();
		System.out.println(posts);
		 System.out.println("Sending JSON: " + new ObjectMapper().writeValueAsString(posts));
		return ResponseEntity.status(HttpStatus.OK).body(posts);
	}
	
	/*@GetMapping("/api/get-post")
	public ResponseEntity<Void> getBlogById(@RequestParam String id){
		return null;
	}*/
	
	@GetMapping("api/read")
	public ResponseEntity<Post> getPostById(@RequestParam String id){
		int postId = Integer.parseInt(id);
		Post tempPost = this.blogPostService.loadPost(postId);
		return ResponseEntity.status(HttpStatus.OK).body(tempPost);
	}
	
	
	@GetMapping("/manager/blog-write")
	public void write(HttpServletResponse response) throws IOException {
		response.sendRedirect("/blog-write.html"); // Redirects to static login page
	}
	
}
