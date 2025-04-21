package repositories;

import org.springframework.stereotype.Repository;
import entity.User;
import jakarta.persistence.EntityManager;
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
		TypedQuery<User> theQuery = this.entityManager.createQuery("from User where email=:theData", User.class);
		theQuery.setParameter("theData", username);
		return theQuery.getSingleResult();
	}
	
	
	public void save(User user) {
		this.entityManager.persist(user);
	}
}	
