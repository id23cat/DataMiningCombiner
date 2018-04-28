package evm.dmc.api.model.datapreview;

import java.io.Serializable;
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

import evm.dmc.core.api.AttributeType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Singular;

@Entity
@Table(name="DATA_PREVIEW")
@NoArgsConstructor
@AllArgsConstructor
@Immutable
@Data
@Builder
public class DataPreview implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2667573835860510658L;

	public static final String DEFAULT_DELIMITER = ",;\t|";
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Setter(AccessLevel.NONE)
	private Long id;
	
	@Column(unique=true)
	@NotNull
	private Long metaDataId;
	
	@Lob
	private String header;
	
	@Column
	@Lob
	@ElementCollection
	@CollectionTable(name = "DATA_PREVIEW_LIST")
	@Singular("line") private List<String> data = new ArrayList<>();
	
	@Builder.Default private String delimiter = DEFAULT_DELIMITER; 
}
