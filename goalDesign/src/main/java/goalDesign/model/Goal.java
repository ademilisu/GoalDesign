package goalDesign.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.DBRef;

@TypeAlias("Goal")
public class Goal {

	@Id
	private String id;

	private String title;
	private GoalAndPlanStatus status;
	private Date create;
	private Date start;
	private Date end;
	private int progress;

	@DBRef
	private Profile owner;
	
	@DBRef
	private Profile shared;

	@DBRef
	private List<Profile> participants;

	public void addParticipant(Profile profile) {
		if (participants == null) {
			participants = new ArrayList<>();
		}
		participants.add(profile);
	}

	public void deleteParticipant(Profile profile) {
		if (participants.size() > 0) {
			List<Profile> pr = new ArrayList<>();
			for (Profile temp : participants) {
				if (!profile.getUsername().equals(temp.getUsername())) {
					pr.add(temp);
				}
			}
			participants = pr;
		}
	}

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

	public int getProgress() {
		return progress;
	}

	public void setProgress(int progress) {
		this.progress = progress;
	}

	public Profile getOwner() {
		return owner;
	}

	public void setOwner(Profile owner) {
		this.owner = owner;
	}

	public List<Profile> getParticipants() {
		return participants;
	}

	public void setParticipants(List<Profile> participants) {
		this.participants = participants;
	}

	public Profile getShared() {
		return shared;
	}

	public void setShared(Profile shared) {
		this.shared = shared;
	}
	
}
