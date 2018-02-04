package evm.dmc.api.model.data;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import evm.dmc.core.api.AttributeType;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

@Data
//@Entity
//@Table(name="DATA_ATTRIBUTE")
@Embeddable
public class DataAttribute {

	private String name;
	
	@Enumerated(EnumType.STRING)
	private AttributeType type;
	
	private Float multiplier = 1.0F;
	private Boolean checked = true;
	
	@Transient
	private String[] preview;


}
