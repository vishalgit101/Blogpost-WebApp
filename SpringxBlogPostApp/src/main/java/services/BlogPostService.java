package services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import DTOs.PostHomeDTO;
import entity.Post;
import jakarta.transaction.Transactional;
import repositories.BlogPostRepo;

@Service
public class BlogPostService {
	// DI Repo...
	private BlogPostRepo blogRepo;

	@Autowired
	public BlogPostService(BlogPostRepo blogRepo) {
		super();
		this.blogRepo = blogRepo;
	}
	
	@Transactional
	public void save(Post post) {
		this.blogRepo.saveData(post);
	}
	
	@Transactional
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
	}
	
	@Transactional
	public Post loadPost(int id) {
		return this.blogRepo.getPost(id);
	}
	
}
