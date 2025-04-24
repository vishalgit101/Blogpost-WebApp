package repositories;

import java.util.List;

import org.springframework.stereotype.Repository;

import entity.Post;
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
	
	public List<Object[]> getAllPosts(){
		System.out.println("Getting all the posts....");
		TypedQuery<Object[]> theQuery = this.entityManager.createQuery(
			    "SELECT p.id, p.title, p.created, p.updated, p.url, p.summary " +
			    "FROM Post p ORDER BY p.id DESC", Object[].class);
		
		List<Object[]> posts =  theQuery.getResultList();
		System.out.println(posts);
		return posts;
	}

	public Post getPost(int id) {
		return this.entityManager.find(Post.class, id);
	}
	
}
