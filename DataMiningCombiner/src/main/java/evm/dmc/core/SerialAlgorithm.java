package evm.dmc.core;

import java.util.LinkedList;
import java.util.List;

import evm.dmc.core.function.DMCFunction;

/**
 * The Class SerialAlgorithm.
 * Serial Implementation of interface, means serial 
 * execution of contained commands
 */
public class SerialAlgorithm implements Algorithm {
	protected List<DMCFunction> algChain = new LinkedList<>();

	/* (non-Javadoc)
	 * @see evm.dmc.core.Algorithm#addCommand(evm.dmc.core.Command)
	 */
	@Override
	public void addCommand(DMCFunction dMCFunction) {
		algChain.add(dMCFunction);
		
	}

	/* (non-Javadoc)
	 * @see evm.dmc.core.Algorithm#delCommand(evm.dmc.core.Command)
	 */
	@Override
	public void delCommand(DMCFunction dMCFunction) {
		algChain.remove(dMCFunction);
		
	}

	/* (non-Javadoc)
	 * @see evm.dmc.core.Algorithm#execute()
	 */
	@Override
	public void execute() {
		// TODO Auto-generated method stub
		
	}

}
