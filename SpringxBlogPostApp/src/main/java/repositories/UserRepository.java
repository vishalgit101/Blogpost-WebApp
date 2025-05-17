package repositories;



import java.util.List;

import org.springframework.stereotype.Repository;
import entity.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;

@Repository
public class UserRepository {
	// DI entity Manager
	private EntityManager entityManager;

	public UserRepository(EntityManager entityManager) {
		super();
		this.entityManager = entityManager;
	}
	
	public User findUserById(int id) {
	return this.entityManager.find(User.class, id);
		//return null;
	}
	
	public User findByUsername(String username) {
		System.out.println("Looking up user with email: " + username); // debug log
		try {
			TypedQuery<User> theQuery = this.entityManager.createQuery("from User where email=:theData", User.class);
			theQuery.setParameter("theData", username);
			return theQuery.getSingleResult();
		}catch (NoResultException ex) {
			System.out.println("No user found for email: " + username); // optional debug log
	        return null;
		}

	}
	
	
	public void save(User user) {
		this.entityManager.persist(user);
	}
	
	public void updateUser(User user) {
		this.entityManager.merge(user);
	}
	
	public List<User> allWriters(String role){
		TypedQuery<User> theQuery = this.entityManager.createQuery("Select u From User u Join u.roles r where r.name=:theData",User.class);
		theQuery.setParameter("theData", role);
		
		return theQuery.getResultList();
		
	}
}	
