package goalDesign.Repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import goalDesign.model.Request;
import goalDesign.model.Profile;

public interface RequestRepository extends MongoRepository<Request, String> {
	
	Request findOneByReceiverAndSender(Profile sender, Profile receiver);
	
	List<Request> findAllByReceiver(Profile profile);

	List<Request> findAllByReceiverOrSender(Profile owner, Profile sender);

}
