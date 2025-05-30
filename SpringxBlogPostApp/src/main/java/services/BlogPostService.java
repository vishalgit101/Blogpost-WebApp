package services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import DTOs.PostHomeDTO;
import entity.Post;
import entity.User;
import jakarta.transaction.Transactional;
import repositories.BlogPostRepo;
import repositories.SavePostRepo;

@Service
public class BlogPostService {
	// DI Repo...
	private BlogPostRepo blogRepo;
	private SavePostRepo savePostRepo;

	@Autowired
	public BlogPostService(BlogPostRepo blogRepo, SavePostRepo savePostRepo) {
		super();
		this.blogRepo = blogRepo;
		this.savePostRepo = savePostRepo;
	}
	
	@Transactional
	public void save(Post post) {
		this.blogRepo.saveData(post);
	}
	
	/*@Transactional
	public List<PostHomeDTO> loadAll() {
		
		List<Post> posts = this.blogRepo.getAllPosts();
		
		List<PostHomeDTO> postDTOs = new ArrayList<>();
		
		for(Post post: posts) {
			PostHomeDTO postDTO = new PostHomeDTO();
			
			postDTO.setId(post.getId());
			postDTO.setCreated(post.getCreated());
			postDTO.setSummary(post.getSummary());
			postDTO.setTitle(post.getTitle());
			postDTO.setUrl(post.getUrl());
			postDTO.setUpdated(post.getUpdated());
			
			postDTOs.add(postDTO);
		}
		
		return postDTOs;
	}*/
	
	@Transactional
	public List<PostHomeDTO> loadAll() {
		
		List<Object[]> posts = this.blogRepo.getAllPosts();
		
		List<PostHomeDTO> postDTOs = new ArrayList<>();
		
		for (Object[] post : posts) {
		    PostHomeDTO postDTO = new PostHomeDTO();

		    postDTO.setId((int) post[0]);                  
		    postDTO.setTitle((String) post[1]);          
		    postDTO.setCreated((String) post[2]);    
		    postDTO.setUpdated((String) post[3]);
		    postDTO.setUrl((String) post[4]);               
		    postDTO.setSummary((String) post[5]);   
			
			postDTOs.add(postDTO); // adding it to the Array-list
		}
		
		return postDTOs;
	}
	
	
	
	@Transactional
	public Post loadPost(int id) {
		return this.blogRepo.getPost(id);
	}

	@Transactional
	public void deletePost(int id) {
		System.out.println("Delete Function.... checking for execution in service");
		this.blogRepo.delete(id);
		
	}

	@Transactional
	public void savePost(int postId, User user) {
		this.savePostRepo.savePost(postId, user);
	}

	public List<PostHomeDTO> getSavedPosts(User user) { // Set of saved posts should return.
		List<PostHomeDTO> posts = this.blogRepo.getSavedPosts(user);
		return posts;
	}
	
}
