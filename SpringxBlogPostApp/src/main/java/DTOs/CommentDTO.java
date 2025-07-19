package DTOs;

import java.util.List;

public class CommentDTO {
    private int id;
    private String comment;
    private String createdAt;
    private String authorName;
    private List<CommentDTO> replies;
    private Integer parentId;
    
    
	public CommentDTO() {
		super();
	}
	public CommentDTO(int id, String comment, String createdAt, String authorName, List<CommentDTO> replies, Integer parentId) {
		super();
		this.id = id;
		this.comment = comment;
		this.createdAt = createdAt;
		this.authorName = authorName;
		this.replies = replies;
		this.parentId = parentId;
	}
	
	
	
	public Integer getParentId() {
		return parentId;
	}
	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}
	public String getAuthorName() {
		return authorName;
	}
	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}
	public List<CommentDTO> getReplies() {
		return replies;
	}
	public void setReplies(List<CommentDTO> replies) {
		this.replies = replies;
	}

    
    
}

