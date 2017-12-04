package evm.dmc.api.model;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=true)
@Entity
public class FunctionSrcModel extends FunctionModel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2433829450685223249L;

	public final static String SRC_PROPERTY_NAME = "source";
	
	private String source = null;
	
	@Enumerated(EnumType.STRING)
	@NotNull
	private DataSrcDstType typeSrcDst = DataSrcDstType.LOCAL_FS;
		
	public FunctionSrcModel(FunctionModel funmodel){
		super(funmodel);
	}

	/**
	 * @return the sourceDest
	 */
	public String getSource() {
		
		return source==null ? super.getProperties().getProperty(SRC_PROPERTY_NAME) : source;
	}

	/**
	 * @param source the source to set
	 */
	public void setSource(String source) {
		this.source = source;
		super.getProperties().setProperty(SRC_PROPERTY_NAME, source);
		
	}

}
