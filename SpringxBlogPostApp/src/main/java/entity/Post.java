package entity;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name="Post")
public class Post {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name ="id")
	private int id;
	
	@Column(name ="title")
	private String title;
	
	@Column(name ="content")
	private String content;
	
	@Column(name ="created_at")
	private String created;
	
	@Column(name ="updated_at")
	private String updated;
	
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name= "author_id") // Direct Relationship
	private User author;
	
	@Column(name="image_url")
	private String url;
	
	@Column(name="summary")
	private String summary;
	
	@JsonIgnore
	@OneToMany(mappedBy = "post") // Refers to field name in Comment.java
	private List<Comment> comments;
	
	@JsonIgnore
	@ManyToMany(mappedBy = "savedPosts", cascade = CascadeType.ALL, fetch = FetchType.LAZY) // cos i dont want to lad all the users
	private Set<User> usersWhoSaved = new HashSet<User>();

	public Post() {
		super();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getCreated() {
		return created;
	}

	public void setCreated(String created) {
		this.created = created;
	}

	public String getUpdated() {
		return updated;
	}

	public void setUpdated(String updated) {
		this.updated = updated;
	}

	/*public List<Comment> getComments() {
		return comments;
	}

	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}*/

	public User getAuthor() {
		return author;
	}

	public void setAuthor(User author) {
		this.author = author;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	@Override
	public String toString() {
		return "Post [id=" + id + ", title=" + title + ", content=" + content + ", created=" + created + ", updated="
				+ updated /*+ ", comments=" + comments*/ + "]";
	}

	public Set<User> getUsersWhoSaved() {
		return usersWhoSaved;
	}

	public void addUsersWhoSaved(User usersWhoSaved) {
		this.usersWhoSaved.add(usersWhoSaved);
	} 
	
	
}
