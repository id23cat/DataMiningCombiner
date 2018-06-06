package evm.dmc.api.model;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;

import evm.dmc.api.model.FunctionSrcModel.FunctionSrcModelBuilder;
import evm.dmc.core.api.back.data.DataSrcDstType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
//@NoArgsConstructor
@Builder(builderMethodName="dstBuilder")
@AllArgsConstructor
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
	@Builder.Default
	private DataSrcDstType typeSrcDst = DataSrcDstType.LOCAL_FS;
	
//	public FunctionDstModel(){
//		super();
//	}
//	
	public FunctionDstModel(FunctionModel funmodel){
		super(funmodel.getId(), 
				funmodel.getName(), 
				funmodel.getFramework(), 
				funmodel.getType(), 
				funmodel.getProperties(), 
				funmodel.getDescription());
		if(funmodel instanceof FunctionDstModel) {
			this.destination = ((FunctionDstModel) funmodel).destination;
			this.typeSrcDst = ((FunctionDstModel) funmodel).typeSrcDst;
		}
	}

//	/**
//	 * @return the sourceDest
//	 */
//	public String getDestination() {
//		return destination==null ? super.getProperty(DST_PROPERTY_NAME) : destination;
//	}

//	/**
//	 * @param sourceDest the sourceDest to set
//	 */
//	public void setDestination(String dest) {
//		this.destination = dest;
//		super.setProperty(DST_PROPERTY_NAME, dest);
//	}

}
