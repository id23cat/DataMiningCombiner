package evm.dmc.core.data;

import java.io.IOException;

import org.junit.BeforeClass;
import org.junit.Test;

public class TableDataTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Test
	public final void testLoad() throws IOException {
		TableData table = new TableData();
		table.load("Data/iris.data");
	}

	@Test
	public final void testStore() throws IOException {
		TableData table = new TableData();
		table.load("Data/iris.data");
		table.store("Data/result");
	}

}
