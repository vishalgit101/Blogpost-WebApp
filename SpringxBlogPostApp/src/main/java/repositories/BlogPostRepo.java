package repositories;

import java.util.List;

import org.springframework.stereotype.Repository;

import DTOs.PostHomeDTO;
import entity.Post;
import entity.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

@Repository
public class BlogPostRepo {
	// DI Entity manager...
	private EntityManager entityManager;

	public BlogPostRepo(EntityManager entityManager) {
		super();
		this.entityManager = entityManager;
	}
	
	public void saveData(Post post) {
		this.entityManager.persist(post);
	}
	
	/*// Get all the posts
	public List<Post> getAllPosts(){
		System.out.println("Getting all the posts....");
	    TypedQuery<Post> theQuery = this.entityManager.createQuery("Select p.id, p.title, p.created, p.updated, p.author, p.url, p.summary from Post p order by p.id desc", Post.class);
		List<Post> posts =  theQuery.getResultList();
		System.out.println(posts);
		return posts;
	}*/
	
	public List<Object[]> getAllPosts(){ // have directly used postDto here, as shown below
		System.out.println("Getting all the posts....");
		TypedQuery<Object[]> theQuery = this.entityManager.createQuery(
			    "SELECT p.id, p.title, p.created, p.updated, p.url, p.summary " +
			    "FROM Post p ORDER BY p.id DESC", Object[].class); // PostDto.class would have been fine, hibernate allows that
		
		List<Object[]> posts =  theQuery.getResultList();
		System.out.println(posts);
		return posts;
	}
	
	/*public List<PostDto> getAllPosts() {
	    System.out.println("Getting all the posts....");

	    // JPQL query that projects fields from Post entity into PostDto
	    TypedQuery<PostDto> theQuery = this.entityManager.createQuery(
	        "SELECT new com.example.PostDto(p.id, p.title, p.created, p.updated, p.url, p.summary) " +
	        "FROM Post p ORDER BY p.id DESC", PostDto.class);

	    List<PostDto> posts = theQuery.getResultList();
	    System.out.println(posts);
	    return posts;
	}*/

	public Post getPost(int id) {
		return this.entityManager.find(Post.class, id);
	}

	public void delete(int id) {
		Post post = this.entityManager.find(Post.class, id);
	    if (post == null) {
	        throw new IllegalArgumentException("Post with ID " + id + " not found.");
	    }
		this.entityManager.remove(post);
	}

	public List<PostHomeDTO> getSavedPosts(User user) {
		TypedQuery<PostHomeDTO> theQuery = this.entityManager.createQuery(
		        "SELECT  new PostHomeDTO(p.id, p.title, p.created, p.updated, p.url, p.summary, a.email) " +
		        "FROM User u JOIN u.savedPosts p " +
		        "JOIN p.author a " + // added later
		        "WHERE u.id = :userId " +
		        "ORDER BY p.id DESC", 
		        PostHomeDTO.class
		    );

		theQuery.setParameter("userId", user.getId());
		    
		theQuery.setParameter("userId", user.getId());
		List<PostHomeDTO> posts = theQuery.getResultList();
		return posts;
	}
	
}
