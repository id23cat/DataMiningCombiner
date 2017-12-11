package evm.dmc.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import evm.dmc.service.RequestPath;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping(RequestPath.project+"/{projectName}"+RequestPath.algorithm+"/{algName}")
@SessionAttributes({"account", "currentProject"})
@Slf4j
public class AlgorithmController {

}