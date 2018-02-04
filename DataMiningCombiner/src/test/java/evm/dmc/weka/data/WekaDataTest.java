package evm.dmc.weka.data;

import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.CoreMatchers.both;
import static org.hamcrest.CoreMatchers.either;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.closeTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.hamcrest.collection.IsMapContaining;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.Random;

import evm.dmc.DataMiningCombinerApplicationTests;
import evm.dmc.core.api.AttributeType;
import evm.dmc.core.api.Data;
import evm.dmc.core.api.Statistics;
import evm.dmc.core.api.back.CSVLoader;
import evm.dmc.core.api.back.data.DataModel;
import evm.dmc.core.api.exceptions.DataOperationException;
import evm.dmc.weka.DMCWekaConfig;
import evm.dmc.weka.WekaFramework;
import weka.core.Instances;

@RunWith(SpringRunner.class)
@TestPropertySource("classpath:wekatest.properties")
@ContextConfiguration(classes = {DataMiningCombinerApplicationTests.class})
@ComponentScan( basePackages="evm.dmc.core, evm.dmc.weka")
@DataJpaTest  // TODO: remove where unneeded
public class WekaDataTest {
	// @Value("#{wekaFramework.getData(Weka_Instances.getClass())}")
	WekaData data;

	@Autowired
	WekaFramework fw;

	@Autowired
	CSVLoader csv;
	@Value("${wekatest.datasource}")
	String sourceFile;

	@Value("${wekatest.datasourceDate}")
	String sourceFileDate;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void loadData() {

		loadData(sourceFile, true);
	}

	private void loadData(String source, boolean hasHeader) {
		csv.hasHeader(hasHeader);
		assertNotNull(csv);
		assertThat(source, startsWith("Data"));
		csv.setSource(source);
		data = fw.castToWekaData(csv.get());
	}

	@Test
	public final void testLoad() throws Exception {
		assertNotNull(data);
		assertNotNull(data.getData());
		Instances inst = data.getData();
		int length = inst.numInstances();
		assertEquals(3333, length);

	}

	@Test
	public final void testInstances() throws Exception {
		// System.out.println(data.getData());
		Instances inst = data.getData();
		printInstancesInfo(inst);
	}

	@Test
	public final void testCopyObject() throws Exception {
		WekaData data2 = data.copyObject();
		assertThat(data2, is(not(sameInstance(data))));
		assertThat(data2.getData(), is(not(sameInstance(data.getData()))));

	}

	@Test
	public final void testGetAttribute() throws Exception {
		int attrNum = 0;
		Data<Instances> attr = data.getAttribute(attrNum);
		assertNotNull(attr);
		Instances inst = attr.getData();
		assertEquals(1, inst.numAttributes());
		assertEquals(data.getData().size(), inst.size());
		int index = new Random().nextInt(inst.size());
		assertEquals(data.getData().get(index).attribute(attrNum), inst.get(index).attribute(0));

	}

	@Test
	public final void testGetAttributeByName() throws Exception {
		WekaData attr = data.getAttribute("Voice mail plan");
		assertNotNull(attr);
		assertNotNull(attr.getData());
		assertEquals("Yes", attr.getValueAsString(1, 0));
	}

	@Test
	public final void testGetAttributes() throws Exception {
		int[] attrNums = { 1, 3, 5 };
		Data<Instances> attr = data.getAttributes(attrNums);
		assertNotNull(attr);
		Instances inst = attr.getData();
		assertEquals(attrNums.length, inst.numAttributes());
		assertEquals(data.getData().size(), inst.size());
		int otherIdx = 0;
		Random rnd = new Random();
		for (int idx : attrNums) {
			int randIndex = rnd.nextInt(inst.size());
			assertEquals(data.getData().get(randIndex).attribute(idx), inst.get(randIndex).attribute(otherIdx++));
		}

	}

	@Test
	public final void testAny() throws Exception {
		Instances inst = data.getData();
		WekaData data2 = data.copyObject();
		Instances instOrig = data2.getData();

		inst.deleteAttributeAt(0);
		inst.deleteAttributeAt(0);
		inst.deleteAttributeAt(0);
		inst.deleteAttributeAt(0);
		inst.deleteAttributeAt(0);
		inst.deleteAttributeAt(0);
		inst.deleteAttributeAt(0);
		System.out.println(inst);
		printInstancesInfo(inst);

	}

	@Test
	public final void testGetSubset() {
		WekaData subDat = data.getSubset(0, 1);
		assertEquals(2, subDat.getData().size());
		assertTrue(subDat.getData().get(0).attribute(1).isNumeric());
		assertThat(subDat.getData().get(0).value(1), closeTo(128, 0.01));

		assertTrue(subDat.getData().get(0).attribute(4).isNominal());
		int nomIndex = (int) subDat.getData().get(0).value(4);
		assertThat(subDat.getData().get(0).attribute(4).value(nomIndex), equalTo("Yes"));

		assertTrue(subDat.getData().get(1).attribute(1).isNumeric());
		assertThat(subDat.getData().get(1).value(1), closeTo(107, 0.01));

		assertTrue(subDat.getData().get(1).attribute(4).isNominal());
		int nomIndex2 = (int) subDat.getData().get(1).value(4);
		assertThat(subDat.getData().get(1).attribute(4).value(nomIndex2), equalTo("Yes"));

	}

	@Test
	public final void testGetInstance() {
		WekaData subDat = data.getInstance(2);
		assertEquals(1, subDat.getData().size());
		assertTrue(subDat.getData().get(0).attribute(1).isNumeric());
		assertThat(subDat.getData().get(0).value(1), closeTo(137, 0.01));

		assertTrue(subDat.getData().get(0).attribute(4).isNominal());
		int nomIndex = (int) subDat.getData().get(0).value(4);
		assertThat(subDat.getData().get(0).attribute(4).value(nomIndex), equalTo("No"));

	}

	@Test(expected = evm.dmc.core.api.exceptions.IndexOutOfRange.class)
	public final void testGetInstanceWrongIndex() {
		WekaData subDat = data.getInstance(3333);
	}

	@Test
	public final void testClone() {
		WekaData cln = data.clone();
		assertNotEquals(data, cln);
		assertEquals(data.getValue(1, 2), cln.getValue(1, 2), 0.0001);
		assertEquals(data.getValueAsString(1, 3), cln.getValueAsString(1, 3));
	}

	@Test
	public final void testGetValue() {
		assertEquals(107, data.getValue(1, 1), 0.0001);
		assertEquals(0.0, data.getValue(1, 3), 0.0001);
	}

	@Test
	public final void testGetValueAsString() {
		assertEquals("No", data.getValueAsString(1, 3));
		assertEquals("OH", data.getValueAsString(3, 0));
		assertEquals("415.0", data.getValueAsString(2, 2));
	}

	@Test(expected = evm.dmc.core.api.exceptions.IndexOutOfRange.class)
	public final void testUsingWrongIndex() {
		data.isDate(3333);
	}

	@Test
	public final void testTypes() {
		assertTrue(data.isNumeric(1));
		assertTrue(data.isNumeric(2));
		assertTrue(data.isNumeric(5));

		assertTrue(data.isNominal(0));
		assertTrue(data.isNominal(3));
	}

	@Test
	public final void testGetAttributeStatisticsForNominalAttr() {
		Statistics stat0 = data.getAttributeStatistics(0);
		data.toNominal(1);
		Statistics stat1 = data.getAttributeStatistics(1);

		assertEquals(AttributeType.NOMINAL, stat0.getType());
		assertEquals("State", stat0.getName());
		assertThat(stat0.getMax(), closeTo(0, 0));
		assertThat(stat0.getMin(), closeTo(0, 0));
		assertEquals(3333, stat0.getCount());
		assertThat(stat0.getElements(), hasItems("KS", "ID", "TX", "NY"));

		assertEquals(AttributeType.NOMINAL, stat1.getType());
		assertEquals("Account length", stat1.getName());
		assertThat(stat1.getMax(), closeTo(0, 0));
		assertThat(stat1.getMin(), closeTo(0, 0));
		assertEquals(3333, stat1.getCount());
		assertThat(stat1.getElements(), hasItems("117", "130", "75", "36"));

	}

	@Test
	public final void testToNominal() {
		loadData(sourceFileDate, false);
		int column;

		column = 0;
		assertTrue(data.isNominal(column));
		data.toNominal(column);
		assertTrue(data.isNominal(column));
		assertEquals("TT", data.getValueAsString(0, column));
		for (int i = 0; i < data.getElementsCount(); i++)
			System.out.println(data.getValue(i, column) + " -> " + data.getValueAsString(i, column));
		data.printDebug();

		column = 1;
		assertTrue(data.isNumeric(column));
		data.toNominal(column);
		assertTrue(data.isNominal(column));
		assertEquals("5.1", data.getValueAsString(0, column));
		for (int i = 0; i < data.getElementsCount(); i++)
			System.out.println(data.getValue(i, column) + " -> " + data.getValueAsString(i, column));
		data.printDebug();

		column = 2;
		assertTrue(data.isNominal(column));
		data.toNominal(column);
		assertTrue(data.isNominal(column));
		assertEquals("23.05.2017", data.getValueAsString(0, column));
		for (int i = 0; i < data.getElementsCount(); i++)
			System.out.println(data.getValue(i, column) + " -> " + data.getValueAsString(i, column));
		data.printDebug();

		column = 3;
		assertTrue(data.isNumeric(column));
		data.toNominal(column);
		assertTrue(data.isNominal(column));
		assertEquals("1", data.getValueAsString(0, column));
		for (int i = 0; i < data.getElementsCount(); i++)
			System.out.println(data.getValue(i, column) + " -> " + data.getValueAsString(i, column));
		data.printDebug();
	}

	@Test
	public final void testGetDataModel() {
		DataModel model = data.getDataModel();
		assertNotNull(model);
		assertEquals(20, model.getTitleTypeMap().size());
		assertThat(model.getTitleTypeMap(), IsMapContaining.hasEntry("State", "NOMINAL"));
		assertThat(model.getTitleTypeMap(), IsMapContaining.hasEntry("Total day charge", "NUMERIC"));
		assertThat(model.getTitleTypeMap(), IsMapContaining.hasEntry("Churn", "NOMINAL"));
		assertThat(model.getTitleTypeMap(), not(IsMapContaining.hasEntry("", "")));
		assertThat(model.getPreview().length, anyOf(equalTo(10), equalTo(DataModel.DEFAULT_ROWS_COUNT.intValue())));

		model = data.getDataModel(3);
		assertThat(model.getPreview().length, equalTo(3));
		System.out.println(Arrays.deepToString(model.getPreview()));

	}

	@Rule
	public ExpectedException thrown = ExpectedException.none();
	
	@Test(expected = DataOperationException.class)
	public final void testEmptyDataException() {
		WekaData emptData = (WekaData) fw.getData(WekaData.class);
		emptData.getDataModel();
		thrown.expectMessage("Empty object: Data does not exists");
		
	}

	void printInstancesInfo(Instances inst) {
		for (int i = 0; i < inst.numAttributes(); i++) {
			System.out.println("[" + i + "]");
			System.out.println("\t" + inst.attribute(i));
			System.out.println("\t" + inst.attribute(i).enumerateValues());
			System.out.println("\t" + inst.attribute(i).weight());
			System.out.println("\t" + inst.attributeStats(i));

		}
	}
}
