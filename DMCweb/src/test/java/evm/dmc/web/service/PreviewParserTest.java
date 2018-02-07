package evm.dmc.web.service;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

import org.junit.Before;
import org.junit.Test;

import evm.dmc.api.model.data.DataAttribute;
import evm.dmc.web.exceptions.DataStructureException;
import evm.dmc.web.service.impls.PreviewParser;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PreviewParserTest {
	String header;
	List<String> withHeader;
	List<String> dataLines;
	String delimiter = ";";
	
	int attrsCount = 4;
	int linesCount = 3;
	
	@Before
	public void init() {
		StringJoiner joiner = new StringJoiner(delimiter);
		dataLines = new ArrayList<>();
		// set header line
		joiner.add("head1").add("head2").add("head3").add("head4");
		header = joiner.toString();
		
		withHeader = new ArrayList<>();
		withHeader.add(header);
		
		// set data lines
		for(int i=0; i<linesCount; i++) {
			joiner = new StringJoiner(delimiter);
			for(int j=0; j<attrsCount; j++) {
				joiner.add(String.valueOf((i+1) * (j+1)));
			}
			dataLines.add(joiner.toString());
		}
		withHeader.addAll(dataLines);
	}

	@Test
	public final void testGetAttributesWithHeader() {
		
		
		log.debug("Generated collection: {}",dataLines);
		
		List<DataAttribute> attrs = PreviewParser.getAttributes(withHeader, ",;\t|", true);
		
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
		
		List<DataAttribute> attrs = PreviewParser.getAttributes(dataLines, delimiter, false);
		
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
		
		List<DataAttribute> attrs = PreviewParser.getAttributes(withHeader, ",", true);
		
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
		
		PreviewParser.getAttributes(dataLines, delimiter, false);
	}
}
