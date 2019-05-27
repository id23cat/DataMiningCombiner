package evm.dmc.core.api;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;
import java.util.Set;

@Setter
@Getter
public class Statistics {
    private AttributeType type;
    private String name;
    private Map<Object, Integer> mapValuesCount;
    private double max = 0;
    private double min = 0;
    private int count = 0;
    private int bins = 30;
    private Set<String> elements;

    public Statistics(AttributeType type, String name) {
        this.type = type;
        this.name = name;
    }
}
