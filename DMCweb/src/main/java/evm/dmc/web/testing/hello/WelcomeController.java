package evm.dmc.web.testing.hello;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import evm.dmc.service.testing.TestingViewsService;

//@RestController
@Controller
public class WelcomeController {
	TestingViewsService views;
	
	@Value("${welcome.message}")
	private String message = "Hello World";
	
	@Autowired
	public WelcomeController(TestingViewsService vservice) {
		views = vservice;
	}
	
	@GetMapping("/welcome")
	public String welcome(Map<String, Object> model) {
		model.put("message", this.message);
		return views.getWelcome();
	}
}
