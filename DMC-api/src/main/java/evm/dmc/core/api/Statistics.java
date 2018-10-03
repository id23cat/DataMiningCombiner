package evm.dmc.core.api;

import lombok.*;
import lombok.Data;

import java.util.Map;
import java.util.Set;

/**
 *  Represents statistics of some data attribute.
 */
@EqualsAndHashCode
@ToString
public class Statistics {
    @Getter
	AttributeType type;
	String attrName;

	@Getter
    @Setter
	Map<Object, Integer> mapValuesCount;

	@Getter
    @Setter
	double max = 0;

    @Getter
    @Setter
	double min = 0;

    @Getter
    @Setter
	int count = 0;

    @Getter
    @Setter
	int bins = 30;

    @Getter
    @Setter
    // mich: why string? Should use attribute type
	Set<String> elements;

	public Statistics(AttributeType type, String name) {
		this.type = type;
		this.attrName = name;
	}
}
