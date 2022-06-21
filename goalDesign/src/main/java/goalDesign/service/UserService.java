package goalDesign.service;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import goalDesign.Dto.UserRequest;
import goalDesign.Dto.UserResponse;
import goalDesign.Repository.ImageRepository;
import goalDesign.Repository.UserRepository;
import goalDesign.model.Image;
import goalDesign.model.Profile;
import goalDesign.model.Request;
import goalDesign.model.User;

@Service
public class UserService {
	
	@Autowired
	private CommentService commentService;
	
	@Autowired
	private FriendService friendService;

	@Autowired
	private ProfileService profileService;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ImageRepository imageRepository;

	public UserResponse login(UserRequest request) {
		User user = userRepository.findByUsername(request.getUsername());
		UserResponse ur = new UserResponse();
		if (user != null && user.getPassword().equals(request.getPassword())) {
			ur.setCode(10);
			ur.setProfile(user.getProfile());
		} else {
			ur.setCode(0);
		}
		return ur;
	}

	public UserResponse signup(UserRequest request) {
		UserResponse userResponse = new UserResponse();
		User user = userRepository.findByUsername(request.getUsername());
		if (user == null) {
			user = new User();
			user.setId(UUID.randomUUID().toString());
			user.setUsername(request.getUsername());
			user.setPassword(request.getPassword());
			Image image = new Image();
			image.setId(UUID.randomUUID().toString());
			imageRepository.save(image);
			Profile profile = new Profile();
			profile.setId(UUID.randomUUID().toString());
			profile.setUsername(user.getUsername());
			profile.setRegister(new Date());
			profile.setImageId(image.getId());
			profile = profileService.saveProfile(profile, null, "true");
			user.setProfile(profile);
			userRepository.save(user);
			userResponse.setCode(10);
		} else {
			userResponse.setCode(0);
		}
		return userResponse;
	}

	public Profile updateProfile(Profile profile, MultipartFile file, String defaultImage) {
		User user = findByUsername(profile.getUsername());
		profile = profileService.saveProfile(profile, file, defaultImage);
		user.setProfile(profile);
		userRepository.save(user);
		return profile;
	}

	public User findByUsername(String username) {
		return userRepository.findByUsername(username);
	}

	public Profile findProfile(String profileId) {
		return profileService.findById(profileId);
	}

	public void deleteAccount(Profile profile) {
		User user = findByUsername(profile.getUsername());
		List<Profile> friends = user.getFriends();
		if(friends != null) {
			for(Profile temp : friends) {
				friendService.removeFriend(profile, temp);
			}
		}
		List<Request> requests = friendService.requestsList(profile);
		if(requests != null) {
			for(Request temp : requests) {
				friendService.removeRequest(temp);
			}
		}
		commentService.deleteAllComments(profile);
		profileService.deleteProfile(profile);
		userRepository.deleteById(user.getId());
	}
}
