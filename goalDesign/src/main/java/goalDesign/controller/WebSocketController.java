package goalDesign.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import goalDesign.model.Request;
import goalDesign.model.RequestStatus;
import goalDesign.service.FriendService;

@Controller
public class WebSocketController {

	@Autowired
	private FriendService friendService;

	@Autowired
	private SimpMessagingTemplate simpMessagingTemplate;

	@MessageMapping("/message/requests")
	public void repylRequests(Request request) throws InterruptedException {
		Request req = friendService.manageRequest(request, RequestStatus.SEND);
		String to = request.getReceiver().getId();
		String from = request.getSender().getId();
		this.simpMessagingTemplate.convertAndSend("/queue/messages/" + to, req);
		this.simpMessagingTemplate.convertAndSend("/queue/messages/" + from, req);
	}

}
