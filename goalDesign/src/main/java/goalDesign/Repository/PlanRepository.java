package goalDesign.Repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import goalDesign.model.Goal;
import goalDesign.model.GoalAndPlanStatus;
import goalDesign.model.Plan;

public interface PlanRepository extends MongoRepository<Plan, String> {
	
	List<Plan> findAllByGoalOrderByEndDesc(Goal goal);
	List<Plan> findAllByGoalAndStatus(Goal goal, GoalAndPlanStatus status);
	List<Plan> findAllByGoalOrderByStart(Goal goal);
}
