package evm.dmc.web.controllers;

import lombok.Data;

@Data
public class CheckboxNamesBean {
	private String[] names = null;
	
	public CheckboxNamesBean() {}

	public CheckboxNamesBean(String[] names) {
		this.names = names;
	}
	
}
