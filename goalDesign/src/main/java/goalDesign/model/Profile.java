package goalDesign.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;

@TypeAlias("Profile")
public class Profile {
	
	@Id
	private String id;
	private String username;
	private String image;
	private String imageId;
	private Date register;
	
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

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getImageId() {
		return imageId;
	}

	public void setImageId(String imageId) {
		this.imageId = imageId;
	}

	public Date getRegister() {
		return register;
	}

	public void setRegister(Date register) {
		this.register = register;
	}

	@Override
	public String toString() {
		return "Profile [id=" + id + ", username=" + username + ", image=" + image + ", imageId=" + imageId
				+ ", register=" + register + "]";
	}

	
}
