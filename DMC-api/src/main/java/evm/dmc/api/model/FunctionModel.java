package evm.dmc.api.model;

import java.io.Serializable;
import java.util.Properties;

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

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

@Data
@Entity
@Table(name = "function")
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
	
	private Properties properties = new Properties();
	
	public FunctionModel(){}
	
	public FunctionModel(FunctionModel funmodel){
		name = funmodel.getName();
		framework = funmodel.getFramework();
		type = funmodel.getType();
		properties = funmodel.getProperties();
	}
	
}
