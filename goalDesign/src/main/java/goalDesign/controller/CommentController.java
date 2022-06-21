package goalDesign.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import static goalDesign.controller.UserController.USER_SESSION;
import goalDesign.Dto.CommentAction;
import goalDesign.Dto.UserResponse;
import goalDesign.model.Comment;
import goalDesign.model.Goal;
import goalDesign.service.CommentService;
import goalDesign.service.GoalService;

@RestController
@RequestMapping("/goals")
public class CommentController {
	
	@Autowired
	private GoalService goalService;
	
	@Autowired
	private CommentService commentService;
	
	@GetMapping("/{goalId}/comments")
	public List<Comment> list(@PathVariable String goalId) {
		Goal goal = goalService.findGoal(goalId);
		return goal != null ? commentService.list(goal) :null;
	}

	@PostMapping("/{goalId}/comments")
	public CommentAction save(@PathVariable String goalId, @RequestBody Comment comment, HttpSession session) {	
		UserResponse userResponse = (UserResponse) session.getAttribute(USER_SESSION);
		Goal goal = goalService.findGoal(goalId);
		CommentAction ca = null;
		if(goal != null && userResponse != null) {
			ca = commentService.save(goal, comment, userResponse.getProfile());
		}
		return ca;
	}

	@DeleteMapping("/comments/{commentId}")
	public void delete(@PathVariable String commentId) {
		 commentService.delete(commentId);
	}
}
