package goalDesign.Repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import goalDesign.model.Profile;

public interface ProfileRepository extends MongoRepository<Profile, String> {

}
