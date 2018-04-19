package evm.dmc.api.model.converters;

import java.util.Map;
import java.util.Optional;

import javax.persistence.AttributeConverter;

import org.springframework.beans.factory.annotation.Autowired;

import evm.dmc.api.model.data.DataAttribute;

public class OptionalMapAttributesToJson implements AttributeConverter<Optional<Map<String, DataAttribute>>, String>{
	private final static MapAttributesToJson mapConverter = new MapAttributesToJson();

	@Override
	public String convertToDatabaseColumn(Optional<Map<String, DataAttribute>> attribute) {
		return mapConverter.convertToDatabaseColumn(attribute.orElse(null));
	}

	@Override
	public Optional<Map<String, DataAttribute>> convertToEntityAttribute(String dbData) {
		return Optional.ofNullable(mapConverter.convertToEntityAttribute(dbData));
	}

}
