package evm.dmc.core;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import evm.dmc.core.arithmetic.AbstractArithmeticFunction.ArithmeticContext;

//import evm.dmc.core.arithmetic.AbstractArithmeticFunction.ArithmeticContext;

@Configuration
//@ComponentScan(basePackages="evm.dmc.core.arithmetic")
//@ComponentScan(basePackageClasses={/*AbstractArithmeticFunction.class, ArithmeticContext.class*/})
@ComponentScan(basePackageClasses={})
public class DMCCoreConfig {

}
