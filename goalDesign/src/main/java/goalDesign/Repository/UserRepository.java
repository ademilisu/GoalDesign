package goalDesign.Repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import goalDesign.model.User;

public interface UserRepository extends MongoRepository<User, String> {
	
	User findByUsername(String username);
	
}
