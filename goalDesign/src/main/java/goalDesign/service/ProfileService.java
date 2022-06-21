package goalDesign.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Optional;
import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import goalDesign.Repository.ImageRepository;
import goalDesign.Repository.ProfileRepository;
import goalDesign.model.Image;
import goalDesign.model.Profile;

@Service
public class ProfileService {

	@Autowired
	private ImageRepository imageRepository;

	@Autowired
	private ProfileRepository profileRepository;
	
	public Profile findById(String id) {
		Optional<Profile> result = profileRepository.findById(id);
		return result.isPresent() ? result.get() : null;
	};

	public Image findImage(String id) {
		Optional<Image> result = imageRepository.findById(id);
		return result.isPresent() ? result.get() : null;
	}

	public Profile saveProfile(Profile profile, MultipartFile file, String defaultImage) {	
		Image image = findImage(profile.getImageId());
		if(image != null) {
			try {
				if(file == null || defaultImage.equals("true")) {
					File f = new File("src/main/resources/static/app/img/account.png");
					FileInputStream input = new FileInputStream(f);
					byte[] bytes = input.readAllBytes();
					image = buildImage(image, bytes);
					input.close();
				} else {
					image = buildImage(image, file.getBytes());
				}
			} catch(IOException exc){
				exc.printStackTrace();
			}
			profile.setImage(Base64.getEncoder().encodeToString(image.getData().getData()));
			profile.setImageId(image.getId());
			
		}
		return profileRepository.save(profile);
	}

	private Image buildImage(Image image, byte[] bytes) {
		image.setData(new Binary(BsonBinarySubType.BINARY, bytes));
		return imageRepository.save(image);
	}
	
	public void deleteProfile(Profile profile) {
		imageRepository.deleteById(profile.getImageId());
		profileRepository.deleteById(profile.getId());
	}

}
