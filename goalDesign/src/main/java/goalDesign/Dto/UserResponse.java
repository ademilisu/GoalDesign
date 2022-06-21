package goalDesign.Dto;

import goalDesign.model.Profile;

public class UserResponse {
	
	private int code;
	private Profile profile;
	
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public Profile getProfile() {
		return profile;
	}
	public void setProfile(Profile profile) {
		this.profile = profile;
	}
	
	
}
