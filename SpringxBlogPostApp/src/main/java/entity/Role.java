package entity;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name= "roles")
public class Role {
	
	@Id
	@Column(name= "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(name ="name")
	private String name;
	
	@ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY) // name of the field in the User Entity, Lazy, cos i dont want to load all the users with different roles
	private Set<User> users = new HashSet<>();

	public Role() {
		super();
	}

	public Set<User> getUsers() {
		return users;
	}



	public void setUsers(Set<User> users) {
		this.users = users;
	}



	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
	
}
