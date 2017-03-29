package evm.dmc.core.arithmetic;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import evm.dmc.core.arithmetic.AbstractArithmeticFunction.ArithmeticContext;

//import evm.dmc.core.arithmetic.AbstractArithmeticFunction.ArithmeticContext;

@Configuration
//@ComponentScan(basePackageClasses={AbstractArithmeticFunction.class, AbstractArithmeticFunction.ArithmeticContext.class})
//@ComponentScan(basePackages="evm.dmc.core.arithmetic")
@ComponentScan(basePackageClasses={AbstractArithmeticFunction.class, ArithmeticContext.class})
public class ArithmeticPackageConfig {

}
