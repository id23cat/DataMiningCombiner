/**
 * 
 */
package evm.dmc.core.arithmetic;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import evm.dmc.core.Framework;
import evm.dmc.core.Function;

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
	
	public Function addFunction(){
		return new AddFunction();
	}
	
	public Function sunFunction(){
		return new SubFunction();
	}
}
