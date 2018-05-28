package evm.dmc.api.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import evm.dmc.api.model.algorithm.PatternMethod;
import evm.dmc.api.model.converters.PropertiesMapToJson;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Singular;
import lombok.ToString;

@Data
@Builder
@EqualsAndHashCode(exclude={"property"})
@ToString(exclude={"dependentAlgorithms", "steps"})
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Function")
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
public class FunctionModel implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2557529452898863152L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Setter(AccessLevel.NONE) 
	private Long id;
	
	@NotBlank
	@Length(max = 100)
	private String name;
	
	@ManyToOne
	@JoinColumn(name = "framework_id")
	private FrameworkModel framework;
	
	@Enumerated(EnumType.STRING)
	@NotNull
	private FunctionType type;
	
	@Singular
	@Column( length = 100000 )
	@Convert(converter = PropertiesMapToJson.class)
	private Map<String, String> properties;
	
	private String description;
	
//	public FunctionModel(FunctionModel funmodel){
//		name = funmodel.getName();
//		framework = funmodel.getFramework();
//		type = funmodel.getType();
//		properties = funmodel.getProperties();
//		description = funmodel.getDescription();
//	}
	
	public String getProperty(String property) {
		return properties.get(property);
	}
	
	public void setProperty(String property, String value) {
		properties.put(property, value);
	}
	
	public final static Map<String,String> propertiesToMap(Properties props) {
		if(props == null)
			return null;
		Map<String,String> map = new HashMap<String,String>();
		for(final String name : props.stringPropertyNames()) {
			map.put(name, props.getProperty(name));
		}
		return map;
	}
	
	public final static Properties mapToProperties(Map<String, String> map) {
		if(map == null)
			return null;
		Properties properties = new Properties();
		properties.putAll(map);
		return properties;
	}
	
}
