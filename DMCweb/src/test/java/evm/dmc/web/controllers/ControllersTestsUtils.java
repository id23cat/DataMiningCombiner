package evm.dmc.web.controllers;

import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

public class ControllersTestsUtils {
	public static UriComponents setUriComponent(String uri, String component) {
		return UriComponentsBuilder.fromPath(uri).buildAndExpand(component);
	}
}
