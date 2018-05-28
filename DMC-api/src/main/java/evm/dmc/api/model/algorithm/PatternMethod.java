package evm.dmc.api.model.algorithm;

import java.io.Serializable;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
import javax.persistence.Table;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

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
@EqualsAndHashCode(exclude={"dependentAlgorithms", "steps"})
@ToString(exclude={"dependentAlgorithms", "steps"})
@Builder(builderMethodName="patternBuilder")
@NoArgsConstructor
@AllArgsConstructor
@Entity
//@EntityListeners(AlgorithmEntityListener.class)
@Table(name="Method"
//	,uniqueConstraints={@UniqueConstraint(columnNames = {"parentProject_id", "name"})}
)
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "entity_type", discriminatorType = DiscriminatorType.STRING)
@DiscriminatorValue("pattern")
public class PatternMethod implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 386821974686652567L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Setter(AccessLevel.NONE) 
	private Long id;
	
	@NotBlank
	@Length(max = 100)
	private String name;
	
	@Length(max=1000)
	private String description;
	
	@Column( length = 100000 )
	@Convert(converter = PropertiesMapToJson.class)
	@Singular 
	private Map<String,String> overridenProperties;
	
//	@ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
//	@JoinTable(name = "alg_subalg", joinColumns = @JoinColumn(name="alg_id", referencedColumnName = "id"),
//			inverseJoinColumns = @JoinColumn(name="subalg_id", referencedColumnName="id"))
//	@OrderColumn(name="INDEX")
//	@Singular
//	private List<PatternMethod> steps; //=new LinkedList<>();
	
	@ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, 
			fetch = FetchType.EAGER)
	@JoinTable(name = "METHOD_STEPS", joinColumns = @JoinColumn(name="METHOD_ID", referencedColumnName = "ID"),
			inverseJoinColumns = @JoinColumn(name="STEP_ID", referencedColumnName="ID"))
	@OrderColumn(name="STEP")
	@Builder.Default
	private List<PatternMethod> steps = new LinkedList<>();
	
	@OneToMany(mappedBy = "method", fetch = FetchType.LAZY, 
			cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST},
			 orphanRemoval = true)
//	@Singular
	private Set<Algorithm> dependentAlgorithms;// = new HashSet<>();
	
	@Column(columnDefinition="boolean default false")
	@Builder.Default
	private Boolean shared = false;
	
	public static class PatternMethodBuilder {
		public PatternMethodBuilder dependentAlgorithm(Algorithm algorithm) {
			this.name = algorithm.getName();
			this.description = algorithm.getDescription();
			if(this.dependentAlgorithms == null) {
				this.dependentAlgorithms = new HashSet<>();
			}
			this.dependentAlgorithms.add(algorithm);
			return this;
		}
	}
}
