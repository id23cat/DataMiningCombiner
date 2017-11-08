package evm.dmc.web.testing.hello;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import evm.dmc.web.testing.TestingController;

@Controller
public class SidebarController {
	private static final Logger logger = LoggerFactory.getLogger(TestingController.class);
	
	@RequestMapping("/testing/sidebar")
    public String sidebar(Model model) {
		
        return "testing/sidebarpage";
    }
}
