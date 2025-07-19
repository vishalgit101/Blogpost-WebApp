package controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import DTOs.CommentDTO;
import entity.Comment;
import entity.Post;
import entity.User;
import model.UserPrincipal;
import repositories.UserRepository;
import services.BlogPostService;
import services.CommentService;

@RestController
public class CommentController {
	
	// DI
	private final CommentService commentService;
	private final UserRepository userRepository;
	private final BlogPostService blogPostService;
	
	@Autowired
	public CommentController(CommentService commentService, UserRepository userRepository,
			BlogPostService blogPostService) {
		super();
		this.commentService = commentService;
		this.userRepository = userRepository;
		this.blogPostService = blogPostService;
	}


	@GetMapping("/api/comments")
	public ResponseEntity<List<CommentDTO>> getComments(@RequestParam("postId") String postId){
		System.out.println("postId: " + postId);
		int tempPostId = Integer.parseInt(postId);
		List<Comment> comments =  this.commentService.allComments(tempPostId);
		

		
		// can also use dto instead of modifying frontend and database queries for solving nested json problem
		// but i need the parent comment and all the replies
		// probably have to restructure things
		List<CommentDTO> topLevel = new ArrayList<>();
		
		if( comments == null ) {
			return ResponseEntity.status(HttpStatus.OK).body(topLevel);
		}
		
		for (Comment comment : comments) {
			if(comment.getParentComment() == null) { // top level
				CommentDTO dto = new CommentDTO();
				// add to to level after setting fields
				String authorName = comment.getAuthor().getEmail();
				String[] arr = authorName.split("@");
				String name = arr[0];
				dto.setAuthorName(name);
				dto.setComment(comment.getComment());
				dto.setId(comment.getId());
				dto.setCreatedAt(comment.getCreated());
				dto.setParentId(null);
				
				// now add replies
				List<CommentDTO> replyList = new ArrayList<>();
				
				for (Comment reply : comment.getReplies()) {
					CommentDTO replyDto = new CommentDTO();
					replyDto.setAuthorName(reply.getAuthor().getEmail().split("@")[0]);
					replyDto.setId(reply.getId());
					replyDto.setComment(reply.getComment());
					replyDto.setCreatedAt(reply.getCreated());
					replyDto.setParentId(reply.getParentComment().getId());
					// no replies of reply so no need to set those
					replyList.add(replyDto);
				}
				dto.setReplies(replyList);
				topLevel.add(dto);
			}
		}
		
		return ResponseEntity.status(HttpStatus.OK).body(topLevel);
		
	}
	
	@PostMapping("/api/comments")
	public ResponseEntity<Void> postComment(@RequestParam(value= "parentCommentId", required=false) String parentCommentId ,@RequestParam("postId") String postId, @AuthenticationPrincipal UserPrincipal principal, @RequestBody Map<String, String> payload){
		System.out.println("Api hit");
		System.out.println("postId: " + postId);
		int postIdInt =  Integer.parseInt(postId);
		String parentId = parentCommentId;
		
		// get logged in user
		String email = principal.getUsername();
		User user = this.userRepository.findByUsername(email);
		Post post = this.blogPostService.loadPost(postIdInt);
		
		Comment comment = new Comment();
		String commentContent = payload.get("commentContent");
		System.out.println("comment content: " +  commentContent);
		comment.setAuthor(user);
		comment.setPost(post);
		comment.setComment(commentContent);
		comment.setCreated(LocalDateTime.now().toString());
		comment.setParentComment(null);
		
		this.commentService.addComment(comment, parentId);
		System.out.println("Comment Added Successfully");
		return ResponseEntity.status(HttpStatus.OK).build();
	}
}
