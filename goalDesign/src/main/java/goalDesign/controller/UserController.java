package goalDesign.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import goalDesign.Dto.UserRequest;
import goalDesign.Dto.UserResponse;
import goalDesign.model.Goal;
import goalDesign.model.Profile;
import goalDesign.service.GoalService;
import goalDesign.service.UserService;

@RestController
public class UserController {

	public static final String USER_SESSION = "user_session";
	
	@Autowired
	private GoalService goalService;
	
	@Autowired
	private UserService userService;

	@PostMapping("/login")
	public UserResponse login(@RequestBody UserRequest request, HttpSession session) {
		UserResponse ur = userService.login(request);
		if (ur.getCode() == 10) {
			session.setAttribute(USER_SESSION, ur);
		}
		return ur;
	}

	@PostMapping("/signup")
	public UserResponse signup(@RequestBody UserRequest request) {
		return userService.signup(request);
	}

	@GetMapping("/logout")
	public void logout(HttpSession session, HttpServletRequest req, HttpServletResponse res) {
		session.removeAttribute(USER_SESSION);
	}
	
	@GetMapping("/accounts/{profileId}/profile")
	public Profile getProfile(@PathVariable String profileId) {
		return userService.findProfile(profileId);
	}
	
	@GetMapping("/accounts/profile")
	public Profile getOwnProfile(HttpSession session) {
		UserResponse userResponse = (UserResponse) session.getAttribute(USER_SESSION);
		return userResponse != null ? userResponse.getProfile() : null;
	}

	@PostMapping(value = "/accounts/profile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public Profile updateProfile(@RequestBody MultipartFile file, @RequestParam("defaultImage") String defaultImage,
			HttpSession session) {
		UserResponse userResponse = (UserResponse) session.getAttribute(USER_SESSION);
		return userService.updateProfile(userResponse.getProfile(), file, defaultImage);
	}
	
	@PostMapping("/accounts/remove")
	public void removeAccount(HttpSession session, @RequestBody Profile profile) {
		List<Goal> goals = goalService.list(profile);
		if(goals != null) {
			for(Goal temp : goals) {
				goalService.delete(temp.getId(), profile);
			}
		}
		userService.deleteAccount(profile);
		
		session.removeAttribute(USER_SESSION);
	}
}
