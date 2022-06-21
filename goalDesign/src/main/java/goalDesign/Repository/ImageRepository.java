package goalDesign.Repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import goalDesign.model.Image;

public interface ImageRepository extends MongoRepository<Image, String> {

}
