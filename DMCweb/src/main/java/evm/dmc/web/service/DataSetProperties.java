package evm.dmc.web.service;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor(access = AccessLevel.PUBLIC)
//@NoArgsConstructor
public class DataSetProperties {
	private String name = "";
	private String Description = "";
	private boolean hasHeader;
	
}
