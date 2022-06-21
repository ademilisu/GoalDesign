package goalDesign.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import goalDesign.Dto.GoalAction;
import goalDesign.Repository.GoalRepository;
import goalDesign.model.Comment;
import goalDesign.model.Goal;
import goalDesign.model.GoalAndPlanStatus;
import goalDesign.model.Plan;
import goalDesign.model.Profile;
import goalDesign.model.User;

@Service
public class GoalService {

	@Autowired
	private GoalRepository goalRepository;
	
	@Autowired
	private CommentService commentService;

	@Autowired
	private PlanService planService;

	@Autowired
	private UserService userService;

	public List<Goal> homeList(Profile profile) {
		List<Goal> myList = list(profile);
		List<Goal> friendList = new ArrayList<>();
		User user = userService.findByUsername(profile.getUsername());
		List<Profile> friends = user.getFriends();
		if (friends != null) {
			for (Profile temp : friends) {
				List<Goal> tempList = list(temp);
				if (tempList != null) {
					friendList.addAll(tempList);
				}
			}
		}
		myList.addAll(friendList);
		return myList;
	}

	public List<Goal> list(Profile profile) {
		return goalRepository.findAllByOwnerOrderByCreateDesc(profile);
	}

	public Goal findGoal(String goalId) {
		Optional<Goal> result = goalRepository.findById(goalId);
		return result.isPresent() ? result.get() : null;
	}

	public GoalAction saveGoal(Goal goal, Profile profile) {
		GoalAction goalAction = new GoalAction();
		if (goal.getId() != null) {
			goal = goalRepository.save(goal);
			goalAction.setCode(10);
			goalAction.setGoal(goal);
		} else {
			if (goal.getTitle() == null) {
				goalAction.setCode(0);
			} else if (findByTitleAndOwner(goal.getTitle(), profile) != null) {
				goalAction.setCode(1);
			} else {
				goal.setId(UUID.randomUUID().toString());
				goal.setCreate(new Date());
				goal.setStatus(GoalAndPlanStatus.CREATED);
				goal.setOwner(profile);
				goal = goalRepository.save(goal);
				goalAction.setCode(10);
				goalAction.setGoal(goal);
			}
		}
		return goalAction;
	}

	public void updateGoal(Goal goal) {
		int progress = getProgress(goal);
		if (progress == 0) {
			goal.setStatus(GoalAndPlanStatus.CREATED);
		} else if (progress == 100) {
			goal.setStatus(GoalAndPlanStatus.FINISHED);
		} else {
			goal.setStatus(GoalAndPlanStatus.STARTED);
		}
		goal.setEnd(getLastEndDate(goal));
		goal.setStart(getFirstStartDate(goal));
		goal.setProgress(progress);
		goalRepository.save(goal);
	}

	public void delete(String goalId, Profile profile) {
		Goal goal = findGoal(goalId);
		List<Goal> goals = list(goal.getShared());
		if (goals.size() > 0) {
			for (Goal temp : goals) {
				if (temp.getTitle().equals(goal.getTitle())) {
					temp.deleteParticipant(profile);
					goalRepository.save(temp);
					break;
				}
			}
		}
		List<Plan> plans = planService.list(goal, null, null);
		if (plans != null) {
			for (Plan temp : plans) {
				planService.deletePlan(temp);
			}
		}
		List<Comment> comments = commentService.list(goal);
		if(comments != null) {
			for(Comment temp: comments) {
				commentService.delete(temp.getId());
			}
		}
		goalRepository.deleteById(goalId);
	}

	public Goal shareGoal(Profile profile, String goalId) {
		Goal goal = findGoal(goalId);
		if (goal != null) {
			goal.addParticipant(profile);
			goalRepository.save(goal);
			List<Plan> plans = planService.list(goal, null, null);
			goal.setId(UUID.randomUUID().toString());
			goal.setCreate(new Date());
			goal.setShared(goal.getOwner());
			goal.setProgress(0);
			goal.setParticipants(null);
			goal.setOwner(profile);
			Goal shared = goalRepository.save(goal);
			if (plans != null) {
				for (Plan temp : plans) {
					temp.setId(UUID.randomUUID().toString());
					temp.setStatus(GoalAndPlanStatus.CREATED);
					temp.setGoal(shared);
					temp.setCreate(new Date());
					planService.save(shared, temp);
				}
			}
			return shared;
		} else
			return null;
	}

	private Goal findByTitleAndOwner(String title, Profile profile) {
		Goal goal = goalRepository.findOneByTitleAndOwner(title, profile);
		if (goal != null) {
			return goal;
		} else
			return null;
	}

	private Date getFirstStartDate(Goal goal) {
		List<Plan> list = planService.list(goal, null, "start");
		if (list.size() > 0) {
			return list.get(list.size() - 1).getStart();
		} else
			return goal.getCreate();
	}

	private Date getLastEndDate(Goal goal) {
		List<Plan> list = planService.list(goal, null, null);
		if (list.size() > 0) {
			return list.get(0).getEnd();
		} else
			return goal.getCreate();
	}

	private int getProgress(Goal goal) {
		List<Plan> listAll = planService.list(goal, null, null);
		List<Plan> completed = planService.list(goal, GoalAndPlanStatus.FINISHED, null);
		int progress = 0;
		if (listAll.size() > 0 && completed.size() > 0) {
			progress = (100 * completed.size()) / listAll.size();
		}
		if (completed.size() == 0) {
			progress = 0;
		}
		return progress;
	}

}
