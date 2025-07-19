package services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import entity.Comment;
import jakarta.transaction.Transactional;
import repositories.CommentRepository;

@Service
public class CommentService {
	// DI repo
	private final CommentRepository commentRepository;
	
	@Autowired
	public CommentService(CommentRepository commentRepository) {
		super();
		this.commentRepository = commentRepository;
	}
	
	public void saveComment(Comment comment) {
		
		Comment parentComment = comment.getParentComment();
		
		if(parentComment != null ) { // meaning not a top level comment

			// Reloading Parent Comment of a reply
			// comment is a reply of another top level comment, find that top level parent comment with its comment id
			parentComment = this.commentRepository.findById(parentComment.getId());
			
			// this validates that even the comments coming with different fake ids (reply comment id)
			// Validate parentComment itself must NOT be a reply as replies of replies are not allowed
			if(parentComment.getParentComment() != null) {
				throw new IllegalArgumentException("Replies of replies are not allowed");
			}
		}
		
		// Save comment normally if validation passes
		this.commentRepository.addComment(comment);
	}
	
	@Transactional
	public void addComment(Comment comment, String parentId) {
		
		if(parentId != null) {
			int id = Integer.parseInt(parentId);
			Comment parentComment = this.commentRepository.findById(id);
			
			if(parentComment.getParentComment() != null) {
				throw new IllegalArgumentException("Replies of replies are not allowed");
			}
			
			comment.setParentComment(parentComment);
			
		}
		// Save comment normally if validation passes
		this.commentRepository.addComment(comment);
	}
	
	public List<Comment> allComments(int postId){
		List<Comment> comments = this.commentRepository.getComments(postId);
		if(comments != null) {
			return comments;
		}
		
		return null;
	}
	
}
