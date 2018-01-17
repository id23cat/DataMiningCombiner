package evm.dmc.web.controllers;

import lombok.Data;

@Data
public class CheckboxBean {
	private String[] names = null;
	
	public CheckboxBean() {}

	public CheckboxBean(String[] names) {
		this.names = names;
	}
	
}
