package evm.dmc.api.model.converters;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.AttributeConverter;


import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PropertiesMapToJson implements AttributeConverter<Map<String, String>, String>{
	private static final ObjectMapper jacksonObjectMapper = new ObjectMapper();

	@Override
	public String convertToDatabaseColumn(Map<String, String> properties) {
		if(properties == null){
			log.debug("-== Map is equal to null");
			return null;
		}
		try {
			return jacksonObjectMapper.writeValueAsString(properties);
		} catch (JsonProcessingException e) {
			throw new AttributeToColumnConversionException(e);
		}
	}

	@Override
	public Map<String, String> convertToEntityAttribute(String dbData) {
		if(dbData == null)
			return new HashMap<>();
		try {
			return jacksonObjectMapper.readValue(dbData, new TypeReference<Map<String, String>>(){});
		} catch (JsonParseException e) {
			throw new AttributeToColumnConversionException(e);
		} catch (JsonMappingException e) {
			throw new AttributeToColumnConversionException(e);
		} catch (IOException e) {
			throw new AttributeToColumnConversionException(e);
		}
	}

}
