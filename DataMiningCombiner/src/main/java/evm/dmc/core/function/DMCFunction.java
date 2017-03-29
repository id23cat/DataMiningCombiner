package evm.dmc.core.function;

import org.springframework.stereotype.Component;

import evm.dmc.core.FrameworkContext;
import evm.dmc.core.data.Data;

public interface DMCFunction<T> {
	void execute();

	default String getName() {return "Warning: No name specified";}

	Integer getArgsCount();
	
	void setName(String name);
	
	void setArgsCount(Integer count);
	
	void addArgument(Data<T> arg);
	
	Data<T> getResult();
	
//	Data<T> getResult;
	
	/**
	 * Returns object that should be used in function object as context of execution
	 * @return
	 */
	FrameworkContext getContext();
	
	/**
	 * Sets object that should be used in function object as context of execution
	 * @return
	 */
	void setContext(FrameworkContext context);
	
	/**
	 * @author id23cat
	 * Interface describes requirements to the context of execution for concrete function
	 */
//	@Component
//	public interface FrameworkContext<T> {
//		/**
//		 * Method is used for first initialization of function context or resetting settings to default
//		 */
//		void initContext();		
//		
//		/**
//		 * Execute concrete function in context
//		 * @param function
//		 */
//		void executeFunction(DMCFunction<T> function);
//
//	}

}
