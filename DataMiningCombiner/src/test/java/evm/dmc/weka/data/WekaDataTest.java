package evm.dmc.weka.data;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.File;

import org.junit.BeforeClass;
import org.junit.Test;

import evm.dmc.weka.data.WekaData;

public class WekaDataTest {
	WekaData data = new WekaData();
	String souceFile = "Data/telecom_churn.csv";

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Test
	public final void testLoad() throws Exception {
		assertNotNull(data);
		assertNull(data.getData());
		data.load(souceFile);
		assertNotNull(data.getData());
		System.out.println(data.getData());

	}

	@Test
	public final void testStore() throws Exception {
		// testLoad();
		data.load(souceFile);
		data.store("Data/test.csv");
		File file = new File("Data/test.csv");
		assertNotEquals(0, file.length());

	}

}
