package repositories;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import entity.Comment;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

@Repository
public class CommentRepository {
	// DI
	private EntityManager entityManager;
	
	@Autowired
	public CommentRepository(EntityManager entityManager) {
		super();
		this.entityManager = entityManager;
	}

	public List<Comment> getComments(int postId){
		try {
			TypedQuery<Comment>	theQuery = this.entityManager.createQuery("SELECT c FROM Comment c where c.post.id=:theData", Comment.class);
			theQuery.setParameter("theData", postId);
			List<Comment> result = theQuery.getResultList();
			
			if(result.isEmpty()) {
				return null;
			}
			return result;
		} catch (Exception e) {
			throw new RuntimeException("Error: " + e.getMessage());
		}
	}
	
	public void addComment(Comment comment) {
		this.entityManager.persist(comment);
	}
	
	public Comment findById(int id) {
		try {
			Comment comment = this.entityManager.find(Comment.class, id);
			return comment;
		} catch (Exception e) {
			throw new RuntimeException("No Comment with: " + id + " found");
		}
		
	}
}
	
	
	
