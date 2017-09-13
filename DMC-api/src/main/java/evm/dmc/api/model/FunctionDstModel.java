package evm.dmc.api.model;

public class FunctionDstModel extends FunctionModel {
	public final static String DST_PROPERTY_NAME = "destination";
	private String destination = null;
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

	/**
	 * @return the type
	 */
	public DataSrcDstType getTypeSrcDst() {
		return typeSrcDst;
	}

	/**
	 * @param type the type to set
	 */
	public void setTypeSrcDst(DataSrcDstType type) {
		this.typeSrcDst = type;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((destination == null) ? 0 : destination.hashCode());
		result = prime * result + ((typeSrcDst == null) ? 0 : typeSrcDst.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		FunctionDstModel other = (FunctionDstModel) obj;
		if (destination == null) {
			if (other.destination != null)
				return false;
		} else if (!destination.equals(other.destination))
			return false;
		if (typeSrcDst != other.typeSrcDst)
			return false;
		return true;
	}
	

}
