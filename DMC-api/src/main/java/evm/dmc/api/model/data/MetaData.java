package evm.dmc.api.model.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.MapKeyColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Version;
import org.springframework.util.StringUtils;

import evm.dmc.api.model.ProjectModel;
import evm.dmc.api.model.converters.StringListConverter;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author id23cat
 *
 */
@Data
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
	private static final long serialVersionUID = 1L;
	public static final String DUPLICATION_POSTFIX = "-DUP";
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Setter(AccessLevel.NONE) 
	private Long id;
	
	private String name;
	
	private String description;
	
	/**
	 * what project this data belongs to
	 */
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "project_id")
	private ProjectModel project;

	@ElementCollection
	@MapKeyColumn(name = "MAP_KEY")
	@Column(name = "ATTRIBUTE")
	@CollectionTable(name = "DATA_ATTRIBUTES", joinColumns=@JoinColumn(name="METADATA_ID"))
	@Setter(AccessLevel.PROTECTED)
	@Getter
	private Map<String, DataAttribute> attributes = new HashMap<>();
	
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
