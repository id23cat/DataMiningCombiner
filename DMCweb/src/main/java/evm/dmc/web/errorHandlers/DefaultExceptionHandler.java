package evm.dmc.web.errorHandlers;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import evm.dmc.web.controllers.SignInController;
import evm.dmc.web.service.Views;
import lombok.extern.slf4j.Slf4j;

/**
 * General error handler for the application.
 */
@Slf4j
@ControllerAdvice
public class DefaultExceptionHandler {
//	private static final Logger logger = LoggerFactory.getLogger("ErrorLog");
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
//	
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
