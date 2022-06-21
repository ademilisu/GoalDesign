package goalDesign.model;

import org.springframework.data.annotation.Id;

public class Request {
	
	@Id
	private String id;
	private Profile sender;
	private Profile receiver;
	
	public Profile getSender() {
		return sender;
	}
	public void setSender(Profile sender) {
		this.sender = sender;
	}
	public Profile getReceiver() {
		return receiver;
	}
	public void setReceiver(Profile receiver) {
		this.receiver = receiver;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
}
