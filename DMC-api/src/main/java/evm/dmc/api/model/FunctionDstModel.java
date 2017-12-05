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
public class FunctionDstModel extends FunctionModel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2340660026774788869L;

	public final static String DST_PROPERTY_NAME = "destination";
	
	private String destination = null;
	
	@Enumerated(EnumType.STRING)
	@NotNull
	private DataSrcDstType typeSrcDst = DataSrcDstType.LOCAL_FS;
	
	public FunctionDstModel(){
		super();
	}
	
	public FunctionDstModel(FunctionModel funmodel){
		super(funmodel);
	}

	/**
	 * @return the sourceDest
	 */
	public String getDestination() {
		return destination==null ? super.getProperties().getProperty(DST_PROPERTY_NAME) : destination;
	}

	/**
	 * @param sourceDest the sourceDest to set
	 */
	public void setDestination(String dest) {
		this.destination = dest;
		super.getProperties().setProperty(DST_PROPERTY_NAME, dest);
	}

}
