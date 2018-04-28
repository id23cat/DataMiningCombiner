package evm.dmc.api.model.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

import evm.dmc.api.model.ProjectModel;
import evm.dmc.core.api.AttributeType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
//@NoArgsConstructor(force = true)
@EqualsAndHashCode(exclude={"lines"})
@ToString(exclude={"lines"})
public class DataAttribute  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3020317305095468013L;

	private String name;
	
	@Enumerated(EnumType.STRING)
	@Builder.Default private AttributeType type = AttributeType.STRING;
	
	@Builder.Default private Double multiplier = 1.0;
	@Builder.Default private Boolean checked = true;
	
	@Transient
	@JsonIgnore
	@Builder.Default private List<String> lines = new ArrayList<>();

//	public DataAttribute(String name) {
//		this.name = name;
//	}
//	
//	public DataAttribute(String name, String value) {
//		this.name = name;
//		lines.add(value);
//	}
	
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
