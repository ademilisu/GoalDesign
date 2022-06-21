package goalDesign.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import goalDesign.Dto.UserResponse;
import goalDesign.model.Request;
import goalDesign.model.RequestStatus;
import goalDesign.model.Profile;
import goalDesign.service.FriendService;

import static goalDesign.controller.UserController.USER_SESSION;

@RestController
@RequestMapping("/accounts")
public class FriendsController {


	@Autowired
	private FriendService friendsService;

	@GetMapping("/friends")
	public List<Profile> friendsList(HttpSession session) {
		UserResponse userResponse = (UserResponse) session.getAttribute(USER_SESSION);
		return userResponse != null ? friendsService.friendsList(userResponse.getProfile()) : null;
	}

	@GetMapping("/requests")
	public List<Request> requestsList(HttpSession session) {
		UserResponse userResponse = (UserResponse) session.getAttribute(USER_SESSION);
		return userResponse != null ? friendsService.requestsList(userResponse.getProfile()) : null;
	}

	@PostMapping("/requests/accept")
	public Request approveRequest(@RequestBody Request request, HttpSession session) {
		return friendsService.manageRequest(request, RequestStatus.ACCEPT);
	}

	@PostMapping("/requests/cancel")
	public Request cancelRequest(@RequestBody Request request, HttpSession session) {
		return friendsService.manageRequest(request, RequestStatus.CANCEL);
	}

	@PostMapping("/friends/remove")
	public Profile removeFriend(@RequestBody Profile profile, HttpSession session) {
		UserResponse userResponse = (UserResponse) session.getAttribute(USER_SESSION);
		return friendsService.removeFriend(userResponse.getProfile(), profile);
	}

	@GetMapping("/search/{username}")
	public List<Profile> searchUser(@PathVariable String username) {
		return friendsService.searchUser(username);
	}
}
