package evm.dmc.web;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import evm.dmc.business.account.Account;
import evm.dmc.business.account.AccountService;
import evm.dmc.service.RequestPath;
import evm.dmc.utils.AjaxUtils;
import evm.dmc.utils.MessageHelper;


@Controller
@RequestMapping
public class RegisterSignInController {
	private static final Logger logger = LoggerFactory.getLogger(RegisterSignInController.class);
	
	@Autowired
	PasswordEncoder encoder;
	
	@Autowired
	private AccountService accountService;
	
	@Value("${views.regsign}")
	private String regSignView;
	
	@Value("${views.register}")
	private String registerView;

	@Value("${views.signin}")
	private String signinView;
	
	@Value("${views.regsig_fragment}")
	private String regsign_fragment ;
	
	@ModelAttribute("registrationForm")
	public Account setupRegistrationForm(){
		return new Account();
	}
	
	@GetMapping(value={RequestPath.REGISTER})
	public String getRegistrationPage(Model model, /*HttpServletRequest request,*/
			@RequestHeader(value = "X-Requested-With", required = false) String requestedWith){
		logger.debug("regsignPage value: {}", regSignView);
		if (AjaxUtils.isAjaxRequest(requestedWith)) {
			logger.debug("registration Ajax: {}", regSignView.concat(" :: regsignForm"));
			return regSignView.concat(String.format(regsign_fragment, "register"));
		}
		
		return registerView;
	}
	
	@GetMapping(RequestPath.SIGNIN)
	public String getSignInPage(Model model, @RequestHeader(value = "X-Requested-With", required = false) String requestedWith){
		logger.debug("SignIn page controller");
		logger.debug("====== password: {} =======", encoder.encode("adminpass"));
		
		if (AjaxUtils.isAjaxRequest(requestedWith)) {
			logger.debug("registration Ajax: {}", regSignView.concat(" :: regsignForm"));

			return regSignView.concat(" :: regsignForm(active='signin')");
		}
		
		return signinView;
		
	}
	
	@PostMapping(RequestPath.AUTH)
	public String authenticate(){
		logger.debug("Auth request");

		return "";
	}
	
	@PostMapping(RequestPath.REGISTER)
	public String register(@Valid @ModelAttribute Account account, 
			Errors errors, RedirectAttributes ra,
			@RequestHeader(value = "X-Requested-With", required = false) String requestedWith){
		if(errors.hasErrors()) {
			if(AjaxUtils.isAjaxRequest(requestedWith)){
				logger.debug("Invalid registration attempt in modal: {}", errors);
				return regSignView.concat(String.format(regsign_fragment, "register"));
			}
			logger.debug("Invalid registration attempt: {}", errors);
			return registerView;
		}
		
		account = accountService.save(account);
		MessageHelper.addSuccessAttribute(ra, "registration success");
		return "redirect:/";
	}

}
