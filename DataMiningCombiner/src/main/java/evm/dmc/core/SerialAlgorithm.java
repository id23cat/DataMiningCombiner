package evm.dmc.core;

import java.util.LinkedList;
import java.util.List;

/**
 * The Class SerialAlgorithm.
 * Serial Implementation of interface, means serial 
 * execution of contained commands
 */
public class SerialAlgorithm implements Algorithm {
	protected List<Function> algChain = new LinkedList<>();

	/* (non-Javadoc)
	 * @see evm.dmc.core.Algorithm#addCommand(evm.dmc.core.Command)
	 */
	@Override
	public void addCommand(Function function) {
		algChain.add(function);
		
	}

	/* (non-Javadoc)
	 * @see evm.dmc.core.Algorithm#delCommand(evm.dmc.core.Command)
	 */
	@Override
	public void delCommand(Function function) {
		algChain.remove(function);
		
	}

	/* (non-Javadoc)
	 * @see evm.dmc.core.Algorithm#execute()
	 */
	@Override
	public void execute() {
		// TODO Auto-generated method stub
		
	}

}
