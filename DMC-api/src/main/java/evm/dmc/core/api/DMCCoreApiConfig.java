package evm.dmc.core.api;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

//import evm.dmc.core.arithmetic.AbstractArithmeticFunction.ArithmeticContext;

@Configuration
// @ComponentScan(basePackages="evm.dmc.core.arithmetic")
// @ComponentScan(basePackageClasses={/*AbstractArithmeticFunction.class,
// ArithmeticContext.class*/})
@ComponentScan( basePackages="evm.dmc.core.api")
//@Import({evm.dmc.weka.DMCWekaConfig.class})
public class DMCCoreApiConfig {

}
