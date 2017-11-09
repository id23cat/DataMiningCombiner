package evm.dmc.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

import evm.dmc.service.RequestPath;
import evm.dmc.utils.AjaxUtils;
import evm.dmc.web.business.RegistrationForm;

@Controller
@RequestMapping
public class RegisterSignInController {
	private static final Logger logger = LoggerFactory.getLogger(RegisterSignInController.class);
	
	@Value("${views.regsign}")
	String regSignView;
	
	@ModelAttribute
	public RegistrationForm setupRegistrationForm(){
		return new RegistrationForm();
	}
	
	@GetMapping(value={RequestPath.REGISTER})
	public String getRegistrationPage(Model model, /*HttpServletRequest request,*/
			@RequestHeader(value = "X-Requested-With", required = false) String requestedWith){
		logger.debug("regsignPage value: {}", regSignView);
		if (AjaxUtils.isAjaxRequest(requestedWith)) {
			logger.debug("registration Ajax: {}", regSignView.concat(" :: regsignForm"));

			return regSignView.concat(" :: regsignForm(active='register')");
		}
		
		return regSignView;
	}
	
	@GetMapping(RequestPath.SIGNIN)
	public String getSignInPage(Model model, @RequestHeader(value = "X-Requested-With", required = false) String requestedWith){
		logger.debug("SignIn page controller");
		
		return regSignView.concat(" :: regsignForm(active='signin')");
	}
	
	@PostMapping(RequestPath.AUTH)
	public String authenticate(){
		logger.debug("Auth request");

		return "";
	}

}
