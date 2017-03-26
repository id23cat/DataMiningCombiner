package evm.dmc.core.data;

import java.io.File;
import java.util.List;
import java.util.stream.Stream;

public abstract class InMemoryData<T> implements Data<T> {
	
	List<T> data;

	@Override
	public List<T> getArgs() {
		return data;
	}

	@Override
	public File getFile() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Stream<T> getStream() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getFileName() {
		// TODO Auto-generated method stub
		return null;
	}
	
	/* (non-Javadoc)
	 * @see evm.dmc.core.data.Data#setArgs(java.util.List)
	 */
	@Override
	public void setArgs(List<T> args) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see evm.dmc.core.data.Data#setFile(java.io.File)
	 */
	@Override
	public void setFile(File file) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see evm.dmc.core.data.Data#setStream(java.util.stream.Stream)
	 */
	@Override
	public void setStream(Stream<T> stream) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see evm.dmc.core.data.Data#setFileName(java.lang.String)
	 */
	@Override
	public void setFileName(String fname) {
		// TODO Auto-generated method stub
		
	}

}
