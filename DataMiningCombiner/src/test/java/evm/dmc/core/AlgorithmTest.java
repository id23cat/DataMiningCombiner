/**
 * 
 */
package evm.dmc.core;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.IOException;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

import evm.dmc.core.api.Algorithm;
import evm.dmc.core.api.DMCFunction;

/**
 * @author id23cat
 *
 */
@RunWith(SpringRunner.class)
public class AlgorithmTest {
	
	@Mock private Algorithm mockedAlg;
	@Mock private DMCFunction<?> mockedCmd;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		
	}

	/**
	 * Test method for {@link evm.dmc.core.api.Algorithm#addCommand(evm.dmc.core.api.DMCFunction)}.
	 */
	@Test
	public final void testAddCommand() {		
		mockedAlg.addCommand(mockedCmd);
		
		verify(mockedAlg, times(1)).addCommand(mockedCmd);
	}

	/**
	 * Test method for {@link evm.dmc.core.api.Algorithm#delCommand(evm.dmc.core.api.DMCFunction)}.
	 */
	@Test
	public final void testDelCommand() {		
		mockedAlg.addCommand(mockedCmd);
		mockedAlg.delCommand(mockedCmd);
		
		verify(mockedAlg, times(1)).addCommand(mockedCmd);
		verify(mockedAlg, times(1)).delCommand(mockedCmd);
	}

	/**
	 * Test method for {@link evm.dmc.core.api.Algorithm#execute()}.
	 * @throws IOException 
	 */
	@Test
	public final void testExecute() throws IOException {
		mockedAlg.addCommand(mockedCmd);
		
		mockedAlg.execute();
		
	}

}
