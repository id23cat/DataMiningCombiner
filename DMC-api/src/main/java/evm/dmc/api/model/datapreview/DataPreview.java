package evm.dmc.api.model.datapreview;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Immutable;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

@Entity
@Table(name="DATA_PREVIEW")
@Immutable
@Data
public class DataPreview {
	public static final String DEFAULT_DELIMITER = ",;\t|";
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Setter(AccessLevel.NONE)
	private Long id;
	
	@Column(unique=true)
	@NotNull
	private Long metaDataId;
	
	private String header;
	
	@Column
	@Lob
	@ElementCollection
	@CollectionTable(name = "PREVIEW_DATA")
	private List<String> data = new ArrayList<>();
	
	private String delimiter = DEFAULT_DELIMITER; 
}
