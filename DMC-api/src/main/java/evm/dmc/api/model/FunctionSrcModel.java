package evm.dmc.api.model;

public class FunctionSrcModel extends FunctionModel {
	public final static String SRC_PROPERTY_NAME = "source";
	private String source = null;
	private DataSrcDstType typeSrcDst = DataSrcDstType.LOCAL_FS;
	
	public FunctionSrcModel(){
		super();
	}
	
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
		result = prime * result + ((source == null) ? 0 : source.hashCode());
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
		FunctionSrcModel other = (FunctionSrcModel) obj;
		if (source == null) {
			if (other.source != null)
				return false;
		} else if (!source.equals(other.source))
			return false;
		if (typeSrcDst != other.typeSrcDst)
			return false;
		return true;
	}
	

}
