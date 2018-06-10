package evm.dmc.web.controllers;

import java.net.URI;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.util.UriComponentsBuilder;

import evm.dmc.api.model.account.Account;
import evm.dmc.rest.controllers.RestProjectController;
import evm.dmc.web.controllers.project.ProjectController;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/ajax")
@Slf4j
public class AjaxController {
	public final static String BASE_URL = "/ajax";
	
	public final static String MODEL_URL_projectsList = "projectsList";
	
	@GetMapping("/project")
	public String getProject(
			@SessionAttribute(ProjectController.SESSION_Account) Account account, 
			Model model) {
		Link link = RestProjectController.projectsListLink(new ResourceSupport(), account.getId())
				.getLink(RestProjectController.LINK_REL_projectsList);
		log.debug("Link {}", link);
		model.addAttribute(MODEL_URL_projectsList, link.getHref());
		return "/ajax/project";
	}
	
	private URI cookUri(String uri, String ...strings) {
		return UriComponentsBuilder.fromPath(uri).buildAndExpand(strings).toUri();
	}
	
//	private URI cookUri()

}
