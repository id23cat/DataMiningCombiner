package evm.dmc.api.model.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

import evm.dmc.core.api.AttributeType;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Entity
@Table(name="DATA_ATTRIBUTES")
//@Embeddable
@NoArgsConstructor(force = true)
public class DataAttribute {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Setter(AccessLevel.NONE) 
	private Long id;

	private String name;
	
	@Enumerated(EnumType.STRING)
	private AttributeType type = AttributeType.STRING;
	
	private Double multiplier = 1.0;
	private Boolean checked = true;
	
	@Transient
	@JsonIgnore
	private List<String> lines = new ArrayList<>();

	public DataAttribute(String name) {
		this.name = name;
	}
	
	public DataAttribute(String name, String value) {
		this.name = name;
		lines.add(value);
	}
	
	/**
	 * @return unmodifiable List
	 */
	public List<String> getLines() {
		return Collections.unmodifiableList(lines);
	}
	
	public synchronized void addLine(String line) {
		lines.add(line);
	}
}
