package goalDesign.model;

import org.bson.types.Binary;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;

@TypeAlias("image")
public class Image {
	
	@Id
	private String id;
	private Binary data;
	
	public Binary getData() {
		return data;
	}

	public void setData(Binary data) {
		this.data = data;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
