package goalDesign.Repository;

import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import goalDesign.model.Comment;
import goalDesign.model.Goal;
import goalDesign.model.Profile;

public interface CommentRepository extends MongoRepository<Comment, String> {
	
	List<Comment> findAllByGoal(Goal goal);

	List<Comment> findAllByOwner(Profile profile);
}
