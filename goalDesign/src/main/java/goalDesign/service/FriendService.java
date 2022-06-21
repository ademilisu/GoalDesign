package goalDesign.service;

import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import goalDesign.Repository.ProfileRepository;
import goalDesign.Repository.RequestRepository;
import goalDesign.Repository.UserRepository;
import goalDesign.model.Profile;
import goalDesign.model.Request;
import goalDesign.model.RequestStatus;
import goalDesign.model.User;

@Service
public class FriendService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RequestRepository requestRepository;

	@Autowired
	private ProfileRepository profileRepository;

	public List<Profile> friendsList(Profile profile) {
		User user = userRepository.findByUsername(profile.getUsername());
		return user != null ? user.getFriends() : null;
	}

	public List<Request> requestsList(Profile profile) {
		return requestRepository.findAllByReceiverOrSender(profile, profile);
	}

	public Request manageRequest(Request request, RequestStatus status) {
		if (status.equals(RequestStatus.SEND)) {
			request.setId(UUID.randomUUID().toString());
			request = requestRepository.save(request);
		}
		if (status.equals(RequestStatus.ACCEPT)) {
			User sUser = userRepository.findByUsername(request.getSender().getUsername());
			User rUser = userRepository.findByUsername(request.getReceiver().getUsername());
			sUser.addFriend(request.getReceiver());
			rUser.addFriend(request.getSender());
			userRepository.save(sUser);
			userRepository.save(rUser);
			requestRepository.delete(request);
		}
		if (status.equals(RequestStatus.CANCEL)) {
			if(request.getId() != null) {
				requestRepository.delete(request);
			}
		}
		return request;
	}

	public Profile removeFriend(Profile owner, Profile friend) {
		User oUser = userRepository.findByUsername(owner.getUsername());
		User fUser = userRepository.findByUsername(friend.getUsername());
		oUser.deleteFriend(friend);
		fUser.deleteFriend(owner);
		userRepository.save(oUser);
		userRepository.save(fUser);
		return friend;
	}

	public void removeRequest(Request request) {
		requestRepository.delete(request);
	}

	public List<Profile> searchUser(String username) {
		Profile profile = new Profile();
		profile.setUsername(username);
		ExampleMatcher matcher = ExampleMatcher.matchingAll().withStringMatcher(ExampleMatcher.StringMatcher.STARTING);
		Example<Profile> example = Example.of(profile, matcher);
		List<Profile> result = profileRepository.findAll(example);
		return result == null ? null : result;
	}

}
