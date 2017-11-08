package evm.dmc.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

import evm.dmc.service.RequestPath;
import evm.dmc.utils.AjaxUtils;
import evm.dmc.web.business.RegistrationForm;

@Controller
@RequestMapping(RequestPath.REGISTER)
public class RegisterController {
	private static final Logger logger = LoggerFactory.getLogger(RegisterController.class);
	
	@Value("${views.register}")
	String regView;
	
	@GetMapping
	public String getRegistrationPage(Model model, @RequestHeader(value = "X-Requested-With", required = false) String requestedWith){
		logger.debug("regPage value: {}", regView);
		model.addAttribute(new RegistrationForm());
		if (AjaxUtils.isAjaxRequest(requestedWith)) {
			logger.debug("registration Ajax: {}", regView.concat(" :: registerForm"));

			return regView.concat(" :: registerForm");
		}
		
		return regView;
	}

}
