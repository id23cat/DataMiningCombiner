package evm.dmc.web.service;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.StringJoiner;

import org.junit.Before;
import org.junit.Test;

import evm.dmc.api.model.data.DataAttribute;
import evm.dmc.api.model.data.MetaData;
import evm.dmc.core.api.AttributeType;
import evm.dmc.web.exceptions.DataStructureException;
import evm.dmc.web.service.impls.CsvPreviewParser;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CsvPreviewParserTest {
	String header;
	MetaData metaData;
	String delimiter = ";";
	
	int attrsCount = 4;
	int linesCount = 3;
	
	@Before
	public void init() {
		StringJoiner joiner = new StringJoiner(delimiter);
		List<String> dataLines = new ArrayList<>();
		// set header line
		joiner.add("head1").add("head2").add("head3").add("head4");
		header = joiner.toString();
		
		// set data lines
		for(int i=0; i<linesCount; i++) {
			joiner = new StringJoiner(delimiter);
			for(int j=0; j<attrsCount; j++) {
				joiner.add(String.valueOf((i+1) * (j+1)));
			}
			dataLines.add(joiner.toString());
		}
		metaData = new MetaData();
		metaData.setPreview(dataLines);
	}
	
	@Test
	public final void testCreatePreviewAttributes() {
		log.debug("Generated collection: {}", metaData.getPreview());
		CsvPreviewParser.createPreviewAttributes(metaData, header);
		<DataAttribute> attrs = metaData.getAttributes().values();
		assertThat(attrs.size(), equalTo(attrsCount));
		assertThat(attrs.get(0).getName(), equalTo("head1"));
		assertThat(attrs.get(3).getName(), equalTo("head4"));
		
		assertThat(attrs.get(0).getLines().get(0), equalTo("1"));
		assertThat(attrs.get(0).getLines().get(1), equalTo("2"));
		assertThat(attrs.get(0).getLines().get(2), equalTo("3"));
		assertThat(attrs.get(1).getLines().get(2), equalTo("6"));
	}

	@Test
	public final void testGetAttributesWithHeader() {
		
		
		log.debug("Generated collection: {}",dataLines);
		
		List<DataAttribute> attrs = CsvPreviewParser.getAttributes(withHeader, ",;\t|", true);
		
		assertThat(attrs.size(), equalTo(attrsCount));
		assertThat(attrs.get(0).getName(), equalTo("head1"));
		assertThat(attrs.get(3).getName(), equalTo("head4"));
		
		assertThat(attrs.get(0).getLines().get(0), equalTo("1"));
		assertThat(attrs.get(0).getLines().get(1), equalTo("2"));
		assertThat(attrs.get(0).getLines().get(2), equalTo("3"));
		assertThat(attrs.get(1).getLines().get(2), equalTo("6"));
		
	}
	
	@Test
	public final void testGetAttributesWithoutHeader() {
		
		log.debug("Generated collection: {}",dataLines);
		
		List<DataAttribute> attrs = CsvPreviewParser.getAttributes(dataLines, delimiter, false);
		
		assertThat(attrs.size(), equalTo(attrsCount));
		assertThat(attrs.get(0).getName(), equalTo("0"));
		assertThat(attrs.get(3).getName(), equalTo("3"));
		
		assertThat(attrs.get(0).getLines().get(0), equalTo("1"));
		assertThat(attrs.get(0).getLines().get(1), equalTo("2"));
		assertThat(attrs.get(0).getLines().get(2), equalTo("3"));
		assertThat(attrs.get(1).getLines().get(2), equalTo("6"));
	}
	
	@Test
	public final void testGetAttributesWithWrongDelimiter() {
		
		log.debug("Generated collection: {}",dataLines);
		
		List<DataAttribute> attrs = CsvPreviewParser.getAttributes(withHeader, ",", true);
		
		assertThat(attrs.size(), equalTo(1));
		
		StringJoiner joiner = new StringJoiner(delimiter);
		joiner.add("head1").add("head2").add("head3").add("head4");
		assertThat(attrs.get(0).getName(), equalTo(joiner.toString()));
		
		assertThat(attrs.get(0).getLines().get(0), equalTo("1;2;3;4"));
	}
	
	@Test(expected = DataStructureException.class)
	public final void testWrongStructure() {
		StringJoiner joiner = new StringJoiner(delimiter);
		joiner.add("22").add("33").add("44");
		
		dataLines.add(joiner.toString());
		
		CsvPreviewParser.getAttributes(dataLines, delimiter, false);
	}
	
	@Test
	public final void testRestorePreviewAttributes() {
		MetaData mdata = new MetaData();
		mdata.setHasHeader(true);
		mdata.setName("TestData");
		mdata.setPreview(withHeader);
		
		CsvPreviewParser.restorePreviewAttributes(mdata);
		
		assertNotNull(mdata.getAttributes());
		assertThat(mdata.getAttributes().size(), equalTo(attrsCount));
		
		DataAttribute head1 = mdata.getAttributes().get("head1");
		assertThat(head1.getLines().size(), equalTo(linesCount));
		assertThat(head1.getLines().get(2), equalTo("3"));
		
		DataAttribute head3 = mdata.getAttributes().get("head3");
		assertThat(head3.getLines().get(1), equalTo("6"));
		
	}
	
	@Test
	public final void testRestorePreviewAttributesWithExistsAttrs() {
		MetaData mdata = new MetaData();
		mdata.setHasHeader(true);
		mdata.setName("TestData");
		mdata.setPreview(withHeader);
		
		DataAttribute attr1 = new DataAttribute();
		attr1.setName("head1");
		attr1.setType(AttributeType.NOMINAL);
		attr1.setMultiplier(1.4);
		
		DataAttribute attr2 = new DataAttribute();
		attr2.setName("head2");
		attr2.setType(AttributeType.DATE);
		attr2.setMultiplier(-123.2);
		
		DataAttribute attr3 = new DataAttribute();
		attr3.setName("head3");
		attr3.setType(AttributeType.STRING);
		attr3.setMultiplier(0.7);
		
		DataAttribute attr4 = new DataAttribute();
		attr4.setName("head4");
		attr4.setType(AttributeType.NUMERIC);
		attr4.setMultiplier(42.);
		
		List<DataAttribute> list = Arrays.asList(attr1, attr2, attr3,attr4);
		mdata.setAttributesCollection(list);
		
		// call method to be tested
		CsvPreviewParser.restorePreviewAttributes(mdata);
		
		DataAttribute head1 = mdata.getAttributes().get("head1");
		assertThat(head1.getType(), equalTo(attr1.getType()));
		assertThat(head1.getMultiplier(), equalTo(attr1.getMultiplier()));
		assertThat(head1.getLines().size(), equalTo(linesCount));
		assertThat(head1.getLines().get(2), equalTo("3"));
		
		DataAttribute head2 = mdata.getAttributes().get("head2");
		assertThat(head2.getType(), equalTo(attr2.getType()));
		assertThat(head2.getMultiplier(), equalTo(attr2.getMultiplier()));
		assertThat(head2.getLines().get(1), equalTo("4"));
	}
}
