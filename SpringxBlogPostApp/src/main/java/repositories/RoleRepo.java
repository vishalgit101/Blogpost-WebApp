package repositories;

import java.util.List;

import org.springframework.stereotype.Repository;

import entity.Role;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;

@Repository
public class RoleRepo {
	//DI Entity manager
	private final EntityManager entityManager;

	public RoleRepo(EntityManager entityManager) {
		super();
		this.entityManager = entityManager;
	}
	
	public Role findRole(String roleName) {
	    try {
	        TypedQuery<Role> query = entityManager.createQuery(
	            "FROM Role r WHERE r.name = :roleName", Role.class
	        );
	        query.setParameter("roleName", roleName);
	        List<Role> role = query.getResultList();
	        System.out.println( "Role: " + role);
	        
	        return role.getFirst();
	    } catch (NoResultException e) {
	        
	        return null; 
	    }
	}
}
