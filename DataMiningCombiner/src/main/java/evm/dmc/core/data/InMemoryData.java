package evm.dmc.core.data;

public abstract class InMemoryData<T> implements Data<T> {
	T data;

	public InMemoryData(T data) {
		super();
		this.data = data;
	}
	
	public InMemoryData(Data<T> data) {
		super();
		this.data = data.getData();
	}

	/**
	 * @return the data
	 */
	public T getData() {
		return data;
	}

	/**
	 * @param data the data to set
	 */
	public void setData(T data) {
		this.data = data;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((data == null) ? 0 : data.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof InMemoryData))
			return false;
		InMemoryData<?> other = (InMemoryData<?>) obj;
		if (data == null) {
			if (other.data != null)
				return false;
		} else if (!data.equals(other.data))
			return false;
		return true;
	}
	
	
//	List<T> data;
//
//	@Override
//	public List<T> getArgs() {
//		return data;
//	}
//
//	@Override
//	public File getFile() {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public Stream<T> getStream() {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public String getFileName() {
//		// TODO Auto-generated method stub
//		return null;
//	}
//	
//	/* (non-Javadoc)
//	 * @see evm.dmc.core.data.Data#setArgs(java.util.List)
//	 */
//	@Override
//	public void setArgs(List<T> args) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	/* (non-Javadoc)
//	 * @see evm.dmc.core.data.Data#setFile(java.io.File)
//	 */
//	@Override
//	public void setFile(File file) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	/* (non-Javadoc)
//	 * @see evm.dmc.core.data.Data#setStream(java.util.stream.Stream)
//	 */
//	@Override
//	public void setStream(Stream<T> stream) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	/* (non-Javadoc)
//	 * @see evm.dmc.core.data.Data#setFileName(java.lang.String)
//	 */
//	@Override
//	public void setFileName(String fname) {
//		// TODO Auto-generated method stub
//		
//	}

}
