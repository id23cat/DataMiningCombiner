package evm.dmc.python.data;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service("Python_Variable")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class PyVar extends JepVariable {

}
