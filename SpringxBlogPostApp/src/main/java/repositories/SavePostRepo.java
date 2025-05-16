package repositories;

import org.springframework.stereotype.Repository;

import entity.Post;
import entity.User;
import jakarta.persistence.EntityManager;

@Repository
public class SavePostRepo {
	//DI entity manager
	private EntityManager entityManager;

	public SavePostRepo(EntityManager entityManager) {
		super();
		this.entityManager = entityManager;
	}
	
	public void savePost(int postId, User user) {
		Post tempPost = this.entityManager.find(Post.class, postId);
		User tempUser = user;

		tempUser.addSavedPosts(tempPost);
		tempPost.addUsersWhoSaved(tempUser);
		
		this.entityManager.merge(tempUser);
		//this.entityManager.merge(tempPost); One is enough, becuase of hibernte persistence contextio(ession)
	}
}
