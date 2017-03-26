package evm.dmc.core.data;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

public class IntegerDataTest {

	Integer value = 50;
	Data data = new IntegerData(50);
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Test
	public final void testGetData() {
		assertEquals(data.getData(), value);
	}

	@Test
	public final void testSetData() {
		Data tmpData = new IntegerData(133);
				
		data.setData(tmpData.getData());
		assertEquals(tmpData, data);
		
		Data tmpData2 = new IntegerData((IntegerData) data);
		tmpData2.setData(1);
		assertNotEquals(tmpData2, data);
	}

}
