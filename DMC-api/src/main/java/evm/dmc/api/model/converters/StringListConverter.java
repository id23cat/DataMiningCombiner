package evm.dmc.api.model.converters;

import java.io.IOException;
import java.util.List;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Converter
public class StringListConverter implements AttributeConverter<List<String>, String> {

	@Override
	public String convertToDatabaseColumn(List<String> attribute) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.writeValueAsString(attribute);
		} catch (JsonProcessingException e) {
			throw new AttributeToColumnConversionException("Converting of List has failed", e); 
		}
	}

	@Override
	public List<String> convertToEntityAttribute(String dbData) {
		ObjectMapper mapper = new ObjectMapper();
		TypeReference<List<String>> typeRef = new TypeReference<List<String>>(){};
		try {
			return mapper.readValue(dbData, typeRef);
		} catch (IOException e) {
			throw new ColumnToAttributeConversionException("Conversion of String has failed", e);
		}
	}


}
