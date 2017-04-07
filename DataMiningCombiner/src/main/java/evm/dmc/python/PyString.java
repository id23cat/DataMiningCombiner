package evm.dmc.python;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service("Python_String")
@Scope("protorype")
public class PyString extends JepVariable {

	public PyString(String variable) {
		super(variable);

	}

	@Override
	public String getData() {
		StringBuilder var = new StringBuilder("\"");
		var.append(variable);
		var.append("\"");
		return var.toString();
	}

}
