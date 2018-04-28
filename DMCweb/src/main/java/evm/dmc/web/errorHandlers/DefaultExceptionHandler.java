package evm.dmc.web.errorHandlers;

import javax.servlet.ServletException;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.HttpSessionRequiredException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.view.RedirectView;

import evm.dmc.web.controllers.project.ProjectController;
import evm.dmc.web.service.Views;
import lombok.extern.slf4j.Slf4j;

/**
 * General error handler for the application.
 */
@Slf4j
@ControllerAdvice
public class DefaultExceptionHandler {
	/**
	 * Handle exceptions thrown by handlers.
	 */
	
	@Autowired
	Views views;
	
	@org.springframework.web.bind.annotation.ExceptionHandler(DataIntegrityViolationException.class)
	public String processConstraintError(DataIntegrityViolationException ex) {
		log.warn("Attempt to add actualy existing user: {}", ExceptionUtils.getRootCause(ex.getCause()).getMessage());
		return views.getErrors().getUserExists();
	}
	
	@org.springframework.web.bind.annotation.ExceptionHandler(ServletRequestBindingException.class)
	//HttpSessionRequiredException
	//ServletRequestBindingException
	public RedirectView missedSessionAttribute(ServletRequestBindingException ex) throws ServletRequestBindingException {
		return redirectToUserHome(ex);
	}
	
	@org.springframework.web.bind.annotation.ExceptionHandler(HttpSessionRequiredException.class)
	public RedirectView missedSessionAttribute(HttpSessionRequiredException ex) throws HttpSessionRequiredException {
		return redirectToUserHome(ex);
		
		
	}
	
	private <T extends ServletException> RedirectView redirectToUserHome(T ex) throws T {
		if(ex.getMessage().matches("Expected session attribute \'currentProject\'") || 
				ex.getMessage().matches("Missing session attribute \'currentProject\' of type ProjectModel")){
			String redirect = ProjectController.BASE_URL;
			RedirectView rw = new RedirectView(redirect);
			return rw;
		} else
			throw ex;
		
	}
	
//	@org.springframework.web.bind.annotation.ExceptionHandler(value = Exception.class)
//	public ModelAndView exception(Exception exception, WebRequest request) {
//		ModelAndView modelAndView = new ModelAndView("error/general");
//        Throwable rootCause = Throwables.getRootCause(exception);
//        modelAndView.addObject("errorMessage", rootCause);
//        LOGGER.error(rootCause.toString(), exception);
//		return modelAndView;
//	}
	
//	@ExceptionHandler
//	public ModelAndView exception(Exception exc, WebRequest request) {
//		if(exc instanceOf )
//		ModelAndVeiw modelAndView = new ModelAndView("errors")
//	}

}
