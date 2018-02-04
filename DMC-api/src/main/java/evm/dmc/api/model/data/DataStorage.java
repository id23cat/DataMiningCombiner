package evm.dmc.api.model.data;

import java.io.Serializable;
import java.net.URL;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import evm.dmc.core.api.back.data.DataSrcDstType;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

@Data
@Entity
@Table(name="DATA_STORAGE")
public class DataStorage implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Setter(AccessLevel.NONE)
	private Long id;
	
	@Enumerated(EnumType.STRING)
	private DataSrcDstType storageType;
	
	@OneToOne
	private MetaData meta;
	
	private URL url;
	
	private String delimiter = ",;\t|";
}
