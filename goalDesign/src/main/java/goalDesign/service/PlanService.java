package goalDesign.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import goalDesign.Dto.PlanAction;
import goalDesign.Repository.PlanRepository;
import goalDesign.model.Goal;
import goalDesign.model.Plan;
import goalDesign.model.GoalAndPlanStatus;

@Service
public class PlanService {

	@Autowired
	private PlanRepository planRepository;

	public List<Plan> list(Goal goal, GoalAndPlanStatus status, String date) {
		if (status == null) {
			if(date == "start") {
				return planRepository.findAllByGoalOrderByStart(goal);
			}
			else return planRepository.findAllByGoalOrderByEndDesc(goal);
		} else {
			return planRepository.findAllByGoalAndStatus(goal, status);
		}
	}

	public Plan findPlan(String planId) {
		Optional<Plan> result = planRepository.findById(planId);
		return result.isPresent() ? result.get() : null;
	}

	public PlanAction save(Goal goal, Plan plan) {
		PlanAction planAction = new PlanAction();
		if (goal != null) {
			if (plan.getId() != null) {
				plan = planRepository.save(plan);
			} else {
				List<Plan> plans = list(goal, null, null);
				if (isThereSamePlan(plans, plan)) {
					planAction.setCode(1);
				} else {
					plan.setId(UUID.randomUUID().toString());
					plan.setCreate(new Date());
					plan.setStatus(GoalAndPlanStatus.CREATED);
					plan.setGoal(goal);
					plan = planRepository.save(plan);
				}
			}
			planAction.setCode(10);
			planAction.setPlan(plan);
		} else {
			planAction.setCode(0);
		}
		return planAction;
	}

	public void deletePlan(Plan plan) {
		planRepository.delete(plan);
	}

	private boolean isThereSamePlan(List<Plan> plans, Plan plan) {
		if (plans != null) {
			for (Plan temp : plans) {
				if (temp.getTitle().equals(plan.getTitle())) {
					return true;
				}
			}
		}
		return false;
	}

}
