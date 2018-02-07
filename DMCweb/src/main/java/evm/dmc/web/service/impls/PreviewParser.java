package evm.dmc.web.service.impls;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.util.StringUtils;

import evm.dmc.api.model.data.DataAttribute;
import evm.dmc.web.exceptions.DataStructureException;

public class PreviewParser {

	public PreviewParser() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * @param lines lines of data
	 * @param delimiters possible delimiter between attributes 
	 * @param hasHeader
	 * @return
	 */
	public static List<DataAttribute> getAttributes(List<String> lines, 
			String delimiters, 
			boolean hasHeader) throws DataStructureException {
//		String headerLine = null;
		List<DataAttribute> dataAtrrs = null;
		AtomicInteger counter = new AtomicInteger(0);
		Iterator<String> headerIterator; 
		if(hasHeader) {
			List<String> headerLine = streamLine(lines.remove(0), delimiters)
					.collect(Collectors.toList());
			
			headerIterator = headerLine.iterator();
		} else {
			headerIterator = Collections.emptyIterator();
		}
		
		try {
		// process first line of data with creating DataAttribute objects
		String firstLine = lines.remove(0);
		
		dataAtrrs = streamLine(firstLine, delimiters)
				.map(word -> createAttribute(word, headerIterator, counter))
				.collect(Collectors.toList());
		
		// process rest lines of data
		
		// Each iterator for one line
		List<Iterator<String>> listlist = lines.stream()
				.map(
						line -> streamLine(line, delimiters).collect(Collectors.toList()).iterator()
						)
				.collect(Collectors.toList());
//		listlist.stream().forEach(arg0);;
		dataAtrrs.stream().forEach((attr) -> 
			listlist.stream().forEach((iter) -> addToAttribute(iter.next(), attr))
		);
		} catch (NoSuchElementException ex) {
			throw new DataStructureException(
					new StringBuilder("Failed to parse data: ").append(lines).toString(),
					ex);
		}
		
		return dataAtrrs;
	}
	
	public static Stream<String> streamLine(String line, String delimiters) {
		return Arrays.stream(StringUtils.tokenizeToStringArray(line, delimiters));
	}
	
	private static DataAttribute createAttribute(String word, Iterator<String> header, AtomicInteger counter) {
		DataAttribute attribute;
		if(header.hasNext()) {
			attribute = new DataAttribute(header.next(), word);
		} else {
			attribute = new DataAttribute(counter.toString(), word);
		}
		counter.incrementAndGet();
		
		return attribute;
	}
	
	private static DataAttribute addToAttribute(String word, DataAttribute attr) {
		attr.getLines().add(word);
		return attr;
	}
	
//	public static final Function<String, List<DataAttribute>> createAttribute = (line) -> {
//		Arrays.stream(StringUtils.tokenizeToStringArray(line, delimiters));
//	}

}
