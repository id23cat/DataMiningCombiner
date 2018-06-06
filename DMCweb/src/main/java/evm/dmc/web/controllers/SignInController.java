package evm.dmc.web.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

import evm.dmc.api.model.account.Account;
import evm.dmc.utils.AjaxUtils;
import evm.dmc.web.service.RequestPath;
import evm.dmc.web.service.Views;


@Controller
@RequestMapping
public class SignInController {
	private static final Logger logger = LoggerFactory.getLogger(SignInController.class);
	
	@Autowired
	private Views views;
	
	@ModelAttribute("registrationForm")
	public Account setupRegistrationForm(){
		return Account.builder().build();
	}
		
	@GetMapping(RequestPath.signin)
	public String getSignInPage(Model model, @RequestHeader(value = "X-Requested-With", required = false) String requestedWith){
		logger.debug("SignIn page controller");
		
		if (AjaxUtils.isAjaxRequest(requestedWith)) {
			logger.debug("SignIn Ajax: {}", views.getSignin().concat(views.getFragments().getSignin()));

			return views.getSignin().concat(views.getFragments().getSignin());
		}
		
		return views.getSignin();
		
	}
	
}
