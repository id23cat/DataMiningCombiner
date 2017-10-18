package evm.dmc.web.testing.hello;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import evm.dmc.service.testing.ViewsService;
import evm.dmc.web.testing.HomeController;

@Controller
public class HelloThymeleafController {
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	ViewsService views;
	
	
	public HelloThymeleafController(@Autowired ViewsService views){
		this.views = views;
	}
	
	@RequestMapping("/testing/helloThyme")
    public String greeting(@RequestParam(value="name", required=false, defaultValue="World") 
    						String name, Model model) {
		logger.info("HW: " +name);
        model.addAttribute("name", name);
        return views.getHellothyme();
    }
}
