package controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
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
	
	@PostMapping("/api/manager/save") // for publishing the post
	public ResponseEntity<Void> publish(@RequestBody Map<String, String> data, @AuthenticationPrincipal UserPrincipal userPrincipal){
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
	
	@GetMapping("api/read") // reading post with username/author name
	public ResponseEntity<Map<String, Object>> getPostById(@RequestParam String id){
		Map<String, Object> mapPost = new HashMap<>();
		int postId = Integer.parseInt(id);
		Post tempPost = this.blogPostService.loadPost(postId);
		String username = tempPost.getAuthor().getEmail();
		
		mapPost.put("username", username);
		mapPost.put("post", tempPost);
		// haeders arent assible to the frontend so carete dto or use map
		//ResponseEntity.status(HttpStatus.OK).header("username", username).body(tempPost);
		
		return ResponseEntity.status(HttpStatus.OK).body(mapPost);
	}
	
	
	@GetMapping("/manager/blog-write")
	public void write(HttpServletResponse response) throws IOException {
		response.sendRedirect("/blog-write2.html"); // Redirects to static login page
	}
	
	@GetMapping("/api/user/me") // get user after login to display their name and role on the header
	public ResponseEntity<Map<String,String>> getUser(@AuthenticationPrincipal UserPrincipal userPrincipal){
		Map<String, String> userInfo = new HashMap<>();
		
		String email = userPrincipal.getUsername();
		//List<String> roles = userPrincipal.getAuthorities();
		Collection<? extends GrantedAuthority> auths = userPrincipal.getAuthorities(); // checks all the roles a user may have
		
		List<String> roles = new ArrayList<>();
		
		for(GrantedAuthority auth: auths) {
			
			roles.add(auth.getAuthority());
			
		}
		
		String highestRole = "";
		
		if(roles.contains("ROLE_ADMIN")) {
			highestRole = "ROLE_ADMIN";
		}else if(roles.contains("ROLE_MANAGER")) {
			highestRole = "ROLE_MANAGER";
		}else {
			highestRole = "ROLE_USER";
		}
		
		userInfo.put("role", highestRole);
		userInfo.put("email", email);
		
		return ResponseEntity.status(HttpStatus.OK).body(userInfo);
		
	}
	
	@DeleteMapping("/api/admin/delete") // /api/admin/delete/?${postId}
	public ResponseEntity<Void> deletePost(@RequestParam String postId){
		int id = Integer.parseInt(postId);
		System.out.println("Delete Function.... checking for execution in controller");
		this.blogPostService.deletePost(id);
		
		return ResponseEntity.status(HttpStatus.OK).build();
		
	}
	
	@PostMapping("/api/save-post") // Post mapping for saving posts 
	public ResponseEntity<Void> savePost(@RequestParam String postId, @AuthenticationPrincipal UserPrincipal principal){ // post saved by user
		int id = Integer.parseInt(postId);
		String email = principal.getUsername();
		User user = this.userRepository.findByUsername(email);
		System.out.println("Save Function.... checking for execution in controller");
		this.blogPostService.savePost(id, user);
		return ResponseEntity.status(HttpStatus.OK).build();
	}
	
	@GetMapping("api/save-post") // GetMappiing to fetch the saved posts by the user
	public ResponseEntity<List<PostHomeDTO>> getSavedPosts( @AuthenticationPrincipal UserPrincipal principal){
		//get the user
		String email = principal.getUsername();
		User user = this.userRepository.findByUsername(email);
		
		// get saved posts form the service and repo
		List<PostHomeDTO> posts = this.blogPostService.getSavedPosts(user);
		System.out.println("Saved Posts" + posts);
		return ResponseEntity.status(HttpStatus.OK).body(posts);
		
	}
	
	
	
}
