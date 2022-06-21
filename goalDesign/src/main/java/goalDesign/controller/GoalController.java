package goalDesign.controller;

import static goalDesign.controller.UserController.USER_SESSION;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import goalDesign.Dto.GoalAction;
import goalDesign.Dto.UserResponse;
import goalDesign.model.Goal;
import goalDesign.model.Profile;
import goalDesign.service.GoalService;
import goalDesign.service.UserService;

@RestController
@RequestMapping("/goals")
public class GoalController {

	@Autowired
	private GoalService goalService;

	@Autowired
	private UserService userService;

	@GetMapping("/home/list")
	public List<Goal> homeList(HttpSession session) {
		UserResponse userResponse = (UserResponse) session.getAttribute(USER_SESSION);
		return userResponse != null ? goalService.homeList(userResponse.getProfile()) : null;
	}

	@GetMapping("/list/{profileId}")
	public List<Goal> list(@PathVariable String profileId) {
		Profile profile = userService.findProfile(profileId);
		return profile != null ? goalService.list(profile) : null;
	}

	@GetMapping("/{goalId}")
	public Goal findGoal(@PathVariable String goalId) {
		return goalService.findGoal(goalId);
	}

	@PostMapping()
	public GoalAction saveGoal(@RequestBody Goal goal, HttpSession session) {
		UserResponse userResponse = (UserResponse) session.getAttribute(USER_SESSION);
		return userResponse != null ? goalService.saveGoal(goal, userResponse.getProfile()) : null;
	}

	@PutMapping()
	public GoalAction updateGoal(@RequestBody Goal goal) {
		return goalService.saveGoal(goal, null);
	}

	@DeleteMapping("/{goalId}")
	public void delete(@PathVariable String goalId, HttpSession session) {
		UserResponse userResponse = (UserResponse) session.getAttribute(USER_SESSION);
		goalService.delete(goalId, userResponse.getProfile());
	}

	@GetMapping("/{goalId}/share")
	public Profile share(HttpSession session, @PathVariable String goalId) {
		UserResponse userResponse = (UserResponse) session.getAttribute(USER_SESSION);
		Goal goal = goalService.shareGoal(userResponse.getProfile(), goalId);
		return goal != null ? userResponse.getProfile() : null;
	}

}
