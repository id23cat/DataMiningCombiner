package evm.dmc.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

import evm.dmc.service.RequestPath;

@Controller
@RequestMapping(RequestPath.SIGNIN)
public class SignInController {
private static final Logger logger = LoggerFactory.getLogger(RegisterController.class);
	
	@Value("${views.signin}")
	String signinView;
	
	@GetMapping
	public String getSignInPage(Model model, @RequestHeader(value = "X-Requested-With", required = false) String requestedWith){
		logger.debug("SignIn page controller");

		
		return signinView.concat(":: signinForm");
	}
	
	@PostMapping(RequestPath.AUTH)
	public String authenticate(){
		logger.debug("Auth request");

		return "";
	}
}
