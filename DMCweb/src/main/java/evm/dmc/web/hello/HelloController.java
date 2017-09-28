package evm.dmc.web.hello;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import evm.dmc.web.HomeController;

@Controller
public class HelloController {
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	@RequestMapping("/hello")
    public String greeting(@RequestParam(value="name", required=false, defaultValue="World") 
    						String name, Model model) {
		logger.info("HW: " +name);
        model.addAttribute("name", name);
        return "greeting";
    }
}
