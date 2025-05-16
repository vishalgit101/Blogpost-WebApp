package entity;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name="User")
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;
	
	@Column(name ="username")
	private String username;
	
	@Column(name="email")
	private String email;
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Column(name="password")
	private String password;
	
	@OneToMany(mappedBy="author")
	private List<Post> posts;  // number of posts author craeted
	
	//Also a field for saving posts... Manany to many needed
	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "saved_posts", joinColumns = {@JoinColumn(name="user_id")}, inverseJoinColumns = {@JoinColumn(name="post_id")})
	private Set<Post>savedPosts = new HashSet<Post>();
	
	@OneToMany(mappedBy = "author") // Refers to field name in Comment.java
	private List<Comment> comments;
	
	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinTable(name="user_roles", 
	joinColumns = {@JoinColumn(name="user_id")},
	inverseJoinColumns = {@JoinColumn(name="role_id")})
	private Set<Role> roles = new HashSet<>(); // set default Role, like user

	public User() {
		super();
	}

	
	
	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public List<Comment> getComments() {
		return comments;
	}

	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}

	public List<Post> getPosts() {
		return posts;
	}

	public void setPosts(List<Post> posts) {
		this.posts = posts;
	}

	public Set<Post> getSavedPosts() {
		return savedPosts;
	}

	public void addSavedPosts(Post post) {
		this.savedPosts.add(post);
	}
	
	public void addRole(Role role) {
		this.roles.add(role);
	}
}
