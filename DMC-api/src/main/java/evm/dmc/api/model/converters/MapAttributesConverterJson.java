package evm.dmc.api.model.converters;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.AttributeConverter;

import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import evm.dmc.api.model.data.DataAttribute;

public class MapAttributesConverterJson implements AttributeConverter<Map<String, DataAttribute>, String>{
	private static final ObjectMapper jacksonObjectMapper = new ObjectMapper();

	@Override
	public String convertToDatabaseColumn(Map<String, DataAttribute> attributes) {
		if(attributes == null)
			return null;
		try {
			
			return jacksonObjectMapper.writeValueAsString(attributes);
		} catch (JsonProcessingException e) {
			throw new AttributeToColumnConversionException(e);
		}
	}

	@Override
	public Map<String, DataAttribute> convertToEntityAttribute(String dbData) {
		if(dbData == null)
			return new HashMap<>();
		try {
			return jacksonObjectMapper.readValue(dbData, new TypeReference<Map<String, DataAttribute>>(){});
		} catch (JsonParseException e) {
			throw new AttributeToColumnConversionException(e);
		} catch (JsonMappingException e) {
			throw new AttributeToColumnConversionException(e);
		} catch (IOException e) {
			throw new AttributeToColumnConversionException(e);
		}
	}

}
