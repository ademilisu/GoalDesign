package goalDesign.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.DBRef;

@TypeAlias("User")
public class User {

	private String id;
	private String username;
	private String password;
	
	@DBRef
	private Profile profile;
	
	@DBRef
	private List<Profile> friends;

	public void addFriend(Profile friend) {
		if (friends == null) {
			friends = new ArrayList<>();
		}
		friends.add(friend);
	}

	public void deleteFriend(Profile friend) {
		if (friends != null) {
			List<Profile> list = new ArrayList<>();
			for (Profile temp : friends) {
				if (!temp.getUsername().equals(friend.getUsername())) {
					list.add(temp);
				}
			}
			friends = list;
		}
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public List<Profile> getFriends() {
		return friends;
	}

	public void setFriends(List<Profile> friends) {
		this.friends = friends;
	}

	public Profile getProfile() {
		return profile;
	}

	public void setProfile(Profile profile) {
		this.profile = profile;
	}

}
