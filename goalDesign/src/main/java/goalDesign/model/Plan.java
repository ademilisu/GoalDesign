package goalDesign.model;

import java.util.Date;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.DBRef;

@TypeAlias("Plan")
public class Plan {
	
	@Id
	private String id;
	private String title;
	private String unit;
	private int startPoint;
	private int targetPoint;
	private String description;
	private GoalAndPlanStatus status;
	private Date create;
	private Date start;
	private Date end;

	@DBRef
	private Goal goal;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public GoalAndPlanStatus getStatus() {
		return status;
	}

	public void setStatus(GoalAndPlanStatus status) {
		this.status = status;
	}

	public Date getCreate() {
		return create;
	}

	public void setCreate(Date create) {
		this.create = create;
	}

	public Date getStart() {
		return start;
	}

	public void setStart(Date start) {
		this.start = start;
	}

	public Date getEnd() {
		return end;
	}

	public void setEnd(Date end) {
		this.end = end;
	}

	public Goal getGoal() {
		return goal;
	}

	public void setGoal(Goal goal) {
		this.goal = goal;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public int getStartPoint() {
		return startPoint;
	}

	public void setStartPoint(int startPoint) {
		this.startPoint = startPoint;
	}

	public int getTargetPoint() {
		return targetPoint;
	}

	public void setTargetPoint(int targetPoint) {
		this.targetPoint = targetPoint;
	}

}
