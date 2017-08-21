package evm.dmc.weka;

import static org.hamcrest.CoreMatchers.startsWith;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

import evm.dmc.core.api.back.CSVLoader;
import evm.dmc.weka.data.WekaData;

@ContextConfiguration(classes = DMCWekaConfig.class)
@TestPropertySource("classpath:wekatest.properties")
public class WekaTestBaseClass {
	protected WekaData data;

	@Autowired
	protected WekaFramework fw;

	@Autowired
	CSVLoader csv;
	@Value("${wekatest.datasource}")
	String sourceFile;

	@Before
	public void loadData() {
		assertNotNull(csv);
		assertThat(sourceFile, startsWith("Data"));
		csv.setSource(sourceFile);
		data = fw.castToWekaData(csv.get());
		// data.load(souceFile);
	}
}
