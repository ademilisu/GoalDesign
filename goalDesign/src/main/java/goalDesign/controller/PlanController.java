package goalDesign.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import goalDesign.Dto.PlanAction;
import goalDesign.model.Goal;
import goalDesign.model.Plan;
import goalDesign.service.GoalService;
import goalDesign.service.PlanService;

@RestController
public class PlanController {

	@Autowired
	private PlanService planService;
	
	@Autowired
	private GoalService goalService;

	@GetMapping("/goals/{goalId}/plans")
	public List<Plan> list(@PathVariable String goalId) {
		Goal goal = goalService.findGoal(goalId);
		return goal != null ? planService.list(goal, null, null): null;
	}

	@GetMapping("/plans/{planId}")
	public Plan findPlan(@PathVariable String planId) {
		return planService.findPlan(planId);
	}

	@PostMapping("/goals/{goalId}/plans")
	public PlanAction save(@PathVariable String goalId, @RequestBody Plan plan) {
		Goal goal = goalService.findGoal(goalId);
		PlanAction pa = null;
		if(goal != null) {
			pa =planService.save(goal, plan);
			goalService.updateGoal(goal);
		}
		return pa;
	}

	@PutMapping("/goals/plans")
	public PlanAction update(@RequestBody Plan plan) {
		Goal goal = goalService.findGoal(plan.getGoal().getId());
		PlanAction pa = null;
		if(goal != null) {
			pa =planService.save(goal, plan);
			goalService.updateGoal(goal);
		}
		return pa;
	}
	
	@DeleteMapping("/plans/{planId}")
	public void delete(@PathVariable String planId) {
		Plan plan = planService.findPlan(planId);
		if(plan != null) {
			Goal goal = goalService.findGoal(plan.getGoal().getId());
			planService.deletePlan(plan);
			goalService.updateGoal(goal);
		}
	}
}
