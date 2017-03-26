/**
 * 
 */
package evm.dmc.core.arithmetic;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import evm.dmc.core.Framework;
import evm.dmc.core.function.DMCFunction;

/**
 * @author id23cat
 *
 */
@Configuration
public class ArthmeticFWConfig {
	@Bean
	public Framework aritmeticFramework(){
		return new ArithmeticFramework();
	}
	
	@Bean
	public DMCFunction addFunction(){
		return new AddFunction();
	}
	
	@Bean
	public DMCFunction subFunction(){
		return new SubFunction();
	}
	
	@Bean
	public DMCFunction divFunction(){
		return new DivFunction();
	}
	
	@Bean
	public DMCFunction mulFunction(){
		return new MulFunction();
	}
}
