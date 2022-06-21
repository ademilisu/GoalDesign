package goalDesign.Repository;

import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import goalDesign.model.Goal;
import goalDesign.model.Profile;

public interface GoalRepository extends MongoRepository<Goal, String> {
	
	List<Goal> findAllByOwnerOrderByCreateDesc(Profile profile);

	Goal findOneByTitleAndOwner(String title, Profile profile);

}
