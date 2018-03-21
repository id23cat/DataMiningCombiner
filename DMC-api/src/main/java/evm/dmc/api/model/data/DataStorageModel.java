package evm.dmc.api.model.data;

import java.io.Serializable;
import java.net.URI;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import evm.dmc.core.api.back.data.DataSrcDstType;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import lombok.ToString;

@Data
@Entity
@Table(name="DATA_STORAGE")
@EqualsAndHashCode(exclude={"meta"})
@ToString(exclude={"meta"})
public class DataStorageModel implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String DEFAULT_DELIMITER = ",;\t|";
	public static final boolean DEFAULT_HASHEADER = true;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Setter(AccessLevel.NONE)
	private Long id;
	
	@Enumerated(EnumType.STRING)
	@NotNull
	private DataSrcDstType storageType;
	
	@OneToOne(mappedBy="storage", cascade = CascadeType.ALL, optional = false)
	@NotNull
	private MetaData meta;
	
//	@NotNull
//	private String URIstring;
	
	@NotNull
	private String uri;
	
	private String delimiter = DEFAULT_DELIMITER;
	
	private boolean hasHeader = DEFAULT_HASHEADER;
	
	public URI getUri() {
		return URI.create(uri);
	}
	
	public void setUri(URI uri) {
		this.uri = uri.toString();
	}
}
