package goalDesign.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class PageController {

	@GetMapping({"/", "/list/**", "/login", "/signup", "/goal/**", "/friends/**", "/home", "/profile/**"})
	public String index() {
		return "index";
	}
}
