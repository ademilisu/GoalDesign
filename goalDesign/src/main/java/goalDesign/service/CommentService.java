package goalDesign.service;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import goalDesign.Dto.CommentAction;
import goalDesign.Repository.CommentRepository;
import goalDesign.model.Comment;
import goalDesign.model.Goal;
import goalDesign.model.Profile;

@Service
public class CommentService {
	
	@Autowired
	private CommentRepository commentRepository;
	
	
	public List<Comment> list(Goal goal) {
		return commentRepository.findAllByGoal(goal);
	}
	
	public List<Comment> listByProfile(Profile profile) {
		return commentRepository.findAllByOwner(profile);
	}
	
	public CommentAction save(Goal goal, Comment comment, Profile owner) {
		CommentAction ca = new CommentAction();
		if(goal != null) {
			comment.setId(UUID.randomUUID().toString());
			comment.setDate(new Date());
			comment.setGoal(goal);
			comment.setOwner(owner);
			commentRepository.save(comment);		
			ca.setCode(10);
			ca.setComment(comment);
		}
		else {
			ca.setCode(0);
		}
		return ca;
	}

	public void delete(String commentId) {
		commentRepository.deleteById(commentId);
	}

	public void deleteAllComments(Profile profile) {
		List<Comment> comments = listByProfile(profile);
		if(comments.size() > 0) {
			for(Comment temp : comments) {
				commentRepository.deleteById(temp.getId());
			}
		}
	}


	
}
