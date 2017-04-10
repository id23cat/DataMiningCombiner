package evm.dmc.python;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import evm.dmc.core.data.Data;

@Service("Python_Data")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class JepVariable implements Data<String> {
	String variable;

	public JepVariable() {

	}

	public JepVariable(String variable) {
		super();
		this.variable = variable;
	}

	@Override
	public String getData() {
		return variable;
	}

	@Override
	public void setData(String data) {
		this.variable = data;

	}

}
