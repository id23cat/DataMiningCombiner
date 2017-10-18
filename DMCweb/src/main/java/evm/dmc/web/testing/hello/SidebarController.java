package evm.dmc.web.testing.hello;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import evm.dmc.web.testing.HomeController;

@Controller
public class SidebarController {
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	@RequestMapping("/testing/sidebar")
    public String sidebar(Model model) {
		
        return "testing/sidebarpage";
    }
}
