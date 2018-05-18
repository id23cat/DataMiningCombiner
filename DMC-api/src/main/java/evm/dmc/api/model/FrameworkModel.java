package evm.dmc.api.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import lombok.ToString;

@Data
//@Builder
@Entity
@Table(name = "Framework")
@EqualsAndHashCode(exclude={"functions"})
@ToString(exclude="functions")
public class FrameworkModel implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6061011758502384732L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Setter(AccessLevel.NONE) 
	private Long id;
	
	@NotBlank
	@Length(max = 100)
	private String name;
	
	@OneToMany(mappedBy = "framework", fetch = FetchType.LAZY,
			cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<FunctionModel> functions = new HashSet<>();
	
	@Enumerated(EnumType.STRING)
	@NotNull
	private FrameworkType type;
	
	@NotNull
	private boolean active = false;
	
	private Properties properties = new Properties();
	
	public FrameworkModel() {
		super();
	}
	
	public FrameworkModel(String name, FrameworkType type) {
		super();
		this.name = name;
		this.type = type;
	}
	
	public boolean isSame(FrameworkModel model) {
		return name.equals(model.getName()) 
				&& type.equals(model.getType()) 
				&& active == model.isActive();
	}
	
}
