package evm.dmc.api.model.data;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import evm.dmc.api.model.ProjectModel;
import evm.dmc.api.model.converters.MapAttributesToJson;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Singular;

/**
 * @author id23cat
 *
 */
@Data
@Builder(toBuilder=true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="METADATA"
	,uniqueConstraints={@UniqueConstraint(columnNames = {"project_id", "name"})}
)
//@EqualsAndHashCode(exclude={"storage"})
//@ToString(exclude={"storage"})
public class MetaData implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6801760958025220213L;

//	@Deprecated
	public static final String DUPLICATION_POSTFIX = "-DUP";
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Setter(AccessLevel.NONE) 
	private Long id;
	
	@NotNull
	private String name;
	
	private String description;
	
	/**
	 * what project this data belongs to
	 */
	@ManyToOne
	@JoinColumn(name = "project_id")
	private ProjectModel project;

	@Column( length = 100000 )
	@Convert(converter = MapAttributesToJson.class)
//	@Setter(AccessLevel.PROTECTED)
//	@Getter
//	@Singular
	@Builder.Default
	private Map<String, DataAttribute> attributes = Collections.synchronizedMap(new HashMap<>());
	
//	@Column
////	@Column(columnDefinition = "TEXT")
//	@Lob
////	@Convert(converter = StringListConverter.class)
//	@ElementCollection
//	@CollectionTable(name = "PREVIEW_DATA", joinColumns=@JoinColumn(name="METADATA_ID"))
//	private List<String> preview = new ArrayList<>();
	
	private Long previewId;
	
	
	// delimiter for preview
	// also can be used for parsing data files instead of storage.delimiter
//	private String previewDelimiter = DEFAULT_DELIMITER;
	
//	

	@OneToOne(cascade = CascadeType.ALL, optional = false)
	@JoinColumn(name = "storage_id")
	private DataStorageModel storage;
	
//	@Version
//    private Long version;
	
	public void setStorage(DataStorageModel storage) {
		storage.setMeta(this);
		this.storage = storage;
	}
	
//	/**
//	 * @return unmodifiable List
//	 */
//	public List<String> getPreview() {
//		return Collections.unmodifiableList(preview);
//	}
	
	
	
	
}
